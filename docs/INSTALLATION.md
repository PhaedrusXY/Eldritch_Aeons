# Installation and Server Setup

## Player installation

The supported player installation method is the approved CurseForge profile. The CurseForge manifest resolves third-party projects and their required versions; this GitHub repository is not a replacement launcher package.

1. Install the Eldritch Aeons profile through CurseForge.
2. Allocate enough memory for a modern modded client. The development profile has been tested with an 8 GB maximum heap; lower allocations may work depending on render distance and optional client features.
3. Launch once before adding shaders, resource packs, or other client-only mods.
4. Confirm the title screen reaches the main menu and create a temporary world before joining a server.

## Dedicated server

Use the server pack that matches the exact CurseForge file version.

- The tested development server used **NeoForge 26.1.2.122** and Java 25.
- Client and server must use matching versions of Thaumaturge, KubeThaum, Build a Spell, Occultism, KubeJS, Ice and Fire, and the pack's custom compatibility addons.
- The `config/`, `defaultconfigs/`, `kubejs/`, and quest data shipped with the server pack are part of progression and should not be omitted.
- Pack-specific custom KubeJS items are registered on both sides; `/reload` is insufficient after changing startup scripts.

## Manual development overlays

Pack-specific overlays are development assets, not complete modpack downloads.

### Arcane Syntax

Merge the package's `config/` and `kubejs/` folders into both the client instance and server root, then fully restart both.

### Draconic Artifice

Merge its `kubejs/` folder after Arcane Syntax. It adds files rather than replacing the Arcane Syntax scripts. Because it adds research, fully restart the server and reopen the world.

## Basic validation

After a development update:

1. Check `logs/kubejs/server.log` for script errors.
2. Run `/kubethaum check` when available.
3. Verify the Arcane Syntax tab appears in the Thaumonomicon.
4. Verify the Worn Wand teaches Cast and an inscribed rune teaches its exact component.
5. Verify a Thaumometer can scan a previously unknown item inside Tom's terminal.
6. Verify Dragonforge Aperture and Core recipes appear as Thaumaturge infusion recipes.
7. Verify Dragonsteel accepts Thaumium rather than iron.
