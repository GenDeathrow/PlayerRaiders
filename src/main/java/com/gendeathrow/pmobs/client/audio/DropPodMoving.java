package com.gendeathrow.pmobs.client.audio;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;

import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.common.*;

public class DropPodMoving extends MovingSound
{

	private final EntityDropPod droppod;
	
	public DropPodMoving(EntityDropPod entityIn) 
	{
		super(SoundEvents.ROCKET_AMBIENT, SoundCategory.NEUTRAL);
		this.droppod = entityIn;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
        this.xPosF = (float)entityIn.posX;
        this.yPosF = (float)entityIn.posY;
        this.zPosF = (float)entityIn.posZ;
	}

	@Override
	public void update() 
	{
		 if (!this.droppod.isDead)
		 {
            float f = MathHelper.sqrt_double(this.droppod.motionY * this.droppod.motionY);

            if ((double)f >= 0.01D)
            {
                this.volume = 0.0F + MathHelper.clamp_float(f, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                this.volume = 0.0F;
            }
            
			 this.xPosF = (float)this.droppod.posX;
			 this.yPosF = (float)this.droppod.posY;
			 this.zPosF = (float)this.droppod.posZ;
        }
        else
        {
            this.donePlaying = true;
        }
	}

}
