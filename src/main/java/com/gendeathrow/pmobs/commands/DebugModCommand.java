package com.gendeathrow.pmobs.commands;

import com.gendeathrow.pmobs.core.PMSettings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class DebugModCommand extends Base_Command
{

	@Override
	public String getCommand() 
	{
		return "debugmode";
	}

    public int getRequiredPermissionLevel()
    {
        return 2;
    }
    
	@Override
	public String getUsageSuffix()
	{
		return "/raiders debugmode <true/false>";
	}
	
	@Override
	public boolean validArgs(String[] args)
	{
		return args.length > 1;
	}
	
	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender,String[] args) 
	{

        if (args.length > 1)
        {
        	boolean i1 = false;
            
            try 
            {
            	i1 = Boolean.parseBoolean(args[1]);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
            sender.getCommandSenderEntity().sendMessage(new TextComponentString("Debug Mode set: "+ i1));
            PMSettings.debugMode = i1;
        }
	}
	
    protected void setAllWorldTimes(MinecraftServer server, int time)
    {
        for (int i = 0; i < server.worlds.length; ++i)
        {
            server.worlds[i].setWorldTime((long)time);
        }
    }
	
}
