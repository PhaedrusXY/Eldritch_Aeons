# Changelog

## Unreleased development — Unified Casting 0.2.1 / Thaumaturge 1.1.0

Development notes updated November 15, 2026. **0.2.1 is the addon version, not a new Eldritch Aeons pack-release number.** Existing public-release records and manifests below are preserved.

### Unified casting and progression migration

- Cast learned Build a Spell components through Thaumaturge wands/staves and inscribed native foci, drawing on wand Vis instead of personal mana.
- Replace Worn/Carved/Runic BaS wand milestones with Lesser/Advanced/Greater foci across Arcane Syntax, Thaumonomicon progression, and the downstream Draconic Artifice requirement.
- Use three equipment tiers: Foundation, Structured, and Infused. Retain Blank Runes, individual Inscribed Runes, existing identifiers, and learned knowledge; the old Unsealed Grammar is no longer a new-character progression requirement.
- Retire old BaS wand recipes/JEI entries and separate player-casting controls; hide the personal-mana HUD by default. Keep Build a Spell installed as the execution engine.
- Retain Arcane Altar Spell Power support with native wands, while removing the altar's obsolete mana-capacity/regeneration options.
- Enforce actual focus tiers during analysis, inscription, and casting. Block Break + Fill before inscription/payment; keep Break + Increased Area available.
- Preserve the 0.1 Vis-per-mana conversion and native wand/equipment discounts.

### 0.2.1 compatibility and accessibility

- Target Thaumaturge 1.1.0 and Build a Spell 1.0.3. Update five renamed Vis-helper references rather than only relaxing the dependency check. Use a compatible KubeThaum; 1.3.1 is the reference test version.
- Move **Conjure to Foundation** across rune learning, recipe research, book placement, and focus validation. Wall and Floor remain Foundation; Fill and Increased Area remain Structured. Conjure materials/costs are unchanged.
- Move 76 existing item textures, six static datapack overrides, and 42 translations into the addon JAR. Event-driven KubeJS registration, learning, recipes, and research remain in scripts. No performance improvement has been measured.
- Keep configuration out of the incremental deployment overlay so existing tuning is not overwritten.

### Narrative and testing

- Polish Arcane Syntax and Draconic Artifice toward a narrator who becomes more confident as their judgment worsens.
- Add the optional **Two Grammars** research entry explaining the current native/Syntax incompatibility. Syntax remains Instant and linear; mixing with native components is not implemented.
- Initial testing confirmed the updated setup loads and research appears. Removing a focus before inscription completed explained the reported missing spell; wait for the Focal Manipulator to finish before taking it out.
- Intermittent focus-selector/mouse behavior remains a retest item, not a confirmed upstream or addon defect. Full casting, fresh-progression, and multiplayer testing remain open.
- The Outer Lands portal worked after the Thaumaturge update while earlier 1.0.2 template overrides were still installed. Remove those overrides for a clean native-template test; this result alone does not prove which templates generated the labyrinth.

### Repository organization

- Establish [Arcane Syntax](https://github.com/PhaedrusXY/Arcane_Syntax) as the home for Unified Casting source, reference pack files, and development documentation.
- Keep historical pack snapshots separate from the current development system. See [Unified Casting notes](docs/UNIFIED_CASTING.md) for validation limits and migration instructions.

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
