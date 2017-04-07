package com.gendeathrow.pmobs.client;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.client.audio.CryingWitch;
import com.gendeathrow.pmobs.client.audio.DropPodMoving;
import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.client.gui.RaidNotification;
import com.gendeathrow.pmobs.common.SoundEvents;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.handlers.EquipmentManager;

@SideOnly(Side.CLIENT)
public class ClientEventHandler 
{

	public static String whoKilled = "";
	
	@SubscribeEvent
	public void showDeathCount(DrawScreenEvent.Post event)
	{  
		if(event.getGui() == null) return;

		if(event.getGui() instanceof GuiGameOver) 
		{
			
			if(whoKilled != "")
			{
				String message = TextFormatting.YELLOW + whoKilled +" has killed you "+ KillCounter.getKillCount(whoKilled) + " times!";
				event.getGui().drawString(Minecraft.getMinecraft().fontRendererObj, message, (event.getGui().width) / 2 - ( Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) / 2) , 115, 16777215);
			}			
		}
	}
	
	
    @SubscribeEvent
	public void spawnEvent(EntityJoinWorldEvent event)
	{
    	if(!event.getWorld().isRemote) return;
    	
    	if(event.getEntity() instanceof EntityRangedAttacker)
    	{
    		if(((EntityRangedAttacker)event.getEntity()).getRaiderRole() == EnumRaiderRole.WITCH)
    		{
    			Minecraft.getMinecraft().getSoundHandler().playSound(new CryingWitch((EntityRangedAttacker) event.getEntity()));
    		}
    	}
    	else if(event.getEntity() instanceof EntityDropPod)
    	{    
    		Minecraft.getMinecraft().getSoundHandler().playSound(new DropPodMoving((EntityDropPod) event.getEntity()));
    	}
	}

    boolean hasCheckedSkins = false;
  //
    

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {

    	
    	if(!hasCheckedSkins)
    	{
        	//System.out.println("CacheSkins");
    		hasCheckedSkins = true;
			
    		if(EquipmentManager.ErrorList.size() > 0)
    		{
    			event.player.addChatComponentMessage(new TextComponentTranslation(EquipmentManager.ErrorList.size() + " Errors were found in Raiders Equipment json. Check Console for more info."));
    		}
    		//RaidersSkinManager.INSTANCE.cacheSkins();
    		RaidersSkinManager.INSTANCE.cacheSkins();
    	}
    	  
    	
    	if(event.player.worldObj == null || !event.player.worldObj.isRemote) return;
    	if(event.player.worldObj.getWorldTime() % 1000 != 0 || event.phase == Phase.END) return;
    	
    	int diff = DifficultyProgression.getRaidDifficulty(event.player.worldObj);
    
    	if(PMSettings.lastRaidCheck != diff)
    	{
    		PMSettings.lastRaidCheck = diff;
    		RaidNotification.ScheduleNotice("Raid Difficulty "+ diff, "Raiders have gotten harder!", SoundEvents.RAID_DAY_SUSPENSE.getRegistryName().toString());
    	}

    }
      
		
	boolean witchNear = false;
	int maxDistance = 35;
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void fogRender(RenderFogEvent event)
	{
		
		if(!PMSettings.screamerFogOn) return;
		
		this.witchNear = false;
		float fogDistance = 0;
		
		if(event.getEntity() instanceof EntityPlayer && event.getEntity().worldObj != null)
		{

			EntityPlayer player = (EntityPlayer) event.getEntity();
			AxisAlignedBB box = player.getEntityBoundingBox();
					
			if(box == null) return;
			
			List<EntityRaiderBase> list = player.worldObj.getEntitiesWithinAABB(EntityRaiderBase.class, box.expand(maxDistance, maxDistance, maxDistance));
			
			for(EntityRaiderBase entity : list)
			{
				if(entity.getRaiderRole() == EnumRaiderRole.WITCH)
				{
					if(((EntityRangedAttacker)entity).isWitchActive())
					{
						this.witchNear = true;
						float percentage = (float) Math.pow(entity.getDistanceToEntity(player)/maxDistance, 5);
						fogDistance = (float) percentage * event.getFarPlaneDistance();
					}
				}
			}
		}
		
		if(this.witchNear) 
		{
			int fogStart = 8;
			int fogEnd = 20;
			
			GlStateManager.setFogDensity(0.1f);
			GlStateManager.setFogStart(fogStart);		
			GlStateManager.setFogEnd(Math.max(fogDistance,20));	
			
		}
	}
	
}
