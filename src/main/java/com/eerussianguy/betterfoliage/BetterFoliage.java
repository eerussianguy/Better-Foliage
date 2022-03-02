package com.eerussianguy.betterfoliage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod(MOD_ID)
public class BetterFoliage
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "betterfoliage";

    public BetterFoliage()
    {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "Nothing", (remote, isServer) -> true));

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            EventHandler.init();
            ForgeEventHandler.init();
        }

        BFConfig.init();
    }


}
