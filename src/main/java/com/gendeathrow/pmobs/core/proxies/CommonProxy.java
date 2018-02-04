package com.gendeathrow.pmobs.core.proxies;

import com.gendeathrow.pmobs.common.BruteSerumHandler;
import com.gendeathrow.pmobs.common.capability.player.PlayerDataProvider;
import com.gendeathrow.pmobs.core.init.RegisterEntities;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy 
{

	public boolean isClient()
	{
		return false;
	}
	
	public boolean isIntergratedServerRunning()
	{
		return false;
	}

	public void registerHandlers()
	{
//        
//		WorldLoader handler = new WorldLoader();
//		
//		MinecraftForge.EVENT_BUS.register(handler);
//		
//		MinecraftForge.EVENT_BUS.register(new EventHandler());
//		MinecraftForge.EVENT_BUS.register(new EntityDropPod(null));
//		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
//		MinecraftForge.EVENT_BUS.register(new BruteSerumHandler());
		
		
	}

	public void registerModels()
	{
		
	}
	
	
	public void preInit(FMLPreInitializationEvent event) 
	{

	}
	
	public void Init(FMLInitializationEvent event) 
	{
		BruteSerumHandler.registerPotion(); 
		PlayerDataProvider.register();
		RegisterEntities.RegisterLootTables();

	}
	public void postInit(FMLPostInitializationEvent event) 
	{
		
	}

	public void registerRenderers() {
		// TODO Auto-generated method stub
		
	}

}
