package com.gendeathrow.pmobs.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

import com.gendeathrow.pmobs.entity.New.EntityRaiderBase;
import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;

public class EntityAIScreamer  extends EntityAIBase{

   private EntityRangedAttacker raider;
   private int maxStrikes = 0;
   private boolean triggered = false;
   
   
   public EntityAIScreamer(EntityRangedAttacker raiderIn)
   {
	   super();
	   
	   this.raider = raiderIn;
	   this.maxStrikes = raiderIn.getRNG().nextInt(3) + 3;
   }
   
      
   public boolean shouldExecute()
   {
       return this.raider.worldObj.isAnyPlayerWithinRangeAt(this.raider.posX, this.raider.posY, this.raider.posZ, 10.0D) || this.triggered;
   }
   
   
   private int ticks = 0;
   private int strikes = 0;
   private boolean hasScreamed = false;

   
   /**
    * Updates the task
    */
   public void updateTask()
   {
       DifficultyInstance difficultyinstance = this.raider.worldObj.getDifficultyForLocation(new BlockPos(this.raider));

       if(strikes <= maxStrikes)
       {
    	   if(ticks == 0 || (ticks % 2 == 0 && this.raider.getRNG().nextInt(2) == 1) || ticks % 4 == 0)
    	   {
    		   if(!hasScreamed)
    			   this.raider.playSound(com.gendeathrow.pmobs.common.SoundEvents.RAIDERS_WITCH_SCREAM, 3, 1); 

    		   this.raider.worldObj.addWeatherEffect(new EntityLightningBolt(this.raider.worldObj, this.raider.posX + getRandomPosition(), this.raider.posY, this.raider.posZ + getRandomPosition(), true));
    		   if(!this.raider.worldObj.isRaining())
    		   {
    			   this.raider.worldObj.getWorldInfo().setThundering(true);
    			   this.raider.worldObj.getWorldInfo().setThunderTime(600);
    		   }
    		   
    		   this.triggered = true;
    		   strikes++;
    	   }
       }
       else if(strikes >= maxStrikes)
       {
    	   this.raider.setWitchCombat();
       }
       ticks++;
   }
   
   public int getRandomPosition()
   {
	   return raider.getRNG().nextInt(20) - 10;
   }
   
   
   void createBarrier(int x0, int y0, int radius)
   {
       int x = radius;
       int y = 0;
       int err = 0;

       while (x >= y)
       {
           //putpixel(x0 + x, y0 + y);
           //putpixel(x0 + y, y0 + x);
           //putpixel(x0 - y, y0 + x);
           //putpixel(x0 - x, y0 + y);
           //putpixel(x0 - x, y0 - y);
           //putpixel(x0 - y, y0 - x);
           //putpixel(x0 + y, y0 - x);
           //putpixel(x0 + x, y0 - y);

           if (err <= 0)
           {
               y += 1;
               err += 2*y + 1;
           }
           if (err > 0)
           {
               x -= 1;
               err -= 2*x + 1;
           }
       }
	   
   }
   

  
}
