package com.phaedrusxy.basshieldfix;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Replaces buildaspell_shield_fix.js without depending on KubeJS.
 * A capacity is not a shield grant: raising this attribute does not add absorption hearts.
 * The original script used a floor of 20 on login/respawn. Preserve that floor and never
 * lower an existing base or erase modifiers supplied by potions, equipment, or other mods.
 */
public final class ShieldCapacity {
    public static final double LEGACY_CAPACITY_FLOOR = 20.0;

    private ShieldCapacity() {}

    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ensureCapacity(event.getEntity(), LEGACY_CAPACITY_FLOOR);
    }

    public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
        ensureCapacity(event.getEntity(), LEGACY_CAPACITY_FLOOR);
    }

    /** Also called immediately before Shield writes absorption to each actual recipient. */
    public static void ensureCapacity(LivingEntity recipient, double configuredShieldCap) {
        if (recipient.level().isClientSide()) return;
        double minimum = LEGACY_CAPACITY_FLOOR;
        if (Double.isFinite(configuredShieldCap)) {
            minimum = Math.max(minimum, configuredShieldCap);
        }
        AttributeInstance maximum = recipient.getAttribute(Attributes.MAX_ABSORPTION);
        if (maximum != null && maximum.getBaseValue() < minimum) {
            maximum.setBaseValue(minimum);
        }
    }
}
