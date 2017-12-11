package com.gendeathrow.pmobs.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.entity.Entity;

public class ObfHelper 
{
	public static boolean obfuscation;
	public static Method setSizeMethod;

	public static final String setSizeObf = "func_70105_a";
	public static final String setSizeDeobf = "setSize";
	
	
	public static void detectObfuscation()
    {
        obfuscation = true;
        try
        {
            Field[] fields = Class.forName("net.minecraft.item.ItemBlock").getDeclaredFields();
            for(Field f : fields)
            {
            	f.setAccessible(true);
            	if(f.getName().equalsIgnoreCase("block"))
            	{
            		obfuscation = false;
            		return;
            	}
            }
        }
        catch (Exception e)
        {
        }
    }
    
	public static void forceSetSize(Entity ent, float width, float height)
	{
		if(setSizeMethod == null)
		{
			try
			{
				Method m = Entity.class.getDeclaredMethod(ObfHelper.obfuscation ? ObfHelper.setSizeObf : ObfHelper.setSizeDeobf, float.class, float.class);
				setSizeMethod = m;
			}
			catch(NoSuchMethodException e)
			{
				ent.width = width;
				ent.height = height;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		if(setSizeMethod != null)
		{
			try
			{
				setSizeMethod.setAccessible(true);
				setSizeMethod.invoke(ent, width, height);		
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
