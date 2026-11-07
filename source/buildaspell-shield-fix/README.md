# Build A Spell Fix v1.1.2

Standalone compatibility mod (`bas_shield_fix`) for Build A Spell 1.0.3 on Minecraft 26.1.2 / NeoForge 26.1.2.122+ / Java 25. Install alongside the original mod, on client and server. No KubeJS dependency.

## Wall geometry

Only Conjure with the Wall modifier is intercepted. Other effects using Wall are unchanged. A wall is a vertical, block-grid-aligned rectangle perpendicular to the caster's horizontal facing at execution, centered on the original target block.

- Without Fill: half-width and half-height = `effects.conjure.expandRadiusBase`.
- With Fill: half-width and half-height = the larger of the base radius and `fillRadiusBase + fillLevel * fillRadiusPerFill`. Fill enlarges both dimensions, not thickness.
- Half-thickness = number of Increased Area modifiers. Total thickness = `1 + 2 * areaLevel`.
- The total successfully placed blocks per Conjure Wall execution is limited by `effects.conjure.fillBudget` (4096 default). Repeat/Double/Echo are still separate executions, not one globally shared budget.
- Positions are visited in square rings centered on the target, with thickness filled locally before the next plane position. A capped thick wall can have an incomplete outer edge. Width/height reach still applies even when budget remains.
- Solid terrain is not replaced; unloaded chunks, build-height limits and the world border are respected. Wall-block connection states are derived before placement.

Examples with expandRadiusBase=1, fillRadiusBase=25, fillRadiusPerFill=5:

| Components | Requested width x height x thickness |
| --- | --- |
| Conjure + Wall | 3 x 3 x 1 |
| Conjure + Wall + Increased Area | 3 x 3 x 3 |
| Conjure + Wall + Fill | 61 x 61 x 1 (3721 positions) |
| Conjure + Wall + Fill + Increased Area | 61 x 61 x 3, stopped at the 4096 placement budget |

Config zero is respected: a basic Wall at radius 0 is a single block; adding Area only thickens it. Set the base radius to 1 for a practical basic wall. No existing config is overwritten.

## Materials

The original `buildaspell:conjurable` block tag and offhand material selection remain authoritative. Keep the Eldritch Aeons masonry-wall tag addition. There is no broad wall-block whitelist bundled with this release: other packs can extend the tag using datapacks. The one-entry tag in `src/testWallRuntime/resources` is a CI fixture only, not a release resource.

## Shield / previous fixes retained

Shield capacity is implemented natively on login, respawn and at Shield application. KubeJS is not required. Do not reinstall the obsolete `kubejs/server_scripts/buildaspell_shield_fix.js`. Equal/weaker Shield recasts do not stack; stronger casts upgrade. Higher existing absorption is preserved.

The prior Break/Fill and Evaporate/Fill restrictions, bounded same-type Break/Chain excavation, and single-layer Floor/Fill platforms remain. Their original Mixin files are unchanged in this version.

## Installation

Stop client and server. Replace the earlier bas_shield_fix JAR with 1.1.2 on both. Keep Build A Spell 1.0.3 and Eldritch Aeons' other scripts/configs. No world reset is required. Saved Wall spells now use the revised geometry; old Wall/Fill pillars are intentionally replaced by wide vertical walls.

## Building and verification

The GitHub Actions workflow downloads the pinned original mod, builds the release before adding test sources, then launches real headless Minecraft/NeoForge servers to exercise walls and Shield with KubeJS absent. Release class bytes must match tested class bytes. Full modpack, interactive-client and claim-protection compatibility still require separate gameplay testing.

Normal build: Java 25, Gradle 9.2.1, exact Build A Spell 1.0.3 in libs/, then `gradle build`.

This is an unofficial community patch, not affiliated with or endorsed by Build A Spell's original developer.
