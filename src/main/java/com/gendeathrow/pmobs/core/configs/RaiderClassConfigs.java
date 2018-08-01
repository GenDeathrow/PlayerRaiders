package com.gendeathrow.pmobs.core.configs;

import java.io.File;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.util.JsonConfig;
import com.google.gson.JsonObject;

public class RaiderClassConfigs extends JsonConfig{

	public RaiderClassConfigs(File dir) {
		super(new File(dir, "raider_classes.json"));
	}

	private String isEnabledID = "isEnabled"; 
	private String spawnWeightID = "spawnWeight";
	private String startDiffID = "startDiff";
	
	public void readFile() {
		
		this.Load();

		JsonObject raiderObject = defaultClassJson("Raider", PMSettings.raidersSpawnWeight, 0);
			PMSettings.raiderClass = isEnabled(raiderObject);
			PMSettings.raidersSpawnWeight = getWeight(raiderObject);
			//TODO Drops

		JsonObject bruteObject = defaultClassJson("Brute", PMSettings.bruteWeight, 0);
			PMSettings.bruteClass = isEnabled(bruteObject);
			PMSettings.bruteWeight = getWeight(bruteObject);
			PMSettings.bruteStartDiff = getStartDiff(bruteObject);
			//TODO Drops
			
		JsonObject pyromaniacObject = defaultClassJson("Pyromaniac", PMSettings.pyroWeight, 0);
			PMSettings.pyroClass = isEnabled(pyromaniacObject);
			PMSettings.pyroWeight = getWeight(pyromaniacObject);
			PMSettings.pyroStartDiff = getStartDiff(pyromaniacObject);

		JsonObject tweakerObject = defaultClassJson("Tweaker", PMSettings.tweakerWeight, 0);
			if(!tweakerObject.has("spawnOnlyAtNight")) {
				tweakerObject.addProperty("spawnOnlyAtNight", false);
				this.setHasChanged(true);
			}
			PMSettings.tweakersClass = isEnabled(tweakerObject);
			PMSettings.tweakerWeight = getWeight(tweakerObject);
			PMSettings.tweakerStartDiff = getStartDiff(tweakerObject);
			PMSettings.tweakerOnlyNight =  tweakerObject.get("spawnOnlyAtNight").getAsBoolean();
			
		JsonObject witchObject = defaultClassJson("Witch", PMSettings.screamerWeight, 0); 
			if(!witchObject.has("hasFogEffect")) {
				witchObject.addProperty("hasFogEffect", true);
				this.setHasChanged(true);
			}
			PMSettings.screamerClass = isEnabled(witchObject);
			PMSettings.screamerWeight = getWeight(witchObject);
			PMSettings.screamerStartDiff = getStartDiff(witchObject);
			PMSettings.screamerFogOn =  witchObject.get("hasFogEffect").getAsBoolean();

		JsonObject archerObject = defaultClassJson("Archer", PMSettings.rangerWeight, 1);
			PMSettings.rangerClass = isEnabled(archerObject);
			PMSettings.rangerWeight = getWeight(archerObject);
			PMSettings.rangerStartDiff = getStartDiff(archerObject);
			if(!archerObject.has("attackSpeedInTicks")) {
				archerObject.addProperty("attackSpeedInTicks", 20);
				this.setHasChanged(true);
			}
			PMSettings.rangerMaxAttackTime = archerObject.get("attackSpeedInTicks").getAsInt();
		
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
	

	public JsonObject defaultClassJson(String cat, int weight, int startDiff) {
		
		JsonObject json  = this.getCategory(cat);
		
		if(!json.has(isEnabledID)) {
			json.addProperty(isEnabledID, true);
			this.setHasChanged(true);
		}

		if(!json.has(spawnWeightID)) {
			json.addProperty(spawnWeightID, weight);
			this.setHasChanged(true);
		}
		
		if(!json.has(startDiffID) && cat != "Raider") {
			json.addProperty(startDiffID, startDiff);
			this.setHasChanged(true);
		}
		
		return json;
	}
	
}
