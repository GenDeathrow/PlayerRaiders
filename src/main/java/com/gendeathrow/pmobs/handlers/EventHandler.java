package com.gendeathrow.pmobs.handlers;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;


public class EventHandler 
{


    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
 	@SubscribeEvent
	public void spawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
 		if(PMSettings.safeForaDay && event.getEntity() instanceof EntityMob &&  event.getWorld().getWorldTime() < 13000)
 		{
 			if(event.getEntity().posY > 50)
 			{
 				event.setResult(Result.DENY);
 			}
 		}
	}
		
		
		
		
		boolean hasChecked = false;
		@SubscribeEvent
		public void onPlayerTick(TickEvent.PlayerTickEvent event) 
		{
			if(!event.player.worldObj.isRemote || hasChecked)
			{
				return;
			}
			hasChecked = true;
			
			if(EquipmentManager.ErrorList.size() > 0)
			{
				event.player.addChatComponentMessage(new TextComponentTranslation(EquipmentManager.ErrorList.size() + " Errors were found in Raiders Equipment json. Check Console for more info."));
			}
			
			RaidersSkinManager.cacheSkins();
			
		}
		
		
//	    @SubscribeEvent
//	    public void onLootTablesLoaded (LootTableLoadEvent event) 
//	    {
//	    	
//	    	
//	    }
		
		
}
