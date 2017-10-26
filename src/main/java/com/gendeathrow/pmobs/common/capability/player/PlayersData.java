package com.gendeathrow.pmobs.common.capability.player;

import net.minecraft.world.World;

public class PlayersData implements IPlayerData
{
	long lastDropTime = 0;

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

}
