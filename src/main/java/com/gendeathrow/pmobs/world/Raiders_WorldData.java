package com.gendeathrow.pmobs.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class Raiders_WorldData extends WorldSavedData 
{
	private static final String IDENTIFIER = "raiders_data";
	
	public static Raiders_WorldData INSTANCE;
	
	private int lastHerobrineSpotted = 0;
	
	private static boolean IS_GLOBAL = true;
	
	private String HEROBRINEID = "lastHerobrineSpotted";
	 
	public Raiders_WorldData(String NAME) 
	{
		super(NAME);
	}

	public int getLastHerobrineSighting()
	{
		return lastHerobrineSpotted;
	}
	
	public void setLastHerobrineSighting(int day)
	{
		lastHerobrineSpotted = day;
		this.markDirty();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		lastHerobrineSpotted = nbt.getInteger(HEROBRINEID);
		RaiderClassDifficulty.readNBT(nbt);
		RaidersWorldDifficulty.INSTANCE.writeNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		compound.setInteger(HEROBRINEID, lastHerobrineSpotted);
		RaiderClassDifficulty.writeNBT(compound);
		RaidersWorldDifficulty.INSTANCE.writeNBT(compound);
		return compound;
	}
	
	public Raiders_WorldData getEMWorldData()
	{
		return INSTANCE;
	}
	
	public static Raiders_WorldData get(World world) 
	{
		MapStorage storage = IS_GLOBAL ? world.getMapStorage() : world.getPerWorldStorage();
		INSTANCE = (Raiders_WorldData) storage.getOrLoadData(Raiders_WorldData.class, IDENTIFIER);

		  if (INSTANCE == null) {
			  INSTANCE = new Raiders_WorldData(IDENTIFIER);
		    storage.setData(IDENTIFIER, INSTANCE);
		  }
		  return INSTANCE;
	}

}
