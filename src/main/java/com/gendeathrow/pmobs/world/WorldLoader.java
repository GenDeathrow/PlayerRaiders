package com.gendeathrow.pmobs.world;

import java.io.File;
import java.util.UUID;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class WorldLoader 
{

	public static boolean isLoaded = false;
	
	@SubscribeEvent
	public static void worldLoad(WorldEvent.Load event)
	{

		Raiders_WorldData.get(event.getWorld());
		
		if(!RaidersMain.proxy.isClient() || isLoaded) return;
	
            if (!RaidersMain.proxy.isIntergratedServerRunning())
            {
                YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
                MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
                GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
                PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.getMinecraft().mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
                RaidersSkinManager.profileCache = playerprofilecache;
                RaidersSkinManager.sessionService = minecraftsessionservice;
                PlayerProfileCache.setOnlineMode(true);
                
                isLoaded =true;
            }
            
 	}
	
	@SubscribeEvent
	public void WorldSave(WorldEvent.Save event)
	{
		if(event.getWorld().isRemote) return;

		RaiderManager.Save();
		
		Raiders_WorldData.get(event.getWorld()).markDirty();
	}
}
