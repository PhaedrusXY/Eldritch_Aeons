# Release Checklist

## Source and content

- [ ] Update `CHANGELOG.md` and the pack version.
- [ ] Export the current CurseForge profile and verify its `manifest.json`.
- [ ] Confirm the release contains the intended `config/`, `defaultconfigs/`, `kubejs/`, and quest data.
- [ ] Exclude saves, backups, logs, crash reports, authentication data, personal waypoints, and development-only output.
- [ ] Confirm the latest Thaumic Storage Scanning JAR is included on both client and server.

## Clean-client test

- [ ] Import the exported ZIP into a new CurseForge profile.
- [ ] Reach the main menu without adding local files.
- [ ] Create a new world and verify quests, starting materials, and guide/research books.
- [ ] Verify Arcane Syntax research, Worn Wand, Blank Rune, and one learned component.
- [ ] Verify Thaumometer scanning in a Tom's terminal.
- [ ] Verify an Occultism ritual inscription.
- [ ] Verify a Thaumaturge infusion inscription.
- [ ] Verify Draconic Artifice research and JEI recipes.

## Dedicated-server test

- [ ] Start a clean server using the matching server pack.
- [ ] Join with a clean client and verify registry synchronization.
- [ ] Run `/kubethaum check`.
- [ ] Review KubeJS, NeoForge, and server logs for new errors.
- [ ] Test one player death/reconnect and one server restart.

## CurseForge listing

- [ ] Upload the 512×512 PNG icon supplied with the release assets; the repository SVG is the scalable source artwork.
- [ ] Use the summary and description in `docs/CURSEFORGE_LISTING.md`.
- [ ] Select Magic, Adventure and RPG, Exploration, Quests, and Multiplayer categories as appropriate.
- [ ] Mark the file beta while upstream dependencies remain in beta/alpha states.
- [ ] Add the GitHub repository as the source/issue-tracker link.
