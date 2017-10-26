package com.gendeathrow.pmobs.common.capability;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent.Entity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.pmobs.common.capability.player.PlayerDataProvider;
import com.gendeathrow.pmobs.core.RaidersCore;

public class CapabilityHandler
{
    public static final ResourceLocation PLAYERSDATA = new ResourceLocation(RaidersCore.MODID, "pr_playersdata");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent event)
    {
    	// HardCoded fakeplayer for jei
        if (!(event.getObject() instanceof EntityPlayer) || (event.getObject() instanceof FakePlayer) || (event.getObject().getClass().getName().equals("mezz.jei.util.FakeClientPlayer"))) return;

        EntityPlayer player = (EntityPlayer) event.getObject();
       if(!event.getCapabilities().containsKey(new ResourceLocation(RaidersCore.MODID, "pr_playersdata")))
        	event.addCapability(new ResourceLocation(RaidersCore.MODID, "pr_playersdata"), new PlayerDataProvider());
    }
}