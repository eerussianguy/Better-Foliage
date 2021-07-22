package com.eerussianguy.betterfoliage.model;

import java.util.Random;

import net.minecraft.core.BlockPos;

import com.eerussianguy.betterfoliage.BFConfig;

public class LeavesOrdinalData implements IModelDataBlank
{
    private static final Random RANDOM = new Random();

    public int ordinal;

    public LeavesOrdinalData(BlockPos pos)
    {
        RANDOM.setSeed(pos.asLong() * 524287L); // It's an extra long long (this is actually very important to do)
        ordinal = RANDOM.nextInt((int) Math.pow(BFConfig.CLIENT.leavesCacheSize.get(), 3));
    }

    public int get()
    {
        return ordinal;
    }
}
