package com.gendeathrow.pmobs.entity.mob;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRanger extends AbstractRangeAttacker{

	
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
    {
        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            super.resetTask();
            EntityRanger.this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
            EntityRanger.this.setSwingingArms(false);
        }
        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            super.startExecuting();
            EntityRanger.this.setSwingingArms(true);
            EntityRanger.this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        }
    };

    
    
	public EntityRanger(World worldIn) {
		super(worldIn);
	}
	
	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();

		this.tasks.addTask(4, new EntityAIAttackRanged(this, 1.0D, 20, 15.0F));
	    this.tasks.addTask(8, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(9, new EntityAILookIdle(this));
	        
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, true));
	}
	
	
    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata){
    		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);

    		this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));

    		if(this.rand.nextDouble() < .05 + (this.difficultyManager.calculateProgressionDifficulty(.05) > .25 ? .25 : this.difficultyManager.calculateProgressionDifficulty(.05))) 
    		{
    			try
    			{
//    				ItemStack tippedArrow = EquipmentManager.getRandomArrows().getCopy();
//    				if(tippedArrow != null)	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, tippedArrow);
    			}catch(Exception e){ RaidersMain.logger.error(e); }
    		}
    		

    		return livingdata;
    }
    

	@Override
	public void rangedAttackTarget(EntityLivingBase target, float distanceFactor) {

        EntityArrow entityarrow = this.getArrow(distanceFactor);
        double d0 = target.posX - this.posX;
        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
        double d2 = target.posZ - this.posZ;
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.spawnEntity(entityarrow);
	}
	
    protected EntityArrow getArrow(float p_190726_1_)
    {
        ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

        if (itemstack.getItem() == Items.SPECTRAL_ARROW)
        {
            EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(this.world, this);
            entityspectralarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
            return entityspectralarrow;
        }
        else
        {
            EntityArrow entityarrow = getDefault(p_190726_1_);

            if (itemstack.getItem() == Items.TIPPED_ARROW && entityarrow instanceof EntityTippedArrow)
            {
                ((EntityTippedArrow)entityarrow).setPotionEffect(itemstack);
            }

            return entityarrow;
        }
    }
    
    protected EntityArrow getDefault(float p_190726_1_) {
        EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
        return entitytippedarrow;
    }

}
