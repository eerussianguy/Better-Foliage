package com.eerussianguy.betterfoliage.particle;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@MethodsReturnNonnullByDefault
public class SpritePicker implements SpriteSet
{
    private List<TextureAtlasSprite> sprites;

    public SpritePicker() { }

    public TextureAtlasSprite get(int age, int lifetime)
    {
        return sprites.get(age * (sprites.size() - 1) / lifetime);
    }

    public TextureAtlasSprite get(Random rand)
    {
        return sprites.get(rand.nextInt(sprites.size()));
    }

    public void rebind(List<TextureAtlasSprite> spriteList)
    {
        sprites = ImmutableList.copyOf(spriteList);
    }
}
