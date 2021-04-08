package com.eerussianguy.betterfoliage.model;

import javax.annotation.Nullable;

import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

/**
 * The manual get and set methods are kinda silly, we can just shuffle stuff around ourselves.
 */
public interface IModelDataBlank extends IModelData
{
    @Override
    default boolean hasProperty(ModelProperty<?> prop)
    {
        return false;
    }

    @Nullable
    @Override
    default <T> T getData(ModelProperty<T> prop)
    {
        return null;
    }

    @Nullable
    @Override
    default <T> T setData(ModelProperty<T> prop, T data)
    {
        return null;
    }
}
