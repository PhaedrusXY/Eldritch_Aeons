// Eldritch Aeons v1.7 EXPERIMENTAL — Dread Incursions, runtime hotfix 4.
// Keep the function-scoped var declarations and explicit loops from hotfix 2.
// Use KubeJS Holder.isTag(identifier), not the hidden Java is(TagKey) overload.
// Keep this filename; do not install a second copy of this scheduler.
// Server-side KubeJS 26.1.2; requires Corail Tombstone and Undead Nights 2.2.5.
//
// Corail Tombstone's Ashes 'n Bones / Bone Crusher milestones (10/50 undead
// kills) unlock and upgrade the assault. The mod's older custom stat is NOT
// registered in Tombstone 9.8.5 on this server, so use Minecraft's persistent
// per-entity kill statistics over undead mob types as a fallback.
// The first event is scheduled 5–10 in-game days AFTER reaching the milestone.
// Following events are independently scheduled 5–10 days apart.
//
// Only one encounter sequence runs at a time across the server, because Undead
// Nights selects its wave template using a GLOBAL setting. We deliberately
// avoid invoking its automatic night-event scheduler, and use the native
// /undeadnights default_horde and spawn_horde commands instead.
//
// Important: the built-in horde difficulty configuration MUST have an empty
// listOfPossibleHordes, or the mod will override the selected template.

var EADIStats = Java.loadClass('net.minecraft.stats.Stats')
var EADIRegistries = Java.loadClass('net.minecraft.core.registries.BuiltInRegistries')
var EADIIdentifier = Java.loadClass('net.minecraft.resources.Identifier')
var EADIUUID = Java.loadClass('java.util.UUID')

var EADI_NEXT_DAY = 'ea_dread_incursion_next_day'
var EADI_UNLOCK_NOTIFIED = 'ea_dread_incursion_unlocked'
var EADI_SECOND_NOTIFIED = 'ea_dread_incursion_second_wave_unlocked'
var EADI_TICK_CHECK = 200             // Every ten seconds; not per-tick player scans
var EADI_SECOND_WAVE_DELAY = 1800    // Ninety seconds
var EADI_MIN_FIRST_WAVE_TIME = 13000
var EADI_MAX_FIRST_WAVE_TIME = 18200
var EADI_MIN_SECOND_WAVE_TIME = 13000
var EADI_MAX_SECOND_WAVE_TIME = 22400
var EADI_STAT_ID = 'tombstone:undead_killed'
// Types to count even if a mod does not add them to #minecraft:undead.
// Avoid counting projectiles; the Lich skull, for example, is not a mob.
var EADI_EXTRA_UNDEAD = [
  'minecraft:zombie', 'minecraft:zombie_villager', 'minecraft:husk',
  'minecraft:drowned', 'minecraft:skeleton', 'minecraft:stray',
  'minecraft:bogged', 'minecraft:wither_skeleton', 'minecraft:phantom',
  'minecraft:zombified_piglin', 'minecraft:wither',
  'minecraft:zombie_horse', 'minecraft:skeleton_horse',
  'iceandfire:dread_thrall', 'iceandfire:dread_ghoul',
  'iceandfire:dread_beast', 'iceandfire:dread_scuttler',
  'iceandfire:dread_knight', 'iceandfire:dread_lich'
]

var eaDreadStat = null
var eaDreadUndeadTypes = null
var eaDreadLogOnce = false
var eaDreadModeAnnounced = false
var eaDreadDisabled = false
var eaDreadReadyAnnounced = false

function eaDreadReport(error) {
  if (eaDreadLogOnce) return
  eaDreadLogOnce = true
  console.error('[Eldritch Aeons] Dread Incursion scheduler disabled after error: ' + String(error))
}

// Do not try this during script evaluation. KubeJS starts loading server
// scripts before the world's registries/tags have finished reloading.
function eaDreadResolveSources() {
  if (eaDreadUndeadTypes != null) return
  var candidates = []
  var typeIterator = EADIRegistries.ENTITY_TYPE.iterator()
  while (typeIterator.hasNext()) {
    var entityType = typeIterator.next()
    var registryId = String(EADIRegistries.ENTITY_TYPE.getKey(entityType))
    var undead = EADI_EXTRA_UNDEAD.indexOf(registryId) >= 0
    if (!undead) {
      // KubeJS hides Holder.is(TagKey). Its exposed is(...) expects a registry
      // identifier; passing EntityTypeTags.UNDEAD there causes the TagKey-to-ID
      // conversion error. isTag accepts a bare tag ID, without '#'.
      undead = entityType.builtInRegistryHolder().isTag('minecraft:undead')
    }
    if (undead) candidates.push(entityType)
  }
  if (candidates.length === 0) {
    // This is a genuine problem (not just Tombstone changing a stat ID).
    throw new Error('No undead entity types were found in the Minecraft registry')
  }
  eaDreadUndeadTypes = candidates

  try {
    var parsed = EADIIdentifier.parse(EADI_STAT_ID)
    var registered = EADIRegistries.CUSTOM_STAT.get(parsed)
    if (registered != null && String(registered) === EADI_STAT_ID) {
      eaDreadStat = EADIStats.CUSTOM.get(registered)
    }
  } catch (ignored) {}

  if (!eaDreadModeAnnounced) {
    eaDreadModeAnnounced = true
    console.info('[Eldritch Aeons] Dread Incursions hotfix 4: tracking ' +
      candidates.length + ' undead mob types via persistent vanilla kill statistics' +
      (eaDreadStat != null ? ' (with Tombstone statistic when available).' : '.'))
  }
}

function eaDreadKills(player) {
  var sum = 0
  for (var typeIndex = 0; typeIndex < eaDreadUndeadTypes.length; typeIndex++) {
    var entityType = eaDreadUndeadTypes[typeIndex]
    // One mod registering a nonstandard entity must not break the scheduler.
    try { sum += Number(player.stats.getKilled(entityType)) || 0 } catch (ignored) {}
  }
  if (eaDreadStat == null) return sum
  // Corail's custom count can include a few additional undead definitions.
  var tombstoneCount = Number(player.stats.get(eaDreadStat)) || 0
  return Math.max(sum, tombstoneCount)
}

function eaDreadStage(kills) {
  if (kills >= 50) return 2
  if (kills >= 10) return 1
  return 0
}

function eaDreadDelayDays() {
  return 5 + Math.floor(Math.random() * 6)
}

function eaDreadDay(server) {
  return Math.floor(Number(server.getOverworld().getOverworldClockTime()) / 24000)
}

function eaDreadTime(server) {
  var t = Number(server.getOverworld().getOverworldClockTime()) % 24000
  if (t < 0) t += 24000
  return t
}

function eaDreadEligibleSurface(player) {
  if (player == null || !player.isAlive()) return false
  if (String(player.level.dimension) !== 'minecraft:overworld') return false
  // Do not spawn a surface army while the player is deep underground or inside.
  return player.level.canSeeSky(player.blockPosition().above())
}

function eaDreadSetNext(player, currentDay) {
  var delay = eaDreadDelayDays()
  player.persistentData.putInt(EADI_NEXT_DAY, currentDay + delay)
  return delay
}

function eaDreadRunWave(server, player, hordeId) {
  // Important: commands are registered with the player requirement and thus
  // cannot be invoked directly from the server console. `execute as` preserves
  // console permission but changes the command source to the target player.
  var name = String(player.getName().getString())
  if (!/^[A-Za-z0-9_]{3,16}$/.test(name)) return false
  server.runCommandSilent('execute as ' + name + ' at ' + name + ' run undeadnights default_horde ' + hordeId)
  server.runCommandSilent('execute as ' + name + ' at ' + name + ' run undeadnights spawn_horde @s')
  return true
}

function eaDreadWarn(player, wave, stage) {
  if (wave === 1) {
    player.tell(stage === 1
      ? '§4The dead gather beyond your sight. A Dreadguard approaches.'
      : '§4An army of the dead approaches. Its master is not far behind.')
  } else {
    player.tell('§5A Dread Lich has come to command the second assault!')
  }
}

// In-memory only: an interrupted assault is abandoned on restart, not replayed.
// Each player's NEXT invasion date is stored in persistent player data before
// the first wave is requested, avoiding restart duplication.
var eaDreadActive = null
var eaDreadTicks = 0

function eaDreadCheck(event) {
  eaDreadResolveSources()
  var server = event.server
  var currentDay = eaDreadDay(server)
  var tod = eaDreadTime(server)
  // KubeJS 8.0.6 renames LevelAccessor.getGameTime to getTime for scripts.
  // This is elapsed world ticks, NOT the day/night clock used above.
  var nowTick = Number(server.getOverworld().getTime())

  // Complete a pending second wave before scheduling any other invasion.
  if (eaDreadActive != null) {
    if (nowTick < eaDreadActive.nextTick) return
    if (eaDreadActive.wave === 2) {
      var target = server.getPlayerList().getPlayer(EADIUUID.fromString(eaDreadActive.uuid))
      if (target != null && eaDreadEligibleSurface(target) &&
          tod >= EADI_MIN_SECOND_WAVE_TIME && tod <= EADI_MAX_SECOND_WAVE_TIME) {
        eaDreadWarn(target, 2, 2)
        eaDreadRunWave(server, target, 2)
      }
    }
    eaDreadActive = null
  }

  var players = server.getPlayerList().getPlayers()
  for (var playerIndex = 0; playerIndex < players.size(); playerIndex++) {
    var player = players.get(playerIndex)
    if (String(player.level.dimension) !== 'minecraft:overworld') continue
    var kills = eaDreadKills(player)
    var stage = eaDreadStage(kills)
    if (stage === 0) continue
    var data = player.persistentData

    if (!data.getBooleanOr(EADI_UNLOCK_NOTIFIED, false)) {
      data.putBoolean(EADI_UNLOCK_NOTIFIED, true)
      player.tell('§8Ashes and bones stir. The Dread will begin to seek you.')
    }
    if (stage === 2 && !data.getBooleanOr(EADI_SECOND_NOTIFIED, false)) {
      data.putBoolean(EADI_SECOND_NOTIFIED, true)
      player.tell('§8The dead have noticed your slaughter. Their Liches now lead the hunt.')
    }

    var nextDay = data.getIntOr(EADI_NEXT_DAY, 0)
    if (nextDay <= 0) {
      eaDreadSetNext(player, currentDay)
      continue
    }
    if (currentDay < nextDay) continue
    if (tod < EADI_MIN_FIRST_WAVE_TIME || tod > EADI_MAX_FIRST_WAVE_TIME) continue
    if (!eaDreadEligibleSurface(player)) continue

    // Reserve next encounter BEFORE dispatching the initial wave so server
    // restarts/temporary errors do not turn one invasion into a spawn loop.
    eaDreadSetNext(player, currentDay)
    eaDreadWarn(player, 1, stage)
    if (!eaDreadRunWave(server, player, 1)) continue

    if (stage === 2) {
      eaDreadActive = {
        uuid: String(player.getUUID()),
        wave: 2,
        nextTick: nowTick + EADI_SECOND_WAVE_DELAY
      }
    } else {
      // A brief global cooldown; prevents simultaneous global template swaps.
      eaDreadActive = {uuid: String(player.getUUID()), wave: 0, nextTick: nowTick + 1200}
    }
    break
  }
}

ServerEvents.tick(function (event) {
  eaDreadTicks++
  if (eaDreadTicks % EADI_TICK_CHECK !== 0 || eaDreadDisabled) return

  // Fail closed BEFORE crossing the Java/Rhino boundary. Some host-side
  // conversion failures can escape a JavaScript catch; if that happens, the
  // latch remains set, rather than repeating the bad call every ten seconds.
  // Normal returns (including no players, daytime and cooldown) re-arm it.
  eaDreadDisabled = true
  try {
    eaDreadCheck(event)
    eaDreadDisabled = false
    if (!eaDreadReadyAnnounced) {
      eaDreadReadyAnnounced = true
      console.info('[Eldritch Aeons] Dread Incursions hotfix 4: scheduler ready; registry and clock checks passed.')
    }
  } catch (error) {
    eaDreadUndeadTypes = null
    eaDreadActive = null
    eaDreadReport(error)
  }
})
