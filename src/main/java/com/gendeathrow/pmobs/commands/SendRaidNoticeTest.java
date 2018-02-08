package com.gendeathrow.pmobs.commands;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.network.RaidNotificationPacket;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class SendRaidNoticeTest extends Base_Command
{

	@Override
	public String getCommand() 
	{
		return "noticeTest";
	}
	
	@Override
	public boolean validArgs(String[] args)
	{
		return args.length >= 0;
	}

	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args)
	{
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("HEADER");
		lines.add("Subtext 1");
		lines.add("Subtext 2");
		lines.add("Subtext 3");
		lines.add("Subtext 4");
		lines.add("Subtext 5");
		lines.add("Subtext 6");
		if(sender.getCommandSenderEntity() instanceof EntityPlayerMP)
			RaidersMain.network.sendTo(new RaidNotificationPacket(lines), (EntityPlayerMP) sender.getCommandSenderEntity());
	}

}
