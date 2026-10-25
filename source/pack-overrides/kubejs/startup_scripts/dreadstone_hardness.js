BlockEvents.modification(event => {
    const dreadstoneBlocks = [
        'iceandfire:dread_stone',
        'iceandfire:dread_stone_bricks',
        'iceandfire:dread_stone_bricks_chiseled',
        'iceandfire:dread_stone_bricks_cracked',
        'iceandfire:dread_stone_bricks_mossy',
        'iceandfire:dread_stone_tile',
        'iceandfire:dread_stone_slab',
        'iceandfire:dread_stone_stairs',
        'iceandfire:dread_stone_face'
    ]

    dreadstoneBlocks.forEach(id => {
        event.modify(id, block => {
            // Eldritch Aeons: Dreadstone is a true progression gate.
            // Negative destroy speed makes it unbreakable to normal mining and
            // also causes Build a Spell's Break effect to reject it.
            block.destroySpeed = -1.0
        })
    })
})
