package com.gendeathrow.pmobs.handlers;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class DifficultyProgression 
{
	
	public static double speedDifficulty = .015;
		public static double speedDifficultyMax = .75;
		public static double speedMIN = 0.1;
		public static double speedMAX = .5;
		
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
    
    
    /** 
     * Get Current Raid Difficultly
     * 
     * @return
     */
    public int getRaidDifficulty()
    {
    	return ((int)(getDay() / PMSettings.dayDifficultyProgression));
    }
    
    public void setSpeedDifficulty(DifficultyInstance difficulty)
    {  
    	if(rand.nextDouble() < calculateProgressionDifficulty(speedDifficulty, speedDifficultyMax))
    	{
        	double speed = -0.01 + (.25 - (-0.01)) * rand.nextDouble();
            raider.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier(EntityRaiderBase.SPEED_BOOST_ID, "Speed Bonus", speed , 0));
    	}
    }
    
    public void setHealthDifficulty(DifficultyInstance difficulty)
    {
    	double healthChance = calculateProgressionDifficulty(healthDifficulty, 1.00D);
    	boolean addDefaultHealth = getRaidDifficulty()>= 1;
    	
        if (this.rand.nextFloat() < difficulty.getClampedAdditionalDifficulty() * 0.025F)
        {
            raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader bonus", this.rand.nextDouble() * 4.0D + 1.0D + calculateProgressionDifficulty(.10), 2));
        }
        else if(this.rand.nextFloat() < healthChance)
        {
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Health bonus", this.rand.nextDouble() * 2.0D + 1.00D + calculateProgressionDifficulty(.1), 2));
        }
        else if(addDefaultHealth)
        {
        	raider.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Small Health bonus", this.rand.nextDouble() + 1.00D + calculateProgressionDifficulty(.10), 2));
        }
    }
    
    public void setDamageDifficulty(DifficultyInstance difficulty)
    {
    	
    }
    
    

}
