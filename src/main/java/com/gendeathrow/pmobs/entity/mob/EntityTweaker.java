package com.gendeathrow.pmobs.entity.mob;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.init.RegisterEntities;
import com.gendeathrow.pmobs.entity.ai.TwitchersAttack;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTweaker extends EntityRaiderBase{

	public EntityTweaker(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		
        this.tasks.addTask(2, new TwitchersAttack(this, 1.0D, false));
        this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        
	    this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
	    this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
	    
	}
	
	
    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata){
    		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
    		
    		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Tweaker Health", -.5, 2));

	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30D);
	        
	        if(!this.isChild() && PMSettings.leapAttackAI && rand.nextDouble() < .07 + difficultyManager.calculateProgressionDifficulty(.05, .10))
	        	this.setLeapAttack(true);  
        	
    		return livingdata;
    }
    
    @Nullable
    protected ResourceLocation getLootTable()
    {
        return RegisterEntities.tweakerLoot;
    }
    
    private int ScreamTick = 1200;
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		
		if(this.getAttackTarget() != null)
		{
			if(ScreamTick++ > 250 && this.canEntityBeSeen(this.getAttackTarget()))
			{
				this.world.playSound(null, this.getPosition(), RaidersSoundEvents.RAIDERS_SCREAM, SoundCategory.HOSTILE, 2.0F, this.getRNG().nextFloat() * 0.4F + 0.8F);
				this.ScreamTick = this.getRNG().nextInt(100);
			}
			if(!this.isSprinting()) 
				this.setSprinting(true);
		}else
			if(this.isSprinting()) 
				this.setSprinting(false);
		
	}
    
  
}
