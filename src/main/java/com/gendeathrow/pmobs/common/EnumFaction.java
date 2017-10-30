package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.EntityRaiderBase;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;

//Factions Enum Class 
public enum EnumFaction
{
	HOSTILE,
	NEUTRAL,
	FRIENDLY;
			
	EnumFaction() { 	}
			
	public static EnumFaction get(int intValue) 
	{
		for(EnumFaction role : EnumFaction.values())
		{
			if (role.ordinal() == intValue) return role;
		}
		return null;
	}

	public static EnumFaction getRandomFaction(EntityRaiderBase raider, DifficultyProgression manager)
	{
		double randval = raider.getRNG().nextDouble();
					
		if(randval < .9 || !PMSettings.factionsEnabled)
		{
			return HOSTILE;
		}
		else
		{
			return FRIENDLY;
		}
	}
}