package com.eerussianguy.betterfoliage.compat;

import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.eerussianguy.betterfoliage.EventHandler;
import com.eerussianguy.betterfoliage.Helpers;
import com.eerussianguy.betterfoliage.ParticleLocation;
import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.wood.Wood;

public enum TFCIntegration implements FoliageIntegrator
{
    INSTANCE;

    @Override
    public void create(TextureSheetParticle particle, BlockState state, Level level, BlockPos pos)
    {
        final int color = state.is(TFCTags.Blocks.SEASONAL_LEAVES) ? TFCColors.getSeasonalFoliageColor(pos, 0) : TFCColors.getFoliageColor(pos, 0);
        if (state.is(getWood(Wood.KAPOK)))
        {
            Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_JUNGLE), color);
        }
        else if (state.is(getWood(Wood.SPRUCE)) || state.is(getWood(Wood.PINE)))
        {
            Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_SPRUCE), color);
        }
        else
        {
            Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF), color);
        }
    }

    private static Block getWood(Wood wood)
    {
        return TFCBlocks.WOODS.get(wood).get(Wood.BlockType.LEAVES).get();
    }
}
