package com.gendeathrow.pmobs.entity.New;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.entity.EntityPlayerRaider;

public class EntityWaterRaider extends EntityPlayerRaider
{

	public EntityWaterRaider(World worldIn) 
	{
		super(worldIn);
	}

	@Override
	public boolean canBreatheUnderwater()
	{
		return true;
	}
	
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		
//		EntityBoat boat = new EntityBoat(worldObj, this.posX, this.posY, this.posZ);
//		
//		this.worldObj.spawnEntityInWorld(boat);
//		
//		boat.startRiding(this);
		
		return livingdata;
		
	}
}
