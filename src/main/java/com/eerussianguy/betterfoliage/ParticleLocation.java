package com.eerussianguy.betterfoliage;

import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

public enum ParticleLocation
{
    LEAF("falling_leaf_default_0", "falling_leaf_default_1", "falling_leaf_default_2"),
    LEAF_SPRUCE("falling_leaf_spruce_0", "falling_leaf_spruce_1", "falling_leaf_spruce_2", "falling_leaf_spruce_3"),
    LEAF_JUNGLE("falling_leaf_jungle_0", "falling_leaf_jungle_1", "falling_leaf_jungle_2", "falling_leaf_jungle_3"),
    SNOWBALL("snowball"),
    SOUL("rising_soul_0", "rising_soul_1"),
    SOUL_TRAIL("soul_track");

    private final List<ResourceLocation> resources;

    ParticleLocation(String... locations)
    {
        this.resources = Arrays.stream(locations).map(Helpers::identifier).toList();
    }

    public List<ResourceLocation> getResourceLocations()
    {
        return resources;
    }
}
