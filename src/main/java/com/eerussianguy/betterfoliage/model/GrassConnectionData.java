package com.eerussianguy.betterfoliage.model;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * Which sides of a grass block should have their overlay pulled up over the neighbour, and whether
 * there is room above for a tuft of grass.
 *
 * Previously this rode along as a {@code ModelProperty} attached in {@code getModelData}; the
 * dynamic block state model gets the level and {@link BlockPos} directly, so it is now computed
 * on demand.
 *
 * @param meta a N/E/S/W bitmask, indexing the 16 pre-baked connection variants
 * @param up   whether the block above is air or snow
 */
public record GrassConnectionData(int meta, boolean up)
{
    public static GrassConnectionData of(BlockAndTintGetter level, BlockPos pos)
    {
        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        final BlockPos down = pos.below();

        int meta = 0;
        if (snowy(level, mutable.setWithOffset(down, Direction.NORTH))) meta |= 1;
        if (snowy(level, mutable.setWithOffset(down, Direction.EAST))) meta |= 2;
        if (snowy(level, mutable.setWithOffset(down, Direction.SOUTH))) meta |= 4;
        if (snowy(level, mutable.setWithOffset(down, Direction.WEST))) meta |= 8;

        final BlockState upState = level.getBlockState(mutable.setWithOffset(pos, Direction.UP));
        return new GrassConnectionData(meta, upState.isAir() || upState.is(Blocks.SNOW));
    }

    private static boolean snowy(BlockAndTintGetter level, BlockPos pos)
    {
        return level.getBlockState(pos).hasProperty(BlockStateProperties.SNOWY);
    }

    /** N, E, S, W - whether that side connects to a neighbouring grass-like block. */
    public static boolean[] sidesFromMeta(int meta)
    {
        return new boolean[] {
            (meta & 1) > 0,
            (meta & 2) > 0,
            (meta & 4) > 0,
            (meta & 8) > 0
        };
    }
}
