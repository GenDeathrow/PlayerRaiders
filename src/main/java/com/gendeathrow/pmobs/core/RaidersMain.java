package com.gendeathrow.pmobs.core;

import java.util.function.BiPredicate;

import com.gendeathrow.pmobs.commands.CommonCommands;
import com.gendeathrow.pmobs.core.configs.ConfigHandler;
import com.gendeathrow.pmobs.core.init.RegisterEntities;
import com.gendeathrow.pmobs.core.init.RegisterItems;
import com.gendeathrow.pmobs.core.proxies.CommonProxy;
import com.gendeathrow.pmobs.network.RaidNotificationPacket;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = RaidersMain.MODID, name=RaidersMain.NAME, version = RaidersMain.VERSION, dependencies="after:biomesoplenty", acceptedMinecraftVersions="[1.12.2]")
public class RaidersMain
{
    public static final String MODID = "raiders";
    public static final String NAME = "Raiders";
    public static final String VERSION = "@VERSION@";
    public static final String CHANNELNAME = "genraiders";

	@Instance(MODID)
	public static RaidersMain instance;

	public static final String PROXY = "com.gendeathrow.pmobs.core.proxies";
	    
	public static org.apache.logging.log4j.Logger logger;
	    
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".ServerProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
	
	public static CreativeTabs RaidersTab= new CreativeTabs("Raiders")
	{
		@Override
		public ItemStack createIcon() 
		{
			return new ItemStack(RegisterItems.backupTransmitter);
		}
	
	};
	
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
		logger = event.getModLog();
		
		ConfigHandler.preInit();
		
    	proxy.preInit(event);
    	
    	RegisterEntities.Register();
    	
    	proxy.registerHandlers();
		
       	RaidersMain.network = NetworkRegistry.INSTANCE.newSimpleChannel(RaidersMain.CHANNELNAME);
       	RaidersMain.network.registerMessage(RaidNotificationPacket.ClientHandler.class, RaidNotificationPacket.class, 0, Side.CLIENT);
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);
    	ConfigHandler.init();
    	RegisterEntities.RegisterSpawns();
    	RegisterEntities.RegisterSpawners();
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	ConfigHandler.postInit();
    }
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommonCommands());
	}
}
