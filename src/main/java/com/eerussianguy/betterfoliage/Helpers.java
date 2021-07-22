package com.eerussianguy.betterfoliage;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

public class Helpers
{
    public static final Direction[] DIRECTIONS = Direction.values();

    public static final ResourceLocation EMPTY = new ResourceLocation(MOD_ID, "empty");

    public static BlockElementFace makeTintedFace(BlockFaceUV uv)
    {
        return new BlockElementFace(null, 0, "", uv);
    }

    public static BlockElementFace makeFace(BlockFaceUV uv)
    {
        return new BlockElementFace(null, -1, "", uv);
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
