package com.instanteat.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "instanteat")
public class InstantEatConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean restoreFullHunger = false;

}