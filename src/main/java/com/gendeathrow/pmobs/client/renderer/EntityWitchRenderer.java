package com.gendeathrow.pmobs.client.renderer;

import com.gendeathrow.pmobs.client.model.RaiderModel;
import com.gendeathrow.pmobs.client.model.RaiderWitchModel;
import com.gendeathrow.pmobs.client.model.renderer.layers.LayerFeatureRenderer;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class EntityWitchRenderer extends RenderBiped<EntityRaiderWitch> 
{
	private final ModelBiped defaultModel;
	    
	private boolean flag = false;
	    
	public EntityWitchRenderer(RenderManager renderManager)
	{
		super(renderManager, new RaiderWitchModel(0F), 0.5F);
		
		this.defaultModel = this.getMainModel();
		this.addLayer(new LayerBipedArmor(this) {
		    @Override
			protected void initArmor()
		    {
		        this.modelLeggings = new RaiderWitchModel(0.5F);
		        this.modelArmor = new RaiderWitchModel(1.0F);
		    }
		});
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerArrow(this));
		
		this.addLayer(new LayerFeatureRenderer(this) {
		    @Override
			protected void initModel()
		    {
		       this.layerModel = new RaiderWitchModel(0F);
		    }
		});
	        
        if(!PMSettings.renderNameTags) 
        	this.NAME_TAG_RANGE = 0;
        else 
        	this.NAME_TAG_RANGE = 64.0f;

		
	}
	    
	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    
	public RaiderModel getMainModel()
	{
		return (RaiderModel)super.getMainModel();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRaiderWitch entity) 
	{
		return entity.getLocationSkin();
	}
	
	@Override
	public void doRender(EntityRaiderWitch entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if(!PMSettings.renderNameTags)
		{
//			if(entity.getRaiderFaction() == EnumFaction.FRIENDLY)
//				this.NAME_TAG_RANGE = 64F;
//			else if(entity.getRaiderFaction() == EnumFaction.HOSTILE)
//				this.NAME_TAG_RANGE = 0;
		}

            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	    		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	}

	    
	/**
	 * Allows the render to do state modifications necessary before the model is rendered.
	 */
	@Override
	protected void preRenderCallback(EntityRaiderWitch entitylivingbaseIn, float partialTickTime)
	{
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);
	}
}
