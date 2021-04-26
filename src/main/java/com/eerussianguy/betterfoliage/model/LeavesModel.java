package com.eerussianguy.betterfoliage.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import com.mojang.datafixers.util.Pair;


public class LeavesModel implements IModelGeometry<LeavesModel>
{
    private final ResourceLocation leaves;
    private final ResourceLocation fluff;
    private final ResourceLocation overlay;
    private final boolean tintLeaves;
    private final boolean tintOverlay;

    public LeavesModel(ResourceLocation leaves, ResourceLocation fluff, ResourceLocation overlay, boolean tintLeaves, boolean tintOverlay)
    {
        this.leaves = leaves;
        this.fluff = fluff;
        this.overlay = overlay;
        this.tintLeaves = tintLeaves;
        this.tintOverlay = tintOverlay;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        return new LeavesBakedModel(modelLocation, leaves, fluff, overlay, tintLeaves, tintOverlay);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        //noinspection deprecation
        ResourceLocation atlas = AtlasTexture.LOCATION_BLOCKS;
        return Arrays.asList(new RenderMaterial(atlas, leaves), new RenderMaterial(atlas, fluff));
    }
}
