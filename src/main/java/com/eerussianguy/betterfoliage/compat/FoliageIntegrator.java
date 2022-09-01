package com.eerussianguy.betterfoliage.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import com.eerussianguy.betterfoliage.EventHandler;
import com.eerussianguy.betterfoliage.Helpers;
import com.eerussianguy.betterfoliage.ParticleLocation;

public interface FoliageIntegrator
{
    void create(TextureSheetParticle particle, BlockState state, Level level, BlockPos pos);

    enum Vanilla implements FoliageIntegrator
    {
        INSTANCE;

        @Override
        public void create(TextureSheetParticle particle, BlockState state, Level level, BlockPos pos)
        {
            final Minecraft mc = Minecraft.getInstance();

            int color = mc.getBlockColors().getColor(state, level, pos); // catches leaves that override default (like birch)
            if (color == FoliageColor.getDefaultColor())
            {
                color = level.getBiome(pos).value().getFoliageColor(); // catches stuff like swamp that uses biome always
            }

            final Holder<Biome> biome = level.getBiome(pos);
            if (biome.is(BiomeTags.IS_TAIGA))
            {
                Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_SPRUCE), color);
            }
            else if (biome.is(BiomeTags.IS_JUNGLE))
            {
                Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_JUNGLE), color);
            }
            else
            {
                Helpers.finishParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF), color);
            }
        }
    }
}
