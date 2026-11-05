package com.phaedrusxy.basshieldfix;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Minimal compatibility mod for Build A Spell 1.0.3.
 *
 * <p>The actual behavior change is isolated to a single mixin which runs only when the Shield
 * effect is executed. There are no tick handlers, entity scans, scheduled tasks, or saved data.</p>
 */
@Mod(BuildASpellShieldFix.MOD_ID)
public final class BuildASpellShieldFix {
    public static final String MOD_ID = "bas_shield_fix";
    private static final String[] TARGET_CLASSES = {
            "buildaspell.spell.execution.EffectExecutors",
            "buildaspell.entity.SpellProjectileEntity",
            "buildaspell.spell.ModifierApplicability"
    };

    public BuildASpellShieldFix(IEventBus modEventBus) {
        // Load—but do not initialize—the patched class once during startup. This forces Mixin to
        // validate the injection immediately, rather than waiting until somebody first casts Shield.
        // It is a one-time compatibility check and does not add any recurring server work.
        for (String target : TARGET_CLASSES) {
            try {
                Class.forName(target, false, BuildASpellShieldFix.class.getClassLoader());
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Build A Spell 1.0.3 class not found: " + target, exception);
            }
        }
    }
}
