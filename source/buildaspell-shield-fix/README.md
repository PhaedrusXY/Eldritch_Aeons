# Build A Spell Shield Fix

A minimal NeoForge 26.1.2 compatibility mod for Build A Spell 1.0.3.

## What it changes

Build A Spell currently calculates Shield absorption as:

```text
min(current absorption + new Shield amount, configured cap)
```

That lets a very cheap Shield be cast repeatedly until it reaches the same cap as a stronger spell.
This patch changes the result to:

```text
max(current absorption, min(new Shield amount, configured cap))
```

A stronger Shield upgrades a weaker one. Equal or weaker recasts do not add more absorption.

## Performance

The patch has no tick handler. It redirects one `Math.min(float, float)` call only while Build A
Spell's Shield effect is being executed.

## Existing max-absorption workaround

This mod fixes stacking only. Eldritch Aeons should retain its existing
`kubejs/server_scripts/buildaspell_shield_fix.js`, which raises players' `MAX_ABSORPTION` base value
on login and respawn so Shield can grant absorption in the first place.

## Compatibility

- Minecraft 26.1.2
- NeoForge 26.1.2.122 or newer within the 26.1.2 line
- Build A Spell 1.0.3
