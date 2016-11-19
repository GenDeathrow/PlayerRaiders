package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class EntityAIPyromaniac extends EntityAIMoveToBlock
{
	  /** Villager that is harvesting */
  private final EntityRaiderBase theRaider;
  private boolean wantsToBurnStuff;
  private int currentTask;

  public EntityAIPyromaniac(EntityRaiderBase theRaiderIn, double speedIn)
  {
      super(theRaiderIn, speedIn, 16);
      this.theRaider = theRaiderIn;
  }

  /**
   * Returns whether the EntityAIBase should begin execution.
   */
  public boolean shouldExecute()
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
          World world = this.theRaider.worldObj;
          BlockPos blockpos = this.destinationBlock.east();
          IBlockState iblockstate = world.getBlockState(blockpos);
          Block block = iblockstate.getBlock();

          if (this.currentTask == 0)
          {
              if (world.isAirBlock(blockpos.up()))
              {
            	  //world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            	  world.setBlockState(blockpos, Blocks.FIRE.getDefaultState(), 11);
              }

              //stack.damageItem(1, playerIn);
          }
          
          this.currentTask = -1;
          this.runDelay = 10;
      }
  }

  /**
   * Return true to set given position as destination
   */
  protected boolean shouldMoveTo(World worldIn, BlockPos pos)
  {
	  
      Block block = worldIn.getBlockState(pos.east()).getBlock();

      if (block.isFlammable(worldIn, pos.east(), EnumFacing.UP))
      {
          if (this.wantsToBurnStuff && this.currentTask < 0)
          {
              this.currentTask = 0;
              return true;
          }
      }

      return false;
  }
}
