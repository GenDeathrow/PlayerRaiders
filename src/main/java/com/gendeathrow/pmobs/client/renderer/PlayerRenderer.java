package com.gendeathrow.pmobs.client.renderer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.gendeathrow.pmobs.client.model.renderer.RaiderModel;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.New.EntityPlayerBase;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

public class PlayerRenderer extends RenderBiped<EntityPlayerBase> 
{
		public static final Factory FACTORY = new Factory();
		 
	   // public ModelPlayer modelBipedMain;
	    
	    private final ModelBiped defaultModel;
	    
	    private static ResourceLocation DEFAULT = new ResourceLocation("textures/entity/steve.png");
	    
	    private static HashMap<String, ResourceLocation> SkinsCache = new HashMap<String, ResourceLocation>();
	    private final List<LayerRenderer<EntityPlayerBase>> defaultLayers;  
	    
	    private boolean flag = false;
	    
	    public PlayerRenderer(RenderManager renderManager)
	    {
	        //super(renderManager, new ModelPlayer(.75F, true), 0.5F); 
	    	super(renderManager, new RaiderModel(.75F, true), 0.5F);
	        
	        this.defaultModel = this.modelBipedMain;
	        
	        this.addLayer(new LayerHeldItem(this));
	        
	        this.addLayer(new LayerArrow(this));
	        
	        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
	        
	        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this);
	        
	        this.addLayer(layerbipedarmor);
	        this.defaultLayers = Lists.newArrayList(this.layerRenderers);
	        
	        if(!PMSettings.renderNameTags) 
	        {
	        	this.NAME_TAG_RANGE = 0;
	        }else 
	        {
	        	this.NAME_TAG_RANGE = 64.0f;
	        }

	        //this.removeLayer(layerbipedarmor);
	    }
	    
	    public void registerSkins()
	    {
	    	
	    }
	    
	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    public void doRender(EntityPlayerRaider entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
	        this.swapArmor(entity);
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
	    }
	    

	    private void swapArmor(EntityPlayerRaider zombie)
	    {

            this.mainModel = this.defaultModel;
            this.layerRenderers = this.defaultLayers;

            this.modelBipedMain = (ModelBiped)this.mainModel;
	    }
	    //
	    public ModelPlayer getMainModel()
	    {
	        return (ModelPlayer)super.getMainModel();
	    }

		@Override
		protected ResourceLocation getEntityTexture(EntityPlayerBase entity) 
		{

			return entity.getLocationSkin();
		}
		
	    protected void preRenderCallback(EntityPlayerRaider entity, float partialTickTime)
	    {
	    	
	    	
	    }
		
		public static class Factory implements IRenderFactory<EntityPlayerBase> 
	  	{
		    @Override
		    public Render<? super EntityPlayerBase> createRenderFor(RenderManager manager) 
		    {
		      return new PlayerRenderer(manager);
		    }
	  	}
		
	    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username)
	    {
	        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
	        ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);


	            itextureobject = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] {StringUtils.stripControlCodes(username)}), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
	            

	            texturemanager.loadTexture(resourceLocationIn, itextureobject);

	        return (ThreadDownloadImageData)itextureobject;
	    }
	    
	    public static UUID getOfflineUUID(String username)
	    {
	        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
	    }


}
