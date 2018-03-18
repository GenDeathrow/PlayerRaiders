package com.gendeathrow.pmobs.util;

import java.io.File;

import com.mojang.authlib.GameProfileRepository;

import net.minecraft.server.management.PlayerProfileCache;

public class RaidersProfileCache extends PlayerProfileCache
{

	public RaidersProfileCache(GameProfileRepository profileRepoIn,	File usercacheFileIn) 
	{
		super(profileRepoIn, usercacheFileIn);
	}

	
}
