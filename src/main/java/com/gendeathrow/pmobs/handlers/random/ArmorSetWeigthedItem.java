package com.gendeathrow.pmobs.handlers.random;

import javax.annotation.Nullable;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

public class ArmorSetWeigthedItem extends WeightedRandom.Item
{

	ItemStack head;
	ItemStack body;
	ItemStack legs;
	ItemStack feet;
	
	boolean alwayFullSet;
	
	
	
	public ArmorSetWeigthedItem(@Nullable ItemStack head, @Nullable ItemStack body, @Nullable ItemStack legs, @Nullable ItemStack feet, int itemWeightIn, boolean alwaysFullSet) 
	{
		super(itemWeightIn);
		
		this.head = head;
		this.body = body;
		this.legs = legs;
		this.feet = feet;
		
		this.alwayFullSet = alwaysFullSet;
	}
	
	@Nullable
	public ItemStack getArmorbyEquipmentSlot(EntityEquipmentSlot slot)
	{
		ItemStack stack = null;

		if(slot == EntityEquipmentSlot.HEAD && head != null)
		{
			stack = head;
		}else if(slot == EntityEquipmentSlot.CHEST && body != null)
		{
			stack = body;
		}else if(slot == EntityEquipmentSlot.LEGS && legs != null)
		{
			//System.out.println("legs"+ legs.getDisplayName() + legs.getMetadata());
			stack = legs;
		}else if(slot == EntityEquipmentSlot.FEET && feet != null)
		{
			//System.out.println("head");
			stack = feet;
		}
		
		return stack;
	}
	
	

}
