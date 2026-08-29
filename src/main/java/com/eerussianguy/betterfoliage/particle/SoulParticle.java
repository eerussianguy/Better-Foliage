package com.eerussianguy.betterfoliage.particle;

import com.eerussianguy.betterfoliage.ForgeEventHandler;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;

import com.eerussianguy.betterfoliage.Helpers;
import com.eerussianguy.betterfoliage.ParticleLocation;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SoulParticle extends SingleQuadParticle
{
    private int ageOffset;
    private double drift;
    private boolean children;

    public SoulParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite)
    {
        super(level, x, y, z, sprite);
        hasPhysics = false;
        setSize(0.02F, 0.02F);
        ageOffset = random.nextInt(15);
        setLifetime(40 + ageOffset);
        drift = (0.5D - random.nextDouble()) / 15D;
        yd = 0.07f;
        children = true;
    }

    public SoulParticle(ClientLevel level, double x, double y, double z, boolean children, double drift, int ageOffset, double yd, int age, float quadSize, TextureAtlasSprite sprite)
    {
        this(level, x, y, z, sprite);
        this.children = children;
        this.drift = drift;
        this.ageOffset = ageOffset;
        this.age = age;
        this.yd = yd;
        this.quadSize = quadSize * 0.66F;
    }

    @Override
    public void tick()
    {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime)
        {
            remove();
            return;
        }
        if (age > 30)
            alpha *= 0.9;
        move(xd, yd, zd);
        double life = 1 - (double) age / lifetime;
        double swirl = (Mth.sin((age + ageOffset) * 0.6F) - 0.5D) * life * 0.08F;
        xd = swirl + drift;
        yd *= 0.98F;
        zd = swirl + drift;
        if (children && (age == 5 || age == 10 || age == 15))
        {
            final TextureAtlasSprite trail = Helpers.pickSprite(ForgeEventHandler.getTextures(ParticleLocation.SOUL_TRAIL, null), random);
            if (trail != null)
            {
                Helpers.addParticle(new SoulParticle(level, x, y - (0.2D * age / 5), z, false, drift, ageOffset, yd, age, quadSize, trail));
            }
        }
    }

    @Override
    protected Layer getLayer()
    {
        return Layer.OPAQUE;
    }
}
