package com.gendeathrow.pmobs.handlers;

import java.util.Random;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty;
import com.gendeathrow.pmobs.world.RaidersWorldDifficulty;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty.DiffEntry;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty.EnumBonusDiff;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class DifficultyProgression 
{
	
	
	protected EntityRaiderBase raider;
	private Random rand;
	
	protected DiffEntry classDifficulty;
	
		
	public DifficultyProgression(EntityRaiderBase raiderIn)
	{
		this.raider = raiderIn;
		this.rand = raiderIn.getRNG();
		this.classDifficulty = RaiderClassDifficulty.getByClass(raiderIn.getClass());  
	}
	
	/**
	 * Gives you the Difficulty for current Raid Difficulty based on your Parameters
	 * 
	 * @param eachIncrease - How much you'd like it to increase each raid difficultly
	 * @return
	 */
	public double calculateProgressionDifficulty(double eachIncrease)
	{
		return  eachIncrease * RaidersWorldDifficulty.INSTANCE.calculateRaidDifficulty(raider.world);
	}
		
	/**
	 * Gives you the Difficulty for current Raid Difficulty based on your Parameters
	 * 
	 * @param eachIncrease - How much you'd like it to increase each raid difficultly
	 * @param Max - Maximum % amount 
	 * @return
	 */
    public double calculateProgressionDifficulty(double eachIncrease, double Max)
    {
    	return calculateProgressionDifficulty(eachIncrease, 0, Max);
    }
    
    /**
     * Gives you the Difficulty for current Raid Difficulty based on your Parameters 
     * 
     * @param eachIncrease - How much you'd like it to increase each raid difficultly
     * @param startDifficulty - What amount % you'd like to start at
     * @param Max - Maximum % amount 
     * @return
     */
    public double calculateProgressionDifficulty(double eachIncrease, int startDifficulty, double Max){
    	if(RaidersWorldDifficulty.INSTANCE.calculateRaidDifficulty(raider.world) > startDifficulty){
    		return eachIncrease * RaidersWorldDifficulty.INSTANCE.calculateRaidDifficulty(raider.world) > Max ? Max : eachIncrease * RaidersWorldDifficulty.INSTANCE.calculateRaidDifficulty(raider.world);
    	}
    	
    	return 0;
    }
    

   public void setupDifficutlyOfRaider(DifficultyInstance difficulty) {

	   if(classDifficulty == null || !PMSettings.isDifficultyProgressionEnabled) return;
	   
    	setSpeedDifficulty(difficulty);
    	setHealthDifficulty(difficulty);
    	setDamageDifficulty(difficulty);
    	setArmorDifficulty(difficulty);
    	setKnockBackResistance(difficulty);
    }
    
    protected void setSpeedDifficulty(DifficultyInstance difficulty)
    {  
    	if(raider.isChild() || raider instanceof EntityRaiderWitch) return; 
       	int speedLevel = classDifficulty.getDifficultyLevel(EnumBonusDiff.SPEEDBONUS);
       	if(speedLevel < 1) return;
    	double speed = (PMSettings.SpeedMaxIncrease < 0 ? speedLevel * PMSettings.SpeedIncrease : MathHelper.clamp(speedLevel * PMSettings.SpeedIncrease, 0, PMSettings.SpeedMaxIncrease) );
        raider.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(EntityRaiderBase.SPEED_BOOST_ID, "Speed Bonus", speed * raider.world.rand.nextDouble(), EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));
    }
    
    protected void setHealthDifficulty(DifficultyInstance difficulty)
    {
    	int classHealthLevel = classDifficulty.getDifficultyLevel(EnumBonusDiff.HEALTHBONUS);
    	if(classHealthLevel <= 1 || raider.isChild()) return; 
    	int bonusHealth = (PMSettings.HealthMaxOut < 0 ? classHealthLevel * PMSettings.HealthIncrease : MathHelper.clamp(classHealthLevel * PMSettings.HealthIncrease, 0, PMSettings.HealthMaxOut) );
        
    	if(this.rand.nextFloat() < 0.10){
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader Health bonus", this.rand.nextDouble() * 4.0D + 1.00D + bonusHealth, EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));
        }else {
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health bonus", this.rand.nextDouble() * 1.00D + bonusHealth, EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));
        }
    }
    
    protected void setDamageDifficulty(DifficultyInstance difficulty)
    {
    	int dmgLevel = classDifficulty.getDifficultyLevel(EnumBonusDiff.DAMAGEBONUS);
    	if(dmgLevel < 1 || raider.isChild()) return;
    	double dmgBonus =  (PMSettings.dmgMaxIncrease < 0 ? ((double) dmgLevel) * PMSettings.dmgIncrease : MathHelper.clamp(((double) dmgLevel) * PMSettings.dmgIncrease, 0, PMSettings.dmgMaxIncrease));
    	raider.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier("damage bonus", this.rand.nextDouble() * dmgBonus, EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));
    }
    
    protected void setArmorDifficulty(DifficultyInstance difficulty)
    {
    	int armorLevel = classDifficulty.getDifficultyLevel(EnumBonusDiff.ARMORBONUS);
    	if(armorLevel < 1 || raider.isChild()) return;
    	double dmgBonus =  (PMSettings.armorMax < 0 ? ((double) armorLevel) * PMSettings.armorIncrease : MathHelper.clamp(((double) armorLevel) * PMSettings.armorIncrease, 0, PMSettings.armorMax));
    	raider.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier("armor bonus", this.rand.nextDouble() * dmgBonus, EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));
    }
    
    protected void setKnockBackResistance(DifficultyInstance difficulty) {
    	
    	int knockbackLevel = classDifficulty.getDifficultyLevel(EnumBonusDiff.KNOCKBACKBONUS);
    	if(knockbackLevel < 1 || raider.isChild()) return;
    	double knockback =  (PMSettings.knockbackMax < 0 ? ((double) knockbackLevel) * PMSettings.knockbackIncrease : MathHelper.clamp(((double) knockbackLevel) * PMSettings.knockbackIncrease, 0, PMSettings.knockbackMax));
    	raider.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("knockback bonus", knockback, EnumAttrModifierOperation.ADD_VAL_TO_BASE.ordinal()));    	
    }
    
    
    
    public enum EnumAttrModifierOperation
    {
        /**
         * Adds the modifier value as-is to the base value d0.<br>
         * Further operations will use the sum of all modifiers with this operation plus the inital base value d0 as the sum pool d1.<br>
         * <tt>d1 = [foreach modifiervalue : d0 += modifiervalue]</tt>
         */
        ADD_VAL_TO_BASE,
        /**
         * Multiplies the base value d0 with the modifier value, which is acting as a percentage, then adds the result to the sum pool d1.<br>
         * <tt>d1 += [foreach modifiervalue : d0 * modifiervalue]</tt>
         */
        ADD_PERC_VAL_TO_SUM,
        /**
         * Multiplies and rises the sum pool d1 with the sum of 1.0 + modifier value (which is acting as a percentage).<br>
         * <tt>[foreach modifiervalue : (d1 *= 1.0F + modifiervalue)]</tt>
         */
        RISE_SUM_WITH_PERC_VAL
    }
}
