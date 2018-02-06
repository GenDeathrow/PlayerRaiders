package com.gendeathrow.pmobs.core;

public class PMSettings 
{
	
	public static boolean attackAll = false;

	
	public static float setEquptmentHard = .1f;
	
	public static float setEquitmentDefault = .025f;
	
	public static int dayDifficultyProgression = 5;
	public static int lastRaidCheck = 0;
	
	public static boolean renderNameTags = true; 
	
	public static boolean sprintersOnlyNight = false; 

	public static boolean renderOverlays = false;

	public static String[] whitelists;
	
	public static boolean leapAttackAI = true;

	// Raiders Classes
	public static boolean raiderClass = true;
	public static boolean pyroClass = true;
	public static boolean bruteClass = true;
	public static boolean tweakersClass = true;
	public static boolean screamerClass = true;
	public static boolean rangerClass = true;
	
	public static int pyroWeight = 20;
	public static int bruteWeight = 94;
	public static int tweakerWeight = 60;
	public static int noneWeight = 100;
	public static int screamerWeight = 10;
	public static int rangerWeight = 100;

	
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

	public static int[] noSpawnDays;
	
	public static boolean shouldDaylightSpawm = false;

	public static boolean veryHostile;

//Progresion Difficulty
	public static int HealthIncrease = 2;
	public static int HealthMaxOut = -1;

	public static double SpeedIncrease = 0.01F;
	public static double SpeedMaxIncrease = 0.12F;
	
	public static double dmgIncrease = 0.25f;
	public static double dmgMaxIncrease = 6;
	
	public static double armorIncrease = 0.25f;
	public static double armorMax = 3;
	
	public static double knockbackIncrease = 0.05;
	public static double knockbackMax = 0.5;
	
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
