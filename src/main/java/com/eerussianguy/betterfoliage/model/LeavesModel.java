package com.eerussianguy.betterfoliage.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.Identifier;

import com.eerussianguy.betterfoliage.Helpers;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

/**
 * The unbaked leaves model, as it appears in a blockstate file:
 *
 * <pre>
 * "variants": { "": {
 *     "neoforge:type": "betterfoliage:leaves",
 *     "leaves": "minecraft:block/oak_leaves",
 *     "fluff":  "betterfoliage:block/oak_fluff"
 * }}
 * </pre>
 *
 * Registered in {@code EventHandler} via {@code RegisterBlockStateModels}.
 */
public record LeavesModel(Identifier leaves, Identifier fluff, Identifier overlay, boolean tintLeaves, boolean tintOverlay) implements CustomUnbakedBlockStateModel
{
    public static final Identifier ID = Helpers.identifier("leaves");

    public static final MapCodec<LeavesModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Identifier.CODEC.fieldOf("leaves").forGetter(LeavesModel::leaves),
        Identifier.CODEC.fieldOf("fluff").forGetter(LeavesModel::fluff),
        Identifier.CODEC.optionalFieldOf("overlay", Helpers.EMPTY).forGetter(LeavesModel::overlay),
        com.mojang.serialization.Codec.BOOL.optionalFieldOf("tintLeaves", true).forGetter(LeavesModel::tintLeaves),
        com.mojang.serialization.Codec.BOOL.optionalFieldOf("tintOverlay", false).forGetter(LeavesModel::tintOverlay)
    ).apply(instance, LeavesModel::new));

    public boolean hasOverlay()
    {
        return !overlay.equals(Helpers.EMPTY);
    }

    @Override
    public BlockStateModel bake(ModelBaker baker)
    {
        return new LeavesBlockStateModel(this, baker);
    }

    @Override
    public void resolveDependencies(Resolver resolver)
    {
        // no sub-models to resolve; textures are looked up directly at bake time
    }

    @Override
    public MapCodec<? extends CustomUnbakedBlockStateModel> codec()
    {
        return MAP_CODEC;
    }
}
