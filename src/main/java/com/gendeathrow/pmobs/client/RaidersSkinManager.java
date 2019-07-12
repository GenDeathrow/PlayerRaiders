package com.gendeathrow.pmobs.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.handlers.RaiderData;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
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
	 * 
	 * @param raiderInfo
	 */
	public static void updateProfile(RaiderData raiderInfo) {
		if(raiderInfo == null) return;
		
		if(!raidersdata.contains(raiderInfo)) raidersdata.add(raiderInfo);

		if (thread2 == null || thread2.getState() == Thread.State.TERMINATED) {
			thread2 = new Thread(new Runnable() 
			{

				@Override
				public void run() {
					
					while (!raidersdata.isEmpty()) 
					{
						RaiderData raider = raidersdata.get(0);
						raider.setProfile(TileEntitySkull.updateGameProfile(raider.getProfile()));
						raider.setProfileUpdated();
						
						try 
						{
//							System.out.println("Getting Profile "+ raider.getOwnerName() +" - "+ raider.getProfile().getId().toString());
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

	/**
	 * Download players skin from mojang
	 * @return
	 */
    public static ResourceLocation DownloadPlayersSkin(EntityRaiderBase raider){
		ResourceLocation resourcelocation =  DefaultPlayerSkin.getDefaultSkinLegacy();
	
		RaiderData playerSkinProfile = RaiderManager.getRaiderProfile(raider.getPlayerProfile());
		
		if (raider.getPlayerProfile() == null || playerSkinProfile == null)
			return resourcelocation;
			
		if(playerSkinProfile.hasCustomSkin())
		{
			return LocalCustomSkinManager.loadSkin(playerSkinProfile);
		}
		else 
		{
			if(playerSkinProfile.hasUpdatedProfile()) {
				Minecraft minecraft = Minecraft.getMinecraft();
				Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(raider.getPlayerProfile());

				if (map.containsKey(Type.SKIN)) {
					resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
				}
			}
			else {
				updateProfile(playerSkinProfile);
			}
		}
		return resourcelocation;
    	
    }
	
    
    
    /// Testing fucntion for
	static LocalCustomSkinManager raidermanager = new LocalCustomSkinManager(Minecraft.getMinecraft().getTextureManager());
	
//	public static ResourceLocation test() {
//		
//		File f = new File(MainConfig.configDir, "/assets/skins/imp.png");
//		return raidermanager.loadSkin(f.getAbsolutePath(), "imp" );
//	}
//	
}
