package com.gendeathrow.pmobs.client.audio;

import com.gendeathrow.pmobs.entity.EntityRaiderWitch;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;

public class CryingWitch extends MovingSound{

    private final EntityRaiderWitch raider;
    private float distance = 0.0F;

    public CryingWitch(EntityRaiderWitch minecartIn)
    {
        super(com.gendeathrow.pmobs.common.RaidersSoundEvents.RAIDERS_WITCH_CRY, SoundCategory.HOSTILE);
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
