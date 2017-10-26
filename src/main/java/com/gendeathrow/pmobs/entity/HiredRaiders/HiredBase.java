package com.gendeathrow.pmobs.entity.HiredRaiders;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumFaction;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;

public class HiredBase extends EntityCreature{

	private NetworkPlayerInfo playerInfo;
	
	private ResourceLocation SKIN = null; 
	
	private static PlayerProfileCache profileCache;
	private static MinecraftSessionService sessionService;
	
	private static ArrayList<GameProfile> profileAskedfor;
	  
	public static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    
	private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
    private static final DataParameter<Integer> RAIDER_VARIANT = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RAIDER_ROLE = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    
	private float raiderWidth = -1.0F;
	private float raiderHeight;
    
    private String playerName;
    
	private GameProfile playerProfile;
	
	//public LayerFeatures features = LayerFeatures.NONE;
	
	protected DifficultyProgression difficultyManager;
	
    private final InventoryBasic raidersInventory;
	
	public HiredBase(World worldIn) 
	{
		super(worldIn);
		this.raidersInventory = new InventoryBasic("Items", false, 3);
		this.playerName = "Steve";
		this.setSize(0.6F, 1.95F);
	}
	
	
	/*
	#################
	  #	AI CODE
	#################
	*/
	
	private boolean isBreakDoorsTaskSet = false;
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
 
    private boolean isLeapAttackTaskSet = false;
    private final EntityAILeapAtTarget leapAttack = new EntityAILeapAtTarget(this, 0.4F);
    protected final EntityAIAttackMelee melee = new EntityAIAttackMelee(this, 0.8, false);
    protected final EntityAINearestAttackableTarget attackLivingAI = new EntityAINearestAttackableTarget(this, EntityLiving.class, true);
    
    protected final EntityAIWatchClosest watchClosestAI =  new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F);
    protected final EntityAIWander wanderAI = new EntityAIWander(this, 1.0D);
    protected final EntityAILookIdle lookIdleAI = new EntityAILookIdle(this);
    
    protected boolean initAI = false; 
    
	protected void initEntityAI()
	{
	        this.tasks.addTask(0, new EntityAISwimming(this));
	        this.tasks.addTask(4, new EntityAIOpenDoor(this, false));
	        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
	        
	        this.applyEntityAI();
	}

	protected void applyEntityAI()
	{
        	//this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
	}

	protected void applyEntityAttributes()
	{
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D);
	        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
	}
	
	protected void applyEntityAIPostInitalSpawn()
	{
        this.targetTasks.addTask(4, attackLivingAI);
        this.tasks.addTask(8, wanderAI);
        this.tasks.addTask(9, watchClosestAI);
        this.tasks.addTask(9, lookIdleAI);
		this.initAI = true;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(SKIN_VARIANT, "Steve");
        this.getDataManager().register(RAIDER_VARIANT,Integer.valueOf(0));
        this.getDataManager().register(RAIDER_ROLE, Integer.valueOf(0));
        this.getDataManager().register(RAIDER_FACTION, Integer.valueOf(0));
        this.getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
	}
	    
	public void setLeapAttack(boolean enabled)
	{
		if (this.isLeapAttackTaskSet != enabled)
		{
			this.isLeapAttackTaskSet = enabled;

			if (enabled)
			{
				this.tasks.addTask(1, this.leapAttack);
			}
			else
			{
	           this.tasks.removeTask(this.leapAttack);
			}
		}
	}
	
    public void setArmsRaised(boolean armsRaised)
    {
        this.getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
    }

    public boolean isArmsRaised()
    {
        return ((Boolean)this.getDataManager().get(ARMS_RAISED)).booleanValue();
    }
	
    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return false;
    }
    
    public void setRaiderRole(EnumRaiderRole role)
    {
    	this.getDataManager().set(RAIDER_ROLE, role.ordinal());
    }
    
    public EnumRaiderRole getRaiderRole()
    {
    	return EnumRaiderRole.get(((Integer)this.getDataManager().get(RAIDER_ROLE)).intValue());
    }
    
    public void setRaiderFaction(EnumFaction faction)
    {
    	this.getDataManager().set(RAIDER_FACTION, faction.ordinal());
    }
    
    public EnumFaction getRaiderFaction()
    {
    	return EnumFaction.get(((Integer)this.getDataManager().get(RAIDER_FACTION)).intValue());
    }
    
    @Override
    public boolean isOnSameTeam(Entity entityIn)
    {
    	if(entityIn instanceof EntityPlayer && this.getRaiderFaction() == EnumFaction.FRIENDLY)
    	{
    		return true;
    	}
    	else if(entityIn instanceof EntityRaiderBase)
    	{
    		return this.getRaiderFaction() == ((EntityRaiderBase) entityIn).getRaiderFaction();
    	}
    	else return this.isOnScoreboardTeam(entityIn.getTeam());
    }
    
    @Override
    public void onDeath(DamageSource cause)
    {
    	super.onDeath(cause);
    	
    	for(int i = 0; i < this.raidersInventory.getSizeInventory(); i++)
    	{
    		ItemStack stack = this.raidersInventory.getStackInSlot(i);
    		
    		if(stack != null)
    		{
    			EntityItem entity = new EntityItem(worldObj, this.posX, this.posY, this.posZ, stack);
    			
    			this.worldObj.spawnEntityInWorld(entity);
    		}
    	}
    }
    
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);
    }
    
    protected int getExperiencePoints(EntityPlayer player)
    {
        if (this.isChild())
        {
            this.experienceValue = (int)((float)this.experienceValue * 2.5F);
        }

        return super.getExperiencePoints(player);
    }
    
    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }
    
    protected final void multiplySize(float size)
    {
        super.setSize(this.raiderWidth * size, this.raiderHeight * size);
    }
    
    protected final void multiplySize(float sizeWidth, float sizeHeight)
    {
        super.setSize(this.raiderWidth * sizeWidth, this.raiderHeight * sizeHeight);
    }
    
    protected final void setSize(float width, float height)
    {
        boolean flag = this.raiderWidth > 0.0F && this.raiderHeight > 0.0F;
        this.raiderWidth = width;
        this.raiderHeight = height;

        if (!flag)
        {
            this.multiplySize(1.0F);
        }
    }
	
    @Override
    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }
    
    public double getYOffset()
    {
        return this.isChild() ? 0.0D : -0.35D;
    }
    
    @Override
    public int getMaxSpawnedInChunk()
    {
    	if(this.worldObj.isDaytime())
    	{
    		return 2;
    	}
        return 4;
    }
    
    @Override
    protected float getWaterSlowDown()
    {
        return .9F;
    }
    
    
    @Override
    public boolean getCanSpawnHere()
    {
    	//if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) return false;

    	return true;
    }
    
	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
		{
			float f = this.getBrightness(1.0F);
			BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);

		}
		
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		
		super.onLivingUpdate();
	}
	
    public boolean isNotColliding()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

   		
    public InventoryBasic getRaidersInventory()
    {
        return this.raidersInventory;
    }
    
    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
    	super.updateEquipmentIfNeeded(itemEntity);
    	
        if (!itemEntity.isDead && !this.worldObj.isRemote)
        {
  			ItemStack returnStack = this.getRaidersInventory().addItem(itemEntity.getEntityItem());
  			
  			if(returnStack == null) itemEntity.setDead();
  			else itemEntity.setEntityItemStack(returnStack);
  			
        }
    }

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (super.attackEntityFrom(source, amount))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean hasHitTarget = super.attackEntityAsMob(entityIn);

		if (hasHitTarget)
		{
			int i = this.worldObj.getDifficulty().getDifficultyId();

			if (this.getHeldItemMainhand() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F)
			{
				entityIn.setFire(2 * i);
			}
			
		}

		return hasHitTarget;
	}

	protected SoundEvent getAmbientSound()
	{
		return com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_SAY;
	}

	protected SoundEvent getHurtSound()
	{
		return com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_HURT;
	}

	protected SoundEvent getDeathSound() 
	{
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	protected void playStepSound(BlockPos pos, Block blockIn)
	{
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEFINED;
	}

	@Nullable
	protected ResourceLocation getLootTable()
	{
		return RaidersCore.playerraidersloot;
	}
	    
    public boolean isHeroBrine()
    {
    	return false;
    }
	
    /**
	 * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
	 * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
	 */
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
	        livingdata = super.onInitialSpawn(difficulty, livingdata);
	        
	        float f = difficulty.getClampedAdditionalDifficulty();
	        
	        this.setRaiderFaction(EnumFaction.FRIENDLY);
	        
	        this.setCanPickUpLoot(true);
	        
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);

	        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
     	
	        this.setAlwaysRenderNameTag(false);
	        
	        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
	        
	        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

	        if (d0 > 1.0D)
	        { 
	            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
	        }

	        this.setHealth(this.getMaxHealth());
	       	        
	        this.applyEntityAIPostInitalSpawn();
 
	        return livingdata;
	}
	
		public DifficultyProgression getDifficultyProgession()
		{
			return this.difficultyManager;
		}
	    
	    /**
	     * (abstract) Protected helper method to read subclass entity data from NBT.
	     */
	    @Override
	    public void readEntityFromNBT(NBTTagCompound compound)
	    {
	        super.readEntityFromNBT(compound);

	        if (compound.hasKey("Owner"))
	        {
	            this.playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
	            this.setOwner(playerProfile.getName());
	        }
	        
	        if(compound.hasKey("OverlayType"))
	        {
	        	this.setFeatures(compound.getInteger("OverlayType"));
	        }
	        
	        NBTTagList nbttaglist = compound.getTagList("Inventory", this.raidersInventory.getSizeInventory());
	        
	        for (int i = 0; i < nbttaglist.tagCount(); ++i)
	        {
	            ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));

	            if (itemstack != null)
	            {
	                this.raidersInventory.addItem(itemstack);
	            }
	        }
	        
	        if(compound.hasKey("raiderClass"))
	        {
	        	this.setRaiderRole(EnumRaiderRole.get(compound.getInteger("raiderClass")));
	        }
	        
	        if(compound.hasKey("faction"))
	        {
	        	this.setRaiderFaction(EnumFaction.get(compound.getInteger("faction")));
	        }
	        
	        //Resets AI
	        if(!this.initAI) this.applyEntityAIPostInitalSpawn();
	    }

	    /**
	     * (abstract) Protected helper method to write subclass entity data to NBT.
	     */
	    @Override
	    public void writeEntityToNBT(NBTTagCompound compound)
	    {
	        super.writeEntityToNBT(compound);
	        if (this.playerProfile != null)
	        {
	            NBTTagCompound nbttagcompound = new NBTTagCompound();
	            NBTUtil.writeGameProfile(nbttagcompound, this.playerProfile);
	            compound.setTag("Owner", nbttagcompound);
	        }
	        
	        compound.setInteger("OverlayType", this.getFeatures().ordinal());
	        
	        NBTTagList nbttaglist = new NBTTagList();

	        for (int i = 0; i < this.raidersInventory.getSizeInventory(); ++i)
	        {
	            ItemStack itemstack = this.raidersInventory.getStackInSlot(i);

	            if (itemstack != null)
	            {
	                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
	            }
	        }

	        compound.setTag("Inventory", nbttaglist);
	        
	        compound.setInteger("raiderClass", this.getRaiderRole().ordinal());
	        
	        compound.setInteger("faction", this.getRaiderFaction().ordinal());
	    }

		private GameProfile randomSkin()
		{
			Object[] profiles = RaiderManager.raidersList.values().toArray();
			return (GameProfile) profiles[rand.nextInt(profiles.length)];
		}
		
		@Deprecated		
		public static void setProfileCache(PlayerProfileCache cache)
		{
			profileCache = cache;
		}
		
		@Deprecated
		public static void setSessionService(MinecraftSessionService minecraftSessionService)
		{
			sessionService = minecraftSessionService;
		}
	    
	    public void setEntitySkin(ResourceLocation skinIn)
	    {
	    	this.SKIN = skinIn;
	    }
	    
	    // player model or new skin. 
	    public boolean isPlayerSkin()
	    {
	    	return false;
	    }
	    
	    /**
	     * 	Layer Features are not synced
	     */
	    //@SideOnly(Side.CLIENT)
	    public void setRandomFeatures()
	    {
	    	setFeatures(LayerFeatures.randomFeature(this.rand).ordinal()); 
	    }
	    
	    public void setFeatures(int ordinal)
	    {
	    	this.dataManager.set(this.RAIDER_VARIANT, ordinal);
	    }
	    
	    @SideOnly(Side.CLIENT)
	    public LayerFeatures getFeatures()
	    {
			return LayerFeatures.values()[this.dataManager.get(RAIDER_VARIANT).intValue()]; 
	    }
	    
	    /**
	     * Returns true if the player instance has an associated skin.
	     */
	    public ResourceLocation getLocationSkin()
	    {
	    	ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();

			if(this.playerProfile == null || !this.playerProfile.isComplete())
	    	{
	    		this.setPlayerProfile(this.getOwnerSkin());
	    	}

			if(RaidersSkinManager.INSTANCE.cachedSkins.containsKey(this.playerProfile.getName()))
			{
				resourcelocation = RaidersSkinManager.INSTANCE.cachedSkins.get(this.playerProfile.getName());
			}  

			return resourcelocation;
	    }  
	    
	    public GameProfile updateGameprofile(GameProfile input)
	    {
	    	
	    	return RaidersSkinManager.profileCache.getGameProfileForUsername((input.getName()));
	    }
	    
	    @Override
	    public ITextComponent getDisplayName()
	    {
	    	TextFormatting color = getRaiderFaction() == EnumFaction.FRIENDLY ? TextFormatting.GREEN : getRaiderFaction() == EnumFaction.HOSTILE ? TextFormatting.RED : TextFormatting.GRAY;
	    	
	        TextComponentString textcomponentstring = new TextComponentString(color + this.getName());
	        textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
	        textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
	        return textcomponentstring;
	    }
	    
	    public void setOwner(String name)
	    {
	        this.dataManager.set(SKIN_VARIANT, name);
	    }
	    
	    private void setPlayerProfile(String name) 
	    {
	    	this.setPlayerProfile(RaiderManager.getRaiderProfile(name));
		}

		public String getOwnerSkin()
	    {
	    	return this.dataManager.get(SKIN_VARIANT);
	    }
	    
	    public GameProfile getPlayerProfile()
	    {
    		return this.playerProfile;
	    }
	    
	    @Deprecated
	    public void setPlayerProfile(@Nullable GameProfile playerProfile)
	    {
	        this.playerProfile = playerProfile;
	    }

//	    private void updatePlayerProfile()
//	    {
//	    		RaidersSkinManager.updateProfile(this);
//	    }

	    protected boolean profileset = false;
	    
		public void setProfileUpdated(boolean b) 
		{
			profileset = b;
		}
	    
		
}