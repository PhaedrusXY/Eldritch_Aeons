# Eldritch Aeons v1.1 pack-owned overrides

This directory records the pack-owned compatibility fixes and tuning notes from the cleaned v1.1 release.

Readable source files included here:

- `kubejs/server_scripts/buildaspell_shield_fix.js` — gives players nonzero max-absorption capacity so Build a Spell Shield can actually set absorption hearts.
- `kubejs/startup_scripts/dreadstone_hardness.js` — raises Dreadstone hardness to 50 so Build a Spell Break does not bypass the Dragonforge progression gate.
- `BUILDASPELL_TUNING.md` — the key v1.1 balance values that differ materially from Build a Spell defaults.

Release metadata is under `packages/`. The CurseForge release remains authoritative for the complete set of KubeJS assets and third-party configuration files.
