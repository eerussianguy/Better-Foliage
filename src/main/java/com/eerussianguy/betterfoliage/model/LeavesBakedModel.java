package com.eerussianguy.betterfoliage.model;

import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Maps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;

import com.eerussianguy.betterfoliage.BFConfig;
import com.eerussianguy.betterfoliage.Helpers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LeavesBakedModel extends BFBakedModel
{
    public static List<LeavesBakedModel> INSTANCES = new ArrayList<>();

    private final ResourceLocation leaves;
    private final ResourceLocation fluff;
    private final ResourceLocation overlay;
    private final ResourceLocation modelLocation;
    private final boolean isOverlay;
    private final boolean tintOverlay;
    private final boolean tintLeaves;

    private TextureAtlasSprite leavesTex;
    private TextureAtlasSprite fluffTex;

    private final BlockModel blockModel;
    private final BakedModel[] crosses = new BakedModel[(int) Math.pow(BFConfig.CLIENT.leavesCacheSize.get(), 3)];

    private BakedModel core;
    private BakedModel outerCore;

    public LeavesBakedModel(ResourceLocation modelLocation, ResourceLocation leaves, ResourceLocation fluff, ResourceLocation overlay, boolean tintLeaves, boolean tintOverlay)
    {
        this.blockModel = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());

        this.modelLocation = modelLocation;
        this.leaves = leaves;
        this.fluff = fluff;
        this.overlay = overlay;
        this.isOverlay = !overlay.equals(Helpers.EMPTY);
        this.tintLeaves = tintLeaves;
        this.tintOverlay = tintOverlay;

        INSTANCES.add(this);
    }

    public void init()
    {
        leavesTex = Helpers.getTexture(leaves);
        fluffTex = Helpers.getTexture(fluff);
        if (isOverlay)
        {
            TextureAtlasSprite overlayTex = Helpers.getTexture(overlay);
            outerCore = buildBlock(overlayTex, tintOverlay);
        }
        core = buildBlock(leavesTex, tintLeaves);
        buildCrosses();
    }

    /**
     * Construct a cache of baked models to overlay onto the base leaves block.
     */
    private void buildCrosses()
    {
        int ordinal = 0;
        float leavesVariationDistance = BFConfig.CLIENT.leavesVariationDistance.get().floatValue();
        float[] intervals = Helpers.intervals(BFConfig.CLIENT.leavesCacheSize.get(), -leavesVariationDistance, leavesVariationDistance);
        for (float x : intervals)
        {
            for (float y : intervals)
            {
                for (float z : intervals)
                {
                    buildCross(ordinal, x, y, z);
                    ordinal++;
                }
            }
        }
    }

    private void buildCross(int ordinal, float x, float y, float z)
    {
        Map<Direction, BlockElementFace> mapFacesIn = Maps.newEnumMap(Direction.class);
        mapFacesIn.put(Direction.NORTH, Helpers.makeTintedFace(Helpers.UV_DEFAULT));
        mapFacesIn.put(Direction.SOUTH, Helpers.makeTintedFace(Helpers.UV_DEFAULT));

        Vector3f from = new Vector3f(-8f, -8f, 8f);
        Vector3f to = new Vector3f(24f, 24f, 8f);
        Vector3f moveVec = new Vector3f(x / 2, y / 1.2f, z / 2);
        from.add(moveVec);
        to.add(moveVec);

        BlockElement part = new BlockElement(from, to, mapFacesIn, makeRotation(45f), false);
        BlockElement partR = new BlockElement(from, to, mapFacesIn, makeRotation(-45f), false);

        SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(blockModel, ItemOverrides.EMPTY, false).particle(leavesTex);
        Helpers.assembleFaces(builder, part, fluffTex, modelLocation);
        Helpers.assembleFaces(builder, partR, fluffTex, modelLocation);

        crosses[ordinal] = builder.build();
    }

    private BlockElementRotation makeRotation(float degrees)
    {
        return new BlockElementRotation(new Vector3f(8f * 0.0625f, 0f, 8f * 0.0625f), Direction.Axis.Y, degrees, false);
    }

    private BakedModel buildBlock(TextureAtlasSprite tex, boolean tint)
    {
        Map<Direction, BlockElementFace> mapFacesIn = Maps.newEnumMap(Direction.class);
        for (Direction d : Helpers.DIRECTIONS)
        {
            mapFacesIn.put(d, tint ? Helpers.makeTintedFace(Helpers.UV_DEFAULT) : Helpers.makeFace(Helpers.UV_DEFAULT));
        }
        BlockElement part = new BlockElement(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), mapFacesIn, null, true);
        SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(blockModel, ItemOverrides.EMPTY, false).particle(tex);

        for (Map.Entry<Direction, BlockElementFace> e : part.faces.entrySet())
        {
            Direction d = e.getKey();
            builder.addCulledFace(d, Helpers.makeBakedQuad(part, e.getValue(), tex, d, BlockModelRotation.X0_Y0, modelLocation));
        }

        return builder.build();
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData extraData)
    {
        List<BakedQuad> coreQuads = core.getQuads(state, side, rand, extraData);

        if (state == null) return coreQuads;
        LeavesOrdinalData data = extraData instanceof LeavesOrdinalData ? (LeavesOrdinalData) extraData : new LeavesOrdinalData(BlockPos.ZERO);

        List<BakedQuad> quads = new ArrayList<>(coreQuads);
        List<BakedQuad> crossQuads = crosses[data.get()].getQuads(state, side, rand, extraData);
        quads.addAll(crossQuads);
        if (isOverlay)
        {
            List<BakedQuad> outQuads = outerCore.getQuads(state, side, rand, extraData);
            quads.addAll(outQuads);
        }
        return quads;
    }

    @Override
    @Nonnull
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData extraData)
    {
        return new LeavesOrdinalData(pos);
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return leavesTex;
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack)
    {
        Helpers.handlePerspective(cameraTransformType, poseStack);
        return this;
    }
}
