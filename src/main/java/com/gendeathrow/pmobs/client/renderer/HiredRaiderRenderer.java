package com.gendeathrow.pmobs.client.renderer;



import java.util.HashMap;

import com.gendeathrow.pmobs.client.model.renderer.HiredRaiderModel;
import com.gendeathrow.pmobs.entity.HiredRaiders.HiredRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HiredRaiderRenderer extends RenderBiped<HiredRaider> 
{
	public static final Factory FACTORY = new Factory();

	private final ModelBiped defaultModel;
	    
	private static ResourceLocation DEFAULT = new ResourceLocation("textures/entity/steve.png");

	private static HashMap<String, ResourceLocation> SkinsCache = new HashMap<String, ResourceLocation>();
	    //private final List<LayerRenderer<EntityPlayerBase>> defaultLayers;  
	    
	private boolean flag = false;
	    
	public HiredRaiderRenderer(RenderManager renderManager)
	{
		super(renderManager, new HiredRaiderModel(0F), 0.5F);
		
		this.defaultModel = this.modelBipedMain;
		this.addLayer(new LayerBipedArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
	        
//	        this.addLayer(new LayerBipedArmor(this)
//	        {
//	            protected void initArmor()
//	            {
//	                this.modelLeggings = new RaiderModel(0.5F);
//	                this.modelArmor = new RaiderModel(1.0F);
//	            }
//	        });
//	        
	        
//		if(PMSettings.renderOverlays)
//		{
//			this.addLayer(new LayerFeatureRenderer(this));
//		}
	}
	    
	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    
	public HiredRaiderModel getMainModel()
	{
		return (HiredRaiderModel)super.getMainModel();
	}

	protected ResourceLocation getEntityTexture(HiredRaider entity) 
	{
		return entity.getLocationSkin() == null ? DefaultPlayerSkin.getDefaultSkinLegacy() : entity.getLocationSkin();
	}
	
	@Override
	public void doRender(HiredRaider entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	    		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}

	    
	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	@Override
	protected void preRenderCallback(HiredRaider entitylivingbaseIn, float partialTickTime)
	{
	        if (entitylivingbaseIn.getRaiderRole() == EnumRaiderRole.BRUTE)
	        {  
	            float f = 1.0625F;
	            GlStateManager.scale(1.5F, 1.5F, 1.5F);
	        }
	        
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}
	    
	public static class Factory implements IRenderFactory<HiredRaider> 
	{
		@Override
		public Render<? super HiredRaider> createRenderFor(RenderManager manager) 
		{
			return new HiredRaiderRenderer(manager);
		}
	}
		
}
