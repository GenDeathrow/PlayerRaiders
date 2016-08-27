package com.gendeathrow.pmobs.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.WeightedRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.pmobs.core.ConfigHandler;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.gendeathrow.pmobs.handlers.random.EquipmentWeigthedItem;
import com.gendeathrow.pmobs.util.JsonHelper;
import com.gendeathrow.pmobs.util.NBTConverter;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class EquipmentManager 
{

	public static ArrayList<ArmorSetWeigthedItem> armorList = new ArrayList<ArmorSetWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> mainHandList = new ArrayList<EquipmentWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> offHandList = new ArrayList<EquipmentWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> tippedArrows = new ArrayList<EquipmentWeigthedItem>();
	
	public static final File equipmentFile = new File(ConfigHandler.configDir, "equipment.json");
	
	public static final Random rand = new Random();
	
	private static boolean markDirty = false;
	
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
			
	        if (equipmentFile.isFile())
	        {
	            try
	            {
	            	LoadEquipment(JsonHelper.ReadJsonFile(equipmentFile));
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	            	RaidersCore.logger.error((String)("Couldn\'t parse Equipment file " + equipmentFile), (Throwable)jsonparseexception);
	            }
	        }
	        //else saveEquipmentFile();
	}

	private static void LoadEquipment(JsonObject jsonObject) 
	{
		armorList.clear();
		offHandList.clear();
		mainHandList.clear();
		

		if(jsonObject.has("Armor_Sets"))
		{
			for(JsonElement element : jsonObject.get("Armor_Sets").getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				ArmorSetWeigthedItem weightedArmor = null;
				String name; 
				String head = null; JsonObject headNBT = null;
				String body = null; JsonObject bodyNBT = null;
				String legs = null; JsonObject legsNBT = null;
				String feet = null; JsonObject feetNBT = null;
				
				boolean fullSet = false;
				int weight = 10;
				
				if(data.has("name")) name = data.get("name").getAsString(); 
				if(data.has("head")) head = data.get("head").getAsString();
				if(data.has("body")) body = data.get("body").getAsString();
				if(data.has("legs")) legs = data.get("legs").getAsString();
				if(data.has("feet")) feet = data.get("feet").getAsString();
				
				if(data.has("head_nbt") && data.get("head_nbt").isJsonObject()) headNBT = data.get("head_nbt").getAsJsonObject();
				if(data.has("body_nbt") && data.get("body_nbt").isJsonObject()) bodyNBT = data.get("body_nbt").getAsJsonObject(); 
				if(data.has("legs_nbt") && data.get("legs_nbt").isJsonObject()) legsNBT = data.get("legs_nbt").getAsJsonObject();
				if(data.has("feet_nbt") && data.get("feet_nbt").isJsonObject()) feetNBT = data.get("feet_nbt").getAsJsonObject();
				
				if(data.has("always spawn full set")) fullSet = data.get("always spawn full set").getAsBoolean();
				if(data.has("weight"))	weight = data.get("weight").getAsInt();

				ItemStack headStack = null;
				ItemStack bodyStack = null; 
				ItemStack legsStack = null;
				ItemStack feetStack = null;

				if(head != null) 
				{
					headStack = getItemStackFromID(head);
					setNBTData(headNBT, headStack);
				}
				if(body != null)
				{
					bodyStack = getItemStackFromID(body);
					setNBTData(bodyNBT, bodyStack);
				}
				if(legs != null) 
				{
					legsStack = getItemStackFromID(legs);
					setNBTData(legsNBT, legsStack);
				}
				if(feet != null) 
				{
					feetStack = getItemStackFromID(feet);
					setNBTData(feetNBT, feetStack);
				}
				weightedArmor = new ArmorSetWeigthedItem(headStack, bodyStack, legsStack, feetStack, weight, fullSet);
				
				if(weightedArmor != null)
				{
					armorList.add(weightedArmor);
				}else System.out.println("Null Weigted item");
				
			}
		}
		
		if(jsonObject.has("MainHand/Weapon"))
		{
			for( JsonElement element : jsonObject.get("MainHand/Weapon").getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				
				EquipmentWeigthedItem weightedItem = null;
				String itemID = null; JsonObject itemNBT = null;
				int weight = 10;
				
				if(data.has("itemID"))	itemID = data.get("itemID").getAsString();
				if(data.has("nbt")  && data.get("nbt").isJsonObject()) itemNBT = data.get("nbt").getAsJsonObject();
				if(data.has("weight"))	weight = data.get("weight").getAsInt();
				
				ItemStack stack = getItemStackFromID(itemID);
				
				setNBTData(itemNBT, stack);
				
				if(stack != null)
				{
					weightedItem = new EquipmentWeigthedItem(stack, weight);
				}
				
				if(weightedItem != null)
				{
					mainHandList.add(weightedItem);
				}else System.out.println("Null Weigted item");
		
			}
		}
		
		if(jsonObject.has("OffHand"))
		{
			for(JsonElement element : jsonObject.get("OffHand").getAsJsonArray())
			{
				JsonObject data = element.getAsJsonObject();
				
				EquipmentWeigthedItem weightedItem = null;
				
				String itemID = null; JsonObject itemNBT = null;
				int weight = 10;
				
				if(data.has("itemID"))	itemID = data.get("itemID").getAsString();
				if(data.has("nbt") && data.get("nbt").isJsonObject()) itemNBT = data.get("nbt").getAsJsonObject();
				if(data.has("weight"))	weight = data.get("weight").getAsInt();
				
				ItemStack stack = getItemStackFromID(itemID);
				
				setNBTData(itemNBT, stack);
				
				if(stack != null)
				{
					weightedItem = new EquipmentWeigthedItem(stack, weight);
				}
				
				if(weightedItem != null)
				{
					offHandList.add(weightedItem);
				}else System.out.println("Null Weigted item");
			}
		}
		
		if(jsonObject.has("Tipped_Arrows_Effects"))
		{
			for(JsonElement arrowSet : jsonObject.get("Tipped_Arrows_Effects").getAsJsonArray())
			{
				
				ItemStack arrowTip = new ItemStack(Items.TIPPED_ARROW);

				EquipmentWeigthedItem weightedItem = null;
				
				String itemID = null; JsonObject itemNBT = null;
				int weight = 10; int ticks = 60;
				String customName = null;
				
				JsonObject arrowElement = arrowSet.getAsJsonObject();
				
				if(arrowElement.has("nbt") && arrowElement.get("nbt").isJsonObject()) itemNBT = arrowElement.get("nbt").getAsJsonObject();
				if(arrowElement.has("weight"))	weight = arrowElement.get("weight").getAsInt();
				if(arrowElement.has("ticks"))	ticks = arrowElement.get("ticks").getAsInt();
				
				arrowTip.setStackDisplayName(arrowSet +" "+ arrowTip.getDisplayName()); 
				
				setNBTData(itemNBT, arrowTip);
				
				if(arrowElement.has("TippedArrowsPotions"))
				{
					List<PotionEffect> collection = (List) new ArrayList();


				
					if(arrowElement.get("TippedArrowsPotions").isJsonArray())
					{
						JsonArray TippedArrows = arrowElement.get("TippedArrowsPotions").getAsJsonArray();
						
						for(JsonElement potionID : TippedArrows)
						{
						
							if(potionID.getAsJsonObject().has("potionID"))
							{
								System.out.println(potionID.getAsJsonObject().get("potionID").getAsString());
									Potion potion = Potion.getPotionFromResourceLocation((potionID.getAsJsonObject().get("potionID").getAsString()));
									if(potion != null) collection.add(new PotionEffect(potion, ticks));
							}
	
						}
					
					}
					else if(arrowElement.get("TippedArrowsPotions").isJsonObject())
					{
						JsonObject potionID = arrowElement.get("TippedArrowsPotions").getAsJsonObject();
						if(potionID.has("potionID"))
						{
							System.out.println(potionID.get("potionID").getAsString());
							Potion potion = Potion.getPotionFromResourceLocation(potionID.get("potionID").getAsString());
							if(potion != null) collection.add(new PotionEffect(potion, ticks));
						}
					}
					
					
					if(arrowTip != null && !collection.isEmpty())
					{	
						PotionUtils.appendEffects(arrowTip, collection);
						weightedItem = new EquipmentWeigthedItem(arrowTip, weight);
					}
				
					if(weightedItem != null)
					{
						tippedArrows.add(weightedItem);
					
					}else System.out.println("Null Weigted item");
					

					
				}
				
			}
		}
		

		System.out.println("MainHandList Size: "+ mainHandList.size());
		System.out.println("OffHand Size: "+ offHandList.size());
		System.out.println("Armor Sets Size: "+ armorList.size());
		System.out.println("Tipped Arrows Size: "+ tippedArrows.size());
	}
	
	private static ItemStack getItemStackFromID(String itemID)
	{
		
		String[] args = itemID.split(":");
		

		ItemStack stack;
		
//		System.out.println("modid: "+ args[0] +"| itemid: "+ args[1]);
		
		if(args.length == 3) 
		{
			try
			{
				int meta = Integer.parseInt(args[2]);
				stack = new ItemStack(Item.getByNameOrId(args[0] +":"+ args[1]), 1, meta);		
				System.out.println("meta:"+ meta);
			}catch(Exception e)
			{
				stack = new ItemStack(Item.getByNameOrId(args[0] +":"+ args[1]));
				System.out.println(e);
			}
		}
		else  stack = new ItemStack(Item.getByNameOrId(args[0] +":"+ args[1]));
		
		System.out.println(stack.getDisplayName());
		return stack;
	}
	
	private static ItemStack setNBTData(JsonObject nbt, ItemStack stack)
	{
		if(nbt != null && stack != null)
		{
			try
			{
				NBTTagCompound tags = new NBTTagCompound();
				NBTConverter.JSONtoNBT_Object(nbt, tags);
			
				if(!tags.hasNoTags()) 
				{
//					if(stack.hasTagCompound()) 
//					{
//						stack.getTagCompound().merge(tags);
//					}
//					else 
					stack.setTagCompound(tags);
				}
		
			}catch(Exception e)
			{
				System.out.println(stack.getDisplayName() + e);
				return stack;
			}
		}
		
		return stack;
	}

	public static void saveEquipmentFile()
	{
	    	FileOutputStream fo = null; 
	        try
	        {
	        	fo = FileUtils.openOutputStream(equipmentFile);
	        			
	            //String json = new GsonBuilder().setPrettyPrinting().create().toJson(dumpJson(armorList));
	        	   
	            //FileUtils.writeStringToFile(equipmentFile, json);
	            
	            fo.close(); // don't swallow close Exception if copy completes normally
	        }
	        catch (IOException ioexception)
	        {
	            RaidersCore.logger.error((String)"Couldn\'t save stats", (Throwable)ioexception);
	        }finally 
	        {
	        	IOUtils.closeQuietly(fo);
	        }
	}

	    
	public static JsonObject dumpJson(ArrayList<EquipmentWeigthedItem> list)
	{
	        JsonObject jsonobject = new JsonObject();

			for (EquipmentWeigthedItem entry : list)
	        {
	            if (((EquipmentWeigthedItem)entry) != null)
	            {
	                JsonObject jsonobject1 = new JsonObject();
	 
//	                jsonobject1.addProperty("weight", (Number)Integer.valueOf(entry.getValue().itemWeight));
//
//	                jsonobject.add(((String)entry), jsonobject1);
	            }
	            else
	            {
	                //jsonobject.addProperty(((String)entry.getKey()), (Number)Integer.valueOf(10));
	            }
	        }

	        
	        return jsonobject;
	}
	
	public static File GenerateDefault()
	{
	
        try 
        {	
        	File file = equipmentFile;
        	
        	if (file.exists())
        	{
        		return file;
        	}else file.createNewFile();
			
        	if (file.canWrite())
        	{
        		FileWriter fw = new FileWriter(file);
        			
        		JsonObject json = new JsonObject();
        		
        		JsonArray ArmorSet = new JsonArray();
        		
        		JsonArray armorSetsArray = new JsonArray();
        		
        		JsonArray OffHandSlots = new JsonArray();
        		
        		JsonArray arrowTipArray = new JsonArray();
        		
        		JsonArray MainHand = new JsonArray();
        		
        		JsonObject object = new JsonObject();
        		
       			// Leather
        		object.addProperty("name", "Leather Armor Set");
        		object.addProperty("head", "minecraft:leather_helmet");
        		object.addProperty("body", "minecraft:leather_chestplate");
        		object.addProperty("legs", Item.REGISTRY.getNameForObject(Items.LEATHER_LEGGINGS).toString());
        		object.addProperty("feet", "minecraft:leather_boots");
        		object.add("head_nbt", new JsonObject());
        		object.add("body_nbt", new JsonObject());
        		object.add("legs_nbt", new JsonObject());
        		object.add("feet_nbt", new JsonObject());
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 20);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
        		JsonElement blank = new JsonArray();

       			// iron
        		object.addProperty("name", "Iron Armor Set");
        		object.addProperty("head", "minecraft:iron_helmet");
        		object.addProperty("body", "minecraft:iron_chestplate");
        		object.addProperty("legs", Item.REGISTRY.getNameForObject(Items.IRON_LEGGINGS).toString());
        		object.addProperty("feet", "minecraft:iron_boots");
        		object.add("head_nbt", new JsonObject());
        		object.add("body_nbt", new JsonObject());
        		object.add("legs_nbt", new JsonObject());
        		object.add("feet_nbt", new JsonObject());
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 20);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
        		
       			// gold
        		object.addProperty("name", "Gold Armor Set");
        		object.addProperty("head", "minecraft:golden_helmet");
        		object.addProperty("body", "minecraft:golden_chestplate");
        		object.addProperty("legs", Item.REGISTRY.getNameForObject(Items.GOLDEN_LEGGINGS).toString());
        		object.addProperty("feet", "minecraft:golden_boots");
        		object.add("head_nbt", new JsonObject());
        		object.add("body_nbt", new JsonObject());
        		object.add("legs_nbt", new JsonObject());
        		object.add("feet_nbt", new JsonObject());
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		
       			// Diamond
        		object.addProperty("name", "Diamond Armor Set");
        		object.addProperty("head", "minecraft:diamond_helmet");
        		object.addProperty("body", "minecraft:diamond_chestplate");
        		object.addProperty("legs", Item.REGISTRY.getNameForObject(Items.DIAMOND_LEGGINGS).toString());
        		object.addProperty("feet", "minecraft:diamond_boots");
        		object.add("head_nbt", new JsonObject());
        		object.add("body_nbt", new JsonObject());
        		object.add("legs_nbt", new JsonObject());
        		object.add("feet_nbt", new JsonObject());
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		object = new JsonObject();
        		//Chain mail
        		object.addProperty("name", "Chain Mail Armor Set");
        		object.addProperty("head", "minecraft:chainmail_helmet");
        		object.addProperty("body", "minecraft:chainmail_chestplate");
        		object.addProperty("legs", Item.REGISTRY.getNameForObject(Items.CHAINMAIL_LEGGINGS).toString());
        		object.addProperty("feet", "minecraft:chainmail_boots");
        		object.add("head_nbt", new JsonObject());
        		object.add("body_nbt", new JsonObject());
        		object.add("legs_nbt", new JsonObject());
        		object.add("feet_nbt", new JsonObject());
        		object.addProperty("always spawn full set", false);
        		object.addProperty("weight", 10);
        		
        		ArmorSet.add(object);
        		
        		
        		// MainHand
        		object = new JsonObject();
        		
        		object.addProperty("itemID", "minecraft:wooden_sword");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 20);
        		
        		
        		MainHand.add(object);
        		
        		object = new JsonObject();

        		object.addProperty("itemID", "minecraft:iron_sword");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 20);

        		MainHand.add(object);
        		
        		object = new JsonObject();
        		
        		object.addProperty("itemID", "minecraft:golden_sword");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		
        		MainHand.add(object);
        		
        		object = new JsonObject();

        		object.addProperty("itemID", "minecraft:diamond_sword");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		
        		MainHand.add(object);
        		
        		
        		// OFFHAND
        		object = new JsonObject();
        		
        		object.addProperty("itemID", "minecraft:feather");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 20);
        		
        		
        		OffHandSlots.add(object);
        		
        		object = new JsonObject();

        		object.addProperty("itemID", "minecraft:map");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 20);

        		OffHandSlots.add(object);
        		
        		object = new JsonObject();
        		
        		object.addProperty("itemID", "minecraft:stick");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);
        		
        		object = new JsonObject();

        		object.addProperty("itemID", "minecraft:skull:1");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);
        		
        		object = new JsonObject();

        		JsonObject head = new JsonObject(); head.addProperty("SkullOwner", "Gen_Deathrow");
        				
        		object.addProperty("itemID", "minecraft:skull:3");
        		object.add("nbt", head);
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);
        		
        		object = new JsonObject();
        		
        		head = new JsonObject(); head.addProperty("SkullOwner", "Darkosto");
        		
        		object.addProperty("itemID", "minecraft:skull:3");
        		object.add("nbt", head);
        		object.addProperty("weight", 10);
        		
        		OffHandSlots.add(object);

        		/// arrow tips
        		
        		object = new JsonObject();
        		
        		JsonArray potionsArray = new JsonArray();
        		JsonObject potion = new JsonObject(); potion.addProperty("potionID","slowness");
        		
        		object.addProperty("CustomName", "Slowness");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 5);
        		object.addProperty("ticks", 60);
        		object.add("TippedArrowsPotions", potion);
        		
        		arrowTipArray.add(object);
    		
        		//***
        		
        		object = new JsonObject();
        		potion = new JsonObject(); potion.addProperty("potionID","instant_damage");
        		object.addProperty("CustomName", "Harmful");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		object.addProperty("ticks", 20);
        		object.add("TippedArrowsPotions", potion);
        		
        		arrowTipArray.add(object);
        		
        		//*********
        		object = new JsonObject();
        		
        		potion = new JsonObject(); potion.addProperty("potionID","nausea");
        		potionsArray.add(potion);
        		
        		object.addProperty("CustomName", "Harmful");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 5);
        		object.addProperty("ticks", 30);
        		object.add("TippedArrowsPotions", potion);
        		
        		arrowTipArray.add(object);
        		
        		//********
        		object = new JsonObject();
 
        		potionsArray = new JsonArray();
        		
        		potion = new JsonObject(); potion.addProperty("potionID","poison");
        		JsonObject potion2 = new JsonObject(); potion2.addProperty("potionID","weakness");
        		potionsArray.add(potion); potionsArray.add(potion2);

        		object.addProperty("CustomName", "Poisonous");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 10);
        		object.addProperty("ticks", 30);
        		object.add("TippedArrowsPotions", potionsArray);
        		
        		arrowTipArray.add(object);
        		
        		//*******
        		
        		object = new JsonObject();
        		
        		potion = new JsonObject(); potion.addProperty("potionID","weakness");
        		
        		object.addProperty("CustomName", "Weakness");
        		object.add("nbt", new JsonObject());
        		object.addProperty("weight", 25);
        		object.addProperty("ticks", 30);
        		object.add("TippedArrowsPotions", potion);
        		
        		arrowTipArray.add(object);
        		
        		//********8
        		
        		json.add("Armor_Sets", ArmorSet);
        		json.add("MainHand/Weapon", MainHand);
        		json.add("OffHand", OffHandSlots);
        		json.add("Tipped_Arrows_Effects", arrowTipArray);
          		
        		new GsonBuilder().setPrettyPrinting().create().toJson(json, fw);
        			
        		fw.flush();
        		fw.close();
        		
                return file;
        	}
			}catch (IOException e) 
        	{
				e.printStackTrace();
        	}
        
        return null;
        
        // Armor Sets
		
		// Offhand
		
		// Weapons
	}
	
	
	
	
}
