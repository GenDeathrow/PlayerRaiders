package com.gendeathrow.pmobs.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.network.RaidNotificationPacket;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty.DiffEntry;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty.EnumBonusDiff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class RaidersWorldDifficulty {

	public static RaidersWorldDifficulty INSTANCE = new RaidersWorldDifficulty();
	
	private int currentRaidDifficulty = 0;
	private int lastRaidDifficulty = 0;
	
	private ArrayList<String> stringDisplay = new ArrayList<String>();
	
	private HashMap<Integer, ArrayList<HistoryEntry>>  HistoryTimeline = new HashMap<Integer, ArrayList<HistoryEntry>>();
	
	/**
	 *  This will update the current world difficulty, will either add or remove class difficulty.
	 * @param world
	 */
	public void UpdateWorldDifficulty(World world) {

		lastRaidDifficulty = currentRaidDifficulty;
		currentRaidDifficulty = calculateRaidDifficulty(world);

		int change = currentRaidDifficulty - lastRaidDifficulty;
		
		ArrayList<String> lines = new ArrayList<String>();
		
		if(change > 0 ) {
			increaseWorldDifficulty(world);

			lines.add("Raid Difficulty "+ currentRaidDifficulty);
			lines.add("Raiders have gotten harder!");
			lines.addAll(getAndResetDisplayLines());
		}
		else if(change < 0) {
			lines.add("Raid Difficulty "+ currentRaidDifficulty);
			lines.add("Difficulty was changed to a previous state.");
			lines.addAll(getAndResetDisplayLines());		
			decreaseWorldDifficulty(world);
		}
		
		if(!lines.isEmpty())
			RaidersMain.network.sendToAll(new RaidNotificationPacket(lines));
		
		Raiders_WorldData.get(world).markDirty();
		
	}
	
	
	/*
	 * Get Lines for Display purposes.
	 */
	public ArrayList<String> getDisplayLines(){
		return stringDisplay;
	}
	
	
	/*
	 * Get and reset Lines for Display
	 */
	public ArrayList<String> getAndResetDisplayLines(){
		ArrayList<String> lines = new ArrayList<String>(stringDisplay);
		stringDisplay.clear();
		return lines;
	}

	
/**
 * This adds new Entry into the History timeline. <br> History Entry allows players to change the day via commands and have the correct raider bonus for that current time.
 * 
 * @param raidDifficutly
 * @param classEntity
 * @param classBonus
 * @param amtChanged
 */
	public void addToTimeline(int raidDifficutly, Class<? extends Entity> classEntity, EnumBonusDiff classBonus, int amtChanged) {
		if(!HistoryTimeline.containsKey(raidDifficutly))
			HistoryTimeline.put(raidDifficutly, new ArrayList<HistoryEntry>());
		
		HistoryTimeline.get(raidDifficutly).add(new HistoryEntry(classEntity, classBonus, amtChanged));
		
	}

	/**
	 * Increases the World Difficulty to next level. If Timeline history exist, it will use that. <br> 
	 * Otherwise it will create a random amount of power up for classes.  <Br> 
	 * This will handle any where from 1+ raid difficulty changes. 
	 * @param world
	 */
	private void increaseWorldDifficulty(World world) {

		int check = lastRaidDifficulty;
		int current = currentRaidDifficulty;
		
		while(check++ < current) {
			
			if(HistoryTimeline.containsKey(check))
			{
				ArrayList<HistoryEntry> historyEntries = HistoryTimeline.get(check);
				for(HistoryEntry entry : historyEntries)
					increaseClassDifficultyFromHistory(world, entry);
			
			}
			else {
				for(int i = 0; i < world.rand.nextInt(3) + 1; i++)
					increaseRandomClassDifficulty(world, check);
			}
		}
		
		
		Raiders_WorldData.get(world).markDirty();
	}
	
	
	/**
	 * Decrease the World Difficulty to previous level. If Timeline history exist, it will use that. <br> 
	 * Otherwise it will create a random amount of power up for classes.  
	 * @param world
	 */
	private void decreaseWorldDifficulty(World world) {
		int check = lastRaidDifficulty+1;
		int current = currentRaidDifficulty;
		
		while(check-- > current) {
			if(HistoryTimeline.containsKey(check))
			{
				ArrayList<HistoryEntry> historyEntries = HistoryTimeline.get(check);
				for(HistoryEntry entry : historyEntries) {
					decreaseClassDifficultyFromHistory(world, entry);
				}
			}
		}
	}
	
	/**
	 * Function that takes a History Entry and makes changes to the Class Difficulty
	 * 
	 * @param world
	 * @param entry
	 */
	private void decreaseClassDifficultyFromHistory(World world, HistoryEntry entry) {
		DiffEntry classDiff = RaiderClassDifficulty.getByClass(entry.entityClass);

		if(classDiff != null) {
			classDiff.addLevel(entry.classBonus, -entry.amt);
		}
	}
	
	/**
	 * Function that takes a History Entry and makes changes to the Class Difficulty
	 * 
	 * @param world
	 * @param entry
	 */
	private void increaseClassDifficultyFromHistory(World world, HistoryEntry entry) {
		increaseClassDifficulty(world, entry.entityClass,  entry.classBonus,  entry.amt);
	}
	
	/**
	 * Update Class Difficulty 
	 * 
	 * @param world
	 * @param entry
	 */
	public void increaseClassDifficulty(World world, Class<? extends Entity> entityClass, EnumBonusDiff classBonus, int amt) {
		DiffEntry classDiff = RaiderClassDifficulty.getByClass(entityClass);
		
		if(classDiff != null) {
			classDiff.addLevel(classBonus, amt);
		}
		
		Entity className = EntityList.createEntityByIDFromName(EntityList.getKey(entityClass), world);
		stringDisplay.add(className.getName() +" "+ classBonus.getUnLocalization() +" ++");
	}
	
	/**
	 * This will give a Random Class Difficulty and Random bonus update. <br> Called when a new Raid Difficulty is added to the list. 
	 * 
	 * @param world
	 * @param raidDifficulty
	 */
	public void increaseRandomClassDifficulty(World world, int raidDifficulty) {
		
		int amt = 1;
		int roll = world.rand.nextInt(3) + 1;
		
		for(int i = 0; i < roll; i++) {
			DiffEntry classDiff = RaiderClassDifficulty.getRandomClass(world);
			EnumBonusDiff bonus = classDiff.increaseRandomBonus(world, amt);
			Entity className = EntityList.createEntityByIDFromName(EntityList.getKey(classDiff.entityClass), world);
			stringDisplay.add(className.getName() +" "+ bonus.getUnLocalization() +" ++");
			addToTimeline(raidDifficulty, classDiff.entityClass, bonus, amt);
		}
			
	}
	
	public void readNBT(NBTTagCompound tag) {
		if(tag.hasKey("raidDifficulty"))
			currentRaidDifficulty = tag.getInteger("raidDifficulty");

		if(tag.hasKey("historyTimeline")) {
			HistoryTimeline.clear();
			NBTTagList historyTimelineNBT = tag.getTagList("historyTimeline", NBT.TAG_COMPOUND);
			for(int i = 0; i < historyTimelineNBT.tagCount(); i++) {
				NBTTagCompound raidDiffEntry = historyTimelineNBT.getCompoundTagAt(i);
				
				int raidDifficultyDay = raidDiffEntry.getInteger("raidDifficulty");
				NBTTagList historyEntry = tag.getTagList("entries", NBT.TAG_COMPOUND);
					for(int j = 0; j < historyEntry.tagCount(); j++) {
						NBTTagCompound entry = historyTimelineNBT.getCompoundTagAt(i);
					
						Class<? extends Entity> classEntity = EntityList.getClass(new ResourceLocation(entry.getString("class")));
						int amt = entry.getInteger("amt");
						EnumBonusDiff bonus = EnumBonusDiff.getBonusByID(entry.getString("bonus"));
						
						if(classEntity != null && bonus != null) {
							addToTimeline(raidDifficultyDay, classEntity, bonus, amt);
						}
					}
			}
		}
	}
	
	
	public NBTTagCompound writeNBT(NBTTagCompound tag) {
		tag.setInteger("raidDifficulty", currentRaidDifficulty);
		
		
		NBTTagList historyTimelineNBT = new NBTTagList();
		
		for(Entry<Integer, ArrayList<HistoryEntry>> raidDifficulty : HistoryTimeline.entrySet()) {
			
			NBTTagCompound raidDifficultyNBT = new NBTTagCompound();
			raidDifficultyNBT.setInteger("raidDifficulty", raidDifficulty.getKey());
			
			NBTTagList historyEntries = new NBTTagList();
			
			for(HistoryEntry entry : raidDifficulty.getValue()) {
				NBTTagCompound entryNBT = new NBTTagCompound();
				entryNBT.setString("class", EntityList.getKey(entry.entityClass).toString());
				entryNBT.setInteger("amt", entry.amt);
				entryNBT.setString("bonus", entry.classBonus.name());
				historyEntries.appendTag(entryNBT);
			}
			raidDifficultyNBT.setTag("entries", historyEntries);
			
			historyTimelineNBT.appendTag(raidDifficultyNBT);
		}
		tag.setTag("historyTimeline", historyTimelineNBT);
		
		return tag;
	}

    /** 
     * Get Current Raid Difficultly
     * 
     * @return
     */
    public int getCurrentRaidDifficulty(World worldObj) {
    	return currentRaidDifficulty;
    }

	/** 
     * Get Current Day
     * @return
     */
    public static int getDay(World world) {
    	return (int)(world.getWorldTime()/24000);
    }
    
    
    /**
     * Calculate what the Raid Difficulty should be.
     *  
     * @param world
     * @return Raid Difficulty Integer
     */
    public static int calculateRaidDifficulty(World world) {
    	return ((int)(getDay(world) / PMSettings.dayDifficultyProgression));
    }

    /**
     * HistoryEntry keeps track of changes to a Classes Difficulty 
     *  
     * @author GenDeathrow
     */
    public static class HistoryEntry{

    	public Class<? extends Entity> entityClass;
    	public EnumBonusDiff classBonus;
    	public int amt;
    	
    	public HistoryEntry(Class<? extends Entity> classEntityIn, EnumBonusDiff classBonusIn, int amtChangedIn) {
    		this.entityClass = classEntityIn;
    		this.classBonus = classBonusIn;
    		this.amt = amtChangedIn;
    	}
    	
    }
}
