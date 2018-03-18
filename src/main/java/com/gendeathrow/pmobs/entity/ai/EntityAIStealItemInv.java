package com.gendeathrow.pmobs.entity.ai;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityAIStealItemInv extends EntityAIMoveToBlock
{
	
	private final EntityRaiderBase raider;
	
	private boolean hasStolen = false;
	
	private ArrayList<BlockPos> checked = new ArrayList<BlockPos>();
	private int targetChance;
	
	public EntityAIStealItemInv(EntityRaiderBase raider, double speedIn) 
	{
		this(raider, speedIn, 10);
	}
	
	public EntityAIStealItemInv(EntityRaiderBase raider, double speedIn, int chance) 
	{
		super(raider, speedIn, 16);
		this.raider = raider;
		this.targetChance = chance;
	}

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.raider.getRNG().nextInt(this.targetChance) <= 1)
        {
            return false;
        }
        
        if (this.runDelay <= 0)
        {
            if (!this.raider.world.getGameRules().getBoolean("mobGriefing"))
            {
                return false;
            }
            this.currentTask = -1;

        }

        return super.shouldExecute();
    }

    public boolean continueExecuting()
    {
        return this.currentTask >= 0 && !hasStolen && super.continueExecuting();
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

        if (this.raider.getDistanceSqToCenter(this.destinationBlock) < 1.0D)
        {
            World world = this.raider.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = null;
            Block block;
            TileEntity te = null;
            
            for(EnumFacing facing : EnumFacing.HORIZONTALS)
            {
            	te = world.getTileEntity(blockpos.offset(facing));
            	
            	if(te == null) continue;
            	if(te instanceof IInventory || te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            	{
            		iblockstate = world.getBlockState(blockpos.offset(facing));
                    break;
            	}
            }
            
            
            if(iblockstate == null || te == null) 
            {
            	this.runDelay = 10;
            	return;
            }
            	
            IInventory chestInventory = null;
            
            InventoryBasic inventorybasic = this.raider.getRaidersInventory();
            
 
            
            if(te instanceof IInventory)
            {
            	chestInventory = (IInventory)te;
                ItemStack stealItem = null;
                int slot = 0;
                boolean flag = false;
          
                for (int i = 0; i < chestInventory.getSizeInventory(); ++i)
                {
                	if(shouldStealItem(stealItem, chestInventory.getStackInSlot(i)))
                	{
                		stealItem = chestInventory.getStackInSlot(i);
                		slot = i;
                	}
                }
                

                if (stealItem != null)
                {
                	flag = true;
                }

                if (flag)
                {
                	hasStolen = true;
                	this.currentTask = -1;
                	world.playSound((EntityPlayer)null, blockpos.getX(), (double)blockpos.getY() + 0.5D, blockpos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                       
                	if(te instanceof TileEntityChest)
                	{
                		++((TileEntityChest)te).numPlayersUsing;
                		world.addBlockEvent(destinationBlock, te.getBlockType(), 1, ((TileEntityChest)te).numPlayersUsing);
                	}
                    	
                	try
                	{
                		chestInventory.setInventorySlotContents(slot, inventorybasic.addItem(stealItem));
                	}
                	catch(NullPointerException e)
                	{
                		e.printStackTrace();
                	}
                	
                }
                
               	this.checked.add(te.getPos());


            }
            else if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            {
            	IItemHandler capInventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                ItemStack stealItem = null;
                int slot = 0;
                boolean flag = false;
                
                for (int i = 0; i < capInventory.getSlots(); ++i)
                {
                	if(shouldStealItem(stealItem, capInventory.getStackInSlot(i)))
                	{
                		stealItem = capInventory.getStackInSlot(i);
                		slot = i;
                	}
                }
                
                    if (stealItem != null)
                    {
                    	flag = true;
                    }

                    if (flag)
                    {
                    	hasStolen = true;
                    	this.currentTask = -1;
                    	world.playSound((EntityPlayer)null, blockpos.getX(), (double)blockpos.getY() + 0.5D, blockpos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                       
                    	if(te instanceof TileEntityChest)
                    	{
                    		++((TileEntityChest)te).numPlayersUsing;
                    		world.addBlockEvent(destinationBlock, te.getBlockType(), 1, ((TileEntityChest)te).numPlayersUsing);
                    	}
                    	
                     	try
                    	{
                     		capInventory.insertItem(slot, inventorybasic.addItem(stealItem), false);
                    	}
                    	catch(NullPointerException e)
                    	{
                    		e.printStackTrace();
                    	}

                    }
                    
                    
                   	this.checked.add(te.getPos());

            }
            this.runDelay = 10;
        }
    }


	
	@Nullable
    private boolean shouldStealItem(ItemStack stealItem, ItemStack itemstack)
    {
        boolean flag = false;
            
            if(itemstack == null) 
            {
            	return false;
            }
            else
            {
                
            	if(stealItem == null) 
            	{
            		flag = true;
            	}
            	else if((itemstack.getItem() instanceof ItemSword || itemstack.getItem() instanceof ItemArmor || itemstack.getItem() instanceof ItemBow) && !(stealItem.getItem() instanceof ItemSword || stealItem.getItem() instanceof ItemArmor || stealItem.getItem() instanceof ItemBow))
            	{
            		flag = true;
            	}
                else if (itemstack.getItem() instanceof ItemSword && stealItem.getItem() instanceof ItemSword)
                {
                    ItemSword itemsword = (ItemSword)itemstack.getItem();
                    ItemSword itemsword1 = (ItemSword)stealItem.getItem();

                    if (itemsword.getDamageVsEntity() == itemsword1.getDamageVsEntity())
                    {
                       flag = itemstack.getMetadata() > stealItem.getMetadata() || itemstack.hasTagCompound() && !stealItem.hasTagCompound();
                    }
                    else
                    {
                        flag = itemsword.getDamageVsEntity() > itemsword1.getDamageVsEntity();
                    }

                }
                else if (itemstack.getItem() instanceof ItemBow && stealItem.getItem() instanceof ItemBow)
                {
                   flag = itemstack.hasTagCompound() && !stealItem.hasTagCompound();
                }
                else if (itemstack.getItem() instanceof ItemArmor && stealItem.getItem() instanceof ItemArmor)
                {
                    ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                    ItemArmor itemarmor1 = (ItemArmor)stealItem.getItem();

                    if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount)
                    {
                        flag = itemstack.getMetadata() > stealItem.getMetadata() || itemstack.hasTagCompound() && !stealItem.hasTagCompound();
                    }
                    else
                    {
                        flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
                    }
                }
                else if((itemstack.getItem() instanceof ItemFood) && (stealItem.getItem() instanceof ItemFood))
                {
                	ItemFood itemfood = (ItemFood)itemstack.getItem();
                	ItemFood itemfood1 = (ItemFood)stealItem.getItem();
                	

                	if(itemfood.getSaturationModifier(itemstack) >=  itemfood1.getSaturationModifier(stealItem))
                	{
                		flag = true;
                	}else if(itemfood.getHealAmount(itemstack) >=  itemfood1.getHealAmount(stealItem))
                	{
                		flag = true;
                	}
                }
                else if(!(stealItem.getItem() instanceof ItemSword || stealItem.getItem() instanceof ItemArmor || stealItem.getItem() instanceof ItemBow || stealItem.getItem() instanceof ItemFood) && this.raider.getRNG().nextDouble() < .20) flag = true;

            }

            return flag;
    }
    
    /**
     * Return true to set given position as destination
     */
    
    private int stolenTimer = 300;

	private int currentTask = 0;
    
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
    	//if() return false;
    	
        Block block = null;
        
        for(EnumFacing facing : EnumFacing.HORIZONTALS)
        {
        	if(this.checked.contains(pos.offset(facing))) continue;
        	
        	block = worldIn.getBlockState(pos.offset(facing)).getBlock();
        	
        	TileEntity te = worldIn.getTileEntity(pos.offset(facing));
        	
        	if(te == null) continue;
        	
        	if(te instanceof IInventory || te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        	{
        		this.currentTask  = 0;
        		return true;
        	}
        }
        
        return false;
    }
    
    

}
