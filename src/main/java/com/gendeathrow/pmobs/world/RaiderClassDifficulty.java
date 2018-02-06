package com.gendeathrow.pmobs.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.entity.mob.EntityBrute;
import com.gendeathrow.pmobs.entity.mob.EntityPyromaniac;
import com.gendeathrow.pmobs.entity.mob.EntityRaider;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;
import com.gendeathrow.pmobs.entity.mob.EntityRanger;
import com.gendeathrow.pmobs.entity.mob.EntityTweaker;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class RaiderClassDifficulty {

	
	public static HashMap<Class<? extends Entity>, DiffEntry> classesDifficulty = new HashMap<Class<? extends Entity>, DiffEntry>();
	
	public static void SetupRaiderClassDifficulty(){
		classesDifficulty.clear();
		
		classesDifficulty.put(EntityRaider.class, new DiffEntry(EntityRaider.class));
		classesDifficulty.put(EntityBrute.class, new DiffEntry(EntityBrute.class));
		classesDifficulty.put(EntityRaiderWitch.class, new DiffEntry(EntityRaiderWitch.class));
		classesDifficulty.put(EntityTweaker.class, new DiffEntry(EntityTweaker.class));
		classesDifficulty.put(EntityPyromaniac.class, new DiffEntry(EntityPyromaniac.class));
		classesDifficulty.put(EntityRanger.class, new DiffEntry(EntityRanger.class));
	}
	
	public static DiffEntry getRandomClass(World world) {
		DiffEntry[] entries = (DiffEntry[]) classesDifficulty.values().toArray();
		return entries[world.rand.nextInt(entries.length)];
	}
	
	@Nullable
	public static DiffEntry getByClass(Class<? extends Entity> clazz) {
		if(classesDifficulty.containsKey(clazz))
			return classesDifficulty.get(clazz);
		
		return null;
	}
	
	
	@Nullable
	public static DiffEntry getByName(String name) {
		for(Entry<Class<? extends Entity>, DiffEntry> classDiff : classesDifficulty.entrySet()) {
			if(classDiff.getKey().getSimpleName().equalsIgnoreCase(name))
				return classDiff.getValue();
		}
		
		return null;
	}
	
	public static void readNBT(NBTTagCompound tag) {

		if(tag.hasKey("classDifficulty")) {
			NBTTagList list = tag.getTagList("classDifficulty", NBT.TAG_COMPOUND);
			
			for(int i=0; i < list.tagCount(); i++) {
				NBTTagCompound entry = list.getCompoundTagAt(i);
				DiffEntry diff = getByName(entry.getString("id"));
				if(diff != null)
					diff.readNBT(entry);
			}
		}
	}
	
	public static NBTTagCompound writeNBT(NBTTagCompound tag) {
		
		NBTTagList list = new NBTTagList();
		
		for(Entry<Class<? extends Entity>, DiffEntry> classDiff : classesDifficulty.entrySet()) {
			NBTTagCompound diffNBT = new NBTTagCompound();
				classDiff.getValue().writeNBT(diffNBT);
			list.appendTag(diffNBT);
		}
		tag.setTag("classDifficulty", list);
		
		return tag;
	}
	
	
	
	/**
	 * Entry for class specific bonus for Raid Difficulty
	 * 
	 * @author GenDeathrow
	 *
	 */
	public static class DiffEntry {
		
		Class<? extends Entity> entityClass;
		int healthLvl = 0;
		int speedLvl = 0;
		int followRangeLvl = 0;
		int dmgLvl = 0;
		int armorLvl = 0;
		int knockbackLvl = 0;
		int abilityLvl = 0;
		
		public DiffEntry(Class<? extends Entity> entityClassIn) {
			this.entityClass = entityClassIn;
		}
		
		public void increaseRandomBonus(World world) {
			EnumBonusDiff[] entries = EnumBonusDiff.values();
			addLevel(entries[world.rand.nextInt(entries.length)], 1);
		}
		
		public void addLevel(EnumBonusDiff bonus, int add) {
			 
			switch(bonus) {
				case HEALTHBONUS:
					healthLvl += add;
					break;
				case SPEEDBONUS:
					speedLvl += add;
					break;
				case FOLLOWBONUS:
					followRangeLvl += add;
					break;
				case DAMAGEBONUS:
					dmgLvl += add;
					break;
				case ARMORBONUS:
					armorLvl += add;
					break;
				case KNOCKBACKBONUS:
					knockbackLvl += add;
					break;
				case ABILITYBONUS:
					abilityLvl += add;
					break;
			}
		}
		
		
		public int getDifficultyLevel(EnumBonusDiff bonus) {
			switch(bonus) {
				case HEALTHBONUS:
					return healthLvl;
				case SPEEDBONUS:
					return speedLvl;
				case FOLLOWBONUS:
					return followRangeLvl;
				case DAMAGEBONUS:
					return dmgLvl;
				case ARMORBONUS:
					return armorLvl;
				case KNOCKBACKBONUS:
					return knockbackLvl;
				case ABILITYBONUS:
					return abilityLvl;
			}
			
			return 0;
		}
		
		public void readNBT(NBTTagCompound tag) {
			
			healthLvl = tag.getInteger("healthBonus");
			speedLvl = tag.getInteger("speedBonus");
			followRangeLvl = tag.getInteger("followBonus");
			dmgLvl = tag.getInteger("dmgBonus");
			armorLvl = tag.getInteger("armorBonus");
			knockbackLvl = tag.getInteger("knockbackBonus");
			
		}
		
		public NBTTagCompound writeNBT(NBTTagCompound tag) {
			tag.setString("id", entityClass.getSimpleName());
			tag.setInteger("healthBonus", healthLvl);
			tag.setInteger("speedBonus", speedLvl);
			tag.setInteger("followBonus", followRangeLvl);
			tag.setInteger("dmgBonus", dmgLvl);
			tag.setInteger("armorBonus", armorLvl);
			tag.setInteger("knockbackBonus", knockbackLvl);
			
			return tag;
		}
	}
	
	
	public static enum EnumBonusDiff {
		
		HEALTHBONUS,
		SPEEDBONUS,
		FOLLOWBONUS,
		DAMAGEBONUS,
		ARMORBONUS,
		KNOCKBACKBONUS,
		ABILITYBONUS;
		
	}
}
