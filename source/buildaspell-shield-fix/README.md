# Build A Spell Shield Fix v1.1.0

This extends our original standalone mod `bas_shield_fix`, alongside the official Build A Spell 1.0.3. It does not replace its JAR.

## Changes from 1.0.0
- Original Shield absorption stacking correction is unchanged.
- Break + Fill acts like ordinary Break; no massive connected terrain excavation.
- Evaporate Water + Fill acts like ordinary local evaporation.
- Break + Chain on Cast/Tracking projectile hits up to 8 additional same-block targets per Chain stack, capped at 32, breaking each extra block individually (not as another area effect); the original per-hop search radius remains 4.
- Conjure + Floor + Fill creates a one-block-thick horizontal platform (default radius 30, max 4096 placements).
- Conjure + Wall + Fill retains its dramatic pillar behavior.
- Build A Spell's builder no longer suggests Fill for Break or Evaporate Water.

## Compatibility
- MC 26.1.2, NeoForge 26.1.2.122+, Java 25.
- Requires Build A Spell 1.0.3 (not an alternate version).
- Keep `kubejs/server_scripts/buildaspell_shield_fix.js` for maximum absorption workaround.
- Install the new compatibility JAR on client AND server; remove 1.0.0.

No saved world data or ticking event listeners are added. Saved spells remain loadable; an existing Break+Fill spell may still charge for Fill until edited.

## Build
Run the GitHub Actions workflow on branch `feature/bas-shield-fix-v1.1.0`, or place official Build A Spell 1.0.3 in `libs/` and build with Java 25 / Gradle 9.2.1. Test on a backup world.
