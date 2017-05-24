package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class EntityAIPyromaniac extends EntityAIBase
{
	  /** Villager that is harvesting */
  private final EntityRaiderBase theRaider;
  private boolean wantsToBurnStuff;
  private int currentTask;
  private int runDelay = 0;

  public EntityAIPyromaniac(EntityRaiderBase theRaiderIn, double speedIn)
  {
      this.theRaider = theRaiderIn;
      this.setMutexBits(1);
      this.runDelay = 200;
  }

  public boolean isInterruptible()
  {
      return false;
  }
  
  /**
   * Returns whether the EntityAIBase should begin execution.
   */
  public boolean shouldExecute()
  {
	  
	  if(this.theRaider.getHeldItemOffhand() != null && this.theRaider.getHeldItemOffhand().getItem() == Items.FLINT_AND_STEEL)
	  {
		  if (this.runDelay-- <= 0)
		  {
			  if (!this.theRaider.worldObj.getGameRules().getBoolean("mobGriefing"))
			  {
				  return false;
			  }

			  this.currentTask = -1;
			  this.wantsToBurnStuff = true;
			  return true;
		  }
	  }
	return false;
  }

  /**
   * Returns whether an in-progress EntityAIBase should continue executing
   */
  public boolean continueExecuting()
  {
      return this.currentTask >= 0 && super.continueExecuting();
  }

  /**
   * Execute a one shot task or start executing a continuous task
   */
  public void startExecuting()
  {
      super.startExecuting();
  }

  /**
   * Resets the task
   */
  public void resetTask()
  {
      super.resetTask();
  }

  /**
   * Updates the task
   */
  public void updateTask()
  {
      super.updateTask();
  
 
      for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(this.theRaider.getPosition().add(-3, -1, -3), this.theRaider.getPosition().add(3, 2, 3)))
      {
    	  try
    	  {
    		  
    		  RayTraceResult rayTrace = theRaider.worldObj.rayTraceBlocks(new Vec3d(theRaider.posX, theRaider.posY + theRaider.getEyeHeight(), theRaider.posZ), new Vec3d(blockpos$mutableblockpos.getX() + 0.5, blockpos$mutableblockpos.getY() + 0.5, blockpos$mutableblockpos.getZ() + 0.5));

    		  if(rayTrace.typeOfHit == Type.BLOCK)
    		  {
    			  BlockPos pos = rayTrace.getBlockPos().offset(rayTrace.sideHit);
    		  
    			  Block block = theRaider.worldObj.getBlockState(rayTrace.getBlockPos()).getBlock();
    			  
    			  if (block.isFlammable(this.theRaider.worldObj, rayTrace.getBlockPos(), rayTrace.sideHit) && !(block instanceof BlockDoublePlant)  && !(block instanceof BlockCrops) && !(block instanceof BlockTallGrass))
    			  {
    				  if (theRaider.worldObj.isAirBlock(pos))
    				  {
    					  this.theRaider.getLookHelper().setLookPosition((double)pos.getX() + 0.5D, (double)(pos.getY() + 1), (double)pos.getZ() + 0.5D, 10.0F, (float)this.theRaider.getVerticalFaceSpeed());
    					  theRaider.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE,  1.0F, theRaider.getRNG().nextFloat() * 0.4F + 0.8F);
    					  theRaider.worldObj.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
    			       	  this.theRaider.swingArm(EnumHand.MAIN_HAND);
    			       	  this.runDelay = 500 + this.theRaider.getRNG().nextInt(200);
    			       	  break;
    				  }
    			  }
    		  }
    	  }catch(Throwable e)
     	 {
    		  //Caused an 8GB file.. no good...
     		// RaidersCore.logger.debug("Raider couldn't set block on fire..."+ e);
     	 }
      }
  }
  
}
