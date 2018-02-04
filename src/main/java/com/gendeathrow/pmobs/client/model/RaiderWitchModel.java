package com.gendeathrow.pmobs.client.model;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;

public class RaiderWitchModel extends RaiderModel{

	public RaiderWitchModel(float modelSize) {
		super(modelSize);
	}

	
	@Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
		boolean witchActive = entityIn instanceof EntityRaiderWitch && ((EntityRaiderWitch) entityIn).isWitchActive();
  
		GlStateManager.pushMatrix();
		//If Witch is Sitting move his model down
		if (!witchActive)
			 GlStateManager.translate(0.0F, 0.50F, 0.0F);
		
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		GlStateManager.popMatrix();
    }
	
	
	@Override
	public void setLivingAnimations(EntityLivingBase entityIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		super.setLivingAnimations(entityIn, p_78086_2_, p_78086_3_, partialTickTime);
		
    	if (entityIn instanceof EntityRaiderWitch && ((EntityRaiderBase)entityIn).isArmsRaised())
        {
			this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			
			for (int i = 0; i < 10; ++i)
			{
				entityIn.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entityIn.posX + (entityIn.getRNG().nextDouble() - 0.5D) * (double)entityIn.width, entityIn.posY + entityIn.getRNG().nextDouble() * (double)entityIn.height - 0.25D, entityIn.posZ + (entityIn.getRNG().nextDouble() - 0.5D) * (double)entityIn.width, (entityIn.getRNG().nextDouble() - 0.5D) * 2.0D, -entityIn.getRNG().nextDouble(), (entityIn.getRNG().nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
        }
	}
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
		boolean witchActive = entityIn instanceof EntityRaiderWitch && ((EntityRaiderWitch) entityIn).isWitchActive();

    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

        if (!witchActive)
        {
            this.bipedRightArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float)Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -1.4137167F;
            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
            this.bipedRightLeg.rotateAngleZ = 0.07853982F;
            this.bipedLeftLeg.rotateAngleX = -1.4137167F;
            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
        }
    }
}
