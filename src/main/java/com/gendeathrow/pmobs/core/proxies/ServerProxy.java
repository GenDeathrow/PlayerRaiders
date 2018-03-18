package com.gendeathrow.pmobs.core.proxies;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy
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
	
	public void preInit(FMLPreInitializationEvent event) 
	{
	}
	
	public void Init(FMLInitializationEvent event) 
	{

		
	}
	public void postInit(FMLPostInitializationEvent event) 
	{
		
	}

	public void registerRenderers() 
	{
		
	}
}
