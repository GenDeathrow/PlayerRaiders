package com.gendeathrow.pmobs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

/**
 * Signal Transmitter - GenDeathrow
 * Created using Tabula 4.1.1
 */
public class SignalTransmitterModel extends ModelBase 
{
    public ModelRenderer SatiliteBase;
    public ModelRenderer SatiliteBase_1;
    public ModelRenderer SatiliteShaft;
    public ModelRenderer SatiliteShaft2;
    public ModelRenderer DishRight;
    public ModelRenderer DishMain;
    public ModelRenderer DishLeft;
    public ModelRenderer Transmitter;

    public SignalTransmitterModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        
        this.DishMain = new ModelRenderer(this, 10, 0);
        this.DishMain.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.DishMain.addBox(-4.0F, -4.0F, 0.0F, 8, 9, 1, 0.0F);
        this.setRotateAngle(DishMain, 1.413716694115407F, 0.0F, 0.0F);
        
        this.DishRight = new ModelRenderer(this, 29, 1);
        this.DishRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.DishRight.addBox(-6.6F, -4.0F, -2.0F, 3, 9, 1, 0.0F);
        this.setRotateAngle(DishRight, 0.0F, 0.5235987755982988F, 0.0F);
       
        this.SatiliteBase = new ModelRenderer(this, 0, 16);
        this.SatiliteBase.setRotationPoint(1.0F, 21.0F, 1.0F);
        this.SatiliteBase.addBox(-7.0F, -8.0F, -7.0F, 12, 3, 12, 0.0F);
        
        this.Transmitter = new ModelRenderer(this, 15, 11);
        this.Transmitter.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.Transmitter.addBox(-0.5F, -0.5F, 0.8F, 1, 1, 2, 0.0F);
        
        this.SatiliteBase_1 = new ModelRenderer(this, 0, 33);
        this.SatiliteBase_1.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.SatiliteBase_1.addBox(-7.0F, -8.0F, -7.0F, 14, 8, 14, 0.0F);
       
        this.DishLeft = new ModelRenderer(this, 0, 0);
        this.DishLeft.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.DishLeft.addBox(3.5F, -8.0F, -2.0F, 3, 9, 1, 0.0F);
        this.setRotateAngle(DishLeft, 0.0F, -0.5235987755982988F, 0.0F);
        
        this.SatiliteShaft2 = new ModelRenderer(this, 51, 6);
        this.SatiliteShaft2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.SatiliteShaft2.addBox(-1.0F, -1.3F, -0.7F, 2, 2, 2, 0.0F);
        this.setRotateAngle(SatiliteShaft2, -0.7330382858376184F, 0.0F, 0.0F);
        
        this.SatiliteShaft = new ModelRenderer(this, 51, 12);
        this.SatiliteShaft.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.SatiliteShaft.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, 0.0F);
        
        this.SatiliteShaft2.addChild(this.DishMain);
        this.DishMain.addChild(this.DishRight);
        this.DishMain.addChild(this.Transmitter);
        this.DishMain.addChild(this.DishLeft);
        this.SatiliteShaft.addChild(this.SatiliteShaft2);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) 
    { 
        GlStateManager.pushMatrix();
        	GlStateManager.translate(0, -0.4, 0);
        	
        	this.SatiliteBase.render(scale);
        	this.SatiliteBase_1.render(scale);
        
            GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);   	
        	
            this.SatiliteShaft.render(scale);
        	
        GlStateManager.popMatrix();    	
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) 
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
