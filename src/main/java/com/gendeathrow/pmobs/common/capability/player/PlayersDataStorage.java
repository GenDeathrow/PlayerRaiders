package com.gendeathrow.pmobs.common.capability.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayersDataStorage implements IStorage<IPlayerData>
{
	private String lastDropPod = "lastDropPod";
	private String isBruteSize = "isBruteSize";
	
    @Override
    public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side)
    {
    	NBTTagCompound nbt = new NBTTagCompound();
    	
    	nbt.setLong(lastDropPod, instance.geLastDropPodCall());
    	nbt.setBoolean(isBruteSize, instance.isBruteSize());
    	
    	
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt)
    {
    	NBTTagCompound compound = (NBTTagCompound) nbt;
    	
         instance.setLastDropPodCall(compound.getLong(lastDropPod));  
         instance.setBruteSize(compound.getBoolean(isBruteSize));
    }
}
