# Eldritch Aeons v1.3 pack-owned overrides

This directory records the pack-owned compatibility fixes and tuning notes from the v1.3 release.

Readable source files included here:

- `kubejs/server_scripts/buildaspell_shield_fix.js` — gives players nonzero max-absorption capacity so Build a Spell Shield can actually set absorption hearts.
- `kubejs/startup_scripts/dreadstone_hardness.js` — makes Dreadstone truly unbreakable with destroy speed -1, including against Build a Spell Break.
- `kubejs/data/iceandfire/loot_table/entities/dread_lich.json` — preserves normal Lich loot but guarantees one Dread Key on player kill.
- `LICH_TUNING.md` — records the Dread Lich spawn change used to make the intended key-gated Mausoleum path practical.
- `BUILDASPELL_TUNING.md` — the key v1.3 balance values that differ materially from Build a Spell defaults.

Release metadata is under `packages/`. The CurseForge release remains authoritative for the complete set of KubeJS assets and third-party configuration files.
