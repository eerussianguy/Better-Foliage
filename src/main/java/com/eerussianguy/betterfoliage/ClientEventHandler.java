package com.eerussianguy.betterfoliage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.eerussianguy.betterfoliage.model.GrassBakedModel;
import com.eerussianguy.betterfoliage.model.GrassLoader;
import com.eerussianguy.betterfoliage.model.LeavesBakedModel;
import com.eerussianguy.betterfoliage.model.LeavesLoader;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler
{
    public static final Map<ParticleLocation, List<TextureAtlasSprite>> MAP = new HashMap<>();

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event)
    {
        LeavesBakedModel.INSTANCES.forEach(LeavesBakedModel::init);
        GrassBakedModel.INSTANCES.forEach(GrassBakedModel::init);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        ModelLoaderRegistry.registerLoader(Helpers.identifier("leaves"), new LeavesLoader());
        ModelLoaderRegistry.registerLoader(Helpers.identifier("grass"), new GrassLoader());

        ForgeModelBakery.addSpecialModel(Helpers.identifier("block/better_grass"));
        ForgeModelBakery.addSpecialModel(Helpers.identifier("block/better_grass_snowed"));
        ForgeModelBakery.addSpecialModel(Helpers.identifier("block/better_mycelium"));
    }

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void onTextureStitch(TextureStitchEvent.Pre event)
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

    @SubscribeEvent
    @SuppressWarnings("deprecation")
    public static void afterTextureStitch(TextureStitchEvent.Post event)
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
