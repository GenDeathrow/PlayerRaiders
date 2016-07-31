package com.gendeathrow.pmobs.entity;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
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
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;

public class OLDEntityPlayerRaider extends EntityMob 
{


	private NetworkPlayerInfo playerInfo;
	
	private ResourceLocation SKIN = null; 
	
	private static PlayerProfileCache profileCache;
	private static MinecraftSessionService sessionService;
	    
	private static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(OLDEntityPlayerRaider.class, DataSerializers.STRING);
	       
	// AI Additions
	private boolean isBreakDoorsTaskSet = false;
    private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor(this);
 
    private boolean isLeapAttackTaskSet = false;
    private final EntityAILeapAtTarget leapAttack = new EntityAILeapAtTarget(this, 0.4F);
   
    private String playerName;
    
	private GameProfile playerProfile;
	
	private boolean skinErrored = false;
	private long skinTimeOut = 0;
	
	public OLDEntityPlayerRaider(World worldIn) 
	{
		super(worldIn);
		this.playerName = "Steve";
	}
	
	protected void initEntityAI()
	{
	        this.tasks.addTask(0, new EntityAISwimming(this));
	        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0, false));
	        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
	        this.tasks.addTask(4, new EntityAIOpenDoor(this, true)  );
	        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
	        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
	        this.tasks.addTask(8, new EntityAILookIdle(this));
	        this.applyEntityAI();
	}

	protected void applyEntityAI()
	{
	        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
	        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
	        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
	        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
	        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityZombie.class, true));
	}

	protected void applyEntityAttributes()
	{
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
	        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(SKIN_VARIANT, this.playerName);
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
	
	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild())
		{
			float f = this.getBrightness(1.0F);
			BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);

		}
	        
		if (this.worldObj.isRemote && this.getOwner().toLowerCase().equals("herobrine"))
		{
			for (int i = 0; i < 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
		}

		super.onLivingUpdate();
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
		return null;
	}

	protected SoundEvent getHurtSound()
	{
		return SoundEvents.ENTITY_PLAYER_HURT;
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
		return LootTableList.ENTITIES_ZOMBIE;
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
	        this.setCanPickUpLoot(true);
	        
	        this.playerProfile = RaiderManager.getRandomRaider().getProfile();
	    	
	        setOwner(this.playerProfile != null ? this.playerProfile.getName() : "Steve");
	        
	        this.setCustomNameTag(getPlayerProfile().getName());
	        
	        // Make sure they can walk though doors
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);
	        
	        this.setAlwaysRenderNameTag(false);
	        
	        // this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1F);
	        
	       // this.setTeleportFindPlayers(this.getOwner().toLowerCase().equals("herobrine"));
	        
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
	        
	        //this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));

	        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
	        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

	        if (d0 > 1.0D)
	        { 
	            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
	        }

	        if (this.rand.nextFloat() < f * 0.05F)
	        {
	            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
	        }
	        else
	        {
	        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health zombie bonus", this.rand.nextDouble() * 2.0D, 2));
	        }
	        
	        boolean spawnAtNight = (PMSettings.sprintersOnlyNight && !this.worldObj.isDaytime() ) || !PMSettings.sprintersOnlyNight;
	        
	    	int day = (int)(this.worldObj.getWorldTime()/24000);
	    	
	    	double speedChance = .05 * ((int)(day / PMSettings.dayDifficultyProgression));
	    	
	        if (this.rand.nextDouble() < (speedChance < .5 ? speedChance : .5) && spawnAtNight)
	        {
	        	double speed = -0.01 + (.23 - (-0.01)) * rand.nextDouble();
	            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("Speed Bonus bonus", speed , 0));
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
	        if (this.rand.nextFloat() < (0.25F * difficulty.getClampedAdditionalDifficulty()) + .1f)
	        {
	            int i = this.rand.nextInt(2);
	            float f = this.worldObj.getDifficulty() == EnumDifficulty.HARD ? PMSettings.setEquptmentHard : PMSettings.setEquitmentDefault;

	            if (this.rand.nextFloat() < 0.95F)
	            {
	                ++i;
	            }

	            if (this.rand.nextFloat() < 0.65F)
	            {
	                ++i;
	            }

	            if (this.rand.nextFloat() < 0.45F)
	            {
	                ++i;
	            }

	            boolean flag = true;

	            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
	            {
	                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
	                {
	                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

	                    if (!flag && this.rand.nextFloat() < f)
	                    {
	                        break;
	                    }

	                    flag = false;

	                    if (itemstack == null)
	                    {
	                        Item item = getArmorByChance(entityequipmentslot, i);

	                        if (item != null)
	                        {
	                            this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
	                        }
	                    }
	                }
	                else if(entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND)
	                {
	                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

	                    if (!flag && this.rand.nextFloat() < f)
	                    {
	                        break;
	                    }

	                    flag = false;

	                    if (itemstack == null)
	                    {
	                        Item item = getWeaponeByChance(entityequipmentslot, i);

	                        if (item != null)
	                        {
	                            this.setItemStackToSlot(entityequipmentslot, new ItemStack(item));
	                        }
	                    }
	                }
	            }
	        } 
	    }
	    
	    
	    public static Item getWeaponeByChance(EntityEquipmentSlot slotIn, int chance)
	    {
	        switch (slotIn)
	        {
	            case MAINHAND:

	                if (chance == 0)
	                {
	                    return Items.WOODEN_SWORD;
	                }
	                else if (chance == 1)
	                {
	                    return Items.STONE_SWORD;
	                }
	                else if (chance == 2)
	                {
	                    return Items.IRON_SWORD;
	                }
	                else if (chance == 3)
	                {
	                    return Items.GOLDEN_SWORD;
	                }
	                else if (chance == 4)
	                {
	                    return Items.DIAMOND_SWORD;
	                }

	            case OFFHAND:

	                if (chance == 0)
	                {
	                    return Items.SKULL;
	                }
	                else if (chance == 1)
	                {
	                    return Items.FEATHER;
	                }
	                else if (chance == 2)
	                {
	                    return Items.MAP;
	                }
	                else if (chance == 3)
	                {
	                    return Items.STICK;
	                }
	                else if (chance == 4)
	                {
	                    return Items.CLOCK;
	                }
	                
	            default:
	                return null;
	        }
	
	    }
	    
	    
	    public static Item getArmorByChance(EntityEquipmentSlot slotIn, int chance)
	    {
	        switch (slotIn)
	        {
	            case HEAD:

	                if (chance == 0)
	                {
	                    return Items.LEATHER_HELMET;
	                }
	                else if (chance == 1)
	                {
	                    return Items.GOLDEN_HELMET;
	                }
	                else if (chance == 2)
	                {
	                    return Items.CHAINMAIL_HELMET;
	                }
	                else if (chance == 3)
	                {
	                    return Items.IRON_HELMET;
	                }
	                else if (chance == 4)
	                {
	                    return Items.DIAMOND_HELMET;
	                }

	            case CHEST:

	                if (chance == 0)
	                {
	                    return Items.LEATHER_CHESTPLATE;
	                }
	                else if (chance == 1)
	                {
	                    return Items.GOLDEN_CHESTPLATE;
	                }
	                else if (chance == 2)
	                {
	                    return Items.CHAINMAIL_CHESTPLATE;
	                }
	                else if (chance == 3)
	                {
	                    return Items.IRON_CHESTPLATE;
	                }
	                else if (chance == 4)
	                {
	                    return Items.DIAMOND_CHESTPLATE;
	                }

	            case LEGS:

	                if (chance == 0)
	                {
	                    return Items.LEATHER_LEGGINGS;
	                }
	                else if (chance == 1)
	                {
	                    return Items.GOLDEN_LEGGINGS;
	                }
	                else if (chance == 2)
	                {
	                    return Items.CHAINMAIL_LEGGINGS;
	                }
	                else if (chance == 3)
	                {
	                    return Items.IRON_LEGGINGS;
	                }
	                else if (chance == 4)
	                {
	                    return Items.DIAMOND_LEGGINGS;
	                }

	            case FEET:

	                if (chance == 0)
	                {
	                    return Items.LEATHER_BOOTS;
	                }
	                else if (chance == 1)
	                {
	                    return Items.GOLDEN_BOOTS;
	                }
	                else if (chance == 2)
	                {
	                    return Items.CHAINMAIL_BOOTS;
	                }
	                else if (chance == 3)
	                {
	                    return Items.IRON_BOOTS;
	                }
	                else if (chance == 4)
	                {
	                    return Items.DIAMOND_BOOTS;
	                }

	            default:
	                return null;
	        }
	    }


	    public GameProfile updateGameprofile(GameProfile input)
	    {
	        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
	        {
	            if (input.isComplete() && input.getProperties().containsKey("textures"))
	            {
	            	//system.out.println("gameprofile: "+input.getName()+" | already setup");
	                return input;
	            }
	            else if (profileCache != null && sessionService != null)
	            {
	                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());

	            	//system.out.println("gameprofile: "+input.getName()+" | Setting up ---> "+ gameprofile == null ? "-not found-" : gameprofile.getName());
	            	
	            	
	                if (gameprofile == null)
	                {
	                    return input;
	                }
	                else
	                {
	                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);

	                    if (property == null)
	                    {
	                    	gameprofile = sessionService.fillProfileProperties(gameprofile, true);
	                    }

	                    property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), null);
	                    
	                    if(property == null)
	                    {
	                    	skinErrored = true;
	                    	this.skinTimeOut = Minecraft.getSystemTime();
	                    }
	                    

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
	    
	    
	    public void setPlayerProfile(@Nullable GameProfile playerProfile)
	    {
	        this.playerProfile = playerProfile;
	        this.updatePlayerProfile();
	    }

	    private void updatePlayerProfile()
	    {
	        this.playerProfile = updateGameprofile(this.playerProfile);
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
	           
	    }

		private GameProfile randomSkin()
		{
			Object[] profiles = RaiderManager.raidersList.values().toArray();

			return (GameProfile) profiles[rand.nextInt(profiles.length)];
		}
		
		public static void setProfileCache(PlayerProfileCache cache)
		{
			profileCache = cache;
		}
		
		public static void setSessionService(MinecraftSessionService minecraftSessionService)
		{
			sessionService = minecraftSessionService;
		}
		
	    /**
	     * Returns true if the player instance has an associated skin.
	     */
	    public ResourceLocation getLocationSkin()
	    {

			ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
			
			if(skinErrored && (Minecraft.getSystemTime() - this.skinTimeOut) < 300000) 
			{
				return resourcelocation;
			}
			else skinErrored = false;
			
			if(this.playerProfile != null && Minecraft.getMinecraft().thePlayer.getGameProfile().getName().toLowerCase().equals(this.playerProfile.getName().toLowerCase()))
			{
				return Minecraft.getMinecraft().thePlayer.getLocationSkin();
			}
			if(this.playerProfile == null || !this.playerProfile.isComplete())
	    	{
	    		this.setPlayerProfile(this.getOwner());
	    		this.updatePlayerProfile();
	    	}
	    	
			GameProfile gameprofile = getPlayerProfile();
			
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
	    
	    public void setEntitySkin(ResourceLocation skinIn)
	    {
	    	this.SKIN = skinIn;
	    }
	    
	    // player model or new skin. 
	    public boolean isPlayerSkin()
	    {
	    	return false;
	    }

}
