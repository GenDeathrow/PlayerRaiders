package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RaidersSoundEvents 
{

	public static SoundEvent RAIDERS_SAY;
	public static SoundEvent RAIDERS_HURT;
	public static SoundEvent RAIDERS_LAUGH;
	public static SoundEvent RAIDERS_SCREAM;
	public static SoundEvent RAIDERS_DEATH;
	public static SoundEvent RAIDERS_BRUTE_LAUGH;
	public static SoundEvent RAIDERS_WITCH_SCREAM;
	public static SoundEvent RAIDERS_WITCH_CRY;
	public static SoundEvent RAID_DAY_SUSPENSE;
	public static SoundEvent SONIC_BOOM;
	public static SoundEvent ROCKET_AMBIENT;
	public static SoundEvent COMS_BEEP;
	public static SoundEvent SERUM_ANGER; 
	
	public static void register()
	{
		RaidersMain.logger.info("Loading Sounds");
		
		RAIDERS_SAY = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.say"));
		RAIDERS_HURT = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.hurt"));
		RAIDERS_LAUGH = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.laugh"));
		RAIDERS_BRUTE_LAUGH = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.brutelaugh"));
		RAIDERS_SCREAM = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.scream"));
		RAIDERS_WITCH_SCREAM = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.screamerscream"));
		RAIDERS_WITCH_CRY = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "mob.raiders.screamercry"));
		RAID_DAY_SUSPENSE = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "ambient.raiddayboom"));
		SONIC_BOOM = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "ambient.sonicboom"));
		ROCKET_AMBIENT = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "ambient.rocketambient"));
		COMS_BEEP = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "item.coms_beep"));
		SERUM_ANGER = registerSoundEvent(new ResourceLocation(RaidersMain.MODID, "item.serum_anger"));
	}
	
	protected static SoundEvent registerSoundEvent(ResourceLocation id){
		SoundEvent event = new SoundEvent(id);
		GameRegistry.register(event, id);
		return event;
	}
	
}
