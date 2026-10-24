# Known Issues and Workarounds

## Thaumaturge Arcane Workbench: JEI Move Items ejects vis crystals

**Status:** Upstream Thaumaturge regression; reproduced with ordinary Thaumaturge recipes.

Thaumaturge 1.0.2 registers slots 1–15 as Arcane Workbench transfer inputs. Slots 10–15 are actually the six vis-crystal storage slots, so clicking JEI's **Move Items** button can remove or eject crystals already stored in the workbench.

**Workaround:** Place ingredients manually, or remove all stored crystals before using JEI transfer.

This behavior was previously reported in [Thaumaturge issue #127](https://github.com/Leclowndu93150/Thaumaturge/issues/127) and fixed by reducing the transfer range to nine crafting slots. Current source appears to have reintroduced the 15-slot range.

## Some modded items have no Thaumaturge aspects

The Thaumometer can only research items present in Thaumaturge's aspect index. An unassigned modded item may show no aspects and may not complete a useful scan. This does not indicate a Tom's Storage scanning failure.

## Research changes require a full restart

KubeThaum research registration is not reliably refreshed by `/reload`. After changing Arcane Syntax or Draconic Artifice research scripts, fully stop and restart the server and reopen the world.

## Development overlays must be installed on both sides

Arcane Syntax registers custom KubeJS items. Installing its files only on the dedicated server or only on the client will cause registry or synchronization problems.

## Additional client mods are unsupported by default

Shaders, performance replacements, inventory utilities, and resource packs may work, but bug reports should first be reproduced in a clean published profile.
