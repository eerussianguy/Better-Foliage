package com.eerussianguy.betterfoliage;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

public class Helpers
{
    public static final Direction[] DIRECTIONS = Direction.values();

    public static ResourceLocation identifier(String name)
    {
        return new ResourceLocation(MOD_ID, name);
    }

    public static final ResourceLocation EMPTY = identifier("empty");

    public static final BlockFaceUV UV_DEFAULT = new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0);;

    public static BlockElementFace makeTintedFace(BlockFaceUV uv)
    {
        return new BlockElementFace(null, 0, "", uv);
    }

    public static BlockElementFace makeFace(BlockFaceUV uv)
    {
        return new BlockElementFace(null, -1, "", uv);
    }

    public static ResourceLocation getOrEmpty(JsonObject json, String member)
    {
        if (!json.has(member)) return EMPTY;
        return new ResourceLocation(json.get(member).getAsString());
    }

    public static Collection<Material> makeMaterials(ResourceLocation... textures)
    {
        //noinspection deprecation
        return Arrays.stream(textures).map(texture -> new Material(TextureAtlas.LOCATION_BLOCKS, texture)).collect(Collectors.toList());
    }

    public static BakedQuad makeBakedQuad(BlockElement BlockElement, BlockElementFace partFace, TextureAtlasSprite atlasSprite, Direction dir, BlockModelRotation modelRotation, ResourceLocation modelResLoc)
    {
        return new FaceBakery().bakeQuad(BlockElement.from, BlockElement.to, partFace, atlasSprite, dir, modelRotation, BlockElement.rotation, true, modelResLoc);
    }

    public static void assembleFaces(SimpleBakedModel.Builder builder, BlockElement part, TextureAtlasSprite sprite, ResourceLocation modelLocation)
    {
        for (Map.Entry<Direction, BlockElementFace> e : part.faces.entrySet())
        {
            Direction d = e.getKey();
            builder.addCulledFace(d, Helpers.makeBakedQuad(part, e.getValue(), sprite, d, BlockModelRotation.X0_Y0, modelLocation));
        }
    }

    public static void assembleFacesConditional(SimpleBakedModel.Builder builder, BlockElement part, Function<Direction, TextureAtlasSprite> getter, ResourceLocation modelLocation)
    {
        for (Map.Entry<Direction, BlockElementFace> e : part.faces.entrySet())
        {
            Direction d = e.getKey();
            builder.addCulledFace(d, Helpers.makeBakedQuad(part, e.getValue(), getter.apply(d), d, BlockModelRotation.X0_Y0, modelLocation));
        }
    }

    @SuppressWarnings("deprecation")
    public static TextureAtlasSprite getTexture(ResourceLocation location)
    {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
    }

    /**
     * Equivalent of a linspace function in numpy or MATLAB or what have you
     *
     * @author AlcatrazEscapee
     */
    public static float[] intervals(int n, float min, float max)
    {
        float[] f = new float[n];
        for (int i = 0; i < n; i++)
        {
            float t = (float) i / (n - 1);
            f[i] = min * t + max * (1 - t);
        }
        return f;
    }
}
