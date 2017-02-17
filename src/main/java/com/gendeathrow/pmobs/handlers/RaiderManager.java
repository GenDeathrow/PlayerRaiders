package com.gendeathrow.pmobs.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import net.minecraft.util.WeightedRandom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.pmobs.core.ConfigHandler;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.util.Tools;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;

public class RaiderManager 
{
	public static final HashMap<String, RaiderData> raidersList = new HashMap<String, RaiderData>();
	//public static List<RaiderData> raiderWeighted = Lists.<RaiderData>newArrayList();
	
	public static final File raiderFile = new File(ConfigHandler.configDir, "raiders.json");
	
	public static final File whiteListFolder = new File("raidersWhitelist");
	
	public static final Random rand = new Random();
	
	private static boolean markDirty = false;
	
	static
	{
		raidersList.put("Gen_Deathrow", new RaiderData(new GameProfile(null, "Gen_Deathrow"), 10));
		raidersList.put("FunwayGuy", new RaiderData(new GameProfile(null, "FunwayGuy"), 10));
		raidersList.put("Darkosto", new RaiderData(new GameProfile(null, "Darkosto"), 10));
		raidersList.put("Kashdeya", new RaiderData(new GameProfile(null, "Kashdeya"), 10));
		raidersList.put("TheMattaBase", new RaiderData(new GameProfile(null, "TheMattaBase"), 10));
		raidersList.put("Jsl7", new RaiderData(new GameProfile(null, "Jsl7"), 10));
		raidersList.put("Turkey2349", new RaiderData(new GameProfile(null, "Turkey2349"), 10));

	}
	
	// ServerSide call
	public static RaiderData getRandomRaider()
	{
		return (RaiderData)WeightedRandom.getRandomItem(rand, getWeightedList());
	}
	
	private static List<RaiderData> getWeightedList()
	{
		return (List<RaiderData>) Lists.newArrayList(raidersList.values());
	}
	
	// Common call / clientside mainly
	public static GameProfile getRaiderProfile(String ownerName)
	{
		if(raidersList.containsKey(ownerName))
		{
			return raidersList.get(ownerName).getProfile();
		}
		else
		{
			raidersList.put(ownerName,  new RaiderData(new GameProfile(null, ownerName), 10));
			
			return raidersList.get(ownerName).getProfile();
		}
	}
	
	public static HashMap<String, RaiderData> getAllRaiders()
	{
		return raidersList;
	}
	
	public static void setRaiderProfile(String ownerName, GameProfile newProfile)
	{
		if(raidersList.containsKey(ownerName))
		{
			raidersList.get(ownerName).setProfile(newProfile);
		}
	}
	
	// Cant get rid of herobrine.. he will add him self back
	protected static void permanentRaiders()
	{
		int weight = (int) (WeightedRandom.getTotalWeight(getWeightedList()) * 0.01);
		
		if(!raidersList.containsKey("Herobrine"))
		{
			raidersList.put("Herobrine", new RaiderData(new GameProfile(null, "Herobrine"), weight < 1 ? 1 : weight));
		}
		if(raidersList.containsKey("Herobrine"))
		{
			raidersList.get("Herobrine").itemWeight = weight < 1 ? 1 : weight;
			markDirty = true;
		}
	}
	
	public static void addNewRaider(String ownerName, int weight)
	{
		
		if(!raidersList.containsKey(ownerName))
		{
			raidersList.put(ownerName,  new RaiderData(new GameProfile(null, ownerName), weight));
			//System.out.println(raidersList.size());
			markDirty = true;
		}
	}

	public static void removeRaider(String ownerName)
	{
		raidersList.remove(ownerName);
		markDirty = true;
	}

	public static void Save()
	{
		if(markDirty)
		{
			saveRaiderFile();
			markDirty = false;
		}
	}
	
	public static void readRaiderFile()
	{
		 getTwitchSubscribers();
		 
	        if (raiderFile.isFile())
	        {
	            try
	            {
	                raidersList.clear();

	                raidersList.putAll(parseJson(FileUtils.readFileToString(raiderFile)));

	                //raiderWeighted.addAll(raidersList.values());
	                
	                permanentRaiders();
	                
	                getTwitchSubscribers();
	            }
	            catch (IOException ioexception)
	            {
	                RaidersCore.logger.error((String)("Couldn\'t read Raiders file " + raiderFile), (Throwable)ioexception);
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	            	RaidersCore.logger.error((String)("Couldn\'t parse Raiders file " + raiderFile), (Throwable)jsonparseexception);
	            }
	        }
	        else saveRaiderFile();
	}

	public static void saveRaiderFile()
	{
	    	FileOutputStream fo = null; 
	        try
	        {
	        	fo = FileUtils.openOutputStream(raiderFile);
	        			
	            String json = new GsonBuilder().setPrettyPrinting().create().toJson(dumpJson(raidersList));
	        	   
	            FileUtils.writeStringToFile(raiderFile, json);
	            
	            fo.close(); 
	        }
	        catch (IOException ioexception)
	        {
	            RaidersCore.logger.error((String)"Couldn\'t save stats", (Throwable)ioexception);
	        }finally 
	        {
	        	IOUtils.closeQuietly(fo);
	        }
	}

	    
	    
	public static Map<String, RaiderData> parseJson(String p_150881_1_)
	{
	        JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);

	        if (!jsonelement.isJsonObject())
	        {
	            return Maps.<String, RaiderData>newHashMap();
	        }
	        else
	        {
	            JsonObject jsonobject = jsonelement.getAsJsonObject();
	            Map<String, RaiderData> map = Maps.<String, RaiderData>newHashMap();

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

	                	//System.out.println(playerOwner);
	                	GameProfile playerProfile = new GameProfile((UUID)null,playerOwner);

	                	RaiderData raiderData = new RaiderData(playerProfile, weight);
	                	
//	                	if(RaidersCore.proxy.isClient()) 
//	                	{
//	                		RaidersCore.logger.info("updateing profile: "+ playerOwner);
	                			//RaidersSkinManager.updateProfile(raiderData);
//	                	}
	                	if(!map.containsKey(playerOwner))
	                	{
	                		map.put(playerOwner, raiderData);
	                		
	                	}
	                	else
	                	{
	                		RaidersCore.logger.warn("Raider already exist in " + raiderFile + ":" + (String)entry.getKey());
	                	}
	                	
	                }
	                else
	                {
	                	RaidersCore.logger.warn("Invalid Raider in " + raiderFile + ": Don\'t know what " + (String)entry.getKey() + " is");
	                }
	            }

	            return map;
	        }
	}
	    
	    
	public static JsonObject dumpJson(Map<String, RaiderData> p_150880_0_)
	{
	        JsonObject jsonobject = new JsonObject();

	        for (Entry<String, RaiderData> entry : p_150880_0_.entrySet())
	        {
	            if (((RaiderData)entry.getValue()) != null)
	            {
	                JsonObject jsonobject1 = new JsonObject();
	 
	                jsonobject1.addProperty("weight", (Number)Integer.valueOf(entry.getValue().itemWeight));

	                jsonobject.add(((String)entry.getKey()), jsonobject1);
	            }
	            else
	            {
	                jsonobject.addProperty(((String)entry.getKey()), (Number)Integer.valueOf(10));
	            }
	        }

	        
	        return jsonobject;
	}
	
	public static void getTwitchSubscribers()
	{
		getTwitchSubscribers(false);
	}
	
	public static void getTwitchSubscribers(boolean force)
	{
		
		whiteListFolder.mkdirs();

		for(String list : PMSettings.whitelists)
		{
		
			// First check to see if you have updated Twitch Subs yet
			File subs = new File(whiteListFolder, list.replaceAll("\\W+", "") +".txt");

			if(!subs.exists() || force)
			{
				try 
				{
					Tools.DownloadFile(list, subs.getPath());
					
					if(subs.exists())
					{
						parseTwitchSubsWhiteList(subs);
					}
					
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		
//			if(subs.exists())
//			{
//				try 
//				{
//					parseTwitchSubsWhiteList(subs);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		
	}
	
	private static void parseTwitchSubsWhiteList(File file) throws IOException
	{
		FileReader input = new FileReader(file);
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		try {
			while ( (myLine = bufRead.readLine()) != null)
			{    
			    String[] array2 = myLine.split("\n");
			    for (int i = 0; i < array2.length; i++)
			    	addNewRaider(array2[i],10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
