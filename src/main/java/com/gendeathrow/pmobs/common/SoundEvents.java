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
	
	public static void register()
	{
		RaidersCore.logger.info("Loading Sounds");
		
		ResourceLocation locationSay = new ResourceLocation(RaidersCore.MODID, "mob.raiders.say");
		ResourceLocation locationHurt = new ResourceLocation(RaidersCore.MODID, "mob.raiders.hurt");
		ResourceLocation locationlaugh = new ResourceLocation(RaidersCore.MODID, "mob.raiders.laugh");
		ResourceLocation locationscream = new ResourceLocation(RaidersCore.MODID, "mob.raiders.scream");

		
		RAIDERS_SAY = new SoundEvent(locationSay);
		RAIDERS_HURT = new SoundEvent(locationHurt);
		RAIDERS_LAUGH = new SoundEvent(locationlaugh);
		RAIDERS_SCREAM = new SoundEvent(locationscream);
		
		GameRegistry.register(SoundEvents.RAIDERS_SAY, locationSay);
		GameRegistry.register(SoundEvents.RAIDERS_LAUGH, locationlaugh);
		GameRegistry.register(SoundEvents.RAIDERS_HURT, locationHurt);
		GameRegistry.register(SoundEvents.RAIDERS_SCREAM, locationscream);
	}
}
