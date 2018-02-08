package com.gendeathrow.pmobs.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommonCommands extends CommandBase
{
	ArrayList<Base_Command> coms = new ArrayList<Base_Command>();
	
	public CommonCommands()
	{
		coms.add(new AddNewPlayer());
		coms.add(new UpdateTwitchWhitelist());
		//coms.add(new SetRaiderLevel());
		coms.add(new SendRaidNoticeTest());
	}
	
	@Override
	public String getName() 
	{
		return "raiders";
	}

	@Override
	public String getUsage(ICommandSender sender) 
	{
		String txt = "";
		
		for(int i = 0; i < coms.size(); i++)
		{
			Base_Command c = coms.get(i);
			txt += "/raiders " + c.getCommand();
			
			if(c.getUsageSuffix().length() > 0)
			{
				txt += " " + c.getUsageSuffix();
			}
			
			if(i < coms.size() -1)
			{
				txt += ", ";
			}
		}
		
		return txt;

	}
	
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
		if(args.length == 1)
		{
			ArrayList<String> base = new ArrayList<String>();
			for(Base_Command c : coms)
			{
				base.add(c.getCommand());
			}
        	return getListOfStringsMatchingLastWord(args, base.toArray(new String[0]));
		} else if(args.length > 1)
		{
			for(Base_Command c : coms)
			{
				if(c.getCommand().equalsIgnoreCase(args[0]))
				{
					return getListOfStringsMatchingLastWord(args, c.autoComplete(sender, args));
				}
			}
		}
		
		return new ArrayList<String>();
    }

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if(args.length < 1)
		{
			throw new WrongUsageException(this.getUsage(sender));
		}

		for(Base_Command c : coms)
		{
			if(c.getCommand().equalsIgnoreCase(args[0]))
			{
				if(c.validArgs(args))
				{
					c.runCommand(server, this, sender, args);
					return;
				} else
				{
					throw c.getException(this);
				}
			}
		}
		
		throw new WrongUsageException(this.getUsage(sender));
	}

}
