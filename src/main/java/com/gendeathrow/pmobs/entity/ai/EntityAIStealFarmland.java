package com.gendeathrow.pmobs.entity.ai;

import com.gendeathrow.pmobs.entity.EntityRaiderBase;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIStealFarmland extends EntityAIMoveToBlock
{
	  /** Villager that is harvesting */
    private final EntityRaiderBase theRaider;
    private boolean wantsToReapStuff;
    private int currentTask;

    public EntityAIStealFarmland(EntityRaiderBase theRaiderIn, double speedIn)
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
            if (!this.theRaider.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }

            this.currentTask = -1;
            this.wantsToReapStuff = true;
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
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
            World world = this.theRaider.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if (this.currentTask == 0 && block instanceof BlockCrops)
            {
                world.destroyBlock(blockpos, true);
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
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0))
            {
                this.currentTask = 0;
                return true;
            }

        }

        return false;
    }
}
