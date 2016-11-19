package com.gendeathrow.pmobs.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.handlers.RaiderData;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;

public class RaidersSkinManager 
{
	public static PlayerProfileCache profileCache;
	public static MinecraftSessionService sessionService;
	
	//public final HashMap<String, ResourceLocation> cachedSkins = new HashMap<String, ResourceLocation>();
	public final HashMap<String, ResourceLocation> cachedSkins = new HashMap<String, ResourceLocation>();
	public static final RaidersSkinManager INSTANCE = new RaidersSkinManager();
	
	public static void cacheSkins()
	{
		
		
		//RaidersSkinManager.updateProfile();
		
		for(Entry<String, RaiderData> raider : RaiderManager.raidersList.entrySet())
		{		
//			if(!INSTANCE.cachedSkins.containsKey(raider.getValue().getProfile().getName()))
//			{
//				System.out.println("get raider skin for "+ raider.getValue().getProfile().getName());
//				INSTANCE.cachedSkins.put(raider.getValue().getProfile().getName(), INSTANCE.cacheRaidersSkins(raider.getValue().getProfile()));
//			}
		}
		
//					for(Entry<String, RaiderData> raider : RaiderManager.raidersList.entrySet())
//					{
//						GameProfile profile = raider.getValue().getProfile();
//		
//						//profile = TileEntitySkull.updateGameprofile(profile);
//						
//						
//						//profileCache.addEntry(gameProfile);
//						setupProfiles(profile);
//					
//						if(!cachedSkins.containsKey(profile.getName()))
//						{
//							cachedSkins.put(profile.getName(), cacheRaidersSkins(profile));
//						}
//					}
	}
	
	
	private static Thread thread;	
	private static List<EntityRaiderBase> raiders = new ArrayList<EntityRaiderBase>();
	
	public synchronized static void updateProfile() {

		
		if (thread == null || thread.getState() == Thread.State.TERMINATED) {

			thread = new Thread(new Runnable() 
			{

				@Override
				public void run() 
				{
					
					for(Entry<String, RaiderData> raider : RaiderManager.raidersList.entrySet())
					{
						//System.out.println("raider profile:"+ raider.getKey());
						GameProfile profile = raider.getValue().getProfile();

						
						
						//setupProfiles(profile);
						
						raider.getValue().setProfile(TileEntitySkull.updateGameprofile(profile));
					
						
						
//						if(!INSTANCE.cachedSkins.containsKey(raider.getValue().getProfile().getName()))
//						{
//							System.out.println("get raider skin for "+ raider.getValue().getProfile().getName());
//							INSTANCE.cachedSkins.put(raider.getValue().getProfile().getName(), INSTANCE.cacheRaidersSkins(raider.getValue().getProfile()));
//						}

					}
				}
			});

			thread.start();
		}
	}
	
	public static void updateProfile(EntityRaiderBase raider) 
	{
		
		if(!badraiders.contains(raider.getOwner())) raiders.add(raider);
		
		

		if (thread == null || thread.getState() == Thread.State.TERMINATED) {
			thread = new Thread(new Runnable() 
			{

				@Override
				public void run() {
					while (!raiders.isEmpty()) 
					{
						EntityRaiderBase raider = raiders.get(0);
						
						raider.setPlayerProfile(TileEntitySkull.updateGameprofile(raider.getPlayerProfile()));
						
						raider.setProfileUpdated(true);
			              
						raiders.remove(0);
						
					}
				}
			});

			thread.start();
		}
	}
	
	public static void cacheSkin(GameProfile profile)
	{
		
		profile = TileEntitySkull.updateGameprofile(profile);
		
		//setupProfiles(profile);
	
		if(!INSTANCE.cachedSkins.containsKey(profile.getName()))
		{
			
		}
		
	}
	
	public static GameProfile setupProfiles(GameProfile input)
	{
        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
        {
            if (input.isComplete() && input.getProperties().containsKey("textures"))
            {
                return input;
            }
            else if (RaidersSkinManager.INSTANCE.profileCache != null && RaidersSkinManager.sessionService != null)
            {
                GameProfile gameprofile = RaidersSkinManager.INSTANCE.profileCache.getGameProfileForUsername(input.getName());

                if (gameprofile == null)
                {
                    return input;
                }
                else
                {
                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
                    
                    if (property == null)
                    {
                    	gameprofile = RaidersSkinManager.sessionService.fillProfileProperties(gameprofile, true);
                    }
                    property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
                    
                    return gameprofile;
                }
            }
            else
            {
                return input;
            }
        }
        else
        {
            return input;
        }
	}
	
	
	
    public ResourceLocation cacheRaidersSkins(GameProfile gameprofile)
    {
    	gameprofile = RaidersSkinManager.INSTANCE.profileCache.getGameProfileForUsername(gameprofile.getName());
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
		
		if(gameprofile != null)
		{
               Map<Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(gameprofile);

               if (map.containsKey(Type.SKIN))
               {
            	     resourcelocation = Minecraft.getMinecraft().getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
               }
               else
               {
                   UUID uuid = EntityPlayer.getUUID(gameprofile);
                   resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
               }
 		}
    	return resourcelocation;
    }

	private static List<String> badraiders = new ArrayList<String>();
	public static void addToBadList(String owner) 
	{
		
		if(!badraiders.contains(owner))
		{
			badraiders.add(owner);
			RaidersCore.logger.error("Could not Get this players profile."+ owner +" Added to the Naughty List");
		}
		// TODO Auto-generated method stub
		
	}
}
