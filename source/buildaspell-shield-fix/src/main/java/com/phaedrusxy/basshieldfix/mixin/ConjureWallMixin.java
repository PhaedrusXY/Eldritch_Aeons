package com.phaedrusxy.basshieldfix.mixin;

import buildaspell.spell.Spell;
import com.phaedrusxy.basshieldfix.ConjureWalls;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Only Conjure + Wall changes: other effects and Conjure shapes retain their existing code. */
@Mixin(targets = "buildaspell.spell.execution.EffectExecutors", remap = false)
abstract class ConjureWallMixin {
    @Shadow(remap = false)
    private static BlockState conjureMaterial(Player caster) {
        throw new AssertionError("Mixin shadow was not applied");
    }

    @Inject(method = "executeConjure", at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private static void basFix$verticalWall(Player caster, Level level, Vec3 origin,
            Spell spell, float power, CallbackInfo ci) {
        if (!spell.hasWall()) return;
        if (level instanceof ServerLevel serverLevel) {
            ConjureWalls.place(caster, serverLevel, origin, spell, conjureMaterial(caster));
        }
        ci.cancel();
    }
}
