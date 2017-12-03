package com.gendeathrow.pmobs.client;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler {


    static boolean hasCheckedSkins = false;
  //
    

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {

    	if(!hasCheckedSkins)
    	{
        	System.out.println("CacheSkins");
    		hasCheckedSkins = true;
			
//    		if(EquipmentManager.ErrorList.size() > 0)
//    		{
//    			event.player.addChatComponentMessage(new TextComponentTranslation(EquipmentManager.ErrorList.size() + " Errors were found in Raiders Equipment json. Check Console for more info."));
//    		}
    		//RaidersSkinManager.INSTANCE.cacheSkins();
    		RaidersSkinManager.cacheSkins();
    	}
    }
}
