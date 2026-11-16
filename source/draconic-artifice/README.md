# Draconic Artifice

Draconic Artifice is the pack's KubeJS progression overlay for Dragonsteel. Its current focus-migration snapshot is maintained alongside Arcane Syntax in [Arcane Syntax](https://github.com/PhaedrusXY/Thaumaturge_integrations/tree/main/pack/kubejs).

## Current development changes

- Adds a Draconic Artifice Thaumonomicon category with Breath-Forged Metallurgy research.
- Uses **Greater Focus** as its equipment milestone in the current integration, replacing the historical Runic Wand requirement. Dreadstone, dragon bones, Thaumium and Thaumaturge infusion remain part of the progression.
- Replaces the three Dragonforge brick recipes with Dreadstone-based masonry.
- Uses Thaumaturge infusion for the three Apertures and three Cores.
- Uses **Thaumium**, not iron, as fire/ice/lightning Dragonsteel feedstock, preserving the original Ice and Fire recipe IDs.
- Polishes the research narration without changing its basic materials or forging role.

## Why Thaumium instead of Void Metal

The earlier design deliberately avoided making baseline Dragonsteel depend on the Eldritch branch. Thaumium supplies a demanding, magically receptive feedstock without making Void Metal a required containment material. A later optional upgrade was discussed but is not implemented by this update.

## Validation

Fully restart after the matching integration update, inspect the KubeJS logs, verify the Greater Focus research milestone, check the Aperture/Core infusion recipes and test Thaumium feedstock. The maintainer's initial 0.2.1 loading report is not a substitute for these progression checks.

Historical release 0.1.0 used Runic Wand. Read historical changelog entries in that version context rather than reinstalling its old prerequisites.
