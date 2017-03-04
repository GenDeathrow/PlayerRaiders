package com.gendeathrow.pmobs.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
	private static String mobai = "MobsAI";
	private static String classes = "raidersClasses";
	private static String ESMHooks = "ESMControl";
	private static ConfigCategory remove = new ConfigCategory("remove");
	public static void load()
	{
		RaidersCore.logger.log(Level.INFO, "Loading Configs...");
		
		PMSettings.noSpawnDays = config.get(config.CATEGORY_GENERAL, "No Spawn Days", new int[0], "Certain days that raiders dont spawn").getIntList();
		  
		PMSettings.whitelists = config.getStringList("WhiteLists", config.CATEGORY_GENERAL, new String[0], "# One whitelist link per line. Example: http://whitelist.twitchapps.com/list.php?id=12345 [default: ]");
		
		//PMSettings.daySpawnPercentage = config.getFloat("DaySpawnPercentage", mobs, 0.45f, 0.01f, 1.0f, "Percentage of day time spawning vs Night time spawn weight.");
		
		PMSettings.raidersSpawnWeight = config.getInt("SpawnWeight", mobs, 10, 1, 1000, "Weight of Raiders spawning");

		PMSettings.raidersMaxGroupSpawn = config.getInt("MaxSpawnGroup", mobs, 1, 1, 10, "Max Spawn group size");
		
		PMSettings.setEquitmentDefault = config.getFloat("Set Equipment Defult Difficulty", mobs, .025f, .01f, 1f, "Sets Raiders default chance to wear equipment based on Easy and Medium difficulty");
		
		PMSettings.setEquptmentHard = config.getFloat("Set Equipment Hard Difficulty", mobs, .1f, .01f, 1f, "Sets Raiders chance to wear equipment based on Hard difficulty");
		
		PMSettings.dayDifficultyProgression = config.getInt("Mob Difficulty Progression", mobs, 5, 1, 100, "This is the Raid difficulty. Each set amount of days the mobs get harder(Raid Difficulty increases +1). Each x amount of days harder mobs have more of a chance to spawn its cumulative");
		
		//PMSettings.sprintersOnlyNight = config.getBoolean("Special Mobs Spawn at Night Only", mobs, false, "Hard/Fast/Special Mobs will only Spawn at night time only. (Except on hard days!)");
		
		PMSettings.removeVanillaSpawners = config.getBoolean("Remove Vanilla Mob Spawner", mobs, false, "Remove Vanilla Mob spawners from Dungeon Hooks");
		
		PMSettings.raidersSpawnerWeight = config.getInt("Mob Spawner Weight", mobs, 200, 1, 1000, "Changes dungeon spawner weight for raiders. Example is zombies are 200, where skeletons are 100.");
		
		PMSettings.safeForaDay = config.getBoolean("Safe for a Day", mobs, false, "Prevents All Mobs from Spawning during first day above Y lvl 50");
		
		PMSettings.factionsEnabled = config.getBoolean("Enable Factions", mobs, true, "If true Some Raiders can belong to a friendly/hostile faction. If false they are all hostile");
		
		PMSettings.leapAttackAI = config.getBoolean("Leap Attack", mobai, true, "Gives some Raiders the abilit to leap attack, small chance increases with each raid difficulty");
		
		PMSettings.daySpeedRestiction = config.getFloat("DayTime Speed Modifier", mobs, 0.4f, -1f, 2f, "Mulitply raiders speed during Daytime. 1 is normal speed.");
		
		config.addCustomCategoryComment(classes, "Edit Class Specific options. Adding Drops Example: 'ModID:Item:Meta:Qty:Chance(0.01 - 1)'");
		// Raiders Classes
		PMSettings.bruteClass = config.getBoolean("Brute Class Enabled", classes, true, "Adds Brute to the mix. They are slower, bigger, extra health, and hit alot harder");
		PMSettings.bruteWeight = config.get(classes, "Brute Weight", 7).getInt(7);
		PMSettings.bruteDrops =  ItemDrop.getArrayItemDrops(config.get(classes, "Brute Drops", new String[0]).getStringList());
		
		// ModName:ItemID:meta:qty:chance


		PMSettings.tweakersClass = config.getBoolean("Tweakers Class Enabled", classes, true, "Adds Tweakers to the mix. They are a lot faster,and have lower health");
		PMSettings.tweakerWeight = config.get(classes,"Tweakers Weight", 5).getInt(5);
		PMSettings.tweakerOnlyNight = config.get(classes, "Tweakers Only at Night", false).getBoolean();
		PMSettings.tweakerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Tweakers Drops", new String[0]).getStringList());

		PMSettings.pyroClass = config.getBoolean("Pyromaniac Class Enabled", classes, true, "Adds Pyromaniacs to the mix, they will seek out blocks to catch fire. small chance increases with each raid difficulty");
		PMSettings.pyroWeight = config.get(classes, "Pyromaniac Weight", 4).getInt(4);
		PMSettings.pyroDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Pyromaniac Drops", new String[0]).getStringList());
		
		PMSettings.noneWeight = config.get(classes, "No Class Weight", 80).getInt(80);
		PMSettings.noneDrops = ItemDrop.getArrayItemDrops(config.get(classes, "No Class Drops", new String[0], "No Class is a normal raider").getStringList());

		String[] screamerDrops = new String[3];
		screamerDrops[0] = Items.EXPERIENCE_BOTTLE.getRegistryName().toString() +":0:3:1";
		screamerDrops[1] = Items.GOLDEN_APPLE.getRegistryName().toString() +":0:1:1";
		screamerDrops[2] = Items.DRAGON_BREATH.getRegistryName().toString() +":0:1:0.01";

		PMSettings.screamerClass = config.getBoolean("Screamer Class Enabled", classes, true, "Adds Screamer to the mix, A type of witch class");
		PMSettings.screamerWeight = config.get(classes, "Screamer Weight", 2).getInt(2);
		PMSettings.screamerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Screamer Drops", screamerDrops).getStringList());
		  
		PMSettings.rangerClass = config.getBoolean("Ranger Class Enabled", classes, true, "Adds Ranger to the mix, These guys are Bow wielding maniacs.");
		PMSettings.rangerWeight = config.get(classes, "Ranger Weight", 10).getInt(10);
		PMSettings.rangerDrops = ItemDrop.getArrayItemDrops(config.get(classes, "Ranger Drops", new String[0]).getStringList());

		  
			  
		//ESM stuff
		PMSettings.esmDiggingPercentage = config.getFloat("Digging AI Percentage", "esm_addon", .3f, 0, 1, "Sets percentage of Raiders that will gain the Digging AI(RNG Gods make the true decisions)");
		PMSettings.esmDemolitionRaidDiff = config.getInt("Demolition AI Raid Difficulty", "esm_addon", 1, 0, 1000, "TNT mobs wont start till x Raid Difficulty based of x amount of dayd set in 'Mob Difficulty Progression' ");
		PMSettings.esmDemoPercentage = config.getFloat("Demolition AI Percentage", "esm_addon", .05f, 0, 1, "Sets percentage of Raiders that will gain the Demolition AI(RNG Gods make the true decisions) (Only regular gain demo)");

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
	
	public static class ItemDrop
	{
		String itemID;
		int metaID;
		double chance;
		int qty;
		
		public ItemDrop(String itemID, int metaID, double chance, int qty)
		{
			this.itemID = itemID;
			this.metaID = metaID;
			this.chance = chance;
			this.qty = qty;
		}
		
		public static ArrayList<ItemDrop> getArrayItemDrops(String[] itemList)
		{
			ArrayList<ItemDrop> drops = new ArrayList<ItemDrop>();
			
			for(String item : itemList)
			{
				try
				{
					drops.add(getItemDrop(item));
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			return drops;
		}
		
		public Item getItem()
		{
			return Item.getByNameOrId(this.itemID);
		}
		
		public int getMeta()
		{
			return this.metaID;
		}
		
		public static ItemDrop getItemDrop(String data) throws NumberFormatException
		{
			String[] split = data.split(":");

			String itemID = split[0]+":"+split[1];
			int metaID = Integer.parseInt(split[2]);
			double chance= Double.parseDouble(split[4]);
			int qty = Integer.parseInt(split[3]);
			
			
			return new ItemDrop(itemID, metaID,chance,qty);
		}
		
		public boolean shouldDrop(Random rand)
		{
			return rand.nextDouble() <= this.chance;
		}
		
		public ItemStack getStack(Random rand)
		{
			return new ItemStack(Item.getByNameOrId(this.itemID), this.qty > 1 ? rand.nextInt(this.qty-1)+1 : this.qty, this.metaID);
		}
	}
	
	
}
