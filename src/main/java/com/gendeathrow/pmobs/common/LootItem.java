package com.gendeathrow.pmobs.common;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class LootItem {

	private String itemID;
	private int metaID;
	private double chance;
	private int qty;
	private NBTTagCompound nbtData;
	private JsonObject nbtRawJson;
	
	private boolean isComplete = false;
	
	private ItemStack stack;
	
	
	Gson gson = new Gson();
	
	public LootItem(){
		itemID = Items.AIR.getRegistryName().toString();
		metaID = 0;
		chance = 0.5d;
		qty = 1;
		nbtData = null;
		stack = ItemStack.EMPTY;
	}
	
	public LootItem(ItemStack stackIn){
		itemID = stackIn.getItem().getRegistryName().toString();
		metaID = stackIn.getMetadata();
		chance = 0.20d;
		qty = stackIn.getCount();
		stack = stackIn;
		nbtData = stackIn.hasTagCompound() ?  stackIn.getTagCompound() : null;
		isComplete = true;
	}
	
	public LootItem(String itemID, int metaID, double chance, int qty) {
		this.itemID = itemID;
		this.metaID = metaID;
		this.chance = chance;
		this.qty = qty;
		this.nbtData = null;
	}
	
	@Nullable
	public Item getItem() {
		return Item.getByNameOrId(this.itemID);
	}
	
	public int getMeta() {
		return this.metaID;
	}
	
	/**
	 * Checks the chance of this item dropping. 
	 * @param rand
	 * @return
	 */
	public boolean shouldDrop(Random rand) {
		return rand.nextDouble() <= this.chance;
	}
	
	/**
	 * Get or Create itemstack for this Loot Item
	 * @param rand
	 * @return
	 */
	public ItemStack getStack(Random rand) {
		
		if(!isComplete) {
			isComplete = true;
			if(getItem() != null) {
				stack = new ItemStack(getItem() , this.qty > 1 ? rand.nextInt(this.qty-1)+1 : this.qty, this.metaID);
				
				if(this.nbtData != null && !this.nbtData.hasNoTags())
	                	stack.setTagCompound(this.nbtData);

				return stack.copy();
			}
		}
		return stack.copy();
	}
	
	public LootItem readJsonObject(JsonObject data) throws NumberFormatException {
		itemID =  data.has("itemID") ? data.get("itemID").getAsString() : Items.AIR.getRegistryName().toString();
		metaID =  data.has("metaID") ? data.get("metaID").getAsInt() : 0;
		chance=   data.has("chance") ? data.get("chance").getAsDouble() : 0.5;
		qty =     data.has("qty")    ? data.get("qty").getAsInt() : 1;
		
		nbtRawJson =  data.has("nbt")  ? data.get("nbt").getAsJsonObject() : null; 
			
		try {
			nbtData = data.has("nbt")  ? JsonToNBT.getTagFromJson(data.get("nbt").getAsJsonObject().toString()) : null;
		} catch (NBTException e) {
			e.printStackTrace();
			nbtData = null;
		}
		
		return this;
	}
	
	public void writeJsonObject(JsonObject data) throws NumberFormatException {
		data.addProperty("itemID", itemID);
		data.addProperty("metaID", metaID);
		data.addProperty("chance", chance);
		data.addProperty("qty", qty);
		
		if(nbtData != null && !nbtData.hasNoTags()) {
			
			JsonElement element = gson.fromJson(nbtData.toString(), JsonElement.class);
			
			data.add("nbt", element.getAsJsonObject());
		}
		
		
	}
	
}
