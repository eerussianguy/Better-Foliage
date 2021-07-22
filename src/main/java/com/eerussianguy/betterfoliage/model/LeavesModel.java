package com.eerussianguy.betterfoliage.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import com.mojang.datafixers.util.Pair;

public record LeavesModel(ResourceLocation leaves, ResourceLocation fluff, ResourceLocation overlay, boolean tintLeaves, boolean tintOverlay) implements IModelGeometry<LeavesModel>
{
    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation)
    {
        return new LeavesBakedModel(modelLocation, leaves, fluff, overlay, tintLeaves, tintOverlay);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        ResourceLocation atlas = TextureAtlas.LOCATION_BLOCKS;
        return Arrays.asList(new Material(atlas, leaves), new Material(atlas, fluff));
    }
}
