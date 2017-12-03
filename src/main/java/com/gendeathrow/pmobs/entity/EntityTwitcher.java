package com.gendeathrow.pmobs.entity;

import javax.annotation.Nullable;

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
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityTwitcher extends EntityRaiderBase{

	public EntityTwitcher(World worldIn) {
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
        	
    		return livingdata;
    }
    
  
}
