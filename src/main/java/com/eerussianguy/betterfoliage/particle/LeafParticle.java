package com.eerussianguy.betterfoliage.particle;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LeafParticle extends TextureSheetParticle
{
    public LeafParticle(ClientLevel worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
        setSize(0.02F, 0.02F);
        quadSize *= random.nextFloat() * 1.2F + 0.2F;
        double rainingAdd = worldIn.isRaining() ? 1D : 0D;
        xd = (Math.random() * 2.0D - 1.0D) * 0.02D + rainingAdd;
        yd = -0.2D + (random.nextFloat() / 6f);
        zd = (Math.random() * 2.0D - 1.0D) * 0.02D + rainingAdd;
        lifetime = 70 + random.nextInt(15);
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
        }
        else
        {
            move(xd, yd, zd);
            xd *= 0.98F;
            yd *= 0.98F;
            zd *= 0.98F;
            if (onGround)
            {
                lifetime--;
                xd = 0;
                yd = 0;
                zd = 0;
            }

        }
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
}
