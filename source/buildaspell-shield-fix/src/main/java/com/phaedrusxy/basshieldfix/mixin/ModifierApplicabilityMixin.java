package com.phaedrusxy.basshieldfix.mixin;

import buildaspell.spell.DeliveryMethod;
import buildaspell.spell.SpellEffect;
import buildaspell.spell.SpellModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Fill creates/places material; it does not amplify effects that destroy material. */
@Mixin(targets = "buildaspell.spell.ModifierApplicability", remap = false)
abstract class ModifierApplicabilityMixin {
    @Inject(method = "isUseful", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void basShieldFix$noFillOnRemoval(SpellModifier modifier, SpellEffect effect,
            DeliveryMethod delivery, CallbackInfoReturnable<Boolean> cir) {
        if (modifier == SpellModifier.FILL &&
                (effect == SpellEffect.BREAK || effect == SpellEffect.EVAPORATE_WATER)) {
            cir.setReturnValue(false);
        }
    }
}
