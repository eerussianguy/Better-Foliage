package com.eerussianguy.betterfoliage.model;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;


import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import com.eerussianguy.betterfoliage.Helpers;
import com.mojang.datafixers.util.Pair;

public record GrassModel(ResourceLocation dirt, ResourceLocation top, ResourceLocation overlay, boolean tint, ResourceLocation grassLocation) implements IUnbakedGeometry<GrassModel>
{
    @Override
    public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
    {
        return new GrassBakedModel(modelLocation, dirt, top, overlay, tint, grassLocation);
    }

    @Override
    public Collection<Material> getMaterials(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        return Helpers.makeMaterials(dirt, top, overlay);
    }
}
