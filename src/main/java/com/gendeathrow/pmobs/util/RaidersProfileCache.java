package com.gendeathrow.pmobs.util;

import java.io.File;
import java.util.Date;
import java.util.Locale;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;

import net.minecraft.server.management.PlayerProfileCache;

public class RaidersProfileCache extends PlayerProfileCache
{

	public RaidersProfileCache(GameProfileRepository profileRepoIn,	File usercacheFileIn) 
	{
		super(profileRepoIn, usercacheFileIn);
	}

	
}
