package com.gendeathrow.pmobs.client.model.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class DropPodModel extends ModelBase
{
	  //fields
	    ModelRenderer TopPlate;
	    ModelRenderer BottomPlate;
	    ModelRenderer RBottomWedge;
	    ModelRenderer BBottomWedge;
	    ModelRenderer LBottomWedge;
	    ModelRenderer FBottomWedge;
	    ModelRenderer Door;
	    ModelRenderer RWall;
	    ModelRenderer BWall;
	    ModelRenderer LWall;
	    ModelRenderer FLCWall;
	    ModelRenderer BRCWall;
	    ModelRenderer FRCWall;
	    ModelRenderer BLCWall;
	    ModelRenderer FLCWedge;
	    ModelRenderer BRCWedge;
	    ModelRenderer FRCWedge;
	    ModelRenderer BLCWedge;
	    ModelRenderer airfoil4;
	    ModelRenderer airfoil3;
	    ModelRenderer airfoil2;
	    ModelRenderer Seat;
	    ModelRenderer airfoil1;
	  
	  public DropPodModel()
	  {
	    textureWidth = 128;
	    textureHeight = 128;
	    
	      TopPlate = new ModelRenderer(this, 0, 34);
	      TopPlate.addBox(-12F, 0F, -12F, 24, 4, 24);
	      TopPlate.setRotationPoint(0F, -15F, 0F);
	      TopPlate.setTextureSize(128, 128);
	      TopPlate.mirror = true;
	      setRotation(TopPlate, 0F, 0F, 0F);
	      BottomPlate = new ModelRenderer(this, 0, 0);
	      BottomPlate.addBox(-9F, 0F, -9F, 18, 6, 18);
	      BottomPlate.setRotationPoint(0F, 18F, 0F);
	      BottomPlate.setTextureSize(128, 128);
	      BottomPlate.mirror = true;
	      setRotation(BottomPlate, 0F, 0F, 0F);
	      RBottomWedge = new ModelRenderer(this, 80, 0);
	      RBottomWedge.addBox(-9F, -12F, -1F, 18, 12, 2);
	      RBottomWedge.setRotationPoint(-8F, 22.46667F, 0F);
	      RBottomWedge.setTextureSize(128, 128);
	      RBottomWedge.mirror = true;
	      setRotation(RBottomWedge, 0.4014257F, 1.570796F, 0F);
	      BBottomWedge = new ModelRenderer(this, 80, 0);
	      BBottomWedge.addBox(-9F, -12F, -1F, 18, 12, 2);
	      BBottomWedge.setRotationPoint(0F, 22.5F, 8F);
	      BBottomWedge.setTextureSize(128, 128);
	      BBottomWedge.mirror = true;
	      setRotation(BBottomWedge, -0.4014257F, 0F, 0F);
	      LBottomWedge = new ModelRenderer(this, 80, 0);
	      LBottomWedge.addBox(-9F, -12F, -1F, 18, 12, 2);
	      LBottomWedge.setRotationPoint(8F, 22.46667F, 0F);
	      LBottomWedge.setTextureSize(128, 128);
	      LBottomWedge.mirror = true;
	      setRotation(LBottomWedge, 0.4014257F, -1.570796F, 0F);
	      LBottomWedge.mirror = false;
	      FBottomWedge = new ModelRenderer(this, 80, 0);
	      FBottomWedge.addBox(-9F, -12F, -1F, 18, 12, 2);
	      FBottomWedge.setRotationPoint(0F, 22.46667F, -8F);
	      FBottomWedge.setTextureSize(128, 128);
	      FBottomWedge.mirror = true;
	      setRotation(FBottomWedge, 0.4014257F, 0F, 0F);
	      Door = new ModelRenderer(this, 0, 65);
	      Door.addBox(-9F, 0F, -2F, 18, 25, 2);
	      Door.setRotationPoint(0F, -13F, -12.1F);
	      Door.setTextureSize(128, 128);
	      Door.mirror = true;
	      setRotation(Door, 0F, 0F, 0F);
	      RWall = new ModelRenderer(this, 40, 65);
	      RWall.addBox(-1F, 0F, -9F, 2, 25, 18);
	      RWall.setRotationPoint(-13.1F, -13F, 0F);
	      RWall.setTextureSize(128, 128);
	      RWall.mirror = true;
	      setRotation(RWall, 0F, 0F, 0F);
	      BWall = new ModelRenderer(this, 0, 65);
	      BWall.addBox(-9F, 0F, 0F, 18, 25, 2);
	      BWall.setRotationPoint(0F, -13F, 11.9F);
	      BWall.setTextureSize(128, 128);
	      BWall.mirror = true;
	      setRotation(BWall, 0F, 0F, 0F);
	      LWall = new ModelRenderer(this, 40, 65);
	      LWall.addBox(-1F, 0F, -9F, 2, 25, 18);
	      LWall.setRotationPoint(12.9F, -13F, 0F);
	      LWall.setTextureSize(128, 128);
	      LWall.mirror = true;
	      setRotation(LWall, 0F, 0F, 0F);
	      LWall.mirror = false;
	      FLCWall = new ModelRenderer(this, 85, 65);
	      FLCWall.addBox(14F, -3F, -3.5F, 1, 25, 7);
	      FLCWall.setRotationPoint(0F, -10F, 0F);
	      FLCWall.setTextureSize(128, 128);
	      FLCWall.mirror = true;
	      setRotation(FLCWall, 0F, 0.7853982F, 0F);
	      BRCWall = new ModelRenderer(this, 85, 65);
	      BRCWall.addBox(14F, -3F, -3.5F, 1, 25, 7);
	      BRCWall.setRotationPoint(0F, -10F, 0F);
	      BRCWall.setTextureSize(128, 128);
	      BRCWall.mirror = true;
	      setRotation(BRCWall, 0F, -2.356194F, 0F);
	      FRCWall = new ModelRenderer(this, 85, 65);
	      FRCWall.addBox(14F, -3F, -3.5F, 1, 25, 7);
	      FRCWall.setRotationPoint(1F, -10F, 0F);
	      FRCWall.setTextureSize(128, 128);
	      FRCWall.mirror = true;
	      setRotation(FRCWall, 0F, 2.356194F, 0F);
	      BLCWall = new ModelRenderer(this, 85, 65);
	      BLCWall.addBox(14F, -3F, -3.5F, 1, 25, 7);
	      BLCWall.setRotationPoint(0F, -10F, 0F);
	      BLCWall.setTextureSize(128, 128);
	      BLCWall.mirror = true;
	      setRotation(BLCWall, 0F, -0.7853982F, 0F);
	      FLCWedge = new ModelRenderer(this, 92, 16);
	      FLCWedge.addBox(14.1F, 0F, -3.5F, 1, 8, 6);
	      FLCWedge.setRotationPoint(0F, 5.5F, 0F);
	      FLCWedge.setTextureSize(128, 128);
	      FLCWedge.mirror = true;
	      setRotation(FLCWedge, 0F, 0.7853982F, 0.418879F);
	      BRCWedge = new ModelRenderer(this, 80, 16);
	      BRCWedge.addBox(15.1F, -1F, -2.4F, 1, 8, 5);
	      BRCWedge.setRotationPoint(0F, 6.5F, 0F);
	      BRCWedge.setTextureSize(128, 128);
	      BRCWedge.mirror = true;
	      setRotation(BRCWedge, 0F, -2.356194F, 0.3839724F);
	      FRCWedge = new ModelRenderer(this, 92, 16);
	      FRCWedge.addBox(14.1F, 0F, -3.5F, 1, 8, 6);
	      FRCWedge.setRotationPoint(0F, 5.5F, 0F);
	      FRCWedge.setTextureSize(128, 128);
	      FRCWedge.mirror = true;
	      setRotation(FRCWedge, 0F, 2.356194F, 0.418879F);
	      BLCWedge = new ModelRenderer(this, 80, 16);
	      BLCWedge.addBox(15.1F, -1F, -2.5F, 1, 8, 5);
	      BLCWedge.setRotationPoint(0F, 6.5F, 0F);
	      BLCWedge.setTextureSize(128, 128);
	      BLCWedge.mirror = true;
	      setRotation(BLCWedge, 0F, -0.7853982F, 0.3839724F);
	      airfoil4 = new ModelRenderer(this, 0, 92);
	      airfoil4.addBox(-3F, -22.26667F, -2F, 6, 22, 4);
	      airfoil4.setRotationPoint(9F, -8F, 9F);
	      airfoil4.setTextureSize(128, 128);
	      airfoil4.mirror = true;
	      setRotation(airfoil4, -0.2617994F, 0.7853982F, 0F);
	      airfoil3 = new ModelRenderer(this, 0, 92);
	      airfoil3.addBox(-3F, -22.26667F, -2F, 6, 22, 4);
	      airfoil3.setRotationPoint(9F, -8F, -9F);
	      airfoil3.setTextureSize(128, 128);
	      airfoil3.mirror = true;
	      setRotation(airfoil3, -0.2617994F, 2.356194F, 0F);
	      airfoil2 = new ModelRenderer(this, 0, 92);
	      airfoil2.addBox(-3F, -22.26667F, -2F, 6, 21, 4);
	      airfoil2.setRotationPoint(-9F, -8F, 9F);
	      airfoil2.setTextureSize(128, 128);
	      airfoil2.mirror = true;
	      setRotation(airfoil2, -0.2617994F, -0.7679449F, 0F);
	      Seat = new ModelRenderer(this, 21, 109);
	      Seat.addBox(-6.5F, 0F, -4F, 12, 6, 11);
	      Seat.setRotationPoint(0F, 12F, 0F);
	      Seat.setTextureSize(128, 128);
	      Seat.mirror = true;
	      setRotation(Seat, 0F, 0F, 0F);
	      airfoil1 = new ModelRenderer(this, 0, 92);
	      airfoil1.addBox(-3F, -22.26667F, -2F, 6, 22, 4);
	      airfoil1.setRotationPoint(-9F, -8F, -9F);
	      airfoil1.setTextureSize(128, 128);
	      airfoil1.mirror = true;
	      setRotation(airfoil1, -0.2617994F, -2.356194F, 0F);
	  }
	  
	  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	  {
	    super.render(entity, f, f1, f2, f3, f4, f5);
        GlStateManager.pushMatrix();
        
	    //setRotationAngles(f, f1, f2, f3, f4, f5);
	    TopPlate.render(f5);
	    BottomPlate.render(f5);
	    RBottomWedge.render(f5);
	    BBottomWedge.render(f5);
	    LBottomWedge.render(f5);
	    FBottomWedge.render(f5);
	    Door.render(f5);
	    RWall.render(f5);
	    BWall.render(f5);
	    LWall.render(f5);
	    FLCWall.render(f5);
	    BRCWall.render(f5);
	    FRCWall.render(f5);
	    BLCWall.render(f5);
	    FLCWedge.render(f5);
	    BRCWedge.render(f5);
	    FRCWedge.render(f5);
	    BLCWedge.render(f5);
	    airfoil4.render(f5);
	    airfoil3.render(f5);
	    airfoil2.render(f5);
	    Seat.render(f5);
	    airfoil1.render(f5);
	    
        GlStateManager.popMatrix();
	  }
	  
	  private void setRotation(ModelRenderer model, float x, float y, float z)
	  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
	  }
	  
	  @Override
	  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity f6)
	  {
	    super.setRotationAngles(f, f1, f2, f3, f4, f5, f6);
	  }

	}
