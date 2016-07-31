package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIStealItem extends EntityAIMoveToBlock
{

	public EntityAIStealItem(EntityCreature creature, double speedIn, int length) 
	{
		super(creature, speedIn, length);
	}

	@Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos) 
	{
		return false;
	}

}
