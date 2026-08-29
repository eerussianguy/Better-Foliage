package com.eerussianguy.betterfoliage.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SnowballParticle extends SingleQuadParticle
{
    public SnowballParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite)
    {
        super(level, x, y, z, sprite);
        setSize(0.02F, 0.02F);
        quadSize *= random.nextFloat() * 1.2F + 0.2F;
        double rainingAdd = level.isRaining() ? 1D : 0D;
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
    protected Layer getLayer()
    {
        return Layer.OPAQUE;
    }
}
