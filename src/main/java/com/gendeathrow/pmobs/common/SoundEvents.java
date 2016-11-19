package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundEvents 
{

	public static SoundEvent RAIDERS_SAY;
	public static SoundEvent RAIDERS_HURT;
	public static SoundEvent RAIDERS_DEATH;
	
	public static void register()
	{
		RaidersCore.logger.info("Loading Sounds");
		
		ResourceLocation locationSay = new ResourceLocation(RaidersCore.MODID, "mob.raiders.say");
		ResourceLocation locationHurt = new ResourceLocation(RaidersCore.MODID, "mob.raiders.hurt");

		
		RAIDERS_SAY = new SoundEvent(locationSay);
		RAIDERS_HURT = new SoundEvent(locationHurt);
		
		GameRegistry.register(SoundEvents.RAIDERS_SAY, locationSay);
		GameRegistry.register(SoundEvents.RAIDERS_HURT, locationHurt);
	}
}
