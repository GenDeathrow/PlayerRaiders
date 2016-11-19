package com.gendeathrow.pmobs.handlers;

import net.minecraft.util.WeightedRandom;

import com.mojang.authlib.GameProfile;

public class RaiderData extends WeightedRandom.Item
{
	private GameProfile gameProfile;
	//private int weight;
	
	public RaiderData(GameProfile profileIn, int weightIn)
	{
		super(weightIn);
		this.gameProfile = profileIn;
	}
	
	public String getOwnerName()
	{
		return this.gameProfile.getName();
	}


	public GameProfile getProfile()
	{
		return this.gameProfile;
	}

	public void setProfile(GameProfile profile)
	{
		gameProfile = profile;
	}
}
