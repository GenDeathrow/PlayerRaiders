package com.gendeathrow.pmobs.entity.New;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.ai.EntityAIRangedAttack;
import com.gendeathrow.pmobs.handlers.EquipmentManager;

public class EntityRangedAttacker extends EntityRider implements IRangedAttackMob
{
	
	public boolean isRangedAttacker = false;
	
    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityRangedAttacker.class, DataSerializers.BOOLEAN);
	
    public final EntityAIRangedAttack aiArrowAttack = new EntityAIRangedAttack(this, 1.0D, 20, 15.0F);
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false)
    {
        /**
         * Resets the task
         */
        public void resetTask()
        {
            super.resetTask();
            EntityRangedAttacker.this.setSwingingArms(false);
        }
        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            super.startExecuting();
            EntityRangedAttacker.this.setSwingingArms(true);
        }
    };
    
	public EntityRangedAttacker(World worldIn) 
	{
		super(worldIn);
		
	        this.setCombatTask();
	}
	
	
	@Override
    protected void entityInit()
    {
        super.entityInit();

		this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
    }
	
	protected void initEntityAI()
	{
		super.initEntityAI();
		
		if(isRangedAttacker)
		{
			this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
		}
		
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) 
	{
//		if(isRangedAttacker)
//		{
			
		       EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.worldObj, this);
		        double d0 = target.posX - this.posX;
		        double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entitytippedarrow.posY;
		        double d2 = target.posZ - this.posZ;
		        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		        entitytippedarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
		        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
		        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
		        DifficultyInstance difficultyinstance = this.worldObj.getDifficultyForLocation(new BlockPos(this));
		        entitytippedarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));

		        if (i > 0)
		        {
		            entitytippedarrow.setDamage(entitytippedarrow.getDamage() + (double)i * 0.5D + 0.5D);
		        }

		        if (j > 0)
		        {
		            entitytippedarrow.setKnockbackStrength(j);
		        }

		        boolean flag = this.isBurning() && difficultyinstance.func_190083_c() && this.rand.nextBoolean();
		        flag = flag || EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0;

		        if (flag)
		        {
		            entitytippedarrow.setFire(100);
		        }

		        ItemStack itemstack = this.getHeldItem(EnumHand.OFF_HAND);

		        if (itemstack != null && itemstack.getItem() == Items.TIPPED_ARROW)
		        {
		            entitytippedarrow.setPotionEffect(itemstack);
		        }

		        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		        this.worldObj.spawnEntityInWorld(entitytippedarrow);
		        
//		}
	}
	
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        
        boolean i = false;

        if (compound.hasKey("RangedAttacker", 99))
        {
            i = compound.getBoolean("RangedAttacker");
        }
        
        if(i) this.setCombatTask();
    }
    
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("RangedAttacker", this.isRangedAttacker);
    }
    
    /**
     * Gives armor or weapon for entity based on given DifficultyInstance
     */
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
		if(isRangedAttacker)
		{
			//this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
		}
		
        super.setEquipmentBasedOnDifficulty(difficulty);
    }

	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
    	
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        
    	      
    	if(this.rand.nextDouble() < .1d && !this.isChild() && !this.isHeroBrine() && this.difficultyManager.calculateProgressionDifficulty(1) >= 1)
    	{
    		this.isRangedAttacker = true;
    		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    		if(this.rand.nextDouble() < .05 + (this.difficultyManager.calculateProgressionDifficulty(.5) > .25 ? .25 : this.difficultyManager.calculateProgressionDifficulty(.5))) 
    		{
    			try
    			{
    				ItemStack tippedArrow = EquipmentManager.getRandomArrows().getCopy();
    				if(tippedArrow != null)	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, tippedArrow);
    			}catch(Exception e){ RaidersCore.logger.error(e); }
    		}
            Biome biome = this.worldObj.getBiomeGenForCoords(new BlockPos(this));
            this.tasks.addTask(1, this.aiArrowAttack);
            
    	}
       	
  	
		return livingdata;
    }
    
    
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, @Nullable ItemStack stack)
    {
        super.setItemStackToSlot(slotIn, stack);

        if (!this.worldObj.isRemote && slotIn == EntityEquipmentSlot.MAINHAND)
        {
            this.setCombatTask();
        }
    }
    
    
    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms()
    {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }
    
    public void setSwingingArms(boolean swingingArms)
    {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }
    
    public void setCombatTask()
    {
      if (this.worldObj != null && !this.worldObj.isRemote)
        {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            ItemStack itemstack = this.getHeldItemMainhand();

            if (itemstack != null && itemstack.getItem() == Items.BOW)
            {
                int i = 20;

                if (this.worldObj.getDifficulty() != EnumDifficulty.HARD)
                {
                    i = 40;
                }

                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(1, this.aiArrowAttack);
            }
            else
            {
                this.tasks.addTask(1, this.aiAttackOnCollide);
            }
        }
    }
}
