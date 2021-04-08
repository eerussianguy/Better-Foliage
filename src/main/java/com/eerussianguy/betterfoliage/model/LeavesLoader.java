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
public class LeavesLoader implements IModelLoader<LeavesModel>
{
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        // do nothing
    }

    @Override
    public LeavesModel read(JsonDeserializationContext deserializer, JsonObject json)
    {
        ResourceLocation leaves = new ResourceLocation(json.get("leaves").getAsString());
        ResourceLocation fluff = new ResourceLocation(json.get("fluff").getAsString());

        return new LeavesModel(leaves, fluff);
    }
}
