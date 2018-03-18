package com.gendeathrow.pmobs.commands.common;

import com.gendeathrow.pmobs.commands.Base_Command;
import com.gendeathrow.pmobs.core.PMSettings;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;

public class SetRaiderLevel extends Base_Command
{

	@Override
	public String getCommand() 
	{
		return "setlevel";
	}

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

	@Override
	public boolean validArgs(String[] args)
	{
		return args.length >= 1;
	}
	
	
	@Override
	public void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender,String[] args) 
	{

        if (args.length > 1)
        {
            int i1;
            
            try 
            {
				i1 = parseInt(args[1], 0);
			} catch (NumberInvalidException e) {
				e.printStackTrace();
				return;
			}
            
            
            this.setAllWorldTimes(server, (i1 * PMSettings.dayDifficultyProgression) * 24000);

            return;
            
            
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
