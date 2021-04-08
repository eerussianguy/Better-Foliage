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

public class GrassModel implements IModelGeometry<GrassModel>
{
    private final ResourceLocation dirt;
    private final ResourceLocation top;
    private final ResourceLocation overlay;
    private final boolean tint;

    public GrassModel(ResourceLocation dirt, ResourceLocation top, ResourceLocation overlay, boolean tint)
    {
        this.dirt = dirt;
        this.top = top;
        this.overlay = overlay;
        this.tint = tint;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation)
    {
        return new GrassBakedModel(modelLocation, dirt, top, overlay, tint);
    }

    @Override
    @SuppressWarnings("deprecation")
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors)
    {
        ResourceLocation atlas = AtlasTexture.LOCATION_BLOCKS;
        return Arrays.asList(new RenderMaterial(atlas, dirt), new RenderMaterial(atlas, top), new RenderMaterial(atlas, overlay));
    }
}
