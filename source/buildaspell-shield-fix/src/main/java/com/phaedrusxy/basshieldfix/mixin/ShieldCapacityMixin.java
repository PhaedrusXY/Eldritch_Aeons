package com.phaedrusxy.basshieldfix.mixin;

import buildaspell.config.ModConfig;
import buildaspell.spell.SpellEffect;
import com.phaedrusxy.basshieldfix.ShieldCapacity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The existing Math.min redirect still computes non-stacking Shield absorption.
 * This independent redirect fixes the zero-capacity problem just before the result is applied.
 * Its scope is only Build A Spell's Shield executor, not other absorption sources.
 */
@Mixin(targets = "buildaspell.spell.execution.EffectExecutors", remap = false)
abstract class ShieldCapacityMixin {
    @Redirect(
            method = "executeShield",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;setAbsorptionAmount(F)V",
                    remap = false),
            require = 1,
            remap = false
    )
    private static void basShieldFix$ensureCapacityBeforeGrant(LivingEntity recipient, float amount) {
        ShieldCapacity.ensureCapacity(recipient,
                ModConfig.effectDouble(SpellEffect.SHIELD, "absorptionCap", 20.0));
        recipient.setAbsorptionAmount(amount);
    }
}
