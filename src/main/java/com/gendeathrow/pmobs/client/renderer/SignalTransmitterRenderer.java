package com.gendeathrow.pmobs.client.renderer;

import com.gendeathrow.pmobs.client.model.renderer.SignalTransmitterModel;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.EntitySignalTransmitter;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class SignalTransmitterRenderer extends Render<EntitySignalTransmitter>
{
	public static final Factory FACTORY = new Factory();
	
	private static ResourceLocation texture = new ResourceLocation(RaidersCore.MODID, "textures/entity/signaltransmitter.png");
	
	private final ModelBase model = new SignalTransmitterModel();
	
	protected SignalTransmitterRenderer(RenderManager renderManager) 
	{
		super(renderManager);
		this.shadowSize = 0.75F;
	}
	
	@Override
    public void doRender(EntitySignalTransmitter entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
	       GlStateManager.pushMatrix();
	        GlStateManager.disableCull();
	        GlStateManager.translate((float)x, (float)y, (float)z);
			GlStateManager.scale(-1D, -1D, -1D);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.enableAlpha();  
	        
	        this.bindEntityTexture(entity);

	        GlStateManager.translate(0, -1.2f, 0);
	        float ticks = entity.animationTicks + (entity.animationTicks - entity.prevAnimationTicks) * partialTicks;
	        
	        this.model.render(entity, 0.0F,  ticks * 3.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
	        GlStateManager.popMatrix();
	        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
	@Override
	protected ResourceLocation getEntityTexture(EntitySignalTransmitter entity) 
	{
		return texture;
	}

	
	public SignalTransmitterModel getMainModel()
	{
		return (SignalTransmitterModel) model;
	}
	
	public static class Factory implements IRenderFactory<EntitySignalTransmitter> 
	{
		@Override
		public Render<? super EntitySignalTransmitter> createRenderFor(RenderManager manager) 
		{
			return new SignalTransmitterRenderer(manager);
		}
	}
		

}

