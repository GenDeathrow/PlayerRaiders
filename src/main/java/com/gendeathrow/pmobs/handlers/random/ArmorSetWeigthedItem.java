package com.gendeathrow.pmobs.handlers.random;

import javax.annotation.Nullable;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorSetWeigthedItem extends AbstractEquipmentWeighted
{

	ItemHolder head;
	ItemHolder body;
	ItemHolder legs;
	ItemHolder feet;
	
	boolean alwayFullSet;
	
	
	
	public ArmorSetWeigthedItem(@Nullable ItemHolder head, @Nullable ItemHolder body, @Nullable ItemHolder legs, @Nullable ItemHolder feet, int itemWeightIn, boolean alwaysFullSet) 
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
		ItemStack stack = ItemStack.EMPTY;

		if(slot == EntityEquipmentSlot.HEAD && head != null)
		{
			stack = head.getStack();
		}else if(slot == EntityEquipmentSlot.CHEST && body != null)
		{
			stack = body.getStack();
		}else if(slot == EntityEquipmentSlot.LEGS && legs != null)
		{
			stack = legs.getStack();
		}else if(slot == EntityEquipmentSlot.FEET && feet != null)
		{
			stack = feet.getStack();
		}
		
		return stack;
	}
	
	

}
