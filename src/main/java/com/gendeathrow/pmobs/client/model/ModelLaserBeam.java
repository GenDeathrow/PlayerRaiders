package com.gendeathrow.pmobs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelLaserBeam extends ModelBase{
	  ModelRenderer beam;
	  
	  
	  public ModelLaserBeam()
	  {
	        this.textureWidth = 32;
	        this.textureHeight = 32;
	    
	    this.beam = new ModelRenderer(this, 0, 0);
	    this.beam.addBox(-0.5F, -0.5F, -2.0F, 1, 1, 5);
	    this.beam.setRotationPoint(0.0F, 0.0F, 0.0F);
	    setRotateAngle(this.beam, 0.0F, 0.0F, 0.0F);
	  }
	  
	  
	  public void render()
	  {
	    this.beam.render(0.0625F);
	  }
	  
	  
	  //Helper
	  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
	        modelRenderer.rotateAngleX = x;
	        modelRenderer.rotateAngleY = y;
	        modelRenderer.rotateAngleZ = z;
	  }
}
