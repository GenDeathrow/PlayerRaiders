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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.gendeathrow.pmobs.client.gui.RaidNotification;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.core.configs.MainConfig;
import com.gendeathrow.pmobs.util.Tools;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.GameProfile;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;

public class RaiderManager 
{
	public static final HashMap<String, RaiderData> raidersListActive = new HashMap<String, RaiderData>();
	
	
	public static final HashMap<String, RaiderData> raidersListInActive = new HashMap<String, RaiderData>();
	//public static List<RaiderData> raiderWeighted = Lists.<RaiderData>newArrayList();
	
	public static final File raiderFile = new File(MainConfig.configDir, "raiders_skins.json");
	
	public static final File whiteListFolder = new File("raidersWhitelist");
	
	public static final Random rand = new Random();
	
	public static RaiderData DEFAULTSKIN = new RaiderData(new GameProfile(null, "Steve"), 10);
	
	private static boolean markDirty = false;
	
	// ServerSide call
	public static RaiderData getRandomRaider()
	{
		if(getWeightedList().size() <= 0)
			return DEFAULTSKIN;
		return (RaiderData)WeightedRandom.getRandomItem(rand, getWeightedList());
	}
	
	private static List<RaiderData> getWeightedList()
	{
		return (List<RaiderData>) Lists.newArrayList(raidersListActive.values());
	}
	
	// Common call / clientside mainly
	public static GameProfile getRaiderProfile(String ownerName)
	{
		if(raidersListActive.containsKey(ownerName))
		{
			return raidersListActive.get(ownerName).getProfile();
		}
		else
		{
			raidersListActive.put(ownerName,  new RaiderData(new GameProfile(null, ownerName), 10));
			
			return raidersListActive.get(ownerName).getProfile();
		}
	}
	

	public static RaiderData getRaiderProfile(GameProfile playerProfile) {
			return raidersListActive.get(playerProfile.getName());
	}
	
	public static HashMap<String, RaiderData> getAllRaiders()
	{
		return raidersListActive;
	}
	
	public static void setRaiderProfile(String ownerName, GameProfile newProfile)
	{
		if(raidersListActive.containsKey(ownerName))
		{
			raidersListActive.get(ownerName).setProfile(newProfile);
		}
	}
	
	//TODO
	// Cant get rid of herobrine.. he will add him self back
	protected static void permanentRaiders()
	{
		int weight = (int) (WeightedRandom.getTotalWeight(getWeightedList()) * 0.01);
		
		if(!raidersListActive.containsKey("Herobrine"))
		{
			raidersListActive.put("Herobrine", new RaiderData(new GameProfile(null, "Herobrine"), weight < 1 ? 1 : weight));
		}
		if(raidersListActive.containsKey("Herobrine"))
		{
			raidersListActive.get("Herobrine").itemWeight = weight < 1 ? 1 : weight;
			markDirty = true;
		}
	}
	
	public static void addNewRaider(String ownerName, int weight)
	{
		
		if(!raidersListActive.containsKey(ownerName))
		{
			raidersListActive.put(ownerName,  new RaiderData(new GameProfile(null, ownerName), weight));
			markDirty = true;
		}
	}

	public static void removeRaider(String ownerName)
	{
		raidersListActive.remove(ownerName);
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
	                raidersListActive.clear();

	                raidersListActive.putAll(parseJson(FileUtils.readFileToString(raiderFile)));
                
	                //permanentRaiders();
	                
	                getTwitchSubscribers();
	            }
	            catch (IOException ioexception)
	            {
	            	RaidersMain.logger.error((String)("Couldn\'t read Raiders file " + raiderFile), (Throwable)ioexception);
	            }
	            catch (JsonParseException jsonparseexception)
	            {
	            	RaidersMain.logger.error((String)("Couldn\'t parse Raiders file " + raiderFile), (Throwable)jsonparseexception);
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
	        			
	            String json = new GsonBuilder().setPrettyPrinting().create().toJson(dumpJson(raidersListActive));
	        	   
	            FileUtils.writeStringToFile(raiderFile, json);
	            
	            fo.close(); 
	        }
	        catch (IOException ioexception)
	        {
	        	RaidersMain.logger.error((String)"Couldn\'t save stats", (Throwable)ioexception);
	        }finally 
	        {
	        	IOUtils.closeQuietly(fo);
	        }
	}
	
	public static Map<String, RaiderData> parseJson(String p_150881_1_)
	{
		
	        JsonElement jsonelement = null;

	        try {
	        	jsonelement = (new JsonParser()).parse(p_150881_1_);
	        	
	        }catch (JsonSyntaxException e) {
	        	ArrayList<String> lines = new ArrayList<String>();
	        	lines.add("ERROR In Raiders Skin File");
	        	lines.add("Please check your latest Logs");
	        	lines.add("And check your raiders_skins.json for parsing errors");
				RaidersMain.logger.error(e.getLocalizedMessage());
	        	RaidNotification.ScheduleErrorNotice(lines);
	        }
	        
	        if (jsonelement == null || !jsonelement.isJsonObject())
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
	                
	            	if(playerOwner.equalsIgnoreCase("_exampleRaider")) 
	            		continue;


	                if (playerOwner != null)
	                {
	                	JsonObject playerData = entry.getValue().getAsJsonObject();
	                	
	                	int weight = 10;
	                	UUID uuid = null;
	                	boolean isEnabled = true;
	                	
	                	
	                	if(playerData.has("weight"))
	                		weight = playerData.get("weight").getAsInt();
	                	
	                	//TODO not working yet
	                	if(playerData.has("enabled"))
	                		isEnabled = playerData.get("enabled").getAsBoolean();
	                				
	                	GameProfile playerProfile = new GameProfile(uuid, playerOwner);
	                	
	                	RaiderData raiderData = new RaiderData(playerProfile, weight, isEnabled);
	                	
	                	if(playerData.has("customSkin")) {
	                		raiderData.setCustomSkin(new ResourceLocation(playerData.get("customSkin").getAsString()));
	                	}
	                	
	                	if(playerData.has("_comment"))
	                		raiderData.setComment(playerData.get("_comment").getAsString());
	                	
	                	if(!map.containsKey(playerOwner))
	                		if(isEnabled)
	                			map.put(playerOwner, raiderData);
	                		else 
	                			RaidersMain.logger.warn("Raider is current disabled in " + raiderFile + ":" + (String)entry.getKey());
	                	else
	                		RaidersMain.logger.warn("Raider already exist in " + raiderFile + ":" + (String)entry.getKey());
	                }
	                else
	                {
	                	RaidersMain.logger.warn("Invalid Raider in " + raiderFile + ": Don\'t know what " + (String)entry.getKey() + " is");
	                }
	            }

	            return map;
	        }
	}
	    
	    
	public static JsonObject dumpJson(Map<String, RaiderData> p_150880_0_)
	{
	        JsonObject jsonobject = new JsonObject();
	        
	        // Example
	        JsonObject jsonobject1 = new JsonObject();
	        
	        jsonobject1.addProperty("weight", "True Weight System, So you can tweak which skins you want to see more.");
	                
	        jsonobject1.addProperty("enabled", "After you get your Twitch list, you may have broken skins because ppl have changed names. So you can disable them here.");

	        jsonobject1.addProperty("customSkin", "Give resourcelocation of custom skin. Use raiders build in resource loading, by adding an image into assets/skins/filename.png Than set custom skin to raiders:skins/filename. You may also use any other resourceloader.");
	         
	        jsonobject1.addProperty("_comment", " Comment section is for you leave a comment if you disabled something to know why. or just any comment you want.");
	                
	        jsonobject.add("_exampleRaider", jsonobject1);
	        
	        // Raider Data
	        for (Entry<String, RaiderData> entry : p_150880_0_.entrySet())
	        {
	            if (((RaiderData)entry.getValue()) != null)
	            {
	                jsonobject1 = new JsonObject();
	                
	                jsonobject1.addProperty("weight", (Number)Integer.valueOf(entry.getValue().itemWeight));
	                
	                jsonobject1.addProperty("enabled", entry.getValue().isEnabled());
	                
	                jsonobject1.addProperty("_comment", " ");
	                
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
		}
		
	}
	
	private static void parseTwitchSubsWhiteList(File file) throws IOException
	{
		FileReader input = new FileReader(file);
		
		@SuppressWarnings("resource")
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
	
	static
	{
		//cool kids
		raidersListActive.put("Gen_Deathrow", new RaiderData(new GameProfile(UUID.fromString("4412cc00-65de-43ff-b19a-10e0ec64cc4a"), "Gen_Deathrow"), 10));
		raidersListActive.put("Funwayguy", new RaiderData(new GameProfile(UUID.fromString("c9ecb54c-6f87-485d-b0e1-0e7f8c777d56"), "Funwayguy"), 10));
		raidersListActive.put("Darkosto", new RaiderData(new GameProfile(UUID.fromString("10755ea6-9721-467a-8b5c-92adf689072c"), "Darkosto"), 10));
		raidersListActive.put("Kashdeya", new RaiderData(new GameProfile(UUID.fromString("e49c3c38-a516-4252-ba19-c2b24ff39987"), "Kashdeya"), 10));
		raidersListActive.put("TheMattaBase", new RaiderData(new GameProfile(UUID.fromString("44ba40ef-fd8a-446f-834b-5aea42119c92"), "TheMattaBase"), 10));
		raidersListActive.put("Jsl7", new RaiderData(new GameProfile(UUID.fromString("94c04938-0f86-4960-a175-e3413a07fe8b"), "Jsl7"), 10));
		raidersListActive.put("Turkey2349", new RaiderData(new GameProfile(UUID.fromString("276130dd-8c9a-4814-8328-2578f034e422"), "Turkey2349"), 10));
		raidersListActive.put("Technicalkaos", new RaiderData(new GameProfile(UUID.fromString("bae6421f-cb68-4521-b744-78a3547aed29"), "technicalkaos"), 10));  
		raidersListActive.put("Mondad", new RaiderData(new GameProfile(UUID.fromString("191ace35-d40f-4703-afa0-fe398fce4afa"), "mondad"), 10));

		
		//invasion pack testers
		raidersListActive.put("Bacon_Donut", new RaiderData(new GameProfile(UUID.fromString("024a0d05-3e3e-4ec5-b394-6e1a22d23fdc"), "Bacon_Donut"), 10));
		raidersListActive.put("SlothMonster_", new RaiderData(new GameProfile(UUID.fromString("d2f772cb-80a4-47bd-820d-94b24bb3cccb"), "SlothMonster_"), 10));
		raidersListActive.put("GWSheridan", new RaiderData(new GameProfile(UUID.fromString("84680660-1372-4890-9935-88272138173d"), "GWSheridan"), 10));
		raidersListActive.put("DatFailGamur", new RaiderData(new GameProfile(UUID.fromString("29fa9b6c-8eb5-4544-87fb-5be8effbcf70"), "DatFailGamur"), 10));
		raidersListActive.put("darkphan", new RaiderData(new GameProfile(UUID.fromString("cf1f2cfc-1a85-40a6-aaf4-a17355ac6579"), "darkphan"), 10));
		raidersListActive.put("SinfulDeity", new RaiderData(new GameProfile(UUID.fromString("2ca3e953-c572-4c68-99b4-9d950fe7f580"), "SinfulDeity"), 10));
		raidersListActive.put("Gooderness", new RaiderData(new GameProfile(UUID.fromString("de6721e7-23b4-42ec-95c0-e4c976c7fa85"), "Gooderness"), 10));
		raidersListActive.put("Vash505", new RaiderData(new GameProfile(UUID.fromString("89215ee6-ae53-4d53-b524-86da50000a8f"), "Vash505"), 10));
		
		//forge
		raidersListActive.put("LexManos",  new RaiderData(new GameProfile(UUID.fromString("d3cf097a-438f-4523-b770-ec11e13ecc32"), "LexManos"), 10));
		raidersListActive.put("cpw11",  new RaiderData(new GameProfile(UUID.fromString("59af7399-5544-4990-81f1-c8f2263b00e5"), "cpw11"), 10));
		
		//modders
		raidersListActive.put("ganymedes01",  new RaiderData(new GameProfile(UUID.fromString("539c3716-ce9a-4ba5-9721-310e755abe5c"), "ganymedes01"), 10));
		raidersListActive.put("iChun",  new RaiderData(new GameProfile(UUID.fromString("0b7509f0-2458-4160-9ce1-2772b9a45ac2"), "iChun"), 10));
		
		//youtubers
		raidersListActive.put("direwolf20",  new RaiderData(new GameProfile(UUID.fromString("bbb87dbe-690f-4205-bdc5-72ffb8ebc29d"), "direwolf20"), 10));
		
		//ftb
		raidersListActive.put("tfox83",  new RaiderData(new GameProfile(UUID.fromString("0e205074-25d8-4703-b989-8323b1a35faa"), "tfox83"), 10));
		raidersListActive.put("slowpoke101",  new RaiderData(new GameProfile(UUID.fromString("d2839efc-727a-4263-97ce-3c73cdee5013"), "slowpoke101"), 10));
		
		//mojang
		raidersListActive.put("Notch",  new RaiderData(new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"), 10));
		raidersListActive.put("jeb_",  new RaiderData(new GameProfile(UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6"), "jeb_"), 10));
		raidersListActive.put("EvilSeph",  new RaiderData(new GameProfile(UUID.fromString("020242a1-7b94-4179-9eff-511eea1221da"), "EvilSeph"), 10));
		raidersListActive.put("C418",  new RaiderData(new GameProfile(UUID.fromString("0b8b2245-8018-456c-945d-4282121e1b1e"), "C418"), 10));
		//raidersList.put("Dinnerbone",  new RaiderData(new GameProfile(null, "Dinnerbone"), 10));
		raidersListActive.put("carnalizer",  new RaiderData(new GameProfile(UUID.fromString("f96f3d63-fc7f-46a7-9643-86eb0b3d66cb"), "carnalizer"), 10));
		//raidersList.put("Grumm",  new RaiderData(new GameProfile(null, "Grumm"), 10));
	}

	

}
