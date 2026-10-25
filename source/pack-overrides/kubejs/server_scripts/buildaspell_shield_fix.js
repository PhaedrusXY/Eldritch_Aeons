const Attributes = Java.loadClass(
    'net.minecraft.world.entity.ai.attributes.Attributes'
)

function ensureShieldCapacity(player) {
    const attribute = player.getAttribute(Attributes.MAX_ABSORPTION)

    if (attribute != null && attribute.getBaseValue() < 20.0) {
        attribute.setBaseValue(20.0)
    }
}

PlayerEvents.loggedIn(event => {
    ensureShieldCapacity(event.player)
})

PlayerEvents.respawned(event => {
    ensureShieldCapacity(event.player)
})
