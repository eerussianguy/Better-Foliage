package com.eerussianguy.betterfoliage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.eerussianguy.betterfoliage.model.GrassBakedModel;
import com.eerussianguy.betterfoliage.model.GrassLoader;
import com.eerussianguy.betterfoliage.model.LeavesBakedModel;
import com.eerussianguy.betterfoliage.model.LeavesLoader;

public class EventHandler
{
    public static final Map<ParticleLocation, List<TextureAtlasSprite>> MAP = new HashMap<>();

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
        bus.addListener(EventHandler::onTextureStitch);
        bus.addListener(EventHandler::afterTextureStitch);
    }

    private static void clientSetup(final FMLClientSetupEvent event)
    {
        if (BFConfig.CLIENT.forceForgeLighting.get() && !OPTIFINE_LOADED.get())
        {
            ForgeConfig.CLIENT.experimentalForgeLightPipelineEnabled.set(true);
        }
    }

    private static void onModelBake(final ModelEvent.BakingCompleted event)
    {
        LeavesBakedModel.INSTANCES.forEach(LeavesBakedModel::init);
        GrassBakedModel.INSTANCES.forEach(GrassBakedModel::init);
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

    @SuppressWarnings("deprecation")
    private static void onTextureStitch(final TextureStitchEvent.Pre event)
    {
        final TextureAtlas atlas = event.getAtlas();
        final ResourceLocation location = atlas.location();
        if (location.equals(TextureAtlas.LOCATION_PARTICLES))
        {
            for (String[] array : ParticleLocation.getAllLocations())
            {
                for (String s : array)
                {
                    event.addSprite(Helpers.identifier("particle/" + s));
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private static void afterTextureStitch(final TextureStitchEvent.Post event)
    {
        final TextureAtlas atlas = event.getAtlas();
        ResourceLocation res = atlas.location();
        if (res.equals(TextureAtlas.LOCATION_PARTICLES))
        {
            for (ParticleLocation location : ParticleLocation.values())
            {
                MAP.put(location, getList(atlas, location.getLocations()));
            }
        }
    }

    private static List<TextureAtlasSprite> getList(TextureAtlas atlas, String... locations)
    {
        List<TextureAtlasSprite> list = new ArrayList<>();
        for (String s : locations)
        {
            list.add(atlas.getSprite(Helpers.identifier("particle/" + s)));
        }
        return list;
    }
}
