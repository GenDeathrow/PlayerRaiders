package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.common.Potion.BruteSerumEffect;
import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BruteSerumHandler 
{

	public static ResourceLocation BRUTESERUMID = new ResourceLocation(RaidersCore.MODID,"bruteserum");
	
	public static void registerPotion()
	{
		Potion.REGISTRY.register(getSparePotionId(), BRUTESERUMID, new BruteSerumEffect());
	}
	
    public static int getSparePotionId()
    {
        int nextId = 1;
        
        // look for a free slot in the Potions array
        // (note we start counting from 1 - vanilla MC doens't use ID 0, nor will we)
        for (; Potion.REGISTRY.getObjectById(nextId) != null; nextId++) {}

        return nextId;
    }
	
	@SubscribeEvent
	public void spawnEvent(EntityJoinWorldEvent event)
	{
    	if(!event.getWorld().isRemote) return;
    	
    	
    	
	}
	
	// this.setSize(0.6F, 1.8F);
	boolean isNormalSize = true;
	
	private boolean isActive(EntityPlayer player)
	{
		return player.isPotionActive(Potion.getPotionFromResourceLocation(BRUTESERUMID.toString()));
	}
	
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {
    	if(isActive(event.player) && event.player.eyeHeight < 2.5F)
    	{
    		System.out.println("GROW");
    		event.player.eyeHeight = 2.5F;
    		
    	
    		double width = (event.player.width * 1.25);
    		double height = (event.player.height * 1.25);
    		
           //event.player.setEntityBoundingBox(new AxisAlignedBB(0, 0, 0, width, height, width));
           
           isNormalSize = false;
    	}else if(!isActive(event.player) && event.player.eyeHeight == 2.5F)
    	{
    		System.out.println("NormalSize");
    		event.player.eyeHeight = event.player.getDefaultEyeHeight();
    		
            //event.player.setEntityBoundingBox(new AxisAlignedBB(0, 0, 0, event.player.width, event.player.height, event.player.width));
            isNormalSize = true;
    	}
    	
    }
    
    @SubscribeEvent    
    public void onPlayerRendered(RenderPlayerEvent.Pre event)
    {
		GlStateManager.pushMatrix();
    	if(isActive(event.getEntityPlayer()))
    	{
    		GlStateManager.scale(1.25, 1.25, 1.25);
    	}
    }
    
    @SubscribeEvent  
    public void onPlayerRendered(RenderPlayerEvent.Post event)
    {
    	GlStateManager.popMatrix();
    }
    
    
}
