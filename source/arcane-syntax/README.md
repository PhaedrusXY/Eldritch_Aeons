# Arcane Syntax 1.0.6

Arcane Syntax replaces Build a Spell's random Spell Rune progression with deliberately crafted, researched, and permanently learned components.

## Installation

Merge the release package's `config/` and `kubejs/` folders into both the client instance and dedicated server root. Fully restart both sides; `/reload` is not sufficient for startup item registration or Build a Spell configuration changes.

## Progression

1. Three Essentia Crystals, gold, and a stick produce the **Worn Wand**, which teaches Cast.
2. The Arcane Workbench produces Blank Runes and Tier 1 inscriptions.
3. A Spirit-Attuned Gem leads to the **Carved Wand** and Tier 2 inscriptions, including Occultism ritual-bound syntax.
4. Thaumaturge infusion produces the **Runic Wand**, Arcane Altar, and Tier 3 syntax.
5. Eldritch infusion and Iesnium lead to **The Unsealed Grammar** and six Tier 4 capstones.

Right-clicking an Inscribed Rune consumes it and teaches its exact Build a Spell component. Duplicate learned runes are retained.

## Commands

- `/ea_arcane_syntax_migrate confirm` — one-time migration from the earlier random-rune prototype.
- `/ea_arcane_syntax_status` — reports current Arcane Syntax milestones and learned component counts.

## 1.0.6 research-page hotfix

Rune, Blink, and Summon are Occultism ritual inscriptions. Their Thaumonomicon entries no longer embed them as ordinary crafting recipes; the research pages instead direct the player to the ritual/JEI path, preventing the empty 3×3 crafting-grid display seen in the earlier build.

## Coverage

The current package contains 75 learned components: six crafted delivery runes, 47 effect runes, 22 modifier runes, plus Cast granted by the Worn Wand. Recipes are distributed among the Arcane Workbench, Occultism rituals, and Thaumaturge infusion.
