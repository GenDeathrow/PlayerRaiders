package com.gendeathrow.pmobs.handlers;

import com.mojang.authlib.GameProfile;

import net.minecraft.util.WeightedRandom;

public class RaiderData extends WeightedRandom.Item
{
	private GameProfile gameProfile;
	private boolean hasUpdated = false;
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

	public void setProfileUpdated() {
		hasUpdated = true;
	}

	public GameProfile getProfile()
	{
		return this.gameProfile;
	}

	public void setProfile(GameProfile profile)
	{
		gameProfile = profile;
	}

	public boolean hasUpdatedProfile() {
		return hasUpdated;
	}
}
