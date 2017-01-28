package com.gendeathrow.pmobs.core;

import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.util.EnumHelper;
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

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.commands.common.CommonCommands;
import com.gendeathrow.pmobs.common.SoundEvents;
import com.gendeathrow.pmobs.core.proxies.CommonProxy;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;

@Mod(modid = RaidersCore.MODID, name=RaidersCore.NAME, version = RaidersCore.VERSION)
public class RaidersCore
{
    public static final String MODID = "playerraiders";
    public static final String NAME = "Player Raiders";
    public static final String VERSION = "1.2.14.29";
    
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
		
    	EntityRegistry.registerModEntity(EntityPlayerRaider.class, "Raiders", 1, this, 80, 3, true, -3971048, -6191748);
     	
     	ConfigHandler.preInit();
    	
    	proxy.preInit(event);
     }
    
    public static ResourceLocation playerraidersloot = new ResourceLocation(MODID, "entities/playerraiders");
    
    public static final EnumCreatureType RaidersDayTime = EnumHelper.addCreatureType("raiderDayTime", EntityRaiderBase.class, 30, Material.GROUND, false, false);
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	proxy.Init(event);
    	
    	proxy.registerHandlers();
    	
    	ConfigHandler.load();
    	
		SoundEvents.register();
		
		LootTableList.register(playerraidersloot);
		
    	Biome[] biomes = new Biome[0];
    	
    	for(Biome biomeEntry : Biome.REGISTRY)
    	{
    		biomes = ArrayUtils.add(biomes,biomeEntry);
    	}
       	
    	RaidersCore.logger.info("Added "+ biomes.length +" biomes to Raiders spawn list.");
    
    	EntityRegistry.addSpawn(EntityPlayerRaider.class, PMSettings.NightSpawnWeight, 1, PMSettings.nightMaxGroupSpawn,EnumCreatureType.MONSTER, biomes);
    	
    	

    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent event)
    {
    	proxy.postInit(event);
    	
    	ConfigHandler.PostLoad();

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
        if (RaidersCore.proxy.isIntergratedServerRunning())
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
