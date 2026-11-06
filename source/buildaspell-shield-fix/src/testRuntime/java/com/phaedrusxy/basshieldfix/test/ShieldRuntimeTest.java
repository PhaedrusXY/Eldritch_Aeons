package com.phaedrusxy.basshieldfix.test;

import buildaspell.config.ModConfig;
import buildaspell.spell.Spell;
import buildaspell.spell.SpellComponent;
import buildaspell.spell.SpellEffect;
import buildaspell.spell.execution.EffectExecutors;
import com.mojang.authlib.GameProfile;
import com.phaedrusxy.basshieldfix.BuildASpellShieldFix;
import com.phaedrusxy.basshieldfix.ShieldCapacity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import java.util.List;
import java.util.UUID;

/** CI ONLY. Runs real Shield casts in a disposable server with NO KubeJS. */
@EventBusSubscriber(modid = BuildASpellShieldFix.MOD_ID)
public final class ShieldRuntimeTest {
    private static int checks;
    private static void check(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
        checks++;
        System.out.println("SHIELD_CHECK " + checks + ": " + message);
    }
    private static void equal(double actual, double expected, String message) {
        check(Math.abs(actual - expected) < 0.0001, message + " actual=" + actual + " expected=" + expected);
    }
    private static AttributeInstance capacity(LivingEntity entity) {
        AttributeInstance attr = entity.getAttribute(Attributes.MAX_ABSORPTION);
        if (attr == null) throw new AssertionError("Missing absorption attribute on test entity");
        return attr;
    }
    private static void cast(FakePlayer caster, ServerLevel level, LivingEntity target, Spell shield, float power) {
        EffectExecutors.executeShield(caster, level, target.position(), shield, power);
    }
    @SubscribeEvent
    public static void onStarted(ServerStartedEvent event) {
        try {
            check(!ModList.get().isLoaded("kubejs"), "KubeJS is absent from runtime");
            ServerLevel level = event.getServer().overworld();
            level.getChunkAt(new BlockPos(0, 100, 0));
            FakePlayer player = FakePlayerFactory.get(level,
                    new GameProfile(UUID.fromString("2f55ff52-c8ec-4708-a1d0-19bd8931a111"), "ShieldTest"));
            player.setPos(0.5, 100, 0.5);
            level.addNewPlayer(player);
            AttributeInstance attr = capacity(player);
            attr.setBaseValue(0);
            player.setAbsorptionAmount(0);
            NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(player));
            equal(attr.getBaseValue(), 20, "Login applies native capacity floor");
            equal(player.getAbsorptionAmount(), 0, "Login grants no free hearts");
            attr.setBaseValue(0);
            NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(player, false));
            equal(attr.getBaseValue(), 20, "Respawn applies native capacity floor");
            equal(player.getAbsorptionAmount(), 0, "Respawn grants no free hearts");
            attr.setBaseValue(80);
            NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerLoggedInEvent(player));
            equal(attr.getBaseValue(), 80, "Existing higher capacity preserved on login");
            NeoForge.EVENT_BUS.post(new PlayerEvent.PlayerRespawnEvent(player, false));
            equal(attr.getBaseValue(), 80, "Existing higher capacity preserved on respawn");
            Identifier modifierId = Identifier.fromNamespaceAndPath("bas_shield_fix", "runtime_bonus");
            attr.addTransientModifier(new AttributeModifier(modifierId, 4, AttributeModifier.Operation.ADD_VALUE));
            ShieldCapacity.ensureCapacity(player, 20);
            equal(attr.getValue(), 84, "Other attribute modifiers preserved");
            attr.removeModifier(modifierId);

            Spell shield = new Spell(null, List.of(new SpellComponent.Effect(SpellEffect.SHIELD)));
            double cap = ModConfig.effectDouble(SpellEffect.SHIELD, "absorptionCap", 20);
            double base = ModConfig.effectDouble(SpellEffect.SHIELD, "absorptionBase", 2);
            double scale = ModConfig.effectDouble(SpellEffect.SHIELD, "absorptionScale", 0.15);
            double weak = Math.min(base + 10 * scale, cap);
            double strong = Math.min(base + 40 * scale, cap);
            check(weak > 0 && strong > weak, "Original Shield config loaded");
            attr.setBaseValue(0);
            player.setAbsorptionAmount(0);
            cast(player, level, player, shield, 10);
            equal(attr.getBaseValue(), Math.max(20, cap), "Cast repairs zero capacity without player events");
            equal(player.getAbsorptionAmount(), weak, "Actual Shield executor grants expected hearts");
            for (int i = 0; i < 25; i++) cast(player, level, player, shield, 10);
            equal(player.getAbsorptionAmount(), weak, "25 equal recasts do not stack");
            cast(player, level, player, shield, 40);
            equal(player.getAbsorptionAmount(), strong, "Stronger cast upgrades instead of adding");
            cast(player, level, player, shield, 10);
            equal(player.getAbsorptionAmount(), strong, "Weaker cast preserves stronger shield");
            cast(player, level, player, shield, 40);
            equal(player.getAbsorptionAmount(), strong, "Equal strong recast remains non-stacking");
            cast(player, level, player, shield, 10000);
            equal(player.getAbsorptionAmount(), cap, "Large cast is limited by configured Shield cap");
            player.setAbsorptionAmount(1);
            cast(player, level, player, shield, 10);
            equal(player.getAbsorptionAmount(), weak, "Consumed shield is replenished, not added to remainder");
            attr.setBaseValue(80);
            player.setAbsorptionAmount(60);
            cast(player, level, player, shield, 10);
            equal(player.getAbsorptionAmount(), 60, "Higher pre-existing absorption is not lowered");
            equal(attr.getBaseValue(), 80, "Higher base remains unchanged during cast");

            attr.setBaseValue(0);
            player.setAbsorptionAmount(0);
            ShieldCapacity.ensureCapacity(player, 40);
            equal(attr.getBaseValue(), 40, "Native capacity supports a higher configured cap");
            equal(player.getAbsorptionAmount(), 0, "Raising custom capacity does not grant hearts");
            attr.setBaseValue(0);
            ShieldCapacity.ensureCapacity(player, Double.NaN);
            equal(attr.getBaseValue(), 20, "Nonfinite cap cannot corrupt attribute");

            LivingEntity mob = EntityType.ARMOR_STAND.create(level, EntitySpawnReason.COMMAND);
            check(mob != null, "Created real non-player Shield recipient");
            mob.setPos(0.5, 105, 0.5);
            check(level.addFreshEntity(mob), "Non-player recipient added to world");
            capacity(mob).setBaseValue(0);
            mob.setAbsorptionAmount(0);
            cast(player, level, mob, shield, 10);
            equal(capacity(mob).getBaseValue(), Math.max(20, cap), "Actual non-player recipient gains capacity");
            equal(mob.getAbsorptionAmount(), weak, "Actual non-player Shield grant works");
            cast(player, level, mob, shield, 10);
            equal(mob.getAbsorptionAmount(), weak, "Non-player recast remains non-stacking");
            mob.discard();
            System.out.println("SHIELD_RUNTIME_TEST_SUCCESS checks=" + checks + " absorptionCap=" + cap);
        } catch (Throwable error) {
            System.out.println("SHIELD_RUNTIME_TEST_FAILURE " + error);
            error.printStackTrace();
        } finally {
            event.getServer().getCommands().performPrefixedCommand(
                    event.getServer().createCommandSourceStack(), "stop");
        }
    }
}
