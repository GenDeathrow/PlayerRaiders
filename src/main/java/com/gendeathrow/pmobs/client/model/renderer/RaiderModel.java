package com.gendeathrow.pmobs.client.model.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.util.EnumHelper;

public class RaiderModel extends ModelBiped
{

	private ModelBiped modelDirt;
	
	public static final ArmPose HoldingSkull  = EnumHelper.addEnum(ArmPose.class, "HOLDING_SKULL", new Class<?>[]{});
	public static final ArmPose TwitcherArms = EnumHelper.addEnum(ArmPose.class, "TWITCHERS_ARMS", new Class<?>[]{});
	
    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
//    private final ModelRenderer bipedCape;
	
	
	public RaiderModel(float modelSize) 
	{
		 super(modelSize, 0.0F, 64, 64);
		 
//		 this.bipedCape = new ModelRenderer(this, 0, 0);
//		 this.bipedCape.setTextureSize(64, 32);
//		 this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, modelSize);
		 
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
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
            
            this.bipedBodyWear.render(scale);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedHead.render(scale);
            this.bipedBody.render(scale);
            this.bipedRightArm.render(scale);
            this.bipedLeftArm.render(scale);
            this.bipedRightLeg.render(scale);
            this.bipedLeftLeg.render(scale);
            this.bipedHeadwear.render(scale);
        }

        GlStateManager.popMatrix();
    }

    public void renderCape(float scale)
    {
      //  this.bipedCape.render(scale);
    }
	
	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack mainHand = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);
		ItemStack offHand = entitylivingbaseIn.getHeldItem(EnumHand.OFF_HAND);

		if(offHand != null && offHand.getItem() == Items.SKULL)
		{
			this.leftArmPose = HoldingSkull;
		}
		
		if(mainHand != null && mainHand.getItem().getRegistryName().toString().equals("lcrdrfs:laser_blaster"))
		{
			this.rightArmPose = HoldingSkull;
		}
		
		super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
	}
	
	
	@Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
	
    	super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        
    	if (this.leftArmPose == HoldingSkull)
        {
        	this.bipedLeftArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
        	this.bipedLeftArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
        }
    	
    	if (this.rightArmPose == HoldingSkull)
        {
        	this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
        	this.bipedRightArm.rotateAngleX = -((float)Math.PI / 2F) + this.bipedHead.rotateAngleX;
        }
    	
    }
	
	@Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        this.bipedLeftArmwear.showModel = visible;
        this.bipedRightArmwear.showModel = visible;
        this.bipedLeftLegwear.showModel = visible;
        this.bipedRightLegwear.showModel = visible;
        this.bipedBodyWear.showModel = visible;
//        this.bipedCape.showModel = invisible;
    }
    
    
    public void postRenderArm(float scale, EnumHandSide side)
    {
        ModelRenderer modelrenderer = this.getArmForSide(side);

            float f = 0.5F * (float)(side == EnumHandSide.RIGHT ? 1 : -1);
            modelrenderer.rotationPointX += f;
            modelrenderer.postRender(scale);
            modelrenderer.rotationPointX -= f;

    }
	
	

}
