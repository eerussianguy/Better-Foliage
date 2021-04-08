package com.eerussianguy.betterfoliage;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class Helpers
{
    public static final Direction[] DIRECTIONS = Direction.values();

    public static BlockPartFace makeTintedFace(BlockFaceUV uv)
    {
        return new BlockPartFace(null, 0, "", uv);
    }

    public static BlockPartFace makeFace(BlockFaceUV uv)
    {
        return new BlockPartFace(null, -1, "", uv);
    }

    public static BakedQuad makeBakedQuad(BlockPart blockPart, BlockPartFace partFace, TextureAtlasSprite atlasSprite, Direction dir, ModelRotation modelRotation, ResourceLocation modelResLoc)
    {
        return new FaceBakery().bakeQuad(blockPart.from, blockPart.to, partFace, atlasSprite, dir, modelRotation, blockPart.rotation, true, modelResLoc);
    }

    public static void assembleFaces(SimpleBakedModel.Builder builder, BlockPart part, TextureAtlasSprite sprite, ResourceLocation modelLocation)
    {
        for (Map.Entry<Direction, BlockPartFace> e : part.faces.entrySet())
        {
            Direction d = e.getKey();
            builder.addCulledFace(d, Helpers.makeBakedQuad(part, e.getValue(), sprite, d, ModelRotation.X0_Y0, modelLocation));
        }
    }

    @SuppressWarnings("deprecation")
    public static TextureAtlasSprite getTexture(ResourceLocation location)
    {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(location);
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
