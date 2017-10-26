package com.gendeathrow.pmobs.entity;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySignalTransmitter extends Entity implements IWorldNameable
{  
  
	private static final DataParameter<Float> DAMAGE = EntityDataManager.<Float>createKey(EntitySignalTransmitter.class, DataSerializers.FLOAT);
	   	   
	public int animationTicks;
	public int prevAnimationTicks;
    private AbstractAttributeMap attributeMap;	
	
	public int nextCall = new Random().nextInt(100)+200;
	
	public EntitySignalTransmitter(World worldIn) 
	{
		super(worldIn);
		this.setSize(1F, 1.4f);
	}

	@Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return entityIn.getEntityBoundingBox();
    }
	
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox()
    {

        return this.getEntityBoundingBox();
    }

    public boolean canBePushed()
    {
        return false;
    }
    
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }
    
	@Override
	protected void entityInit() 
	{
        this.dataManager.register(DAMAGE, Float.valueOf(0.0F));
	}
    
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
	}
    
    public void onKillCommand()
    {
        super.onKillCommand();
    }

    
    @Override
//	public boolean attackEntityFrom(DamageSource source, float amount)
//    {
//        if (!this.worldObj.isRemote && !this.isDead)
//        {
//            if (this.isEntityInvulnerable(source))
//            {
//                return false;
//            }
//            else
//            {
//   
//                this.setBeenAttacked();
//                this.setDamage(this.getDamage() + amount * 10.0F);
//                boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
//
//                if (flag || this.getDamage() > 40.0F)
//                {
//                    this.removePassengers();
//
//                    if (flag && !this.hasCustomName())
//                    {
//                        this.setDead();
//                    }
//                    else
//                    {
//                        this.killMinecart(source);
//                    }
//                }
//
//                return true;
//            }
//        }
//        else
//        {
//            return true;
//        }
//    }
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.isDead && !this.worldObj.isRemote)
        {
        	
        	if (this.isEntityInvulnerable(source))
        	{
        		return false;
        	}
        	else
        	{

        		this.setBeenAttacked();
        		this.playSound(net.minecraft.init.SoundEvents.BLOCK_ANVIL_HIT, 1f, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
        		
        		this.setDamage(this.getDamage() + amount);
        		boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

        		if (flag || this.getDamage() > 50.0F)
        		{
                    this.killTransmitter(source);
        		}
        		return true;   	
            }

            
        }
    	return true;
    }
    
    
    public void killTransmitter(DamageSource source)
    {
        this.setDead();

        this.worldObj.createExplosion((Entity)null, this.posX, this.posY, this.posZ, .5F, true);

        if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
        {
//            ItemStack itemstack = new ItemStack(Items.MINECART, 1);
//
//            if (this.getName() != null)
//            {
//                itemstack.setStackDisplayName(this.getName());
//            }
//
//            this.entityDropItem(itemstack, 0.0F);
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
			if(this.nextCall-- < 8)
			{
				if(this.nextPod >= this.podMax)
				{
					this.nextCall = this.rand.nextInt(600) + 800;
					this.nextPod = 0;
				}
				else 
				{
					this.nextPod++;
					this.nextCall = this.rand.nextInt(10) + 5;
				}
				
				this.onCallDropPods();

			}
			
			
		}
		super.onUpdate();
	}


	private int nextPod = 0;
	private int podMax = 4;
	
	public void onCallDropPods()
	{
		int range = 60;
		
		int randX = this.rand.nextInt(range) - (range/2);
		int randZ = this.rand.nextInt(range) - (range/2);
		
		randX = randX > 0 ? MathHelper.clamp_int(randX, 5, (range/2)) : MathHelper.clamp_int(randX, -(range/2), -5);
		randZ = randZ > 0 ? MathHelper.clamp_int(randZ, 5, (range/2)) : MathHelper.clamp_int(randZ, -(range/2), -5);
		
//		System.out.println(randX +":"+ randZ);
		IBlockState ground = this.worldObj.getGroundAboveSeaLevel(new BlockPos(this.posX + randX, 300, this.posZ + randZ));
		
		
		EntityDropPod droppod = new EntityDropPod(this.worldObj);
		droppod.setPosition(this.posX + randX, 300, this.posZ + randZ);
		this.worldObj.spawnEntityInWorld(droppod);
		droppod.playSound(com.gendeathrow.pmobs.common.SoundEvents.SONIC_BOOM, 17, 1f);


		EntityLiving raider = new EntityPlayerRaider(this.worldObj);
		raider.onInitialSpawn(this.worldObj.getDifficultyForLocation(this.getPosition()),  (IEntityLivingData)null);
		raider.hurtResistantTime = 60;
		raider.setLocationAndAngles(droppod.posX, droppod.posY, droppod.posZ, raider.rotationYaw, 0.0F);
		//raider.enablePersistence();
		
		this.worldObj.spawnEntityInWorld(raider);	
		//System.out.println(raider.getPosition().toString());

		raider.startRiding(droppod, true);
	}
	
    public float getDamage()
    {
        return ((Float)this.dataManager.get(DAMAGE)).floatValue();
    }

    public void setDamage(float damage)
    {
        this.dataManager.set(DAMAGE, Float.valueOf(damage));
    }


    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
//        this.setRollingDirection(-this.getRollingDirection());
//        this.setRollingAmplitude(10);
//       this.setDamage(this.getDamage());
    }
    


}
