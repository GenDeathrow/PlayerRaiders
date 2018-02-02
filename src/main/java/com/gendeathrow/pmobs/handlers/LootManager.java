package com.gendeathrow.pmobs.handlers;

import java.util.ArrayList;
import java.util.Random;

import com.gendeathrow.pmobs.handlers.random.LootDropRandom;
import com.google.gson.JsonObject;

import net.minecraft.util.WeightedRandom;

//TODO Not Implemented yet 
public class LootManager {
	
	public String id;
	
	public ArrayList<LootDropRandom> dropList = new ArrayList<LootDropRandom>();

	public final Random rand = new Random();
	
	public LootManager(String id, LootDropRandom... dropRandoms) {
		this.id = id;

	}
	
	public LootDropRandom getRandomDrop()
	{
		return (LootDropRandom)WeightedRandom.getRandomItem(rand, dropList);
	}
	
	public static LootManager createLootManager(JsonObject dropJson)
	{

		
		return null;
	}
	
	
}
