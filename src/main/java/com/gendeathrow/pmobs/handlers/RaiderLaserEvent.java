package com.gendeathrow.pmobs.handlers;

import com.gendeathrow.pmobs.entity.ai.EntityAIShootLaser;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RaiderLaserEvent {
	@SubscribeEvent
	public void onRaiderSpawn(EntityJoinWorldEvent event) 
	{
		if (event.getEntity() instanceof EntityLivingBase) 
		{
			EntityLivingBase raider = (EntityLivingBase) event.getEntity();
			if (raider instanceof EntityZombie) 
			{
				World world = raider.world;
				if (!world.isRemote) 
				{
					if (raider.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == null) 
					{
						if (Loader.isModLoaded("lcrdrfs")) 
						{
							ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("lcrdrfs:laser_blaster")), 1, 0);
							raider.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
							((EntityLiving) raider).setDropChance(EntityEquipmentSlot.MAINHAND, 1F);
							((EntityZombie) raider).targetTasks.addTask(0, new EntityAINearestAttackableTarget((EntityZombie) raider, EntityLivingBase.class, 0, true, false, null));
							((EntityZombie) raider).tasks.addTask(2, new EntityAIShootLaser((EntityZombie)raider));
						}
					}
				}
			}
		}
	}
}