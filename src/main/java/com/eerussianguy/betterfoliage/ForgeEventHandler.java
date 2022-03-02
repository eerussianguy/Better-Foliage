package com.eerussianguy.betterfoliage;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import com.eerussianguy.betterfoliage.particle.LeafParticle;
import com.eerussianguy.betterfoliage.particle.SoulParticle;

public class ForgeEventHandler
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        bus.addListener(ForgeEventHandler::onClientTick);
    }

    private static void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused()) return;

        Entity entity = mc.getCameraEntity();
        if (entity == null) return;

        ClientLevel level = (ClientLevel) entity.level;
        if (level.getGameTime() % 2 != 0) return;
        Vec3 pos = entity.position();

        Random rand = level.random;
        final int spawnDistance = BFConfig.CLIENT.particleDistance.get();

        for (int i = 0; i < BFConfig.CLIENT.particleAttempts.get(); i++)
        {
            BlockPos searchPos = new BlockPos(pos.add(rand.nextInt(spawnDistance) - rand.nextInt(spawnDistance), rand.nextInt(spawnDistance) - 1, rand.nextInt(spawnDistance) - rand.nextInt(spawnDistance)));
            BlockState state = level.getBlockState(searchPos);
            if (state.is(BlockTags.LEAVES) && level.isEmptyBlock(searchPos.below()))
            {
                LeafParticle particle = new LeafParticle(level, searchPos.getX() + 0.5D, searchPos.getY() - 1D, searchPos.getZ() + 0.5D);

                if (BFConfig.CLIENT.snowballs.get() && rand.nextInt(2) == 0 && level.getBlockState(searchPos.above()).is(Blocks.SNOW))
                {
                    Helpers.addParticle(particle, EventHandler.MAP.get(ParticleLocation.SNOWBALL));
                }
                else if (BFConfig.CLIENT.leaves.get())
                {
                    Holder<Biome> biome = level.getBiome(searchPos);
                    if (biome.is(BiomeTags.IS_TAIGA))
                    {
                        Helpers.addTintedParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_SPRUCE), state, level, searchPos);
                    }
                    else if (biome.is(BiomeTags.IS_JUNGLE))
                    {
                        Helpers.addTintedParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF_JUNGLE), state, level, searchPos);
                    }
                    else
                    {
                        Helpers.addTintedParticle(particle, EventHandler.MAP.get(ParticleLocation.LEAF), state, level, searchPos);
                    }
                }
            }
            else if (BFConfig.CLIENT.souls.get() && state.is(BlockTags.SOUL_FIRE_BASE_BLOCKS) && level.isEmptyBlock(searchPos.above()))
            {
                SoulParticle particle = new SoulParticle(level, searchPos.getX() + 0.5D, searchPos.getY() + 1.0D, searchPos.getZ() + 0.5D);
                Helpers.addParticle(particle, EventHandler.MAP.get(ParticleLocation.SOUL));
            }
        }

    }
}
