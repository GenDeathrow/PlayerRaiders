package com.gendeathrow.pmobs.entity;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.entity.ai.EntityAIPyromaniac;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
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
	}
	

    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
    		
    		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));
    		
    		return livingdata;
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		if(this.getRNG().nextDouble() < .45) return RaidersSoundEvents.RAIDERS_LAUGH;
		else return super.getAmbientSound();
	}
}
