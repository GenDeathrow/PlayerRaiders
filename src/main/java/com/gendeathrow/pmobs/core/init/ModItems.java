package com.gendeathrow.pmobs.core.init;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.pmobs.common.items.BackupTransmitter;
import com.gendeathrow.pmobs.common.items.BruteSerum;
import com.gendeathrow.pmobs.common.items.SpecialSpawnEgg;
import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	//ITEMS
	public static BackupTransmitter backupTransmitter = new BackupTransmitter();
	public static Item satTransmitterPart = new Item(){
		@Override
		public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
		{
			tooltip.add("Drops from Raiders");
		}

	}.setNoRepair().setCreativeTab(RaidersCore.RaidersTab);
	public static Item spawnEgg = new SpecialSpawnEgg();
	public static Item bruteSerum = new BruteSerum();
	public static Item bruteSerumSample = new Item(){
			
			@Override
			public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
			{
				tooltip.add("Drops from Brute Raiders");
			}

		}.setNoRepair().setCreativeTab(RaidersCore.RaidersTab);
	
	
	
	public static void RegisterItems()
	{
		registerItem(ModItems.backupTransmitter, "backup_transmitter");
		
		registerItem(ModItems.spawnEgg, "raider_custom_egg");
		
		registerItem(ModItems.bruteSerum, "brute_serum");
		
		registerItem(ModItems.satTransmitterPart, "sat_transmitter_part");
		
		registerItem(ModItems.bruteSerumSample, "brute_serum_sample");
	}
	
	
	private static void registerItem(Item item, String name)
	{
		item.setUnlocalizedName(RaidersCore.MODID +"."+ name);
		GameRegistry.register(item.setRegistryName(name));
		ITEMS.add(item);
	}
	

}
