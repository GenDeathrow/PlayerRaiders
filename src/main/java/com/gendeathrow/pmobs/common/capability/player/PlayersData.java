package com.gendeathrow.pmobs.common.capability.player;

import net.minecraft.world.World;

public class PlayersData implements IPlayerData
{
	long lastDropTime = 0;
	boolean isBruteSize = false;

	@Override
	public boolean canCallDropPod(World world) 
	{
		return (world.getWorldTime() - this.geLastDropPodCall() > 24000);
	}

	@Override
	public long geLastDropPodCall() 
	{
		return lastDropTime ;
	}

	@Override
	public void setLastDropPodCall(Long val) 
	{
		this.lastDropTime = val;
	}

	@Override
	public boolean isBruteSize() {

		return this.isBruteSize;
	}

	@Override
	public void setBruteSize(boolean val) {
		this.isBruteSize = val;
	}

}
