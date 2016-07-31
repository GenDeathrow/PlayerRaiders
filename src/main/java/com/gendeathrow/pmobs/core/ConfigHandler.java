package com.gendeathrow.pmobs.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.handlers.RaiderManager;

public class ConfigHandler 
{

	public static File configDir = new File("config/raiders");
	
	public static Configuration config;
	
	private static String mobs = "Mobs";
	
	public static void load()
	{
		RaidersCore.logger.log(Level.INFO, "Loading Configs...");
		
		Configuration config;
		File file = new File(configDir, "settings.cfg");
		
		config = new Configuration(file);
		
		config.load();
		
		PMSettings.daySpawnWeight = config.getInt("DaySpawnWeight", mobs, 20, 1, 1000, "Weight of Raiders spawning during Day  (Cuts into animal spawning)");
		
		PMSettings.NightSpawnWeight = config.getInt("NightSpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning during Night");

		PMSettings.dayMaxGroupSpawn = config.getInt("DayMaxSpawnGroup", mobs, 1, 1, 10, "Max Group Number of Raiders spawning during Day");
		
		PMSettings.nightMaxGroupSpawn = config.getInt("NightMaxSpawnGroup", mobs, 3, 1, 10, "Weight of Raiders spawning during Night");
		
		PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		PMSettings.dayDifficultyProgression = config.getInt("Mob Difficulty Progression", mobs, 5, 1, 100, "Each set amount of days the mobs get harder. Each x amount of days harder mobs have more of a chance to spawn its cumulative");
		
		PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.renderNameTags = config.getBoolean("Render Name Tags", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders Name tags about thier heads.");
		
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents Raiders from Spawning during first day");
		config.save();
		
		RaiderManager.readRaiderFile();
	
	}
}
