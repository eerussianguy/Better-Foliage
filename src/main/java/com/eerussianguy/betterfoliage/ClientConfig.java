package com.eerussianguy.betterfoliage;

import java.util.function.Function;

import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

public class ClientConfig
{
    public final IntValue particleAttempts;
    public final IntValue particleDistance;

    public final BooleanValue souls;
    public final BooleanValue leaves;
    public final BooleanValue snowballs;

    public final IntValue leavesCacheSize;
    public final DoubleValue leavesVariationDistance;

    public final BooleanValue forceForgeLighting;

    ClientConfig(Builder innerBuilder)
    {
        Function<String, Builder> builder = name -> innerBuilder.translation(MOD_ID + ".config.server." + name);

        innerBuilder.push("general");

        particleAttempts = builder.apply("particleAttempts").comment("Attempts per tick to spawn a particle").defineInRange("particleAttempts", 2, 0, Integer.MAX_VALUE);
        particleDistance = builder.apply("particleDistance").comment("Horizontal and Vertical distance particles will spawn from").defineInRange("particleDistance", 15, 0, Integer.MAX_VALUE);
        souls = builder.apply("souls").comment("Enable Souls?").define("souls", true);
        leaves = builder.apply("leaves").comment("Enable Leaves?").define("leaves", true);
        snowballs = builder.apply("snowballs").comment("Enable Snowballs?").define("snowballs", true);
        leavesCacheSize = builder.apply("leavesCacheSize").comment("Determines the size of the leaves cache. Number of models cached per leaf block will be the number you input to the third power. Bigger cache = more RAM, but more variation and less z-fighting as a result").worldRestart().defineInRange("leavesCacheSize", 7, 5, 20);
        leavesVariationDistance = builder.apply("leavesVariationDistance").comment("Determines the max distance leaves block fluff can deviate from the actual block. 0.0 means no distance variation (all fluff is in the middle of the block)").worldRestart().defineInRange("leavesVariationDistance", 2.75f, 0f, 7f);
        forceForgeLighting = builder.apply("forceForgeLighting").comment("Force Forge Lighting Pipeline? (should be true when not using Optifine)").define("forceForgeLighting", true);

        innerBuilder.pop();
    }
}
