package com.gendeathrow.pmobs.entity;

import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityTweaker extends EntityRaiderBase{

    private static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);

	
	public EntityTweaker(World worldIn) {
		super(worldIn);
	}

	
	public void setArmsRaised(boolean armsRaised)
	{
		this.getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
	}

	public boolean isArmsRaised()
	{
		return ((Boolean)this.getDataManager().get(ARMS_RAISED)).booleanValue();
	}
  
}
