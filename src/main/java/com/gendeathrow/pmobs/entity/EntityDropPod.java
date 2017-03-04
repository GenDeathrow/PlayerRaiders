package com.gendeathrow.pmobs.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityDropPod extends Entity
{

	public EntityDropPod(World worldIn) 
	{
		super(worldIn);
		
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
	}

	@Override
	protected void entityInit() 
	{
	}

    public double getMountedYOffset()
    {
        return 0.0D;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    
    
    public void onUpdate()
    {
    	super.onUpdate();
    }
    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }
    
    
    protected double getMaximumSpeed()
    {
        return 0.4D;
    }
    
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{

	}

}
