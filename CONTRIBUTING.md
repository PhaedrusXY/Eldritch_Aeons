# Contributing and Reporting Issues

This repository primarily tracks a curated modpack rather than a general-purpose software library. Useful contributions are reproducible bug reports, compatibility findings, documentation corrections, tested balance observations, and narrowly scoped pull requests.

## Bug reports

Before opening an issue:

1. Reproduce the problem with the exact published pack version.
2. Remove manually added mods, resource packs, shaders, and client utilities unless the report is specifically about compatibility with one of them.
3. Determine whether the problem occurs in single-player, on a dedicated server, or both.
4. Preserve the relevant `latest.log`, `debug.log`, and crash report.

Include exact steps, expected behavior, actual behavior, and screenshots when the interface matters.

## Balance reports

Describe the player's equipment, progression stage, difficulty, party size, and whether commands or creative-mode items were used. “Too hard” and “too easy” are difficult to act on without this context.

## Pull requests

Keep changes narrow. Do not commit third-party mod JARs, full world saves, personal map data, authentication information, server secrets, or generated logs. Changes to research or startup item registration generally require a full client/server restart for testing.
