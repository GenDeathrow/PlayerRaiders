package com.gendeathrow.pmobs.client.gui;

import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RaidNotification 
{
	
	static ArrayList<QuestNotice> notices = new ArrayList<QuestNotice>();

	
	public static void ScheduleNotice(ArrayList<String> lines, String sound)
	{
		//Temp fix for only having one notic
		notices.add(0, new QuestNotice(lines, sound));

	}
	
	@SubscribeEvent
	public static void onDrawScreen(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() != RenderGameOverlayEvent.ElementType.HELMET || notices.size() <= 0)
		{
			return;
		}
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int width = resolution.getScaledWidth();
		int height = resolution.getScaledHeight();
		QuestNotice notice = notices.get(0);
		
		if(!notice.init)
		{
			if(mc.isGamePaused() || mc.currentScreen != null)
			{
				return; // Do not start showing a new notice if the player isn't looking
			}
			
			notice.init = true;
			notice.startTime = Minecraft.getSystemTime();
			mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(new SoundEvent(new ResourceLocation(notice.sound)), 1.0F));
		}
		
		if(notice.getTime() >= 6F)
		{
			notices.remove(0);
			return;
		}
		
		GlStateManager.pushMatrix();
		
		float scale = width > 600? 1.5F : 1F;
		
		GlStateManager.scale(scale, scale, scale);
		width = MathHelper.ceil(width/scale);
		height = MathHelper.ceil(height/scale);
		
		float alpha = notice.getTime() <= 4F? Math.min(1F, notice.getTime()) : Math.max(0F, 5F - notice.getTime());
		alpha = MathHelper.clamp(alpha, 0.02F, 1F);
		int color = new Color(1F, 1F, 1F, alpha).getRGB();
		
		GlStateManager.color(1F, 1F, 1F, alpha);
		
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
     	
		String tmp = TextFormatting.RED + "" + TextFormatting.UNDERLINE + "" + TextFormatting.BOLD + I18n.format(notice.getLine(0));
		int txtW = mc.fontRenderer.getStringWidth(tmp);
		mc.fontRenderer.drawString(tmp, width/2 - txtW/2, height/4, color, true);
		
		
		for(int i = 1; i < notice.lines.size(); i++) {
			if(i >= 12) break;
			
			tmp = TextFormatting.YELLOW + "" + I18n.format(notice.getLine(i));
			
			if(i == 11)
				tmp = TextFormatting.YELLOW + "Additional "+ (notice.getLinesSize() - 10) +" Changes";
			
			txtW = mc.fontRenderer.getStringWidth(tmp);
			mc.fontRenderer.drawString(tmp, width/2 - txtW/2, height/4 + (12 * i), color, true);
		}
		
		//GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
	
	public static class QuestNotice
	{
		long startTime = 0;
		public boolean init = false;
		public ArrayList<String> lines = new ArrayList<String>();
		public ItemStack icon = null;
		public String sound = "random.levelup";
		
		public QuestNotice(ArrayList<String> linesIn, String sound)
		{
			this.startTime = Minecraft.getSystemTime();
			lines = linesIn;
			this.sound = sound;
		}
		
		public String getLine(int line) {
			if(validate(line))
				return lines.get(line);
			return "";
		}
		
		private boolean validate(int line) {
			if(line < lines.size())
				return true;
			return false;
		}
		
		public int getLinesSize() {
			return lines.size();
		}
		
		
		public float getTime()
		{
			return (Minecraft.getSystemTime() - startTime)/1000F;
		}
	}
}