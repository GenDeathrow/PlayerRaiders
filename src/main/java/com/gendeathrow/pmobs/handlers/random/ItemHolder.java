package com.gendeathrow.pmobs.handlers.random;

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

public class ItemHolder
{
	private String itemID;
	private int metaID;
	private NBTTagCompound nbtData;
	private JsonObject nbtRawJson;
	
	private boolean isComplete = false;
	
	private ItemStack stack =  ItemStack.EMPTY;
	
	
	Gson gson = new Gson();
	
	public ItemHolder(){
		itemID = Items.AIR.getRegistryName().toString();
		metaID = 0;
		nbtData = null;
		stack = ItemStack.EMPTY;
	}
	
	public ItemHolder(Item itemIn) {
		itemID = itemIn.getRegistryName().toString();
		metaID = 0;
		nbtData = null;
		stack = ItemStack.EMPTY;
	}
	
	public ItemHolder(ItemStack stackIn){
		itemID = stackIn.getItem().getRegistryName().toString();
		metaID = stackIn.getMetadata();
		stack = stackIn;
		nbtData = stackIn.hasTagCompound() ?  stackIn.getTagCompound() : null;
		isComplete = true;
	}
	
	public ItemHolder(String itemID, int metaID, double chance, int qty) {
		this.itemID = itemID;
		this.metaID = metaID;
		this.nbtData = null;
	}
	

	@Nullable
	public Item getItem() {
		return Item.getByNameOrId(this.itemID);
	}
	
	public int getMeta() {
		return this.metaID;
	}
	
	public int getAmount() {
		return 1;
	}
	
	/**
	 * Get or Create itemstack for this Loot Item
	 * @return
	 */
	public ItemStack getStack() {
		
		if(!isComplete) {
			isComplete = true;
			if(getItem() != null) {
				stack = new ItemStack(getItem(), this.getAmount(), this.metaID);
				
				if(this.nbtData != null && !this.nbtData.hasNoTags())
	                	stack.setTagCompound(this.nbtData);
			}
		}
		//System.out.println("Getting: "+ stack.getDisplayName());
		return stack.copy();
	}
	
	public ItemHolder readJsonObject(JsonObject data) throws NumberFormatException {
		itemID =  data.has("itemID") ? data.get("itemID").getAsString() : Items.AIR.getRegistryName().toString();
		metaID =  data.has("metaID") ? data.get("metaID").getAsInt() : 0;
	
		nbtRawJson =  data.has("nbt")  ? data.get("nbt").getAsJsonObject() : null; 
			
		try {
			nbtData = data.has("nbt")  ? JsonToNBT.getTagFromJson(data.get("nbt").getAsJsonObject().toString()) : null;
		} catch (NBTException e) {
			e.printStackTrace();
			nbtData = null;
		}
		
		return this;
	}
	
	public JsonObject writeJsonObject(JsonObject data) throws NumberFormatException {
		data.addProperty("itemID", itemID);
		data.addProperty("metaID", metaID);
		
		if(nbtData != null && !nbtData.hasNoTags()) {
			JsonElement element = gson.fromJson(nbtData.toString(), JsonElement.class);
			data.add("nbt", element.getAsJsonObject());
		}
		
		return data;
	}
}
