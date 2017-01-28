package com.gendeathrow.pmobs.client;

import java.lang.reflect.Field;

import com.gendeathrow.pmobs.client.data.KillCounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler 
{

	private static String whoKilled = "";
	
	@SubscribeEvent
	public void playerKilled(LivingDeathEvent event)
	{
		if(!event.getEntity().worldObj.isRemote) return;
		
		if(event.getEntity() != null)
		{
			if(event.getEntityLiving() == Minecraft.getMinecraft().thePlayer)
			{
				whoKilled = "";
		
				if (event.getSource().getSourceOfDamage() instanceof EntityLiving)
				{
					whoKilled = event.getSource().getEntity().getName();
					KillCounter.addKillCount(whoKilled);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void guiinit(GuiOpenEvent event)
	{
		if(event.getGui() == null) return;
		System.out.println(event.getGui().getClass().getName());
	}
	
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
			//100this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + TextFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
		}
	}
	
	
	
}
