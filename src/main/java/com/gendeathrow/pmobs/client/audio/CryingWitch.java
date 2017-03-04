package com.gendeathrow.pmobs.client.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

public class CryingWitch extends MovingSound{

    private final EntityRangedAttacker raider;
    private float distance = 0.0F;

    public CryingWitch(EntityRangedAttacker minecartIn)
    {
        super(com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_WITCH_CRY, SoundCategory.HOSTILE);
        this.raider = minecartIn;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 2f;
    }

	@Override
	public void update() 
	{
		 if (this.raider.isDead || this.raider.isWitchActive())
		 {
			 this.donePlaying = true;
		 }
		 else
		 {
			 this.xPosF = (float)this.raider.posX;
			 this.yPosF = (float)this.raider.posY;
			 this.zPosF = (float)this.raider.posZ;
			 
			 
//			 float f = MathHelper.sqrt_double(this.raider.motionX * this.raider.motionX + this.raider.motionZ * this.raider.motionZ);
//
//			 if ((double)f >= 0.01D)
//			 {
//				 this.distance = MathHelper.clamp_float(this.distance + 0.0025F, 0.0F, 1.0F);
//				 this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.5F);
//			 }
//			 else
//			 {
//				 this.distance = 0.0F;
//				 this.volume = 0.0F;
//			 }
		 }
	}
}
