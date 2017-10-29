package com.gendeathrow.pmobs.core;

import org.apache.commons.lang3.ArrayUtils;

import com.gendeathrow.pmobs.core.proxies.CommonProxy;
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

		
	  	EntityRegistry.registerModEntity(new ResourceLocation(MODID, "raider"), EntityRaider.class, "Raider", 1, this, 80, 3, true, -3971048, -6191748);
	  	 
	  	
       	RaidersMain.network = NetworkRegistry.INSTANCE.newSimpleChannel(RaidersMain.CHANNELNAME);
    }
    
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);

    	proxy.registerHandlers();
    	
    	
		
    	Biome[] biomes = new Biome[0];
    	
     	  for (Biome biomeEntry : ForgeRegistries.BIOMES.getValues()) 
    	  {
    		  boolean flag = true;
    		  
    		  if((BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.NETHER) && !PMSettings.spawnNether) || 
    			 (BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.END) && !PMSettings.spawnEnd)  ||
    			 (BiomeDictionary.hasType(biomeEntry, BiomeDictionary.Type.WATER)))			
    		  {
    			  flag = false;
    		  }
    		  
    		  if (flag)
    		  {
    			  biomes = ArrayUtils.add(biomes,biomeEntry);
    		  }
    	  }
       	
    	RaidersMain.logger.info("Added "+ biomes.length +" biomes to Raiders spawn list.");
    	EntityRegistry.addSpawn(EntityRaider.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn,EnumCreatureType.MONSTER, biomes);

    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	
       	DungeonHooks.addDungeonMob(new ResourceLocation(MODID ,"raiders"), PMSettings.raidersSpawnerWeight); 
        
    }
}
