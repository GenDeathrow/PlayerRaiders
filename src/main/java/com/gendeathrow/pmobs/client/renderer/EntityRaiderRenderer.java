package com.gendeathrow.pmobs.client.renderer;



import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.client.model.renderer.LayerFeatureRenderer;
import com.gendeathrow.pmobs.client.model.renderer.RaiderModel;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.google.common.base.Charsets;

public class EntityRaiderRenderer extends RenderBiped<EntityRaiderBase> 
{
		public static final Factory FACTORY = new Factory();
		 
	   // public ModelPlayer modelBipedMain;
	    
	    private final ModelBiped defaultModel;
	    
	    private static ResourceLocation DEFAULT = new ResourceLocation("textures/entity/steve.png");

	    private static HashMap<String, ResourceLocation> SkinsCache = new HashMap<String, ResourceLocation>();
	    //private final List<LayerRenderer<EntityPlayerBase>> defaultLayers;  
	    
	    private boolean flag = false;
	    
	    public EntityRaiderRenderer(RenderManager renderManager)
	    {
	    	super(renderManager, new RaiderModel(.0F, true), 0.5F);
	        
	        this.defaultModel = this.modelBipedMain;
	        
	        this.addLayer(new LayerBipedArmor(this));
	        this.addLayer(new LayerHeldItem(this));
	        this.addLayer(new LayerArrow(this));
	        
	        if(PMSettings.renderOverlays)
	        {

	        		this.addLayer(new LayerFeatureRenderer(this));
	        }
	        
	        if(!PMSettings.renderNameTags) 
	        {
	        	this.NAME_TAG_RANGE = 0;
	        }else 
	        {
	        	this.NAME_TAG_RANGE = 64.0f;
	        }
	    }
	    
	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    public void doRender(EntityPlayerRaider entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	    	super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }
	    


	    private void swapArmor(EntityPlayerRaider zombie)
	    {

            this.mainModel = this.defaultModel;
            //this.layerRenderers = this.defaultLayers;

            this.modelBipedMain = (ModelBiped)this.mainModel;
	    }
	    
	    //
	    public RaiderModel getMainModel()
	    {
	        return (RaiderModel)super.getMainModel();
	    }
	    
//	    @Override
//	    public void bindTexture(ResourceLocation location)
//	    {
//	        this.renderManager.renderEngine.bindTexture(location);
//	    }

		@Override
		protected ResourceLocation getEntityTexture(EntityRaiderBase entity) 
		{
			return entity.getLocationSkin() == null ? DefaultPlayerSkin.getDefaultSkinLegacy() : entity.getLocationSkin();
		}
		
	    protected void preRenderCallback(EntityPlayerRaider entity, float partialTickTime)
	    {
	    	
	    	
	    }
		
		public static class Factory implements IRenderFactory<EntityRaiderBase> 
	  	{
		    @Override
		    public Render<? super EntityRaiderBase> createRenderFor(RenderManager manager) 
		    {
		      return new EntityRaiderRenderer(manager);
		    }
	  	}
		
}
