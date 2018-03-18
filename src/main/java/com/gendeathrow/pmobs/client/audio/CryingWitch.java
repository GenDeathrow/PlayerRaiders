package com.gendeathrow.pmobs.client.audio;

import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;

public class CryingWitch extends MovingSound{

    private final EntityRangedAttacker raider;
    private float distance = 0.0F;

    public CryingWitch(EntityRangedAttacker minecartIn)
    {
        super(com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_WITCH_CRY, SoundCategory.HOSTILE);
        this.raider = minecartIn;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 2.5f;
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
		 }
	}
}
