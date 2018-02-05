package com.gendeathrow.pmobs.common.potion;

import java.awt.Color;

import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class BruteSerumEffect  extends Potion 
{

	public BruteSerumEffect() 
	{
		super(false, Color.black.getRGB());
		this.setBeneficial();
		this.setRegistryName(RaidersMain.MODID, "bruteserum");
		this.setPotionName("effect."+RaidersMain.MODID+".bruteserum");
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "f38152a2-32d4-11e7-93ae-92361f002671", -0.15000000596046448D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MAX_HEALTH, "53771a48-32d5-11e7-93ae-92361f002671", 1.25, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "53771cf0-32d5-11e7-93ae-92361f002671", 20D, 0);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "33c306e0-33a1-11e7-a919-92ebcb67fe33", -0.10000000149011612D, 2);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "cffb7800-32d7-11e7-93ae-92361f002671", 4D, 0);
		this.setIconIndex(4, 0);
	}
	
	
    @Override
    public boolean isReady(int duration, int amplifier)
    {
        return duration >= 1;
    }
    
    
    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
    	super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    	
    	if(entityLivingBaseIn instanceof EntityPlayer)
    	{
    		((EntityPlayer)entityLivingBaseIn).eyeHeight = 2.5F;
    	}
    	
    	
    }
    
    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
    {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);

        if (entityLivingBaseIn.getHealth() > entityLivingBaseIn.getMaxHealth())
        {
            entityLivingBaseIn.setHealth(entityLivingBaseIn.getMaxHealth());
        }
        
    	
    	if(entityLivingBaseIn instanceof EntityPlayer)
    	{
    		((EntityPlayer)entityLivingBaseIn).eyeHeight =((EntityPlayer)entityLivingBaseIn).getDefaultEyeHeight();;
    		((EntityPlayer)entityLivingBaseIn).stepHeight -= 1.4;
    	}
    	
    	
		entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 1200));
		entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 600, 3));
		entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 1200, 2));
		entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 1));
    }
	
}
