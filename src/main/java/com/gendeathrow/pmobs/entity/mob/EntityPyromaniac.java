package com.gendeathrow.pmobs.entity.mob;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.init.RaidersSoundEvents;
import com.gendeathrow.pmobs.core.init.RegisterEntities;
import com.gendeathrow.pmobs.entity.ai.EntityAIPyromaniac;
import com.gendeathrow.pmobs.world.RaidersWorldDifficulty;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityPyromaniac extends EntityRaiderBase{

	public EntityPyromaniac(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		
		this.tasks.addTask(1, new EntityAIPyromaniac(this, 2D));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        
	    this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, true));
	}
	

    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
    		
	        if(!this.isChild() && PMSettings.leapAttackAI && rand.nextDouble() < .15 + difficultyManager.calculateProgressionDifficulty(.05, .35))
	        	this.setLeapAttack(true);  
	        
    		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));
    		
    		return livingdata;
    }
    
	/**
	 * Called to update the entity's position/logic.
	 */
    @Override
	public void onUpdate()
	{
		lastBurnTick++;
		super.onUpdate();
	}
    
    @Override
    public boolean canSpawnAtDifficulty() {
    	return RaidersWorldDifficulty.calculateRaidDifficulty(this.world) >= PMSettings.pyroStartDiff;
    };
    
    
	
	private long lastBurnTick = 600;
	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean hasHitTarget = super.attackEntityAsMob(entityIn);

		if (hasHitTarget)
		{
			int i = this.world.getDifficulty().getDifficultyId();
			
			if(lastBurnTick > 600 && !this.getHeldItemOffhand().isEmpty() && this.getHeldItemOffhand().getItem() == Items.FLINT_AND_STEEL && this.rand.nextFloat() < (float)i * 0.3F )
			{
				this.swingArm(EnumHand.OFF_HAND);
    			this.world.playSound(null, entityIn.getPosition(), SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
    			this.world.playSound(null, this.getPosition(), RaidersSoundEvents.RAIDERS_LAUGH, SoundCategory.HOSTILE, 1.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
				entityIn.setFire(2 * i);
				lastBurnTick = 0;
			}
		}
		
		return hasHitTarget;
	}
	
	@Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }
    
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return RegisterEntities.pyromaniacLoot;
    }
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		if(this.getRNG().nextDouble() < .45) return RaidersSoundEvents.RAIDERS_LAUGH;
		else return super.getAmbientSound();
	}
}
