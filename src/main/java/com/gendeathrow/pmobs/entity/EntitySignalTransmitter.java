package com.gendeathrow.pmobs.entity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySignalTransmitter extends Entity
{


	public int animationTicks;
	public int prevAnimationTicks;
	
	
	public int nextCall = new Random().nextInt(100)+200;
	
	public EntitySignalTransmitter(World worldIn) 
	{
		super(worldIn);
		this.setSize(1F, 1.3f);
	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
	}
	
    public boolean canBeCollidedWith()
    {
        return true;
    }
    
    public void onKillCommand()
    {
        super.onKillCommand();
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            if (!this.isDead && !this.worldObj.isRemote)
            {
                this.setDead();

                if (!this.worldObj.isRemote)
                {
                    this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, .5F, true);
                }
            }

            return true;
        }
    }
    
	@Override
	public void onUpdate() 
	{
		this.setGlowing(true);
		
		if (worldObj.isRemote) 
		{
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 5;
			if (animationTicks >= 360) 
			{
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}
		else
		{
			if(this.nextCall-- <= 0)
			{
				this.onCallDropPods();
				
				this.nextCall = this.rand.nextInt(200) + 100;

			}
			
			
		}
		
		
		super.onUpdate();
	}

	
	public void onCallDropPods()
	{
		System.out.println("Call");

		int randX = this.rand.nextInt(18) - 9;
		int randZ = this.rand.nextInt(18) - 9;
		

		EntityDropPod droppod = new EntityDropPod(this.worldObj);
		droppod.setPosition(this.posX + randX, 300, this.posZ + randZ);
		this.worldObj.spawnEntityInWorld(droppod);		


		EntityLiving raider = new EntityPlayerRaider(this.worldObj);
		raider.onInitialSpawn(this.worldObj.getDifficultyForLocation(this.getPosition()),  (IEntityLivingData)null);
		raider.hurtResistantTime = 60;
		raider.setLocationAndAngles(droppod.posX, droppod.posY, droppod.posZ,raider.rotationYaw, 0.0F);
		
		System.out.println(this.worldObj.spawnEntityInWorld(raider));	
		System.out.println(raider.getPosition().toString());

		raider.startRiding(droppod, true);
	}
}
