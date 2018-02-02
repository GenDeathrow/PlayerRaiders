package com.gendeathrow.pmobs.util;

import java.io.File;
import java.io.FileReader;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonHelper 
{

	public static JsonObject ReadJsonFile(File f) 
	{
		try
		{
			FileReader fr = new FileReader(f);
			JsonObject json = new Gson().fromJson(fr, JsonObject.class);
			fr.close();
			return json;
		} catch(Exception e)
		{
			RaidersMain.logger.log(Level.ERROR, "An error occured while loading JSON from file:", e);
			return new JsonObject(); // Just a safety measure against NPEs
		}
	}
	
}
