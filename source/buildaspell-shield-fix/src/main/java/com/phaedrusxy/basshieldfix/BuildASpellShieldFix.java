package com.phaedrusxy.basshieldfix;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

/** Targeted compatibility fixes; no tick listeners, scheduled tasks, or entity scans. */
@Mod(BuildASpellShieldFix.MOD_ID)
public final class BuildASpellShieldFix {
    public static final String MOD_ID = "bas_shield_fix";
    private static final String[] TARGET_CLASSES = {
            "buildaspell.spell.execution.EffectExecutors",
            "buildaspell.entity.SpellProjectileEntity",
            "buildaspell.spell.ModifierApplicability"
    };

    public BuildASpellShieldFix(IEventBus modEventBus) {
        // Validate injection targets immediately, before the first cast.
        for (String target : TARGET_CLASSES) {
            try {
                Class.forName(target, false, BuildASpellShieldFix.class.getClassLoader());
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Build A Spell 1.0.3 class not found: " + target, exception);
            }
        }
        // Native equivalents of the former KubeJS loggedIn/respawned script.
        NeoForge.EVENT_BUS.addListener(ShieldCapacity::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(ShieldCapacity::onPlayerRespawned);
    }
}
