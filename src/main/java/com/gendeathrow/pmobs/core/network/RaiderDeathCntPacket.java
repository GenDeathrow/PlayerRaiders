package com.gendeathrow.pmobs.core.network;

import com.gendeathrow.pmobs.client.data.KillCounter;
import com.gendeathrow.pmobs.core.RaidersCore;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RaiderDeathCntPacket implements IMessage 
{
	public String entityName = "";
	public String sourceDMG = "";
	
	public RaiderDeathCntPacket(){}
	
	public RaiderDeathCntPacket(String entityName, String sourceDmg)
	{
		this.entityName = entityName;
		this.sourceDMG = sourceDmg;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		entityName = ByteBufUtils.readUTF8String(buf);
		sourceDMG = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeUTF8String(buf, entityName);
		ByteBufUtils.writeUTF8String(buf, sourceDMG);
	}
	
	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	
	public static class ClientHandler implements IMessageHandler<RaiderDeathCntPacket, IMessage> 
	{

		private static long lastRecieved = 0;
		@Override
		public IMessage onMessage(final RaiderDeathCntPacket message, MessageContext ctx) 
		{
			
			if(message == null)
			{
				RaidersCore.logger.error("A critical NPE error occured during while handling a RaiderDeathCntPacket Client side", new NullPointerException());
				return null;
			}
			
		    IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
            mainThread.addScheduledTask(new Runnable() 
            {
                @Override
                public void run() 
                {
                	
                	
                	if((Minecraft.getSystemTime() - lastRecieved) > 100)
                	{
                		KillCounter.addKillCount(message.sourceDMG);
                    	
                    	lastRecieved = Minecraft.getSystemTime();
                	}
                	


                }
            });
            
			return null;
		}
	}
		
	
	/////////////////////////////////////////////
	// Server Message
	////////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<RaiderDeathCntPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final RaiderDeathCntPacket message, final MessageContext ctx) 
		{
			return message;
			
		}
	}

}
