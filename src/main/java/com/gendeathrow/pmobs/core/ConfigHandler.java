package com.gendeathrow.pmobs.core;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;

public class ConfigHandler 
{

	public static File configDir = new File("config/raiders");
	
	public static Configuration config;
	
	private static String mobs = "Mobs";
	private static String mobai = "MobsAI";
	private static String classes = "raidersClasses";
	private static String ESMHooks = "ESMControl";
	private static ConfigCategory remove = new ConfigCategory("remove");
	public static void load()
	{
		RaidersCore.logger.log(Level.INFO, "Loading Configs...");
		
		PMSettings.whitelists = config.getStringList("WhiteLists", config.CATEGORY_GENERAL, new String[0], "# One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");
		
		//PMSettings.daySpawnPercentage = config.getFloat("DaySpawnPercentage", mobs, 0.45f, 0.01f, 1.0f, "Percentage of day time spawning vs Night time spawn weight.");
		
		PMSettings.NightSpawnWeight = config.getInt("SpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning during Night");

		PMSettings.nightMaxGroupSpawn = config.getInt("MaxSpawnGroup", mobs, 3, 1, 10, "Max Spawn group size");
		
		PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		PMSettings.dayDifficultyProgression = config.getInt("Mob Difficulty Progression", mobs, 5, 1, 100, "Each set amount of days the mobs get harder. Each x amount of days harder mobs have more of a chance to spawn its cumulative");
		
		PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.removeVanillaSpawners = config.getBoolean("Remove Vanilla Mob Spawner", mobs, false, "Remove Vanilla Mob spawners from Dungeon Hooks");
		
		PMSettings.raidersSpawnerWeight = config.getInt("Mob Spawner Weight", mobs, 200, 1, 1000, "Changes dungeon spawner weight for raiders. Example is zombies are 200, where skeletons are 100.");
		
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day");
		
		PMSettings.factionsEnabled = config.getBoolean("Enable Factions", mobs, true, "If true Some Raiders can belong to a friendly/hostile faction. If false they are all hostile");
		
		PMSettings.leapAttackAI = config.getBoolean("Leap Attack", mobai, true, "Gives some Raiders the abilit to leap attack, small chance increases with each raid difficulty");
		

		// Raiders Classes
		PMSettings.bruteClass = config.getBoolean("Brute Class Enabled", classes, true, "Adds Brute to the mix. They are slower, bigger, extra health, and hit alot harder");
		//PMSettings.bruteWeight = config.getInt("Brute Weight", classes, 5, 1, 1000, "");
		PMSettings.bruteWeight = config.get(classes, "Brute Weight", 5).getInt(5);

		PMSettings.tweakersClass = config.getBoolean("Tweakers Class Enabled", classes, true, "Adds Tweakers to the mix. They are a lot faster,and have lower health");
		//PMSettings.tweakerWeight = config.getInt("Tweakers Weight", classes, 5, 1, 1000, "");
		PMSettings.tweakerWeight = config.get(classes,"Tweakers Weight", 5).getInt(5);
		
		
		PMSettings.pyroClass = config.getBoolean("Pyromaniac Class Enabled", classes, true, "Adds Pyromaniacs to the mix, they will seek out blocks to catch fire. small chance increases with each raid difficulty");
		//PMSettings.pyroWeight = config.getInt("Pyromaniac Weight", classes, 5, 1, 1000, "");
		PMSettings.pyroWeight = config.get(classes, "Pyromaniac Weight", 2).getInt(2);
		
		PMSettings.noneWeight = config.get(classes, "No Class Weight", 80, "No Class is a normal raider").getInt();

		config.removeCategory(remove);
		
		if(config.hasChanged())
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
