package com.eerussianguy.betterfoliage.particle;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;

import com.eerussianguy.betterfoliage.EventHandler;
import com.eerussianguy.betterfoliage.Helpers;
import com.eerussianguy.betterfoliage.ParticleLocation;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SoulParticle extends TextureSheetParticle
{
    private int ageOffset;
    private double drift;
    private boolean children;

    public SoulParticle(ClientLevel level, double x, double y, double z)
    {
        super(level, x, y, z);
        hasPhysics = false;
        setSize(0.02F, 0.02F);
        ageOffset = random.nextInt(15);
        setLifetime(40 + ageOffset);
        drift = (0.5D - random.nextDouble()) / 15D;
        yd = 0.07f;
        children = true;
    }

    public SoulParticle(ClientLevel level, double x, double y, double z, boolean children, double drift, int ageOffset, double yd, int age, float quadSize)
    {
        this(level, x, y, z);
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
            SoulParticle particle = new SoulParticle(level, x, y - (0.2D * age / 5), z, false, drift, ageOffset, yd, age, quadSize);
            Helpers.addParticle(particle, EventHandler.MAP.get(ParticleLocation.SOUL_TRAIL));
        }
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
