package com.gendeathrow.pmobs.common.items;

import java.util.Random;

import com.gendeathrow.pmobs.common.EnumFaction;
import com.gendeathrow.pmobs.common.RaidersSoundEvents;
import com.gendeathrow.pmobs.common.capability.player.IPlayerData;
import com.gendeathrow.pmobs.common.capability.player.PlayerDataProvider;
import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.entity.mob.EntityRaider;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.neutral.EntityDropPod;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class BackupTransmitter extends Item
{
	public BackupTransmitter()
	{
		super();
		 this.setCreativeTab(RaidersMain.RaidersTab);
        this.setMaxStackSize(1);
	}
	
	private boolean hasUsed = false;

	
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemStackIn = playerIn.getHeldItem(handIn);
		
		if(!worldIn.isRemote)
		{

			
			if(playerIn.hasCapability(PlayerDataProvider.PLAYERDATA, null))
			{
				IPlayerData playerdata = playerIn.getCapability(PlayerDataProvider.PLAYERDATA, null);
				
				if(playerdata.canCallDropPod(worldIn) || playerIn.isCreative())
				{
					playerdata.setLastDropPodCall(worldIn.getWorldTime());
					playerIn.sendMessage(new TextComponentTranslation("Calling in Mercenaries"));
					for(int i = worldIn.rand.nextInt(3)+3; i > 0; i--)
					{
						onCallDropPods(worldIn.rand, worldIn, playerIn);
					}
					

				}else
				{
					long millis = (24000 - (worldIn.getWorldTime() - playerdata.geLastDropPodCall())); 

					long second = (millis / 20) % 60;
					
					long minute = (millis / 1200) % 60;
					//long hour = (millis / (1000 * 60 * 60)) % 24;
					
					playerIn.sendMessage(new TextComponentTranslation("Cant use this item for another "+String.format("%02d", minute) +":"+ String.format("%02d", second)));
				}
				
				
			}
			

		}
		playerIn.playSound(RaidersSoundEvents.COMS_BEEP, 1f, 1f);

		
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}

	
	
	
	
//	int range = 60;
//	
//	//this.hasUsed = true;
//	int randX = worldIn.rand.nextInt(range) - (range/2);
//	int randZ = worldIn.rand.nextInt(range) - (range/2);
//	
//	randX = randX > 0 ? MathHelper.clamp_int(randX, 5, (range/2)) : MathHelper.clamp_int(randX, -(range/2), -5);
//	randZ = randZ > 0 ? MathHelper.clamp_int(randZ, 5, (range/2)) : MathHelper.clamp_int(randZ, -(range/2), -5);
//	
//
//	EntityDropPod droppod = new EntityDropPod(worldIn);
//	droppod.setPosition(playerIn.posX + randX, 300, playerIn.posZ + randZ);
//
//	EntityRaiderBase raider = new EntityPlayerRaider(worldIn);
//	raider.onInitialSpawn(worldIn.getDifficultyForLocation(playerIn.getPosition()),  (IEntityLivingData)null);
//	
//	raider.setRaiderFaction(EnumFaction.FRIENDLY);
//	raider.setRaiderRole(EnumRaiderRole.NONE);
//	
//	raider.setLocationAndAngles(droppod.posX, droppod.posY, droppod.posZ, raider.rotationYaw, 0.0F);
//	
//	EntityDropPod.addDropPodtoQueue(raider, droppod);
	
//	@Override
//    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
//    {
//
//    	if(entityIn instanceof EntityPlayer && this.hasUsed  && !worldIn.isRemote)
//    	{
//    		
//			if(this.nextCall-- < 8)
//			{
//				if(this.nextPod >= this.podMax)
//				{
//					this.hasUsed = false;
//					this.nextPod = 0;
//					System.out.println("stop");
//				}
//				else 
//				{
//					this.nextPod++;
//					System.out.println("next");
//					this.nextCall = worldIn.rand.nextInt(10) + 5;
//					
//				}
//			
//				this.onCallDropPods(worldIn.rand, worldIn, (EntityPlayer) entityIn);
//			}
//			
//
//    	}
//    }
    
	public int nextCall = 10;
	private int nextPod = 0;
	private int podMax = 4;
	
	public void onCallDropPods(Random rand, World worldObj, EntityPlayer player)
	{
		int range = 60;
		
		int randX = rand.nextInt(range) - (range/2);
		int randZ = rand.nextInt(range) - (range/2);
		
		randX = randX > 0 ? MathHelper.clamp(randX, 5, (range/2)) : MathHelper.clamp(randX, -(range/2), -5);
		randZ = randZ > 0 ? MathHelper.clamp(randZ, 5, (range/2)) : MathHelper.clamp(randZ, -(range/2), -5);
		
		IBlockState ground = worldObj.getGroundAboveSeaLevel(new BlockPos(player.posX + randX, 300, player.posZ + randZ));
		
		
		EntityDropPod droppod = new EntityDropPod(worldObj);
		droppod.setPosition(player.posX + randX, 300, player.posZ + randZ);
		droppod.playSound(RaidersSoundEvents.SONIC_BOOM, 17, 1f);

		EntityRaiderBase raider = new EntityRaider(worldObj);
		raider.onInitialSpawn(worldObj.getDifficultyForLocation(player.getPosition()),  (IEntityLivingData)null);
		
		raider.setRaiderFaction(EnumFaction.FRIENDLY);
		raider.setLeftHanded(false);
		
		raider.setLocationAndAngles(droppod.posX, droppod.posY, droppod.posZ, raider.rotationYaw, 0.0F);
		
		
		if (Loader.isModLoaded("lcrdrfs")) 
		{
			ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("lcrdrfs:laser_blaster")), 1, 0);
			raider.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
			raider.setDropChance(EntityEquipmentSlot.MAINHAND, 0.05F);
			//raider.targetTasks.addTask(0, new EntityAINearestAttackableTarget((EntityZombie) raider, EntityLivingBase.class, 0, true, false, null));
			//raider.tasks.addTask(2, new EntityAIShootLaser((EntityZombie)raider));
		}

		

		EntityDropPod.addDropPodtoQueue(raider, droppod);
	}

}
