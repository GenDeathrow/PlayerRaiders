package com.gendeathrow.pmobs.entity;

import java.util.UUID;

import com.gendeathrow.pmobs.entity.ai.EntityAIScreamerAttack;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityRaiderWitch extends EntityRaiderRangeAttacker {

	
    private static final DataParameter<Boolean> IS_AGGRESSIVE = EntityDataManager.<Boolean>createKey(EntityRaiderWitch.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_ACTIVE = EntityDataManager.<Boolean>createKey(EntityRaiderWitch.class, DataSerializers.BOOLEAN);
    
    private static final UUID MODIFIER_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier MODIFIER = (new AttributeModifier(MODIFIER_UUID, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
    
    public final EntityAIAttackRanged aiPotionAttack = new EntityAIAttackRanged(this, 1.0D, 60, 10.0F);
    
    public final EntityAIScreamerAttack aiScreamerAttack = new EntityAIScreamerAttack(this);
    
	public EntityRaiderWitch(World worldIn) {
		super(worldIn);
	}

	@Override
	public void rangedAttackTarget(EntityLivingBase target, float distanceFactor) {
		
	}

	@Override
    protected void entityInit() {
        super.entityInit();
		this.getDataManager().register(IS_AGGRESSIVE, Boolean.valueOf(false));
		this.getDataManager().register(IS_ACTIVE, Boolean.valueOf(false));
    }
	
	
    //TODO
    public void setWitchCombat() {
    	this.setWitchActive(true);
    }
    
    
    public void setAggressive(boolean aggressive)  {
        this.getDataManager().set(IS_AGGRESSIVE, Boolean.valueOf(aggressive));
    }
    
    public void setWitchActive(boolean active) {
    	this.getDataManager().set(IS_ACTIVE, Boolean.valueOf(active));
    }

    public boolean isWitchActive() {
    	 return ((Boolean)this.getDataManager().get(IS_ACTIVE)).booleanValue();
    }
    
    public boolean isDrinkingPotion() {
        return ((Boolean)this.getDataManager().get(IS_AGGRESSIVE)).booleanValue();
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
