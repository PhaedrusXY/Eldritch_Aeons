package com.phaedrusxy.basshieldfix.mixin;

import buildaspell.config.ModConfig;
import buildaspell.entity.SpellProjectileEntity;
import buildaspell.registry.ModBlocks;
import buildaspell.spell.Spell;
import buildaspell.spell.SpellEffect;
import buildaspell.spell.SpellModifier;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.HashSet;
import java.util.Set;

/** Scoped Break + Chain correction; all other projectile chains use original behavior. */
@Mixin(targets = "buildaspell.entity.SpellProjectileEntity", remap = false)
abstract class SpellProjectileEntityMixin {
    @Shadow(remap = false) @Nullable private Spell spell;
    @Unique @Nullable private Block basShieldFix$originalBlock;

    // Capture BEFORE executeSpellAtLocationWithDelay: the original Break turns the
    // hit block into air, which otherwise prevents upstream block chaining.
    @ModifyArg(
        method = "tick",
        at = @At(value = "INVOKE",
            target = "Lbuildaspell/spell/execution/SpellExecutor;executeSpellAtLocationWithDelay(Lnet/minecraft/world/entity/player/Player;Lbuildaspell/spell/Spell;Lnet/minecraft/world/phys/Vec3;)V",
            ordinal = 0, remap = false),
        index = 2, require = 1, remap = false
    )
    private Vec3 basShieldFix$captureBeforeBreak(Vec3 location, @Local BlockHitResult blockHit) {
        basShieldFix$originalBlock = null;
        if (spell != null && spell.getChainLevel() > 0 && spell.getEffects().contains(SpellEffect.BREAK)) {
            Level level = ((SpellProjectileEntity) (Object) this).level();
            if (level.isLoaded(blockHit.getBlockPos())) {
                basShieldFix$originalBlock = level.getBlockState(blockHit.getBlockPos()).getBlock();
            }
        }
        return location;
    }

    // Only break spells take the new single-block path. Original Chain stays in
    // charge of entity chaining and all non-Break block effects.
    @Inject(method = "resolveBlockChain", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void basShieldFix$limitedBlockChain(Player caster, BlockPos impact, CallbackInfo ci) {
        if (spell == null || !spell.getEffects().contains(SpellEffect.BREAK)) return;
        ci.cancel();

        SpellProjectileEntity projectile = (SpellProjectileEntity) (Object) this;
        if (!(projectile.level() instanceof ServerLevel level)) return;
        Block type = basShieldFix$originalBlock;
        if (type == null || type.defaultBlockState().isAir()) return;

        int count = (int) Math.min(32L, 8L * Math.max(0, spell.getChainLevel()));
        int radius = Math.max(1, ModConfig.modifierInt(SpellModifier.CHAIN, "searchRadius", 4));
        float hardness = (float) ModConfig.effectDouble(SpellEffect.BREAK, "hardnessBase", 3.0)
                + spell.getPowerLevel() * (float) ModConfig.effectDouble(SpellEffect.BREAK, "hardnessPerPower", 2.0);
        ItemStack tool = basShieldFix$breakTool(level, spell);
        Set<BlockPos> seen = new HashSet<>();
        BlockPos current = impact.immutable();
        seen.add(current);
        for (int i = 0; i < count; i++) {
            BlockPos next = basShieldFix$nearest(level, current, type, radius, hardness, seen);
            if (next == null) break;
            seen.add(next);
            if (basShieldFix$breakOne(caster, level, next, tool, hardness)) current = next;
        }
    }

    @Unique @Nullable
    private static BlockPos basShieldFix$nearest(ServerLevel level, BlockPos from,
            Block type, int radius, float hardness, Set<BlockPos> seen) {
        BlockPos result = null;
        double best = Double.POSITIVE_INFINITY;
        for (BlockPos pos : BlockPos.betweenClosed(
                from.offset(-radius, -radius, -radius), from.offset(radius, radius, radius))) {
            if (seen.contains(pos) || !level.isLoaded(pos)) continue;
            if (!level.getBlockState(pos).is(type) || !basShieldFix$canBreak(level, pos, hardness)) continue;
            double dist = from.distSqr(pos);
            if (dist < best) {
                best = dist;
                result = pos.immutable();
            }
        }
        return result;
    }

    @Unique
    private static boolean basShieldFix$canBreak(Level level, BlockPos pos, float maxHardness) {
        BlockState state = level.getBlockState(pos);
        if (state.isAir()) return false;
        if (state.is(Blocks.LIGHT) || state.is(ModBlocks.SPELL_LIGHT.get())) return true;
        float hardness = state.getDestroySpeed(level, pos);
        return hardness >= 0 && hardness <= maxHardness;
    }

    @Unique
    private static boolean basShieldFix$breakOne(Player caster, ServerLevel level, BlockPos pos,
            ItemStack tool, float maxHardness) {
        if (!level.isLoaded(pos) || !basShieldFix$canBreak(level, pos, maxHardness)) return false;
        BlockState state = level.getBlockState(pos);
        if (state.is(Blocks.LIGHT) || state.is(ModBlocks.SPELL_LIGHT.get())) {
            level.removeBlock(pos, false);
            return true;
        }
        for (ItemStack drop : Block.getDrops(state, level, pos,
                level.getBlockEntity(pos), caster, tool)) {
            level.addFreshEntity(new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
        level.sendParticles(ParticleTypes.POOF, pos.getX() + 0.5,
                pos.getY() + 0.5, pos.getZ() + 0.5, 4, 0.3, 0.3, 0.3, 0.15);
        level.removeBlock(pos, false);
        return true;
    }

    @Unique
    private static ItemStack basShieldFix$breakTool(Level level, Spell spell) {
        if (spell.hasGentleness()) {
            ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
            tool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.SILK_TOUCH), 1);
            return tool;
        }
        int fortune = spell.getFortuneLevel();
        if (fortune > 0) {
            ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
            tool.enchant(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.FORTUNE), fortune);
            return tool;
        }
        return ItemStack.EMPTY;
    }
}
