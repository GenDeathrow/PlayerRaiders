package com.gendeathrow.pmobs.world;

import java.util.HashMap;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class RaiderClassDifficulty {

	
	public static HashMap<Class<? extends Entity>, DiffEntry> classesDifficulty = new HashMap<Class<? extends Entity>, DiffEntry>();
	
	static {
		SetupRaiderClassDifficulty();
	}
	
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
		Object[] entries = classesDifficulty.values().toArray();
		return (DiffEntry) entries[world.rand.nextInt(entries.length)];
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
			NBTTagCompound diffNBT = classDiff.getValue().writeNBT(new NBTTagCompound());
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
		
		public Class<? extends Entity> entityClass;
		private int healthLvl = 0;
		private int speedLvl = 0;
		private int dmgLvl = 0;
		private int armorLvl = 0;
		private int knockbackLvl = 0;
		private int abilityLvl = 0;
		
		public DiffEntry(Class<? extends Entity> entityClassIn) {
			this.entityClass = entityClassIn;
		}
		
		public EnumBonusDiff increaseRandomBonus(World world, int amt) {
			EnumBonusDiff[] entries = EnumBonusDiff.values();
			EnumBonusDiff bonus = entries[world.rand.nextInt(entries.length)];
			addLevel(bonus, amt);
			return bonus;
		}
		
		public void addLevel(EnumBonusDiff bonus, int add) {
			 
			switch(bonus) {
				case HEALTHBONUS:
					healthLvl += add;
					break;
				case SPEEDBONUS:
					speedLvl += add;
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
				//case ABILITYBONUS:
				//	abilityLvl += add;
				//	break;
			}
			
			MathHelper.clamp(healthLvl, 0, Integer.MAX_VALUE);
			MathHelper.clamp(speedLvl, 0, Integer.MAX_VALUE);
			MathHelper.clamp(dmgLvl, 0, Integer.MAX_VALUE);
			MathHelper.clamp(armorLvl, 0, Integer.MAX_VALUE);
			MathHelper.clamp(knockbackLvl, 0, Integer.MAX_VALUE);
			
		}
		
		
		public int getDifficultyLevel(EnumBonusDiff bonus) {
			switch(bonus) {
				case HEALTHBONUS:
					return healthLvl;
				case SPEEDBONUS:
					return speedLvl;
				case DAMAGEBONUS:
					return dmgLvl;
				case ARMORBONUS:
					return armorLvl;
				case KNOCKBACKBONUS:
					return knockbackLvl;
				//case ABILITYBONUS:
				//	return abilityLvl;
			}
			
			return 0;
		}
		
		public void readNBT(NBTTagCompound tag) {
			
			healthLvl = tag.getInteger("healthBonus");
			speedLvl = tag.getInteger("speedBonus");
			dmgLvl = tag.getInteger("dmgBonus");
			armorLvl = tag.getInteger("armorBonus");
			knockbackLvl = tag.getInteger("knockbackBonus");
			
		}
		
		public NBTTagCompound writeNBT(NBTTagCompound tag) {
			
			tag.setString("id", entityClass.getSimpleName());
			tag.setInteger("healthBonus", healthLvl);
			tag.setInteger("speedBonus", speedLvl);
			tag.setInteger("dmgBonus", dmgLvl);
			tag.setInteger("armorBonus", armorLvl);
			tag.setInteger("knockbackBonus", knockbackLvl);
			
			return tag;
		}
	}
	
	
	public static enum EnumBonusDiff {
		
		HEALTHBONUS("Health"),
		SPEEDBONUS("Speed"),
		DAMAGEBONUS("Damage"),
		ARMORBONUS("Armor"),
		KNOCKBACKBONUS("Knockback Resistance");
		//ABILITYBONUS("Ability");
		
		String unlocalizedName;
		EnumBonusDiff(String idIn) {
			unlocalizedName = idIn;
		}
		
		public String getUnLocalization() {
			return unlocalizedName;
		}
		
		public static EnumBonusDiff getBonusByID(String id) {
			for(EnumBonusDiff value : EnumBonusDiff.values()) {
				if(id.equalsIgnoreCase(value.name()))
					return value;
			}
			return null;
		}
		

	}
}
