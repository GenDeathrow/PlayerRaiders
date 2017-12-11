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
import net.minecraft.entity.EntityLivingBase;

public class LayerFeatureRenderer implements LayerRenderer<EntityLivingBase>
{
		
	private ModelBiped layerModel;
	private RenderLivingBase<?> renderer;
	   
	public LayerFeatureRenderer(RenderLivingBase<?> rendererIn)
	{
		this.renderer = rendererIn;
		initModel();
	}
	
    protected void initModel()
    {
       this.layerModel = new RaiderModel(0F);
    }
    
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if (entitylivingbaseIn instanceof EntityRaiderBase)
		{

			this.layerModel.setModelAttributes(this.renderer.getMainModel());
			this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);

			if(((EntityRaiderBase)entitylivingbaseIn).getFeatures() != LayerFeatures.NONE 
					&& !entitylivingbaseIn.isInvisible() 
					&& PMSettings.renderOverlays) {
	            	
					GlStateManager.pushMatrix();
		            	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						GlStateManager.enableBlend();
						GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
							this.renderer.bindTexture(((EntityRaiderBase)entitylivingbaseIn).getFeatures().resource);
							this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						GlStateManager.disableBlend();
					GlStateManager.popMatrix();
			}

		}
	}
	
	@Override
	public boolean shouldCombineTextures() 
	{
		return true;
	}

}
