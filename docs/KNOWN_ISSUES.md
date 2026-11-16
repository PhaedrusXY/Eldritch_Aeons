# Known Issues and Workarounds

## Current 0.2.1 test: finish focus inscription before removing the item

**Status: explanation found for the reported missing spell; not established as a persistence defect.**

The Focal Manipulator takes time to finish inscription. Removing the focus as soon as the operation begins leaves the intended spell unfinished. Wait for completion, then verify that the focus carries its spell before testing the selector. This explained the missing spell during the November 15 testing session.

## Focus selector and intermittent mouse input

**Status: needs controlled retesting; no confirmed attribution.**

An invisible focus-selection menu and intermittent right-click failures were reported; alt-tabbing restored eating temporarily. A keyboard rebind stopped the reported mouse interference in one test, but selection still lacked a completed focus. The supplied logs did not identify a focus-switch exception. Thaumaturge's mouse-event handling is a hypothesis, not a confirmed cause.

Retest with one fully inscribed focus, a nonconflicting keyboard binding, and a fully restarted client. Keep existing foci/world data backed up. If the behavior persists, compare a copy of the test instance with and without Unified Casting before attributing it upstream.

## Remove obsolete Outer Lands 1.0.2 template patches when upgrading

The earlier KubeJS patches contain NBT structure overrides, not a new portal mechanic. They can override Thaumaturge 1.1.0's native templates. Remove only the old test files under `kubejs/data/thaumaturge/structure/outer_lands/`; do not delete unrelated Thaumaturge research data.

A portal worked after upgrading, but the overrides were still present. This confirms the combined setup could open a portal, not that all native templates were independently tested. Removing overrides does not rewrite already-generated labyrinth chunks.

## Historical Thaumaturge 1.0.2: JEI Move Items ejects workbench crystals

**Scope: the previously documented 1.0.2 setup; status on 1.1.0 has not been retested here.**

The earlier analysis found slots 1–15 registered as Arcane Workbench transfer inputs although slots 10–15 store vis crystals. JEI **Move Items** could therefore remove/eject stored crystals. A related upstream report is [Thaumaturge issue #127](https://github.com/Leclowndu93150/Thaumaturge/issues/127).

Workaround for affected setups: place ingredients manually, or remove stored crystals before transferring. Do not assume this historical report proves the same defect in every newer version.

## Other log findings, separate from focus switching

The November 15 test logs contain Twisted Mobs recipes referencing `twisted_mobs:deleted_mod_element`, as well as unrelated recipe/data and JEI errors. These are cleanup/retest items, not evidence that any one of them caused the focus-selector behavior.

## Some modded items have no Thaumaturge aspects

The Thaumometer can only research items present in Thaumaturge's aspect index. An unassigned item may show no aspects and may not produce a useful scan. This does not by itself indicate a Tom's Storage scanning failure.

## Restart and client/server requirements

Fully close Minecraft for JAR or startup-script changes. Research script changes require reopening the world/server; use a full restart for migration testing. Avoid `/reload` in this test workflow because earlier pack testing encountered Curios/control problems after it.

Arcane Syntax registers custom KubeJS items and uses the addon on both sides. Dedicated servers and clients must have matching addon, dependency, and overlay versions. Installing only part of an update can cause registry/synchronization problems.

## Additional client mods

Shaders, inventory utilities, performance replacements, and resource packs may work, but reports should identify the exact profile and reproduce the issue with controlled changes rather than treating every test overlay as a published release.
