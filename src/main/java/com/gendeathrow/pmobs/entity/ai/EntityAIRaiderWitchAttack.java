package com.gendeathrow.pmobs.entity.ai;

import java.util.List;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIRaiderWitchAttack extends EntityAIBase
{
    private final EntityRaiderWitch raider;
    private int attackStep;
    private int attackTime;
    
    private int fireBallCoolDown = 55;
    private int lightingCoolDown = 100;
    private int potionCooldown = 20;
    private int pushBackCooldown = 20;
    
	
    private int lightningAttack = 0;
    private int nxLightingAttack = 15;
	private int maxStrikes = 3;
	private int strikes = 0;
    
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    
    private final double moveSpeedAmp = 1.4D;
    private final float maxAttackDistance = 60;
    
    private int armAnimation = 20;
    
    public EntityAIRaiderWitchAttack(EntityRaiderWitch raider)
    {
        this.raider = raider;
    }
    
    public boolean isInterruptible()
    {
        return false;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        EntityLivingBase entitylivingbase = this.raider.getAttackTarget();
        
        return entitylivingbase != null && raider.isWitchActive() && entitylivingbase.isEntityAlive();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.attackStep = 0;
        this.raider.setSwingingArms(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
    	super.resetTask();
        this.raider.setSwingingArms(false);
        this.seeTime = 0;
        this.attackTime = -1;
    }

    
    private String attackType = "potion";
    /**
     * Updates the task
     */
    public void updateTask()
    {
        --this.attackTime;
    	this.fireBallCoolDown--;
    	this.lightingCoolDown--;
    	this.potionCooldown--;
    	this.pushBackCooldown--;
        
        EntityLivingBase entitylivingbase = this.raider.getAttackTarget();
        double d0 = this.raider.getDistanceSqToEntity(entitylivingbase);
        boolean flag = this.raider.getEntitySenses().canSee(entitylivingbase);
        boolean flag1 = this.seeTime > 0;      
        
        boolean armsRaised = this.raider.isArmsRaised();
        
        if(armsRaised && this.armAnimation-- <= 0)
        {
        	this.raider.setArmsRaised(false);
        	this.armAnimation = 20;
        }
        
        if (flag != flag1)
        {
            this.seeTime = 0;
        }

        if (flag)
        {
            ++this.seeTime;
        }
        else
        {
            --this.seeTime;
        }

        if (d0 <= (double)this.maxAttackDistance && this.seeTime >= 20)
        {
            this.raider.getNavigator().clearPathEntity();
            ++this.strafingTime;
        }
        else
        {
            this.raider.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
            this.strafingTime = -1;
        }

        if (this.strafingTime >= 20)
        {  
            if ((double)this.raider.getRNG().nextFloat() < 0.3D)
            {
                this.strafingClockwise = !this.strafingClockwise;
            }

            if ((double)this.raider.getRNG().nextFloat() < 0.3D)
            {
                this.strafingBackwards = !this.strafingBackwards;
            }

            this.strafingTime = 0;
        } 
        
        if (this.strafingTime > -1)
        {
            if (d0 > (double)(this.maxAttackDistance * 0.75F))
            {
                this.strafingBackwards = false;
            }
            else if (d0 < (double)(this.maxAttackDistance * 0.25F))
            {
                this.strafingBackwards = true;
            }

            this.raider.getMoveHelper().strafe(this.strafingBackwards ? -0.85F : 1F, this.strafingClockwise ? 0.75F : -0.75F);
            this.raider.faceEntity(entitylivingbase, 30.0F, 30.0F);
        }
        else
        {
            this.raider.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
        }
        
        if (d0 < maxAttackDistance)
        {
        	if(this.attackTime <= 0)
        	{
        		if(this.pushBackCooldown <= 0 && d0 <= 10)
        		{
        			this.PushBackAttack(entitylivingbase);
        		}
        		else if(this.fireBallCoolDown <= 6)
        		{
        			this.FireBallAttack(entitylivingbase, d0);
        		}
        		else if(this.lightingCoolDown <= 0 && d0 <= 30)
        		{
        			this.LightingAttack(entitylivingbase, d0);
        		}
        		else if(this.potionCooldown <= 0 && d0 <= 25)
        		{
        			this.ThrowPotion(entitylivingbase);
        		}
        		
        		
        	}
        }


        super.updateTask();
    }
    
    private void FireBallAttack(EntityLivingBase entitylivingbase,  double d0)
    {
        double d1 = entitylivingbase.posX - this.raider.posX;
        double d2 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (this.raider.posY + (double)(this.raider.height / 2.0F));
        double d3 = entitylivingbase.posZ - this.raider.posZ;

        if (this.fireBallCoolDown <= 0)
        {
            ++this.attackStep;

            if (this.attackStep == 1)
            {
                this.fireBallCoolDown = 60;
            }
            else if (this.attackStep <= 4)
            {
                this.fireBallCoolDown = 6;
            }
            else
            {
                this.fireBallCoolDown = 50 + (this.raider.getRNG().nextInt(20) - 5);
                this.attackStep = 0;
                this.attackTime = 60;
            }

            if (this.attackStep > 1)
            {
            	this.raider.setArmsRaised(true);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                this.raider.world.playEvent((EntityPlayer)null, 1018, new BlockPos((int)this.raider.posX, (int)this.raider.posY, (int)this.raider.posZ), 0);

                for (int i = 0; i < 1; ++i)
                {
                    EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.raider.world, this.raider, d1 + this.raider.getRNG().nextGaussian() * (double)f, d2, d3 + this.raider.getRNG().nextGaussian() * (double)f);
                    entitysmallfireball.posY = this.raider.posY + (double)(this.raider.height / 2.0F) + 0.5D;
                    this.raider.world.spawnEntity(entitysmallfireball);
                }
            }
        }

        this.raider.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);

    }
    
    private void PushBackAttack(EntityLivingBase target) {
		
    	List<Entity> entities = this.raider.world.getEntitiesWithinAABBExcludingEntity(this.raider, this.raider.getEntityBoundingBox().grow(10.0D, 3.0D, 10.0D));
    	
    	if(!entities.isEmpty())
    	{
     	
        	this.raider.setArmsRaised(true);
        	
    		double d0 = (this.raider.getEntityBoundingBox().minX + this.raider.getEntityBoundingBox().maxX) / 2.0D;
    		double d1 = (this.raider.getEntityBoundingBox().minZ + this.raider.getEntityBoundingBox().maxZ) / 2.0D;
		
    		for (Entity entity : entities)
    		{
    			
    	   		this.raider.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1, 1.0F + (this.raider.world.rand.nextFloat() - this.raider.world.rand.nextFloat()) * 0.4F);
     	   		this.raider.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1, 1.0F + (this.raider.world.rand.nextFloat() - this.raider.world.rand.nextFloat()) * 0.4F);
     	   		this.raider.playSound(SoundEvents.EVOCATION_ILLAGER_CAST_SPELL, 1, 1.0F + (this.raider.world.rand.nextFloat() - this.raider.world.rand.nextFloat()) * 0.4F);

    			
    			double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 4.0D, 0.2333333D, d3 / d4 * 4.0D);
				
				if(entity instanceof EntityPlayerMP)
					((EntityPlayerMP) entity).connection.sendPacket(new SPacketEntityVelocity(entity));
    		}
    		
    		this.pushBackCooldown = 100 + (this.raider.getRNG().nextInt(30) - this.raider.getRNG().nextInt(15));
    		
    	 	this.attackTime = 30;
    	}
    	

    }
    
    
    private void LightingAttack(EntityLivingBase target,  double d0)
    {
	       if(strikes <= maxStrikes)
	       {
	    	   this.raider.setArmsRaised(true);
	    	   this.raider.world.addWeatherEffect(new EntityLightningBolt(this.raider.world, target.posX + getRandomPosition(), target.posY, target.posZ + getRandomPosition(), false));
	    	   strikes++;
	       }
	       else
	       {
	    	   this.nxLightingAttack = this.raider.getRNG().nextInt(10)+15;
	    	   lightningAttack = 0;
	    	   lightingCoolDown = 100 + (this.raider.getRNG().nextInt(20) - 10);
	    	   strikes = 0;
	    	   maxStrikes = this.raider.getRNG().nextInt(2)+1;
	    	   this.attackTime = 60;
	       }
    }
    
    private void ThrowPotion(EntityLivingBase target)
    {
    	double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
        double d1 = target.posX + target.motionX - this.raider.posX;
        double d2 = d0 - this.raider.posY;
        double d3 = target.posZ + target.motionZ - this.raider.posZ;
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3);
        PotionType potiontype = PotionTypes.HARMING;

        if (f >= 8.0F && !target.isPotionActive(MobEffects.SLOWNESS))
        {
            potiontype = PotionTypes.SLOWNESS;
        }
        else if (target.getHealth() >= 8.0F && !target.isPotionActive(MobEffects.POISON))
        {
            potiontype = PotionTypes.POISON;
        }
        else if (f <= 3.0F && !target.isPotionActive(MobEffects.WEAKNESS) && this.raider.getRNG().nextFloat() < 0.25F)
        {
            potiontype = PotionTypes.WEAKNESS;
        }

        EntityPotion entitypotion = new EntityPotion(this.raider.world, this.raider, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potiontype));
        entitypotion.rotationPitch -= 0.0F;
        entitypotion.setThrowableHeading(d1, d2 + (double)(f * 0.2F), d3, 0.75F, 8.0F);
        this.raider.world.playSound((EntityPlayer)null, this.raider.posX, this.raider.posY, this.raider.posZ, SoundEvents.ENTITY_WITCH_THROW, this.raider.getSoundCategory(), 1.0F, 0.8F + this.raider.getRNG().nextFloat() * 0.4F);
        this.raider.world.spawnEntity(entitypotion);
        this.potionCooldown = 60 + (this.raider.getRNG().nextInt(15) - this.raider.getRNG().nextInt(10));
        this.attackTime = 60;
    }
    
    
	private int getRandomPosition()
	{
		return this.raider.getRNG().nextInt(20) - 10;
	}
}
