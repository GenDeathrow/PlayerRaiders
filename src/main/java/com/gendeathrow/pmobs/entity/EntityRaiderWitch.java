package com.gendeathrow.pmobs.entity;

import java.util.List;
import java.util.UUID;

import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.entity.ai.EntityAIScreamer;
import com.gendeathrow.pmobs.entity.ai.EntityAIScreamerAttack;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRaiderWitch extends EntityRaiderRangeAttacker {

	
    private static final DataParameter<Boolean> IS_DRINKINGPOTION = EntityDataManager.<Boolean>createKey(EntityRaiderWitch.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ACTIVE = EntityDataManager.<Boolean>createKey(EntityRaiderWitch.class, DataSerializers.BOOLEAN);
    
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    
    public final EntityAIAttackRanged aiPotionAttack = new EntityAIAttackRanged(this, 1.0D, 60, 10.0F);
    
    
    
    private int potionDrinkCooldown;
    private int cryTick = 30;
    private boolean hasCastInvisability = false;

    
	public EntityRaiderWitch(World worldIn) {
		super(worldIn);
	}

	protected void initEntityAI(){
		super.initEntityAI();
        this.tasks.addTask(0, new EntityAIScreamer(this));  
        this.tasks.addTask(9, new EntityAILookDepressed(this));
        this.tasks.addTask(1, new EntityAIScreamerAttack(this));
	}
	
	@Override
	public void rangedAttackTarget(EntityLivingBase target, float distanceFactor) {
		
	}

	@Override
	public void onLivingUpdate()
    {
        if (!this.world.isRemote)
        {

        	if(!isWitchActive() && cryTick-- <= 0)
            {
            	// not working correctly
         	   this.world.playRecord(this.getPosition(), RaidersSoundEvents.RAIDERS_WITCH_CRY);
         	   cryTick = 600;
            }
             
            if (this.isDrinkingPotion())
            {
                if (this.potionDrinkCooldown-- <= 0)
                {
                    this.setDrinkingPotion(false);
                    ItemStack itemstack = this.getHeldItemMainhand();
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);

                    if (!itemstack.isEmpty() && itemstack.getItem() == Items.POTIONITEM)
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
                    this.potionDrinkCooldown = this.getHeldItemMainhand().getMaxItemUseDuration();
                    this.setDrinkingPotion(true);
                    this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.rand.nextFloat() * 0.4F);
                    IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
                    iattributeinstance.removeModifier(MODIFIER);
                    iattributeinstance.applyModifier(MODIFIER);
                }
            }

            if (this.rand.nextFloat() < 7.5E-4F)
            {
                this.world.setEntityState(this, (byte)15);
            }

            
            if(this.isWitchActive())
            	this.RemoveEntitiesfromArea(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(30.0D, 10.0D, 30.0D)));
        }

        super.onLivingUpdate();
    }
	
	
	
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
    	
    	if(key == EntityRaiderWitch.IS_ACTIVE) {
    		
    		System.out.println(this.isWitchActive());
    		if(this.isWitchActive())
    			this.setWitchCombat();
    	}
    		
        super.notifyDataManagerChange(key);
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public void handleStatusUpdate(byte id)
    {
        if (id == 15)
        {
            for (int i = 0; i < this.rand.nextInt(35) + 10; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

	@Override
    protected void entityInit() {
        super.entityInit();
		this.getDataManager().register(IS_DRINKINGPOTION, Boolean.valueOf(false));
		this.getDataManager().register(IS_ACTIVE, Boolean.valueOf(false));
    }
	
	
	
    public void setWitchCombat() {
        this.targetTasks.addTask(1, attackPlayerAI);
        this.targetTasks.addTask(2, attackVillagerAI);
        this.targetTasks.addTask(3, attackLivingAI);
        this.tasks.addTask(8, wanderAI);
        this.tasks.addTask(9, watchClosestAI);

    }
    
    
    public void setDrinkingPotion(boolean aggressive)  {
        this.getDataManager().set(IS_DRINKINGPOTION, Boolean.valueOf(aggressive));
    }
    
    public void setWitchActive(boolean active) {
    	this.getDataManager().set(IS_ACTIVE, Boolean.valueOf(active));
    }

    public boolean isWitchActive() {
    	 return ((Boolean)this.getDataManager().get(IS_ACTIVE)).booleanValue();
    }
    
    public boolean isDrinkingPotion() {
        return ((Boolean)this.getDataManager().get(IS_DRINKINGPOTION)).booleanValue();
    }
	
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	if(!this.isWitchActive())
    		return false;
		return super.attackEntityFrom(source, amount);
    }
	
    @Override
    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
        damage = super.applyPotionDamageCalculations(source, damage);

        if (source.getTrueSource() == this)
        {
            damage = 0.0F;
        }

        if (source.isMagicDamage())
        {
            damage = (float)((double)damage * 0.50D);
        }

        return damage;
    }
    
    
    
    /**
     * Push Entites away from the Witch when the witch is active
     * @param p_70970_1_
     */
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
	
	/** 
	 * Used to make the witch sit and look at the ground.
	 * 
	 * @author GenDeathrow
	 */
    public class EntityAILookDepressed extends EntityAIBase {
        /** The entity that is looking idle. */
        private final EntityLiving idleEntity;
        /** X offset to look at */
        private double lookX;
        /** Z offset to look at */
        private double lookZ;
        /** A decrementing tick that stops the entity from being idle once it reaches 0. */
        private int idleTime;

        public EntityAILookDepressed(EntityLiving entitylivingIn) {
            this.idleEntity = entitylivingIn;
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute() {
            return true;//this.idleEntity.getRNG().nextFloat() < 0.02F;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting() {
            return this.idleTime >= 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.lookX = this.idleEntity.getRNG().nextInt(3) - 2;
            this.lookZ = this.idleEntity.getRNG().nextInt(3) - 2;
            this.idleTime = 100 + this.idleEntity.getRNG().nextInt(100);
        }

        /**
         * Updates the task
         */
        public void updateTask() {
            --this.idleTime;
            this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY - 2 , this.idleEntity.posZ + this.lookZ, (float)this.idleEntity.getHorizontalFaceSpeed(), (float)this.idleEntity.getVerticalFaceSpeed());
        }
    }
}
