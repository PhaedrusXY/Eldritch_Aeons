package com.phaedrusxy.basshieldfix.mixin;

import com.llamalad7.mixinextras.sugar.Local;
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
}
