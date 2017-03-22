package com.gendeathrow.pmobs.entity.New;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.ai.EntityAIRangedAttack;
import com.gendeathrow.pmobs.entity.ai.EntityAIScreamer;
import com.gendeathrow.pmobs.entity.ai.EntityAIScreamerAttack;
import com.gendeathrow.pmobs.handlers.EquipmentManager;

public class EntityRangedAttacker extends EntityRider implements IRangedAttackMob
{
	
	public boolean isRangedAttacker = false;
	
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityRangedAttacker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityRangedAttacker.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ACTIVE = EntityDataManager.<Boolean>createKey(EntityRangedAttacker.class, DataSerializers.BOOLEAN);
    //private static final DataParameter<Integer> SCREAMERATTACK = EntityDataManager.<BOOLEAN>createKey(EntityRangedAttacker.class, DataSerializers.BOOLEAN);
    
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    
    public final EntityAIRangedAttack aiArrowAttack = new EntityAIRangedAttack(this, 1.0D, 20, 15.0F);
    
    public final EntityAIAttackRanged aiPotionAttack = new EntityAIAttackRanged(this, 1.0D, 60, 10.0F);
    
    public final EntityAIScreamerAttack aiScreamerAttack = new EntityAIScreamerAttack(this);
    
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
    {
        /**
         * Resets the task
         */
        public void resetTask()
        {
            super.resetTask();
            EntityRangedAttacker.this.setSwingingArms(false);
        }
        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            super.startExecuting();
            EntityRangedAttacker.this.setSwingingArms(true);
        }
    };
    
    
    
    
	public EntityRangedAttacker(World worldIn) 
	{
		super(worldIn);
		
	        //this.setCombatTask();
	}
	
	
	@Override
    protected void entityInit()
    {
        super.entityInit();

		this.getDataManager().register(SWINGING_ARMS, Boolean.valueOf(false));
		this.getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
		this.getDataManager().register(IS_ACTIVE, Boolean.valueOf(false));
		
    }
	
	protected void initEntityAI()
	{
		super.initEntityAI();
		
		if(isRangedAttacker)
		{
			this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
		}
		
	}
	
    /**
     * Set whether this witch is aggressive at an entity.
     */
    public void setAggressive(boolean aggressive)
    {
        this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
    }
    
    public void setWitchActive(boolean active)
    {
    	this.getDataManager().set(IS_ACTIVE, Boolean.valueOf(active));
    }

    public boolean isWitchActive()
    {
    	 return ((Boolean)this.getDataManager().get(IS_ACTIVE)).booleanValue();
    }
    
    public boolean isDrinkingPotion()
    {
        return ((Boolean)this.getDataManager().get(IS_AGGRESSIVE)).booleanValue();
    }

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) 
	{
		
		if(this.getRaiderRole() == EnumRaiderRole.RANGED)
			rangerAttack(target, distanceFactor);
	
		//else if(this.getRaiderRole() == EnumRaiderRole.WITCH)
		///screamerAttack(target, distanceFactor);
	}
	
	private int fireballTick = 0;
	private int nxFireball = 2;
	private int lightningAttack = 0;
	private int nxLightingAttack = 15;
	private int maxStrikes = 3;
	private int strikes = 0;
	
	private void screamerAttack(EntityLivingBase target, float distanceFactor)
	{
		 if (!this.isDrinkingPotion())
	     {
			 
			 if(fireballTick++ >= nxFireball )
			 {
				 nxFireball = this.getRNG().nextInt(2)+1;
				 fireballTick = 0;
				 double d0 = this.getDistanceSqToEntity(target); 
				 double d1 = target.posX - this.posX;
				 double d2 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
				 double d3 = target.posZ - this.posZ;
				 
				 
                 float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
                 this.worldObj.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);

                 for (int i = 0; i < 1; ++i)
                 {
                     EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.worldObj, this, d1 + this.getRNG().nextGaussian() * (double)f, d2, d3 + this.getRNG().nextGaussian() * (double)f);
                     entitysmallfireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
                     this.worldObj.spawnEntityInWorld(entitysmallfireball);
                 }
                 
                 
				 //EntitySmallFireball entityfireball = new EntitySmallFireball(this.worldObj,this, d1, d2, d3);
				 //this.worldObj.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
				 //this.worldObj.spawnEntityInWorld(entityfireball);
		            
			 }
			 else if(lightningAttack++ >= nxLightingAttack)
			 {  
			       if(strikes <= maxStrikes)
			       {
			    		   this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, target.posX + getRandomPosition(), target.posY, target.posZ + getRandomPosition(), false));
			    		   strikes++;
			       }
			       else
			       {
			    	   this.nxLightingAttack = this.getRNG().nextInt(10)+15;
			    	   lightningAttack = 0;
			    	   strikes = 0;
			    	   maxStrikes = this.getRNG().nextInt(2)+1;
			       }
			 }
			 else
			 {
	            double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
	            double d1 = target.posX + target.motionX - this.posX;
	            double d2 = d0 - this.posY;
	            double d3 = target.posZ + target.motionZ - this.posZ;
	            float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
	            PotionType potiontype = PotionTypes.HARMING;

	            if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS))
	            {
	                potiontype = PotionTypes.SLOWNESS;
	            }
	            else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON))
	            {
	                potiontype = PotionTypes.POISON;
	            }
	            else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.rand.nextFloat() < 0.25F)
	            {
	                potiontype = PotionTypes.WEAKNESS;
	            }

	            EntityPotion entitypotion = new EntityPotion(this.worldObj, this, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
	            entitypotion.rotationPitch -= 0.0F;
	            entitypotion.setThrowableHeading(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
	            this.worldObj.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_THROW, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
	            this.worldObj.spawnEntityInWorld(entitypotion);
			 }
	     }
	}
	
	public int getRandomPosition()
	{
		return this.getRNG().nextInt(20) - 10;
	}
	
	private void rangerAttack(EntityLivingBase target, float p_82196_2_)
	{
	       EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.worldObj, this);
	       double d0 = target.posX - this.posX;
	       double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytippedarrow.posY;
	       double d2 = target.posZ - this.posZ;
	       double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
	       entitytippedarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
	       int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
	       int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
	       DifficultyInstance difficultyinstance = this.worldObj.getDifficultyForLocation(new BlockPos(this));
	       entitytippedarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));

	       if (i > 0)
	       {
	    	   entitytippedarrow.setDamage(entitytippedarrow.getDamage() + (double)i * 0.5D + 0.5D);
	       }

	       if (j > 0)
	       {
	    	   entitytippedarrow.setKnockbackStrength(j);
	       }

	       boolean flag = this.isBurning() && difficultyinstance.isHard() && this.rand.nextBoolean();
	       flag = flag || EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0;

	       if (flag)
	       {
	    	   entitytippedarrow.setFire(100);
	       }

	       ItemStack itemstack = this.getHeldItem(EnumHand.OFF_HAND);

	       if (itemstack != null && itemstack.getItem() == Items.TIPPED_ARROW)
	       {
	    	   entitytippedarrow.setPotionEffect(itemstack);
	       }

	       this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
	       this.worldObj.spawnEntityInWorld(entitytippedarrow);
	}
	

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        
        boolean i = false;

        if (compound.hasKey("RangedAttacker", 99))
        {
            i = compound.getBoolean("RangedAttacker");
        }
        
        if(i) this.setCombatTask();
        
        if(this.getRaiderRole().equals(EnumRaiderRole.WITCH))
        {
        	if(this.isWitchActive())
        		this.setWitchCombat();
        	else 
        		this.setWitchPreCombat();
        }
    }
    
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        
        if(this.getRaiderRole() == EnumRaiderRole.RANGED)
        	compound.setBoolean("RangedAttacker", this.isRangedAttacker);
        
        if(this.getRaiderRole() == EnumRaiderRole.WITCH)
        	compound.setBoolean("isWitchActive", this.isWitchActive());
        
    }

    private int witchAttackTimer;
    
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
    	
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        
    	      
    	if(this.getRaiderRole() == EnumRaiderRole.RANGED)
    	{
    		this.isRangedAttacker = true;
    		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    		if(this.rand.nextDouble() < .05 + (this.difficultyManager.calculateProgressionDifficulty(.05) > .25 ? .25 : this.difficultyManager.calculateProgressionDifficulty(.05))) 
    		{
    			try
    			{
    				ItemStack tippedArrow = EquipmentManager.getRandomArrows().getCopy();
    				if(tippedArrow != null)	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, tippedArrow);
    			}catch(Exception e){ RaidersCore.logger.error(e); }
    		}
            this.tasks.addTask(1, this.aiArrowAttack);
            //this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeAllModifiers();
            this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
            
    	}else if(this.getRaiderRole().equals(EnumRaiderRole.WITCH))
    	{
    		this.setWitchPreCombat();
    		//this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeAllModifiers();
    		this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
    		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
    		this.setHealth(this.getMaxHealth());
    	}
       	
  	
		return livingdata;
    }
    
    public boolean preCombatSet = false;
    private boolean postCombatSet = false;
    
    private void setWitchPreCombat()
    {
        this.tasks.addTask(0, new EntityAIScreamer(this));
        this.tasks.addTask(9, new EntityAILookDepressed(this));
        this.tasks.removeTask(attackPlayerAI);
        this.tasks.removeTask(attackVillagerAI);
        this.tasks.removeTask(attackLivingAI);
        this.tasks.removeTask(watchClosestAI);
        this.tasks.removeTask(wanderAI);
        this.tasks.removeTask(lookIdleAI);
		this.tasks.removeTask(melee);
		
		this.preCombatSet = true;
		this.setWitchActive(false);
    }
    
    public void setWitchCombat()
    {
        this.targetTasks.addTask(3, attackPlayerAI);
        this.targetTasks.addTask(4, attackVillagerAI);
        this.targetTasks.addTask(4, attackLivingAI);
        this.tasks.addTask(8, wanderAI);
        this.tasks.addTask(9, watchClosestAI);
        //this.tasks.addTask(9, lookIdleAI);
		//this.tasks.addTask(2, this.aiPotionAttack);
        this.tasks.addTask(2, this.aiScreamerAttack);
		postCombatSet = true;
		preCombatSet = false;
		this.setWitchActive(true);
    }
    
    private int cryTick = 30;
    private boolean hasCastInvisability = false;
    public void onLivingUpdate()
    {
        if (!this.worldObj.isRemote && this.getRaiderRole().equals(EnumRaiderRole.WITCH))
        {

        	if(preCombatSet && cryTick-- <= 0)
            {
            	// not working correctly
         	   //this.worldObj.playRecord(this.getPosition(), com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_WITCH_CRY);
         	   cryTick = 600;
            }
             
            if (this.isDrinkingPotion())
            {
                if (this.witchAttackTimer-- <= 0)
                {
                    this.setAggressive(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack)null);

                    if (itemstack != null && itemstack.getItem() == Items.POTIONITEM)
                    {
                        List<PotionEffect> list = PotionUtils.getEffectsFromStack(itemstack);

                        if (list != null)
                        {
                            for (PotionEffect potioneffect : list)
                            {
                                this.addPotionEffect(new PotionEffect(potioneffect));
                            }
                        }
                    }

                    this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MODIFIER);
                }
            }
            else
            {
                PotionType potiontype = null;

                if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.WATER) && !this.isPotionActive(MobEffects.WATER_BREATHING))
                {
                    potiontype = PotionTypes.WATER_BREATHING;
                }
                else if (this.rand.nextFloat() < 0.15F && (this.isBurning() || this.getLastDamageSource() != null && this.getLastDamageSource().isFireDamage()) && !this.isPotionActive(MobEffects.FIRE_RESISTANCE))
                {
                    potiontype = PotionTypes.FIRE_RESISTANCE;
                }
                else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth())
                {
                	if(this.rand.nextFloat() < 0.05)
                		potiontype = PotionTypes.REGENERATION;
                	else
                		potiontype = PotionTypes.HEALING;
                }
                else if (this.rand.nextFloat() < 0.5F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.SPEED) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D)
                {
                    potiontype = PotionTypes.SWIFTNESS;
                }
                else if(!hasCastInvisability && this.rand.nextFloat() < 0.01F && this.getAttackTarget() != null && !this.isPotionActive(MobEffects.INVISIBILITY) && this.getAttackTarget().getDistanceSqToEntity(this) < 5.0D)
                {
                	if(this.rand.nextFloat() > 0.4F)
                		potiontype = PotionTypes.INVISIBILITY;
                	hasCastInvisability = true;
                }
                
                if (potiontype != null)
                {
                	ItemStack stack = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potiontype);
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
                    this.witchAttackTimer = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setAggressive(true);
                    this.worldObj.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    iattributeinstance.removeModifier(MODIFIER);
                    iattributeinstance.applyModifier(MODIFIER);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F)
            {
                this.worldObj.setEntityState(this, (byte)15);
            }
            
            if(this.isWitchActive())
            	this.RemoveEntitiesfromArea(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(30.0D, 10.0D, 30.0D)));

        }

        super.onLivingUpdate();
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 15)
        {
            for (int i = 0; i < this.rand.nextInt(35) + 10; ++i)
            {
                this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	if(this.getRaiderRole() == EnumRaiderRole.WITCH && !this.isWitchActive())
    		return false;
    	else
    		return super.attackEntityFrom(source, amount);
     }
    
    
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
    	if(this.getRaiderRole().equals(EnumRaiderRole.WITCH))
    	{
    		
    	}
    }
    
    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
        damage = super.applyPotionDamageCalculations(source, damage);

        if (source.getEntity() == this)
        {
            damage = 0.0F;
        }

        if (source.isMagicDamage())
        {
            damage = (float)((double)damage * 0.50D);
        }

        return damage;
    }
    
    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack)
    {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.worldObj.isRemote && slotIn == EntityEquipmentSlot.MAINHAND)
        {
            this.setCombatTask();
        }
    }
    
    
    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms()
    {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }
    
    public void setSwingingArms(boolean swingingArms)
    {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }
    
    public void setCombatTask()
    {
      if (this.worldObj != null && !this.worldObj.isRemote)
        {
    	  
    	  if(this.getRaiderRole() != EnumRaiderRole.RANGED) return;
    	  
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack != null && itemstack.getItem() == Items.BOW)
            {
                int i = 20;

                if (this.worldObj.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(2, this.aiArrowAttack);
            }
            else
            {
                this.tasks.addTask(2, this.aiAttackOnCollide);
                //this.tasks.removeTask(this.melee);
            }
        }
    }
    
    
	@Override
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{
//
//		if(this.getRaiderRole() == EnumRaiderRole.WITCH)
//		{
//			this.dropItem(Items.EXPERIENCE_BOTTLE, this.getRNG().nextInt(2)+1);
//			this.dropItem(Items.GOLDEN_APPLE, 1);
//			if(this.getRNG().nextDouble() <= 0.01) this.dropItem(Items.DRAGON_BREATH, 1);
//		}

		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
	
	
	private void RemoveEntitiesfromArea(List<Entity> p_70970_1_)
	{
		double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
		double d1 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;

		for (Entity entity : p_70970_1_)
		{
			if(entity instanceof EntityPlayer) 
			{
				continue;
			}
	        	
			if (entity instanceof EntityMob)
			{
				double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 4.0D, 0.0D, d3 / d4 * 4.0D);
			}
		}
	}
    
    public class EntityAILookDepressed extends EntityAIBase
    {
        /** The entity that is looking idle. */
        private final EntityLiving idleEntity;
        /** X offset to look at */
        private double lookX;
        /** Z offset to look at */
        private double lookZ;
        /** A decrementing tick that stops the entity from being idle once it reaches 0. */
        private int idleTime;

        public EntityAILookDepressed(EntityLiving entitylivingIn)
        {
            this.idleEntity = entitylivingIn;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;//this.idleEntity.getRNG().nextFloat() < 0.02F;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            return this.idleTime >= 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.lookX = this.idleEntity.getRNG().nextInt(3) - 2;
            this.lookZ = this.idleEntity.getRNG().nextInt(3) - 2;
            this.idleTime = 100 + this.idleEntity.getRNG().nextInt(100);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            --this.idleTime;
            this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY - 2 , this.idleEntity.posZ + this.lookZ, (float)this.idleEntity.getHorizontalFaceSpeed(), (float)this.idleEntity.getVerticalFaceSpeed());
        }
    }
}
