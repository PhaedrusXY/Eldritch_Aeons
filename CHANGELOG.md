# Changelog

## v1.3 — public beta

### Arcane Syntax 1.0.7

- Added a Runic Mastery Thaumonomicon discovery explaining that some rune combinations behave like grammatical phrases rather than merely additive formula terms.
- Uses **Launch + Levitation + Slow Fall → Creative Flight** as the first example without implying that literal slot order is mechanically required.

### Dread Mausoleum progression

- Made all Dreadstone variants truly unbreakable (`destroySpeed = -1`), closing both normal-mining and Build a Spell Break bypasses.
- Raised natural Dread Lich spawn chance from 0.1 to 0.5 while retaining spawn weight 4.
- Dread Liches killed by a player now always award exactly one Dread Key.
- Existing bone and Dread Shard loot is preserved.

## v1.2 — public beta

### Arcane Syntax 1.0.6

- Fixed Thaumonomicon rendering for Occultism ritual-bound Rune, Blink, and Summon inscriptions so they no longer appear as empty ordinary crafting grids.
- Preserved the 75-component crafted-rune progression and four-tier research structure.
- Kept the Arcane Workbench / Occultism ritual / Thaumaturge infusion split used by the release build.

### Build a Spell tuning and compatibility

- Reduced Break's base mana cost to 10 for pack balance.
- Applied the duration rebalance used by Eldritch Aeons, including longer buffs and utility effects, 60-second base Creative Flight, longer summons, and extended trap lifetime.
- Kept Runic Wand mana reduction at 40%.
- Added the Shield compatibility workaround that raises player `minecraft:max_absorption` capacity to 20 without granting absorption by itself.
- Set Dreadstone hardness to 50 as the initial anti-bypass measure.

### Release packaging

- Public project name for `thaumic_storage_scanning` is **Tom's Thaumometer**; release version is 0.1.6.
- Removed local web-server/authentication configuration, generated JEI lookup history, stock KubeJS example scripts, and a stale nested Build a Spell config ZIP from the cleaned release archive.

## Initial public beta — release candidate

### Arcane Syntax 1.0.5

- Replaced Build a Spell's random Spell Rune progression with 75 deliberately crafted and learned runes.
- Added four research tiers: Foundation, Structured, Infused, and Forbidden.
- Added Worn, Carved, and Runic Wand milestones plus the Arcane Altar and Unsealed Grammar progression.
- Integrated Arcane Workbench inscriptions, Occultism rituals, and Thaumaturge infusion recipes.

### Draconic Artifice 0.1.0

- Added the Draconic Artifice Thaumonomicon category and Breath-Forged Metallurgy research.
- Reworked Dragonforge bricks around Dreadstone.
- Converted Dragonforge Apertures and Cores into Thaumaturge infusion recipes.
- Replaced iron with Thaumium as the feedstock for fire, ice, and lightning Dragonsteel.
