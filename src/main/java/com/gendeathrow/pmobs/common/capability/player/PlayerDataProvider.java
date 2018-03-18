package com.gendeathrow.pmobs.common.capability.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerDataProvider implements ICapabilitySerializable<NBTBase>
{
	
    @CapabilityInject(IPlayerData.class)
    public static final Capability<IPlayerData> PLAYERDATA = null;
    
    private IPlayerData instance = PLAYERDATA.getDefaultInstance();
	
    
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayersDataStorage(), PlayersData.class);
	}

	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		  return capability == PLAYERDATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		 return capability == PLAYERDATA ? PLAYERDATA.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT()   
	{
		return PLAYERDATA.getStorage().writeNBT(PLAYERDATA, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) 
	{
		PLAYERDATA.getStorage().readNBT(PLAYERDATA, this.instance, null, nbt);
	}



}
