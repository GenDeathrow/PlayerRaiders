package com.gendeathrow.pmobs.core.configs;

import java.io.File;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.init.RegisterItems;
import com.gendeathrow.pmobs.util.JsonConfig;
import com.google.gson.JsonObject;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RaiderClassConfigs extends JsonConfig{

	public RaiderClassConfigs(File dir) {
		super(new File(dir, "raider_classes.json"));
	}

	private String isEnabledID = "isEnabled";
	private String spawnWeightID = "spawnWeight";
	private String startDiffID = "startDiff";
	private String extraDropPercentageID = "extraDropPercentage";
	private String extraDropsID = "extraDrops";
	
	public void readFile() {

		
		JsonObject raiderObject = defaultClassJson("Raider", PMSettings.raidersSpawnWeight, 0, 0.1, null);
			PMSettings.raiderClass = isEnabled(raiderObject);
			PMSettings.raidersSpawnWeight = getWeight(raiderObject);
			//TODO Drops
			
		JsonObject bruteObject = defaultClassJson("Brute", PMSettings.bruteWeight, 0, 0.1, new Object[] {new ItemStack(RegisterItems.bruteSerumSample), 10});
			PMSettings.bruteClass = isEnabled(bruteObject);
			PMSettings.bruteWeight = getWeight(bruteObject);
			PMSettings.bruteStartDiff = getStartDiff(bruteObject);
			//TODO Drops
			
		JsonObject pyromaniacObject = defaultClassJson("Pyromaniac", PMSettings.pyroWeight, 0, 0.1, null);
			PMSettings.pyroClass = isEnabled(pyromaniacObject);
			PMSettings.pyroWeight = getWeight(pyromaniacObject);
			PMSettings.pyroStartDiff = getStartDiff(pyromaniacObject);
		
		JsonObject tweakerObject = defaultClassJson("Tweaker", PMSettings.tweakerWeight, 0, 0.1, null);
			if(!tweakerObject.has("spawnOnlyAtNight")) {
				tweakerObject.addProperty("spawnOnlyAtNight", false);
				this.setHasChanged(true);
			}
			PMSettings.tweakersClass = isEnabled(tweakerObject);
			PMSettings.tweakerWeight = getWeight(tweakerObject);
			PMSettings.tweakerStartDiff = getStartDiff(tweakerObject);
			PMSettings.tweakerOnlyNight =  tweakerObject.get("spawnOnlyAtNight").getAsBoolean();
			
		JsonObject witchObject = defaultClassJson("Witch", PMSettings.screamerWeight, 0, 0.1, new Object[] {new ItemStack(Items.DRAGON_BREATH), 1}, new Object[] {new ItemStack(Items.EMERALD), 10}, new Object[] {new ItemStack(Items.SPIDER_EYE), 20}, new Object[] {new ItemStack(Items.EXPERIENCE_BOTTLE, 3), 40}, new Object[] {new ItemStack(Items.MUSHROOM_STEW), 40}); 
			if(!witchObject.has("hasFogEffect")) {
				witchObject.addProperty("hasFogEffect", true);
				this.setHasChanged(true);
			}
			PMSettings.screamerClass = isEnabled(witchObject);
			PMSettings.screamerWeight = getWeight(witchObject);
			PMSettings.screamerStartDiff = getStartDiff(witchObject);
			PMSettings.screamerFogOn =  witchObject.get("hasFogEffect").getAsBoolean();
			
		JsonObject archerObject = defaultClassJson("Archer", PMSettings.rangerWeight, 1, 0.1, null);
			PMSettings.rangerClass = isEnabled(archerObject);
			PMSettings.rangerWeight = getWeight(archerObject);
			PMSettings.rangerStartDiff = getStartDiff(archerObject);
		
		if(this.hasChanged())
			this.Save();
	}
	
	private int getWeight(JsonObject obj) {
		return obj.get(spawnWeightID).getAsInt();
	}
	
	private boolean isEnabled(JsonObject obj) {
		return obj.get(isEnabledID).getAsBoolean();
	}
	
	private int getStartDiff(JsonObject obj) {
		return obj.get(startDiffID).getAsInt();
	}
	

	public JsonObject defaultClassJson(String cat, int weight, int startDiff, double extraDropPercentage, Object[]... object) {
		
		JsonObject json  = this.getCategory(cat);
		 
		if(!json.has(isEnabledID)) {
			json.addProperty(isEnabledID, true);
			this.setHasChanged(true);
		}
		
		if(!json.has(spawnWeightID)) {
			json.addProperty(spawnWeightID, weight);
			this.setHasChanged(true);
		}
		
		if(!json.has(startDiffID)) {
			json.addProperty(startDiffID, startDiff);
			this.setHasChanged(true);
		}
		
//		if(!json.has(extraDropPercentageID)) {
//			json.addProperty(extraDropPercentageID, extraDropPercentage);
//			this.setHasChanged(true);
//		}
		
//		if(!json.has(extraDropsID)) {
//			JsonArray array = new JsonArray();
//			
//			if(object != null)
//				for(Object[] drop : object) {
//				
//					if(drop.equals(null)) continue;
//				
//					ItemStack stack = ((ItemStack) drop[0]);
//				
//					if(stack.isEmpty()) continue;
//					
//					int weightIn = (int) drop[1];
//				
//					JsonObject itemJson = new JsonObject();
//						itemJson.addProperty("itemid", stack.getItem().getRegistryName().toString());
//						itemJson.addProperty("meta", stack.getMetadata());
//						itemJson.addProperty("minAmt", 1);
//						itemJson.addProperty("maxAmt", stack.getCount());
//						itemJson.addProperty("weight", weightIn);
//					
//						if(stack.hasTagCompound())
//							itemJson.addProperty("nbt", stack.getTagCompound().toString());
//					
//						array.add(itemJson);
//				}
//			
//			json.add("extraDrops", array);
//			
//			this.setHasChanged(true);
//		}

		
		return json;
	}
	
}
