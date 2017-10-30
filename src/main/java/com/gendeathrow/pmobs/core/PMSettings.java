package com.gendeathrow.pmobs.core;

import java.util.ArrayList;

import com.gendeathrow.pmobs.common.CustomItemDrop;

public class PMSettings 
{
	
	public static boolean attackAll = true;

	public static float daySpawnPercentage = 0.45f;
	
	public static float setEquptmentHard = .1f;
	
	public static float setEquitmentDefault = .025f;
	
	public static int dayDifficultyProgression = 5;
	public static int lastRaidCheck = 0;
	
	public static boolean renderNameTags = true; 
	
	public static boolean sprintersOnlyNight = false; 

	public static boolean renderOverlays = false;

	public static String[] whitelists;
	

	@Deprecated
	public static int daySpawnWeight = 100;
	@Deprecated
	public static int dayMaxGroupSpawn = 1;


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

	
	public static int rangerStartDiff = 1;
	public static int screamerStartDiff = 0;
	public static int bruteStartDiff = 0;
	public static int tweakerStartDiff = 0;
	public static int pyroStartDiff = 0;

	public static boolean tweakerOnlyNight = false;

	//Factions
	public static boolean factionsEnabled = true;

	//ESM SETTINGS
	public static float esmDiggingPercentage = 0.6f;
	public static float esmDemoPercentage = 1f;
	public static int esmDemolitionRaidDiff = 1;
	
	public static float daySpeedRestiction = 0.60F;

	public static ArrayList<CustomItemDrop> screamerDrops;
	public static ArrayList<CustomItemDrop> tweakerDrops;
	public static ArrayList<CustomItemDrop> pyroDrops;
	public static ArrayList<CustomItemDrop> noneDrops;
	public static ArrayList<CustomItemDrop> bruteDrops;
	public static ArrayList<CustomItemDrop> rangerDrops;

	public static int[] noSpawnDays;
	
	public static boolean shouldDaylightSpawm = true;

	public static boolean veryHostile;

//Progresion Difficulty
	public static int HealthIncrease = 20;
	public static int HealthMaxOut = -1;
	public static double BonusHealthPercentageIncrease = 0.025F;
	public static double BonusHealthMaxPercentage = 0.10F;
	
	public static double SpeedPercentageIncrease = 0.05F;
	public static double SpeedMaxPercentage = 0.40F;
	
	
	

	public static int[] dimensions = new int[]{0};
	
	// SpawnSettings
	public static boolean removeVanillaSpawners = false;
	public static int raidersSpawnerWeight = 200;
	public static int raidersSpawnWeight = 100;
	public static boolean safeForaDay = false;	
	public static int raidersMaxGroupSpawn = 3;

	public static boolean spawnNether = true;
	public static boolean spawnEnd = false;

	//esm
	public static int esmDiggingTools = 20;
	public static int esmDiamondDiggingTools = 5;
	public static int esmDiamondToolsRaidDiff = 2;
	public static int esmDigginRaidDiff = 0;

	public static boolean screamerFogOn = true;

	public static boolean torchStopSpawns = true;

	//Items
	public static boolean dropSerum = true;
	public static boolean dropTransmitter = true;
	
}
