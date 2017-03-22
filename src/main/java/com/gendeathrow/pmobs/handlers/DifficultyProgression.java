package com.gendeathrow.pmobs.handlers;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;

public class DifficultyProgression 
{
	
	public static double speedDifficulty = .035;
		public static double speedDifficultyMax = .35;
		public static double speedMIN = 0.1;
		public static double speedMAX = .35;
		
	public static double healthDifficulty = .05;
		
		public static double healthIncrease = .05;
		
		protected EntityRaiderBase raider;
		private Random rand;
	
		
	public DifficultyProgression(EntityRaiderBase raiderIn)
	{
		this.raider = raiderIn;
		this.rand = raiderIn.getRNG();
	}
	
	/**
	 * Gives you the Difficulty for current Raid Difficulty based on your Parameters
	 * 
	 * @param eachIncrease - How much you'd like it to increase each raid difficultly
	 * @return
	 */
	public double calculateProgressionDifficulty(double eachIncrease)
	{
		return  eachIncrease * getRaidDifficulty();
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
    
    public double calculateProgressionDifficulty(double eachIncrease, int startDifficulty, double Max)
    {
    	if(getRaidDifficulty() > startDifficulty)
    	{
    		return eachIncrease * getRaidDifficulty() > Max ? Max : eachIncrease * getRaidDifficulty();
    	}
    	
    	return 0;
    }
    
    /** 
     * Get Current Day
     * @return
     */
    public int getDay()
    {
    	return (int)(raider.worldObj.getWorldTime()/24000);
    }
    
    public static int getDay(World worldObj)
    {
    	return (int)(worldObj.getWorldTime()/24000);
    }
    
    /** 
     * Get Current Raid Difficultly
     * 
     * @return
     */
    public int getRaidDifficulty()
    {
    	return ((int)(getDay() / PMSettings.dayDifficultyProgression));
    }
    
    public static int getRaidDifficulty(World worldObj)
    {
    	return ((int)(getDay(worldObj) / PMSettings.dayDifficultyProgression));
    }
    
    
    
    
    public void setSpeedDifficulty(DifficultyInstance difficulty)
    {  
    	if(raider.isChild() || raider.isHeroBrine() || (raider.getRaiderRole() != EnumRaiderRole.NONE || raider.getRaiderRole() != EnumRaiderRole.PYROMANIAC)) return;
    	
    	if(rand.nextDouble() < calculateProgressionDifficulty(speedDifficulty, speedDifficultyMax))
    	{
        	double speed = -0.05 + (.25- (-0.05)) * rand.nextDouble();
            raider.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(EntityRaiderBase.SPEED_BOOST_ID, "Speed Bonus", speed , 0));
    	}
    }
    
    public void setHealthDifficulty(DifficultyInstance difficulty)
    {
    	double healthChance = calculateProgressionDifficulty(healthDifficulty, 0.10D) + (difficulty.getClampedAdditionalDifficulty() * 0.015F);
    	boolean addDefaultHealth = getRaidDifficulty()>= 1;
    	
    	double extraHealth = PMSettings.HealthMaxOut < 0 ? calculateProgressionDifficulty(PMSettings.HealthIncrease) : getRaidDifficulty() >= PMSettings.HealthMaxOut ? PMSettings.HealthIncrease * PMSettings.HealthMaxOut : calculateProgressionDifficulty(PMSettings.HealthIncrease);
    	
        if(this.rand.nextFloat() < healthChance)
        {
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health bonus", this.rand.nextDouble() * 5.0D + 1.00D + extraHealth, 0));
        }
        else if(addDefaultHealth || raider.getRaiderRole() == EnumRaiderRole.WITCH)
        {
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Small Health bonus", this.rand.nextDouble() + 1.00D + extraHealth, 0));
        }
        
    	if(raider.isChild())
    	{
    		raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Child Health", -.75, 2));
    	}
    }
    
    public void setDamageDifficulty(DifficultyInstance difficulty)
    {
    	if(raider.isChild())
    	{
    		raider.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Child Dmg", -.5, 2));
    	}else if(raider.getRaiderRole() == EnumRaiderRole.TWEAKER)
    	{
    		raider.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("Tweaker Dmg", 1, 0));
    	}
    }
    
    

}
