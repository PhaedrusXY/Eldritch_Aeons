# Changelog

## v1.2 — public beta

### Arcane Syntax 1.0.7

- Fixed Thaumonomicon rendering for Occultism ritual-bound Rune, Blink, and Summon inscriptions so they no longer appear as empty ordinary crafting grids.
- Preserved the 75-component crafted-rune progression and four-tier research structure.
- Kept the Arcane Workbench / Occultism ritual / Thaumaturge infusion split used by the release build.
- Added a Runic Mastery Thaumonomicon page explaining that some rune combinations behave like grammatical phrases rather than merely additive formula terms, using Launch + Levitation + Slow Fall → Creative Flight as the first example.

### Build a Spell tuning and compatibility

- Reduced Break's base mana cost to 10 for pack balance.
- Applied the duration rebalance used by Eldritch Aeons, including longer buffs and utility effects, 60-second base Creative Flight, longer summons, and extended trap lifetime.
- Kept Runic Wand mana reduction at 40%.
- Added the Shield compatibility workaround that raises player `minecraft:max_absorption` capacity to 20 without granting absorption by itself.
- Made all Dreadstone variants truly unbreakable (`destroySpeed = -1`), closing both normal-mining and Build a Spell Break bypasses around Dread Mausoleum progression.
- Raised natural Dread Lich spawn chance from 0.1 to 0.5 while retaining its spawn weight of 4.
- Replaced the Dread Lich loot table so a player kill always awards one Dread Key, while preserving the existing bone and Dread Shard rolls.

### Release packaging

- Public project name for `thaumic_storage_scanning` is **Tom's Thaumometer**; release version is 0.1.6.
- Removed local web-server/authentication configuration, generated JEI lookup history, stock KubeJS example scripts, and a stale nested Build a Spell config ZIP from the cleaned release archive.
- Recorded the cleaned v1.2 CurseForge manifest and release checksums under `packages/` for auditability and recovery.

## Initial public beta — release candidate

### Arcane Syntax 1.0.5 (initial beta)

- Replaced Build a Spell's random rune-conversion progression with 75 deliberately crafted and learned runes.
- Added four research tiers: Foundation, Structured, Infused, and Forbidden.
- Added Worn, Carved, and Runic Wand milestones plus the Arcane Altar and Unsealed Grammar progression.
- Integrated Arcane Workbench inscriptions, Occultism rituals, and Thaumaturge infusion recipes.
- Added custom rune items, textures, tooltips, migration/status commands, and Build a Spell balance configuration.
- Disabled Blank Rune essence accumulation and the original random Spell Rune transformation loop.

### Draconic Artifice 0.1.0

- Added the Draconic Artifice Thaumonomicon category and Breath-Forged Metallurgy research.
- Reworked Dragonforge bricks around Dreadstone.
- Converted Dragonforge Apertures and Cores into Thaumaturge infusion recipes.
- Replaced iron with Thaumium as the feedstock for fire, ice, and lightning Dragonsteel.
- Preserved the original Ice and Fire recipe IDs for JEI and Bestiary compatibility.

### Thaumic Storage Scanning 0.1.6

- Added Thaumometer scanning inside Tom's Simple Storage terminals.
- Added known-aspect display for scanned terminal items.
- Added visible scanning progress and server-side validation of the selected stored item.

### Pack integration

- Added custom Arcane Syntax and Draconic Artifice research text.
- Connected Thaumaturge, Build a Spell, Occultism, and Ice and Fire progression.
- Documented the upstream Thaumaturge Arcane Workbench JEI transfer regression and workaround.

## Release policy

A feature is not considered release-verified until it has been tested in a clean client profile and on a matching dedicated server. CurseForge file notes should list any system that remains experimental.
