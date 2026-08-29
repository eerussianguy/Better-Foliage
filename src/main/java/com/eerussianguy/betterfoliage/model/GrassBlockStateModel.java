package com.eerussianguy.betterfoliage.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
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
 * The baked grass block model.
 *
 * Sixteen variants are baked up front, one per N/E/S/W connection bitmask; each is a dirt cube with
 * the top texture on its upper face, plus a second cube whose side faces carry either the top
 * texture (side connects) or the side overlay (it does not). {@link #collectParts} reads the
 * neighbours to choose one, and may add a tuft of grass on top.
 */
public class GrassBlockStateModel implements DynamicBlockStateModel
{
    private static final CuboidFace.UVs UV_DEFAULT = new CuboidFace.UVs(0f, 0f, 16f, 16f);

    private static final String TEX_DIRT = "dirt";
    private static final String TEX_TOP = "top";
    private static final String TEX_OVERLAY = "overlay";

    private final BFModelPart[] models = new BFModelPart[16];
    @Nullable private final BFModelPart extraGrass;
    private final Material.Baked particle;
    @BakedQuad.MaterialFlags
    private final int materialFlags;

    public GrassBlockStateModel(GrassModel model, ModelBaker baker)
    {
        final ModelDebugName debugName = () -> "betterfoliage:grass/" + model.top();
        final Material.Baked dirtTex = baker.materials().get(new Material(model.dirt()), debugName);
        final Material.Baked topTex = baker.materials().get(new Material(model.top()), debugName);
        final Material.Baked overlayTex = baker.materials().get(new Material(model.overlay()), debugName);

        this.particle = dirtTex;

        for (int meta = 0; meta < models.length; meta++)
        {
            models[meta] = new BFModelPart(bake(baker, meta, model.tint(), dirtTex, topTex, overlayTex), dirtTex);
        }

        this.extraGrass = model.hasGrass() ? bakeExtraGrass(baker, model, topTex) : null;

        int flags = 0;
        for (BFModelPart part : models)
        {
            flags |= part.materialFlags();
        }
        if (extraGrass != null)
        {
            flags |= extraGrass.materialFlags();
        }
        this.materialFlags = flags;
    }

    /**
     * The tuft of grass placed on top is an ordinary model, so it is resolved and baked through the
     * usual pipeline rather than assembled by hand.
     */
    private static BFModelPart bakeExtraGrass(ModelBaker baker, GrassModel model, Material.Baked fallbackParticle)
    {
        final ResolvedModel resolved = baker.getModel(model.grass());
        final QuadCollection quads = resolved.bakeTopGeometry(resolved.getTopTextureSlots(), baker, BlockModelRotation.IDENTITY);
        return new BFModelPart(quads, fallbackParticle);
    }

    /**
     * One connection variant: the dirt core plus the overlay shell, baked into a single collection.
     */
    private static QuadCollection bake(ModelBaker baker, int meta, boolean tint, Material.Baked dirtTex, Material.Baked topTex, Material.Baked overlayTex)
    {
        final boolean[] sides = GrassConnectionData.sidesFromMeta(meta);

        // core: dirt all round, top texture on the upper face, tinted there only
        final Map<Direction, CuboidFace> coreFaces = Maps.newEnumMap(Direction.class);
        for (Direction d : Helpers.DIRECTIONS)
        {
            coreFaces.put(d, face(d, d == Direction.UP ? TEX_TOP : TEX_DIRT, d == Direction.UP && tint));
        }

        // overlay: the sides that do not connect get the overlay texture, everything else the top
        final Map<Direction, CuboidFace> overlayFaces = Maps.newEnumMap(Direction.class);
        for (Direction d : Helpers.DIRECTIONS)
        {
            overlayFaces.put(d, face(d, overlaySlot(d, sides), d != Direction.DOWN && tint));
        }

        final Function<String, Material.Baked> materials = name -> switch (name)
        {
            case TEX_TOP -> topTex;
            case TEX_OVERLAY -> overlayTex;
            default -> dirtTex;
        };

        final QuadCollection.Builder builder = new QuadCollection.Builder();
        UnbakedElementsHelper.bakeElements(baker, builder, List.of(cube(coreFaces), cube(overlayFaces)), materials, BlockModelRotation.IDENTITY);
        return builder.build();
    }

    private static String overlaySlot(Direction d, boolean[] sides)
    {
        return switch (d)
        {
            case UP -> TEX_TOP;
            case NORTH -> sides[0] ? TEX_TOP : TEX_OVERLAY;
            case EAST -> sides[1] ? TEX_TOP : TEX_OVERLAY;
            case SOUTH -> sides[2] ? TEX_TOP : TEX_OVERLAY;
            case WEST -> sides[3] ? TEX_TOP : TEX_OVERLAY;
            default -> TEX_DIRT;
        };
    }

    private static CuboidModelElement cube(Map<Direction, CuboidFace> faces)
    {
        return new CuboidModelElement(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), faces, null, true, 0);
    }

    private static CuboidFace face(Direction cull, String texture, boolean tint)
    {
        return new CuboidFace(
            cull,
            tint ? 0 : CuboidFace.NO_TINT,
            texture,
            UV_DEFAULT,
            Quadrant.R0,
            ExtraFaceData.DEFAULT,
            new MutableObject<>()
        );
    }

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts)
    {
        final GrassConnectionData data = GrassConnectionData.of(level, pos);
        parts.add(models[data.meta()]);
        if (wantsExtraGrass(data, random))
        {
            parts.add(extraGrass);
        }
    }

    /**
     * Keyed on the connection variant and whether the tuft is present. Both this and
     * {@link #collectParts} draw from {@code random} exactly once, in the same order, so the two
     * agree for a given block.
     */
    @Override
    @Nullable
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random)
    {
        final GrassConnectionData data = GrassConnectionData.of(level, pos);
        return new GeometryKey(this, data.meta(), wantsExtraGrass(data, random));
    }

    private boolean wantsExtraGrass(GrassConnectionData data, RandomSource random)
    {
        return extraGrass != null && data.up() && random.nextInt(BFConfig.CLIENT.extraGrassRarity.get()) == 0;
    }

    private record GeometryKey(GrassBlockStateModel model, int meta, boolean extraGrass) {}

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
