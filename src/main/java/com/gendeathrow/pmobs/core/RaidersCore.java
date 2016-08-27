package com.gendeathrow.pmobs.core;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import org.apache.commons.lang3.ArrayUtils;

import com.gendeathrow.pmobs.commands.common.CommonCommands;
import com.gendeathrow.pmobs.core.proxies.CommonProxy;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;

@Mod(modid = RaidersCore.MODID, name=RaidersCore.NAME, version = RaidersCore.VERSION)
public class RaidersCore
{
    public static final String MODID = "playerraiders";
    public static final String NAME = "Player Raiders";
    public static final String VERSION = "1.2.12";
    
	@Instance(MODID)
	public static RaidersCore instance;

	public static final String PROXY = "com.gendeathrow.pmobs.core.proxies";
	    
	public static org.apache.logging.log4j.Logger logger;
	    
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;


    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
		logger = event.getModLog();
		
    	EntityRegistry.registerModEntity(EntityPlayerRaider.class, "Raiders", 1, this, 80, 3, true);
    	
     	EntityRegistry.registerEgg(EntityPlayerRaider.class, -3971048, -6191748);
     	
     	ConfigHandler.preInit();
    	
    	proxy.preInit(event);
     }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);
    	
    	ConfigHandler.load();
    	
    	
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	
    	
    	ConfigHandler.PostLoad();

    	
    	Biome[] biomes = new Biome[0];

    	biomes = ArrayUtils.add(biomes, Biomes.BEACH);
    	biomes = ArrayUtils.add(biomes, Biomes.PLAINS);
    	biomes = ArrayUtils.add(biomes, Biomes.DESERT);
    	biomes = ArrayUtils.add(biomes, Biomes.EXTREME_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.FOREST);
    	biomes = ArrayUtils.add(biomes, Biomes.TAIGA);
    	biomes = ArrayUtils.add(biomes, Biomes.SWAMPLAND);
    	biomes = ArrayUtils.add(biomes, Biomes.HELL);
    	biomes = ArrayUtils.add(biomes, Biomes.FROZEN_OCEAN);
    	biomes = ArrayUtils.add(biomes, Biomes.ICE_PLAINS);
    	biomes = ArrayUtils.add(biomes, Biomes.ICE_MOUNTAINS);
    	biomes = ArrayUtils.add(biomes, Biomes.MUSHROOM_ISLAND);
    	biomes = ArrayUtils.add(biomes, Biomes.MUSHROOM_ISLAND_SHORE);
    	biomes = ArrayUtils.add(biomes, Biomes.BEACH);
    	biomes = ArrayUtils.add(biomes, Biomes.DESERT_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.FOREST_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.TAIGA_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.EXTREME_HILLS_EDGE);
    	biomes = ArrayUtils.add(biomes, Biomes.JUNGLE);
    	biomes = ArrayUtils.add(biomes, Biomes.JUNGLE_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.JUNGLE_EDGE);
    	biomes = ArrayUtils.add(biomes, Biomes.STONE_BEACH);
    	biomes = ArrayUtils.add(biomes, Biomes.COLD_BEACH);
    	biomes = ArrayUtils.add(biomes, Biomes.BIRCH_FOREST);
    	biomes = ArrayUtils.add(biomes, Biomes.BIRCH_FOREST_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.ROOFED_FOREST);
    	biomes = ArrayUtils.add(biomes, Biomes.COLD_TAIGA);
    	biomes = ArrayUtils.add(biomes, Biomes.COLD_TAIGA_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.REDWOOD_TAIGA);
    	biomes = ArrayUtils.add(biomes, Biomes.REDWOOD_TAIGA_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.EXTREME_HILLS_WITH_TREES);
    	biomes = ArrayUtils.add(biomes, Biomes.SAVANNA);
    	biomes = ArrayUtils.add(biomes, Biomes.SAVANNA_PLATEAU);
    	biomes = ArrayUtils.add(biomes, Biomes.MESA);
    	biomes = ArrayUtils.add(biomes, Biomes.MESA_ROCK);
    	biomes = ArrayUtils.add(biomes, Biomes.MESA_CLEAR_ROCK);
    	biomes = ArrayUtils.add(biomes, Biomes.VOID);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_PLAINS);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_DESERT);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_EXTREME_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_FOREST);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_TAIGA);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_SWAMPLAND);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_ICE_FLATS);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_JUNGLE);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_JUNGLE_EDGE);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_BIRCH_FOREST); 
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_BIRCH_FOREST_HILLS); 
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_ROOFED_FOREST);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_TAIGA_COLD);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_REDWOOD_TAIGA);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_REDWOOD_TAIGA_HILLS);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_EXTREME_HILLS_WITH_TREES);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_SAVANNA);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_SAVANNA_ROCK);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_MESA);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_MESA_ROCK);
    	biomes = ArrayUtils.add(biomes, Biomes.MUTATED_MESA_CLEAR_ROCK); 
    	
//    	System.out.println("biome cnt:"+ biomes.length);
    	
    	EntityRegistry.addSpawn(EntityPlayerRaider.class, PMSettings.daySpawnWeight, 1, PMSettings.dayMaxGroupSpawn, EnumCreatureType.CREATURE, biomes);
    	
    	EntityRegistry.addSpawn(EntityPlayerRaider.class, PMSettings.NightSpawnWeight, 1, PMSettings.nightMaxGroupSpawn, EnumCreatureType.MONSTER, biomes);
    	

    }
    
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
        if (RaidersCore.proxy.isIntergratedServerRunning())
        {	
        	EntityPlayerRaider.setProfileCache(event.getServer().getPlayerProfileCache());
        	EntityPlayerRaider.setSessionService(event.getServer().getMinecraftSessionService());
        }
        
		ICommandManager command = event.getServer().getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		
		manager.registerCommand(new CommonCommands());

	}
}
