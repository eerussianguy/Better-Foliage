package com.eerussianguy.betterfoliage.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelLoader;

import com.eerussianguy.betterfoliage.Helpers;
import mcp.MethodsReturnNonnullByDefault;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

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
        JsonElement overElement = json.get("overlay");
        ResourceLocation overlay = overElement != null ? new ResourceLocation(overElement.getAsString()) : Helpers.EMPTY;
        JsonElement tintElement1 = json.get("tintLeaves");
        boolean tintLeaves = tintElement1 == null || tintElement1.getAsBoolean(); // default TRUE
        JsonElement tintElement2 = json.get("tintOverlay");
        boolean tintOverlay = tintElement2 != null && tintElement2.getAsBoolean(); // default FALSE

        return new LeavesModel(leaves, fluff, overlay, tintLeaves, tintOverlay);
    }
}
