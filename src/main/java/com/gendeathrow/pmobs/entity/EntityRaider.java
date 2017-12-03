package com.gendeathrow.pmobs.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRaider extends EntityRaiderBase{

	public static final UUID BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	private static final AttributeModifier BABY_SPEED_BOOST = new AttributeModifier(BABY_SPEED_BOOST_ID, "Baby speed boost", 0.45D, 1);
	
	private static final DataParameter<Boolean> IS_CHILD = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
	
	public EntityRaider(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(IS_CHILD, false);
	}
	

	protected final EntityAILookIdle lookIdleAI = new EntityAILookIdle(this);

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
			//this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, true));

	        
	}

    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata){
    		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
    		
    		if(this.world.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance) 
	        	this.setChild(true); 
  
    		return livingdata;
    }
	
	
    @Override
    public float getEyeHeight()
    {
        float f = 1.74F;

        if (this.isChild())
        {
            f = (float)((double)f - 0.81D);
        }

        return f;
    }
    
    public double getYOffset()
    {
        return this.isChild() ? 0.0D : -0.35D;
    }
	
    public boolean isChild()
    {
        return ((Boolean)this.getDataManager().get(IS_CHILD)).booleanValue();
    }
    
    public void setChild(boolean childZombie)
    {
        this.getDataManager().set(IS_CHILD, Boolean.valueOf(childZombie));

        if (this.world != null && !this.world.isRemote)
        {
        	
            IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(BABY_SPEED_BOOST);
            iattributeinstance.removeModifier(SPEED_BOOST_ID);

            if (childZombie)
            {
                iattributeinstance.applyModifier(BABY_SPEED_BOOST);
            }
            
        }

        this.setChildSize(childZombie);
    }
    
    public void setChildSize(boolean isChild)
    {
        this.multiplySize(isChild ? 0.5F : 1.0F);
    }
    
    public void notifyDataManagerChange(DataParameter<?> key)
    {
    	super.notifyDataManagerChange(key);
    	
        if (IS_CHILD.equals(key))
        {
            this.setChildSize(this.isChild());
        }
    }

      

    
}
