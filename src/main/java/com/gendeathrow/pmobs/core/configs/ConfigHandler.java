package com.gendeathrow.pmobs.core.configs;

import com.gendeathrow.pmobs.client.LocalCustomSkinManager;
import com.gendeathrow.pmobs.handlers.EquipmentManager;

public class ConfigHandler {

	public static RaiderClassConfigs raiderClassConfigs = new RaiderClassConfigs(MainConfig.configDir);
	
	public static void preInit() {
		MainConfig.preInit();
	}
	
	
	public static void init() {
		MainConfig.load();
		raiderClassConfigs.readFile();
		LocalCustomSkinManager.setupDirectory();
	}
	
	public static void postInit() {
		MainConfig.PostLoad();
		EquipmentManager.readEquipmentFile();
	}
}
