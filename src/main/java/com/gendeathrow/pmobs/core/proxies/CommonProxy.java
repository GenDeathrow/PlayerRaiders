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
