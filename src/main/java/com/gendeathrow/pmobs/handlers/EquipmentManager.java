package com.gendeathrow.pmobs.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.core.configs.MainConfig;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.gendeathrow.pmobs.handlers.random.EquipmentWeigthedItem;
import com.gendeathrow.pmobs.handlers.random.ItemHolder;
import com.gendeathrow.pmobs.handlers.random.PotionItemHolder;
import com.gendeathrow.pmobs.util.JsonHelper;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
//TODO
public class EquipmentManager 
{

	public static ArrayList<ArmorSetWeigthedItem> armorList = new ArrayList<ArmorSetWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> mainHandList = new ArrayList<EquipmentWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> offHandList = new ArrayList<EquipmentWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> tippedArrows = new ArrayList<EquipmentWeigthedItem>();
	
	public static final File equipmentFile = new File(MainConfig.configDir, "equipment.json");
	
	public static final File equimentFolder = new File(MainConfig.configDir, "equipment");
	public static final File armorFile = new File(equimentFolder, "armor.json");
	public static final File mainHandFile = new File(equimentFolder, "main_hand.json");
	public static final File offHandFile = new File(equimentFolder, "off_hand.json");
	public static final File arrorwsFile = new File(equimentFolder, "tipped_arrows.json");
	
	public static final Random rand = new Random();
	
	private static boolean markDirty = false;
	
	public static ArrayList<String> ErrorList = new ArrayList<String>();
	
	static
	{

	}
	
	// ServerSide call
	public static ArmorSetWeigthedItem getRandomArmor()
	{
		return (ArmorSetWeigthedItem)WeightedRandom.getRandomItem(rand, armorList);
	}

	public static EquipmentWeigthedItem getRandomWeapons()
	{
		return (EquipmentWeigthedItem)WeightedRandom.getRandomItem(rand, mainHandList);
	}
	
	public static EquipmentWeigthedItem getRandomOffHand()
	{
		return (EquipmentWeigthedItem)WeightedRandom.getRandomItem(rand, offHandList);
	}
	
	public static EquipmentWeigthedItem getRandomArrows()
	{
		return (EquipmentWeigthedItem)WeightedRandom.getRandomItem(rand, tippedArrows);
	}
	
	private static List<ArmorSetWeigthedItem> getWeightedList()
	{
		return (List<ArmorSetWeigthedItem>) Lists.newArrayList(armorList);
	}
	
	public static void Save()
	{
		if(markDirty)
		{
			//saveEquipmentFile();
			markDirty = false;
		}
	}
	
	public static void readEquipmentFile()
	{
			GenerateDefault();
			
	        if (equimentFolder.isDirectory())
	        {
	            try
	            {
	            	LoadEquipment();
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	            	RaidersMain.logger.error((String)("Couldn\'t parse Equipment file " + equipmentFile), (Throwable)jsonparseexception);
	            }
	        }
	}

	private static void LoadEquipment() 
	{
		armorList.clear();
		offHandList.clear();
		mainHandList.clear();
		
		ErrorList.clear();
		
        if (armorFile.isFile())
            try {
        		LoadArmorFile(JsonHelper.ReadJsonFile(armorFile)); }
            catch (JsonParseException jsonparseexception) {
            	RaidersMain.logger.error((String)("Couldn\'t parse Equipment file " + equipmentFile), (Throwable)jsonparseexception); }

        if (mainHandFile.isFile())
            try {
        		LoadMainHandFile(JsonHelper.ReadJsonFile(mainHandFile)); }
            catch (JsonParseException jsonparseexception) {
            	RaidersMain.logger.error((String)("Couldn\'t parse Equipment file " + mainHandFile), (Throwable)jsonparseexception); }

        if (offHandFile.isFile())
            try {
            	LoadOffHandFile(JsonHelper.ReadJsonFile(offHandFile)); }
            catch (JsonParseException jsonparseexception) {
            	RaidersMain.logger.error((String)("Couldn\'t parse Equipment file " + offHandFile), (Throwable)jsonparseexception); }

        if (arrorwsFile.isFile())
            try {
            	loadTippedArrowsFile(JsonHelper.ReadJsonFile(arrorwsFile)); }
            catch (JsonParseException jsonparseexception) {
            	RaidersMain.logger.error((String)("Couldn\'t parse Equipment file " + arrorwsFile), (Throwable)jsonparseexception); }

		RaidersMain.logger.info("MainHandList Size: "+ mainHandList.size());
		RaidersMain.logger.info("OffHand Size: "+ offHandList.size());
		RaidersMain.logger.info("Armor Sets Size: "+ armorList.size());
		RaidersMain.logger.info("Tipped Arrows Size: "+ tippedArrows.size());
		RaidersMain.logger.info("Errors: "+ ErrorList.size());
	}
	

	public static void saveEquipmentFile()
	{
	    	FileOutputStream fo = null; 
	        try
	        {
	        	fo = FileUtils.openOutputStream(equipmentFile);
	        			
	            fo.close(); // don't swallow close Exception if copy completes normally
	        }
	        catch (IOException ioexception)
	        {
	        	RaidersMain.logger.error((String)"Couldn\'t save stats", (Throwable)ioexception);
	        }finally 
	        {
	        	IOUtils.closeQuietly(fo);
	        }
	}

	public static void LoadArmorFile(JsonElement jsonElement) {
		
		 Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
		
			while(iterator.hasNext())
			{
				JsonObject data = iterator.next().getAsJsonObject();;
				ArmorSetWeigthedItem weightedArmor = null;
				String name; 
				ItemHolder head = null; 
				ItemHolder body = null; 
				ItemHolder legs = null; 
				ItemHolder feet = null; 
				
				boolean fullSet = false;
				int weight = 10;
				
				if(data.has("name")) name = data.get("name").getAsString(); 
				if(data.has("head")) head = new ItemHolder().readJsonObject(data.get("head").getAsJsonObject());
				if(data.has("body")) body = new ItemHolder().readJsonObject(data.get("body").getAsJsonObject());
				if(data.has("legs")) legs = new ItemHolder().readJsonObject(data.get("legs").getAsJsonObject());
				if(data.has("feet")) feet = new ItemHolder().readJsonObject(data.get("feet").getAsJsonObject());
				
				
				if(data.has("always spawn full set")) fullSet = data.get("always spawn full set").getAsBoolean();
				if(data.has("weight"))	weight = data.get("weight").getAsInt();

				weightedArmor = new ArmorSetWeigthedItem(head, body, legs, feet, weight, fullSet);
				
				if(weightedArmor != null)
				{
					armorList.add(weightedArmor);
				}
				
			}
	}
	
	public static void LoadMainHandFile(JsonElement jsonElement) {
		
		 Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
			
			while(iterator.hasNext())
			{
				JsonObject data = iterator.next().getAsJsonObject();;
				
				EquipmentWeigthedItem weightedItem = null;
				int weight = 10;
				
				weight = data.get("weight").getAsInt();
				
				ItemHolder itemHolder = new ItemHolder().readJsonObject(data);
				
				weightedItem = new EquipmentWeigthedItem(itemHolder, weight);
				
				if(weightedItem != null)
				{
					mainHandList.add(weightedItem);
				}
		}
	}
	
	public static void LoadOffHandFile(JsonElement jsonElement) {
		
		 Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
			
			while(iterator.hasNext())
			{
				JsonObject data = iterator.next().getAsJsonObject();
				
				EquipmentWeigthedItem weightedItem = null;
				
				String itemID = null; JsonObject itemNBT = null;
				int weight = 10;
				
				if(data.has("weight"))	weight = data.get("weight").getAsInt();
				
				ItemHolder itemHolder = new ItemHolder().readJsonObject(data);
				
				weightedItem = new EquipmentWeigthedItem(itemHolder, weight);  
				
				if(weightedItem != null)
				{
					offHandList.add(weightedItem);
				}
			}
		
	}
	
	public static void loadTippedArrowsFile(JsonElement jsonElement) {
	
		
		 Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
			
			while(iterator.hasNext())
			{
				JsonObject arrowSet = iterator.next().getAsJsonObject();
				
				ItemHolder PotionItemHolder = new PotionItemHolder(Items.TIPPED_ARROW);

				EquipmentWeigthedItem weightedItem = null;

				int weight = 10;
				
				if(arrowSet.getAsJsonObject().has("weight"))	weight = arrowSet.getAsJsonObject().get("weight").getAsInt();
				
				PotionItemHolder.readJsonObject(arrowSet.getAsJsonObject());
				
				weightedItem = new EquipmentWeigthedItem(PotionItemHolder, weight);  
				
				if(weightedItem != null)
				{
					tippedArrows.add(weightedItem);
				
				}
			}
	}
	
	public static File GenerateDefault()
	{
	        	
        	if (!equimentFolder.exists())
        	{
        		equimentFolder.mkdirs();
        	}
        	
        	
        		JsonArray ArmorSet = new JsonArray();
        		
        		JsonArray OffHandSlots = new JsonArray();
        		
        		JsonArray arrowTipArray = new JsonArray();
        		
        		JsonArray MainHand = new JsonArray();
        		
        		JsonObject object = new JsonObject();
        		
       			// Leather
        		object.addProperty("name", "Leather Armor Set");
        		object.add("head", new ItemHolder(Items.LEATHER_HELMET).writeJsonObject(new JsonObject()));
        		object.add("body", new ItemHolder(Items.LEATHER_CHESTPLATE).writeJsonObject(new JsonObject()));
        		object.add("legs", new ItemHolder(Items.LEATHER_LEGGINGS).writeJsonObject(new JsonObject()));
        		object.add("feet", new ItemHolder(Items.LEATHER_BOOTS).writeJsonObject(new JsonObject()));
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 20);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
       			// iron
        		object.addProperty("name", "Iron Armor Set");
        		object.add("head", new ItemHolder(Items.IRON_HELMET).writeJsonObject(new JsonObject()));
        		object.add("body", new ItemHolder(Items.IRON_CHESTPLATE).writeJsonObject(new JsonObject()));
        		object.add("legs", new ItemHolder(Items.IRON_LEGGINGS).writeJsonObject(new JsonObject()));
        		object.add("feet", new ItemHolder(Items.IRON_BOOTS).writeJsonObject(new JsonObject()));
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 20);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
        		
       			// gold
        		object.addProperty("name", "Gold Armor Set");
        		object.add("head", new ItemHolder(Items.GOLDEN_HELMET).writeJsonObject(new JsonObject()));
        		object.add("body", new ItemHolder(Items.GOLDEN_CHESTPLATE).writeJsonObject(new JsonObject()));
        		object.add("legs", new ItemHolder(Items.GOLDEN_LEGGINGS).writeJsonObject(new JsonObject()));
        		object.add("feet", new ItemHolder(Items.GOLDEN_BOOTS).writeJsonObject(new JsonObject()));
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
       			// Diamond
        		object.addProperty("name", "Diamond Armor Set");
        		object.add("head", new ItemHolder(Items.DIAMOND_HELMET).writeJsonObject(new JsonObject()));
        		object.add("body", new ItemHolder(Items.DIAMOND_CHESTPLATE).writeJsonObject(new JsonObject()));
        		object.add("legs", new ItemHolder(Items.DIAMOND_LEGGINGS).writeJsonObject(new JsonObject()));
        		object.add("feet", new ItemHolder(Items.DIAMOND_BOOTS).writeJsonObject(new JsonObject()));
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		//Chain mail
        		object.addProperty("name", "Chain Mail Armor Set");
        		object.add("head", new ItemHolder(Items.CHAINMAIL_HELMET).writeJsonObject(new JsonObject()));
        		object.add("body", new ItemHolder(Items.CHAINMAIL_CHESTPLATE).writeJsonObject(new JsonObject()));
        		object.add("legs", new ItemHolder(Items.CHAINMAIL_LEGGINGS).writeJsonObject(new JsonObject()));
        		object.add("feet", new ItemHolder(Items.CHAINMAIL_BOOTS).writeJsonObject(new JsonObject()));
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		
        		// MainHand
        		object = new ItemHolder(Items.WOODEN_SWORD).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 20);
        		
        		
        		MainHand.add(object);
        		
        		object = new ItemHolder(Items.IRON_SWORD).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 20);

        		MainHand.add(object);
        		
        		object = new ItemHolder(Items.GOLDEN_SWORD).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 10);
        		
        		MainHand.add(object);
        		
        		object = new ItemHolder(Items.DIAMOND_SWORD).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 10);
        		
        		MainHand.add(object);
        		
        		
        		// OFFHAND
        		object = new ItemHolder(Items.FEATHER).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 20);
        		
        		
        		OffHandSlots.add(object);
        		
        		object = new ItemHolder(Items.MAP).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 20);

        		OffHandSlots.add(object);
        		
        		ItemStack bigStick = new ItemStack(Items.STICK);
        		bigStick.setStackDisplayName("Big Stick");
        		object = new ItemHolder(bigStick).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);
        		
        		object = new ItemHolder(Items.SKULL).writeJsonObject(new JsonObject());
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);
        		
        		/// arrow tips
        		
        		
        		object = new JsonObject();

        		PotionItemHolder holder = new PotionItemHolder(Items.TIPPED_ARROW);
        		holder.addPotionEffect("minecraft:slowness", 60);

        		object.addProperty("CustomName", "Slowness");
        		object.addProperty("weight", 5);
        		holder.writeJsonPotions(object);
        		
        		arrowTipArray.add(object);
    		
        		//***
        		
        		object = new JsonObject();
        		
        		holder = new PotionItemHolder(Items.TIPPED_ARROW);
        		holder.addPotionEffect("minecraft:instant_damage", 20);

        		
        		object.addProperty("CustomName", "Harmful");
        		object.addProperty("weight", 10);
        		holder.writeJsonPotions(object);
        		
        		arrowTipArray.add(object);
        		
 
        		object = new JsonObject();
        		
        		holder = new PotionItemHolder(Items.TIPPED_ARROW);
        		holder.addPotionEffect("minecraft:poison", 30);
        		holder.addPotionEffect("minecraft:weakness", 60);

        		
        		object.addProperty("CustomName", "Poisonous");
        		object.addProperty("weight", 10);
        		holder.writeJsonPotions(object);
        		
        		arrowTipArray.add(object);
        		
        		//*******
        		
        		object = new JsonObject();
        		
        		holder = new PotionItemHolder(Items.TIPPED_ARROW);
        		holder.addPotionEffect("weakness", 40);
        		
        		object.addProperty("CustomName", "Weakness");
        		object.addProperty("weight", 25);
        		holder.writeJsonPotions(object);
        		
        		arrowTipArray.add(object);
        		
        		//********8
        		
        		writeJson(armorFile, ArmorSet);
        		writeJson(mainHandFile, MainHand);
        		writeJson(offHandFile, OffHandSlots);
        		writeJson(arrorwsFile, arrowTipArray);
          		
        		//new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);
        			
        return null;
	}
	
	
	private static File writeJson(File file, JsonArray json ) {
        try 
        {	
        	if (!file.exists())
        		file.createNewFile();

        	if (file.canWrite())
        	{

        		FileWriter fw = new FileWriter(file);
       		
        		new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);
       		
        		fw.flush();
        		fw.close();
       		
        		return file;
        	}
        }catch (IOException e) 
        {
        	e.printStackTrace();
        }
        
		return file;
	}
	
}
