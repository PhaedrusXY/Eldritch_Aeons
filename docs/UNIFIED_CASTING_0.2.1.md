# Unified Casting 0.2.1 development update

**Status as reported on 2026-11-15: experimental, initial single-player testing encouraging.** No new CurseForge pack release, stable tag or performance improvement is asserted here.

Source, reference scripts and the original test artifact: [Arcane Syntax](https://github.com/PhaedrusXY/Thaumaturge_integrations).

## What changed

The integration moves Build a Spell's learned vocabulary onto Thaumaturge wands/foci and stored Vis. Three focus milestones replace the old wand-based prerequisites throughout Arcane Syntax and the downstream Greater Focus gate for Draconic Artifice. Blank Runes and the 75 learned rune identities remain.

The 0.2.1 update targets Thaumaturge 1.1.0 and updates five Vis-helper references. Conjure moves to Foundation in the learning catalog, recipe research, book placement and Java focus-tier checks. Costs/materials stay unchanged. Native discounts and the 0.1 total-Vis/raw-mana ratio remain.

Two Grammars explains the separation of native and Syntax spell construction. Revised research starts cautiously and grows increasingly overconfident. Static assets, translations and selected datapack overrides move into the addon; event-driven recipes and progression remain KubeJS.

## What has actually been observed

The maintainer loaded the addon and existing test world and saw the revised research. The reported missing spell was explained by removing a focus before the timed inscription completed. The final report was that the update appeared generally functional, with more tests planned.

Portal entry succeeded after upgrading Thaumaturge/companions with the custom addon removed, while old structure patches were still present. That observation is not proof that the addon fixed portals or that native templates were used.

The source checks pass 4,656 core assertions and 971 script-mock assertions. They do not launch Minecraft. The original deployment JAR is an offline compatibility candidate, not a completed dependency-resolved NeoForge build.

## Remaining checks

Focus switching and ordinary right-click behavior need retesting with fully inscribed foci. Verify Syntax and native casting separately, Vis payment, fresh-character Foundation Conjure, higher-tier rejection, all three focus milestones, multiplayer parity and save/reopen persistence.

Remove only the old 1.0.2 Outer Lands patch templates before testing a newly generated native labyrinth. Keep current configurations and unrelated scripts. Use the backed-up sandbox for functionality and a fresh character/world for progression.

The original addon mod ID remains `arcane_syntax_unified`; the new repository name does not change saved identifiers.
