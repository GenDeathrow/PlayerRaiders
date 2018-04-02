package com.gendeathrow.pmobs.network;

import java.util.ArrayList;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.client.gui.RaidNotification;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.core.init.RaidersSoundEvents;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RaidNotificationPacket implements IMessage 
{
	private int count;
	private ArrayList<String> lines = new ArrayList<String>();
	
	public RaidNotificationPacket(){	}

	public RaidNotificationPacket(ArrayList<String> linesIn) // Use PacketDataTypes to instantiate new packets
	{
		lines = linesIn;
		count = linesIn.size();
	}
	
		
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		lines.clear();
		
		count = ByteBufUtils.readVarShort(buf);
		
		for(int i = 0; i < count; i++)
			lines.add(ByteBufUtils.readUTF8String(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		// write amount of lines.
		ByteBufUtils.writeVarShort(buf, lines.size());
		
		for(String line : lines)
			ByteBufUtils.writeUTF8String(buf, line);
			
	}
		
	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<RaidNotificationPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final RaidNotificationPacket message, MessageContext ctx) 
		{
			if(message == null || message.lines.isEmpty())
			{
				RaidersMain.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Raiders notification packet Client side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					if(PMSettings.debugMode) {
						RaidersMain.logger.warn("!!Client recieve notification packet!!");
					}
					
					
					if(PMSettings.renderRaidNotifications)
						RaidNotification.ScheduleNotice(message.lines,  RaidersSoundEvents.RAID_DAY_SUSPENSE.getRegistryName().toString());
					else
						for(String line : message.lines)
							Minecraft.getMinecraft().player.sendMessage(new TextComponentString(line));	
	
				}
			});
			return null;
		}
			
	}
	
	
	///////////////////////////////////////////
	// Server Handler
	///////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<RaidNotificationPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final RaidNotificationPacket message, MessageContext ctx) 
		{
			if(message == null || message.lines.isEmpty())
			{
				RaidersMain.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Raiders notification packet Server side", new NullPointerException());
				return null;
			}
				
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.world;
			mainThread.addScheduledTask(new Runnable()	{
				@Override
				public void run() {
					RaidNotification.ScheduleNotice(message.lines,  RaidersSoundEvents.RAID_DAY_SUSPENSE.getRegistryName().toString());
				}
			});
			return null;
		}
			
	}


}