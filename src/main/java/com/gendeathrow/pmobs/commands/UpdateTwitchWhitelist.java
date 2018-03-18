package com.gendeathrow.pmobs.commands;

import com.gendeathrow.pmobs.handlers.RaiderManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
