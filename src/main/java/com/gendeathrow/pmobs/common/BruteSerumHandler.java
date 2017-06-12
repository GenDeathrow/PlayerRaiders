package com.gendeathrow.pmobs.common;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.common.Potion.BruteSerumEffect;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.util.ObfHelper;

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
	
	private boolean isSerumActive(EntityPlayer player)
	{
		return player.isPotionActive(Potion.getPotionFromResourceLocation(BRUTESERUMID.toString()));
	}
	
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {
    	if(event.phase != TickEvent.Phase.END) return;
    	

    	
    	if(isSerumActive(event.player))
    	{
    		event.player.eyeHeight = 2.5F;
    		
    		if(event.player.stepHeight < 2)
    			event.player.stepHeight = 2;

    		double width = (event.player.width * 1.25);
    		double height = (event.player.height * 1.25);
    		ObfHelper.forceSetSize((Entity)event.player, (float)width, (float)height);
        	event.player.setSprinting(false);
           isNormalSize = false;
    	}else if(!isSerumActive(event.player) && event.player.eyeHeight == 2.5F)
    	{
    		event.player.eyeHeight = event.player.getDefaultEyeHeight();
    		if(event.player.stepHeight > 0.6F)
    		{
    			event.player.stepHeight = 0.6F;
    		}
    			
            isNormalSize = true;
    	}
    	
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent    
    public void onPlayerRendered(RenderPlayerEvent.Pre event)
    {
		GlStateManager.pushMatrix();
    	if(isSerumActive(event.getEntityPlayer()))
    	{
    		GlStateManager.scale(1.25, 1.25, 1.25);
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent  
    public void onPlayerRendered(RenderPlayerEvent.Post event)
    {
    	GlStateManager.popMatrix();
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent  
    public void onPlayerRendered(AttackEntityEvent event)
    {
    	
    	if(event.getTarget() instanceof EntityLivingBase && this.isSerumActive(event.getEntityPlayer()))
    	{
    		((EntityLivingBase)event.getTarget()).knockBack(event.getTarget() , 1, (double)MathHelper.sin(event.getEntityPlayer() .rotationYaw * 0.017453292F), (double)(-MathHelper.cos(event.getEntityPlayer() .rotationYaw * 0.017453292F)));
    	}
    }
}
