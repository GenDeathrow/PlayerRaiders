package com.gendeathrow.pmobs.common.Potion;

import java.awt.Color;

import com.gendeathrow.pmobs.core.RaidersCore;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class BruteSerumEffect  extends Potion 
{

	public BruteSerumEffect() 
	{
		super(false, Color.black.getRGB());
		this.setBeneficial();
		this.setRegistryName(RaidersCore.MODID, "bruteserum");
		
	
	}
	
	@Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
    {
    	
    }
	
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
	
}
