package com.eerussianguy.betterfoliage.model;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraftforge.client.model.data.IDynamicBakedModel;

public abstract class BFBakedModel implements IDynamicBakedModel
{
    @Override
    public boolean useAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return true;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides()
    {
        return ItemOverrides.EMPTY;
    }
}
