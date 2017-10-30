package com.gendeathrow.pmobs.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomItemDrop
{
	String itemID;
	int metaID;
	double chance;
	int qty;
	
	public CustomItemDrop(String itemID, int metaID, double chance, int qty)
	{
		this.itemID = itemID;
		this.metaID = metaID;
		this.chance = chance;
		this.qty = qty;
	}
	
	public static ArrayList<CustomItemDrop> getArrayItemDrops(String[] itemList)
	{
		ArrayList<CustomItemDrop> drops = new ArrayList<CustomItemDrop>();
		for(String item : itemList)
		{
			try
			{
				drops.add(getItemDrop(item));
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		return drops;
	}
	
	public Item getItem()
	{
		return Item.getByNameOrId(this.itemID);
	}
	
	public int getMeta()
	{
		return this.metaID;
	}
	
	public static CustomItemDrop getItemDrop(String data) throws NumberFormatException
	{
		
		String[] split = data.split(":");

		String itemID = split[0]+":"+split[1];
		int metaID = Integer.parseInt(split[2]);
		double chance= Double.parseDouble(split[4]);
		int qty = Integer.parseInt(split[3]);
		

		return new CustomItemDrop(itemID, metaID,chance,qty);
	}
	
	public boolean shouldDrop(Random rand)
	{
		return rand.nextDouble() <= this.chance;
	}
	
	public ItemStack getStack(Random rand)
	{
		return new ItemStack(Item.getByNameOrId(this.itemID), this.qty > 1 ? rand.nextInt(this.qty-1)+1 : this.qty, this.metaID);
	}
}
