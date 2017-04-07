package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class OLDEntityAIPyromaniac extends EntityAIMoveToBlock
{
	  /** Villager that is harvesting */
  private final EntityRaiderBase theRaider;
  private boolean wantsToBurnStuff;
  private int currentTask;

  public OLDEntityAIPyromaniac(EntityRaiderBase theRaiderIn, double speedIn)
  {
      super(theRaiderIn, speedIn, 16);
      this.theRaider = theRaiderIn;
      this.setMutexBits(1);
     // this.runDelay = 1200;
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
		  if (this.runDelay <= 0)
		  {
			  if (!this.theRaider.worldObj.getGameRules().getBoolean("mobGriefing"))
			  {
				  return false;
			  }

			  this.currentTask = -1;
			  this.wantsToBurnStuff = true;
		  }
	  }

      return super.shouldExecute();
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
      this.theRaider.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.theRaider.getVerticalFaceSpeed());

      if (this.getIsAboveDestination())
      {
          World worldIn = this.theRaider.worldObj;
          BlockPos blockpos = this.destinationBlock;
          IBlockState iblockstate = worldIn.getBlockState(blockpos);
          Block block = iblockstate.getBlock();
          boolean flag = false;
          BlockPos burnPos = blockpos;
          
          if (this.currentTask == 0)
          {
        	  try
        	  {
        		  for(EnumFacing facing : EnumFacing.HORIZONTALS)
        		  {
        			  block = worldIn.getBlockState(blockpos.offset(facing,2).up()).getBlock();
              	
        			  if (block.getFlammability(worldIn, blockpos.offset(facing,2), EnumFacing.UP) > 0 && !(block instanceof BlockDoublePlant)  && !(block instanceof BlockCrops) && !(block instanceof BlockTallGrass))
        			  {
        				  for(EnumFacing burnlocation : EnumFacing.VALUES)
        				  {
        					  burnPos = blockpos.offset(facing,2).up().offset(burnlocation);
        					  if(worldIn.getBlockState(burnPos).getBlock() == Blocks.AIR) // && worldIn.getBlockState(burnPos.down()).getBlock().isSideSolid(worldIn.getBlockState(burnPos.down()), worldIn, burnPos.down(), EnumFacing.UP)
        					  {
        						  flag = true;
        						  break;
        					  }
        					  
        				  }
        				  if(flag) break;
        			  }
        		  }
        	 }catch(Throwable e)
        	 {
        		 RaidersCore.logger.debug("Raider couldn't set block on fire..."+ e);
        		 flag = false;
        	 }
          }
          
          if(flag)
          {
        	  	this.theRaider.swingArm(EnumHand.MAIN_HAND);
    			worldIn.playSound(null, burnPos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, this.theRaider.getRNG().nextFloat() * 0.4F + 0.8F);
    			worldIn.setBlockState(burnPos, Blocks.FIRE.getDefaultState(), 11);
          }
          this.currentTask = -1;
          this.runDelay = 600;
      }
  }

  /**
   * Return true to set given position as destination
   */
  protected boolean shouldMoveTo(World worldIn, BlockPos pos)
  {
      Block block = null;
	  
      boolean flag = false;
      
      for(EnumFacing facing : EnumFacing.HORIZONTALS)
      {
      	block = worldIn.getBlockState(pos.offset(facing,2).up()).getBlock();
      	try
      	{
      		if (!(block instanceof BlockDoublePlant)  && !(block instanceof BlockCrops) && !(block instanceof BlockTallGrass) && block.getFlammability(worldIn, pos.offset(facing,2), EnumFacing.UP) > 0)
      		{

      			for(EnumFacing burnlocation : EnumFacing.VALUES)
      			{
      				BlockPos burnPos = pos.offset(facing,2).up().offset(burnlocation);
      				if(worldIn.getBlockState(burnPos).getBlock() == Blocks.AIR)
      				{
      					flag = true;
      				}
      			}
      		}
      	}catch(Throwable e)
      	{
      		RaidersCore.logger.debug("Raider couldn't set block on fire..." + e);
      		flag = false;
      	}
      }
  	  
      
		if (this.wantsToBurnStuff && this.currentTask < 0 && flag)
		{
			this.currentTask = 0;
			return true;
		}

      return false;
  }
}
