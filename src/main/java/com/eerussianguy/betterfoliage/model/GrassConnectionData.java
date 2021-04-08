package com.eerussianguy.betterfoliage.model;

public class GrassConnectionData implements IModelDataBlank
{
    private final int meta;

    public GrassConnectionData(boolean north, boolean east, boolean south, boolean west)
    {
        int i = 0;
        if (north)
            i |= 1;
        if (east)
            i |= 2;
        if (south)
            i |= 4;
        if (west)
            i |= 8;

        meta = i;
    }

    public int get()
    {
        return meta;
    }
}
