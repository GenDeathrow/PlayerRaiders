package com.gendeathrow.pmobs.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.mojang.authlib.exceptions.AuthenticationException;
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
	
		Iterator<RaiderData> raidersList = RaiderManager.getAllRaiders().values().iterator();
		
		while(raidersList.hasNext())
		{
			updateProfile(raidersList.next());
		}
		
		
	}
	
	
	private static Thread thread;
	
	private static Thread thread2;
	private static List<EntityRaiderBase> raiders = new ArrayList<EntityRaiderBase>();
	
	private static List<RaiderData> raidersdata = new ArrayList<RaiderData>();
	
	public static void updateProfile(RaiderData raiderInfo) {

		
		if(!badraiders.contains(raiderInfo.getOwnerName())) raidersdata.add(raiderInfo);

		if (thread2 == null || thread2.getState() == Thread.State.TERMINATED) {
			thread2 = new Thread(new Runnable() 
			{

				@Override
				public void run() {
					
					while (!raidersdata.isEmpty()) 
					{
						RaiderData raider = raidersdata.get(0);

						//raider.setPlayerProfile(TileEntitySkull.updateGameprofile(raider.getPlayerProfile()));
						raider.setProfile(setupProfiles(raider.getProfile()));
						
						try 
						{
							//System.out.println("Getting Profile "+ raider.getOwnerName());
							Thread.sleep(250);
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						
						raidersdata.remove(0);
					}
				}
			});

			thread2.start();
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

						//boolean flag = profileCache.getUsernames() != null ? (Arrays.asList(profileCache.getUsernames()).contains(raider.getOwner())) : false;
						
						//raider.setPlayerProfile(TileEntitySkull.updateGameprofile(raider.getPlayerProfile()));
						raider.setPlayerProfile(setupProfiles(raider.getPlayerProfile()));
						raider.setProfileUpdated(true);
						
						//RaiderManager.setRaiderProfile(raider.getOwner(), raider.getPlayerProfile());
						
						try 
						{
							System.out.println("->"+ raider.getOwner());
							//if(!flag)
							//{
							
							
								Thread.sleep(250);
							//}
						} catch (InterruptedException e) 
						{
							e.printStackTrace();
						}
						
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
	
//		if(!INSTANCE.cachedSkins.containsKey(profile.getName()))
//		{
//			
//		}
		
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
            	GameProfile gameprofile;
                gameprofile = RaidersSkinManager.INSTANCE.profileCache.getGameProfileForUsername(input.getName());

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
