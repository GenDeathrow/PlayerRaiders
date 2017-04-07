package com.gendeathrow.pmobs.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gendeathrow.pmobs.common.capability.CapabilityHandler;
import com.gendeathrow.pmobs.common.capability.player.IPlayerData;
import com.gendeathrow.pmobs.common.capability.player.PlayersData;
import com.gendeathrow.pmobs.common.capability.player.PlayersDataStorage;
import com.gendeathrow.pmobs.core.init.ModItems;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.handlers.EventHandler;
import com.gendeathrow.pmobs.world.WorldLoader;

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
