package com.gendeathrow.pmobs.world;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.handlers.RaiderManager;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

@Mod.EventBusSubscriber
public class WorldHandler 
{

	public static boolean isLoaded = false;

	static long i = 0;
	@SubscribeEvent
	public static void tickEvent(WorldTickEvent event)//|| event.world.provider.getDimension() != 0
	{
		if(event.phase != Phase.END || !PMSettings.isDifficultyProgressionEnabled || event.world.provider.getDimension() != 0) return;
		i++;
		if (i % 200 == 0) {
			int currentDiff = RaidersWorldDifficulty.INSTANCE.getCurrentRaidDifficulty(event.world);
			int checkDiff = RaidersWorldDifficulty.calculateRaidDifficulty(event.world);

			if(checkDiff != currentDiff) {
				RaidersWorldDifficulty.INSTANCE.UpdateWorldDifficulty(event.world);
				if(PMSettings.debugMode) {
					RaidersMain.logger.warn("--Server Sends World Difficulty Update Server -> Client--");
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public static void worldLoad(WorldEvent.Load event)
	{
		Raiders_WorldData.get(event.getWorld());
 	}
	
	@SubscribeEvent
	public static void WorldSave(WorldEvent.Save event)
	{
		if(event.getWorld().isRemote) return;

		RaiderManager.Save();
		
		Raiders_WorldData.get(event.getWorld()).setDirty(true);
	}
}
