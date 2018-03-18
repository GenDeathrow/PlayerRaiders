package com.gendeathrow.pmobs.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerDelaySerumEffect {

	int delay;
	EntityPlayer player;
	boolean isLoaded = false;
	
	public PlayerDelaySerumEffect(int delayIn, EntityPlayer playerIn) {
		delay = delayIn;
		player = playerIn;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.PlayerTickEvent event){
		
		if(event.player.world == null || event.player.world.isRemote == true || delay-- > 0 || event.player != player) return;

		if(true) {
			player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200));
			player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 3));
			player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1200, 2));
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1));
		}
	  	MinecraftForge.EVENT_BUS.unregister(this);
	}
}