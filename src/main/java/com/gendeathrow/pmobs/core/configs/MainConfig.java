package com.gendeathrow.pmobs.core.configs;

import java.io.File;
import java.util.Arrays;

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
		PMSettings.renderRaidNotifications = config.getBoolean("Show Raid Notification GUI", Configuration.CATEGORY_CLIENT, true, "Renders the Raid Notifications");
	}
	
	public static void load()
	{
		RaidersMain.logger.log(Level.INFO, "Loading Configs...");  
		
		//PMSettings.noSpawnDays = config.get(Configuration.CATEGORY_GENERAL, "No Spawn Days", new int[0], "Certain days that raiders dont spawn").getIntList();
		
		PMSettings.whitelists = config.getStringList("WhiteLists", Configuration.CATEGORY_GENERAL, new String[0], "Meant for Twitch/Other sub whitelist. # One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");

		PMSettings.removeVanillaSpawners = config.getBoolean("Remove Vanilla Mob Spawner", mobs, false, "Remove Vanilla Mob spawners from Dungeon Hooks, So only raiders will have spawners & possible other modded mobs.");
			
		PMSettings.raidersSpawnerWeight = config.getInt("Mob Spawner Weight", mobs, 200, 1, 1000, "Changes dungeon spawner weight for raiders. Example is zombies are 200, where skeletons are 100.");

		PMSettings.daySpeedRestiction = config.getFloat("DayTime Speed Modifier", mobs, 1, 0, 2f, "Mulitply raiders speed during Daytime. 1 is normal speed.");

		PMSettings.shouldDaylightSpawm = config.getBoolean("Spawn in Daylight", mobs, false, "Raiders will spawn regardless of light levels. Basically Daytime Spawning. False sets it to Vanilla Spawning rules");

		PMSettings.torchStopSpawns = config.getBoolean("Torches/GlowStone Stop Spawning", mobs, true, "GlowStone and torches will still stop a 7x7x7 spawn area. If cant see sky. Only works with 'Spawn in Daylight' == true");
	
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day above Y lvl 50");
		
		PMSettings.factionsEnabled = config.getBoolean("Enable Factions", mobs, true, "If true Some Raiders can belong to a friendly/hostile faction. If false they are all hostile");
		
		config.setCategoryPropertyOrder(mobs, Arrays.asList("WhiteLists", 
																"Remove Vanilla Mob Spawner",
																"Mob Spawner Weight",
																"Spawn in Daylight",
																"Safe for a Day",
																"Torches/GlowStone Stop Spawning",
				 												"DayTime Speed Modifier",
				 												"Enable Factions"));


		
		// Mob AI stuff
		PMSettings.leapAttackAI = config.getBoolean("Leap Attack", mobai, true, "Gives some Raiders the ability to leap attack, small chance increases with each raid difficulty");
		PMSettings.veryHostile = config.getBoolean("Hostile to All", mobai, true, "Raiders will attack any Hostile mob that moves. Raiders will always attack passive mobs regaurdless of settings.");
		PMSettings.mobsAttackCreepers = config.getBoolean("Attack Creepers", mobai, true, "Set AI to attack creepers");
		
		// Progression Difficulty stats
		

		String progCat = "Progessive Raid Difficulty";
		config.addCustomCategoryComment(progCat, "You can set what the raiders get on when ever the Difficulty increases.");
		
		
		PMSettings.isDifficultyProgressionEnabled = config.getBoolean("Enable Difficulty Progression", progCat, true, "If you want to use progressive raider difficulty set true. If not raiders will stay at default stats. ");
		PMSettings.dayDifficultyProgression = config.getInt("Raid Difficulty Progression", progCat, 5, 1, 500, "This is the Raid difficulty. Each set amount of days the mobs get harder(Raid Difficulty increases +1). \n Every x Days you set the Mobs will get more difficult. So if you set it to 5, every 5 days mobs get harder. ");
		
		PMSettings.armorOverrideChance = config.getFloat("Added Armor Chance", progCat, 0, 0, 1, "Adds an override for raiders chance to get armor. Only active when Raid Difficulty is turned off");
		PMSettings.setEquitmentDefault = config.getFloat("Equipment Chance (Easy/Normal)", progCat, 0.025f, 0, 1, "A raider may only get part of his armor/weapon set, Setting this to a higher number will give hime a better chance of getting more pieces [For Easy & Normal Difficulty]");
		PMSettings.setEquptmentHard = config.getFloat("Equipment Chance (Hard)", progCat, 0.1f, 0, 1, "A raider may only get part of his armor/weapon set, Setting this to a higher number will give hime a better chance of getting more pieces [For Hard Difficulty]");
		
		PMSettings.HealthIncrease = config.getInt("Health Increase", progCat, PMSettings.HealthIncrease, 10, 100, "Each point = Half a Heart.");
		PMSettings.HealthMaxOut = config.getInt("Health Max", progCat, PMSettings.HealthMaxOut, -1, 100, "Sets what Raid Difficulty the health will max out at(Does not mean max health). -1 Means there is no max Difficulty.");
		
		PMSettings.armorIncrease = config.get(progCat, "Armor Increase", PMSettings.armorIncrease, "Adds Armor defense points").getDouble();
		PMSettings.armorMax = config.get(progCat, "Armor Max", PMSettings.armorMax, "Max bonus armor defense points. ").getDouble(); 
		
		PMSettings.SpeedIncrease = config.get(progCat, "Speed Increase", PMSettings.SpeedIncrease, "Increases the speed of raider by x amt.").getDouble();
		PMSettings.SpeedMaxIncrease = config.get(progCat, "Speed Max", PMSettings.SpeedMaxIncrease, "Max bonus that can be applied to a raider. Raiders start out at '.25' + speed bonus. \n Other mob examples: Zombies 0.23, creepers 0.25, & enderman 0.30").getDouble(); 
		
		PMSettings.dmgIncrease = config.get(progCat, "Damge Increase", PMSettings.dmgIncrease, "Damage delt in Half Hearts. 2 = 1 heart").getDouble();
		PMSettings.dmgMaxIncrease = config.get(progCat, "Damge Max", PMSettings.dmgMaxIncrease, "Max amout of bonus damage raiders can recieve.").getDouble();
		
		PMSettings.knockbackIncrease = config.get(progCat, "Knockback Resistance Increase", PMSettings.knockbackIncrease, "Adds knockback Resistance to raider. 0 = 0%  / 1 = 100%").getDouble();
		PMSettings.knockbackMax = config.get(progCat, "Knockback Resistance  Max", PMSettings.knockbackMax, "Max bonus they can get on top of what every they may have. ").getDouble();

	
		config.setCategoryPropertyOrder(progCat, Arrays.asList("Enable Difficulty Progression", 
																"Raid Difficulty Progression",
																"Added Armor Chance",
																"Equipment Chance (Easy/Normal)",
																"Equipment Chance (Hard)",
																"Health Increase",
																"Health Max",
																 "Armor Increase",
																 "Armor Max",
																 "Speed Increase",
																 "Speed Max",
																 "Damge Increase",
																 "Damge Max",
																 "Knockback Resistance Increase",
																 "Knockback Resistance  Max"));
		

		
		String baseCat = "Raider Base Stats";
		config.addCustomCategoryComment(baseCat, "Set Base stats of a raider. All classes use these base stats and will add/multiply the stats for that class");
		
		PMSettings.movementSpeedStat = config.get(baseCat, "Movement Speed", PMSettings.movementSpeedStat).getDouble();
		PMSettings.attackDamageStat = config.get(baseCat, "Attack Damage", PMSettings.attackDamageStat).getDouble();
		PMSettings.armorStat = config.get(baseCat, "Armor", PMSettings.armorStat).getDouble();
		PMSettings.maxHealthStat = config.get(baseCat, "Max Health", PMSettings.maxHealthStat).getDouble();
		
		config.removeCategory(remove);
		
		if(config.hasChanged())
			config.save();
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
