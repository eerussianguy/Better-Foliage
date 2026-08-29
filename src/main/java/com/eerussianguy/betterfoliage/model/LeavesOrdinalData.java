package com.eerussianguy.betterfoliage.model;

import java.util.Random;

import net.minecraft.core.BlockPos;

/**
 * Picks which pre-baked fluff cross a leaves block uses, from its position.
 *
 * Previously this rode along as a {@code ModelProperty} attached in {@code getModelData}; the
 * dynamic block state model gets the {@link BlockPos} directly, so it is now just a function.
 */
public final class LeavesOrdinalData
{
    private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(Random::new);

    public static int ordinal(BlockPos pos, int count)
    {
        final Random random = RANDOM.get();
        random.setSeed(pos.asLong() * 524287L);
        return random.nextInt(count);
    }

    private LeavesOrdinalData() {}
}
