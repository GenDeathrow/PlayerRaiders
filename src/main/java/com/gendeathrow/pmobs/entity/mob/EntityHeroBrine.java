package com.gendeathrow.pmobs.entity.mob;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityHeroBrine extends EntityRaiderBase
{

    private static final DataParameter<Boolean> SCREAMING = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    private int lastCreepySound;
    private int targetChangeTime;

    
	public EntityHeroBrine(World worldIn) 
	{
		super(worldIn);

		invulnerable.add(DamageSource.DROWN);
		invulnerable.add(DamageSource.CACTUS);
		invulnerable.add(DamageSource.IN_FIRE);
		invulnerable.add(DamageSource.LAVA);
		invulnerable.add(DamageSource.FALL);
		invulnerable.add(DamageSource.LIGHTNING_BOLT);
		invulnerable.add(DamageSource.ON_FIRE);
		invulnerable.add(DamageSource.IN_FIRE);
		invulnerable.add(DamageSource.WITHER);
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
    
    public void playEndermanSound()
    {
        if (this.ticksExisted >= this.lastCreepySound + 400)
        {
            this.lastCreepySound = this.ticksExisted;

            if (!this.isSilent())
            {
                this.world.playSound(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, SoundEvents.ENTITY_ENDERMEN_STARE, this.getSoundCategory(), 2.5F, 1.0F, false);
            }
        }
    }
    
    @Override
    public void notifyDataManagerChange(DataParameter<?> key)
    {
        if (SCREAMING.equals(key) && this.isScreaming() && this.world.isRemote)
        {
            this.playEndermanSound();
        }

        super.notifyDataManagerChange(key);
    }
    
    @Override
    protected void frostWalk(BlockPos pos)
    {
   		EnchantmentFrostWalker.freezeNearby(this, this.world, pos, 2);
   		coolLavaNearby(this,this.world, pos, 2);
   		
    	super.frostWalk(pos);
    }
    
    public static void coolLavaNearby(EntityLivingBase living, World worldIn, BlockPos pos, int level)
    {
        if (living.onGround)
        {
            float f = (float)Math.min(16, 2 + level);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(0, 0, 0);

            for (BlockPos.MutableBlockPos blockpos$mutableblockpos1 : BlockPos.getAllInBoxMutable(pos.add((double)(-f), -1.0D, (double)(-f)), pos.add((double)f, -1.0D, (double)f)))
            {
                if (blockpos$mutableblockpos1.distanceSqToCenter(living.posX, living.posY, living.posZ) <= (double)(f * f))
                {
                    blockpos$mutableblockpos.setPos(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY() + 1, blockpos$mutableblockpos1.getZ());
                    IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos);

                    if (iblockstate.getMaterial() == Material.AIR)
                    {
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos$mutableblockpos1);

                        if (iblockstate1.getMaterial() == Material.LAVA && ((Integer)iblockstate1.getValue(BlockLiquid.LEVEL)).intValue() == 0 && worldIn.mayPlace(Blocks.OBSIDIAN, blockpos$mutableblockpos1, false, EnumFacing.DOWN, (Entity)null))
                        {
                            worldIn.setBlockState(blockpos$mutableblockpos1, Blocks.OBSIDIAN.getDefaultState());
                            worldIn.scheduleUpdate(blockpos$mutableblockpos1.toImmutable(), Blocks.OBSIDIAN, MathHelper.getInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }
        }
    }
    
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
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

		if(this.rand.nextDouble() <= .75)
		{
			this.dropItem(Items.ENDER_PEARL, 1);
		}

		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
    
	@Override
	public void onLivingUpdate()
	{
		
		if (this.world.isRemote)
		{
			for (int i = 0; i < 2; ++i)
			{
				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
		}
		else if(!this.world.isRemote)
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
    		++this.deathTime;
			this.setInvisible(true);
    		if (this.deathTime == 40)
    		{

    			
    			if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
    			{
    				int i = this.getExperiencePoints(this.attackingPlayer);
    				i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
    				while (i > 0)
    				{	
    					int j = EntityXPOrb.getXPSplit(i);
    					i -= j;
    					this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
    				}
    			}

    			for (int k = 0; k < 20; ++k)
    			{
    				double d2 = this.rand.nextGaussian() * 0.02D;
    				double d0 = this.rand.nextGaussian() * 0.02D;
    				double d1 = this.rand.nextGaussian() * 0.02D;
    				this.world.spawnParticle(EnumParticleTypes.DRAGON_BREATH, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    				this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    				this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1, new int[0]);
    			}
    			


    			
    			this.setDead();
    		}
    	super.onDeathUpdate();
    }

	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
		
        this.targetTasks.addTask(1, new EntityHeroBrine.AIFindPlayer(this));
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SPEED_BOOST_ID);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.35D);
            
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
            
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(60.0D);
            //this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60.0D);

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
        double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.posY + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
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
            this.world.playSound((EntityPlayer)null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
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

    		if(source.getTrueSource() != null && this.rand.nextInt(10) <= 2 && source.getTrueSource() instanceof EntityPlayer)
    		{
    			this.teleportToEntityBack(source.getTrueSource());
    		}
    		else if (source.isUnblockable() && this.rand.nextInt(10) <= 5)
    		{
    			this.teleportRandomly();
    		}

    		return flag;
    	}
    }
    
    List<DamageSource> invulnerable = new ArrayList<DamageSource>();
    
    
    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
    	if(this.invulnerable.contains(source)) return true;
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
            this.player = this.raider.world.getNearestAttackablePlayer(this.raider.posX, this.raider.posY, this.raider.posZ, d0, d0, (Function<EntityPlayer, Double>)null, new Predicate<EntityPlayer>()
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
        public boolean shouldContinueExecuting()
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
                return this.targetEntity != null && ((EntityPlayer)this.targetEntity).isEntityAlive() ? true : super.shouldContinueExecuting();
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
