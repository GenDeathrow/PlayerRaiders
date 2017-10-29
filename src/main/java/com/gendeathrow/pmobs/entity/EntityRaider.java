package com.gendeathrow.pmobs.entity;

import java.util.UUID;

import net.minecraft.world.World;

public class EntityRaider extends EntityRaiderBase{

	public static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	
	public EntityRaider(World worldIn) {
		super(worldIn);
	}

}
