# Installation and Server Setup

## Published pack

Install the approved CurseForge profile and use a dedicated server pack matching that exact file version. This repository is not a replacement launcher package. Historical v1.2/v1.3 manifests remain under `packages/`; the integration work below does not announce a new CurseForge release.

Client/server dependencies, pack-specific KubeJS items, configuration and quest data must match the selected pack version. The development target uses Minecraft 26.1.2, NeoForge 26.1.2.122 and Java 25.

## Experimental Arcane Syntax 0.2.1 update

Current source, installation overlay and test artifact: [Arcane Syntax](https://github.com/PhaedrusXY/Arcane_Syntax).

The incremental overlay assumes the existing 0.2.0 Eldritch Aeons focus migration. It is not a standalone installer for arbitrary or older release profiles.

1. Back up the test instance/world and close Minecraft completely; stop a dedicated server when applicable.
2. Use Thaumaturge 1.1.0 and Build a Spell 1.0.3, with compatible companion mods. The observed test profile includes KubeThaum 1.3.1 and KubeJS 8.0.8; retain Build A Spell Fix 1.1.2.
3. Remove older `arcane_syntax_unified` JARs and merge the replacement `mods/` and `kubejs/` folders. Do not replace whole folders.
4. Remove only the old 1.0.2 Outer Lands template-fix files listed in the update. Keep unrelated research/data overrides.
5. Preserve current configuration. The source repository's `pack/config/` is a reference snapshot, not a mandatory overwrite.
6. Fully restart Minecraft. Install matching integration/dependencies and synchronize the pack layer on both sides for multiplayer. Do not use `/reload` for this migration.

The optional game installer verifies hashes and backs up touched files, not world saves. Its original build-time validation did not include Windows execution. Follow the package's instructions or use manual copying.

## Basic validation

First use the backed-up sandbox for loading and functionality checks. A new world is not required by the intended migration; use a fresh character/world later to test research gates.

- Check the current session's logs for script, registry or Mixin errors.
- Confirm Arcane Syntax and Two Grammars appear in the Thaumonomicon; inspect `/ea_arcane_syntax_status`.
- Confirm initial Cast and subsequent progression use Lesser/Advanced/Greater Foci, not legacy wands.
- Complete an inscription before removing the focus; select it with a keyboard binding, cast with a charged native wand, and verify Vis payment and ordinary right-click input afterward.
- Test Foundation Conjure/Wall/Floor and rejection of higher-tier components with a Lesser Focus.
- Verify Draconic Artifice uses Greater Focus and its infusion/Thaumium feedstock rules remain correct.

An unlock-all character does not validate progression. Restore matching instance and world backups for rollback; do not assume an upgraded save can safely be downgraded.
