package com.eerussianguy.betterfoliage;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;

import org.jspecify.annotations.Nullable;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

public class Helpers
{
    public static final Direction[] DIRECTIONS = Direction.values();

    public static Identifier identifier(String name)
    {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final Identifier EMPTY = identifier("empty");

    /**
     * Equivalent of a linspace function in numpy or MATLAB or what have you
     *
     * @author AlcatrazEscapee
     */
    public static float[] intervals(int n, float min, float max)
    {
        float[] f = new float[n];
        for (int i = 0; i < n; i++)
        {
            float t = (float) i / (n - 1);
            f[i] = min * t + max * (1 - t);
        }
        return f;
    }

    /**
     * Particles take their sprite in the constructor now, and we build ours by hand rather than
     * through a registered {@code ParticleProvider}, so the sprite has to be chosen off the atlas
     * before the particle exists.
     *
     * @return null if the sprites have not been stitched yet, in which case there is nothing to spawn
     */
    @Nullable
    public static TextureAtlasSprite pickSprite(@Nullable List<TextureAtlasSprite> sprites, RandomSource random)
    {
        if (sprites == null || sprites.isEmpty()) return null;
        return sprites.get(random.nextInt(sprites.size()));
    }

    public static void addParticle(Particle particle)
    {
        Minecraft.getInstance().particleEngine.add(particle);
    }
}
