package com.gendeathrow.pmobs.entity.New;


import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRider extends EntityHeroBrine
{
	
	private EntityHorse horse;

	public EntityRider(World worldIn) 
	{
		super(worldIn);
	}

	
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		

	}
	    
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		
	}
	    
	public void onUpdate()
	{
//		if(this.horse != null && !this.horse.equals(this.getRidingEntity()))
//		{
//			this.horse.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
//			this.startRiding(this.horse,true);
//		}
//		
//		if(this.isDead && this.horse != null) this.horse.setDead();
//		
//		if(this.getRidingEntity() instanceof EntityHorse)
//		{
//			System.out.println(this.getCustomNameTag() +": @ "+ this.posX + ", " + this.posY + ", "+ this.posZ);
//			System.out.println(this.getRidingEntity().getCustomNameTag() +": @ "+ this.getRidingEntity().posX + ", " + this.getRidingEntity().posY + ", "+ this.getRidingEntity().posZ);
//			System.out.println("--------------------------------------------");
//		}
		super.onUpdate();
	}
	/**
	 * Gives armor or weapon for entity based on given DifficultyInstance
	 */
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
	{

		super.setEquipmentBasedOnDifficulty(difficulty);
	}

		
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
	{
		
		livingdata = super.onInitialSpawn(difficulty, livingdata);
//		
//		if(this.horse!=null) System.out.println("horse");
//	
//		if(this.rand.nextDouble() < .08d && !this.isChild() && !this.isHeroBrine())
//		{
//			
//			List<EntityHorse> list = this.worldObj.<EntityHorse>getEntitiesWithinAABB(EntityHorse.class, this.getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
//			
//			if (!list.isEmpty())
//			{
//				if(this.startRiding((EntityHorse)list.get(0))) this.horse = (EntityHorse)list.get(0);
//			}
//			else
//			{
//			
//				this.horse = new EntityHorse(worldObj);
//				this.horse.onInitialSpawn(difficulty, (IEntityLivingData)null);
//				this.horse.setPosition(this.posX, this.posY, this.posZ);
//				this.horse.setType(HorseType.HORSE);
//				this.horse.setRearing(false);
//				this.horse.setSprinting(true);
//				this.horse.setHorseTamed(true);
//				this.horse.setGrowingAge(0);
//				this.horse.setCustomNameTag(this.getCustomNameTag() +"'s Horse");
//				this.worldObj.spawnEntityInWorld(this.horse);
//				this.startRiding(this.horse, true);
////			}
//			
//			if(this.horse == null) return livingdata;
//			System.out.println(this.getCustomNameTag() +": @ "+ this.posX + ", " + this.posY + ", "+ this.posZ);
//			System.out.println(this.horse.getCustomNameTag() +": @ "+ horse.posX + ", " + horse.posY + ", "+ horse.posZ);
//			System.out.println("--------------------------------------------");
//
//			
//			
//		}

			
		return livingdata;
	}
	
}
