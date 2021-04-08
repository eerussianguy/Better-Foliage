package com.eerussianguy.betterfoliage.model;

import java.util.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import com.eerussianguy.betterfoliage.Helpers;
import mcp.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GrassBakedModel implements IDynamicBakedModel
{
    public static List<GrassBakedModel> INSTANCES = new ArrayList<>();

    private final BlockModel blockModel;

    private final ResourceLocation modelLocation;
    private final ResourceLocation dirt;
    private final ResourceLocation top;
    private final ResourceLocation overlay;
    private final boolean tint;

    private TextureAtlasSprite dirtTex;
    private TextureAtlasSprite topTex;
    private TextureAtlasSprite overlayTex;

    private final IBakedModel[] models = new IBakedModel[16];

    private BlockPart core;

    public GrassBakedModel(ResourceLocation modelLocation, ResourceLocation dirt, ResourceLocation top, ResourceLocation overlay, boolean tint)
    {
        this.blockModel = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT, ItemCameraTransforms.NO_TRANSFORMS, ItemOverrideList.EMPTY.getOverrides());

        this.modelLocation = modelLocation;
        this.dirt = dirt;
        this.top = top;
        this.overlay = overlay;
        this.tint = tint;

        INSTANCES.add(this);
    }

    public void init()
    {
        dirtTex = Helpers.getTexture(dirt);
        topTex = Helpers.getTexture(top);
        overlayTex = Helpers.getTexture(overlay);

        buildCore();
        generateModels();
    }

    public void buildCore()
    {
        Map<Direction, BlockPartFace> mapFacesIn = Maps.newEnumMap(Direction.class);
        for (Direction d : Helpers.DIRECTIONS)
        {
            BlockFaceUV faceUV = new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0);
            mapFacesIn.put(d, (d == Direction.UP && tint) ? Helpers.makeTintedFace(faceUV) : Helpers.makeFace(faceUV));
        }
        core = new BlockPart(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), mapFacesIn, null, true);
    }

    public void generateModels()
    {
        for (int meta = 0; meta < 16; meta++)
        {
            Map<Direction, BlockPartFace> mapFacesIn = Maps.newEnumMap(Direction.class);
            for (Direction d : Helpers.DIRECTIONS)
            {
                BlockFaceUV faceUV = new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0);
                mapFacesIn.put(d, (d != Direction.DOWN && tint) ? Helpers.makeTintedFace(faceUV) : Helpers.makeFace(faceUV));
            }
            BlockPart part = new BlockPart(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), mapFacesIn, null, true);
            SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(blockModel, ItemOverrideList.EMPTY, false).particle(topTex);

            for (Map.Entry<Direction, BlockPartFace> e : core.faces.entrySet()) // we unwrap and re-wrap this every time, since its 16 distinct models
            {
                Direction d = e.getKey();
                // add the 'interior' of the block
                builder.addCulledFace(d, Helpers.makeBakedQuad(core, e.getValue(), d == Direction.UP ? topTex : dirtTex, d, ModelRotation.X0_Y0, modelLocation));
            }
            for (Map.Entry<Direction, BlockPartFace> e : part.faces.entrySet())
            {
                Direction d = e.getKey();
                // add the overlay, be it a full covering or a 'side'
                builder.addCulledFace(d, Helpers.makeBakedQuad(part, e.getValue(), resolveTexture(d, stateFromMeta(meta)), d, ModelRotation.X0_Y0, modelLocation));
            }
            models[meta] = builder.build();
        }
    }

    private TextureAtlasSprite resolveTexture(Direction d, boolean[] booleans)
    {
        switch (d)
        {
            case UP:
                return topTex;
            case DOWN:
            default:
                return dirtTex;
            case NORTH:
                return booleans[0] ? topTex : overlayTex;
            case EAST:
                return booleans[1] ? topTex : overlayTex;
            case SOUTH:
                return booleans[2] ? topTex : overlayTex;
            case WEST:
                return booleans[3] ? topTex : overlayTex;
        }
    }

    private static boolean[] stateFromMeta(int meta)
    {
        boolean[] state = {false, false, false, false}; // N E S W
        state[0] = (meta & 1) > 0;
        state[1] = (meta & 2) > 0;
        state[2] = (meta & 4) > 0;
        state[3] = (meta & 8) > 0;
        return state;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand, IModelData extraData)
    {
        int meta = (state != null && extraData instanceof GrassConnectionData) ? ((GrassConnectionData) extraData).get() : 0;
        return models[meta].getQuads(state, side, rand, extraData);
    }

    @Override
    public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData extraData)
    {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        BlockPos down = pos.below();
        boolean north = world.getBlockState(mutable.setWithOffset(down, Direction.NORTH)).hasProperty(BlockStateProperties.SNOWY);
        boolean east = world.getBlockState(mutable.setWithOffset(down, Direction.EAST)).hasProperty(BlockStateProperties.SNOWY);
        boolean south = world.getBlockState(mutable.setWithOffset(down, Direction.SOUTH)).hasProperty(BlockStateProperties.SNOWY);
        boolean west = world.getBlockState(mutable.setWithOffset(down, Direction.WEST)).hasProperty(BlockStateProperties.SNOWY);
        return new GrassConnectionData(north, east, south, west);
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return false;
    }

    @Override
    public boolean usesBlockLight()
    {
        return true;
    }

    @Override
    public boolean isCustomRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return dirtTex;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.EMPTY;
    }
}
