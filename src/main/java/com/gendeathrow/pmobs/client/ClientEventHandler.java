package com.gendeathrow.pmobs.client;

import java.util.List;

import com.gendeathrow.pmobs.client.audio.CryingWitch;
import com.gendeathrow.pmobs.client.audio.DropPodMoving;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;
import com.gendeathrow.pmobs.entity.neutral.EntityDropPod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(Side.CLIENT)
public class ClientEventHandler 
{

	public static String whoKilled = "";
	
//	@SubscribeEvent
//	public void showDeathCount(DrawScreenEvent.Post event)
//	{  
//		if(event.getGui() == null) return;
//
//		if(event.getGui() instanceof GuiGameOver) 
//		{
//			
//			if(whoKilled != "")
//			{
//				String message = TextFormatting.YELLOW + whoKilled +" has killed you "+ KillCounter.getKillCount(whoKilled) + " times!";
//				event.getGui().drawString(Minecraft.getMinecraft().fontRenderer, message, (event.getGui().width) / 2 - ( Minecraft.getMinecraft().fontRendererObj.getStringWidth(message) / 2) , 115, 16777215);
//			}			
//		}
//	}
	
	
    @SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
    	if(!event.getWorld().isRemote) return;
    	
    	if(event.getEntity() instanceof EntityRaiderWitch)
    	{
   			Minecraft.getMinecraft().getSoundHandler().playSound(new CryingWitch((EntityRaiderWitch) event.getEntity()));
    	}
    	else if(event.getEntity() instanceof EntityDropPod)
    	{    
    		Minecraft.getMinecraft().getSoundHandler().playSound(new DropPodMoving((EntityDropPod) event.getEntity()));
    	}
	}

    boolean hasCheckedSkins = false;	
  //
    

//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent event) 
//    {
//
//    	
//    	if(event.player.worldObj == null || !event.player.worldObj.isRemote) return;
//    	if(event.player.worldObj.getWorldTime() % 1000 != 0 || event.phase == Phase.END) return;
//    	
//    	int diff = DifficultyProgression.getRaidDifficulty(event.player.worldObj);
//    
//    	if(PMSettings.lastRaidCheck != diff)
//    	{
//    		PMSettings.lastRaidCheck = diff;
//    		RaidNotification.ScheduleNotice("Raid Difficulty "+ diff, "Raiders have gotten harder!", SoundEvents.RAID_DAY_SUSPENSE.getRegistryName().toString());
//    	}
//
//    }
//      
		
	private static boolean witchNear = false;
	private static int maxDistance = 35;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void fogRender(RenderFogEvent event)
	{
		
		if(!PMSettings.screamerFogOn) return;
		
		witchNear = false;
		float fogDistance = 0;
		
		if(event.getEntity() instanceof EntityPlayer && event.getEntity().world != null)
		{

			EntityPlayer player = (EntityPlayer) event.getEntity();
			AxisAlignedBB box = player.getEntityBoundingBox();
					
			if(box == null) return;
			
			List<EntityRaiderWitch> list = player.world.getEntitiesWithinAABB(EntityRaiderWitch.class, box.grow(maxDistance));
			
			for(EntityRaiderWitch entity : list)
			{

				if(entity.isWitchActive())
				{
					witchNear = true;
					float percentage = (float) Math.pow(entity.getDistanceToEntity(player)/maxDistance, 5);
					fogDistance = (float) percentage * event.getFarPlaneDistance();
					break;
				}
			}
		}
		
		if(witchNear) 
		{
			int fogStart = 8;
			int fogEnd = 20;
			
			GlStateManager.setFogDensity(0.1f);
			GlStateManager.setFogStart(fogStart);		
			GlStateManager.setFogEnd(Math.max(fogDistance,20));	
			
		}
	}
	
}
