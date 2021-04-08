package com.eerussianguy.betterfoliage.particle;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

import com.eerussianguy.betterfoliage.ClientEventHandler;
import com.eerussianguy.betterfoliage.ClientForgeEventHandler;
import com.eerussianguy.betterfoliage.ParticleLocation;
import mcp.MethodsReturnNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SoulParticle extends SpriteTexturedParticle
{
    private int ageOffset;
    private double drift;
    private boolean children;

    public SoulParticle(ClientWorld world, double x, double y, double z)
    {
        super(world, x, y, z);
        hasPhysics = false;
        setSize(0.02F, 0.02F);
        ageOffset = random.nextInt(15);
        setLifetime(40 + ageOffset);
        drift = (0.5D - random.nextDouble()) / 15D;
        yd = 0.07f;
        children = true;
    }

    public SoulParticle(ClientWorld world, double x, double y, double z, boolean children, double drift, int ageOffset, double yd, int age, float quadSize)
    {
        this(world, x, y, z);
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
        double swirl = (MathHelper.sin((age + ageOffset) * 0.6F) - 0.5D) * life * 0.08F;
        xd = swirl + drift;
        yd *= 0.98F;
        zd = swirl + drift;
        if (children && (age == 5 || age == 10 || age == 15))
        {
            SoulParticle particle = new SoulParticle(level, x, y - (0.2D * age / 5), z, false, drift, ageOffset, yd, age, quadSize);
            ClientForgeEventHandler.addParticle(particle, ClientEventHandler.MAP.get(ParticleLocation.SOUL_TRAIL));
        }
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
