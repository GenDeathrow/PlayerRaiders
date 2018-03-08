package com.gendeathrow.pmobs.util;

import java.io.File;
import java.io.FileReader;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonHelper 
{

	public static JsonElement ReadJsonFile(File f) 
	{
		try
		{
			FileReader fr = new FileReader(f);
			JsonElement json = new Gson().fromJson(fr, JsonElement.class);
			fr.close();
			return json;
		} catch(Exception e)
		{
			RaidersMain.logger.log(Level.ERROR, "An error occured while loading JSON from file:", e);
			return new JsonObject(); // Just a safety measure against NPEs
		}
	}
	
}
