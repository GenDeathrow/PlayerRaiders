package com.gendeathrow.pmobs.handlers;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.google.common.base.Predicate;


public class EventHandler 
{

    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
    



		@SubscribeEvent
		public void spawnEvent(EntityJoinWorldEvent event)
		{
			//if(PMSettings.safeForaDay && event.getEntity() instanceof EntityPlayerRaider &&  event.getWorld().getWorldTime() < 13000)
			if(PMSettings.safeForaDay && event.getEntity() instanceof EntityMob &&  event.getWorld().getWorldTime() < 13000)
			{
				if(event.getEntity().posY > 50)
				{
					event.setCanceled(true);
				}
			}
			else if(!event.getWorld().isRemote)
			{
				

//				if(!(event.getEntity() instanceof EntityRaiderBase)) event.setCanceled(true);
//				
				//				
//				List<EntityRaiderBase> list = event.getWorld().getEntities(EntityRaiderBase.class,  new Predicate<EntityRaiderBase>() 
//						{
//				    		@Override public boolean apply(EntityRaiderBase number) 
//				    		{
//				    			return true;
//				    		}       
//						}
//				);
//
//				
//				System.out.println(list.size()+1 +">"+ (PMSettings.daySpawnPercentage * 80));
//				
//				
//				if(list.size()+1 > (PMSettings.daySpawnPercentage * 80))
//				{
//					event.setCanceled(true);
//					System.out.println("Entity Size: "+ list.size());
//				}


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
		
		
	    @SubscribeEvent
	    public void onLootTablesLoaded (LootTableLoadEvent event) 
	    {
	    	
	    	
	    }
		
		
}
