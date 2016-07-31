package com.gendeathrow.pmobs.handlers;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;

public class EquipmentWeigthedItem extends WeightedRandom.Item
{

	Item item;
	int meta;
	NBTTagCompound tag;

	EntityEquipmentSlot equiptmentslot;
	
	public EquipmentWeigthedItem(int itemWeightIn) 
	{
		super(itemWeightIn);
		
		
	}

}
