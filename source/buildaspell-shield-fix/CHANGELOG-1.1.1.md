## Version 1.1.1 — Fully Standalone Shield Fix

- Moved the Shield absorption-capacity workaround from KubeJS into the mod itself. No KubeJS installation or Shield script is required.
- Automatically ensures a minimum player absorption capacity of 20 on login and respawn, preserving the previous script's behavior without granting free absorption hearts.
- Checks capacity on each actual Shield recipient before applying the spell, including fresh players and non-player targets. Higher configured Shield caps are respected.
- Preserves the existing non-stacking Shield behavior and higher absorption capacity supplied by other mods.
- All Break/Fill, Break/Chain, Floor/Fill, Wall/Fill, and Evaporate Water changes from v1.1.0 are unchanged.

**Upgrading:** Replace the old patch JAR on client and server, remove `kubejs/server_scripts/buildaspell_shield_fix.js`, and fully restart. Keep the original Build A Spell 1.0.3 installed. Do not remove unrelated KubeJS scripts or KubeJS itself from modpacks that still use it.
