package com.gendeathrow.pmobs.core.proxies;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gendeathrow.pmobs.client.ClientEventHandler;
import com.gendeathrow.pmobs.client.gui.RaidNotification;
import com.gendeathrow.pmobs.client.renderer.DropPodRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityRaiderRenderer;
import com.gendeathrow.pmobs.client.renderer.HiredRaiderRenderer;
import com.gendeathrow.pmobs.client.renderer.SignalTransmitterRenderer;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.entity.EntitySignalTransmitter;
import com.gendeathrow.pmobs.entity.HiredRaiders.HiredRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

public class ClientProxy extends CommonProxy
{
	

	@Override
	public boolean isClient()
	{
		return true; 
	}

	@Override
	public boolean isIntergratedServerRunning()
	{
		return Minecraft.getMinecraft().isIntegratedServerRunning();
	}
	 
	@Override
	public void preInit(FMLPreInitializationEvent event) 
	{
		registerRenderers();
		super.preInit(event);
	}

	@Override
	public void Init(FMLInitializationEvent event) 
	{
		super.Init(event);
		registerHandlers();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) 
	{
		super.postInit(event);
	
	}
	
	@Override
	public void registerHandlers()
	{
		super.registerHandlers();
		MinecraftForge.EVENT_BUS.register(new RaidNotification());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
	
	@Override
	public void registerModels()
	{
		//registerBlock(ModBlocks.gdDragonEgg);  
	}
	
		
	@Override
	public void registerRenderers()
	{
		
		RenderingRegistry.registerEntityRenderingHandler(EntityRaiderBase.class, EntityRaiderRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(HiredRaider.class, HiredRaiderRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDropPod.class, DropPodRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySignalTransmitter.class, SignalTransmitterRenderer.FACTORY);

//		
//			RenderingRegistry.registerEntityRenderingHandler(EntityRider.class, RenderEntityRider.FACTORY);
//			RenderingRegistry.registerEntityRenderingHandler(Darkosto.class, RenderEntityRider.FACTORY);
//			RenderingRegistry.registerEntityRenderingHandler(GDDragonOLD.class, RenderOldDragon.FACTORY);
//			RenderingRegistry.registerEntityRenderingHandler(EntityDragonWings.class, RenderWings.FACTORY);
//		
    		//RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    		//ModelResourceLocation model = new ModelResourceLocation(GDAddons.MODID + ":" + ((GDDragonEgg) GDAddons.gdDragonEgg).getName(), "inventory");
    		//blocks
    		//renderItem.getItemModelMesher().register(Item.getItemFromBlock(GDAddons.gdDragonEgg), 0, model);
 	}
	
	
	
	private static void registerBlock(Block b)
	{
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
	}
	
	
}
