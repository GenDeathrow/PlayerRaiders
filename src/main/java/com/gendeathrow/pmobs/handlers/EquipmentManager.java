package com.gendeathrow.pmobs.handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.WeightedRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.pmobs.core.ConfigHandler;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class EquipmentManager 
{

	public static ArrayList<EquipmentWeigthedItem> armorList = new ArrayList<EquipmentWeigthedItem>();
	
	public static ArrayList<EquipmentWeigthedItem> weaponList = new ArrayList<EquipmentWeigthedItem>();
	
	public static final File equipmentFile = new File(ConfigHandler.configDir, "equipment.json");
	
	public static final Random rand = new Random();
	
	private static boolean markDirty = false;
	
	static
	{

	}
	
	// ServerSide call
	public static EquipmentWeigthedItem getRandomArmor()
	{
		return (EquipmentWeigthedItem)WeightedRandom.getRandomItem(rand, armorList);
	}

	public static EquipmentWeigthedItem getRandomWeapons()
	{
		return (EquipmentWeigthedItem)WeightedRandom.getRandomItem(rand, weaponList);
	}
	
	private static List<EquipmentWeigthedItem> getWeightedList()
	{
		return (List<EquipmentWeigthedItem>) Lists.newArrayList(armorList);
	}
	
	public static void Save()
	{
		if(markDirty)
		{
			saveEquipmentFile();
			markDirty = false;
		}
	}
	
	public static void readEquipmentFile()
	{
	        if (equipmentFile.isFile())
	        {
	            try
	            {
	            	//raiderWeighted.clear();
	            	armorList.clear();

	            	armorList.addAll(null); 
	            	
	            	parseJson(FileUtils.readFileToString(equipmentFile));

	                //raiderWeighted.addAll(raidersList.values());
	                
	                //permanentRaiders();
	            }
	            catch (IOException ioexception)
	            {
	                RaidersCore.logger.error((String)("Couldn\'t read Equipment file " + equipmentFile), (Throwable)ioexception);
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	            	RaidersCore.logger.error((String)("Couldn\'t parse Equipment file " + equipmentFile), (Throwable)jsonparseexception);
	            }
	        }
	        else saveEquipmentFile();
	}

	public static void saveEquipmentFile()
	{
	    	FileOutputStream fo = null; 
	        try
	        {
	        	fo = FileUtils.openOutputStream(equipmentFile);
	        			
	            String json = new GsonBuilder().setPrettyPrinting().create().toJson(dumpJson(armorList));
	        	   
	            FileUtils.writeStringToFile(equipmentFile, json);
	            
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

	    
	    
	public static Map<String, EquipmentWeigthedItem> parseJson(String p_150881_1_)
	{
	        JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);

	        if (!jsonelement.isJsonObject())
	        {
	            return Maps.<String, EquipmentWeigthedItem>newHashMap();
	        }
	        else
	        {
	            JsonObject jsonobject = jsonelement.getAsJsonObject();
	            Map<String, EquipmentWeigthedItem> map = Maps.<String, EquipmentWeigthedItem>newHashMap();

	            for (Entry<String, JsonElement> entry : jsonobject.entrySet())
	            {
	                String playerOwner = (String)entry.getKey();

	                if (playerOwner != null)
	                {
	                	JsonObject playerData = entry.getValue().getAsJsonObject();
	                	
	                	int weight = 10;
	                	
	                	if(playerData.has("weight"))
	                	{
	                		weight = playerData.get("weight").getAsInt();
	                	}

	                	EquipmentWeigthedItem raiderData = new EquipmentWeigthedItem(weight);
	                	
	                	if(!map.containsKey(playerOwner))
	                	{
	                		map.put(playerOwner, raiderData);
	                		
	                	}
	                	else
	                	{
	                		RaidersCore.logger.warn("Equipment already exist in " + equipmentFile + ":" + (String)entry.getKey());
	                	}
	                	
	                }
	                else
	                {
	                	RaidersCore.logger.warn("Invalid Equipment in " + equipmentFile + ": Don\'t know what " + (String)entry.getKey() + " is");
	                }
	            }

	            return map;
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
	
	
	
	
}
