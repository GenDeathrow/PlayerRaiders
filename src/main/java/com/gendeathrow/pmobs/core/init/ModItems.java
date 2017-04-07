package com.gendeathrow.pmobs.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.pmobs.common.items.BackupTransmitter;
import com.gendeathrow.pmobs.core.RaidersCore;

public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//ITEMS
	public static BackupTransmitter backupTransmitter = new BackupTransmitter();
	
	public static void RegisterItems()
	{
		registerItem(ModItems.backupTransmitter, "backup_transmitter");
	}
	
	
	private static void registerItem(Item item, String name)
	{
		item.setUnlocalizedName(RaidersCore.MODID +"."+ name);
		GameRegistry.register(item.setRegistryName(name));
		ITEMS.add(item);
	}
	

}
