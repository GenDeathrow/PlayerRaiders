package com.gendeathrow.pmobs.client.model.renderer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

public class RaiderModel extends ModelBiped
{

	private ModelBiped modelDirt;
	public static final ArmPose HoldingSkull  = EnumHelper.addEnum(ArmPose.class, "HOLDING_SKULL", new Class<?>[]{});

    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private final ModelRenderer bipedCape;
    private final ModelRenderer bipedDeadmau5Head;
    private final boolean smallArms;
	
	
	public RaiderModel(float modelSize, boolean smallArmsIn) 
	{
		 super(modelSize, 0.0F, 64, 64);
		 
		 this.smallArms = smallArmsIn;
		 this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
		 this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, modelSize);
		 this.bipedCape = new ModelRenderer(this, 0, 0);
		 this.bipedCape.setTextureSize(64, 32);
		 this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
		 
		 if (smallArmsIn)
	        {
	            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
	            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
	            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
	            this.bipedRightArm = new ModelRenderer(this, 40, 16);
	            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize);
	            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
	            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
	            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
	            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
	            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
	            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, modelSize + 0.25F);
	            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
	        }
	        else
	        {
	            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
	            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
	            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
	            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
	            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
	            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
	            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
	            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
	        }

	        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
	        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
	        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
	        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
	        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
	        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
	        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
	        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.25F);
	        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
	        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
	        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.25F);
	        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
	}
	
	
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        }

        GlStateManager.popMatrix();
    }
    
    public void renderDeadmau5Head(float scale)
    {
        copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0F;
        this.bipedDeadmau5Head.rotationPointY = 0.0F;
        this.bipedDeadmau5Head.render(scale);
    }

    public void renderCape(float scale)
    {
        this.bipedCape.render(scale);
    }
	

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack mainHand = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offHand = entitylivingbaseIn.getHeldItem(EnumHand.OFF_HAND);

		if (mainHand != null && mainHand.getItem() == Items.BOW && ((EntityRangedAttacker)entitylivingbaseIn).isSwingingArms())
		{
			if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
			else
			{
				this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}
		
		if(offHand != null && offHand.getItem() == Items.SKULL)
		{
			this.leftArmPose = HoldingSkull;
		}
		


		super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
	}
	
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        
    	copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);

        if (entityIn.isSneaking())
        {
            this.bipedCape.rotationPointY = 2.0F;
        }
        else
        {
            this.bipedCape.rotationPointY = 0.0F;
        }
        
    	if (this.leftArmPose == HoldingSkull)
        {
        	this.bipedLeftArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
        	//this.bipedLeftArmwear.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
        	//this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
        	this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
        	//this.bipedLeftArmwear.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
        	//this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
        }
    }
	
	
    public void setInvisible(boolean invisible)
    {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }
    
    
    public void postRenderArm(float scale, EnumHandSide side)
    {
        ModelRenderer modelrenderer = this.getArmForSide(side);

        if (this.smallArms)
        {
            float f = 0.5F * (float)(side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;
        }
        else
        {
            modelrenderer.postRender(scale);
        }
    }
	
	

}
