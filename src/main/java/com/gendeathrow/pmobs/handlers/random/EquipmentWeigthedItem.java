package com.gendeathrow.pmobs.handlers.random;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class EquipmentWeigthedItem extends WeightedRandom.Item
{

	ItemStack stack;
	
	ArrayList<EquipmentWeigthedItem> offHand;
	
	public EquipmentWeigthedItem(ItemStack itemstack, int itemWeightIn) 
	{
		super(itemWeightIn);
		this.stack = itemstack;
	}
	
	public EquipmentWeigthedItem(ItemStack itemstack, int itemWeightIn, ArrayList<EquipmentWeigthedItem> offHand) 
	{
		this(itemstack, itemWeightIn);
		this.offHand = offHand;
	} 
	
	@Nullable
	public ItemStack getCopy()
	{
		if(stack.copy() != null) return stack.copy();
		return null;
	}

	public String toString()
	{
		System.out.println(stack.getDisplayName() +":"+ stack.getItemDamage());
		return null;
	}
	
	public EquipmentWeigthedItem getRandomOffHand()
	{
		EquipmentWeigthedItem outOffHand = WeightedRandom.getRandomItem(new Random(), this.offHand);
		return outOffHand;
	}
}
