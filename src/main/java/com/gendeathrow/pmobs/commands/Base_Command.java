package com.gendeathrow.pmobs.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public abstract class Base_Command 
{
	public abstract String getCommand();
	
	public List<String> getCommandAliases() 
	{
		return Collections.<String>emptyList();
	}
	
	public String getUsageSuffix()
	{
		return "";
	}
	
	/**
	 * Are the passed arguments valid?<br>
	 * NOTE: Argument 1 is always the returned value of getCommand()
	 */
	public boolean validArgs(String[] args)
	{
		return args.length == 1;
	}
	
	public List<String> autoComplete(ICommandSender sender, String[] args)
	{
		return new ArrayList<String>();
	}
	
	public abstract void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args);
	
	public final WrongUsageException getException(CommandBase command)
	{
		String message = command.getName() + " " + getCommand();
		
		if(getUsageSuffix().length() > 0)
		{
			message += " " + getUsageSuffix();
		}
		
		return new WrongUsageException(message);
	}
	
	
    public static int parseInt(String input) throws NumberInvalidException
    {
        try
        {
            return Integer.parseInt(input);
        }
        catch (NumberFormatException var2)
        {
            throw new NumberInvalidException("commands.generic.num.invalid", new Object[] {input});
        }
    }

	
    public static int parseInt(String input, int min) throws NumberInvalidException
    {
        return parseInt(input, min, Integer.MAX_VALUE);
    }

    public static int parseInt(String input, int min, int max) throws NumberInvalidException
    {
        int i = parseInt(input);

        if (i < min)
        {
            throw new NumberInvalidException("commands.generic.num.tooSmall", new Object[] {Integer.valueOf(i), Integer.valueOf(min)});
        }
        else if (i > max)
        {
            throw new NumberInvalidException("commands.generic.num.tooBig", new Object[] {Integer.valueOf(i), Integer.valueOf(max)});
        }
        else
        {
            return i;
        }
    }
}