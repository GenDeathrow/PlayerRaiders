package com.gendeathrow.pmobs.entity.ai;

import java.util.ArrayList;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.storage.InventoryStroageModifiable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
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
	
	public EntityAIStealItemInv(EntityRaiderBase raider, double speedIn) {
		this(raider, speedIn, 10);
	}
	
	public EntityAIStealItemInv(EntityRaiderBase raider, double speedIn, int chance) {
		super(raider, speedIn, 16);
		this.raider = raider;
		this.targetChance = chance;
	}

	/**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.raider.getRNG().nextInt(this.targetChance) <= 1) {
            return false;
        }
        
        if (this.runDelay <= 0){
            if (!this.raider.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            this.currentTask = -1;
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return this.currentTask >= 0 && !hasStolen && super.shouldContinueExecuting();
    }
    
    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        super.resetTask();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        
        this.raider.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.raider.getVerticalFaceSpeed());

        if (this.raider.getDistanceSqToCenter(this.destinationBlock) < 1.0D) {
            World world = this.raider.world;
            BlockPos blockpos = this.destinationBlock;
            IBlockState iblockstate = null;
            Block block;
            TileEntity te = null;
            
            for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            	te = world.getTileEntity(blockpos.offset(facing));
            	
            	if(te == null) continue;
            	if(te instanceof IInventory || te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
            	{
            		iblockstate = world.getBlockState(blockpos.offset(facing));
                    break;
            	}
            }
            
            
            if(iblockstate == null || te == null) {
            	this.runDelay = 10;
            	return;
            }
            	
            IItemHandler chestInventory = null;
            
            InventoryStroageModifiable radiersInv = this.raider.getRaidersInventory();
            
            if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
            	chestInventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            	
                ItemStack stealItem = ItemStack.EMPTY;
                int slot = 0;
                
                for (int i = 0; i < chestInventory.getSlots(); ++i) {
                	if(shouldStealItem(chestInventory.getStackInSlot(i))) {
                		stealItem = chestInventory.getStackInSlot(i);
                		slot = i;
                		break;
                	}
                }
                

                if (!stealItem.isEmpty())
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
                		ItemStack extracted = chestInventory.extractItem(slot, chestInventory.getStackInSlot(slot).getCount(), false);
                		
                		if(!extracted.isEmpty())
                				this.raider.updateEquipmentIfNeeded(extracted);
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
    private boolean shouldStealItem(ItemStack itemstack)
    {
        boolean flag = false;
            
            if(itemstack.isEmpty()) 
            {
            	return false;
            }
            else
            {
            	ItemStack mainHandWeapon = this.raider.getHeldItemMainhand();

            	if (itemstack.getItem() instanceof ItemSword && mainHandWeapon.getItem() instanceof ItemSword)
                {
                    ItemSword itemsword = (ItemSword)itemstack.getItem();
                    ItemSword itemsword1 = (ItemSword)mainHandWeapon.getItem();

                    if (itemsword.getDamage(itemstack) == itemsword1.getDamage(mainHandWeapon))
                       flag = itemstack.getMetadata() > mainHandWeapon.getMetadata() || itemstack.hasTagCompound() && !mainHandWeapon.hasTagCompound();
                    else
                       flag = itemsword.getDamage(itemstack) > itemsword1.getDamage(mainHandWeapon);
                }
                else if (itemstack.getItem() instanceof ItemBow && mainHandWeapon.getItem() instanceof ItemBow) {
                   flag = itemstack.hasTagCompound() && !mainHandWeapon.hasTagCompound();
                }
                else if (itemstack.getItem() instanceof ItemArmor)
                {
                	EntityEquipmentSlot slottype = EntityRaiderBase.getSlotForItemStack(itemstack);
                    ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                    ItemStack itemarmor1 = this.raider.getItemStackFromSlot(slottype);
                   	
                    if(itemarmor1.isEmpty()) {
                    	flag = true;
                    }
                    else if (itemarmor1.getItem() instanceof ItemArmor && itemarmor.damageReduceAmount == ((ItemArmor)itemarmor1.getItem()).damageReduceAmount)
                        flag = itemstack.getMetadata() > mainHandWeapon.getMetadata() || itemstack.hasTagCompound() && !itemarmor1.hasTagCompound();
                    else if (itemarmor1.getItem() instanceof ItemArmor)
                        flag = itemarmor.damageReduceAmount > ((ItemArmor)itemarmor1.getItem()).damageReduceAmount;
                }else {
                	return this.raider.world.rand.nextFloat() < 0.3333;
                }

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
