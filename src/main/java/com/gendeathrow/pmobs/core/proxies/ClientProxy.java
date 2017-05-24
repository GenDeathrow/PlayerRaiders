package com.gendeathrow.pmobs.core.proxies;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.pmobs.client.ClientEventHandler;
import com.gendeathrow.pmobs.client.IItemColorHandler;
import com.gendeathrow.pmobs.client.gui.RaidNotification;
import com.gendeathrow.pmobs.client.renderer.DropPodRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityRaiderRenderer;
import com.gendeathrow.pmobs.client.renderer.HiredRaiderRenderer;
import com.gendeathrow.pmobs.client.renderer.SignalTransmitterRenderer;
import com.gendeathrow.pmobs.core.init.ModItems;
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
	
		FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler(new IItemColorHandler(), ModItems.spawnEgg);

		registerItemModel(ModItems.backupTransmitter);  
		registerItemModel(ModItems.spawnEgg);
		registerItemModel(ModItems.bruteSerum);  
		registerItemModel(ModItems.bruteSerumSample);  
		registerItemModel(ModItems.satTransmitterPart);  
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
		//System.out.println("client");
		MinecraftForge.EVENT_BUS.register(new RaidNotification());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}
	
	@Override
	public void registerModels()
	{
		
	}
	
		
	@Override
	public void registerRenderers()
	{
		
		RenderingRegistry.registerEntityRenderingHandler(EntityRaiderBase.class, EntityRaiderRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(HiredRaider.class, HiredRaiderRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDropPod.class, DropPodRenderer.FACTORY);
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySignalTransmitter.class, SignalTransmitterRenderer.FACTORY);

 	}
	
	
	
	private static void registerBlock(Block b)
	{
		//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));
	}
	
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item)
	{
		registerItemModel(item, 0, item.getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, int meta, String name)
	{
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		
		if(!name.equals(item.getRegistryName()))
		{
		    ModelBakery.registerItemVariants(item, model);
		}
		
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model);
	}
	
}
