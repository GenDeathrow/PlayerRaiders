package com.gendeathrow.pmobs.client.model;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.mob.EntityTwitcher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class TwitcherModel extends RaiderModel{

	public TwitcherModel(float modelSize) {
		super(modelSize);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		boolean twitcherActive = entityIn instanceof EntityTwitcher && ((EntityRaiderBase)entityIn).isArmsRaised();
    
    	if (twitcherActive)
        {
             
        	this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 1F);
        	this.bipedLeftArm.rotateAngleY -= -((float)Math.PI / .75F);
        	this.bipedLeftArm.rotateAngleZ += 1.25;
            
        	this.bipedRightArm.rotateAngleX -= -((float)Math.PI / 1F);
        	this.bipedRightArm.rotateAngleY -= -((float)Math.PI / .75F); 
        	this.bipedRightArm.rotateAngleZ -= 1.25;

        }
    	

	}

}
