package com.gendeathrow.pmobs.client.model.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class LayerFeatureRenderer implements LayerRenderer<EntityLivingBase>
{

//	protected static final ResourceLocation dirtOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/dirtySkin.png");
//	protected static final ResourceLocation bloodOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/bloodySkin.png");
//	protected static final ResourceLocation bloodyHandsOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/bloodyHands.png");
//	protected static final ResourceLocation HorrorOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/horror.png");
//	protected static final ResourceLocation stabbedOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/bleeding.png");
//	protected static final ResourceLocation TuxOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/Tux.png");
//	protected static final ResourceLocation butcherOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/butcher.png");
//	protected static final ResourceLocation russianOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/russianSoldier.png");
//	protected static final ResourceLocation martialOverlay = new ResourceLocation(RaidersCore.MODID ,"textures/entity/martialArtist.png");
		
	private ModelBiped layerModel;
	private RenderLivingBase<?> renderer;
	   
	public LayerFeatureRenderer(RenderLivingBase<?> rendererIn)
	{
		this.renderer = rendererIn;
		initDirt();
	}
	
    protected void initDirt()
    {
       this.layerModel = new RaiderModel(0F, true);
    }
    
	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) 
	{
		if (entitylivingbaseIn instanceof EntityRaiderBase)
		{


			this.layerModel.setModelAttributes(this.renderer.getMainModel());
			this.layerModel.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);

			
			if(((EntityRaiderBase)entitylivingbaseIn).getFeatures() != LayerFeatures.NONE)
			{
				GlStateManager.enableBlend();
					//this.renderer.bindTexture(((EntityRaiderBase)entitylivingbaseIn).features.resource);
					this.renderer.bindTexture(((EntityRaiderBase)entitylivingbaseIn).getFeatures().resource);
					this.layerModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
				GlStateManager.disableBlend();
			}

		}
	}

	@Override
	public boolean shouldCombineTextures() 
	{
		return true;
	}

}
