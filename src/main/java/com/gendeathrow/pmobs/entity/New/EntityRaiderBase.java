package com.gendeathrow.pmobs.entity.New;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
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
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.ConfigHandler.ItemDrop;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.core.init.ModItems;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.entity.ai.EntityAIPyromaniac;
import com.gendeathrow.pmobs.entity.ai.EntityAIShootLaser;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealFarmland;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealItemInv;
import com.gendeathrow.pmobs.entity.ai.TwitchersAttack;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.gendeathrow.pmobs.world.Raiders_WorldData;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;


public class EntityRaiderBase extends EntityMob
{

	private NetworkPlayerInfo playerInfo;
	
	private ResourceLocation SKIN = null; 
	
	private static PlayerProfileCache profileCache;
	private static MinecraftSessionService sessionService;
	
	private static ArrayList<GameProfile> profileAskedfor;
	
	  
	public static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    public static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    public static final UUID DAY_SPEED_MODIFIER_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    public static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");
    
	private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAIDER_VARIANT = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RAIDER_ROLE = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    
	private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.45D, 1);
	private static final AttributeModifier DAY_SPEED_MODIFIER = new AttributeModifier(DAY_SPEED_MODIFIER_ID, "Day speed reduced", PMSettings.daySpeedRestiction - 1, 2);
	
	private float raiderWidth = -1.0F;
	private float raiderHeight;
    
    private String playerName;
    
	protected GameProfile playerProfile;
	
	private boolean skinErrored = false;
	private long skinTimeOut = 0;
	
	public boolean willSteal = false;
	
	//public LayerFeatures features = LayerFeatures.NONE;
	
	protected DifficultyProgression difficultyManager;
	
    private final InventoryBasic raidersInventory;
	
	public EntityRaiderBase(World worldIn) 
	{
		super(worldIn);
		
		this.raidersInventory = new InventoryBasic("Items", false, 3);
		 
		this.playerName = "Steve";
	
		this.setSize(0.6F, 1.95F);
		
		difficultyManager = new DifficultyProgression(this);

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

    private boolean isPyroTaskSet = false;
    private final EntityAIPyromaniac pyromaniac = new EntityAIPyromaniac(this, 2D);

    private boolean isTweaker = false;
    protected final EntityAIAttackMelee melee = new EntityAIAttackMelee(this, 0.8, false);
    private final TwitchersAttack meleeTweaker = new TwitchersAttack(this, 1, false);
	
    protected final EntityAINearestAttackableTarget attackPlayerAI = new EntityAINearestAttackableTarget(this, EntityPlayer.class, true);
    protected final EntityAINearestAttackableTarget attackVillagerAI =  new EntityAINearestAttackableTarget(this, EntityVillager.class, false);
    protected final EntityAINearestAttackableTarget attackLivingAI = new EntityAINearestAttackableTarget(this, EntityLiving.class, true);
    
    protected final EntityAIWatchClosest watchClosestAI =  new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F);
    protected final EntityAIWander wanderAI = new EntityAIWander(this, 1.0D);
    protected final EntityAILookIdle lookIdleAI = new EntityAILookIdle(this);
    
    protected boolean initAI = false; 
    
	protected void initEntityAI()
	{
	        this.tasks.addTask(0, new EntityAISwimming(this));
	        this.tasks.addTask(2, new EntityAIShootLaser(this));
	        this.tasks.addTask(4, new EntityAIOpenDoor(this, false));
	        this.tasks.addTask(4, new EntityAIStealItemInv(this, 1.0D, 10));
	        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
	        this.tasks.addTask(7, new EntityAIStealFarmland(this, 0.6D));

	        
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
	        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
	}
	
	protected void applyEntityAIPostInitalSpawn()
	{
        this.targetTasks.addTask(3, attackPlayerAI);
        this.targetTasks.addTask(4, attackVillagerAI);
		this.targetTasks.addTask(4, attackLivingAI);
		
		
        this.tasks.addTask(8, wanderAI);
        this.tasks.addTask(9, watchClosestAI);
        this.tasks.addTask(9, lookIdleAI);
        
		if(!this.getRaiderRole().equals(EnumRaiderRole.TWEAKER) && !this.getRaiderRole().equals(EnumRaiderRole.WITCH) )
			this.tasks.addTask(2, this.meleeTweaker);
		
		if(this.getRaiderRole() == EnumRaiderRole.PYROMANIAC) 
			this.setPyromaniac(true);
		
		
		this.initAI = true;
	}

	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(SKIN_VARIANT, "Steve");
		this.getDataManager().register(IS_CHILD, Boolean.valueOf(false));
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
	
	
	public void setPyromaniac(boolean enabled)
	{
		if (this.isPyroTaskSet != enabled)
		{
			this.isPyroTaskSet = enabled;

			if (enabled)
			{
				this.tasks.addTask(1, this.pyromaniac);
				
			}
			else
			{
	           this.tasks.removeTask(this.pyromaniac);
			}
		}
	}
	
	public void setTweakerMelee(boolean enabled)
	{
		
		if (this.isTweaker != enabled)
		{
			this.isTweaker = enabled;

			if (enabled)
			{
				this.tasks.addTask(2, this.meleeTweaker);
		        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
			}
			else
			{
				this.tasks.removeTask(this.meleeTweaker);
		        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
			}
		}
		
	}
	
	
    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
    	if(this.getRaiderRole() == EnumRaiderRole.PYROMANIAC && (source == DamageSource.inFire|| source == DamageSource.onFire)) return true;
    	
    	return super.isEntityInvulnerable(source);
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
        return ((Boolean)this.getDataManager().get(IS_CHILD)).booleanValue();
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
    public boolean canAttackClass(Class entity) 
    {
    	
    	if(EntityPlayer.class.isAssignableFrom(entity))
    		return true;
    	else if(PMSettings.veryHostile && EntityMob.class.isAssignableFrom(entity))
    		return true;
    	else if(this.getClass().isAssignableFrom(entity))
    		return true;
    	else if(EntityMob.class.isAssignableFrom(entity))
    		return false;
    	else if (EntityLiving.class.isAssignableFrom(entity))
    		return true;
    	
    	return false;
    }
    
    public void setChild(boolean childZombie)
    {
        this.getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));

        if (this.worldObj != null && !this.worldObj.isRemote)
        {
        	
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);
            iattributeinstance.removeModifier(SPEED_BOOST_ID);

            if (childZombie)
            {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
            
        }

        this.setChildSize(childZombie);
    }
    
    public void setBrute(boolean isBrute)
    {

    	this.setBruteSize(true);
    }
    
    @Override
    public void onDeath(DamageSource cause)
    {
    	//if(this.wasridding) System.out.println("was ridding.. but died");
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
        if (IS_CHILD.equals(key))
        {
            this.setChildSize(this.isChild());
        }
        else if(RAIDER_ROLE.equals(key))
        {
        	if(EnumRaiderRole.BRUTE == this.getRaiderRole())
        	{
        		setBruteSize(true);
        	}
        }

        super.notifyDataManagerChange(key);
    }
    
    protected int getExperiencePoints(EntityPlayer player)
    {
        if (this.isChild())
        {
            this.experienceValue = (int)((float)this.experienceValue * 0.5F);
        }
        
        if(this.getDifficultyProgession().getRaidDifficulty() > 0)
        {
        	this.experienceValue = this.experienceValue + this.getRNG().nextInt(this.getDifficultyProgession().getRaidDifficulty());
        }

        return super.getExperiencePoints(player);
    }
    
    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }
    
    public void setBruteSize(boolean isBrute)
    {
    	this.multiplySize(isBrute ? 1.5F : 1.0F);
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
//    	if(this.worldObj.isDaytime())
//    	{
//    		return 2;
//    	}
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
    	if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) return false;
    	
    	
    	return this.isValidLightLevel();
    	
//     	if(PMSettings.anyLightLvlSpawning) 
//    	{
//    		
//     		if(PMSettings.torchStopSpawns)
//    		{
//    			if(!this.worldObj.canSeeSky(this.getPosition()))
//    			{
//					boolean flag = false;
//					
//    				for(MutableBlockPos blockpos : this.getPosition().getAllInBoxMutable(this.getPosition().add(3, 3, 3), this.getPosition().add(-3, -3, -3)))
//    				{
//    					Block block = this.worldObj.getBlockState(blockpos).getBlock();
//
//    					if(block instanceof BlockTorch || block instanceof BlockGlowstone)
//    					{
//    						flag = true;
//    						//System.out.println("Found torch/glowstone");
//    					}
//    				}
//    				
//					if(flag && this.worldObj.getLightBrightness(this.getPosition()) < 8)
//					{
//						//System.out.println("light level too low");
//						return true;
//					}else return false;
//    			}
//    			else 
//    			{
//    				//System.out.println("can see sky");
//    				return true;
//    			}
//    		}
//    		else 
//    		{
//    			//System.out.println("can spawn any light level");
//    			return true;
//    		}
//
//    	}else
//    	{
//    		//System.out.println("normal vanilla");
//    		return this.isValidLightLevel();
//    	}
//
//
//    	//return super.getCanSpawnHere();
    }
    
    @Override
    protected boolean isValidLightLevel() 
    {
     	if(PMSettings.shouldDaylightSpawm) 
    	{
     		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
     		if (this.worldObj.getLightFor(EnumSkyBlock.BLOCK, blockpos) >= 8)
     		{
     			return false;
     		}
     		return true;
    	}
     	else
     		return super.isValidLightLevel();
    }
    
	@Override
    public boolean isNotColliding()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

	@Override
    protected boolean canDespawn()
    {
    	if(this.getRidingEntity() instanceof EntityDropPod) return false;
        return super.canDespawn();
    }  
    
    private int ScreamTick = 1200;
	@Override
	public void onLivingUpdate()
	{

		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
		{
			float f = this.getBrightness(1.0F);
			BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
		}
		
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		
		if(!this.isChild()&& this.getRaiderFaction() != EnumFaction.FRIENDLY && this.getRaiderRole() != EnumRaiderRole.WITCH && !this.isHeroBrine() && this.getRaiderRole() != EnumRaiderRole.BRUTE && this.getRaiderRole() != EnumRaiderRole.DROPPERS)
		{
			if(this.worldObj.isDaytime())
			{
				if(!iattributeinstance.hasModifier(DAY_SPEED_MODIFIER))
				{
					iattributeinstance.applyModifier(DAY_SPEED_MODIFIER);
				}

			}
			else
			{
				if(iattributeinstance.hasModifier(DAY_SPEED_MODIFIER))
				{
					iattributeinstance.removeModifier(DAY_SPEED_MODIFIER);
				}
			
			}

		}
		
		if(!this.worldObj.isRemote && this.getRaiderRole() == EnumRaiderRole.TWEAKER)
		{

			if(this.getAttackTarget() != null)
			{
				if(ScreamTick++ > 200 && this.canEntityBeSeen(this.getAttackTarget()))
				{
					this.worldObj.playSound(null, this.getPosition(), com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_SCREAM, SoundCategory.HOSTILE, 2.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
					this.ScreamTick = this.getRNG().nextInt(100);
				}
				if(!this.isSprinting()) this.setSprinting(true);
				if(!this.isArmsRaised()) this.setArmsRaised(true);				
				
			}else if(this.getAttackTarget() == null && this.isArmsRaised())
			{
				this.setArmsRaised(false);
				if(this.isSprinting()) this.setSprinting(false);
			}
		}
		super.onLivingUpdate();
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
			EntityLivingBase entitylivingbase = null;
			
			if (source.getEntity() instanceof EntityLivingBase)
			{
				entitylivingbase = (EntityLivingBase)source.getEntity();
			}

			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY);
			int k = MathHelper.floor_double(this.posZ);
			
			if(entitylivingbase != null && this.getRNG().nextInt(2) == 0)
			{
				this.setAttackTarget((EntityLivingBase)entitylivingbase);
			}

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
	public void onUpdate()
	{
		lastBurnTick++;
		super.onUpdate();
	}
	
	private int lastBurnTick = 0;
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
			
			if(lastBurnTick > 600 && this.getHeldItemOffhand() != null && this.getHeldItemOffhand().getItem() == Items.FLINT_AND_STEEL && this.rand.nextFloat() < (float)i * 0.3F )
			{
				this.swingArm(EnumHand.OFF_HAND);
    			this.worldObj.playSound(null, entityIn.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
    			this.worldObj.playSound(null, this.getPosition(), com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_LAUGH, SoundCategory.HOSTILE, 1.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
				entityIn.setFire(2 * i);
				lastBurnTick = 0;
			}
			
			if(this.getRaiderRole() == EnumRaiderRole.BRUTE && entityIn instanceof EntityLivingBase)
			{
				((EntityLivingBase)entityIn).knockBack(entityIn, 1, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
			}
		}

		return hasHitTarget;
	}

	@Override
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{
		if(source.getEntity() != null && source.getEntity() instanceof EntityPlayer)
		{
			double dropit = this.rand.nextDouble();
			
			if( dropit < (.025)) //lootingModifier*0.025 + 
			{
				ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
				
				if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());

				stack.getTagCompound().setString("SkullOwner", this.getOwner());

				EntityItem skull = new EntityItem(worldObj, this.posX, this.posY, this.posZ, stack);
				
				this.worldObj.spawnEntityInWorld(skull);
			}
			
			if(PMSettings.dropSerum && this.getRaiderRole() == EnumRaiderRole.BRUTE && this.getRNG().nextDouble() <= 0.10)
			{
				this.entityDropItem(new ItemStack(ModItems.bruteSerumSample), 0.0f);
			}
			
			boolean flag = false;

			if(this.getRaiderRole() == EnumRaiderRole.NONE  && this.getRNG().nextDouble() <= 0.02)
				flag = true;
			else if(this.getRaiderRole() == EnumRaiderRole.WITCH && this.getRNG().nextDouble() <= 0.35)
				flag = true;

			
			if(flag && PMSettings.dropTransmitter)
				this.entityDropItem(new ItemStack(ModItems.satTransmitterPart), 0.0f);
			
			
			this.getRaiderRole().dropLoot(this);
		}
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	

	protected SoundEvent getAmbientSound()
	{
		if(this.getRaiderRole() == EnumRaiderRole.PYROMANIAC && this.getRNG().nextDouble() < .45) return com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_LAUGH;
		else if(this.getRaiderRole() == EnumRaiderRole.BRUTE && this.getRNG().nextDouble() < .45) return com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_BRUTE_LAUGH;
		else return com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_SAY;
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
	        
	        this.setRandomFeatures(); 
	        
	        this.setRaiderFaction(EnumFaction.getRandomFaction(this, this.difficultyManager));
	        
	        this.setCanPickUpLoot(true);
	      
	        this.playerProfile = randomSkin();//RaiderManager.getRandomRaider().getProfile();

	        setOwner(this.playerProfile != null ? this.playerProfile.getName() : "Steve");

	        this.setCustomNameTag(getPlayerProfile().getName());
	        
	        // Make sure they can walk though doors
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);

	        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
     	
	        this.setAlwaysRenderNameTag(false);
	        
	        this.setEquipmentBasedOnDifficulty(difficulty);
	        
	        this.setEnchantmentBasedOnDifficulty(difficulty);

	        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
	        {
	            Calendar calendar = this.worldObj.getCurrentDate();

	            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
	            {
	                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
	                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
	            }
	        }
	        
	        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
	        
	        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

	        if (d0 > 1.0D)
	        { 
	            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
	        }

	        
	        if(!this.isHeroBrine() && !this.isChild())
	        {
	        	this.setRaiderRole(EnumRaiderRole.getRandomRole(this, this.difficultyManager));
	        	
	        	if(PMSettings.tweakerOnlyNight && this.worldObj.isDaytime() && this.getRaiderRole() == EnumRaiderRole.TWEAKER)
	        	{
	        		this.setRaiderRole(EnumRaiderRole.NONE);
	        	}
	        	
	        	if(this.getRaiderRole().equals(EnumRaiderRole.TWEAKER))
	        	{
	        		this.setTweakerMelee(true);
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Tweaker Health", -.5, 2));
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.BRUTE))
	        	{
		        	this.setBrute(true);
	        		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(.7);
	        		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.15);
	        		this.stepHeight = 2F;
	        		//Apparetnly this is side only
	        		//this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeAllModifiers();
	        		this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
		        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Brute Health", 1.25, 2));
		        	if(this.getHeldItem(EnumHand.OFF_HAND) != null) this.setHeldItem(EnumHand.OFF_HAND, null);
		        	if(this.getHeldItem(EnumHand.MAIN_HAND) == null || this.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.IRON_SWORD) this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));

	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.PYROMANIAC))
	        	{
	        		this.setPyromaniac(true);
	        		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.WITCH))
	        	{
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15);
	        		this.setRaiderFaction(EnumFaction.HOSTILE);
	        		
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.RANGED))
	        	{
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);
	        	}
	        }
	        

	        if(this.getRaiderRole() == EnumRaiderRole.NONE && this.worldObj.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance && !this.getOwner().equalsIgnoreCase("herobrine") && !((EntityRangedAttacker)this).isRangedAttacker) 
	        {
	        	this.setChild(true); 
	        	//this.setFeatures(0);
	        }
  
	        
	        if(!this.isChild() && (this.getRaiderRole() == EnumRaiderRole.NONE || this.getRaiderRole() == EnumRaiderRole.PYROMANIAC) && PMSettings.leapAttackAI && rand.nextDouble() < .15 + difficultyManager.calculateProgressionDifficulty(.05, .35))
	        {
	        	this.setLeapAttack(true);
	        }
	        
	        difficultyManager.setHealthDifficulty(difficulty);
	        
	        difficultyManager.setDamageDifficulty(difficulty);
	        
	        if(!this.isChild() && !this.isHeroBrine())
	        {
	        	difficultyManager.setSpeedDifficulty(difficulty);
	        }
	        
	        this.setHealth(this.getMaxHealth());
	       	        
	        this.applyEntityAIPostInitalSpawn();
 
	        return livingdata;
	}
	
		public DifficultyProgression getDifficultyProgession()
		{
			return this.difficultyManager;
		}
	    
	    @Override
	    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	    {


	    	if (this.rand.nextFloat() < (0.25F * difficulty.getClampedAdditionalDifficulty()) + this.difficultyManager.calculateProgressionDifficulty(.05))
	        {
		    	//System.out.println((0.25F * difficulty.getClampedAdditionalDifficulty()) + this.difficultyManager.calculateProgressionDifficulty(.05));
		    	
	            int i = this.rand.nextInt(2);
	            float f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? PMSettings.setEquptmentHard : PMSettings.setEquitmentDefault;

	            boolean armorflag = true;
	            boolean handflag = true;

	            ArmorSetWeigthedItem ArmorSet = EquipmentManager.getRandomArmor();
	            
	            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
	            {
	            	//System.out.print(entityequipmentslot.getName());
	                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
	                {
	                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

	                    if (!armorflag && this.rand.nextFloat() > f + this.difficultyManager.calculateProgressionDifficulty(.05))
	                    {
	                        break;
	                    }

	                    armorflag = false;

	                    if (itemstack == null & ArmorSet != null)
	                    {
	                   		ItemStack stack = ArmorSet.getArmorbyEquipmentSlot(entityequipmentslot);
	                   		
	                        if (stack != null && stack.getItem() != null)
	                        {
	                        	this.setItemStackToSlot(entityequipmentslot, stack.copy());
	                        }
	                    }
	                }
	                else if(entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND)
	                {
	                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

	                    if (!handflag && this.rand.nextFloat() > f + this.difficultyManager.calculateProgressionDifficulty(.05))
	                    {
	                        break;
	                    }

	                    handflag = false;

	                    if (itemstack == null)
	                    {
	                    	ItemStack stack = null;
	                    	
	                    	if(entityequipmentslot == EntityEquipmentSlot.MAINHAND)
	                    	{
	                    		stack = EquipmentManager.getRandomWeapons().getCopy();
	                    	}
	                    	else if(entityequipmentslot == EntityEquipmentSlot.OFFHAND)
	                    	{
	                    		stack = EquipmentManager.getRandomOffHand().getCopy();
	                    	}
	                        if (stack != null && stack.getItem() != null)
	                        {
	                            this.setItemStackToSlot(entityequipmentslot, stack);
	                        }
	                    }
	                }
	                
	            }
	        } 
	    }
	    
	    
	    public void removeAllModifiers(IAttribute attribute)
	    {
	    	
	    	for(AttributeModifier modifier : this.getEntityAttribute(attribute).getModifiers())
	    	{
	    		this.getEntityAttribute(attribute).removeModifier(modifier);
	    	}
	    	
	    	
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
	        
	        if (compound.getBoolean("IsBaby"))
	        {
	            this.setChild(true);
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
	        	
	        	if(this.getRaiderRole() == EnumRaiderRole.BRUTE)
	        		this.stepHeight = 2F;
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
	        
	        if (this.isChild())
	        {
	            compound.setBoolean("IsBaby", true);
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

	    private static int lastHerobrineSpawn = 0;
	    
		protected GameProfile randomSkin()
		{
			GameProfile profile;
			
			int herobineLastCheck = Raiders_WorldData.INSTANCE != null ? Raiders_WorldData.INSTANCE.getLastHerobrineSighting() : 0;
			
			

			if(herobineLastCheck == this.getDifficultyProgession().getDay() || this.getDifficultyProgession().getDay() < 3)
			{
				
				do{
					 profile = RaiderManager.getRandomRaider().getProfile();
				}while(profile.getName().equalsIgnoreCase("Herobrine"));
			}
			else
			{
				profile = RaiderManager.getRandomRaider().getProfile();
			}
			
			if(profile == RaiderManager.getRaiderProfile("herobrine")) 
				Raiders_WorldData.INSTANCE.setLastHerobrineSighting(this.getDifficultyProgession().getDay());

			return profile;
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
	    		this.setPlayerProfile(this.getOwner());
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

		public String getOwner()
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

	    private void updatePlayerProfile()
	    {
	    		RaidersSkinManager.updateProfile(this);
	    }

	    protected boolean profileset = false;
	    
		public void setProfileUpdated(boolean b) 
		{
			profileset = b;
		}
	    
		
		public enum EnumRaiderRole
		{
			//weight , startDifficulty
			NONE(PMSettings.noneWeight,0, PMSettings.noneDrops),
			PYROMANIAC(PMSettings.pyroWeight, PMSettings.pyroStartDiff, PMSettings.pyroDrops),
			TWEAKER(PMSettings.tweakerWeight, PMSettings.tweakerStartDiff, PMSettings.tweakerDrops),
			BRUTE(PMSettings.bruteWeight, PMSettings.bruteStartDiff, PMSettings.bruteDrops),
			WITCH(PMSettings.screamerWeight,PMSettings.screamerStartDiff, PMSettings.screamerDrops),
			RANGED(PMSettings.rangerWeight,PMSettings.rangerStartDiff, PMSettings.rangerDrops),
			DROPPERS(0, 0, new ArrayList<ItemDrop>(), false);
			
			//Troll mob
			//Summoner
			//illusionist
			//technomancer
			//scouts
			//puppeter
			

			private int weight;
			private int maxWeight;
			private int startDifficulty;
			private ArrayList<ItemDrop> drops;
			private boolean worldSpawn = true;
			
			private final static List<WeightedRoles> weightedRoles = Lists.newArrayList();
			private static int lastRefresh = 0;
			
			public static EnumRaiderRole get(int intValue) 
			{
				for(EnumRaiderRole role : EnumRaiderRole.values())
				{
					if (role.ordinal() == intValue) return role;
				}
				return null;
			}

			EnumRaiderRole(int weight, int startDifficulty, ArrayList<ItemDrop> drops)
			{
				this.weight = weight;
				this.startDifficulty = startDifficulty;
				this.drops = drops;
			}
			
			EnumRaiderRole(int weight, int startDifficulty, ArrayList<ItemDrop> drops, boolean shouldSpawn)
			{
				this(weight, startDifficulty, drops);
				worldSpawn = shouldSpawn;
			}
			

			public boolean isEnabled()
			{
				if(this == PYROMANIAC) return PMSettings.pyroClass;
				else if(this == BRUTE) return PMSettings.bruteClass;
				else if(this == TWEAKER) return PMSettings.tweakersClass;
				else if(this == WITCH) return PMSettings.screamerClass;
				else if(this == RANGED) return PMSettings.rangerClass;
				else return true;
			}
			
			public void dropLoot(EntityRaiderBase raider)
			{
				if(this.drops == null) return;
				
				for(ItemDrop drop : this.drops)
				{
					if(drop.shouldDrop(raider.getRNG()))
					{
						raider.entityDropItem(drop.getStack(raider.getRNG()), 0.0f);
					}
				}

			}
			
			
			public static EnumRaiderRole getRandomRole(EntityRaiderBase raider, DifficultyProgression manager)
			{
				if(raider.getDifficultyProgession().getDay() != lastRefresh) 
				{
					weightedRoles.clear();
				}

				if(weightedRoles.size() <= 0)
				{
					for(EnumRaiderRole value : EnumRaiderRole.values())
					{
						if(value.isEnabled() && value.worldSpawn && raider.getDifficultyProgession().getRaidDifficulty() >= value.startDifficulty) 
						{
							int extraWeight = value.weight + manager.getRaidDifficulty();
							int weight = value != NONE ? extraWeight > (value.weight * 2) ? value.weight * 2 : extraWeight : value.weight;
							
							weightedRoles.add(new WeightedRoles(value, weight));
						}
					}
					lastRefresh = raider.getDifficultyProgession().getDay();
				}
				
				return WeightedRandom.getRandomItem(raider.getRNG(), weightedRoles).GetRole();
			}
		}

		
		public static class WeightedRoles extends WeightedRandom.Item
		{

			private EnumRaiderRole role;
			
			public WeightedRoles(EnumRaiderRole role, int itemWeightIn) 
			{
				super(itemWeightIn);
				this.role = role;
			}
			
			public EnumRaiderRole GetRole() { return role; }
			
		}
		
		//Factions
		public enum EnumFaction
		{
			HOSTILE,
			NEUTRAL,
			FRIENDLY;
			
			EnumFaction() { 	}
			
			public static EnumFaction get(int intValue) 
			{
				for(EnumFaction role : EnumFaction.values())
				{
					if (role.ordinal() == intValue) return role;
				}
				return null;
			}

			public static EnumFaction getRandomFaction(EntityRaiderBase raider, DifficultyProgression manager)
			{
				double randval = raider.getRNG().nextDouble();
					
				if(randval < .9 || !PMSettings.factionsEnabled)
				{
					return HOSTILE;
				}
				else
				{
					return FRIENDLY;
				}
			}
		}
}
