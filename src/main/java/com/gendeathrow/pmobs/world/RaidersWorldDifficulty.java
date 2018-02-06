package com.gendeathrow.pmobs.world;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.world.RaiderClassDifficulty.DiffEntry;

import net.minecraft.world.World;

public class RaidersWorldDifficulty {

	
	
	
	public void IncreaseDifficulty(World world) {
		
		int roll = world.rand.nextInt(3) + 1;
		
		for(int i = 0; i < roll; i++) {
			DiffEntry classDiff = RaiderClassDifficulty.getRandomClass(world);
			
			
		}
			
	}
	
	
	public void readNBT(NBTTagCompound tag) {
		
	}
	
	
	public NBTTagCompound writeNBT(NBTTagCompound tag) {
		return tag;
	}
	
    /** 
     * Get Current Day
     * @return
     */
    public static int getDay(World world)
    {
    	return (int)(world.getWorldTime()/24000);
    }
    
    /** 
     * Get Current Raid Difficultly
     * 
     * @return
     */
    public static int getRaidDifficulty(World worldObj)
    {
    	return ((int)(getDay(worldObj) / PMSettings.dayDifficultyProgression));
    }
    

}
