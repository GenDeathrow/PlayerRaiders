package com.gendeathrow.pmobs.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gendeathrow.pmobs.handlers.EventHandler;
import com.gendeathrow.pmobs.world.WorldLoader;

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
	
	@SuppressWarnings("deprecation")
	public void registerHandlers()
	{
		WorldLoader handler = new WorldLoader();
		MinecraftForge.EVENT_BUS.register(handler);
		
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
	}

	public void registerRenderers()
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
		
	}
	public void postInit(FMLPostInitializationEvent event) 
	{
		
	}

}
