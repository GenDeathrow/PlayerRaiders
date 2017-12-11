package com.gendeathrow.pmobs.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.handlers.RaiderData;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;

public class RaidersSkinManager 
{
	public static PlayerProfileCache profileCache;
	public static MinecraftSessionService sessionService;
	
	public final HashMap<String, ResourceLocation> cachedSkins = new HashMap<String, ResourceLocation>();
	
	public static final RaidersSkinManager INSTANCE = new RaidersSkinManager();
	
	private static Thread thread2;

	private static List<RaiderData> raidersdata = new ArrayList<RaiderData>();	
	
	/**
	 *  Run thur the Raiders list and Cache the Skin's localy
	 *  
	 */
	public static void cacheSkins()
	{
		Iterator<RaiderData> raidersList = RaiderManager.getAllRaiders().values().iterator();
		
		while(raidersList.hasNext())
			updateProfile(raidersList.next());
	}
	
	
	/**
	 * 
	 * @param raiderInfo
	 */
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

						raider.setProfile(TileEntitySkull.updateGameprofile(raider.getProfile()));
						
						try 
						{
							System.out.println("Getting Profile "+ raider.getOwnerName() +" - "+ raider.getProfile().getId().toString());
							Thread.sleep(300);
							
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

	private static List<String> badraiders = new ArrayList<String>();
	public static void addToBadList(String owner) 
	{
		if(!badraiders.contains(owner))
		{
			badraiders.add(owner);
			RaidersMain.logger.error("Could not Get this players profile."+ owner +" Added to the Naughty List");
		}
	}
	
	
	/**
	 * Download players skin from mojang
	 * @return
	 */
	@SuppressWarnings(value = { "unchecked" })
    public static ResourceLocation DownloadPlayersSkin(EntityRaiderBase raider){
		ResourceLocation resourcelocation =  DefaultPlayerSkin.getDefaultSkinLegacy();
		
        if (raider.getPlayerProfile() != null)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(raider.getPlayerProfile());

            if (map.containsKey(Type.SKIN))
            {
                resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
            }
            else
            {
                UUID uuid = EntityPlayer.getUUID(raider.getPlayerProfile());
                resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }
        
        
        
//		if (raider.getPlayerProfile() != null && raider.getPlayerProfile().getName() != null) 
//		{
//			Minecraft minecraft = Minecraft.getMinecraft();
//			
//            Property property = (Property)Iterables.getFirst(raider.getPlayerProfile().getProperties().get("textures"), null);
//
//            if ((raider.profileset == true && property == null ))
//            {
//            	return resourcelocation;
//            }
//            else if(raider.profileset = false)
//            {
//            	return resourcelocation;
//            }
//            
//            
//			Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(RaiderManager.getRaiderProfile(raider.getOwner()));
//
//			if (map.containsKey(Type.SKIN))
//			{
//				resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
//			}
//		}
//		else
//		{
//			if(RaiderManager.getRaiderProfile(raider.getOwner()) != null)
//			{
//				//raider.setPlayerProfile(RaiderManager.getRaiderProfile(raider.getOwner()));
//			}
//			else
//			{
//				raider.setPlayerProfile(RaiderManager.getRaiderProfile(raider.getOwner()));
//				
//				raider.updateProfile(raider);
//				
//				RaiderManager.setRaiderProfile(raider.getOwner());
//			
//			}
//		}
		
		return resourcelocation;
    	
    }
	
}
