package com.gendeathrow.pmobs.client.renderer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import com.gendeathrow.pmobs.client.model.renderer.DropPodModel;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class DropPodRenderer extends Render<EntityDropPod>
{
	public static final Factory FACTORY = new Factory();
	
	private static ResourceLocation texture = new ResourceLocation(RaidersCore.MODID, "textures/entity/droppod.png");
	
	private final ModelBase model = new DropPodModel();
	
	protected DropPodRenderer(RenderManager renderManager) 
	{
		super(renderManager);
		this.shadowSize = 1.2F;
	}
	
	@Override
    public void doRender(EntityDropPod entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
	       GlStateManager.pushMatrix();
	        GlStateManager.disableCull();
	        GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.scale(-1D, -1D, -1D);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.enableAlpha();
	        
	        this.bindEntityTexture(entity);

	        GlStateManager.translate(0, -1.2f, 0);
	        this.model.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);

	        GlStateManager.popMatrix();
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(EntityDropPod entity) 
	{
		return texture;
	}

	
	public DropPodModel getMainModel()
	{
		return (DropPodModel) model;
	}
	
	public static class Factory implements IRenderFactory<EntityDropPod> 
	{
		@Override
		public Render<? super EntityDropPod> createRenderFor(RenderManager manager) 
		{
			return new DropPodRenderer(manager);
		}
	}
		

}
