package com.gendeathrow.pmobs.core;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import org.apache.commons.lang3.ArrayUtils;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.commands.common.CommonCommands;
import com.gendeathrow.pmobs.common.SoundEvents;
import com.gendeathrow.pmobs.common.capability.player.IPlayerData;
import com.gendeathrow.pmobs.common.capability.player.PlayersData;
import com.gendeathrow.pmobs.common.capability.player.PlayersDataStorage;
import com.gendeathrow.pmobs.core.init.ModRecipes;
import com.gendeathrow.pmobs.core.network.ClientUpdatePacket;
import com.gendeathrow.pmobs.core.network.RaiderDeathCntPacket;
import com.gendeathrow.pmobs.core.proxies.CommonProxy;
import com.gendeathrow.pmobs.entity.EntityDropPod;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.EntitySignalTransmitter;

@Mod(modid = RaidersCore.MODID, name=RaidersCore.NAME, version = RaidersCore.VERSION, dependencies="after:BiomesOPlenty", acceptedMinecraftVersions="[1.10.2]", guiFactory = "com.gendeathrow.pmobs.client.ConfigGuiFactory")
public class RaidersCore
{
    public static final String MODID = "playerraiders";
    public static final String NAME = "Player Raiders";
    public static final String VERSION = "1.3.11";
    public static final String CHANNELNAME = "genraiders";
    
	@Instance(MODID)
	public static RaidersCore instance;

	public static final String PROXY = "com.gendeathrow.pmobs.core.proxies";
	    
	public static org.apache.logging.log4j.Logger logger;
	    
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper network;

    public static ResourceLocation playerraidersloot = new ResourceLocation(MODID, "entities/playerraiders");
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
		logger = event.getModLog();
		
		CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayersDataStorage(), PlayersData.class);
		
    	EntityRegistry.registerModEntity(EntityPlayerRaider.class, "Raiders", 1, this, 80, 3, true, -3971048, -6191748);
    	EntityRegistry.registerModEntity(EntityDropPod.class, "DropPod", 2, this, 80, 1, true);
    	EntityRegistry.registerModEntity(EntitySignalTransmitter.class, "Transmitter", 3, this, 80, 1, false);
    	
    	//EntityRegistry.registerModEntity(HiredRaider.class, "HiredRaiders", 3, this, 80, 3, true, -1971048, -3191748);
    	
     	ConfigHandler.preInit();
     	
       	RaidersCore.network = NetworkRegistry.INSTANCE.newSimpleChannel(RaidersCore.CHANNELNAME);
       	
       	network.registerMessage(RaiderDeathCntPacket.ClientHandler.class, RaiderDeathCntPacket.class, 0, Side.CLIENT);
       	network.registerMessage(ClientUpdatePacket.ClientHandler.class, ClientUpdatePacket.class, 1, Side.CLIENT);
       	
    	proxy.preInit(event);
     }
    
   
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);

    	proxy.registerHandlers();
   	
    	ConfigHandler.load();
    	
		SoundEvents.register();
		
	
		LootTableList.register(playerraidersloot);
		
    	Biome[] biomes = new Biome[0];
    	
    	  for (Biome biomeEntry : ForgeRegistries.BIOMES.getValues()) 
    	  {
    		  boolean flag = true;
    		  
    		  if((BiomeDictionary.isBiomeOfType(biomeEntry, BiomeDictionary.Type.NETHER) && !PMSettings.spawnNether) || 
    				  (BiomeDictionary.isBiomeOfType(biomeEntry, BiomeDictionary.Type.END) && !PMSettings.spawnEnd)){
    			  flag = false;
    		  }
    		  
    		  if (flag)
    		  {
    			  biomes = ArrayUtils.add(biomes,biomeEntry);
    		  }
    	  }
       	
    	RaidersCore.logger.info("Added "+ biomes.length +" biomes to Raiders spawn list.");
    	EntityRegistry.addSpawn(EntityPlayerRaider.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn,EnumCreatureType.MONSTER, biomes);

    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	
    	ConfigHandler.PostLoad();

    	ModRecipes.RegisterRecipes();
    	
    	DungeonHooks.addDungeonMob(MODID +".Raiders", PMSettings.raidersSpawnerWeight); 
        	
    	if(PMSettings.removeVanillaSpawners)
    	{
    		logger.info("Removeing Vanilla Spawners from Dungeon Hooks");
    		if(DungeonHooks.removeDungeonMob("Spider") == 0) logger.error("Tried to remove Spider from Mob Spawner. It didn't exist.");
    		if(DungeonHooks.removeDungeonMob("Skeleton") == 0) logger.error("Tried to remove Skeleton from Mob Spawner. It didn't exist.");
    		if(DungeonHooks.removeDungeonMob("Creeper") == 0) logger.error("Tried to remove Creeper from Mob Spawner. It didn't exist.");
    		if(DungeonHooks.removeDungeonMob("Zombie") == 0) logger.error("Tried to remove Zombie from Mob Spawner. It didn't exist.");
    		if(DungeonHooks.removeDungeonMob("Enderman") == 0) logger.error("Treid to remove Enderman from Mob Spawner. It didn't exist.");
    	}
    }
    
    
	@EventHandler
	public void serverStart(FMLServerStartingEvent event)
	{
        if (RaidersCore.proxy.isIntergratedServerRunning() || RaidersCore.proxy.isClient())
        {	
//        	EntityPlayerRaider.setProfileCache(event.getServer().getPlayerProfileCache());
//        	EntityPlayerRaider.setSessionService(event.getServer().getMinecraftSessionService());
        	
        	RaidersSkinManager.profileCache = event.getServer().getPlayerProfileCache();
        	RaidersSkinManager.sessionService = event.getServer().getMinecraftSessionService();
        }
        
		ICommandManager command = event.getServer().getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		
		manager.registerCommand(new CommonCommands());

	}
}
