package com.eerussianguy.betterfoliage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import com.eerussianguy.betterfoliage.particle.SnowballParticle;
import com.eerussianguy.betterfoliage.particle.SoulParticle;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ForgeEventHandler
{
    private static final Map<ParticleLocation, List<TextureAtlasSprite>> SPRITE_CACHE = new HashMap<>();

    public static void init()
    {
        final var bus = NeoForge.EVENT_BUS;

        bus.addListener(ForgeEventHandler::onClientTick);
    }

    public static void clearCache()
    {
        SPRITE_CACHE.clear();
    }

    private static void onClientTick(ClientTickEvent.Post event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused()) return;

        Entity entity = mc.getCameraEntity();
        if (entity == null) return;

        ClientLevel level = (ClientLevel) entity.level();
        if (level.getGameTime() % 2 != 0) return;
        final Vec3 ePos = entity.position();
        final Vec3i pos = new Vec3i((int) ePos.x, (int) ePos.y, (int) ePos.z);

        RandomSource rand = level.getRandom();
        final int spawnDistance = BFConfig.CLIENT.particleDistance.get();

        final AbstractTexture particleTexture = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES);
        if (particleTexture instanceof TextureAtlas atlas)
        {
            for (int i = 0; i < BFConfig.CLIENT.particleAttempts.get(); i++)
            {
                BlockPos searchPos = new BlockPos(pos.offset(rand.nextInt(spawnDistance) - rand.nextInt(spawnDistance), rand.nextInt(spawnDistance) - 1, rand.nextInt(spawnDistance) - rand.nextInt(spawnDistance)));
                BlockState state = level.getBlockState(searchPos);
                if (BFConfig.CLIENT.snowballs.get() && state.is(BlockTags.LEAVES) && level.isEmptyBlock(searchPos.below())
                    && rand.nextInt(2) == 0 && level.getBlockState(searchPos.above()).is(Blocks.SNOW))
                {
                    TextureAtlasSprite sprite = Helpers.pickSprite(getTextures(ParticleLocation.SNOWBALL, atlas), rand);
                    if (sprite != null)
                    {
                        Helpers.addParticle(new SnowballParticle(level, searchPos.getX() + 0.5D, searchPos.getY() - 1D, searchPos.getZ() + 0.5D, sprite));
                    }
                }
                else if (BFConfig.CLIENT.souls.get() && (state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL)) && level.isEmptyBlock(searchPos.above()))
                {
                    TextureAtlasSprite sprite = Helpers.pickSprite(getTextures(ParticleLocation.SOUL, atlas), rand);
                    if (sprite != null)
                    {
                        Helpers.addParticle(new SoulParticle(level, searchPos.getX() + 0.5D, searchPos.getY() + 1.0D, searchPos.getZ() + 0.5D, sprite));
                    }
                    getTextures(ParticleLocation.SOUL_TRAIL, atlas); // warm the cache; the trail particles spawn without an atlas to hand
                }
            }
        }

    }

    public static List<TextureAtlasSprite> getTextures(ParticleLocation location, @Nullable TextureAtlas atlas)
    {
        if (atlas == null)
        {
            return SPRITE_CACHE.get(location);
        }
        return SPRITE_CACHE.computeIfAbsent(location, s -> location.getResourceLocations().stream().map(atlas::getSprite).toList());
    }
}
