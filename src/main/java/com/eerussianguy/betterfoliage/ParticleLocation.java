package com.eerussianguy.betterfoliage;

import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.Identifier;

public enum ParticleLocation
{
    SNOWBALL("snowball"),
    SOUL("rising_soul_0", "rising_soul_1"),
    SOUL_TRAIL("soul_track");

    private final List<Identifier> resources;

    ParticleLocation(String... locations)
    {
        this.resources = Arrays.stream(locations).map(Helpers::identifier).toList();
    }

    public List<Identifier> getResourceLocations()
    {
        return resources;
    }
}
