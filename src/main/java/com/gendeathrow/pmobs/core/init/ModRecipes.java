package com.gendeathrow.pmobs.core.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes 
{

	public static void RegisterOreDic()
	{

	}
	
	public static void RegisterRecipes()
	{

		GameRegistry.addRecipe(new ItemStack(ModItems.backupTransmitter), 
				"RPR", 
				"ICI", 
				"IEI",
				'C', Items.COMPASS,
				'R', Items.REPEATER,
				'P', Items.COMPARATOR,
				'E', Items.EMERALD,
				'I', Items.IRON_INGOT);

	}
	
}
