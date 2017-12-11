package com.gendeathrow.pmobs.entity.neutral;

import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityLaserBeam extends EntityThrowable{

	public EntityLaserBeam(World worldIn) {
		super(worldIn);
		this.setSize(0.0625F, 0.0625F);
	}

	@Override
	protected void onImpact(RayTraceResult result) {

		
	}

}
