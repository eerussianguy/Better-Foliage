package com.eerussianguy.betterfoliage;

import java.util.function.Function;

import net.neoforged.neoforge.common.ModConfigSpec;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

public class ClientConfig
{
    public final ModConfigSpec.IntValue particleAttempts;
    public final ModConfigSpec.IntValue particleDistance;

    public final ModConfigSpec.BooleanValue souls;
    public final ModConfigSpec.BooleanValue snowballs;

    public final ModConfigSpec.IntValue leavesCacheSize;
    public final ModConfigSpec.DoubleValue leavesVariationDistance;
    public final ModConfigSpec.IntValue extraGrassRarity;

    public final ModConfigSpec.BooleanValue forceForgeLighting;

    ClientConfig(ModConfigSpec.Builder innerBuilder)
    {
        Function<String, ModConfigSpec.Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.server." + name);

        innerBuilder.push("general");

        particleAttempts = builder.apply("particleAttempts").comment("Attempts per tick to spawn a particle").defineInRange("particleAttempts", 2, 0, Integer.MAX_VALUE);
        particleDistance = builder.apply("particleDistance").comment("Horizontal and Vertical distance particles will spawn from").defineInRange("particleDistance", 15, 0, Integer.MAX_VALUE);
        souls = builder.apply("souls").comment("Enable Soul Particles?").define("souls", true);
        snowballs = builder.apply("snowballs").comment("Enable Snowballs?").define("snowballs", true);
        leavesCacheSize = builder.apply("leavesCacheSize").comment("Determines the size of the leaves cache. Number of models cached per leaf block will be the number you input to the third power. Bigger cache = more RAM, but more variation and less z-fighting as a result").worldRestart().defineInRange("leavesCacheSize", 7, 5, 20);
        leavesVariationDistance = builder.apply("leavesVariationDistance").comment("Determines the max distance leaves block fluff can deviate from the actual block. 0.0 means no distance variation (all fluff is in the middle of the block)").worldRestart().defineInRange("leavesVariationDistance", 2.75f, 0f, 7f);
        forceForgeLighting = builder.apply("forceForgeLighting").comment("Force Forge Lighting Pipeline? (should be true when not using Optifine)").define("forceForgeLighting", true);
        extraGrassRarity = builder.apply("extraGrassRarity").comment("Inverse of the rarity of the extra grass. Increase the value to make it less common.").defineInRange("extraGrassRarity", 2, 1, Integer.MAX_VALUE);

        innerBuilder.pop();
    }
}
