# Arcane Syntax

## Current development: Arcane Syntax 0.2.1

The maintained integration source has moved to **[Arcane Syntax](https://github.com/PhaedrusXY/Thaumaturge_integrations)**. It targets Thaumaturge 1.1.0 and Build a Spell 1.0.3 on Minecraft 26.1.2.

Foundation/Structured/Infused use Lesser/Advanced/Greater Foci. Blank Runes and 75 Inscribed Runes remain, with Cast granted at the first focus milestone. Conjure, Wall and Floor are Foundation; Fill/Increased Area are Structured. Spells use native wands and Vis. The old BaS wand milestones, separate player-casting route and personal-mana HUD are retired.

Native and Syntax components remain incompatible within one spell. Syntax is Instant-only and linear. Research prose includes Two Grammars and the progression from caution toward overconfidence. Draconic Artifice now depends on Greater Focus instead of Runic Wand.

Use the current repository's installation guide and matched overlay, not the older scripts in this directory. `/ea_arcane_syntax_status` remains the diagnostic status command. Wait for the Focal Manipulator to finish inscription before removing a focus.

See [current custom systems](../../docs/CUSTOM_SYSTEMS.md) and [the 0.2.1 development note](../../docs/UNIFIED_CASTING_0.2.1.md).

## Historical snapshot in this directory

The retained `runic_mastery_phrases.js` files and `RUNIC_MASTERY_PHRASES.md` describe the older Arcane Syntax 1.0.7 release. That release used Worn/Carved/Runic wand progression and a fourth Forbidden tier; those are **not the current focus-based design**. Do not layer those historical scripts over 0.2.1.

The older Runic Mastery phrase example, Launch + Levitation + Slow Fall producing Creative Flight, described a combination rather than requiring literal slot order. The historical source remains for traceability, not current installation.
