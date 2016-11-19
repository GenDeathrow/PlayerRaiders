package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class EntityAIStealItem extends EntityAIMoveToBlock
{
	
	private final EntityRaiderBase raider;
	
	private boolean hasStolen;

	public EntityAIStealItem(EntityRaiderBase raider, double speedIn) 
	{
		super(raider, speedIn, 16);
		this.raider = raider;
		
	}
	

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!this.raider.worldObj.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            this.currentTask = -1;

        }

        return super.shouldExecute();
    }

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
        
        this.raider.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.raider.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World world = this.raider.worldObj;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            
            if (block instanceof BlockChest)
            {
            	TileEntity te = world.getTileEntity(this.destinationBlock);
            	
            	//if(te == null || !(te instanceof IInventory) || !te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return;
            	if(te == null || !(te instanceof IInventory)) return;
            	
            	
            	IInventory chestInventory = (IInventory)te;
            	
                InventoryBasic inventorybasic = this.raider.getRaidersInventory();
//                boolean flag1 = false;
//                int slot = 0;
//                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
//                {
//                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
//                    
//                    
//                    if(itemstack == null) 
//                    {
//                    	flag1 = true;
//                    	slot = i;
//                    }
//                }
//                
//                if(!flag1) return;

                for (int i = 0; i < chestInventory.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = chestInventory.getStackInSlot(i);
                    boolean flag = false;

                    if (itemstack != null)
                    {
                    	flag = true;
                    }

                    if (flag)
                    {
                    	hasStolen = true;
                    	this.currentTask = -1;
                    	world.playSound((EntityPlayer)null, blockpos.getX(), (double)blockpos.getY() + 0.5D, blockpos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                       
                    	++((TileEntityChest)te).numPlayersUsing;
                    	world.addBlockEvent(destinationBlock, te.getBlockType(), 1, ((TileEntityChest)te).numPlayersUsing);
                    	//inventorybasic.setInventorySlotContents(slot, itemstack.copy());
                    	
                       	chestInventory.setInventorySlotContents(i, inventorybasic.addItem(itemstack));

                        break;
                    }
                }
            }

            this.runDelay = 10;
        }
    }

    /**
     * Return true to set given position as destination
     */
    
    private int stolenTimer = 300;

	private int currentTask = -1;
    
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
    	if(hasStolen) return false;
//    	if(hasStolen && stolenTimer-- > 0) return false;
//    	else if(hasStolen) 
//    	{
//    		this.hasStolen = false;
//    		stolenTimer = 300;
//    	}
//    	
    	
        Block block = worldIn.getBlockState(pos).getBlock();
//  
//    	TileEntity te = worldIn.getTileEntity(this.destinationBlock);
//    	
//    	if(te == null || !(te instanceof IInventory)) return false;
//    	
//    	if(te instanceof IInventory || te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
//    	{
//    		return true;
//    	}
    	
        if (block == Blocks.CHEST)
        {
        		this.currentTask  = 0;
                return true;
        }

        return false;
    }
    
    

}
