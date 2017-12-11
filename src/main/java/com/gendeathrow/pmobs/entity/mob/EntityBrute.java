package com.gendeathrow.pmobs.entity.mob;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.common.RaidersSoundEvents;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityBrute extends EntityRaiderBase{

	public EntityBrute(World worldIn) {
		super(worldIn);
    	this.setBrute();
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
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
    		
    		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(.7);
    		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.15);
    		this.stepHeight = 2F;
    		this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Brute Health", 1.25, 2));
        	
        	if(!this.getHeldItem(EnumHand.OFF_HAND).isEmpty()) this.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
        	if(this.getHeldItem(EnumHand.MAIN_HAND).isEmpty() || this.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.IRON_SWORD) this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));

        	
    		return livingdata;
    }
    
	// Raider attacks Target
    @Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean hasHitTarget = super.attackEntityAsMob(entityIn);
		if (hasHitTarget){
			if(entityIn instanceof EntityLivingBase){
				((EntityLivingBase)entityIn).knockBack(entityIn, 1, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
			}
		}
		return hasHitTarget;
	}
    
    public void setBrute() {
    	this.setBruteSize();
    }
    
    public void setBruteSize() {
    	this.multiplySize(1.5F);
    }
    
	protected SoundEvent getAmbientSound() {
		if(this.getRNG().nextDouble() < .45) 
			return RaidersSoundEvents.RAIDERS_BRUTE_LAUGH;
		else return RaidersSoundEvents.RAIDERS_SAY;
	}
    
}
