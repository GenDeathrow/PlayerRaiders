package com.gendeathrow.pmobs.entity;

import net.minecraft.world.World;

public class EntityRaiderWitch extends EntityRaiderBase{

	public EntityRaiderWitch(World worldIn) {
		super(worldIn);
	}

	public boolean isWitchActive() {
		return false;
	}

}
