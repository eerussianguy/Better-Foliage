package com.eerussianguy.betterfoliage.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.Identifier;

import com.eerussianguy.betterfoliage.Helpers;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;

/**
 * The unbaked grass block model, as it appears in a blockstate file:
 *
 * <pre>
 * "variants": { "snowy=false": {
 *     "neoforge:type": "betterfoliage:grass",
 *     "dirt":    "minecraft:block/dirt",
 *     "top":     "minecraft:block/grass_block_top",
 *     "overlay": "minecraft:block/grass_block_side_overlay",
 *     "tint":    true,
 *     "grass":   "betterfoliage:block/better_grass"
 * }}
 * </pre>
 *
 * Registered in {@code EventHandler} via {@code RegisterBlockStateModels}.
 */
public record GrassModel(Identifier dirt, Identifier top, Identifier overlay, boolean tint, Identifier grass) implements CustomUnbakedBlockStateModel
{
    public static final Identifier ID = Helpers.identifier("grass");

    public static final MapCodec<GrassModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Identifier.CODEC.fieldOf("dirt").forGetter(GrassModel::dirt),
        Identifier.CODEC.fieldOf("top").forGetter(GrassModel::top),
        Identifier.CODEC.fieldOf("overlay").forGetter(GrassModel::overlay),
        Codec.BOOL.optionalFieldOf("tint", false).forGetter(GrassModel::tint),
        Identifier.CODEC.optionalFieldOf("grass", Helpers.EMPTY).forGetter(GrassModel::grass)
    ).apply(instance, GrassModel::new));

    /** Podzol and the grassless snow variant have no tuft on top. */
    public boolean hasGrass()
    {
        return !grass.equals(Helpers.EMPTY);
    }

    @Override
    public BlockStateModel bake(ModelBaker baker)
    {
        return new GrassBlockStateModel(this, baker);
    }

    @Override
    public void resolveDependencies(Resolver resolver)
    {
        if (hasGrass())
        {
            resolver.markDependency(grass);
        }
    }

    @Override
    public MapCodec<? extends CustomUnbakedBlockStateModel> codec()
    {
        return MAP_CODEC;
    }
}
