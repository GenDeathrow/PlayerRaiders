package com.gendeathrow.pmobs.client.gui;

import java.awt.Color;
import java.io.IOException;

import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.world.RaidersWorldDifficulty;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class RaidersReportGUI extends GuiScreen
{
	/*
	
	Raiders Spawn Percentage  Raiders | weight | % to Spawn|)
	
	Equipment %: Name | type (main|offhand|armor) | % to spawn
	
	Poison Arrow: name | potions effects | % to spawn raid difficutly X (0, 1, 2, 3, 4, 5, 6, 7, 8)
	
	 Combat
	
	  	Raid Difficulty X
	  	  Stats:
			 Health: % Chance to increase | normal:(min/max) Leader: (min/max)
			 Speed:  % Chance to increase | normal:(min/max)
			 Leap Attackers: % Chance to have

		  Equipment
			
			 Armor: % chance to get (feet|legs|chest|head)
			 
			 Weapons: % chance to get (Main Hand | OffHand)
	
	*/
	DifficultyProgression DiffProg;
	
	public RaidersReportGUI()
	{
		
	}
	
	@Override
    public void initGui()
    {
		if(Minecraft.getMinecraft().world != null)
			DiffProg = new DifficultyProgression(new EntityRaiderBase(Minecraft.getMinecraft().world));
		else Minecraft.getMinecraft().displayGuiScreen(null);
    }
	
	@Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
    	
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
		this.drawDefaultBackground();
		
		int i = 10;
		
		this.drawString(this.fontRenderer, "Current Raid Difficulty: "+ RaidersWorldDifficulty.INSTANCE.calculateRaidDifficulty(Minecraft.getMinecraft().world), 10, i, Color.WHITE.getRGB());

		//this.drawString(this.fontRendererObj, "Raid 1: "+ DiffProg.getRaidDifficulty(), 10, i * 2, Color.WHITE.getRGB());
		
		///this.drawString(this.fontRendererObj, "Speed %/ Raiders: "+ DiffProg.calculateProgressionDifficulty(DiffProg.speedDifficulty, DiffProg.speedDifficultyMax), 10, i * 3, Color.WHITE.getRGB());
		
		this.drawString(this.fontRenderer, "Health %/ Raiders: ", 10, i * 4, Color.WHITE.getRGB());
		//this.drawString(this.fontRendererObj, "   Health Bonus %: "+ DiffProg.calculateProgressionDifficulty(DiffProg.healthDifficulty, 1) +"%", 10, i * 6, Color.WHITE.getRGB());
		this.drawString(this.fontRenderer, "   or Small Health Bonus %: 100% ", 10, i * 7, Color.WHITE.getRGB());
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
    }
	

}
