package com.gendeathrow.pmobs.core.configs;

import java.io.File;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.handlers.RaiderManager;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class MainConfig 
{

	public static File configDir = new File("config/raiders");
	
	public static Configuration config;
	
	private static String mobs = "Mobs";
	private static String mobai = "MobsAI";
	private static String classes = "raidersClasses";

	private static ConfigCategory remove = new ConfigCategory("remove");
	
	
	public static void preInit()
	{
		File file = new File(configDir, "settings.cfg");
		config = new Configuration(file);
		config.load();
		
		PMSettings.renderNameTags = config.getBoolean("Render Name Tags", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders Name tags about thier heads.");
		PMSettings.renderOverlays = config.getBoolean("Render Skin Overlays", Configuration.CATEGORY_CLIENT, true, "Renders the Raiders skins overlay features.");
	}
	
	public static void load()
	{
		RaidersMain.logger.log(Level.INFO, "Loading Configs...");  
		
		//PMSettings.noSpawnDays = config.get(Configuration.CATEGORY_GENERAL, "No Spawn Days", new int[0], "Certain days that raiders dont spawn").getIntList();
		
		PMSettings.whitelists = config.getStringList("WhiteLists", Configuration.CATEGORY_GENERAL, new String[0], "Meant for Twitch/Other sub whitelist. # One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");

		
		PMSettings.raidersSpawnWeight = config.getInt("SpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning");

		PMSettings.raidersMaxGroupSpawn = config.getInt("MaxSpawnGroup", mobs, 1, 1, 10, "Max Spawn group size");

		//PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		//PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		//PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.removeVanillaSpawners = config.getBoolean("Remove Vanilla Mob Spawner", mobs, false, "Remove Vanilla Mob spawners from Dungeon Hooks, So only raiders will have spawners & possible other modded mobs.");
			
		PMSettings.raidersSpawnerWeight = config.getInt("Mob Spawner Weight", mobs, 200, 1, 1000, "Changes dungeon spawner weight for raiders. Example is zombies are 200, where skeletons are 100.");
		
		//PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day above Y lvl 50");
		
		//PMSettings.factionsEnabled = config.getBoolean("Enable Factions", mobs, true, "If true Some Raiders can belong to a friendly/hostile faction. If false they are all hostile");
		
		PMSettings.daySpeedRestiction = config.getFloat("DayTime Speed Modifier", mobs, 1, 0, 2f, "Mulitply raiders speed during Daytime. 1 is normal speed.");

		PMSettings.shouldDaylightSpawm = config.getBoolean("Spawn in Daylight", mobs, true, "Raiders will spawn reguardless of light levels. Basically Daytime Spawning. False sets it to Vanilla Spawning rules");
		PMSettings.torchStopSpawns = config.getBoolean("Torches/GlowStone Stop Spawning", mobs, true, "GlowStone and torches will still stop a 7x7x7 spawn area. If cant see sky. Only works with 'Spawn in Daylight' == true");
		
		// Mob AI stuff
		PMSettings.leapAttackAI = config.getBoolean("Leap Attack", mobai, true, "Gives some Raiders the ability to leap attack, small chance increases with each raid difficulty");
		//PMSettings.veryHostile = config.getBoolean("Hostile to All", mobai, true, "Raiders will attack any Hostile mob that moves. Raiders will always attack passive mobs regaurdless of settings.");
		
		//TODO 
//		PMSettings.spawnNether = config.getBoolean("Enable Nether", mobs, true, "Spawn in Nether");
//		PMSettings.spawnEnd = config.getBoolean("Enable The End", mobs, false, "Spawn in The End");
		
		
		// Progression Difficulty stats
		
		/*
		String progCat = "Progessive Difficulty";
		config.addCustomCategoryComment(progCat, "You can set what the raiders get on each Difficulty Increase");
		
		PMSettings.HealthIncrease = config.getInt("Health Increase", progCat, 2, 10, 100, "Each point = Half a Heart.");
		PMSettings.HealthMaxOut = config.getInt("Health Max Outs", progCat, -1, -1, 100, "Sets what Raid Difficulty the health will max out at(Does not mean max health). -1 Means there is no max Difficulty.");
		
		PMSettings.BonusHealthPercentageIncrease = config.get(progCat, "Health Bonus Health Percentage Increase", 0.025D, "Adds Percentage Chance for raider to gain bonus health to each Raid Difficulty IE. (0.05 * Raid 5 = 25%)").getDouble();
		PMSettings.BonusHealthMaxPercentage = config.get(progCat, "Health Bonus Max Percentage", 0.10D, "Max Percentage of Raiders that can get bonus health based off (Health Bonus Health Percentage Increase)").getDouble(); 
		
		PMSettings.SpeedPercentageIncrease = config.get(progCat, "Speed Percentage Increase", 0.05D, "Adds Percentage Chance for raider to gain extra speed to each Raid Difficulty IE. (0.05 * Raid 5 = 25%)").getDouble();
		PMSettings.SpeedMaxPercentage = config.get(progCat, "Speed Max Percentage", 0.40D, "Max Percentage of Raiders that can get speed increase based off (Speed Raid Increase)").getDouble(); 
		
		if(config.hasKey(mobs, "Mob Difficulty Progression"))
		{
			config.moveProperty(mobs, "Mob Difficulty Progression", progCat);
			config.renameProperty(progCat, "Mob Difficulty Progression", "Raid Day Difficulty Progression");
		}
		  
		PMSettings.dayDifficultyProgression = getRaidDifficultyProgressions();
		if(config.hasKey(progCat, "Raid Difficulty Day Progression"))
		{
			config.moveProperty(progCat, "Raid Difficulty Day Progression", remove.getName());
		}
		
		 */	
		String items = "items";
 		//ESM stuff
		//PMSettings.dropSerum = config.getBoolean("Brutes Drop Serum Samples", items, true, "If false Brutes dont drop samples. THIS IS THE ONLY WAY TO GET THEM");
		//PMSettings.dropTransmitter = config.getBoolean("Drop Tramsmitter Part", items, true, "If false Raiders dont drop samples. THIS IS THE ONLY WAY TO GET THEM");

		config.removeCategory(remove);
		
		if(config.hasChanged())
			config.save();
	}
	
	public static int getRaidDifficultyProgressions()	{
		return config.getInt("Raid Day Difficulty Progression", "Progessive Difficulty", 5, 1, 100, "This is the Raid difficulty. Each set amount of days the mobs get harder(Raid Difficulty increases +1). Each x amount of days harder mobs have more of a chance to spawn its cumulative");
	}

	
	
	public static void PostLoad()
	{
		RaiderManager.readRaiderFile();

		//TODO
		//EquipmentManager.readEquipmentFile();
		
		// Load Kill Counter
		//TODO
		//KillCounter.initilize();
	}
	
	
}
