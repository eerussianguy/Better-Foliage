package com.eerussianguy.betterfoliage;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod(MOD_ID)
public class BetterFoliage
{
    public static final String MOD_ID = "betterfoliage";

    public BetterFoliage(ModContainer mod, IEventBus bus)
    {
        if (FMLEnvironment.getDist() == Dist.CLIENT)
        {
            EventHandler.init(bus);
            ForgeEventHandler.init();
        }

        mod.registerConfig(ModConfig.Type.CLIENT, BFConfig.SPEC);
    }


}
