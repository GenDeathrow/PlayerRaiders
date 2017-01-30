package com.gendeathrow.pmobs.entity.New;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class EntityHeroBrine extends EntityRaiderBase
{

    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    private int lastCreepySound;
    private int targetChangeTime;

    
	public EntityHeroBrine(World worldIn) 
	{
		super(worldIn);

		invulnerable.add(DamageSource.drown);
		invulnerable.add(DamageSource.cactus);
		invulnerable.add(DamageSource.inFire);
		invulnerable.add(DamageSource.lava);
		invulnerable.add(DamageSource.fall);
		invulnerable.add(DamageSource.lightningBolt);
		invulnerable.add(DamageSource.onFire);
		invulnerable.add(DamageSource.inFire);
		invulnerable.add(DamageSource.wither);
	}
	
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(SCREAMING, Boolean.valueOf(false));
    }
    
    @Override
    protected void initEntityAI()
    {
    	
        super.initEntityAI();
    }
    
    @Override
    public boolean isHeroBrine()
    {
    	return this.getOwner().toLowerCase().equals("herobrine");
    }
	
    public void playEndermanSound()
    {
        if (this.ticksExisted >= this.lastCreepySound + 400)
        {
            this.lastCreepySound = this.ticksExisted;

            if (!this.isSilent())
            {
                this.worldObj.playSound(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }
    
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SCREAMING.equals(key) && this.isScreaming() && this.worldObj.isRemote &&  isHeroBrine())
        {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }
    
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
        	ItemStack itemstack = player.inventory.armorInventory[3];

            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double)this.getEyeHeight() - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
    }
    
    
	@Override
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{

		if(this.isHeroBrine() && this.rand.nextDouble() <= .75)
		{
			this.dropItem(Items.ENDER_PEARL, 1);
		}

		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
    
	@Override
	public void onLivingUpdate()
	{
		
		if (this.worldObj.isRemote && isHeroBrine())
		{
			for (int i = 0; i < 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
		}
		else if(!this.worldObj.isRemote && isHeroBrine())
		{
			if(this.getActivePotionEffects().size() > 0)
			{
				this.clearActivePotions();
			}
			
		}

		super.onLivingUpdate();
	}
	
	
    protected void onDeathUpdate()
    {
    	
    	if(this.isHeroBrine())
    	{
    		++this.deathTime;
			this.setInvisible(true);
    		if (this.deathTime == 40)
    		{

    			
    			if (!this.worldObj.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.worldObj.getGameRules().getBoolean("doMobLoot")))
    			{
    				int i = this.getExperiencePoints(this.attackingPlayer);
    				i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
    				while (i > 0)
    				{	
    					int j = EntityXPOrb.getXPSplit(i);
    					i -= j;
    					this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
    				}
    			}

    			for (int k = 0; k < 20; ++k)
    			{
    				double d2 = this.rand.nextGaussian() * 0.02D;
    				double d0 = this.rand.nextGaussian() * 0.02D;
    				double d1 = this.rand.nextGaussian() * 0.02D;
    				this.worldObj.spawnParticle(EnumParticleTypes.DRAGON_BREATH, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    			}
    			


    			
    			this.setDead();
    		}
    	}
    	else super.onDeathUpdate();
    }

	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
		
        
        if(this.isHeroBrine()) 	
       	{
        	this.targetTasks.addTask(1, new EntityHeroBrine.AIFindPlayer(this));
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(BABY_SPEED_BOOST_ID);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_BOOST_ID);
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.35D);
            
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
            
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
            //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);
            
            if(((EntityRangedAttacker)this).isRangedAttacker)
            {
            	this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, null);
            	this.tasks.removeTask(((EntityRangedAttacker)this).aiArrowAttack);
            	
            	((EntityRangedAttacker)this).isRangedAttacker = false;
            }
       	}
        
		return livingdata;
    }
	
    protected boolean teleportRandomly()
    {
        double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = this.posY + (double)(this.rand.nextInt(64) - 32);
        double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
        return this.teleportTo(d0, d1, d2);
    }
    
    protected boolean teleportToEntity(Entity p_70816_1_)
    {
        Vec3d vec3d = new Vec3d(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - p_70816_1_.posY + (double)p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.xCoord * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.yCoord * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.zCoord * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }
    
    protected boolean teleportToEntityBack(Entity p_70816_1_)
    {
    	EnumFacing facingEnum = p_70816_1_.getHorizontalFacing();
    	Vec3i facing = facingEnum.getDirectionVec();
    	double dx = p_70816_1_.posX - (facing.getX() * 2);
    	double dy = p_70816_1_.posY + p_70816_1_.getEyeHeight(); // you probably don't actually want to subtract the vec.yCoord, unless the position depends on the player's pitch
    	double dz = p_70816_1_.posZ - (facing.getY() * 2);
    	
        return this.teleportTo(dx, dy, dz);
    }
    
    private boolean teleportTo(double x, double y, double z)
    {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag)
        {
            this.worldObj.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
    }
    
    protected SoundEvent getAmbientSound()
    {
        return this.isScreaming() ? SoundEvents.ENTITY_ENDERMEN_SCREAM : super.getAmbientSound();
    }
    
    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	if(isHeroBrine())
    	{
    		if (this.isEntityInvulnerable(source))
    		{
    			return false;
    		}
    		else if (source instanceof EntityDamageSourceIndirect)
    		{
    			for (int i = 0; i < 64; ++i)
    			{
    				
    				if (this.teleportRandomly())
    				{
    					return true;
    				}
    			}

    			return false;
    		}
    		else
    		{
    			boolean flag = super.attackEntityFrom(source, amount);

				if(source.getEntity() != null && this.rand.nextInt(10) <= 2 && source.getEntity() instanceof EntityPlayer)
				{
					this.teleportToEntityBack(source.getEntity());
				}
				else if (source.isUnblockable() && this.rand.nextInt(10) <= 5)
    			{
    				this.teleportRandomly();
    			}

    			return flag;
    		}
    	}else return super.attackEntityFrom(source, amount);
    }
    
    List<DamageSource> invulnerable = new ArrayList<DamageSource>();
    
    
    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
    	if(this.isHeroBrine()) 
    	{
    		if(this.invulnerable.contains(source)) return true;
    	}
    		
    		
       return super.isEntityInvulnerable(source);
    }

    
    public boolean isScreaming()
    {
        return ((Boolean)this.dataManager.get(SCREAMING)).booleanValue();
    }
    
    static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer>
    {
        private final EntityHeroBrine raider;
        /** The player */
        private EntityPlayer player;
        private int aggroTime;
        private int teleportTime;

        public AIFindPlayer(EntityHeroBrine p_i45842_1_)
        {
            super(p_i45842_1_, EntityPlayer.class, false);
            this.raider = p_i45842_1_;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            double d0 = this.getTargetDistance();
            this.player = this.raider.worldObj.getNearestAttackablePlayer(this.raider.posX, this.raider.posY, this.raider.posZ, d0, d0, (Function<EntityPlayer, Double>)null, new Predicate<EntityPlayer>()
            {
                public boolean apply(@Nullable EntityPlayer p_apply_1_)
                {
                    return p_apply_1_ != null && AIFindPlayer.this.raider.shouldAttackPlayer(p_apply_1_);
                }
            });
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            this.player = null;
            super.resetTask();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            if (this.player != null)
            {
                if (!this.raider.shouldAttackPlayer(this.player))
                {
                    return false;
                }
                else
                {
                    this.raider.faceEntity(this.player, 10.0F, 10.0F);
                    return true;
                }
            }
            else
            {
                return this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive() ? true : super.continueExecuting();
            }
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            if (this.player != null)
            {
                if (--this.aggroTime <= 0)
                {
                    this.targetEntity = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            }
            else
            {
                if (this.targetEntity != null)
                {

                    if (this.raider.shouldAttackPlayer((EntityPlayer)this.targetEntity))
                    {
                        if (((EntityPlayer)this.targetEntity).getDistanceSqToEntity(this.raider) < 25.0D && this.teleportTime >= 40)
                        {
                        	if(this.raider.rand.nextDouble() < 40)	
                        	{
                        		
                    			for (int i = 0; i < 64; ++i)
                    			{
                    				if(this.raider.teleportToEntityBack((EntityPlayer)this.targetEntity)) break;
                    			}

                        	}
                        	else this.raider.teleportRandomly();
                        	
                        	this.teleportTime = 0;
                        }

                        
                    }
                    else if (((EntityPlayer)this.targetEntity).getDistanceSqToEntity(this.raider) > 128.0D && this.teleportTime >= 30 && this.raider.teleportToEntity(this.targetEntity))
                    {
                    	if(this.raider.rand.nextDouble() < 40)	
                    	{
                				this.raider.teleportToEntityBack((EntityPlayer)this.targetEntity); 
                    	}
                    	else this.raider.teleportToEntity((EntityPlayer)this.targetEntity);

                    	
                        this.teleportTime = 0;
                    }
                    
                    
                	this.teleportTime++;
                }

                super.updateTask();
            }
        }
    }

}
