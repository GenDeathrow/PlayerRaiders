package com.gendeathrow.pmobs.core.network;

import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientUpdatePacket implements IMessage 
{
	public int serverRaidProgression;

	public ClientUpdatePacket(){}
	
	public ClientUpdatePacket(int progression)
	{
		this.serverRaidProgression = progression;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		serverRaidProgression = ByteBufUtils.readVarInt(buf, 2);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, this.serverRaidProgression, 2);
	}
	
	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	
	public static class ClientHandler implements IMessageHandler<ClientUpdatePacket, IMessage> 
	{
		private static long lastRecieved = 0;
		@Override
		public IMessage onMessage(final ClientUpdatePacket message, MessageContext ctx) 
		{
			
			if(message == null)
			{
				RaidersCore.logger.error("A critical NPE error occured during while handling a ClientUpdatePacket Client side", new NullPointerException());
				return null;
			}
			
		    IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
            mainThread.addScheduledTask(new Runnable() 
            {
                @Override
                public void run() 
                {
               		PMSettings.dayDifficultyProgression = message.serverRaidProgression;
               		PMSettings.lastRaidCheck = 0; // Reset last checked
               		//System.out.println(PMSettings.dayDifficultyProgression +"---"+ ConfigHandler.getRaidDifficultyProgressions());
                }
            });
            
			return null;
		}
	}
		
	
	/////////////////////////////////////////////
	// Server Message
	////////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<ClientUpdatePacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final ClientUpdatePacket message, final MessageContext ctx) 
		{
			return message;
			
		}
	}
}