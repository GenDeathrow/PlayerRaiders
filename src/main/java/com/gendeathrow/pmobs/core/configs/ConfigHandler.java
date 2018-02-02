package com.gendeathrow.pmobs.core.configs;

public class ConfigHandler {

	public static RaiderClassConfigs raiderClassConfigs = new RaiderClassConfigs(MainConfig.configDir);
	
	public static void preInit() {
		MainConfig.preInit();
	}
	
	
	public static void init() {
		MainConfig.load();
		raiderClassConfigs.readFile();

	}
	
	public static void postInit() {
		MainConfig.PostLoad();
	}
}
