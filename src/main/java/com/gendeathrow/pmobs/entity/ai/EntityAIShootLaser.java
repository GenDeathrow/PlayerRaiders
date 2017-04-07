package com.gendeathrow.pmobs.entity.ai;

import java.util.UUID;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Loader;

import com.mojang.authlib.GameProfile;

public class EntityAIShootLaser extends EntityAIBase 
{

	private final EntityLivingBase parentEntity;
	public int attackTimer;

	public EntityAIShootLaser(EntityLivingBase raider) 
	{
		parentEntity = raider;
	}

	public boolean shouldExecute() 
	{
		if (parentEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) != null && ((EntityLiving) parentEntity).getAttackTarget() != null) 
		{
			if (Loader.isModLoaded("lcrdrfs")) 
			{
				if(parentEntity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).getItem().getRegistryName().toString().equals("lcrdrfs:laser_blaster"))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void startExecuting() 
	{
		attackTimer = 0;
	}

	public void resetTask() 
	{
		((EntityLiving) parentEntity).setAttackTarget(null);
	}

	public void updateTask() 
	{
		EntityLivingBase entitylivingbase = ((EntityLiving) parentEntity).getAttackTarget();
		
		if(parentEntity instanceof EntityZombie) 
		{
			EntityZombie raider = (EntityZombie) parentEntity;
			raider.setArmsRaised(false);
		}
		
		ItemStack stack = parentEntity.getHeldItemMainhand();
		if (entitylivingbase != null) 
		{
			if (!parentEntity.worldObj.isRemote && parentEntity.getHeldItemMainhand() != null && parentEntity.getHeldItemMainhand() == stack) 
			{
				World world = parentEntity.worldObj;
				if (entitylivingbase.getDistanceSqToEntity(parentEntity) < 576 && entitylivingbase.getDistanceSqToEntity(parentEntity) > 9 && parentEntity.canEntityBeSeen(entitylivingbase)) 
				{
					++attackTimer;
					if (attackTimer == 10) 
					{
						stack.getItem().onItemRightClick(stack, parentEntity.worldObj, getFakePlayer(), EnumHand.MAIN_HAND);
						attackTimer = -1;
					}
				}
			}
		} else if (attackTimer > 0)
			--attackTimer;
	}

	public EntityPlayer getFakePlayer() 
	{
		EntityPlayerMP fakePlayer = FakePlayerFactory.get((WorldServer)parentEntity.worldObj, new GameProfile(UUID.nameUUIDFromBytes("Laser Raider".getBytes()), "Laser Raider"));
		fakePlayer.copyLocationAndAnglesFrom(parentEntity);
		fakePlayer.setRotationYawHead(parentEntity.getRotationYawHead());
		return fakePlayer;
	}
}
