package com.gendeathrow.pmobs.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import com.gendeathrow.pmobs.commands.Base_Command;
import com.gendeathrow.pmobs.handlers.RaiderManager;

public class UpdateTwitchWhitelist extends Base_Command
{

	@Override
	public String getCommand() 
	{
		return "reloadWhiteList";
	}
  
	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args) 
	{
		RaiderManager.getTwitchSubscribers(true);
		sender.sendMessage(new TextComponentString("Updated Twitch Sub list"));
	}

}
