package com.eerussianguy.betterfoliage;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class BetterFoliage
{
    private static final Logger LOGGER = LogManager.getLogger();

    public static Logger getLogger()
    {
        return LOGGER;
    }

    public static final String MOD_ID = "betterfoliage";

    public BetterFoliage()
    {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        BFConfig.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        if (!ModList.get().isLoaded("optifine"))
        {
            ForgeConfig.CLIENT.experimentalForgeLightPipelineEnabled.set(true);
        }
    }
}
