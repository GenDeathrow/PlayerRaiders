package com.gendeathrow.pmobs.handlers.random;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;

public class LootDropRandom  extends WeightedRandom.Item{

	ItemStack stack = ItemStack.EMPTY;
	String itemid;
	int meta = 0;
	int minAmt = 1;
	int maxAmt = 1;
	JsonObject nbt = null;
	Random rand = new Random();
	
	public LootDropRandom(String itemIn, int metaIn, int itemWeightIn) 
	{
		super(itemWeightIn);  
		itemid = itemIn;
		meta = metaIn;
	}
	
	public LootDropRandom(String itemIn, int metaIn, int minAmtIn,int maxAmtIn, int itemWeightIn, JsonObject nbtIn) 
	{
		this(itemIn, metaIn, minAmtIn, maxAmtIn, itemWeightIn);
		nbt = nbtIn;
	}
	
	public LootDropRandom(String itemIn, int metaIn, int minAmtIn,int maxAmtIn, int itemWeightIn) 
	{
		this(itemIn, metaIn, itemWeightIn);
		this.minAmt = minAmtIn;
		this.maxAmt = maxAmtIn;
	}
	
	public LootDropRandom(ItemStack itemstack, int itemWeightIn) 
	{
		super(itemWeightIn);
		this.stack = itemstack;
	}
	
	@Nullable
	public ItemStack getCopy()
	{
		if(!stack.copy().isEmpty()) return stack.copy();
		else {
			Item item = Item.getByNameOrId(itemid);
			if(item != null) {
				int amt = this.minAmt;
				
				if(this.minAmt != this.maxAmt)
					amt = MathHelper.clamp(rand.nextInt(maxAmt - minAmt) + minAmt, 1, item.getItemStackLimit());
				stack = new ItemStack(item, amt, this.meta);
				
				if(nbt != null) {
					try {
						NBTTagCompound newTag = JsonToNBT.getTagFromJson(nbt.toString());
						stack.setTagCompound(newTag);
					} catch (NBTException e) {
						e.printStackTrace();
					}
				}
			}
			else
				RaidersMain.logger.error("Error trying to find Item '"+  itemid + "' While trying to drop loot for a raider Mob. Check to make sure all your item id is correct.");
		}
		return null;
	}

	public String toString()
	{
		if(!stack.isEmpty())
			RaidersMain.logger.info("Stack Created: "+ stack.getDisplayName() +":"+ stack.getItemDamage());
		else {
			RaidersMain.logger.info("Stack Waiting: "+ itemid +":"+ meta);
		}
		return null;
	}
}
