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

    public BuildASpellShieldFix(IEventBus modEventBus) {
        // No event listeners are needed. The mixin is declared in neoforge.mods.toml.
    }
}
