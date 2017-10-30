package com.gendeathrow.pmobs.core;

import org.apache.commons.lang3.ArrayUtils;

import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.core.init.RegisterEntities;
import com.gendeathrow.pmobs.core.proxies.CommonProxy;
import com.gendeathrow.pmobs.entity.EntityBrute;
import com.gendeathrow.pmobs.entity.EntityRaider;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = RaidersMain.MODID, name=RaidersMain.NAME, version = RaidersMain.VERSION, dependencies="after:BiomesOPlenty", acceptedMinecraftVersions="[1.11.2]")
public class RaidersMain
{
    public static final String MODID = "raiders";
    public static final String NAME = "Raiders";
    public static final String VERSION = "2.0.0";
    public static final String CHANNELNAME = "genraiders";

	@Instance(MODID)
	public static RaidersMain instance;

	public static final String PROXY = "com.gendeathrow.pmobs.core.proxies";
	    
	public static org.apache.logging.log4j.Logger logger;
	    
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper network;
	
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
		logger = event.getModLog();
		
    	proxy.preInit(event);
    	
    	RegisterEntities.RegisterEntities();
    	
    	proxy.registerHandlers();
		
       	RaidersMain.network = NetworkRegistry.INSTANCE.newSimpleChannel(RaidersMain.CHANNELNAME);
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);

    	RegisterEntities.RegisterSpawns();
    	RegisterEntities.RegisterSpawners();
    	
		RaidersSoundEvents.register();
		
 
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
          
    }
}
