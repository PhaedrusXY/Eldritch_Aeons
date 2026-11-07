# Build A Spell Fix 1.1.2 - True Conjured Walls

- Conjure + Wall now creates a vertical rectangular wall rather than a cube or pillar. It faces perpendicular to the caster's horizontal facing direction, aligned to the block grid.
- Base wall width and height use Conjure's expandRadiusBase: each is 2 * radius + 1. Default thickness is one block.
- Fill expands BOTH width and height using Conjure's fill radius settings, without increasing thickness.
- Increased Area increases wall thickness only: 1, 3, 5, ... blocks for 0, 1, 2, ... stacks. Ordinary Conjure and Floor retain their previous Increased Area behavior.
- All new wall placements obey Conjure's fillBudget (4096 by default). Construction proceeds outwards from the target; filled/blocked positions do not consume the placement budget.
- Retains the original conjurable block tag and offhand material selection. Existing masonry-wall whitelist additions continue to work; the patch does not broaden the whitelist.
- Calculates connections for whitelisted wall blocks during placement. Skips solid blocks, unloaded chunks, build-height limits, and positions outside the world border.
- Preserves the standalone Shield capacity fix and non-stacking behavior, controlled Break + Chain, Break/Fill and Evaporate/Fill restrictions, and Floor + Fill platforms.
- In-game display name updated to Build A Spell Fix. Internal mod ID remains bas_shield_fix for compatibility.

## Upgrade
Replace the previous patch JAR on client and server. Keep original Build A Spell 1.0.3.
No KubeJS script is required for this mod. Keep Eldritch Aeons' other scripts and your existing conjurable-material tag additions.
No config values are overwritten. A previous expandRadiusBase=0 remains zero; set it to 1 for a basic 3-wide x 3-high x 1-thick wall.

Wall + Fill intentionally no longer produces the old narrow pillar. Previously saved spells remain readable but use the new geometry. Large multi-layer walls may exhaust the block budget before completing their outer edge.
