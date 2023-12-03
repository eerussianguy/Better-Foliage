package com.eerussianguy.betterfoliage;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import static com.eerussianguy.betterfoliage.BetterFoliage.MOD_ID;

@Mod(MOD_ID)
public class BetterFoliage
{
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "betterfoliage";

    public static boolean LEAVES_DISABLED_BY_MOD = false;

    public BetterFoliage()
    {
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            EventHandler.init();
            ForgeEventHandler.init();
        }

        BFConfig.init();
    }


}
