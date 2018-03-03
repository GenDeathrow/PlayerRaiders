package com.gendeathrow.pmobs.handlers.random;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class EquipmentWeigthedItem extends AbstractEquipmentWeighted
{

	ItemHolder stackHolder;
	
	ArrayList<EquipmentWeigthedItem> offHand;
	
	public EquipmentWeigthedItem(ItemHolder itemstack, int itemWeightIn) 
	{
		super(itemWeightIn);
		this.stackHolder = itemstack;
	}
	
	public EquipmentWeigthedItem(ItemHolder itemstack, int itemWeightIn, ArrayList<EquipmentWeigthedItem> offHand) 
	{
		this(itemstack, itemWeightIn);
		this.offHand = offHand;
	} 
	
	@Nullable
	public ItemStack getCopy()
	{
		if(stackHolder.getStack() != null) return stackHolder.getStack();
		return null;
	}

	public String toString()
	{
		System.out.println(stackHolder.getStack().getDisplayName() +":"+ stackHolder.getStack().getItemDamage());
		return null;
	}
	
	public EquipmentWeigthedItem getRandomOffHand()
	{
		EquipmentWeigthedItem outOffHand = WeightedRandom.getRandomItem(new Random(), this.offHand);
		return outOffHand;
	}
}
