from mcresources import ResourceManager


def generate(rm: ResourceManager):
    vanilla_woods = ('oak', 'acacia', 'dark_oak', 'birch', 'jungle', 'spruce', 'azalea', 'flowering_azalea')
    tfc_woods = ('acacia', 'ash', 'aspen', 'birch', 'blackwood', 'chestnut', 'douglas_fir', 'hickory', 'kapok',
                 'maple', 'oak', 'pine', 'rosewood', 'sequoia', 'spruce', 'sycamore', 'white_cedar', 'willow')  # palm omitted
    tfc_fruits = ('cherry', 'green_apple', 'lemon', 'olive', 'orange', 'peach', 'plum', 'red_apple')

    for wood in vanilla_woods:
        leaves_model(rm, 'minecraft:%s_leaves' % wood, 'minecraft:block/%s_leaves' % wood, 'betterfoliage:block/%s_fluff' % wood)
    for wood in tfc_woods:
        leaves_model(rm, 'tfc:wood/leaves/%s' % wood, 'tfc:block/wood/leaves/%s' % wood, 'tfc:block/wood/leaves/%s_fluff' % wood)
        # compat for vexxels pack
        leaves_model(rm, 'tfc:wood/leaves/mirrored/%s' % wood, 'tfc:block/wood/leaves/%s' % wood, 'tfc:block/wood/leaves/%s_fluff' % wood)
    for fruit in tfc_fruits:
        for life in ('', '_fruiting', '_flowering', '_dry'):
            leaves_model(rm, 'tfc:plant/%s%s_leaves' % (fruit, life), 'tfc:block/fruit_tree/%s%s_leaves' % (fruit, life), 'betterfoliage:block/tfc/%s%s_leaves_fluff' % (fruit, life))

    pad = 0
    for flower in range(0, 1 + 1):
        for root in range(0, 2 + 1):
            rm.block_model('betterfoliage:lily_pad%d' % pad, parent='betterfoliage:block/lily_pad', textures={
                'flower': 'betterfoliage:block/lilypad_flower%d' % flower,
                'roots': 'betterfoliage:block/lilypad_roots%d' % root
            })
            pad += 1

    rm.block_model('cactus', parent='minecraft:block/cactus', no_textures=True)
    cactus_variants = [
        {'model': 'betterfoliage:block/cactus%s' % i, 'weight': w, 'y': r}
        for i, w in (('', 3), ('1', 2), ('2', 4), ('3', None), ('4', None), ('5', None))
        for r in (None, 90, 180, 270)]
    rm.blockstate('minecraft:cactus', variants={"": cactus_variants}, use_default_model=False)

    rm.blockstate('minecraft:grass_block', variants={
        'snowy=false': {'model': 'betterfoliage:block/grass_block'},
        'snowy=true': {'model': 'betterfoliage:block/snowy_grass_block'}
    })

    rm.blockstate('minecraft:mycelium', variants={
        'snowy=false': {'model': 'betterfoliage:block/mycelium'},
        'snowy=true': {'model': 'betterfoliage:block/snowy_grass_block'}
    })

    rm.blockstate('minecraft:podzol', variants={
        'snowy=false': {'model': 'betterfoliage:block/podzol'},
        'snowy=true': {'model': 'betterfoliage:block/snowy_grass_block_no_grass'}
    })

    rm.custom_block_model('betterfoliage:grass_block', 'betterfoliage:grass', {
        'dirt': 'minecraft:block/dirt',
        'top': 'minecraft:block/grass_block_top',
        'overlay': 'minecraft:block/grass_block_side_overlay',
        'tint': True,
        'grass': 'betterfoliage:block/better_grass'
    })

    rm.custom_block_model('betterfoliage:snowy_grass_block', 'betterfoliage:grass', {
        'dirt': 'minecraft:block/dirt',
        'top': 'minecraft:block/snow',
        'overlay': 'minecraft:block/grass_block_snow',
        'tint': False,
        'grass': 'betterfoliage:block/better_grass_snowed'
    })

    rm.custom_block_model('betterfoliage:snowy_grass_block_no_grass', 'betterfoliage:grass', {
        'dirt': 'minecraft:block/dirt',
        'top': 'minecraft:block/snow',
        'overlay': 'minecraft:block/grass_block_snow',
        'tint': False,
    })

    rm.custom_block_model('betterfoliage:mycelium', 'betterfoliage:grass', {
        'dirt': 'minecraft:block/dirt',
        'top': 'minecraft:block/mycelium_top',
        'overlay': 'minecraft:block/mycelium_side',
        'tint': False,
        'grass': 'betterfoliage:block/better_mycelium'
    })

    rm.custom_block_model('betterfoliage:podzol', 'betterfoliage:grass', {
        'dirt': 'minecraft:block/dirt',
        'top': 'minecraft:block/podzol_top',
        'overlay': 'minecraft:block/podzol_side',
        'tint': False
    })

    rm.block_model('better_grass', {'cross': 'betterfoliage:block/better_grass'}, parent='betterfoliage:block/tinted_cross_high')
    rm.block_model('better_mycelium', {'cross': 'betterfoliage:block/better_mycelium'}, parent='betterfoliage:block/cross_high')
    rm.block_model('better_grass_snowed', {'cross': 'betterfoliage:block/better_grass_snowed'}, parent='betterfoliage:block/cross_high')

    # enhanced farming
    for fruit in ('apple', 'avocado', 'banana', 'cherry', 'lemon', 'mango', 'olive', 'orange', 'pear'):
        base = 'oak' if fruit != 'banana' else 'jungle'
        leaves_model(rm, 'enhancedfarming:%s_leaves_fruity' % fruit, 'minecraft:block/%s_leaves' % base, 'betterfoliage:block/%s_fluff' % base, 'enhancedfarming:block/leaves/%s_leaves_fruity' % fruit)
        leaves_model(rm, 'enhancedfarming:%s_leaves_blooming' % fruit, 'minecraft:block/%s_leaves' % base, 'betterfoliage:block/%s_fluff' % base, 'enhancedfarming:block/leaves/%s_leaves_blooming' % fruit)


def leaves_model(rm: ResourceManager, model: str, block: str, fluff: str, overlay: str = None):
    rm.custom_block_model(model, 'betterfoliage:leaves', {
        'leaves': block,
        'fluff': fluff,
        'overlay': overlay
    })
