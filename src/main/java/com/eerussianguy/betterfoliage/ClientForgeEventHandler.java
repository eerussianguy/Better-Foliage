package com.eerussianguy.betterfoliage;

import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.eerussianguy.betterfoliage.particle.LeafParticle;
import com.eerussianguy.betterfoliage.particle.SoulParticle;
import com.eerussianguy.betterfoliage.particle.SpritePicker;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEventHandler
{
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
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
                    addParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.SNOWBALL));
                }
                else if (BFConfig.CLIENT.leaves.get())
                {
                    level.getBiomeName(searchPos).ifPresent(key -> {
                        if (BiomeDictionary.hasType(key, BiomeDictionary.Type.CONIFEROUS))
                        {
                            addTintedParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.LEAF_SPRUCE), state, level, searchPos);
                        }
                        else if (BiomeDictionary.hasType(key, BiomeDictionary.Type.JUNGLE))
                        {
                            addTintedParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.LEAF_JUNGLE), state, level, searchPos);
                        }
                        else
                        {
                            addTintedParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.LEAF), state, level, searchPos);
                        }
                    });
                }
            }
            else if (BFConfig.CLIENT.souls.get() && state.is(BlockTags.SOUL_FIRE_BASE_BLOCKS) && level.isEmptyBlock(searchPos.above()))
            {
                SoulParticle particle = new SoulParticle(level, searchPos.getX() + 0.5D, searchPos.getY() + 1.0D, searchPos.getZ() + 0.5D);
                addParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.SOUL));
            }
        }

    }

    public static void addParticle(TextureSheetParticle particle, List<TextureAtlasSprite> sprites)
    {
        Minecraft mc = Minecraft.getInstance();

        SpritePicker picker = new SpritePicker();
        picker.rebind(sprites);

        particle.pickSprite(picker);
        mc.particleEngine.add(particle);
    }

    public static void addTintedParticle(TextureSheetParticle particle, List<TextureAtlasSprite> sprites, BlockState state, ClientLevel level, BlockPos pos)
    {
        Minecraft mc = Minecraft.getInstance();

        SpritePicker picker = new SpritePicker();
        picker.rebind(sprites);

        int color = mc.getBlockColors().getColor(state, level, pos); // catches leaves that override default (like birch)
        if (color == FoliageColor.getDefaultColor())
        {
            color = level.getBiome(pos).getFoliageColor(); // catches stuff like swamp that uses biome always
        }

        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        //float a = ((color >> 24) & 0xFF) / 255F;

        particle.pickSprite(picker);
        particle.setColor(r, g, b);
        mc.particleEngine.add(particle);
    }
}
