package com.instanteat;

import com.instanteat.config.InstantEatConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstantEatMod implements ModInitializer {
	public static final String MOD_ID = "instanteat-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static InstantEatConfig config;

	@Override
	public void onInitialize() {
		AutoConfig.register(InstantEatConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(InstantEatConfig.class).getConfig();

		LOGGER.info("InstantEatMod has been successfully loaded! Food won't hold you up anymore.");
	}
}