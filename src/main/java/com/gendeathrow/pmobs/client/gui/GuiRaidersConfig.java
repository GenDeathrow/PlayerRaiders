package com.gendeathrow.pmobs.client.gui;

import java.util.ArrayList;

import com.gendeathrow.pmobs.core.ConfigHandler;
import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiRaidersConfig extends GuiConfig
{

	public GuiRaidersConfig(GuiScreen parent)
	{
		super(parent, getCategories(ConfigHandler.config), RaidersCore.MODID, false, false, RaidersCore.NAME);
	}
	
	public static ArrayList<IConfigElement> getCategories(Configuration config)
	{
		ArrayList<IConfigElement> cats = new ArrayList<IConfigElement>();
		
		for(String s : config.getCategoryNames())
		{
			cats.add(new ConfigElement(config.getCategory(s)));
		}
		
		return cats;
	}
	

}