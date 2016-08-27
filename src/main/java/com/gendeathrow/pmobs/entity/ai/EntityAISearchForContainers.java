package com.gendeathrow.pmobs.entity.ai;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class EntityAISearchForContainers extends EntityAIBase
{

	@Override
	public boolean shouldExecute() {
		// TODO Auto-generated method stub
		return false;
	}

//	  protected EntityLiving theThief;
//	    /** The closest entity which is being watched by this one. */
//	    protected TileEntity closestEntity;
//	    /** This is the Maximum distance that the AI will look for the Entity */
//	    protected float maxDistanceForPlayer;
//	   // private int lookTime;
//	    private final float chance;
//	    //protected Class <? extends Entity > watchedClass;
//	    
//	    private static final Method methodIsChunkLoaded;
//
//	    static 
//	    {
//	        Method m;
//	        try {
//	            m = World.class.getDeclaredMethod("isChunkLoaded", int.class, int.class, boolean.class);
//	            m.setAccessible(true);
//	       } catch (Exception e) {
//	            throw new RuntimeException(e);
//	       }
//	       methodIsChunkLoaded = m;
//	    }
//	    
//	    public EntityAISearchForContainers(EntityLiving entitylivingIn, float maxDistance)
//	    {
//	    	this.theThief = entitylivingIn;
//	    	
//	    	this.maxDistanceForPlayer = maxDistance;
//	    	
//	    	this.chance = 0.02F;
//	    	
//	        this.setMutexBits(1);
//	    }
//	    
//	    
//	    @Override
//	    public boolean shouldExecute() 
//	    {
//	        if (this.theThief.getRNG().nextFloat() >= this.chance)
//	        {
//	            return false;
//	        }
//	        else
//	        {
//	        	try 
//	        	{
//					List<TileEntity> list = this.getEntitiesWithinAABB(TileEntityChest.class, this.theThief.getEntityBoundingBox().expand((double)this.maxDistanceForPlayer, 3.0D, (double)this.maxDistanceForPlayer), null);
//					
//					if(list.isEmpty()) return false;
//					else
//					{
//						this.closestEntity = list.get(0);
//						return true;
//					}
//
//				} catch (IllegalAccessException e){e.printStackTrace();} 
//	        	  catch (IllegalArgumentException e){e.printStackTrace();}
//	        	  catch (InvocationTargetException e){e.printStackTrace();}
//	        	
//	        	return false;
//	        }
//	    }
//	    
//	    @Override
//	    public void startExecuting()
//	    {
//	       // DEBUG
//	       System.out.println("search for container startExecute()");
//	    }
//	    
//	    @Override
//	    public boolean continueExecuting()
//	    {
////	       theEntity.decrementRearingCounter();;
//	       Boolean continueExecuting = true;
////	       if (!continueExecuting)
////	       {
////	          theEntity.setRearing(false);
////	          // now attack back
////	          theEntity.setAttackTarget(theEntity.getLastAttacker()); 
////	       }
////	       // DEBUG
//	       System.out.println("searchforcontainer continueExecuting = true");
////	          +continueExecuting);
//	       return (continueExecuting);
//	    }
//	    
//	    
//	    
//	    public <T extends TileEntity> void getEntitiesOfTypeWithinAAAB(Chunk chunk, Class <? extends TileEntity > entityClass, AxisAlignedBB aabb, List<TileEntity> list, Predicate<? super T> filter)
//	    {
//	        int i = MathHelper.floor_double((aabb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
//	        int j = MathHelper.floor_double((aabb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
//	        i = MathHelper.clamp_int(i, 0, chunk.getTileEntityMap().values().size() - 1);
//	        j = MathHelper.clamp_int(j, 0, chunk.getTileEntityMap().values().size() - 1);
//
//	        Iterator iterator = chunk.getTileEntityMap().values().iterator();
//			
//			while (iterator.hasNext())
//			{
//				TileEntity te = (TileEntity)iterator.next();
//				if(entityClass.isInstance(te))
//				{
//					if (te.getRenderBoundingBox().intersectsWith(aabb))
//	                {
//						list.add(te);
//	                }
//				}
//			}
//	    }
//	    
//	    
//	    public <T extends TileEntity> List<TileEntity> getEntitiesWithinAABB(Class <? extends TileEntity> clazz, AxisAlignedBB aabb, @Nullable Predicate <? super T > filter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
//	    {
//	        int i = MathHelper.floor_double((aabb.minX - maxDistanceForPlayer) / 16.0D);
//	        int j = MathHelper.ceiling_double_int((aabb.maxX + maxDistanceForPlayer) / 16.0D);
//	        int k = MathHelper.floor_double((aabb.minZ - maxDistanceForPlayer) / 16.0D);
//	        int l = MathHelper.ceiling_double_int((aabb.maxZ + maxDistanceForPlayer) / 16.0D);
//	        List<TileEntity> list = Lists.<TileEntity>newArrayList();
//
//	        for (int i1 = i; i1 < j; ++i1)
//	        {
//	            for (int j1 = k; j1 < l; ++j1)
//	            {
//	                if ((boolean) methodIsChunkLoaded.invoke(i1, j1, true))
//	                {
//	                    this.getEntitiesOfTypeWithinAAAB(this.theThief.worldObj.getChunkFromChunkCoords(i1, j1), clazz, aabb, list, filter);
//	                }
//	            }
//	        }
//
//	        return list;
//	    }

}
