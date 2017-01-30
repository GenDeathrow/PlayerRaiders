package com.gendeathrow.pmobs.client.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

import com.gendeathrow.pmobs.client.ClientEventHandler;
import com.gendeathrow.pmobs.util.Tools;

public class KillCounter 
{
	
	private static File killcounterFile = new File("raidersKillCnt.txt");
	
	public static HashMap<String, Integer> killcnt = new HashMap<String, Integer>();
	
	public static void initilize()
	{
		if(killcounterFile.exists())
		{
			loadKillCounter();
		}
		else SaveKillCounter();
	}
	
	public static void loadKillCounter()
	{
		
		try 
		{
			
			String data = Tools.readFile(killcounterFile.getPath(), Charset.defaultCharset());
			
			String[] split = data.split("\n");
			

			for(String line : split)
			{
				String[] lineSplit = line.split(":");
				if(lineSplit.length > 1)  killcnt.put(lineSplit[0], Integer.parseInt(lineSplit[1]));
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void SaveKillCounter()
	{
		String filedata = "";
		
		for(Entry<String, Integer> killer : killcnt.entrySet())
		{
			filedata += killer.getKey() +":"+ killer.getValue() +"\n";
			
		}
		System.out.print(filedata);
		
		try 
		{
			PrintWriter out = new PrintWriter(killcounterFile);
			out.println(filedata);
			out.close ();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static int addKillCount(String killer)
	{
		
		
		ClientEventHandler.whoKilled = killer;
		
		
		if(killcnt.containsKey(killer))
		{
			killcnt.put(killer, killcnt.get(killer).intValue() + 1);
		}
		else
		{
			killcnt.put(killer, 1);
		}
		
		
		SaveKillCounter();
		
		return killcnt.get(killer);
	}

	public static String getKillCount(String killer) 
	{
		if(killcnt.containsKey(killer))
		{
			return ""+killcnt.get(killer);
		}
		return ""+0;
	}

}
