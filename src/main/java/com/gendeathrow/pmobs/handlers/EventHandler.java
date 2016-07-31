package com.gendeathrow.pmobs.handlers;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;


public class EventHandler 
{

		@SubscribeEvent
		public void spawnEvent(EntityJoinWorldEvent event)
		{
			if(PMSettings.safeForaDay && event.getEntity() instanceof EntityPlayerRaider &&  event.getWorld().getWorldTime() < 13000)
			{
				if(event.getEntity().posY > 50)
				{
					event.setCanceled(true);
				}
			}
		}
		
}
