package com.gendeathrow.pmobs.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

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
        this.setSize(2F, 3.1f);
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
        return !this.isDead;
    }

    int despawnOnGround = 300;
    
    
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.fallTime++ == 0)
        {
        	// on stops?
        }
        
        if(this.onGround)
        {
        	despawnOnGround--;
        	
        	if(despawnOnGround <= 0)
        		this.setDead();
        }
        
        if (!this.hasNoGravity())
        {
            this.motionY -= 0.03999999910593033D;
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (!this.worldObj.isRemote)
        {
            BlockPos blockpos1 = new BlockPos(this);

            if (this.onGround)
            {
                IBlockState iblockstate = this.worldObj.getBlockState(blockpos1);

                if (this.worldObj.isAirBlock(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))) //Forge: Don't indent below.
                if (BlockFalling.canFallThrough(this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.009999999776482582D, this.posZ))))
                {
                    this.onGround = false;
                    return;
                }

                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
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
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.EntityMountEvent(player, this, this.worldObj, true))) return true;

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
            if (!this.worldObj.isRemote)
            {
                player.startRiding(this);
            }

            return true;
        }
    }
    
    
    public void fall(float distance, float damageMultiplier)
    {
        if (this.hurtEntities)
        {
            int i = MathHelper.ceiling_float_int(distance - 1.0F);

            if (i > 0)
            {
                List<Entity> list = Lists.newArrayList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox()));
                DamageSource damagesource = DamageSource.anvil;

                for (Entity entity : list)
                {
                    entity.attackEntityFrom(damagesource, (float)Math.min(MathHelper.floor_float((float)i * this.fallHurtAmount), this.fallHurtMax));
                }

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

	}

}
