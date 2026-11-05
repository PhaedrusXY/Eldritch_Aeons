package com.phaedrusxy.basshieldfix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import buildaspell.config.ModConfig;
import buildaspell.spell.Spell;
import buildaspell.spell.SpellComponent;
import buildaspell.spell.SpellModifier;
import buildaspell.spell.SpellEffect;
import buildaspell.spell.execution.EffectExecutors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Changes Build A Spell's Shield calculation from additive stacking to replacement/upgrade behavior.
 *
 * <p>Build A Spell 1.0.3 computes:</p>
 * <pre>
 * min(currentAbsorption + shieldGrant, absorptionCap)
 * </pre>
 *
 * <p>This redirect intercepts only that {@link Math#min(float, float)} call. The first argument still
 * contains the uncapped sum, so the individual grant can be recovered without copying the rest of
 * Build A Spell's method:</p>
 * <pre>
 * shieldGrant = (currentAbsorption + shieldGrant) - currentAbsorption
 * result      = max(currentAbsorption, min(shieldGrant, absorptionCap))
 * </pre>
 *
 * <p>A weaker or equal recast therefore leaves the existing absorption unchanged; a stronger cast
 * upgrades it. The injection executes only when Shield is cast and adds no tick-time workload.</p>
 */
@Mixin(targets = "buildaspell.spell.execution.EffectExecutors", remap = false)
abstract class EffectExecutorsMixin {

    @Redirect(
            method = "executeShield",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;min(FF)F",
                    ordinal = 0,
                    remap = false
            ),
            require = 1,
            remap = false
    )
    private static float basShieldFix$replaceInsteadOfStack(
            float currentPlusGrant,
            float absorptionCap,
            @Local LivingEntity entity
    ) {
        float currentAbsorption = entity.getAbsorptionAmount();
        float shieldGrant = Math.max(0.0F, currentPlusGrant - currentAbsorption);
        float cappedGrant = Math.min(shieldGrant, absorptionCap);
        return Math.max(currentAbsorption, cappedGrant);
    }

    // Fill only changes creation effects. Break/Evaporate use their unmodified executor
    // with a temporary spell copy minus Fill, retaining their normal range/drops.
    @Inject(method = "executeBreak", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void basShieldFix$breakIgnoresFill(Player caster, Level level, Vec3 origin,
            Spell spell, float power, CallbackInfo ci) {
        if (spell.getFillLevel() == 0) return;
        EffectExecutors.executeBreak(caster, level, origin, basShieldFix$withoutFill(spell), power);
        ci.cancel();
    }

    @Inject(method = "executeEvaporateWater", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void basShieldFix$evaporateIgnoresFill(Player caster, Level level, Vec3 origin,
            Spell spell, float power, CallbackInfo ci) {
        if (spell.getFillLevel() == 0) return;
        EffectExecutors.executeEvaporateWater(caster, level, origin, basShieldFix$withoutFill(spell), power);
        ci.cancel();
    }

    private static Spell basShieldFix$withoutFill(Spell spell) {
        List<SpellComponent> components = new ArrayList<>();
        for (SpellComponent component : spell.getComponents()) {
            if (component instanceof SpellComponent.Modifier modifier && modifier.modifier() == SpellModifier.FILL) continue;
            components.add(component);
        }
        return new Spell(spell.getDelivery(), components, spell.getDeliveryModifiers(), spell.getVisual());
    }

    // Preserve the upstream whitelist of conjurable offhand materials.
    @Shadow(remap = false)
    private static BlockState conjureMaterial(Player caster) {
        throw new AssertionError("Mixin shadow was not applied");
    }

    // Floor + Fill creates a single flat, center-out platform. Wall + Fill stays intact.
    @Inject(method = "executeConjure", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void basShieldFix$floorFill(Player caster, Level level, Vec3 origin,
            Spell spell, float power, CallbackInfo ci) {
        if (!spell.hasFloor() || spell.hasWall() || spell.getFillLevel() == 0) return;
        int radius = Math.max(0, ModConfig.effectInt(SpellEffect.CONJURE, "fillRadiusBase", 25)
                + spell.getFillLevel() * ModConfig.effectInt(SpellEffect.CONJURE, "fillRadiusPerFill", 5));
        int budget = Math.max(0, ModConfig.effectInt(SpellEffect.CONJURE, "fillBudget", 4096));
        BlockPos center = BlockPos.containing(origin);
        BlockState material = conjureMaterial(caster);
        level.playSound(null, origin.x, origin.y, origin.z,
                SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.7f, 1.2f);
        int placed = 0;
        outer:
        for (int ring = 0; ring <= radius; ring++) {
            for (int dx = -ring; dx <= ring; dx++) {
                for (int dz = -ring; dz <= ring; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != ring) continue;
                    if ((long) dx * dx + (long) dz * dz > (long) radius * radius) continue;
                    if (placed >= budget) break outer;
                    BlockPos pos = center.offset(dx, 0, dz);
                    if (!level.isLoaded(pos)) continue;
                    BlockState previous = level.getBlockState(pos);
                    if (!previous.isAir() && !previous.canBeReplaced()) continue;
                    level.setBlockAndUpdate(pos, material);
                    placed++;
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.ENCHANT,
                                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                3, 0.2, 0.2, 0.2, 0.1);
                    }
                }
            }
        }
        ci.cancel();
    }
}
