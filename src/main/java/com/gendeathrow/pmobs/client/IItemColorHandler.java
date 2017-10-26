package com.gendeathrow.pmobs.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

import com.gendeathrow.pmobs.common.items.SpecialSpawnEgg;
import com.gendeathrow.pmobs.common.items.SpecialSpawnEgg.RaiderEggInfo;

public class IItemColorHandler implements IItemColor
{

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) 
    { 	
    	RaiderEggInfo entitylist$entityegginfo = (RaiderEggInfo)SpecialSpawnEgg.RAIDERS_EGGS.get(SpecialSpawnEgg.getEntityIdFromItem(stack));
        return entitylist$entityegginfo == null ? -1 : (tintIndex == 0 ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor);
    }
    
    

}
