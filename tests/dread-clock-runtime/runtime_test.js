// TEST ONLY. Appended to the candidate in the disposable CI server; not shipped.
var EADITestDone = false
var EADITestChecks = 0
function eaDreadTestAssert(value, message) {
  if (!value) throw new Error('DREAD_RUNTIME_ASSERT: ' + message)
  EADITestChecks++
  console.info('[DREAD_RUNTIME_TEST] PASS ' + message)
}
ServerEvents.tick(function (event) {
  if (EADITestDone || eaDreadTicks < 240) return
  EADITestDone = true
  var realServer = event.server
  var savedDay = eaDreadDay
  var savedTod = eaDreadTime
  try {
    eaDreadTestAssert(!eaDreadDisabled, 'natural tick callback succeeds after initialization')
    eaDreadTestAssert(eaDreadReadyAnnounced, 'readiness message reached')
    var world = realServer.getOverworld()
    eaDreadTestAssert(isFinite(Number(world.getTime())), 'real KubeJS elapsed-time accessor')
    eaDreadTestAssert(isFinite(eaDreadDay(realServer)), 'real Overworld day clock')
    eaDreadTestAssert(isFinite(eaDreadTime(realServer)), 'real Overworld time-of-day clock')
    eaDreadTestAssert(eaDreadUndeadTypes.length > 0, 'real entity registry and undead tags')
    eaDreadTestAssert(realServer.getPlayerList().getPlayers().size() === 0, 'real server player-list API')
    var FakePlayerFactory = Java.loadClass('net.neoforged.neoforge.common.util.FakePlayerFactory')
    var GameProfile = Java.loadClass('com.mojang.authlib.GameProfile')
    var EntityType = Java.loadClass('net.minecraft.world.entity.EntityType')
    var ArrayList = Java.loadClass('java.util.ArrayList')
    var testId = EADIUUID.fromString('9b0f886c-7539-455a-a9ad-6f8fc1525004')
    eaDreadTestAssert(realServer.getPlayerList().getPlayer(testId) == null, 'real UUID player-lookup API')
    var player = FakePlayerFactory.get(world, new GameProfile(testId, 'DreadTest'))
    world.getChunk(0, 0)
    player.setPos(0.5, 200, 0.5)
    eaDreadTestAssert(String(player.getName().getString()) === 'DreadTest', 'real player name API')
    eaDreadTestAssert(String(player.getUuid()) === String(testId), 'real player UUID API')
    eaDreadTestAssert(String(player.level.dimension) === 'minecraft:overworld', 'real player dimension property')
    eaDreadTestAssert(eaDreadEligibleSurface(player), 'real surface and alive checks')
    var data = player.persistentData
    data.putBoolean(EADI_UNLOCK_NOTIFIED, false)
    data.putBoolean(EADI_SECOND_NOTIFIED, false)
    data.putInt(EADI_NEXT_DAY, 0)
    eaDreadTestAssert(data.getIntOr(EADI_NEXT_DAY, 99) === 0, 'real persistent integer read/write')
    eaDreadTestAssert(!data.getBooleanOr(EADI_UNLOCK_NOTIFIED, true), 'real persistent boolean read/write')
    var zombies = EADIStats.ENTITY_KILLED.get(EntityType.ZOMBIE)
    player.stats.set(zombies, 9)
    eaDreadTestAssert(eaDreadKills(player) === 9, 'real persistent entity-kill statistics')
    var testPlayers = new ArrayList()
    testPlayers.add(player)
    var commands = []
    var facade = {
      getOverworld: function () { return world },
      getPlayerList: function () { return {
        getPlayers: function () { return testPlayers },
        getPlayer: function (id) { return String(id) === String(testId) ? player : null }
      } },
      runCommandSilent: function (command) { commands.push(String(command)) }
    }
    // Only the selected day/time and online-player list are controlled.
    // World, FakePlayer, tag holders, NBT, and statistic APIs are real objects.
    // Command dispatch is recorded, not sent to Undead Nights in this minimal test.
    eaDreadDay = function () { return 100 }
    eaDreadTime = function () { return 14000 }
    eaDreadCheck({server:facade})
    eaDreadTestAssert(data.getIntOr(EADI_NEXT_DAY, 0) === 0, 'nine kills do not unlock')
    player.stats.set(zombies, 10)
    eaDreadCheck({server:facade})
    var due = data.getIntOr(EADI_NEXT_DAY, 0)
    eaDreadTestAssert(due >= 105 && due <= 110, 'existing ten kills schedule first event five to ten days later')
    eaDreadTestAssert(data.getBooleanOr(EADI_UNLOCK_NOTIFIED, false), 'first milestone notification persists')
    player.stats.set(zombies, 50)
    eaDreadCheck({server:facade})
    eaDreadTestAssert(data.getIntOr(EADI_NEXT_DAY, 0) === due, 'fifty kills preserve existing date')
    eaDreadTestAssert(data.getBooleanOr(EADI_SECOND_NOTIFIED, false), 'second milestone notification persists')
    data.putInt(EADI_NEXT_DAY, 99)
    eaDreadCheck({server:facade})
    eaDreadTestAssert(commands.length === 2, 'first wave issues exactly two commands')
    eaDreadTestAssert(commands[0].indexOf('default_horde 1') >= 0, 'first horde selected')
    eaDreadTestAssert(commands[1].indexOf('spawn_horde @s') >= 0, 'first spawn command targets player')
    eaDreadTestAssert(eaDreadActive != null && eaDreadActive.wave === 2, 'second wave pending')
    eaDreadTestAssert(eaDreadActive.nextTick === Number(world.getTime()) + 1800, 'second wave uses same elapsed-tick clock with 1800 tick delay')
    eaDreadCheck({server:facade})
    eaDreadTestAssert(commands.length === 2, 'second wave cannot fire early')
    eaDreadActive.nextTick = Number(world.getTime())
    eaDreadCheck({server:facade})
    eaDreadTestAssert(commands.length === 4 && commands[2].indexOf('default_horde 2') >= 0, 'due second wave resolves target and issues horde two')
    eaDreadTestAssert(eaDreadActive == null, 'completed encounter clears active state')
    eaDreadCheck({server:facade})
    eaDreadTestAssert(commands.length === 4, 'future date prevents immediate replay')
    console.info('DREAD_RUNTIME_TEST_SUCCESS checks=' + EADITestChecks)
  } catch (error) {
    console.error('DREAD_RUNTIME_TEST_FAILURE after=' + EADITestChecks + ' error=' + String(error))
    if (error.stack) console.error(String(error.stack))
  } finally {
    eaDreadDay = savedDay
    eaDreadTime = savedTod
  }
})
