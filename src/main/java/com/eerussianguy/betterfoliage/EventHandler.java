package com.eerussianguy.betterfoliage;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.eerussianguy.betterfoliage.model.GrassBakedModel;
import com.eerussianguy.betterfoliage.model.GrassLoader;
import com.eerussianguy.betterfoliage.model.LeavesBakedModel;
import com.eerussianguy.betterfoliage.model.LeavesLoader;

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

    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(EventHandler::clientSetup);
        bus.addListener(EventHandler::onModelBake);
        bus.addListener(EventHandler::onModelRegister);
        bus.addListener(EventHandler::onLoaderRegister);
        bus.addListener(EventHandler::afterTextureStitch);
    }

    private static void clientSetup(final FMLClientSetupEvent event)
    {
        if (BFConfig.CLIENT.forceForgeLighting.get() && !OPTIFINE_LOADED.get() && !ModList.get().isLoaded("oculus"))
        {
            ForgeConfig.CLIENT.experimentalForgeLightPipelineEnabled.set(true);
        }

        if (ModList.get().isLoaded("tfc"))
        {
            BetterFoliage.LEAVES_DISABLED_BY_MOD = true;
        }
    }

    private static void onModelBake(final ModelEvent.BakingCompleted event)
    {
        LeavesBakedModel.INSTANCES.forEach(LeavesBakedModel::init);
        GrassBakedModel.INSTANCES.forEach(GrassBakedModel::init);
    }

    private static void afterTextureStitch(final TextureStitchEvent.Post event)
    {
        ForgeEventHandler.clearCache();
    }

    private static void onLoaderRegister(final ModelEvent.RegisterGeometryLoaders event)
    {
        event.register("leaves", new LeavesLoader());
        event.register("grass", new GrassLoader());
    }

    private static void onModelRegister(final ModelEvent.RegisterAdditional event)
    {
        event.register(Helpers.identifier("block/better_grass"));
        event.register(Helpers.identifier("block/better_grass_snowed"));
        event.register(Helpers.identifier("block/better_mycelium"));
    }
}
