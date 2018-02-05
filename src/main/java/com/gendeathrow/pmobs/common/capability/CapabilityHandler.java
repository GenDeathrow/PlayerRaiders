package com.gendeathrow.pmobs.common.capability;

import com.gendeathrow.pmobs.common.capability.player.PlayerDataProvider;
import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CapabilityHandler
{
    public static final ResourceLocation PLAYERSDATA = new ResourceLocation(RaidersMain.MODID, "pr_playersdata");

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
    	// HardCoded fakeplayer for jei
        if (!(event.getObject() instanceof EntityPlayer) || (event.getObject() instanceof FakePlayer) || (event.getObject().getClass().getName().equals("mezz.jei.util.FakeClientPlayer"))) return;

       if(!event.getCapabilities().containsKey(PLAYERSDATA)) {
        	event.addCapability(PLAYERSDATA, new PlayerDataProvider());
       }
    }
}