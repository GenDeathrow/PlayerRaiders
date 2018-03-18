package com.gendeathrow.pmobs.core.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.pmobs.common.items.BackupTransmitter;
import com.gendeathrow.pmobs.common.items.BruteSerum;
import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RegisterItems {

	
	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static Item backupTransmitter;
	public static Item satTransmitterPart;
	public static Item bruteSerum;
	public static Item bruteSerumSample;

	//public static FluidPump pump = new FluidPump();
	public static IForgeRegistry<Item> itemRegistry;
	
	
	public static Item setUpItem(Item item, String name) {
		return item.setRegistryName(new ResourceLocation(RaidersMain.MODID, name)).setUnlocalizedName(RaidersMain.MODID+"."+ name).setCreativeTab(RaidersMain.RaidersTab);
	}
	
	@SubscribeEvent
	public static void itemRegistry(RegistryEvent.Register<Item> event) {
		itemRegistry = event.getRegistry();
		
		itemRegistry.registerAll(backupTransmitter, satTransmitterPart, bruteSerum, bruteSerumSample);
	}
	
    @SubscribeEvent
	public static void registerRenderer(ModelRegistryEvent event) {
		//Register Items
		try {
			for (Field field : RegisterItems.class.getDeclaredFields()) {
				Object obj = field.get(null);
				
				if (obj instanceof Item) {
					Item item = (Item) obj;
					
					NonNullList<ItemStack> list = NonNullList.<ItemStack>create();
			
					item.getSubItems(RaidersMain.RaidersTab, list);
			
					if(list.size() > 1)
						for(ItemStack metaitem : list)
							ModelLoader.setCustomModelResourceLocation(item, metaitem.getMetadata(), new ModelResourceLocation(item.getRegistryName()+"_"+metaitem.getMetadata(), "inventory"));
					else
						ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
					
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}


	}

	
	static 
	{
		backupTransmitter = setUpItem(new BackupTransmitter(), "backup_transmitter");
		bruteSerum = setUpItem(new BruteSerum(), "brute_serum");
		
		satTransmitterPart = setUpItem(new Item(){
		}.setNoRepair(), "sat_transmitter_part");
		

		bruteSerumSample = setUpItem(new Item(){
			}.setNoRepair(), "brute_serum_sample");
	}
}
