package com.gendeathrow.pmobs.handlers.random;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class LootDropRandom  extends WeightedRandom.Item{

	ItemStack stack;
	Item item;
	int meta;
	String nbt = null;
	

	public LootDropRandom(Item itemIn, int metaIn, int itemWeightIn) 
	{
		super(itemWeightIn);
		item = itemIn;
		meta = metaIn;
	}
	
	public LootDropRandom(Item itemIn, int metaIn, int itemWeightIn, String nbtIn) 
	{
		this(itemIn, metaIn, itemWeightIn);
		nbt = nbtIn;
	}
	
	public LootDropRandom(ItemStack itemstack, int itemWeightIn) 
	{
		super(itemWeightIn);
		this.stack = itemstack;
	}
	
	@Nullable
	public ItemStack getCopy()
	{
		if(stack.copy() != null) return stack.copy();
		else {
			
		}
		return null;
	}

	public String toString()
	{
		System.out.println(stack.getDisplayName() +":"+ stack.getItemDamage());
		return null;
	}
}
