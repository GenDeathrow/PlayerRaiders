package com.gendeathrow.pmobs.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.common.EnumFaction;
import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealFarmland;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealItemInv;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.storage.InventoryStroageModifiable;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityRaiderBase extends EntityMob{

	protected GameProfile playerProfile;
	
    public boolean profileset = false;

    public static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    public static final UUID DAY_SPEED_MODIFIER_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    public static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");

    private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> OVERLAY_VARIANT = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
  
	private static final AttributeModifier DAY_SPEED_MODIFIER = new AttributeModifier(DAY_SPEED_MODIFIER_ID, "Day speed reduced", PMSettings.daySpeedRestiction - 1, 2);

	protected DifficultyProgression difficultyManager;
	
    private final InventoryStroageModifiable raidersInventory;
    
	private float raiderWidth = 0.6F;
	private float raiderHeight = 1.95F;
	
	protected final EntityAINearestAttackableTarget attackPlayerAI = new EntityAINearestAttackableTarget(this, EntityPlayer.class, true);
	protected final EntityAINearestAttackableTarget attackVillagerAI =  new EntityAINearestAttackableTarget(this, EntityVillager.class, false);
	protected final EntityAINearestAttackableTarget attackLivingAI = new EntityAINearestAttackableTarget(this, EntityLiving.class, true);
	    
	protected final EntityAIWatchClosest watchClosestAI =  new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F);
	protected final EntityAIWander wanderAI = new EntityAIWander(this, 1.0D);
	protected final EntityAILookIdle lookIdleAI = new EntityAILookIdle(this);
	    
	
	public EntityRaiderBase(World worldIn){
		super(worldIn);
		this.raidersInventory = new InventoryStroageModifiable("Items", 3);
		this.setSize(0.6F, 1.95F);
		difficultyManager = new DifficultyProgression(this);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(SKIN_VARIANT, "Steve");
        this.getDataManager().register(RAIDER_FACTION, Integer.valueOf(0));
        this.getDataManager().register(OVERLAY_VARIANT,Integer.valueOf(0));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
	        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}

    @Override
	protected void initEntityAI() {
    	super.initEntityAI();
    	
        this.tasks.addTask(0, new EntityAISwimming(this));
        //this.tasks.addTask(4, new EntityAIOpenDoor(this, false));
        //this.tasks.addTask(4, new EntityAIStealItemInv(this, 1.0D, 10));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
        //this.tasks.addTask(7, new EntityAIStealFarmland(this, 0.6D));

	}
    
    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata){
    		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
	        
	        GameProfile gameprofile = getRandomPlayerSkin();
	        
	        this.setOwner(gameprofile.getName());
	        
	        this.setRandomFeatures(); 
	        
	        this.setCustomNameTag(gameprofile.getName());
	        
	        return livingdata;
	}
    
	public DifficultyProgression getDifficultyProgession(){
		return this.difficultyManager;
	}
    
    public GameProfile getPlayerProfile(){
		return RaiderManager.getRaiderProfile(this.getOwner());
    }
   
    // Handle Skins
    public ResourceLocation getLocationSkin(){
		return RaidersSkinManager.DownloadPlayersSkin(this);
    }
    
    /** 
     * Set raiders skin owner
     * @param name
     */
    public void setOwner(String name){
        this.dataManager.set(SKIN_VARIANT, name);
    }
    
    /**
     * Get raiders skin owner
     * @return
     */
	public String getOwner(){
    	return this.dataManager.get(SKIN_VARIANT);
    }
	
    public void setRandomFeatures()
    {
    	setFeatures(LayerFeatures.randomFeature(this.rand).ordinal()); 
    }
    
    public void setFeatures(int ordinal)
    {
    	this.dataManager.set(EntityRaiderBase.OVERLAY_VARIANT, ordinal);
    }
    
    public LayerFeatures getFeatures()
    {
		return LayerFeatures.values()[this.dataManager.get(OVERLAY_VARIANT).intValue()]; 
    }
    
	
    public void setRaiderFaction(EnumFaction faction){
    	this.getDataManager().set(RAIDER_FACTION, faction.ordinal());
    }
    
    public EnumFaction getRaiderFaction()
    {
    	return EnumFaction.get(((Integer)this.getDataManager().get(RAIDER_FACTION)).intValue());
    }
    
    public InventoryStroageModifiable getRaidersInventory(){
        return this.raidersInventory;
    }
    
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Owner"))
            this.setOwner(compound.getString("Owner"));
        
        if(compound.hasKey("OverlayType"))
        	this.setFeatures(compound.getInteger("OverlayType"));
        
        this.raidersInventory.readFromNBT(compound);
        
        if(compound.hasKey("faction"))
        	this.setRaiderFaction(EnumFaction.get(compound.getInteger("faction")));
        
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound){
    	
        super.writeEntityToNBT(compound);
        
        compound.setString("Owner", this.getOwner());

        compound.setInteger("OverlayType", this.getFeatures().ordinal());
        
        this.raidersInventory.writeToNBT(compound);

        compound.setInteger("faction", this.getRaiderFaction().ordinal());
    }
    
    @Override
	protected SoundEvent getHurtSound() {
		return RaidersSoundEvents.RAIDERS_HURT;
	}

    @Override
	protected SoundEvent getDeathSound(){
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn){
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute(){
		return EnumCreatureAttribute.UNDEFINED;
	}
	
	/**
	 *  This gets a random GameProfile from registered raider player skins. 
	 */
	protected static GameProfile getRandomPlayerSkin(){
		GameProfile profile;
			 profile = RaiderManager.getRandomRaider().getProfile();
		return profile;
	}

	
    protected final void multiplySize(float size)
    {
        super.setSize(this.raiderWidth * size, this.raiderHeight * size);
    }
    
    protected final void multiplySize(float sizeWidth, float sizeHeight)
    {
        super.setSize(this.raiderWidth * sizeWidth, this.raiderHeight * sizeHeight);
    }
    
    /**
     * Remove all Attributes within this entity. Used to get around a client side only call.
     * 
     * @param attribute
     */
    public void removeAllModifiers(IAttribute attribute)
    {
    	for(AttributeModifier modifier : this.getEntityAttribute(attribute).getModifiers())
    		this.getEntityAttribute(attribute).removeModifier(modifier);
    }
    
    
    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
    		return true;
    	else
    		return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return (T) this.raidersInventory;
        }
        return super.getCapability(capability, facing);    
    }
}
