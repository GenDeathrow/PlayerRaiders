package com.gendeathrow.pmobs.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.client.audio.CryingWitch;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.core.network.RaiderDeathCntPacket;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

import funwayguy.epicsiegemod.api.EsmTaskEvent;


public class EventHandler 
{


    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
 	
    @SubscribeEvent
	public void spawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
 		if((PMSettings.safeForaDay && event.getEntity() instanceof EntityMob &&  event.getWorld().getWorldTime() < 13000))
 		{
 			if(event.getEntity().posY > 50)
 			{
 				event.setResult(Result.DENY);
 			}
 		}else if(event.getEntity() instanceof EntityRaiderBase)
 		{
 			int day = (int) (event.getWorld().getWorldTime() / 24000);
 			
 			for(int noday : PMSettings.noSpawnDays)
 			{
 				if(day == noday)
 				{
 					event.setResult(Result.DENY);
 					return;
 				}

 			}
 		}
	}

	@SubscribeEvent
	public void playerKilled(LivingDeathEvent event)
	{
		if (event.getEntity().worldObj.isRemote || event.getSource() == null || event.getSource().getSourceOfDamage() == null || event.isCanceled())
			return;
		
		if(!event.getEntity().worldObj.isRemote && event.getEntity() instanceof EntityPlayerMP)
		{

			if (event.getSource().getSourceOfDamage() instanceof EntityLiving)
			{
				RaidersCore.network.sendTo(new RaiderDeathCntPacket(event.getSource().getSourceOfDamage().getName(), event.getSource().getSourceOfDamage().getName()), (EntityPlayerMP) event.getEntity());
			}
		}
	
	}
	
	public void getPotentialSpwans(WorldEvent.PotentialSpawns event)
	{
//		if(event.getType() == RaidersCore.HostileWaterMobs)
//		{
//    		List<EntityRaiderBase> list = event.getWorld().getEntities(EntityRaiderBase.class,  new Predicate<EntityRaiderBase>() 
//  	    		{
//  	    			@Override public boolean apply(EntityRaiderBase number) 
//   	    			{
//   	    				return true;
//   	    			}       
//   	    		});
//	    		
//			if(list.size() >= RaidersCore.HostileWaterMobs.getMaxNumberOfCreature())
//			{
//				event.setCanceled(true);
//			}
//				
//		}
			
			
	}
	
	@SubscribeEvent
	public void getESM(EsmTaskEvent.Addition event)
	{
		if(event.getEntity() instanceof EntityRaiderBase)
		{
			EntityRaiderBase raider = (EntityRaiderBase) event.getEntity();
			
			if(event.getAddition() != null)
			{
//				System.out.println(event.getAddition().getAdditionalAI(event.getEntity()).getClass().getSimpleName());
			
				EnumRaiderRole raiderClass = raider.getRaiderRole();
				
				String className = event.getAddition().getAdditionalAI(raider).getClass().getSimpleName(); 
	
				if(className.equalsIgnoreCase("ESM_EntityAIDemolition"))
				{
					//System.out.println("ESM_EntityAIDemolition--"+ raiderClass.name() + ":"+ raider.getDifficultyProgession().getRaidDifficulty());
					
					boolean flag = false;
					boolean holdingTnt = raider.getHeldItemOffhand() != null && raider.getHeldItemOffhand().getItem() == Item.getItemFromBlock(Blocks.TNT);
					
					if(raider.getDifficultyProgession().getRaidDifficulty() < PMSettings.esmDemolitionRaidDiff)
					{
						//System.out.println("Denied ESM_EntityAIDemolition for under raid dificulty 1");
						flag = true;
					}
					else if(raiderClass != EnumRaiderRole.NONE || raider.isHeroBrine() || raider.isChild())
					{
						//System.out.println("Denied ESM_EntityAIDemolition for "+ raider.getOwner() + ">>"+ raiderClass);
						flag = true;
					}else if(holdingTnt && raider.getRNG().nextDouble() > PMSettings.esmDemoPercentage )
					{
						flag = true;
					}
					
					if(flag)
					{
						event.setResult(Result.DENY);
						
						if(holdingTnt)
						{
							if(raiderClass == EnumRaiderRole.PYROMANIAC) 
								raider.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.FLINT_AND_STEEL));
							else raider.setHeldItem(EnumHand.OFF_HAND, null);
						}
					}
				}
				else if(className.equalsIgnoreCase("ESM_EntityAIDigging"))
				{
					if((raider.isChild() || raiderClass != EnumRaiderRole.NONE) && raider.getRNG().nextFloat() > PMSettings.esmDiggingPercentage)
					{
						event.setResult(Result.DENY);
					}
				}
				
				
			}
		}
		
	}
	
	
	@SubscribeEvent
	public void getESM(EsmTaskEvent.Modified event)
	{
		if(event.getEntity() instanceof EntityRaiderBase)
		{
			EntityRaiderBase raider = (EntityRaiderBase) event.getEntity();
			
			if(event.getModifier() != null)
			{

				//System.out.println(event.getModifier().getClass().getSimpleName());
				//ModifierNearestAttackable
			}
		}
		
	}
	
	@SubscribeEvent
	public void onEntitySpawn(EntityJoinWorldEvent event)
	{

	}
		
		
    boolean hasChecked = false;
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {
    	
    	if(!event.player.worldObj.isRemote || hasChecked)
    	{
    		return;
    	}
    	hasChecked = true;
			
    	if(EquipmentManager.ErrorList.size() > 0)
    	{
    		event.player.addChatComponentMessage(new TextComponentTranslation(EquipmentManager.ErrorList.size() + " Errors were found in Raiders Equipment json. Check Console for more info."));
    	}
			
    	//RaidersSkinManager.INSTANCE.cacheSkins();
    	RaidersSkinManager.INSTANCE.cacheSkins();	
    }
		
		
//	    @SubscribeEvent
//	    public void onLootTablesLoaded (LootTableLoadEvent event) 
//	    {
//	    	
//	    	
//	    }
		
		
}
