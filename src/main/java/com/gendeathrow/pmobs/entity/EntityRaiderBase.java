package com.gendeathrow.pmobs.entity;

import java.util.ArrayList;
import java.util.UUID;

import com.gendeathrow.pmobs.util.DifficultyProgression;
import com.gendeathrow.pmobs.util.EnumFaction;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;

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
	
	public static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    public static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    public static final UUID DAY_SPEED_MODIFIER_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    public static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");

    private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    
	private static final AttributeModifier DAY_SPEED_MODIFIER = new AttributeModifier(DAY_SPEED_MODIFIER_ID, "Day speed reduced", PMSettings.daySpeedRestiction - 1, 2);

	
	private final ResourceLocation SKIN = null; 

	protected DifficultyProgression difficultyManager;
	
    private final InventoryBasic raidersInventory;
	
	public EntityRaiderBase(World worldIn){
		super(worldIn);
		
		this.raidersInventory = new InventoryBasic("Items", false, 3);

	}

    //TODO
	// Get Raiders Skin
	public ResourceLocation getLocationSkin(){
		return null;
    }
	
	
    //TODO
	protected void initEntityAI()
	{
		
	}
	
	
    public void setArmsRaised(boolean armsRaised)
    {
        this.getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
    }

    public boolean isArmsRaised()
    {
        return ((Boolean)this.getDataManager().get(ARMS_RAISED)).booleanValue();
    }
    
    public void setRaiderFaction(EnumFaction faction)
    {
    	this.getDataManager().set(RAIDER_FACTION, faction.ordinal());
    }
    

}
