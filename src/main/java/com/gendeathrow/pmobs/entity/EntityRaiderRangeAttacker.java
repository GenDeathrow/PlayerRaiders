package com.gendeathrow.pmobs.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityRaiderRangeAttacker extends EntityRaiderBase implements IRangedAttackMob{

    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(EntityRaiderRangeAttacker.class, DataSerializers.BOOLEAN);
    
    private final EntityAIAttackMelee aiAttackOnCollide = new EntityAIAttackMelee(this, 1.2D, false) {
        public void resetTask() {
            super.resetTask();
            EntityRaiderRangeAttacker.this.setSwingingArms(false);
        }
        public void startExecuting() {
            super.startExecuting();
            EntityRaiderRangeAttacker.this.setSwingingArms(true);
        }
    };
    
	public EntityRaiderRangeAttacker(World worldIn) {
		super(worldIn);
	}
	
    @Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
	}
    
	@Override
    protected void entityInit() {
        super.entityInit();
		this.getDataManager().register(SWINGING_ARMS, Boolean.valueOf(false));
    }
	
    
	public abstract void rangedAttackTarget(EntityLivingBase target, float distanceFactor);

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		this.rangedAttackTarget(target, distanceFactor);
	}
	
    @SideOnly(Side.CLIENT)
    public boolean isSwingingArms() {
        return ((Boolean)this.dataManager.get(SWINGING_ARMS)).booleanValue();
    }
    
    public void setSwingingArms(boolean swingingArms) {
        this.dataManager.set(SWINGING_ARMS, Boolean.valueOf(swingingArms));
    }


}
