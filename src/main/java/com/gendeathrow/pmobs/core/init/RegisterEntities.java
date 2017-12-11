package com.gendeathrow.pmobs.core.init;

import org.apache.commons.lang3.ArrayUtils;

import com.gendeathrow.pmobs.client.renderer.DropPodRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityBruteRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityPyroRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityRaiderRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityRangerRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityTwitcherRenderer;
import com.gendeathrow.pmobs.client.renderer.EntityWitchRenderer;
import com.gendeathrow.pmobs.client.renderer.SignalTransmitterRenderer;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.entity.mob.EntityBrute;
import com.gendeathrow.pmobs.entity.mob.EntityPyromaniac;
import com.gendeathrow.pmobs.entity.mob.EntityRaider;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;
import com.gendeathrow.pmobs.entity.mob.EntityRanger;
import com.gendeathrow.pmobs.entity.mob.EntityTwitcher;
import com.gendeathrow.pmobs.entity.neutral.EntityDropPod;
import com.gendeathrow.pmobs.entity.neutral.EntitySignalTransmitter;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterEntities {
	
	static int nextID = 1;
	public static void Register() {
		
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "raider"), EntityRaider.class, "raider", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 15677239);
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "brute"),  EntityBrute.class, "brute", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 1310720);
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "witch"),  EntityRaiderWitch.class, "witch", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 971165);
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "twitcher"),  EntityTwitcher.class, "twitcher", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 971165);
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "pyromaniac"),  EntityPyromaniac.class, "pyromaniac", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 971165);
	  	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "ranger"),  EntityRanger.class, "ranger", nextID++, RaidersMain.instance, 80, 3, true, -3971048, 971165);
    	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "droppod"), EntityDropPod.class, "DropPod",  nextID++, RaidersMain.instance, 80, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation(RaidersMain.MODID, "signaltransmitter"), EntitySignalTransmitter.class, "Transmitter",  nextID++, RaidersMain.instance, 80, 1, false);
	}
	
	@SideOnly(Side.CLIENT)
	public static void RegisterRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityRaider.class, EntityRaiderRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBrute.class, EntityBruteRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityTwitcher.class, EntityTwitcherRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRaiderWitch.class, EntityWitchRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPyromaniac.class, EntityPyroRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityRanger.class, EntityRangerRenderer::new);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDropPod.class, DropPodRenderer::new);  
		RenderingRegistry.registerEntityRenderingHandler(EntitySignalTransmitter.class, SignalTransmitterRenderer::new);
	}
	
	public static void RegisterSpawners() {
	  	
      	DungeonHooks.addDungeonMob(new ResourceLocation(RaidersMain.MODID ,"raiders"), PMSettings.raidersSpawnerWeight); 
      	DungeonHooks.addDungeonMob(new ResourceLocation(RaidersMain.MODID ,"brute"), PMSettings.raidersSpawnerWeight);
      	DungeonHooks.addDungeonMob(new ResourceLocation(RaidersMain.MODID ,"twitcher"), PMSettings.raidersSpawnerWeight);
      	DungeonHooks.addDungeonMob(new ResourceLocation(RaidersMain.MODID ,"pyromaniac"), PMSettings.raidersSpawnerWeight);
      	DungeonHooks.addDungeonMob(new ResourceLocation(RaidersMain.MODID ,"ranger"), PMSettings.raidersSpawnerWeight);

	}
	
	public static void RegisterSpawns() {
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
  	
	   	EntityRegistry.addSpawn(EntityRaider.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn, EnumCreatureType.MONSTER, biomes);
	   	EntityRegistry.addSpawn(EntityBrute.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn, EnumCreatureType.MONSTER, biomes);
	   	EntityRegistry.addSpawn(EntityRaiderWitch.class, 3, 1, 1, EnumCreatureType.MONSTER, biomes);
	   	EntityRegistry.addSpawn(EntityTwitcher.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn, EnumCreatureType.MONSTER, biomes);
	   	EntityRegistry.addSpawn(EntityPyromaniac.class, PMSettings.raidersSpawnWeight, 1, 1, EnumCreatureType.MONSTER, biomes);
	   	EntityRegistry.addSpawn(EntityRanger.class, PMSettings.raidersSpawnWeight, 1, PMSettings.raidersMaxGroupSpawn, EnumCreatureType.MONSTER, biomes);

	}

}
