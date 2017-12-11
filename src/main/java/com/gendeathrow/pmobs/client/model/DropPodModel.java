package com.gendeathrow.pmobs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class DropPodModel extends ModelBase
{
	  public ModelRenderer BLCWedge;
	    public ModelRenderer FLCWedge;
	    public ModelRenderer FRCWedge;
	    public ModelRenderer BRCWedge;
	    public ModelRenderer TopPlate;
	    public ModelRenderer BottomPlate;
	    public ModelRenderer RBottomWedge;
	    public ModelRenderer BBottomWedge;
	    public ModelRenderer LBottomWedge;
	    public ModelRenderer FBottomWedge;
	    public ModelRenderer RWall;
	    public ModelRenderer BWall;
	    public ModelRenderer LWall;
	    public ModelRenderer FLCWall;
	    public ModelRenderer BRCWall;
	    public ModelRenderer FRCWall;
	    public ModelRenderer BLCWall;
	    public ModelRenderer airfoil4;
	    public ModelRenderer airfoil3;
	    public ModelRenderer airfoil2;
	    public ModelRenderer Seat;
	    public ModelRenderer airfoil1;
	    public ModelRenderer DoorRight;
	    public ModelRenderer DoorLeft;
	    public ModelRenderer DoorBottom;
	    public ModelRenderer DoorWindow;
	    public ModelRenderer DoorTop;

	    public DropPodModel() {
	        this.textureWidth = 128;
	        this.textureHeight = 128;
	        
	        this.BRCWedge = new ModelRenderer(this, 80, 16);
	        this.BWall = new ModelRenderer(this, 0, 65);

	        
	        this.FRCWedge = new ModelRenderer(this, 80, 16);
	        this.BLCWedge = new ModelRenderer(this, 80, 16);
	        this.FLCWedge = new ModelRenderer(this, 80, 16);
	        this.airfoil1 = new ModelRenderer(this, 0, 92);
	        this.airfoil2 = new ModelRenderer(this, 0, 92);
	        this.airfoil3 = new ModelRenderer(this, 0, 92);
	        this.airfoil4 = new ModelRenderer(this, 0, 92);
	        this.Seat = new ModelRenderer(this, 21, 109);
	        this.FRCWall = new ModelRenderer(this, 85, 65);
	        this.BRCWall = new ModelRenderer(this, 85, 65);
	        this.FLCWall = new ModelRenderer(this, 85, 65);
	        this.LWall = new ModelRenderer(this, 40, 65);
	        this.BLCWall = new ModelRenderer(this, 85, 65);
	        this.RWall = new ModelRenderer(this, 40, 65);
	        this.BBottomWedge = new ModelRenderer(this, 80, 0);
	        this.RBottomWedge = new ModelRenderer(this, 80, 0);
	        this.FBottomWedge = new ModelRenderer(this, 80, 0);
	        this.LBottomWedge = new ModelRenderer(this, 80, 0);
	        this.TopPlate = new ModelRenderer(this, 0, 34);
	        this.BottomPlate = new ModelRenderer(this, 0, 0);
	        this.DoorLeft = new ModelRenderer(this, 0, 65);
	        this.DoorBottom = new ModelRenderer(this, 0, 65);
	        this.DoorRight = new ModelRenderer(this, 0, 65);
	        this.DoorWindow = new ModelRenderer(this, 98, 49);
	        this.DoorTop = new ModelRenderer(this, 0, 65);	        
	        
	        this.BRCWedge.setRotationPoint(0.0F, 7.2F, 0.0F);
	        this.BRCWedge.addBox(14.2F, 0.0F, -2.5F, 1, 8, 5, 0.0F);
	        this.setRotateAngle(BRCWedge, 0.31869712141416456F, 0.7853981633974483F, 0.45378560551852565F);
	        this.BWall.setRotationPoint(0.0F, -13.0F, 11.899999618530273F);
	        this.BWall.addBox(-9.0F, 0.0F, 0.0F, 18, 25, 2, 0.0F);
	        this.RBottomWedge.setRotationPoint(-8.0F, 22.47F, 0.0F);
	        this.RBottomWedge.addBox(-9.0F, -12.0F, -1.0F, 18, 12, 2, 0.0F);
	        this.setRotateAngle(RBottomWedge, 0.4363323129985824F, 1.5707963267948966F, 0.0F);
	        this.DoorWindow.setRotationPoint(0.0F, -13.0F, -12.1F);
	        this.DoorWindow.addBox(-5.0F, 6.0F, -0.5F, 10, 13, 0, 0.0F);
	        this.setRotateAngle(DoorWindow, -5.235987755982988E-4F, -0.0F, 0.0F);
	        this.DoorTop.setRotationPoint(0.0F, -13.0F, -12.100000381469727F);
	        this.DoorTop.addBox(-9.0F, 0.0F, -2.0F, 18, 6, 2, 0.0F);
	        this.FLCWedge.setRotationPoint(0.0F, 7.2F, 0.0F);
	        this.FLCWedge.addBox(14.3F, 0.0F, -2.5F, 1, 8, 5, 0.0F);
	        this.setRotateAngle(FLCWedge, 0.31869712141416456F, -2.367539130330308F, -0.45378560551852565F);
	        this.LBottomWedge.mirror = true;
	        this.LBottomWedge.setRotationPoint(8.0F, 22.47F, 0.0F);
	        this.LBottomWedge.addBox(-9.0F, -12.0F, -1.0F, 18, 12, 2, 0.0F);
	        this.setRotateAngle(LBottomWedge, 0.4363323129985824F, -1.5707963267948966F, 0.0F);
	        this.BLCWedge.setRotationPoint(0.0F, 7.2F, -0.7F);
	        this.BLCWedge.addBox(14.4F, 0.0F, -2.5F, 1, 8, 5, 0.0F);
	        this.setRotateAngle(BLCWedge, -0.31869712141416456F, -0.7853981633974483F, 0.45378560551852565F);
	        this.DoorRight.setRotationPoint(0.0F, -13.0F, -12.100000381469727F);
	        this.DoorRight.addBox(-9.0F, 6.0F, -2.0F, 4, 13, 2, 0.0F);
	        this.FRCWall.setRotationPoint(1.0F, -8.0F, 0.0F);
	        this.FRCWall.addBox(14.0F, -3.0F, -3.5F, 1, 23, 7, 0.0F);
	        this.setRotateAngle(FRCWall, 0.0F, 0.7853981633974483F, 0.0F);
	        this.BottomPlate.setRotationPoint(0.0F, 18.0F, 0.0F);
	        this.BottomPlate.addBox(-9.0F, 0.0F, -9.0F, 18, 6, 18, 0.0F);
	        this.DoorBottom.setRotationPoint(0.0F, -13.0F, -12.100000381469727F);
	        this.DoorBottom.addBox(-9.0F, 19.0F, -2.0F, 18, 6, 2, 0.0F);
	        this.FRCWedge.setRotationPoint(0.0F, 7.2F, 0.0F);
	        this.FRCWedge.addBox(14.2F, 0.0F, -2.5F, 1, 8, 5, 0.0F);
	        this.setRotateAngle(FRCWedge, -0.31869712141416456F, 2.367539130330308F, -0.4553564018453205F);
	        this.airfoil2.setRotationPoint(-9.0F, -8.0F, 9.0F);
	        this.airfoil2.addBox(-3.0F, -22.27F, -2.0F, 6, 21, 4, 0.0F);
	        this.setRotateAngle(airfoil2, -0.3665191429188092F, -0.7853981633974483F, 0.0F);
	        this.Seat.setRotationPoint(0.0F, 14.0F, 0.0F);
	        this.Seat.addBox(-6.5F, 0.0F, -6.0F, 12, 4, 12, 0.0F);
	        this.airfoil1.setRotationPoint(-9.0F, -8.0F, -9.0F);
	        this.airfoil1.addBox(-3.0F, -22.27F, -2.0F, 6, 22, 4, 0.0F);
	        this.setRotateAngle(airfoil1, 0.3665191429188092F, 0.7853981633974483F, 0.0F);
	        this.BRCWall.setRotationPoint(0.0F, -8.0F, 0.0F);
	        this.BRCWall.addBox(14.0F, -3.0F, -3.5F, 1, 23, 7, 0.0F);
	        this.setRotateAngle(BRCWall, 0.0F, -2.356194490192345F, 0.0F);
	        this.FLCWall.setRotationPoint(0.0F, -8.0F, 0.0F);
	        this.FLCWall.addBox(14.0F, -3.0F, -3.5F, 1, 23, 7, 0.0F);
	        this.setRotateAngle(FLCWall, 0.0F, -0.7853981633974483F, 0.0F);
	        this.airfoil3.setRotationPoint(9.0F, -8.0F, -9.0F);
	        this.airfoil3.addBox(-3.0F, -22.27F, -2.0F, 6, 22, 4, 0.0F);
	        this.setRotateAngle(airfoil3, 0.3665191429188092F, -0.7853981633974483F, 0.0F);
	        this.airfoil4.setRotationPoint(9.0F, -8.0F, 9.0F);
	        this.airfoil4.addBox(-3.0F, -22.27F, -2.0F, 6, 22, 4, 0.0F);
	        this.setRotateAngle(airfoil4, -0.3665191429188092F, 0.7853981633974483F, 0.0F);
	        this.LWall.mirror = true;
	        this.LWall.setRotationPoint(12.899999618530273F, -13.0F, 0.0F);
	        this.LWall.addBox(-1.0F, 0.0F, -9.0F, 2, 25, 18, 0.0F);
	        this.BLCWall.setRotationPoint(0.0F, -8.0F, 0.0F);
	        this.BLCWall.addBox(14.0F, -3.0F, -3.5F, 1, 23, 7, 0.0F);
	        this.setRotateAngle(BLCWall, 0.0F, 2.356194490192345F, 0.0F);
	        this.BBottomWedge.setRotationPoint(0.0F, 22.5F, 8.0F);
	        this.BBottomWedge.addBox(-9.0F, -12.0F, -1.0F, 18, 12, 2, 0.0F);
	        this.setRotateAngle(BBottomWedge, -0.4363323129985824F, -0.0F, 0.0F);
	        this.TopPlate.setRotationPoint(0.0F, -15.0F, 0.0F);
	        this.TopPlate.addBox(-12.0F, 0.0F, -12.0F, 24, 4, 24, 0.0F);
	        this.RWall.setRotationPoint(-13.1F, -13.0F, 0.0F);
	        this.RWall.addBox(-1.0F, 0.0F, -9.0F, 2, 25, 18, 0.0F);
	        this.FBottomWedge.setRotationPoint(0.0F, 22.47F, -8.0F);
	        this.FBottomWedge.addBox(-9.0F, -12.0F, -1.0F, 18, 12, 2, 0.0F);
	        this.setRotateAngle(FBottomWedge, 0.4363323129985824F, -0.0F, 0.0F);
	        this.DoorLeft.setRotationPoint(0.0F, -13.0F, -12.100000381469727F);
	        this.DoorLeft.addBox(5.0F, 6.0F, -2.0F, 4, 13, 2, 0.0F);
	    }

	    @Override
	    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
	        this.BRCWedge.render(f5);
	        this.BWall.render(f5);
	        this.RBottomWedge.render(f5);
	        this.DoorWindow.render(f5);
	        this.DoorTop.render(f5);
	        this.FLCWedge.render(f5);
	        this.LBottomWedge.render(f5);
	        this.BLCWedge.render(f5);
	        this.DoorRight.render(f5);
	        this.FRCWall.render(f5);
	        this.BottomPlate.render(f5);
	        this.DoorBottom.render(f5);
	        this.FRCWedge.render(f5);
	        this.airfoil2.render(f5);
	        this.Seat.render(f5);
	        this.airfoil1.render(f5);
	        this.BRCWall.render(f5);
	        this.FLCWall.render(f5);
	        this.airfoil3.render(f5);
	        this.airfoil4.render(f5);
	        this.LWall.render(f5);
	        this.BLCWall.render(f5);
	        this.BBottomWedge.render(f5);
	        this.TopPlate.render(f5);
	        this.RWall.render(f5);
	        this.FBottomWedge.render(f5);
	        this.DoorLeft.render(f5);
	    }

	    /**
	     * This is a helper function from Tabula to set the rotation of model parts
	     */
	    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
	        modelRenderer.rotateAngleX = x;
	        modelRenderer.rotateAngleY = y;
	        modelRenderer.rotateAngleZ = z;
	    }

	}
