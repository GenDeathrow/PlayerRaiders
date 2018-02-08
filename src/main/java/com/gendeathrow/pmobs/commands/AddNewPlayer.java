package com.gendeathrow.pmobs.commands;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.handlers.RaiderManager;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class AddNewPlayer extends Base_Command
{

	@Override
	public String getCommand() 
	{
		return "raider";
	}
	
	@Override
	public String getUsageSuffix()
	{
		return "<add, remove> <playerName> <weight>";
	}
	
	@Override
	public boolean validArgs(String[] args)
	{
		return args.length >= 3;
	}

	@Override
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		if(args.length == 2)
		{
			return new ArrayList<String>(){{add("add");add("remove");}};
		}
		else if(args.length == 3)
		{
			return new ArrayList<String>(){{add("playerName");}};
		}
		else if(args.length == 4)
		{
			return new ArrayList<String>(){{add("10");}};
		}  

		return new ArrayList<String>();
	}
	
	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args)
	{
		
		if(args.length < 3)
		{
			return;
		}

		String ownerName = args[2];
		
		if(args[1].equalsIgnoreCase("add"))
		{
			int weight = 10;
			if(args.length > 4)
			{
				
				try
				{
					weight = Integer.parseInt(args[3]);
				}catch(NumberFormatException e)
				{
					RaidersMain.logger.error(e);
				}
			}
			
			if(RaiderManager.raidersList.containsKey(ownerName))
			{
				sender.sendMessage(new TextComponentString(ownerName + " already exist!"));
			}
			else
			{
				RaiderManager.addNewRaider(ownerName, weight);	
				sender.sendMessage(new TextComponentString(ownerName + " was added!"));
			}
			
		}
		else if(args[1].equalsIgnoreCase("remove"))
		{
			if(RaiderManager.raidersList.containsKey(ownerName))
			{
				RaiderManager.removeRaider(ownerName);	
				sender.sendMessage(new TextComponentString(ownerName + " was removed!"));
			}
			else
			{
				sender.sendMessage(new TextComponentString(ownerName + " doesn't exist!"));
			}
		}
	}


}
