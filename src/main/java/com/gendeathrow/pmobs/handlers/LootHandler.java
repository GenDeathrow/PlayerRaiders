package com.gendeathrow.pmobs.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootHandler 
{

	public LootHandler()
	{
	 ResourceLocation a = LootTableList.CHESTS_ABANDONED_MINESHAFT;
	}

	@SubscribeEvent
	public static void loadEvent(LootTableLoadEvent event)
	{
	
		LootTable table = event.getTable();
		
		LootPool pool = new LootPool(null, null, null, null, null);
		
		
		
	}
}
