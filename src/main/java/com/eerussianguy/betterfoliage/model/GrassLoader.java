package com.eerussianguy.betterfoliage.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.IModelLoader;

import com.eerussianguy.betterfoliage.Helpers;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GrassLoader implements IModelLoader<GrassModel>
{
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
        // do nothing
    }

    @Override
    public GrassModel read(JsonDeserializationContext deserializationContext, JsonObject json)
    {
        ResourceLocation dirt = Helpers.requireID(json, "dirt");
        ResourceLocation top = Helpers.requireID(json, "top");
        ResourceLocation overlay = Helpers.requireID(json, "overlay");
        boolean tint = GsonHelper.getAsBoolean(json, "tint", false);
        ResourceLocation grass = Helpers.identifierOrEmpty(json, "grass");

        return new GrassModel(dirt, top, overlay, tint, grass);
    }
}
