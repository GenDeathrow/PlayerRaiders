package com.gendeathrow.pmobs.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EntityDropPod extends Entity
{
    public int fallTime;
    private boolean hurtEntities;
    private int fallHurtMax = 200;
    private float fallHurtAmount = 8.0F;

	public EntityDropPod(World worldIn) 
	{
		super(worldIn);
		
        this.preventEntitySpawning = true;
        this.setSize(1.8F, 3.3f);
	}

	@Override
	protected void entityInit() 
	{
	}


    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.getEntityBoundingBox();
    }

    /**
     * Returns the collision bounding box for this entity
     */
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox()
    {
    	return this.getEntityBoundingBox();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    
    public double getMountedYOffset()
    {
        return 0.0D;
    }

    @Override
    public boolean canBeCollidedWith()
    {
    	
        return !this.isDead || !this.isAirBorne;
    }

    int despawnOnGround = 300;
    
    boolean soundDrop = false;
    
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    	
        if(this.fallDistance > 10)
    	{
        	soundDrop = true;

    	}
        
        if(this.onGround)
        {
        	despawnOnGround--;
        	
        	if(despawnOnGround <= 0)
        		this.setDead();
        	
        	if(!this.getPassengers().isEmpty())
        	{
        		for(Entity passenger :this.getPassengers())
        		{
        			passenger.dismountRidingEntity();
        		}
        	}
        	
        	if(this.soundDrop)
        	{
        		this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 5F, 1F);
        		this.soundDrop = false;
        	}
        }
        
        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (!this.world.isRemote)
        {
            
          	this.destroyBlocksInAABB(this.getCollisionBoundingBox().expand(2, 2, 2));
          	
            BlockPos blockpos1 = new BlockPos(this);

            if (this.onGround)
            {
                IBlockState iblockstate = this.world.getBlockState(blockpos1);

                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
            }
            else
            {
            	this.HitEntitiesFalling(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(.25, .25, .25)));
            }

            
            
        }
        
    	super.onUpdate();
    }
    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }
    
      
    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_)
    {
        int i = MathHelper.floor(p_70972_1_.minX);
        int j = MathHelper.floor(p_70972_1_.minY);
        int k = MathHelper.floor(p_70972_1_.minZ);
        int l = MathHelper.floor(p_70972_1_.maxX);
        int i1 = MathHelper.floor(p_70972_1_.maxY);
        int j1 = MathHelper.floor(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag1 = false;

        for (int k1 = i; k1 <= l; ++k1)
        {
            for (int l1 = j; l1 <= i1; ++l1)
            {
                for (int i2 = k; i2 <= j1; ++i2)
                {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();

                    if (!block.isAir(iblockstate, this.world, blockpos) && iblockstate.getMaterial() != Material.FIRE)
                    {
                    	if (block.canEntityDestroy(iblockstate, this.world, blockpos, this))
                        {
                    		
                            if (block.isReplaceable(this.world, blockpos) || block.isLeaves(iblockstate, this.world, blockpos) || block instanceof BlockLeaves || iblockstate.getMaterial() == Material.VINE)
                            {
                            	if(!BlockFalling.canFallThrough(iblockstate))
                            		flag = this.world.setBlockToAir(blockpos);
                            }
                            else
                            {
                                flag = true;
                            }
                        }
                        else
                        {
                            flag = true;
                        }
                    }
                }
            }
        }
		return flag;
    }

    
    @Override
    public boolean canBeRidden(Entity entityIn)
    {
        return true;
    }
    
    protected double getMaximumSpeed()
    {
        return 0.4D;
    }
    
    
    public boolean processInitialInteract(EntityPlayer player, @Nullable ItemStack stack, EnumHand hand)
    {
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityMountEvent(player, this, this.world, true))) return true;

        if (player.isSneaking())
        {
            return false;
        }
        else if (this.isBeingRidden())
        {
            return true;
        }
        else
        {
            if (!this.world.isRemote)
            {
                player.startRiding(this);
            }

            return true;
        }
    }
    
    @Override
    public void fall(float distance, float damageMultiplier)
    {
            int i = MathHelper.ceil(distance - 1.0F);

            if (i > 0)
            {
                List<Entity> list = Lists.newArrayList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                DamageSource damagesource = DamageSource.anvil;

                for (Entity entity : list)
                {
                		damageHitEntity(entity, i);
                }

            }
    }

    private void damageHitEntity(Entity entity, float distance)
    {
        DamageSource damagesource = DamageSource.anvil;
        
        int i = MathHelper.ceil(distance - 1.0F);

        	if(entity instanceof EntityDropPod)
        		entity.setDead();
        	else if(!this.isRidingOrBeingRiddenBy(entity))
        	{
        		entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax));
        	}
        		
        	
    }
    
	private void HitEntitiesFalling(List<Entity> p_70970_1_)
	{
		double d0 = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0D;
		double d1 = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0D;

		for (Entity entity : p_70970_1_)
		{
			if (entity instanceof EntityLivingBase)
			{
				double d2 = entity.posX - d0;
				double d3 = entity.posZ - d1;
				double d4 = d2 * d2 + d3 * d3;
				entity.addVelocity(d2 / d4 * 2D, 0.0D, d3 / d4 * 2D);
				
				damageHitEntity(entity, this.fallDistance);
			}
		}
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
        compound.setInteger("Time", this.fallTime);
        compound.setBoolean("HurtEntities", this.hurtEntities);
        compound.setFloat("FallHurtAmount", this.fallHurtAmount);
        compound.setInteger("FallHurtMax", this.fallHurtMax);
	}
	
	
	public static ArrayList<CalledDrop> dropPodqueue = new ArrayList<CalledDrop>();
	
	public int nextCall = 10;
	private int nextPod = 0;
	private int podMax = 4;
	private boolean spawned = false;
	private boolean spawning = false;
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		
		if(event.side == Side.SERVER)
		{
			
			if(event.phase == Phase.END && event.type == Type.WORLD)
			{
				if(dropPodqueue.size() > 0)
				{

					CalledDrop call = dropPodqueue.get(0);
					
					if(call == null || call.raider == null || call.dropPod == null || call.raider.world == null) return;
					if(event.world == call.raider.world && this.nextCall-- < 0)
					{
						event.world.spawnEntity(call.dropPod);
						event.world.spawnEntity(call.raider);
						call.raider.startRiding(call.dropPod);
					
						call.dropPod.playSound(com.gendeathrow.pmobs.common.SoundEvents.SONIC_BOOM,25, 1f);
					
						this.nextCall = event.world.rand.nextInt(10) + 5;
						spawned = true;
						dropPodqueue.remove(0);
						spawned = false;
					}
				}
			}
		}
	}
	
	public static void addDropPodtoQueue(EntityLivingBase rider, EntityDropPod dropPod)
	{
		CalledDrop drop = new CalledDrop(rider, dropPod);
		
		dropPodqueue.add(drop);
		
		//System.out.println("size "+ dropPodqueue.size());
	}
	
	
	public static class CalledDrop
	{
		EntityLivingBase raider;
		EntityDropPod dropPod;
		
		public CalledDrop(EntityLivingBase raider, EntityDropPod dropPod)
		{
			this.raider = raider;
			this.dropPod = dropPod;
		}
	}

}
