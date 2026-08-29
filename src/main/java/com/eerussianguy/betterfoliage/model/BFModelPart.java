package com.eerussianguy.betterfoliage.model;

import java.util.List;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

/**
 * One pre-baked layer of a block: a leaves core, one of its fluff crosses, a grass connection
 * variant, and so on.
 *
 * Replaces the pieces that used to be individual {@code BakedModel}s combined in {@code getQuads}.
 */
public record BFModelPart(QuadCollection quads, Material.Baked particleMaterial) implements BlockStateModelPart
{
    @Override
    public List<BakedQuad> getQuads(@Nullable Direction direction)
    {
        return quads.getQuads(direction);
    }

    @Override
    @Deprecated
    public boolean useAmbientOcclusion()
    {
        return true;
    }

    @Override
    @BakedQuad.MaterialFlags
    public int materialFlags()
    {
        return quads.materialFlags();
    }
}
