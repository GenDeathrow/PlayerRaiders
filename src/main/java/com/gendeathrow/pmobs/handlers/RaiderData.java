package com.gendeathrow.pmobs.handlers;

import com.mojang.authlib.GameProfile;

import net.minecraft.util.WeightedRandom;

public class RaiderData extends WeightedRandom.Item
{
	private GameProfile gameProfile;
	private boolean hasUpdated = false;
	private boolean isEnabled = true;
	private String comment = " ";
	//private int weight;
	
	public RaiderData(GameProfile profileIn, int weightIn, boolean isEnabledIn)
	{
		this(profileIn, weightIn);
		this.isEnabled = isEnabledIn;
	}
	
	public RaiderData(GameProfile profileIn, int weightIn)
	{
		super(weightIn);
		this.gameProfile = profileIn;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String commentIn) {
		this.comment = commentIn;
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
