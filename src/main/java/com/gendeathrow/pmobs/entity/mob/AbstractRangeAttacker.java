package com.gendeathrow.pmobs.entity.mob;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractRangeAttacker extends EntityRaiderBase implements IRangedAttackMob{

    private static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.<Boolean>createKey(AbstractRangeAttacker.class, DataSerializers.BOOLEAN);
    
	public AbstractRangeAttacker(World worldIn) {
		super(worldIn);
	}
	
    @Override
	protected void initEntityAI() {
		super.initEntityAI();
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
