package com.phaedrusxy.basshieldfix.test;

import buildaspell.config.ModConfig;
import buildaspell.registry.ModTags;
import buildaspell.spell.Spell;
import buildaspell.spell.SpellComponent;
import buildaspell.spell.SpellEffect;
import buildaspell.spell.SpellModifier;
import buildaspell.spell.execution.EffectExecutors;
import com.mojang.authlib.GameProfile;
import com.phaedrusxy.basshieldfix.BuildASpellShieldFix;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Test-only casts into a real server world. The test fixture tag is not in the release JAR. */
@EventBusSubscriber(modid = BuildASpellShieldFix.MOD_ID)
public final class WallRuntimeTest {
    private static MinecraftServer server;
    private static ServerLevel level;
    private static FakePlayer caster;
    private static int ticks, checks;
    private static final BlockPos CENTER = new BlockPos(0, 180, 0);
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        System.out.println("WALL_CHECK " + (++checks) + ": " + message);
    }
    private static void tune(String key, int value) throws Exception {
        Field field = ModConfig.class.getDeclaredField("EFFECTS");
        field.setAccessible(true);
        ModConfig.Effects effects = (ModConfig.Effects) field.get(null);
        effects.tuneI.get("conjure." + key).set(value);
        check(ModConfig.effectInt(SpellEffect.CONJURE, key, -1) == value, "Runtime config " + key + "=" + value);
    }
    private static Spell spell(boolean wall, boolean floor, boolean fill, int area) {
        List<SpellComponent> components = new ArrayList<>();
        components.add(new SpellComponent.Effect(SpellEffect.CONJURE));
        if (wall) components.add(new SpellComponent.Modifier(SpellModifier.WALL));
        if (floor) components.add(new SpellComponent.Modifier(SpellModifier.FLOOR));
        if (fill) components.add(new SpellComponent.Modifier(SpellModifier.FILL));
        for (int i = 0; i < area; i++) components.add(new SpellComponent.Modifier(SpellModifier.INCREASED_AREA));
        return new Spell(null, components);
    }
    private static void cast(BlockPos center, Spell spell) {
        EffectExecutors.executeConjure(caster, level, center.getCenter(), spell, 1.0f);
    }
    private static Set<BlockPos> scan(BlockPos center, int radius, int depth, Direction.Axis axis, Block material) {
        Set<BlockPos> found = new HashSet<>();
        for (int a = -radius; a <= radius; a++) for (int y = -radius; y <= radius; y++) {
            for (int d = -depth; d <= depth; d++) {
                BlockPos pos = axis == Direction.Axis.X ? center.offset(d, y, a) : center.offset(a, y, d);
                if (!level.isOutsideBuildHeight(pos) && level.isLoaded(pos) && level.getBlockState(pos).is(material)) found.add(pos);
            }
        }
        return found;
    }
    private static Set<BlockPos> expected(BlockPos center, int radius, int depth, Direction.Axis axis) {
        Set<BlockPos> set = new HashSet<>();
        for (int a = -radius; a <= radius; a++) for (int y = -radius; y <= radius; y++) {
            for (int d = -depth; d <= depth; d++) set.add(axis == Direction.Axis.X ? center.offset(d,y,a) : center.offset(a,y,d));
        }
        return set;
    }
    private static void clear(Set<BlockPos> positions) {
        for (BlockPos pos : positions) level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
    }
    private static boolean connected(BlockPos pos, String side) {
        var state = level.getBlockState(pos);
        var property = state.getBlock().getStateDefinition().getProperty(side);
        return property != null && !state.getValue(property).toString().equalsIgnoreCase("none");
    }
    private static Set<BlockPos> checkShape(Spell spell, int radius, int depth, Block material, String description) {
        Direction.Axis axis = caster.getDirection().getAxis();
        cast(CENTER, spell);
        Set<BlockPos> found = scan(CENTER, radius + 2, depth + 2, axis, material);
        check(found.equals(expected(CENTER, radius, depth, axis)), description + " count=" + found.size());
        clear(found);
        return found;
    }
    @SubscribeEvent
    public static void start(ServerStartedEvent event) {
        server = event.getServer();
        try {
            level = server.overworld();
            server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "forceload add -48 -48 48 48");
            for (int x = -3; x <= 3; x++) for (int z = -3; z <= 3; z++) level.getChunkAt(new BlockPos(x * 16, 180, z * 16));
            caster = FakePlayerFactory.get(level, new GameProfile(UUID.fromString("2f55ff52-c8ec-4708-a1d0-19bd8931a112"), "WallTest"));
            caster.setPos(0.5, 100, 0.5);
            caster.setNoGravity(true);
            level.addNewPlayer(caster);
        } catch (Throwable error) { fail(error); stop(); }
    }
    @SubscribeEvent
    public static void tick(ServerTickEvent.Post event) {
        if (server == null || ++ticks != 40) return;
        try { run(); } catch (Throwable error) { fail(error); } finally { stop(); }
    }
    private static void fail(Throwable error) {
        System.out.println("WALL_RUNTIME_TEST_FAILURE " + error);
        error.printStackTrace();
    }
    private static void stop() {
        MinecraftServer s = server;
        server = null;
        if (s != null) s.getCommands().performPrefixedCommand(s.createCommandSourceStack(), "stop");
    }
    private static void run() throws Exception {
        check(!ModList.get().isLoaded("kubejs"), "KubeJS absent; no script supplies wall geometry");
        // Clear only this disposable test world's intersecting construction planes.
        for (int x=-33; x<=33; x++) for (int z=-33; z<=33; z++) {
            if (Math.abs(x)>5 && Math.abs(z)>5) continue;
            for (int y=147; y<=213; y++) {
                BlockPos pos=new BlockPos(x,y,z);
                if (!level.getBlockState(pos).isAir()) level.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
            }
        }
        tune("expandRadiusBase", 1);
        tune("fillRadiusBase", 25);
        tune("fillRadiusPerFill", 5);
        tune("fillBudget", 4096);
        caster.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        for (int yaw : new int[]{0, 90, 180, 270}) {
            caster.setYRot(yaw);
            checkShape(spell(true,false,false,0),1,0,Blocks.STONE,"3x3x1 wall facing " + caster.getDirection());
        }
        caster.setYRot(0);
        caster.setXRot(-85);
        checkShape(spell(true,false,false,0),1,0,Blocks.STONE,"Looking upward still gives a vertical plane");
        caster.setXRot(85);
        checkShape(spell(true,false,false,0),1,0,Blocks.STONE,"Looking downward still gives a vertical plane");
        caster.setXRot(0);
        checkShape(spell(true,false,false,1),1,1,Blocks.STONE,"Area 1: 3x3 plane becomes 3 thick, not wider/taller");
        checkShape(spell(true,false,false,2),1,2,Blocks.STONE,"Area 2: 3x3 plane becomes 5 thick");
        tune("expandRadiusBase",2);
        checkShape(spell(true,false,false,0),2,0,Blocks.STONE,"Config radius 2 gives 5x5x1 wall");
        checkShape(spell(true,false,false,1),2,1,Blocks.STONE,"Radius 2 plus Area gives 5x5x3 wall");
        tune("expandRadiusBase",0);
        checkShape(spell(true,false,false,0),0,0,Blocks.STONE,"Radius zero is one block");
        checkShape(spell(true,false,false,1),0,1,Blocks.STONE,"Radius zero plus Area only increases thickness");
        tune("fillRadiusBase",2);
        tune("fillRadiusPerFill",1);
        checkShape(spell(true,false,true,0),3,0,Blocks.STONE,"Fill expands both width and height at radius zero");
        checkShape(spell(true,false,true,1),3,1,Blocks.STONE,"Fill plus Area increases thickness only");
        caster.setYRot(90);
        checkShape(spell(true,false,true,0),3,0,Blocks.STONE,"Wide Fill wall rotates with horizontal facing");
        caster.setYRot(0);
        tune("expandRadiusBase",1);
        checkShape(spell(true,true,false,0),1,0,Blocks.STONE,"Wall retains priority over Floor if both are present");
        tune("fillRadiusBase",25);
        tune("fillRadiusPerFill",5);
        checkShape(spell(true,false,true,0),30,0,Blocks.STONE,"Full default Fill wall is 61x61x1 = 3721 blocks");
        tune("fillBudget",37);
        cast(CENTER,spell(true,false,true,1));
        Set<BlockPos> capped = scan(CENTER,32,3,Direction.Axis.Z,Blocks.STONE);
        check(capped.size()==37,"Thicker Fill wall stops exactly at 37 successful placements");
        check(capped.contains(CENTER),"Budget-limited wall starts at the target");
        check(capped.stream().allMatch(p -> Math.abs(p.getX())<=2 && Math.abs(p.getY()-180)<=2 && Math.abs(p.getZ())<=1),"Budget-limited wall grows outward, not from far corner");
        clear(capped);
        tune("fillBudget",4096);
        cast(CENTER,spell(true,false,true,1));
        capped = scan(CENTER,32,3,Direction.Axis.Z,Blocks.STONE);
        check(capped.size()==4096,"Default thicker Fill wall respects 4096 placement budget");
        clear(capped);
        tune("fillRadiusBase",2);
        tune("fillRadiusPerFill",1);
        tune("fillBudget",9);
        level.setBlockAndUpdate(CENTER,Blocks.BEDROCK.defaultBlockState());
        level.setBlockAndUpdate(CENTER.above(),Blocks.CHEST.defaultBlockState());
        cast(CENTER,spell(true,false,true,0));
        Set<BlockPos> around = scan(CENTER,5,2,Direction.Axis.Z,Blocks.STONE);
        check(around.size()==9,"Occupied positions do not consume successful-placement budget");
        check(level.getBlockState(CENTER).is(Blocks.BEDROCK),"Existing solid terrain preserved");
        check(level.getBlockState(CENTER.above()).is(Blocks.CHEST) && level.getBlockEntity(CENTER.above())!=null,"Existing block entity preserved");
        clear(around);
        clear(Set.of(CENTER,CENTER.above()));
        tune("fillBudget",4096);
        BlockPos far = new BlockPos(1000000,180,1000000);
        check(!level.isLoaded(far),"Unloaded test target starts unloaded");
        cast(far,spell(true,false,true,0));
        check(!level.isLoaded(far),"Cast does not force-load target chunk");
        BlockPos top = new BlockPos(0,319,0);
        check(level.isOutsideBuildHeight(top.above()),"Top boundary is known");
        cast(top,spell(true,false,false,0));
        Set<BlockPos> upper = scan(top,3,2,Direction.Axis.Z,Blocks.STONE);
        check(upper.size()==6 && upper.stream().allMatch(p -> p.getY()<320),"Wall clips at build height");
        clear(upper);
        double oldSize = level.getWorldBorder().getSize();
        level.getWorldBorder().setSize(3.0);
        cast(CENTER,spell(true,false,true,0));
        Set<BlockPos> border = scan(CENTER,5,2,Direction.Axis.Z,Blocks.STONE);
        check(!border.isEmpty() && border.stream().allMatch(p -> level.getWorldBorder().isWithinBounds(p)),"Wall respects world border");
        level.getWorldBorder().setSize(oldSize);
        clear(border);
        check(Blocks.COBBLED_DEEPSLATE_WALL.defaultBlockState().is(ModTags.CONJURABLE),"Test-only tag allows the wall material");
        caster.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Blocks.COBBLED_DEEPSLATE_WALL));
        cast(CENTER,spell(true,false,false,0));
        Set<BlockPos> masonry = scan(CENTER,3,2,Direction.Axis.Z,Blocks.COBBLED_DEEPSLATE_WALL);
        check(masonry.equals(expected(CENTER,1,0,Direction.Axis.Z)),"Actual Conjure whitelist selects offhand deepslate wall");
        check(connected(CENTER.west(),"east"),"Western endpoint connects inward");
        check(connected(CENTER.east(),"west"),"Eastern endpoint connects inward");
        check(connected(CENTER,"east") && connected(CENTER,"west"),"Middle wall block connects on both sides");
        check(caster.getOffhandItem().getCount()==1,"Conjure does not consume offhand template");
        clear(masonry);
        caster.setYRot(90);
        cast(CENTER,spell(true,false,false,0));
        masonry=scan(CENTER,3,2,Direction.Axis.X,Blocks.COBBLED_DEEPSLATE_WALL);
        check(connected(CENTER.north(),"south") && connected(CENTER.south(),"north"),"North/south wall connections also work");
        clear(masonry);
        caster.setYRot(0);
        caster.setItemSlot(EquipmentSlot.OFFHAND,new ItemStack(Blocks.GOLD_BLOCK));
        checkShape(spell(true,false,false,0),1,0,Blocks.STONE,"Unwhitelisted material still falls back to stone");
        caster.setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
        tune("expandRadiusBase",0);
        checkShape(spell(false,false,false,0),0,0,Blocks.STONE,"Non-wall Conjure radius zero unchanged");
        cast(CENTER,spell(false,false,false,1));
        Set<BlockPos> ordinary=scan(CENTER,3,3,Direction.Axis.Z,Blocks.STONE);
        check(ordinary.size()==7,"Increased Area still expands ordinary Conjure in three dimensions");
        clear(ordinary);
        cast(CENTER,spell(false,true,true,0));
        Set<BlockPos> floor=scan(CENTER,5,5,Direction.Axis.Z,Blocks.STONE);
        check(floor.size()==29 && floor.stream().allMatch(p -> p.getY()==180),"Existing Floor + Fill remains a single-layer circular platform");
        clear(floor);
        tune("expandRadiusBase",4);
        checkShape(spell(true,false,true,0),4,0,Blocks.STONE,"Fill never shrinks a larger configured base radius");
        System.out.println("WALL_RUNTIME_TEST_SUCCESS checks="+checks);
    }
}
