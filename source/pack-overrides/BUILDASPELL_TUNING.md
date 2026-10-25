# Build a Spell tuning — Eldritch Aeons v1.2

This file records the material balance changes in the v1.2 release. The checksum file under `packages/` identifies the exact TOML files used.

## General

- Spell base range: **2.0** (default 5.0).
- Maximum spell components: **100**.
- Stock Blank Rune progression effectively disabled:
  - `essenceRequired = 100000`
  - `killEssence = 0`
  - `bossEssence = 0`
  - `castEssenceRatio = 0.0`
  - no starter deliveries/effects
  - no starter guidebook

## Core effects

- Damage multiplier: **2.0**.
- Break base mana cost: **10**.
- Break Fill radius: **4**, +**2** per Fill.
- Break Fill block budget: **128**.
- Light duration: **12,000 ticks** (10 minutes).

## Shared timed effects

The shared area-buff family uses:

- base duration **600 ticks** (30 seconds);
- +**200 ticks** per Increased Power;
- +**600 ticks** per Prolonged.

Individual hostile/control durations are tuned separately; they are intentionally shorter.

## Combo highlights

- Creative Flight: **1,200 ticks** base (60 seconds), +300 per power, +1,200 per Prolonged.
- Black Hole: **160 ticks** base.
- Tornado: **200 ticks** base.
- Vex/Skeleton/Vindicator summons: **120 seconds** base, +60 seconds per Duration.
- Fortress: **600 ticks** base.
- Sanctuary: **1,200 ticks** base.
- Lightning Storm: **240 ticks** base.
- Blizzard: **300 ticks** base.

## Deliveries

- Tracking remains at the upstream v1.2 release setting of **16-block homing range** and **0.3 homing strength**. A longer range was discussed after dragon testing but was **not** part of the uploaded v1.2 build.
- Touch duration: **600 ticks**.
- Trap lifetime: **6,000 ticks** (5 minutes), +1,200 per Duration.

## Wands

- Worn: 10% mana reduction, +6 Spell Power.
- Carved: 25% mana reduction, +15 Spell Power.
- Runic: 40% mana reduction, +30 Spell Power.

## Compatibility fixes

See the readable KubeJS files beside this document:

- Shield: ensure `minecraft:max_absorption` base capacity is at least 20.
- Dreadstone: set all relevant Ice and Fire Dreadstone variants to destroy speed -1. Build a Spell Break explicitly rejects negative destroy speed, so the Mausoleum cannot be tunneled through with spell mining.
