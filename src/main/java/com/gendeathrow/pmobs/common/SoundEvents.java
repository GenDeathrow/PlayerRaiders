package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundEvents 
{

	public static SoundEvent RAIDERS_SAY;
	public static SoundEvent RAIDERS_HURT;
	public static SoundEvent RAIDERS_LAUGH;
	public static SoundEvent RAIDERS_SCREAM;
	public static SoundEvent RAIDERS_DEATH;
	public static SoundEvent RAIDERS_BRUTE_LAUGH;
	public static SoundEvent RAIDERS_WITCH_SCREAM;
	public static SoundEvent RAIDERS_WITCH_CRY;
	
	public static void register()
	{
		RaidersCore.logger.info("Loading Sounds");
		
		ResourceLocation locationSay = new ResourceLocation(RaidersCore.MODID, "mob.raiders.say");
		ResourceLocation locationHurt = new ResourceLocation(RaidersCore.MODID, "mob.raiders.hurt");
		ResourceLocation locationlaugh = new ResourceLocation(RaidersCore.MODID, "mob.raiders.laugh");
		ResourceLocation locationbrutelaugh = new ResourceLocation(RaidersCore.MODID, "mob.raiders.brutelaugh");
		ResourceLocation locationscream = new ResourceLocation(RaidersCore.MODID, "mob.raiders.scream");
		ResourceLocation locationWitchscream = new ResourceLocation(RaidersCore.MODID, "mob.raiders.screamerscream");
		ResourceLocation locationWitchscry = new ResourceLocation(RaidersCore.MODID, "mob.raiders.screamercry");

		
		RAIDERS_SAY = new SoundEvent(locationSay);
		RAIDERS_HURT = new SoundEvent(locationHurt);
		RAIDERS_LAUGH = new SoundEvent(locationlaugh);
		RAIDERS_SCREAM = new SoundEvent(locationscream);
		RAIDERS_BRUTE_LAUGH = new SoundEvent(locationbrutelaugh);
		RAIDERS_WITCH_SCREAM = new SoundEvent(locationWitchscream);
		RAIDERS_WITCH_CRY = new SoundEvent(locationWitchscry);
		
		GameRegistry.register(SoundEvents.RAIDERS_SAY, locationSay);
		GameRegistry.register(SoundEvents.RAIDERS_LAUGH, locationlaugh);
		GameRegistry.register(SoundEvents.RAIDERS_HURT, locationHurt);
		GameRegistry.register(SoundEvents.RAIDERS_SCREAM, locationscream);
		GameRegistry.register(SoundEvents.RAIDERS_BRUTE_LAUGH, locationbrutelaugh);
		GameRegistry.register(SoundEvents.RAIDERS_WITCH_SCREAM, locationWitchscream);
		GameRegistry.register(SoundEvents.RAIDERS_WITCH_CRY, locationWitchscry);
	}
}
