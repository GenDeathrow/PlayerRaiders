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
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.ai.EntityAIPyromaniac;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealFarmland;
import com.gendeathrow.pmobs.entity.ai.EntityAIStealItem;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;


public class EntityRaiderBase extends EntityMob 
{

	private NetworkPlayerInfo playerInfo;
	
	private ResourceLocation SKIN = null; 
	
	private static PlayerProfileCache profileCache;
	private static MinecraftSessionService sessionService;
	
	private static ArrayList<GameProfile> profileAskedfor;
	
    protected static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    protected static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    protected static final UUID NIGHT_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    protected static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");
    
	private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> RAIDER_VARIANT = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    
	private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5D, 1);
	private static final AttributeModifier NIGHT_TIME_BOOST = new AttributeModifier(NIGHT_SPEED_BOOST_ID, "Night speed boost", 0.1D, 1);
	
	private float raiderWidth = -1.0F;
	 private float raiderHeight;
	// AI Additions
	private boolean isBreakDoorsTaskSet = false;
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
 
    private boolean isLeapAttackTaskSet = false;
    private final EntityAILeapAtTarget leapAttack = new EntityAILeapAtTarget(this, 0.4F);
   
    private String playerName;
    
	private GameProfile playerProfile;
	
	private boolean skinErrored = false;
	private long skinTimeOut = 0;
	
	public LayerFeatures features = LayerFeatures.NONE;
	
	
    private final InventoryBasic raidersInventory;
	
	public EntityRaiderBase(World worldIn) 
	{
		super(worldIn);
		
		this.raidersInventory = new InventoryBasic("Items", false, 3);
		 
		this.playerName = "Steve";
	
		this.setSize(0.6F, 1.95F);

	}
	
	protected void initEntityAI()
	{
	        this.tasks.addTask(0, new EntityAISwimming(this));
	        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false));
	        this.tasks.addTask(4, new EntityAIOpenDoor(this, false)  );
	        //this.tasks.addTask(3, new EntityAIPyromaniac(this, 0.6D));
	        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
	        this.tasks.addTask(6, new EntityAIStealItem(this, 0.6D));
	        this.tasks.addTask(7, new EntityAIStealFarmland(this, 0.6D));
	        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
	        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	        this.tasks.addTask(9, new EntityAILookIdle(this));
	        this.applyEntityAI();
	}

	protected void applyEntityAI()
	{
	        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
	        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
	        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
	        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLiving.class, true));
	}

	protected void applyEntityAttributes()
	{
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
	        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.5D);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SKIN_VARIANT, "Steve");
        this.dataManager.register(IS_CHILD, Boolean.valueOf(false));
        this.dataManager.register(RAIDER_VARIANT,Integer.valueOf(0));
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
	
    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return ((Boolean)this.getDataManager().get(IS_CHILD)).booleanValue();
    }
    
    @Override
    public boolean canAttackClass(Class entity) 
    {
    	if(entity == this.getClass()) return false;
    	
    	return true;
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
        if (IS_CHILD.equals(key))
        {
            this.setChildSize(this.isChild());
        }
        
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
    public boolean getCanSpawnHere()
    {
    	if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) return false;
    	
    	if(this.worldObj.isDaytime())
    	{
    		List<EntityRaiderBase> list = this.worldObj.getEntities(EntityRaiderBase.class,  new Predicate<EntityRaiderBase>() 
    		{
    			@Override public boolean apply(EntityRaiderBase number) 
    			{
    				return true;
    			}       
    		});
    	
    		if(list.size()+1 >= (PMSettings.daySpawnPercentage * EnumCreatureType.MONSTER.getMaxNumberOfCreature()))
    		{
    			

    			return false;
    		}
    	}
    	
    	
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
		
		if(PMSettings.sprintersOnlyNight && !this.isChild())
		{
			if(!this.worldObj.isDaytime())
			{
				if(!iattributeinstance.hasModifier(NIGHT_TIME_BOOST))
				{
					iattributeinstance.applyModifier(NIGHT_TIME_BOOST);
				}

			}
			else
			{
				if(iattributeinstance.hasModifier(NIGHT_TIME_BOOST))
				{
					iattributeinstance.removeModifier(NIGHT_TIME_BOOST);
				}
			
			}
		
//			if(iattributeinstance.getAttributeValue() > .5f) 
//			{
//				AttributeModifier SpeedOffset = new AttributeModifier(SPEED_OFFSET_ID, "Speed Offset", .5f - iattributeinstance.getAttributeValue(), 0);
//
//				iattributeinstance.applyModifier(SpeedOffset);
//			}
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
			EntityLivingBase entitylivingbase = this.getAttackTarget();

			if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
			{
				entitylivingbase = (EntityLivingBase)source.getEntity();
			}

			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY);
			int k = MathHelper.floor_double(this.posZ);

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
		super.onUpdate();
	}
	
	@Override
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{

		if(source.getEntity() != null && source.getEntity() instanceof EntityPlayer)
		{
			
			double dropit = this.rand.nextDouble();
			
			if( dropit < (lootingModifier*0.025 + .08))
			{
				ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
				
				if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());

				stack.getTagCompound().setString("SkullOwner", this.getOwner());

				EntityItem skull = new EntityItem(worldObj, this.posX, this.posY, this.posZ, stack);
				
				this.worldObj.spawnEntityInWorld(skull);
			}
			
			
		}

		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	

	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean flag = super.attackEntityAsMob(entityIn);

		if (flag)
		{
			int i = this.worldObj.getDifficulty().getDifficultyId();

			if (this.getHeldItemMainhand() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F)
			{
				entityIn.setFire(2 * i);
			}
		}

		return flag;
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
	        
	        this.setCanPickUpLoot(true);
	      
	        this.playerProfile = RaiderManager.getRandomRaider().getProfile();

	        setOwner(this.playerProfile != null ? this.playerProfile.getName() : "Steve");


	        this.setCustomNameTag(getPlayerProfile().getName());
	        
	        // Make sure they can walk though doors
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);

	        if(this.worldObj.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance && this.getOwner().equalsIgnoreCase("herobrine")) this.setChild(true); 
        	
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
	    	
	        int day = (int)(this.worldObj.getWorldTime()/24000);
	    	
	    	double healthChance = getProgressionDifficulty(.05);
	    	
	        if (this.rand.nextFloat() < f * 0.05F)
	        {
	            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader bonus", this.rand.nextDouble() * 4.0D + 1.0D, 2));
	            
	        }
	        else if(this.rand.nextFloat() < healthChance)
	        {
	        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health bonus", this.rand.nextDouble() * 2.0D + 1.0D, 2));
	        }
	        
	        this.setHealth(this.getMaxHealth());
	        

	        
	        boolean spawnAtNight = (PMSettings.sprintersOnlyNight && !this.worldObj.isDaytime() ) || !PMSettings.sprintersOnlyNight;
	        
	    	double speedChance = getProgressionDifficulty(.05);
	    	
	        if (this.rand.nextDouble() < (speedChance < .5 ? speedChance : .5) && spawnAtNight && !((EntityRangedAttacker)this).isRangedAttacker && !this.isChild())
	        {
	        	double speed = -0.01 + (.20 - (-0.01)) * rand.nextDouble();
	            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(SPEED_BOOST_ID, "Speed Bonus", speed , 0));
	           // this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Damage Speed Bonus bonus", this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() < this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() ? 2d : -1d , 0));
	        }
	        
	        if(rand.nextDouble() < .05 &&  spawnAtNight)
	        {
	        	this.setLeapAttack(true);
	        }

	        return livingdata;
	}
	    
	    @Override
	    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	    {

	    	if (this.rand.nextFloat() < (0.25F * difficulty.getClampedAdditionalDifficulty()) + getProgressionDifficulty(.035))
	        {
	            int i = this.rand.nextInt(2);
	            float f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? PMSettings.setEquptmentHard : PMSettings.setEquitmentDefault;

	            boolean armorflag = true;
	            boolean handflag = true;

	            ArmorSetWeigthedItem ArmorSet = EquipmentManager.getRandomArmor();
	            
	            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
	            {
	                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
	                {
	                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

	                    if (!armorflag && this.rand.nextFloat() > f + getProgressionDifficulty(.035))
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

	                    if (!handflag && this.rand.nextFloat() > f + getProgressionDifficulty(.035))
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
	    
	    
	    /**
	     * gets the difficulty progression count, basically if you have progression set to 5 days than every 
	     * 5 days it it multiply the value by the eachIncrease so 10 days = (2 * eachIncrease)
	     * 
	     * @param eachIncrease
	     * @return
	     */
	    protected double getProgressionDifficulty(double eachIncrease)
	    {
	        int day = (int)(this.worldObj.getWorldTime()/24000);
	    	return eachIncrease * ((int)(day / PMSettings.dayDifficultyProgression));
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
	            //System.out.println(compound.getCompoundTag("Owner"));
	            this.setOwner(playerProfile.getName());
	            
	            this.updatePlayerProfile();
	        }
	        
	        if (compound.getBoolean("IsBaby"))
	        {
	            this.setChild(true);
	        }
	        
	        if(compound.hasKey("OverlayType"))
	        {
	        	this.features = LayerFeatures.values()[compound.getInteger("OverlayType")];
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
	        
	        compound.setInteger("OverlayType", this.features.ordinal());
	        
	        
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
	    	//this.features = LayerFeatures.randomFeature(this.rand);
	    	
			this.dataManager.set(this.RAIDER_VARIANT, LayerFeatures.randomFeature(this.rand).ordinal()); 
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
//			if (playerProfile != null && playerProfile.getName() != null) 
//			{
//				Minecraft minecraft = Minecraft.getMinecraft();
//				Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(getPlayerProfile());
//				
//				System.out.println(map != null ? map.size() : "map null");
//				if (map.containsKey(Type.SKIN))
//				{
//					System.out.println("get skin");
//					return minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
//				}
//				
//			}
			
//			ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
			if(this.playerProfile == null || !this.playerProfile.isComplete())
	    	{
	    		this.setPlayerProfile(this.getOwner());
	    	}
//			
			if(RaidersSkinManager.INSTANCE.cachedSkins.containsKey(this.playerProfile.getName()))
			{
				resourcelocation = RaidersSkinManager.INSTANCE.cachedSkins.get(this.playerProfile.getName());
			}
//			
//			if(skinErrored && (Minecraft.getSystemTime() - this.skinTimeOut) < 300000) 
//			{
//				return resourcelocation;
//			}
//			else skinErrored = false;
//			
//			
//			
//			if(this.playerProfile != null && Minecraft.getMinecraft().thePlayer.getGameProfile().getName().toLowerCase().equals(this.playerProfile.getName().toLowerCase()))
//			{
//				return Minecraft.getMinecraft().thePlayer.getLocationSkin();
//			}
//			if(this.playerProfile == null || !this.playerProfile.isComplete())
//	    	{
//	    		this.setPlayerProfile(this.getOwner());
//	    		this.updatePlayerProfile();
//	    	}
//	    	
//			GameProfile gameprofile = getPlayerProfile();
//			
//			if(gameprofile != null)
//			{
//	               Map<Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(gameprofile);
//
//	               if (map.containsKey(Type.SKIN))
//	               {
//	            	     resourcelocation = Minecraft.getMinecraft().getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
//	               }
//	               else
//	               {
//	                   UUID uuid = EntityPlayer.getUUID(gameprofile);
//	                   resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
//	               }
//	 		}
//			
	    	return resourcelocation;
	    }  
	    
	    public GameProfile updateGameprofile(GameProfile input)
	    {
	    	
	    	return RaidersSkinManager.profileCache.getGameProfileForUsername((input.getName()));
	    	
//	        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
//	        {
//	            if (input.isComplete() && input.getProperties().containsKey("textures"))
//	            {
//	                return input;
//	            }
//	            else if (profileCache != null && sessionService != null)
//	            {
//	                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());
//
//	                if (gameprofile == null)
//	                {
//	                    return input;
//	                }
//	                else
//	                {
//	                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
//
//	                    if (property == null)
//	                    {
//	                    	gameprofile = sessionService.fillProfileProperties(gameprofile, true);
//	                    }
//
//	                    property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
//	                    
//	                    if(property == null)
//	                    {
//	                    	skinErrored = true;
//	                    	this.skinTimeOut = Minecraft.getSystemTime();
//	                    }
//	                    
//
//	                    return gameprofile;
//	                }
//	            }
//	            else
//	            {
//	                return input;
//	            }
//	        }
//	        else
//	        {
//	            return input;
//	        }
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
//	    		if (playerProfile == null) 
//	    		{
//	    			playerProfile = new GameProfile(null, getOwner());
//	    			RaidersSkinManager.updateProfile(this);  
//	    		}
    		return this.playerProfile;
	    }
	    
	    @Deprecated
	    public void setPlayerProfile(@Nullable GameProfile playerProfile)
	    {
	        this.playerProfile = playerProfile;
	        //this.updatePlayerProfile();
	    }

	    private void updatePlayerProfile()
	    {
	    		RaidersSkinManager.updateProfile(this);
	     //   this.playerProfile = updateGameprofile(this.playerProfile);
	    }

	    protected boolean profileset = false;
	    
		public void setProfileUpdated(boolean b) 
		{
			profileset = b;
		}
	    

}
