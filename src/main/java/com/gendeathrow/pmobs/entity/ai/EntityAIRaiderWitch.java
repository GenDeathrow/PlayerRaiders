package com.gendeathrow.pmobs.entity.ai;

import com.gendeathrow.pmobs.core.init.RaidersSoundEvents;
import com.gendeathrow.pmobs.entity.mob.EntityRaiderWitch;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

public class EntityAIRaiderWitch  extends EntityAIBase{

   private EntityRaiderWitch raider;
   private int maxStrikes = 0;
   private boolean triggered = false;
   
   
   public EntityAIRaiderWitch(EntityRaiderWitch raiderIn) {	
	   super();
	   
	   this.raider = raiderIn;
	   this.maxStrikes = raiderIn.getRNG().nextInt(3) + 3;
   }
   
      
   public boolean shouldExecute() {
	   
	   return this.raider.world.getNearestPlayerNotCreative(this.raider, 10) != null || this.triggered;
       //return this.raider.world.isAnyPlayerWithinRangeAt(this.raider.posX, this.raider.posY, this.raider.posZ, 10.0D) || this.triggered;
   }
   
   
   private int ticks = 0;
   private int strikes = 0;
   private boolean hasScreamed = false;

   
   /**
    * Updates the task
    */
   public void updateTask() {
       DifficultyInstance difficultyinstance = this.raider.world.getDifficultyForLocation(new BlockPos(this.raider));

       if(strikes <= maxStrikes) {
    	   if(ticks == 0 || (ticks % 2 == 0 && this.raider.getRNG().nextInt(2) == 1) || ticks % 4 == 0) {
    		   if(!hasScreamed)
    			   this.raider.playSound(RaidersSoundEvents.RAIDERS_WITCH_SCREAM, 3, 1); 

    		   this.raider.world.addWeatherEffect(new EntityLightningBolt(this.raider.world, this.raider.posX + getRandomPosition(), this.raider.posY, this.raider.posZ + getRandomPosition(), true));
    		   if(!this.raider.world.isRaining())
    		   {
    			   this.raider.world.getWorldInfo().setThundering(true);
    			   this.raider.world.getWorldInfo().setThunderTime(600);
    		   }
    		   
    		   this.triggered = true;
    		   strikes++;
    	   }
       }
       else if(strikes >= maxStrikes)
       {
    	   this.raider.setWitchActive(true);
       }
       ticks++;
   }
   
   public int getRandomPosition() {
	   return raider.getRNG().nextInt(20) - 10;
   }
   
   
   void createBarrier(int x0, int y0, int radius) {
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
