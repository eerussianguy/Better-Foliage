package com.eerussianguy.betterfoliage.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.CuboidRotation;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import com.eerussianguy.betterfoliage.BFConfig;
import com.eerussianguy.betterfoliage.Helpers;
import com.mojang.math.Quadrant;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import org.apache.commons.lang3.mutable.MutableObject;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

/**
 * The baked leaves model.
 *
 * Everything is baked up front: the solid core, an optional overlay shell, and a cache of fluff
 * "crosses" whose offsets are spread over leavesVariationDistance. Which cross a block gets is
 * chosen from its {@link BlockPos} in {@link #collectParts}, which is what makes neighbouring
 * leaves blocks look different from one another.
 */
public class LeavesBlockStateModel implements DynamicBlockStateModel
{
    private static final CuboidFace.UVs UV_DEFAULT = new CuboidFace.UVs(0f, 0f, 16f, 16f);

    /** Rotation origin is in block units (0-1), unlike from/to which are in pixels. */
    private static final Vector3f CROSS_ORIGIN = new Vector3f(8f * 0.0625f, 0f, 8f * 0.0625f);

    private static final String TEX_CORE = "core";
    private static final String TEX_FLUFF = "fluff";

    private final BFModelPart core;
    @Nullable private final BFModelPart overlay;
    private final BFModelPart[] crosses;
    private final Material.Baked particle;
    @BakedQuad.MaterialFlags
    private final int materialFlags;

    public LeavesBlockStateModel(LeavesModel model, ModelBaker baker)
    {
        final ModelDebugName debugName = () -> "betterfoliage:leaves/" + model.leaves();
        final Material.Baked leavesTex = baker.materials().get(new Material(model.leaves()), debugName);
        final Material.Baked fluffTex = baker.materials().get(new Material(model.fluff()), debugName);

        this.particle = leavesTex;
        this.core = new BFModelPart(bakeBlock(baker, leavesTex, model.tintLeaves()), leavesTex);

        if (model.hasOverlay())
        {
            final Material.Baked overlayTex = baker.materials().get(new Material(model.overlay()), debugName);
            this.overlay = new BFModelPart(bakeBlock(baker, overlayTex, model.tintOverlay()), overlayTex);
        }
        else
        {
            this.overlay = null;
        }

        this.crosses = bakeCrosses(baker, leavesTex, fluffTex, model.tintLeaves());

        int flags = core.materialFlags();
        for (BFModelPart cross : crosses)
        {
            flags |= cross.materialFlags();
        }
        if (overlay != null)
        {
            flags |= overlay.materialFlags();
        }
        this.materialFlags = flags;
    }

    /**
     * Build the cache of fluff crosses to overlay onto the base leaves block. One is baked for every
     * point on a leavesCacheSize-cubed grid of offsets.
     */
    private static BFModelPart[] bakeCrosses(ModelBaker baker, Material.Baked leavesTex, Material.Baked fluffTex, boolean tint)
    {
        final int cacheSize = BFConfig.CLIENT.leavesCacheSize.get();
        final float variation = BFConfig.CLIENT.leavesVariationDistance.get().floatValue();
        final float[] intervals = Helpers.intervals(cacheSize, -variation, variation);

        final BFModelPart[] crosses = new BFModelPart[cacheSize * cacheSize * cacheSize];
        int ordinal = 0;
        for (float x : intervals)
        {
            for (float y : intervals)
            {
                for (float z : intervals)
                {
                    crosses[ordinal] = new BFModelPart(bakeCross(baker, fluffTex, tint, x, y, z), leavesTex);
                    ordinal++;
                }
            }
        }
        return crosses;
    }

    private static QuadCollection bakeCross(ModelBaker baker, Material.Baked fluffTex, boolean tint, float x, float y, float z)
    {
        final Map<Direction, CuboidFace> faces = Maps.newEnumMap(Direction.class);
        faces.put(Direction.NORTH, face(Direction.NORTH, TEX_FLUFF, tint, false));
        faces.put(Direction.SOUTH, face(Direction.SOUTH, TEX_FLUFF, tint, false));

        final Vector3f move = new Vector3f(x / 2, y / 1.2f, z / 2);
        final Vector3f from = new Vector3f(-8f, -8f, 8f).add(move);
        final Vector3f to = new Vector3f(24f, 24f, 8f).add(move);

        final List<CuboidModelElement> elements = List.of(
            new CuboidModelElement(from, to, faces, rotation(45f), false, 0),
            new CuboidModelElement(from, to, faces, rotation(-45f), false, 0)
        );

        return bake(baker, elements, fluffTex);
    }

    private static QuadCollection bakeBlock(ModelBaker baker, Material.Baked tex, boolean tint)
    {
        final Map<Direction, CuboidFace> faces = Maps.newEnumMap(Direction.class);
        for (Direction d : Helpers.DIRECTIONS)
        {
            faces.put(d, face(d, TEX_CORE, tint, true));
        }

        final CuboidModelElement element = new CuboidModelElement(
            new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), faces, null, true, 0
        );

        return bake(baker, List.of(element), tex);
    }

    private static QuadCollection bake(ModelBaker baker, List<CuboidModelElement> elements, Material.Baked material)
    {
        final QuadCollection.Builder builder = new QuadCollection.Builder();
        UnbakedElementsHelper.bakeElements(baker, builder, elements, name -> material, BlockModelRotation.IDENTITY);
        return builder.build();
    }

    private static CuboidFace face(Direction cull, String texture, boolean tint, boolean ao)
    {
        return new CuboidFace(
            cull,
            tint ? 0 : CuboidFace.NO_TINT,
            texture,
            UV_DEFAULT,
            Quadrant.R0,
            new ExtraFaceData(0xFFFFFFFF, 0, ao),
            new MutableObject<>()
        );
    }

    private static CuboidRotation rotation(float degrees)
    {
        return new CuboidRotation(CROSS_ORIGIN, new CuboidRotation.SingleAxisRotation(Direction.Axis.Y, degrees), false);
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts)
    {
        parts.add(core);
        parts.add(crosses[LeavesOrdinalData.ordinal(pos, crosses.length)]);
        if (overlay != null)
        {
            parts.add(overlay);
        }
    }

    /**
     * Blocks that land on the same cross can share baked geometry, so key on the ordinal.
     */
    @Override
    @Nullable
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random)
    {
        return new GeometryKey(this, LeavesOrdinalData.ordinal(pos, crosses.length));
    }

    private record GeometryKey(LeavesBlockStateModel model, int ordinal) {}

    @Override
    @Deprecated
    public Material.Baked particleMaterial()
    {
        return particle;
    }

    @Override
    @Deprecated
    @BakedQuad.MaterialFlags
    public int materialFlags()
    {
        return materialFlags;
    }
}
