package com.gendeathrow.pmobs.common.capability.player;

import net.minecraft.world.World;

public interface IPlayerData 
{

	public abstract boolean canCallDropPod(World world);
	
	public abstract long geLastDropPodCall();
	
	public abstract void setLastDropPodCall(Long val);
	
	public abstract boolean isBruteSize();
	
	public abstract void setBruteSize(boolean val);
	
}
