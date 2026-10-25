# Dread Lich / Mausoleum tuning — v1.2

Eldritch Aeons treats the Dread Mausoleum as a key-gated structure rather than a mine-through dungeon.

Ice and Fire's default natural Lich settings in the tested configuration were:

- `spawn = true`
- `spawnWeight = 4`
- `spawnChance = 0.1`

v1.2 changes only `spawnChance` to **0.5**. Spawn weight remains **4**.

The Dread Lich loot table is overridden separately so a player kill always drops exactly one Dread Key. The existing bone and Dread Shard rolls are preserved.

Together with unbreakable Dreadstone, this makes the intended route practical: find and kill a naturally spawned Dread Lich, obtain the key, then enter the Mausoleum through its gate.
