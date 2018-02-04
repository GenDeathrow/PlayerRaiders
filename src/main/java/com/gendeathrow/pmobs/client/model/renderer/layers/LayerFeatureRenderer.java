package com.gendeathrow.pmobs.client.model.renderer.layers;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.model.RaiderModel;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerFeatureRenderer implements LayerRenderer<EntityRaiderBase>
{
		
	protected ModelBiped layerModel;
	private RenderLivingBase<?> renderer;
	private RaiderModel mainModel;
	   
	public LayerFeatureRenderer(RenderLivingBase<?> rendererIn)
	{
		this.renderer = rendererIn;
		mainModel = (RaiderModel) rendererIn.getMainModel();
		initModel();
	}
	
    protected void initModel()
    {
       this.layerModel = new RaiderModel(0F);
    }
    
	@Override
	public void doRenderLayer(EntityRaiderBase raider, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
			this.layerModel.setModelAttributes(this.renderer.getMainModel());
			this.layerModel.setLivingAnimations(raider, limbSwing, limbSwingAmount, partialTicks);

//			this.layerModel.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, raider);
			
			if(raider.getFeatures() != LayerFeatures.NONE 
					&& !raider.isInvisible() 
					&& PMSettings.renderOverlays) {
	            	
					GlStateManager.pushMatrix();
		            	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						GlStateManager.enableBlend();
						GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
							this.renderer.bindTexture(raider.getFeatures().resource);
							this.layerModel.render(raider, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						GlStateManager.disableBlend();
					GlStateManager.popMatrix();
			}
	}
	
	@Override
	public boolean shouldCombineTextures() 
	{
		return true;
	}

}
