# Unified Casting development and migration notes

Updated November 17, 2026. Addon source: [PhaedrusXY/Arcane_Syntax](https://github.com/PhaedrusXY/Arcane_Syntax).

## Version boundary

The current experimental addon is **Arcane Syntax 1.2**, targeting Minecraft 26.1.2, NeoForge 26.1.2.122, Thaumaturge 1.1.0, and Build a Spell 1.0.3. The reported test instance uses KubeJS 26.1.2-8.0.8 and KubeThaum 1.3.1. Build A Spell Fix 1.1.2 remains separate.

This version number belongs to the addon. It is not an Eldritch Aeons public-release number, and no new CurseForge manifest is declared by these documentation changes.

The current addon release uses **`arcane_syntax_unified-1.2.jar`** with matching internal metadata, following its former CurseForge upload numbered **1.1.0**. Development builds 0.2.1 and 0.2.2 are historical. Version 1.2 preserves their spell logic and corrects the Advanced/Greater Focus research positions; the older in-game testing reports below remain historical, not proof of full 1.2 validation.

## What changed

The 0.2.0 migration moved progression from BaS wand types to native focus tiers, retained crafted-rune learning, retired separate player-mana casting, and rewired the downstream Draconic Artifice gate. The 0.2.1 candidate ports five renamed Vis-helper references for Thaumaturge 1.1.0 and moves Conjure to Foundation throughout learning, recipes, research placement, and focus validation.

The current candidate bundles 76 item textures, six static datapack overrides, and 42 translations in its JAR. Runtime KubeJS item registration, milestones/rune learning, recipes, research, and client callbacks remain scripted. Configuration is intentionally excluded from the incremental installer. This packaging change is not a measured performance optimization.

## Safe migration

Back up the instance and world, close Minecraft completely, remove the prior Unified Casting JAR, and install matching new addon/scripts. Do not copy a complete historical `pack/config` reference over current settings. For multiplayer, apply matching versions to both client and server.

Remove old 1.0.2 Outer Lands test NBT files under `kubejs/data/thaumaturge/structure/outer_lands/`; preserve unrelated namespace content. The old fixes should not be carried into a fresh 1.1.0 pack export. Do not replace whole KubeJS/config directories with an overlay that contains only changed files.

An existing backed-up test world is appropriate for loading/UI/casting tests. A later fresh character/world is needed to verify that Foundation progression works without command-granted research.

## Testing record

- The supplied logs show Unified Casting 0.2.1, Thaumaturge 1.1.0, and KubeThaum 1.3.1 loaded. Startup, client, and server scripts reported successful loading; custom research appeared in game.
- The user identified incomplete focus inscription as the cause of the missing spell: the item had been removed before the Focal Manipulator finished. This is not recorded as a confirmed persistence bug.
- The Outer Lands portal worked after the upstream upgrade with old NBT overrides still installed. Native-template-only generation remains a separate test.
- The user reported that the update appeared generally functional. Controlled custom casting, Vis accounting, tier enforcement, fresh progression, multiplayer, and focus-selector/input regression tests are still outstanding.

## Build evidence is not runtime certification

The supplied test JAR was assembled by an offline compatibility-port process: prior addon classes were reused, the dependency-free core was recompiled, and five API-symbol references plus metadata/resources were changed. It was **not** a complete dependency-resolved NeoForge build against the released 1.1.0 binary.

Historical build reports record **4,656 core assertions** and **971 assertions against API-shaped script mocks**. Those establish the tested local logic, not successful behavior of every transformed class or interaction in Minecraft. The later user loading/research observations improve the runtime evidence but do not remove the remaining build/test limitations.

## Next checks

Complete a simple focus inscription, equip it with a nonconflicting keyboard key, and cast a Syntax effect using a charged native wand. Verify Vis use and ordinary item interaction afterward. Check Foundation Conjure with Wall/Floor on a Lesser Focus, reject a known higher-tier component on that same focus, and test native Thaumaturge spells separately. Preserve logs on failure.

This documentation update does not change gameplay code, publish a binary release, or claim a performance improvement.
