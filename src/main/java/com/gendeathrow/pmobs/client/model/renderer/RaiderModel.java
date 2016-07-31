package com.gendeathrow.pmobs.client.model.renderer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;

import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

public class RaiderModel extends ModelPlayer
{

	public RaiderModel(float modelSize, boolean smallArmsIn) 
	{
		super(modelSize, smallArmsIn);
	}
	

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
	{
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);

		if (itemstack != null && itemstack.getItem() == Items.BOW && ((EntityRangedAttacker)entitylivingbaseIn).isSwingingArms())
		{
			if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT)
			{
				this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
			else
			{
				this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}

		super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
	}
	
}
