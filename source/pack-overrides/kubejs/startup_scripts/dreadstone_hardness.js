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
            block.destroySpeed = 50.0
        })
    })
})
