# Draconic Artifice 0.1.0

Draconic Artifice is an additive KubeJS progression overlay. It does not replace or reconstruct Arcane Syntax.

## What it changes

- Adds a **Draconic Artifice** Thaumonomicon category with **Breath-Forged Metallurgy** research.
- Requires a Runic Wand, Dreadstone, dragon bones, Thaumium, and Thaumaturge infusion knowledge.
- Replaces all three Dragonforge brick recipes so they use Dreadstone rather than ordinary stone bricks.
- Replaces all three Dragonforge Aperture (`*_input`) recipes with Thaumaturge infusion recipes.
- Replaces all three Dragonforge Core recipes with high-cost Thaumaturge infusion recipes.
- Replaces iron with **Thaumium** as the feedstock for fire, ice, and lightning Dragonsteel.
- Keeps the original Ice and Fire recipe IDs so JEI and Bestiary references continue to work.

## Why Thaumium instead of Void Metal

Void Metal exists in Thaumaturge, but it belongs to the Eldritch branch. Requiring it would make every form of Dragonsteel post-Eldritch and would make the containment structure depend on a deliberately unstable material. Thaumium is difficult enough to establish a real Thaumaturge gate, magically receptive, and thematically suited to permanent transformation by dragon blood and breath.

Void Metal remains available for a later optional Dragonsteel upgrade or forbidden Dragonforge research.

## Validation

After installation, fully restart the server and reopen the world. Check the KubeJS server log, run `/kubethaum check`, confirm the three Apertures and three Cores appear as infusion recipes, and verify Dragonsteel accepts Thaumium rather than iron.
