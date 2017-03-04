package com.gendeathrow.pmobs.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.pmobs.client.audio.CryingWitch;
import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

public class ClientEventHandler 
{

	public static String whoKilled = "";
	
	@SubscribeEvent
	public void showDeathCount(DrawScreenEvent.Post event)
	{  
		if(event.getGui() == null) return;

		if(event.getGui() instanceof GuiGameOver) 
		{
			
			if(whoKilled != "")
			{
				String message = TextFormatting.YELLOW + whoKilled +" has killed you "+ KillCounter.getKillCount(whoKilled) + " times!";
				event.getGui().drawString(Minecraft.getMinecraft().fontRendererObj, message, (event.getGui().width) / 2 - ( Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) / 2) , 115, 16777215);
			}			
		}
	}
	
	
    @SubscribeEvent
	public void spawnEvent(EntityJoinWorldEvent event)
	{
    	if(!event.getWorld().isRemote) return;
    	
    	if(event.getEntity() instanceof EntityRangedAttacker)
    	{
    		if(((EntityRangedAttacker)event.getEntity()).getRaiderRole() == EnumRaiderRole.WITCH)
    		{
    			Minecraft.getMinecraft().getSoundHandler().playSound(new CryingWitch((EntityRangedAttacker) event.getEntity()));
    		}
    	}
	}
    
    
	boolean witchNear = false;
	int maxDistance = 35;
	@SubscribeEvent
	public void fogRender(RenderFogEvent event)
	{
		this.witchNear = false;
		float fogDistance = 0;
		
		if(event.getEntity() instanceof EntityPlayer && event.getEntity().worldObj != null)
		{

			EntityPlayer player = (EntityPlayer) event.getEntity();
			AxisAlignedBB box = player.getEntityBoundingBox();
					
			if(box == null) return;
			
			List<EntityRaiderBase> list = player.worldObj.getEntitiesWithinAABB(EntityRaiderBase.class, box.expand(maxDistance, maxDistance, maxDistance));
			
			for(EntityRaiderBase entity : list)
			{
				if(entity.getRaiderRole() == EnumRaiderRole.WITCH)
				{
					if(((EntityRangedAttacker)entity).isWitchActive())
					{
						this.witchNear = true;
						float percentage = (float) Math.pow(entity.getDistanceToEntity(player)/maxDistance, 5);
						fogDistance = (float) percentage * event.getFarPlaneDistance();
					}
				}
			}
		}
		
		if(this.witchNear) 
		{
			int fogStart = 8;
			int fogEnd = 20;
			
			GlStateManager.setFogDensity(0.1f);
			GlStateManager.setFogStart(fogStart);		
			GlStateManager.setFogEnd(Math.max(fogDistance,10));	
			
		}
	}
	
}
