package com.gendeathrow.pmobs.core;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;

public class ConfigHandler 
{

	public static File configDir = new File("config/raiders");
	
	public static Configuration config;
	
	private static String mobs = "Mobs";
	private static ConfigCategory remove = new ConfigCategory("remove");
	public static void load()
	{
		RaidersCore.logger.log(Level.INFO, "Loading Configs...");
		
		PMSettings.whitelists = config.getStringList("WhiteLists", config.CATEGORY_GENERAL, new String[0], "# One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");

		if(config.hasKey(mobs,"DayMaxSpawnGroup"))
		{
			PMSettings.dayMaxGroupSpawn = config.getInt("DayMaxSpawnGroup", mobs, 1, 1, 10, "Max Group Number of Raiders spawning during Day");
			config.moveProperty(mobs, "DayMaxSpawnGroup", remove.getName());
		}

		if(config.hasKey(mobs,"DaySpawnWeight"))
		{
			PMSettings.daySpawnWeight = config.getInt("DaySpawnWeight", mobs, 20, 1, 1000, "Weight of Raiders spawning during Day  (Cuts into animal spawning)");
			config.moveProperty(mobs, "DaySpawnWeight", remove.getName());
		}
		
		PMSettings.daySpawnPercentage = config.getFloat("DaySpawnPercentage", mobs, 0.45f, 0.01f, 1.0f, "Percentage of day time spawning vs Night time spawn weight.");
		
		PMSettings.NightSpawnWeight = config.getInt("NightSpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning during Night");

		PMSettings.nightMaxGroupSpawn = config.getInt("NightMaxSpawnGroup", mobs, 3, 1, 10, "Weight of Raiders spawning during Night");
		
		PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		PMSettings.dayDifficultyProgression = config.getInt("Mob Difficulty Progression", mobs, 5, 1, 100, "Each set amount of days the mobs get harder. Each x amount of days harder mobs have more of a chance to spawn its cumulative");
		
		PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day");
		
		
		config.removeCategory(remove);
		config.save();
		

	
		
		// Load Kill Counter
		
		KillCounter.initilize();
	}
	
	public static void preInit()
	{
		File file = new File(configDir, "settings.cfg");
		
		config = new Configuration(file);
		
		config.load();
		
		
		PMSettings.renderNameTags = config.getBoolean("Render Name Tags", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders Name tags about thier heads.");
		
		PMSettings.renderOverlays = config.getBoolean("Render Skin Overlays", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders skins overlay features.");
	}
	
	
	public static void PostLoad()
	{
		RaiderManager.readRaiderFile();
		
		EquipmentManager.readEquipmentFile();
	}
}
