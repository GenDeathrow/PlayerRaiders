package com.gendeathrow.pmobs.client.renderer;

import com.gendeathrow.pmobs.client.model.DropPodModel;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.entity.neutral.EntityDropPod;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class DropPodRenderer extends Render<EntityDropPod>
{

	private static ResourceLocation texture = new ResourceLocation(RaidersMain.MODID, "textures/entity/droppod.png");
	
	protected final ModelBase model = new DropPodModel();
	
	public DropPodRenderer(RenderManager renderManager) 
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
	
}
