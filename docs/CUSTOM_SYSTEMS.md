# Custom Systems

This page describes the **current experimental focus migration**, Arcane Syntax Unified Casting 0.2.1. Older pack releases used different equipment and research gates; their changelog and archived source are retained separately.

## Arcane Syntax: Unified Casting

Arcane Syntax turns Build a Spell's concepts into a researched magical language, then integrates that language with Thaumaturge's native foci, wands, and Vis. Source and reference pack files: [Thaumaturge Integrations](https://github.com/PhaedrusXY/Thaumaturge_integrations).

### Learning and equipment

- **Cast** is granted at the first focus milestone rather than by obtaining a Worn Wand.
- The other 75 components retain their individual Inscribed Rune identities. Blank Runes remain part of crafting.
- Consuming a crafted rune teaches that exact component permanently; an already-known rune is not consumed.
- Knowledge and equipment are separate restrictions: a known higher-tier component still cannot be inscribed or cast through a lower-tier focus.

| Syntax tier | Equipment gate | Construction access |
| --- | --- | --- |
| Foundation | Lesser Focus | Conjure, Wall, and Floor |
| Structured | Advanced Focus | Includes Fill and Increased Area |
| Infused | Greater Focus | Advanced component access |

Resonant Casting no longer requires a Worn Wand. Structured Syntax and Runic Mastery no longer depend on Carved or Runic BaS wands. The former higher-wand upgrade materials now produce Advanced/Greater foci; the native Lesser Focus recipe remains. Existing research IDs, item registrations, and learned knowledge are preserved rather than wiped.

### Inscription and casting

Compose a spell in the Focal Manipulator, supply its requirements, and **allow inscription to finish** before removing the focus. Equip the inscribed focus through the native focus selector and cast with a native wand/staff.

The default conversion is **0.1 Vis per BaS mana**, with native wand/equipment discounts applied afterward. Separate personal-mana casting and its HUD are retired by default. The Arcane Altar remains for Spell Power and accepts native wands; its obsolete mana-enchantment options are removed.

Native Thaumaturge spells remain usable, but **native and Syntax components cannot be mixed**. Syntax spells currently require Instant casting and a single linear composition. The optional *Two Grammars* research entry explains this distinction in the journal's voice.

### Protection and balance

**Break + Fill is rejected before inscription/payment.** Break + Increased Area remains usable within the configured cost, focus-tier, and equipment limits. Conjure's move to Foundation does not change its ingredients or costs. The original Build a Spell mod and separate Build A Spell Fix remain installed.

No blanket performance benefit is claimed for moving static textures, text, and datapack files into the addon. Event-driven learning, recipes, research, and KubeJS item registration remain in scripts.

## Draconic Artifice

Dragonforge bricks use Dreadstone. Apertures and Cores are built through the Thaumaturge Infusion Matrix, and Thaumium replaces ordinary iron as Dragonsteel feedstock. In the focus migration, the downstream equipment requirement is a **Greater Focus**, so removing old BaS wand recipes does not strand the Dragonforge research branch.

Arcane Syntax and Draconic Artifice prose now follows a gradual progression from cautious observation to intellectual arrogance. The narrator interprets their worsening judgment as increasing competence.

## Thaumic Storage Scanning

**Tom's Thaumometer** (`thaumic_storage_scanning`) is a separate compatibility addon for Thaumaturge research inside Tom's Simple Storage terminals.

- Hover an item in the terminal while holding the Thaumometer on the cursor.
- Known items display their aspects; unknown items are scanned and validated by the server.
- Items without an aspect assignment cannot yield aspect research; that is distinct from terminal-scanning compatibility.

## Validation status

Initial loading and research visibility have been reported with Thaumaturge 1.1.0 and Unified Casting 0.2.1. This is not yet a comprehensive progression, multiplayer, or casting certification. See [Unified Casting development notes](UNIFIED_CASTING.md) and [Known Issues](KNOWN_ISSUES.md).
