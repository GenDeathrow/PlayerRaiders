package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;

public class EntityAISearchForContainers  extends EntityAIBase
{

	  protected EntityLiving theThief;
	    /** The closest entity which is being watched by this one. */
	    protected Entity closestEntity;
	    /** This is the Maximum distance that the AI will look for the Entity */
	    protected float maxDistanceForPlayer;
	   // private int lookTime;
	    private final float chance;
	    //protected Class <? extends Entity > watchedClass;
	    
	    
	    public EntityAISearchForContainers(EntityLiving entitylivingIn, float maxDistance)
	    {
	    	this.theThief = entitylivingIn;
	    	
	    	this.maxDistanceForPlayer = maxDistance;
	    	
	    	this.chance = 0.02F;
	    	
	        this.setMutexBits(2);
	    }
	    
	    
	    @Override
	    public boolean shouldExecute() 
	    {
	        if (this.theThief.getRNG().nextFloat() >= this.chance)
	        {
	            return false;
	        }
	        else
	        {
	        	//this.theThief.worldObj..findNearestEntityWithinAABB(TileEntity.class, this.theThief.getEntityBoundingBox().expand((double)this.maxDistanceForPlayer, 3.0D, (double)this.maxDistanceForPlayer), this.maxDistanceForPlayer);
	        	
	        	
	        	return true;
	        }
//	            if (this.theThief.getAttackTarget() != null)
//	            {
//	                this.closestEntity = this.theThief.getAttackTarget();
//	            }
//
//	            if (this.watchedClass == EntityPlayer.class)
//	            {
//	                this.closestEntity = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, (double)this.maxDistanceForPlayer);
//	            }
//	            else
//	            {
//	                this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand((double)this.maxDistanceForPlayer, 3.0D, (double)this.maxDistanceForPlayer), this.theWatcher);
//	            }
//
//	            return this.closestEntity != null;
//	        }
	    }

}
