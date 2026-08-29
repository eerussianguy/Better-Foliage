package com.eerussianguy.betterfoliage;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import com.eerussianguy.betterfoliage.model.GrassModel;
import com.eerussianguy.betterfoliage.model.LeavesModel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;
import net.neoforged.neoforge.client.config.NeoForgeClientConfig;

public class EventHandler
{
    private static final Supplier<Boolean> OPTIFINE_LOADED = Suppliers.memoize(() ->
    {
        try
        {
            Class.forName("net.optifine.Config");
            return true;
        }
        catch (ClassNotFoundException ignored)
        {
            return false;
        }
    });

    public static void init(IEventBus bus)
    {
        bus.addListener(EventHandler::clientSetup);
        bus.addListener(EventHandler::onBlockStateModelRegister);
        bus.addListener(EventHandler::afterTextureStitch);
    }

    private static void clientSetup(final FMLClientSetupEvent event)
    {
        if (BFConfig.CLIENT.forceForgeLighting.get() && !OPTIFINE_LOADED.get() && !ModList.get().isLoaded("oculus"))
        {
            NeoForgeClientConfig.INSTANCE.enhancedLighting.set(true);
        }
    }

    private static void afterTextureStitch(final TextureAtlasStitchedEvent event)
    {
        ForgeEventHandler.clearCache();
    }

    private static void onBlockStateModelRegister(final RegisterBlockStateModels event)
    {
        event.registerModel(LeavesModel.ID, LeavesModel.MAP_CODEC);
        event.registerModel(GrassModel.ID, GrassModel.MAP_CODEC);
    }
}
