package com.eerussianguy.betterfoliage;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import com.eerussianguy.betterfoliage.compat.FoliageIntegrator;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod(MOD_ID)
public class BetterFoliage
{
    public static final String MOD_ID = "betterfoliage";

    public static FoliageIntegrator FOLIAGE = FoliageIntegrator.Vanilla.INSTANCE;

    public BetterFoliage()
    {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "Nothing", (remote, isServer) -> true));

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            EventHandler.init();
            ForgeEventHandler.init();
        }

        BFConfig.init();
    }


}
