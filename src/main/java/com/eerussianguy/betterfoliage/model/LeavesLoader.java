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
public class LeavesLoader implements IModelLoader<LeavesModel>
{
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager)
    {
        // do nothing
    }

    @Override
    public LeavesModel read(JsonDeserializationContext deserializer, JsonObject json)
    {
        ResourceLocation leaves = Helpers.requireID(json, "leaves");
        ResourceLocation fluff = Helpers.requireID(json, "fluff");
        ResourceLocation overlay = Helpers.identifierOrEmpty(json, "overlay");
        boolean tintLeaves = GsonHelper.getAsBoolean(json, "tintLeaves", true);
        boolean tintOverlay = GsonHelper.getAsBoolean(json, "tintOverlay", false);

        return new LeavesModel(leaves, fluff, overlay, tintLeaves, tintOverlay);
    }
}
