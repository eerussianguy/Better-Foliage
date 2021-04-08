package com.eerussianguy.betterfoliage.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

import mcp.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GrassLoader implements IModelLoader<GrassModel>
{
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        // do nothing
    }

    @Override
    public GrassModel read(JsonDeserializationContext deserializationContext, JsonObject json)
    {
        ResourceLocation dirt = new ResourceLocation(json.get("dirt").getAsString());
        ResourceLocation top = new ResourceLocation(json.get("top").getAsString());
        ResourceLocation overlay = new ResourceLocation(json.get("overlay").getAsString());
        boolean tint = json.get("tint").getAsBoolean();

        return new GrassModel(dirt, top, overlay, tint);
    }
}
