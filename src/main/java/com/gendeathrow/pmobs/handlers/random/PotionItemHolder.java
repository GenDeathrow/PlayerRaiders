package com.gendeathrow.pmobs.handlers.random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;

public class PotionItemHolder extends ItemHolder{

	HashMap<String, Integer> potions = new HashMap<String, Integer>();

	boolean potionsAdded = false;
	
	public PotionItemHolder(Item itemIn) {
		super(itemIn);
	}
	
	public PotionItemHolder(ItemStack stackIn){
		super(stackIn);
	}
	
	@Override
	public ItemStack getStack() {
		ItemStack item = super.getStack();

		if(!potionsAdded)
		{
			List<PotionEffect> collection = new ArrayList<PotionEffect>();
			for(Entry<String, Integer> potionEntry : potions.entrySet()) {
				Potion potion = Potion.getPotionFromResourceLocation(potionEntry.getKey());
				if(potion != null) collection.add(new PotionEffect(potion, potionEntry.getValue()));
			}
			
			if(item != null && !collection.isEmpty())
				PotionUtils.appendEffects(item, collection);
			
			potionsAdded = true;
		}
		return item;
	}
	
	@SuppressWarnings("unused")
	private boolean hasEffect(PotionEffect potion, ItemStack stack) {
		if(PotionUtils.getEffectsFromStack(stack).contains(potion))
			return true;
		return false;
	}
	
	public void addPotionEffect(PotionEffect potionIn) {
		String id = potionIn.getPotion().getRegistryName().toString();
		int ticks = potionIn.getDuration();
		addPotionEffect(id, ticks);
	}
	
	public void addPotionEffect(String potionID, int ticks) {
		if(!this.potions.containsKey(potionID))
			this.potions.put(potionID, ticks);
	}
	
	@Override
	public ItemHolder readJsonObject(JsonObject data) throws NumberFormatException {
		super.readJsonObject(data);
		potions.clear();
		
		
		if(data.has("TippedArrowsPotions")) {
			JsonArray potionNBTList = data.get("TippedArrowsPotions").getAsJsonArray();

			for(JsonElement potionNBT : potionNBTList) {
				String potionID = null;
				int ticks = 40;
				
				if(potionNBT.getAsJsonObject().has("potionID"))
					potionID = potionNBT.getAsJsonObject().get("potionID").getAsString();
					
				if(potionNBT.getAsJsonObject().has("ticks")) 
					ticks = potionNBT.getAsJsonObject().get("ticks").getAsInt();
				
				if(potionID != null)
					addPotionEffect(potionID, ticks);
			}
		}
		return this;
	}
	
	@Override
	public JsonObject writeJsonObject(JsonObject data) throws NumberFormatException {
		super.writeJsonObject(data);
		
		JsonArray potionNBTList = new JsonArray();
		
		for(Entry<String, Integer> potion : potions.entrySet()) {
			JsonObject potionObject = new JsonObject();	
				potionObject.addProperty("potionID", potion.getKey());
				potionObject.addProperty("ticks", potion.getValue());
			potionNBTList.add(potionObject);
		}
		
		data.add("TippedArrowsPotions", potionNBTList);
		return data;
	}
}
