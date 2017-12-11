package com.gendeathrow.pmobs.common;

import com.gendeathrow.pmobs.common.capability.player.IPlayerData;
import com.gendeathrow.pmobs.common.capability.player.PlayerDataProvider;
import com.gendeathrow.pmobs.common.potion.BruteSerumEffect;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.util.ObfHelper;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class BruteSerumHandler 
{

	public static ResourceLocation BRUTESERUMID = new ResourceLocation(RaidersMain.MODID,"bruteserum");
	
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
	
	// this.setSize(0.6F, 1.8F);
    //TODO this needs to move into capability
	static boolean isNormalSize = true;
	
	private static boolean isSerumActive(EntityPlayer player)
	{
		return player.isPotionActive(Potion.getPotionFromResourceLocation(BRUTESERUMID.toString()));
	}
	
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) 
    {
    	if(event.phase != TickEvent.Phase.END) return;

		if(event.player.hasCapability(PlayerDataProvider.PLAYERDATA, null))
		{
			IPlayerData playerdata = event.player.getCapability(PlayerDataProvider.PLAYERDATA, null);

			if(isSerumActive(event.player))
			{
				event.player.eyeHeight = 2.1F;
				
					float height = (float) (event.player.height * 1.25);
					
					if(event.player.stepHeight < 2f) {
						event.player.stepHeight = 2f;	
					}
		    		
					playerdata.setBruteSize(true);
					event.player.setSprinting(false);
					ObfHelper.forceSetSize((Entity)event.player, event.player.width, height);
			        
    		}else if(!isSerumActive(event.player) && playerdata.isBruteSize())
    		{
    			event.player.eyeHeight = event.player.getDefaultEyeHeight();
    			playerdata.setBruteSize(false);
    			event.player.stepHeight = 0.6f;
    		}
    }
    	
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent    
    public static void onPlayerRendered(RenderPlayerEvent.Pre event)
    {
		GlStateManager.pushMatrix();
    	if(isSerumActive(event.getEntityPlayer()))
    	{
    		GlStateManager.scale(1.25, 1.25, 1.25);
    	}
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent  
    public static void onPlayerRendered(RenderPlayerEvent.Post event)
    {
    	GlStateManager.popMatrix();
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent  
    public static void onPlayerAttack(AttackEntityEvent event)
    {
    	if(event.getTarget() instanceof EntityLivingBase && isSerumActive(event.getEntityPlayer()))
    	{
    		((EntityLivingBase)event.getTarget()).knockBack(event.getTarget() , 1, (double)MathHelper.sin(event.getEntityPlayer() .rotationYaw * 0.017453292F), (double)(-MathHelper.cos(event.getEntityPlayer() .rotationYaw * 0.017453292F)));
    	}
    }
}
