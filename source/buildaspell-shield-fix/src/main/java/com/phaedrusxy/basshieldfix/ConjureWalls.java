package com.phaedrusxy.basshieldfix;

import buildaspell.config.ModConfig;
import buildaspell.spell.Spell;
import buildaspell.spell.SpellEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Places a vertical rectangular wall centered at the original Conjure target.
 * Radius controls width/height, Fill selects the larger radius, and Increased Area
 * adds one thickness layer on each side per stack. No global state or tick handler.
 */
public final class ConjureWalls {
    private final ServerLevel level;
    private final BlockPos center;
    private final BlockState material;
    private final Direction.Axis normalAxis;
    private final int halfThickness;
    private final int budget;
    private int placed;

    private ConjureWalls(ServerLevel level, BlockPos center, BlockState material,
            Direction.Axis normalAxis, int halfThickness, int budget) {
        this.level = level;
        this.center = center;
        this.material = material;
        this.normalAxis = normalAxis;
        this.halfThickness = halfThickness;
        this.budget = budget;
    }

    public static int place(Player caster, ServerLevel level, Vec3 origin,
            Spell spell, BlockState material) {
        int fill = Math.max(0, spell.getFillLevel());
        int baseRadius = Math.max(0, ModConfig.effectInt(SpellEffect.CONJURE, "expandRadiusBase", 1));
        long filledRadius = (long) ModConfig.effectInt(SpellEffect.CONJURE, "fillRadiusBase", 25)
                + (long) fill * ModConfig.effectInt(SpellEffect.CONJURE, "fillRadiusPerFill", 5);
        int radius = fill > 0 ? (int) Math.max(baseRadius, Math.min(Integer.MAX_VALUE - 1L, filledRadius)) : baseRadius;
        int halfThickness = Math.max(0, spell.getModifierCount(buildaspell.spell.SpellModifier.INCREASED_AREA));
        int budget = Math.max(0, ModConfig.effectInt(SpellEffect.CONJURE, "fillBudget", 4096));
        ConjureWalls wall = new ConjureWalls(level, BlockPos.containing(origin), material,
                caster.getDirection().getAxis(), halfThickness, budget);
        level.playSound(null, origin.x, origin.y, origin.z,
                SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 0.7f, 1.2f);
        if (budget == 0) return 0;

        // Visit each plane position exactly once, in concentric square perimeters.
        // Complete thickness at each position before expanding the plane further.
        // A capped cast therefore grows near the target, not from a remote corner.
        for (int ring = 0; ring <= radius; ring++) {
            for (int y = -ring; y <= ring; y++) {
                if (Math.abs(y) == ring) {
                    for (int width = -ring; width <= ring; width++) {
                        if (!wall.column(width, y)) return wall.placed;
                    }
                } else {
                    if (!wall.column(-ring, y) || !wall.column(ring, y)) return wall.placed;
                }
            }
        }
        return wall.placed;
    }

    /** Center thickness layer, then symmetric negative/positive pairs. */
    private boolean column(int width, int y) {
        if (!at(width, y, 0)) return false;
        for (int depth = 1; depth <= halfThickness; depth++) {
            if (!at(width, y, -depth) || !at(width, y, depth)) return false;
        }
        return true;
    }

    private boolean at(int width, int y, int depth) {
        if (placed >= budget) return false;
        BlockPos pos = normalAxis == Direction.Axis.X
                ? center.offset(depth, y, width) : center.offset(width, y, depth);
        if (level.isOutsideBuildHeight(pos) || !level.isLoaded(pos)
                || !level.getWorldBorder().isWithinBounds(pos)) return true;
        // Neighbour-shape queries for real wall blocks must not load extra chunks.
        for (Direction side : Direction.Plane.HORIZONTAL) {
            if (!level.isLoaded(pos.relative(side))) return true;
        }
        BlockState previous = level.getBlockState(pos);
        if (!previous.isAir() && !previous.canBeReplaced()) return true;

        // Default WallBlock states do not know their neighbouring connections.
        // Derive them before placement; ordinary update flags maintain subsequent links.
        BlockState state = Block.updateFromNeighbourShapes(material, level, pos);
        if (level.setBlockAndUpdate(pos, state)) {
            placed++;
            level.sendParticles(ParticleTypes.ENCHANT, pos.getX() + 0.5, pos.getY() + 0.5,
                    pos.getZ() + 0.5, 3, 0.2, 0.2, 0.2, 0.1);
        }
        return placed < budget;
    }
}
