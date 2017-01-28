package com.gendeathrow.pmobs.client.renderer;



import java.util.HashMap;

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

import com.gendeathrow.pmobs.client.model.renderer.RaiderModel;
import com.gendeathrow.pmobs.client.model.renderer.layers.LayerFeatureRenderer;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;

@SideOnly(Side.CLIENT)
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
	    	super(renderManager, new RaiderModel(0F), 0.5F);
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
	        
	        if(PMSettings.renderOverlays)
	        {
	        		this.addLayer(new LayerFeatureRenderer(this));
	        }
	    }
	    
	    /**
	     * Renders the desired {@code T} type Entity.
	     */
	    
	    public void doRender(EntityPlayerRaider entity, double x, double y, double z, float entityYaw, float partialTicks)
	    {
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
//	        if (entity.getRaiderRole() == EnumRaiderRole.BRUTE)
//	        {
//	        	this.scale = 10F;
//	        }
	    		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	        GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
	    }

	    
//	    public float prepareScale(EntityPlayerRaider entitylivingbaseIn, float partialTicks)
//	    {
//	        GlStateManager.enableRescaleNormal();
//	        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
//	        GlStateManager.scale(10.5F, 10.5F, 10.5F);
//	        System.out.println("c");
//	        float f = 0.0625F;
//	        GlStateManager.translate(0.0F, -1.501F, 0.0F);
//	        return 0.0625F;
//	    }

	    
	    
	    /**
	     * Allows the render to do state modifications necessary before the model is rendered.
	     */
	    @Override
	    protected void preRenderCallback(Entity entitylivingbaseIn, float partialTickTime)
	    {
	        super.preRenderCallback(entitylivingbaseIn, partialTickTime);

	    	System.out.println("c");
	    	
            GlStateManager.scale(10.5F, 10.5F, 10.5F);
            
	        if (entitylivingbaseIn.getRaiderRole() == EnumRaiderRole.BRUTE)
	        {
	            float f = 1.0625F;
	            System.out.println("b");
	            GlStateManager.scale(10.5F, 10.5F, 10.5F);
	        }
	    }
	    //
	    public RaiderModel getMainModel()
	    {
	        return (RaiderModel)super.getMainModel();
	    }

		@Override
		protected ResourceLocation getEntityTexture(EntityRaiderBase entity) 
		{
			return entity.getLocationSkin() == null ? DefaultPlayerSkin.getDefaultSkinLegacy() : entity.getLocationSkin();
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
