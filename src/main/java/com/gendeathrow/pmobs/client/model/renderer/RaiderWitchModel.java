package com.gendeathrow.pmobs.client.model.renderer;

import com.gendeathrow.pmobs.entity.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.EntityRaiderWitch;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;

public class RaiderWitchModel extends RaiderModel{

	public RaiderWitchModel(float modelSize) {
		super(modelSize);
	}

	
	
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;

    	if (((EntityRaiderBase)entitylivingbaseIn).isArmsRaised())
        {
			this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			
			for (int i = 0; i < 10; ++i)
			{
				entitylivingbaseIn.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entitylivingbaseIn.posX + (entitylivingbaseIn.getRNG().nextDouble() - 0.5D) * (double)entitylivingbaseIn.width, entitylivingbaseIn.posY + entitylivingbaseIn.getRNG().nextDouble() * (double)entitylivingbaseIn.height - 0.25D, entitylivingbaseIn.posZ + (entitylivingbaseIn.getRNG().nextDouble() - 0.5D) * (double)entitylivingbaseIn.width, (entitylivingbaseIn.getRNG().nextDouble() - 0.5D) * 2.0D, -entitylivingbaseIn.getRNG().nextDouble(), (entitylivingbaseIn.getRNG().nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
        }
	}
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
		boolean witchActive = entityIn instanceof EntityRaiderWitch && ((EntityRaiderWitch) entityIn).isWitchActive();
        
		//If Witch is Sitting move his model down
		if (!witchActive)
			 GlStateManager.translate(0.0F, 0.25F, 0.0F);
		
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
