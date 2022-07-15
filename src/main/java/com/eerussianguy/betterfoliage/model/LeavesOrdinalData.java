package com.eerussianguy.betterfoliage.model;

import java.util.Random;

import net.minecraft.core.BlockPos;

import net.minecraftforge.client.model.data.ModelProperty;

import com.eerussianguy.betterfoliage.BFConfig;

public class LeavesOrdinalData
{
    public static final ModelProperty<LeavesOrdinalData> PROPERTY = new ModelProperty<>();
    private static final Random RANDOM = new Random();

    public int ordinal;

    public LeavesOrdinalData(BlockPos pos)
    {
        RANDOM.setSeed(pos.asLong() * 524287L);
        ordinal = RANDOM.nextInt((int) Math.pow(BFConfig.CLIENT.leavesCacheSize.get(), 3));
    }

    public int get()
    {
        return ordinal;
    }

}
