# Build A Spell Shield Fix v1.1.1

Standalone patch (`bas_shield_fix`) for Build A Spell 1.0.3, Minecraft 26.1.2,
NeoForge 26.1.2.122+, Java 25. Install on client and server alongside the original mod.

## Shield is now fully native: no KubeJS dependency

Version 1.1.0 fixed additive Shield stacking but still relied on
`kubejs/server_scripts/buildaspell_shield_fix.js` to raise players' MAX_ABSORPTION
base value to at least 20 on login and respawn. Without that script (or another
source of capacity), a zero maximum could suppress Shield's absorption.

Version 1.1.1 moves both player-event handlers into Java and ensures capacity
immediately before each actual Shield recipient receives absorption. This covers
fresh players and non-player recipients as well as existing saves. Raising capacity
does not grant hearts: Shield still supplies its configured amount. The original
non-stacking behavior remains: equal/weaker casts do not add protection, stronger
casts upgrade it, and existing higher absorption is not lowered.

The login/respawn floor is 20, exactly like the old script. Cast-time capacity also
accommodates Build A Spell's configured `effects.shield.absorptionCap` if higher.
No higher existing base values or attribute modifiers are removed. Negative
attribute modifiers from other mods are not bypassed. No tick loop is added.

## Upgrade from 1.1.0

1. Stop server and client; replace the old patch JAR with `bas_shield_fix-1.1.1.jar` on both.
2. Remove only `kubejs/server_scripts/buildaspell_shield_fix.js` from the server and pack/client overrides.
3. Keep Build A Spell 1.0.3 and all other KubeJS scripts/configuration unchanged. Eldritch Aeons still needs KubeJS for unrelated systems.
4. Fully restart. No world reset, spell migration, or player-data edit is required.

The legacy script is idempotent with the native 20-point floor, so accidentally
leaving it in place does not add absorption repeatedly; it is simply unnecessary.

## Existing v1.1.0 changes retained

- Break ignores Fill; normal Break behavior and other modifiers remain.
- Evaporate Water ignores Fill.
- Break + Chain projectile block hits: up to 8 additional same-type blocks per Chain level, capped at 32, within the configured per-hop search radius (default 4).
- Conjure + Floor + Fill creates a one-block-thick horizontal platform; existing whitelist, radius and placement budget apply.
- Conjure + Wall + Fill remains unchanged, including vertical pillars.
- Fill is marked not useful for Break and Evaporate Water in the spell builder.

No changes to Arcane Syntax progression, Dread Incursions, Champion scripts, or modpack recipes.

## Build and test

Use Java 25 and Gradle 9.2.1. Put official `buildaspell-1.0.3-mc26.1.2.jar` in `libs/`, then run `gradle clean build`.
CI produces the release JAR first, then separately includes `src/testRuntime/java`
with `-PshieldRuntimeTest` to exercise real Minecraft entities, NeoForge player
events, and the original Build A Spell Shield executor. No KubeJS is installed.
The CI-only harness is not shipped in the release JAR.
