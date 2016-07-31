package com.gendeathrow.pmobs.world;

import java.io.File;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityPlayerBase;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

public class WorldLoader 
{

	public static boolean isLoaded = false;
	
	@SubscribeEvent
	public void worldLoad(WorldEvent.Load event)
	{
		if(!RaidersCore.proxy.isClient() || isLoaded) return;
	
            if (!RaidersCore.proxy.isIntergratedServerRunning())
            {
                YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
                MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
                GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
                PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(Minecraft.getMinecraft().mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
                EntityPlayerBase.setProfileCache(playerprofilecache);
                EntityPlayerBase.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(true);
                
                this.isLoaded =true;
            }
            
 	}
	
	@SubscribeEvent
	public void WorldSave(WorldEvent.Save event)
	{
		if(event.getWorld().isRemote) return;

		RaiderManager.Save();
	}
}
