package com.gendeathrow.pmobs.entity;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.util.DifficultyProgression;
import com.gendeathrow.pmobs.util.EnumFaction;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityRaiderBase extends EntityMob{

	
	private static PlayerProfileCache profileCache;
	private static MinecraftSessionService sessionService;
	private static ArrayList<GameProfile> profileAskedfor;
	
	protected GameProfile playerProfile;
	
    protected boolean profileset = false;

    public static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    public static final UUID DAY_SPEED_MODIFIER_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    public static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");

    private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    
	private static final AttributeModifier DAY_SPEED_MODIFIER = new AttributeModifier(DAY_SPEED_MODIFIER_ID, "Day speed reduced", PMSettings.daySpeedRestiction - 1, 2);

	
	private final ResourceLocation SKIN = null; 

	protected DifficultyProgression difficultyManager;
	
    private final InventoryBasic raidersInventory;
	
	public EntityRaiderBase(World worldIn){
		super(worldIn);
		
		this.raidersInventory = new InventoryBasic("Items", false, 3);

	}

    public GameProfile getPlayerProfile()
    {
		return this.playerProfile;
    }
    
    //TODO
	// Get Raiders Skin
	@SuppressWarnings(value = { "unchecked" })
    public ResourceLocation getLocationSkin()
    {
		ResourceLocation resourcelocation =  DefaultPlayerSkin.getDefaultSkinLegacy();
		
	
		
		if (this.getPlayerProfile() != null && this.getPlayerProfile().getName() != null) 
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			
            Property property = (Property)Iterables.getFirst(this.getPlayerProfile().getProperties().get("textures"), null);

            if ((this.profileset == true && property == null ))
            {
            	return resourcelocation;
            }
            else if(this.profileset = false)
            {
            	return resourcelocation;
            }
            
            
			Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(getPlayerProfile());

			if (map.containsKey(Type.SKIN))
			{
				resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
			}
		}
		else
		{
			if(RaiderManager.getRaiderProfile(this.getOwner()) != null)
			{
				this.setPlayerProfile(RaiderManager.getRaiderProfile(this.getOwner()));
			}
			else
			{
				this.setPlayerProfile(RaiderManager.getRaiderProfile(this.getOwner()));
				
				RaidersSkinManager.updateProfile(this);
			
			}
		}
		
		return resourcelocation;
    	
    }

		
    //TODO
	protected void initEntityAI()
	{
		
	}
	
    public void setRaiderFaction(EnumFaction faction)
    {
    	this.getDataManager().set(RAIDER_FACTION, faction.ordinal());
    }
    

}
