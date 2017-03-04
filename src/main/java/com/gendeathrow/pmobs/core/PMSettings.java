package com.gendeathrow.pmobs.core;

import java.util.ArrayList;

import com.gendeathrow.pmobs.core.ConfigHandler.ItemDrop;

public class PMSettings 
{
	
	public static float daySpawnPercentage = 0.45f;
	
	public static int raidersSpawnWeight = 100;
	
	public static int raidersMaxGroupSpawn = 3;
	
	public static float setEquptmentHard = .1f;
	
	public static float setEquitmentDefault = .025f;
	
	public static int dayDifficultyProgression = 5;
	
	public static boolean renderNameTags = true; 
	
	public static boolean sprintersOnlyNight = false; 
	
	public static boolean safeForaDay = false;

	public static boolean renderOverlays = true;

	public static String[] whitelists;
	

	@Deprecated
	public static int daySpawnWeight = 100;
	@Deprecated
	public static int dayMaxGroupSpawn = 1;

	public static boolean removeVanillaSpawners = false;

	public static int raidersSpawnerWeight = 200;

	public static boolean leapAttackAI = true;

	// Raiders Classes
	public static boolean pyroClass = true;
		public static float pyroChance = 0.05f;
	public static boolean bruteClass = true;
	public static boolean tweakersClass = true;
	public static boolean screamerClass = true;
	public static boolean rangerClass = true;
	
	public static int pyroWeight = 4;
	public static int bruteWeight = 7;
	public static int tweakerWeight = 5;
	public static int noneWeight = 80;
	public static int screamerWeight = 2;
	public static int rangerWeight = 10;
	
	public static boolean tweakerOnlyNight = false;
	//Factions
	
	public static boolean factionsEnabled = true;

	//ESM SETTINGS
	public static float esmDiggingPercentage = 0.3f;
	public static float esmDemoPercentage = 0.05f;
	public static int esmDemolitionRaidDiff = 1;
	
	public static float daySpeedRestiction = 0.40F;

	public static ArrayList<ItemDrop> screamerDrops;
	public static ArrayList<ItemDrop> tweakerDrops;
	public static ArrayList<ItemDrop> pyroDrops;
	public static ArrayList<ItemDrop> noneDrops;
	public static ArrayList<ItemDrop> bruteDrops;
	public static ArrayList<ItemDrop> rangerDrops;

	public static int[] noSpawnDays;

}
