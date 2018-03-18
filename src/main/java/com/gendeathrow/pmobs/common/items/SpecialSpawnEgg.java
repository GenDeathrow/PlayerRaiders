package com.gendeathrow.pmobs.common.items;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.EntityPlayerRaider;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumFaction;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.google.common.collect.Maps;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpecialSpawnEgg extends Item
{
	   public SpecialSpawnEgg()
	    {
	        this.setCreativeTab(RaidersCore.RaidersTab);
	    }

	    public String getItemStackDisplayName(ItemStack stack)
	    {
	        String s = ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
	        String s1 = getEntityIdFromItem(stack);

	        if (s1 != null)
	        {
	            s = s + " " + I18n.translateToLocal("entity.playerraiders.role."+s1.toLowerCase());
	        }

	        return s;
	    }

	    /**
	     * Called when a Block is right-clicked with this Item
	     */
	    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        if (worldIn.isRemote)
	        {
	            return EnumActionResult.SUCCESS;
	        }
	        else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
	        {
	            return EnumActionResult.FAIL;
	        }
	        else
	        {
	            IBlockState iblockstate = worldIn.getBlockState(pos);

	            pos = pos.offset(facing);
	            double d0 = 0.0D;

	            if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
	            {
	                d0 = 0.5D;
	            }

	            Entity entity = spawnCreature(worldIn, getEntityIdFromItem(stack), (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D);

	            if (entity != null)
	            {
	                if (entity instanceof EntityLivingBase && stack.hasDisplayName())
	                {
	                    entity.setCustomNameTag(stack.getDisplayName());
	                }

	                applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);

	                if (!playerIn.capabilities.isCreativeMode)
	                {
	                    --stack.stackSize;
	                }
	            }

	            return EnumActionResult.SUCCESS;
	        }
	    }

	    /**
	     * Applies the data in the RaiderTag tag of the given ItemStack to the given Entity.
	     */
	    public static void applyItemEntityDataToEntity(World entityWorld, @Nullable EntityPlayer player, ItemStack stack, @Nullable Entity targetEntity)
	    {
	        MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

	        if (minecraftserver != null && targetEntity != null)
	        {
	            NBTTagCompound nbttagcompound = stack.getTagCompound();

	            if (nbttagcompound != null && nbttagcompound.hasKey("RaiderTag", 10))
	            {
	                if (!entityWorld.isRemote && targetEntity.ignoreItemEntityData() && (player == null || !minecraftserver.getPlayerList().canSendCommands(player.getGameProfile())))
	                {
	                    return;
	                }

	                NBTTagCompound nbttagcompound1 = targetEntity.writeToNBT(new NBTTagCompound());
	                UUID uuid = targetEntity.getUniqueID();
	                nbttagcompound1.merge(nbttagcompound.getCompoundTag("RaiderTag"));
	                targetEntity.setUniqueId(uuid);
	                targetEntity.readFromNBT(nbttagcompound1);
	            }
	        }
	    }

	    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	    {
	        if (worldIn.isRemote)
	        {
	            return new ActionResult(EnumActionResult.PASS, itemStackIn);
	        }
	        else
	        {
	            RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);

	            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
	            {
	                BlockPos blockpos = raytraceresult.getBlockPos();

	                if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
	                {
	                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                }
	                else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn))
	                {
	                    Entity entity = spawnCreature(worldIn, getEntityIdFromItem(itemStackIn), (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

	                    if (entity == null)
	                    {
	                        return new ActionResult(EnumActionResult.PASS, itemStackIn);
	                    }
	                    else
	                    {
	                        if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
	                        {
	                            entity.setCustomNameTag(itemStackIn.getDisplayName());
	                        }

	                        applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);

	                        if (!playerIn.capabilities.isCreativeMode)
	                        {
	                            --itemStackIn.stackSize;
	                        }

	                        playerIn.addStat(StatList.getObjectUseStats(this));
	                        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	                    }
	                }
	                else
	                {
	                    return new ActionResult(EnumActionResult.FAIL, itemStackIn);
	                }
	            }
	            else
	            {
	                return new ActionResult(EnumActionResult.PASS, itemStackIn);
	            }
	        }
	    }

	    /**
	     * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	     * Parameters: world, entityID, x, y, z.
	     */
	    @Nullable
	    public static Entity spawnCreature(World worldIn, @Nullable String entityID, double x, double y, double z)
	    {
	        if (entityID != null && RAIDERS_EGGS.containsKey(entityID))
	        {
	            Entity entity = null;

	            RaiderEggInfo egginfo = RAIDERS_EGGS.get(entityID);
	            
	            for (int i = 0; i < 1; ++i)
	            {
	                entity = new EntityPlayerRaider(worldIn);

	                if (entity instanceof EntityPlayerRaider)
	                {
	                	EntityPlayerRaider entityliving = (EntityPlayerRaider)entity;
	                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
	                    entityliving.rotationYawHead = entityliving.rotationYaw;
	                    entityliving.renderYawOffset = entityliving.rotationYaw;
	                    entityliving.CreateRaider(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null, egginfo.role, egginfo.faction);
	                    worldIn.spawnEntity(entity);
	                    entityliving.playLivingSound();
	                }
	            }

	            return entity;
	        }
	        else
	        {
	            return null;
	        }
	    }

	    /**
	     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	     */
	    @SideOnly(Side.CLIENT)
	    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
	    {
	    	
	    	
	    	for(RaiderEggInfo egg : RAIDERS_EGGS.values())
	    	{
		    	ItemStack raiderNoClass = new ItemStack(itemIn, 1);
	    			applyIDToItemStack(raiderNoClass, egg.spawnedID);
	    			subItems.add(raiderNoClass);	
	    	}
	    	/*
	    	ItemStack raiderNoClass = new ItemStack(itemIn, 1);
	    		applyIDToItemStack(raiderNoClass, EnumRaiderRole.NONE.toString(),  EnumRaiderRole.NONE.toString(), EnumFaction.FRIENDLY.toString());
	    		 subItems.add(raiderNoClass);

	    	ItemStack raiderBrute = new ItemStack(itemIn, 1);
	    		applyIDToItemStack(raiderBrute, EnumRaiderRole.BRUTE.toString(), EnumRaiderRole.BRUTE.toString(),  EnumFaction.HOSTILE.toString());
	    		 subItems.add(raiderBrute);
	    		 
	    	ItemStack raiderWitch = new ItemStack(itemIn, 1);
	    		applyIDToItemStack(raiderWitch, EnumRaiderRole.WITCH.toString(),  EnumRaiderRole.WITCH.toString(),  EnumFaction.HOSTILE.toString());
	    		 subItems.add(raiderWitch);
	    		 
	    	ItemStack raiderPyro = new ItemStack(itemIn, 1);
	    		applyIDToItemStack(raiderPyro, EnumRaiderRole.PYROMANIAC.toString(),  EnumRaiderRole.PYROMANIAC.toString(),  EnumFaction.HOSTILE.toString());
	    		 subItems.add(raiderPyro);
	    		 
	    	ItemStack raiderTweaker = new ItemStack(itemIn, 1);
	    		applyIDToItemStack(raiderTweaker, EnumRaiderRole.TWEAKER.toString(), EnumRaiderRole.TWEAKER.toString(),  EnumFaction.HOSTILE.toString());
	    		 subItems.add(raiderTweaker);
	    		 
	    	ItemStack raiderRanged = new ItemStack(itemIn, 1);
	    	applyIDToItemStack(raiderRanged, EnumRaiderRole.RANGED.toString(), EnumRaiderRole.RANGED.toString(),  EnumFaction.HOSTILE.toString());
	    		 subItems.add(raiderRanged);
	    		 
		    ItemStack raiderFriendly= new ItemStack(itemIn, 1);
		    	applyIDToItemStack(raiderFriendly, EnumFaction.FRIENDLY.toString(), EnumRaiderRole.NONE.toString(), EnumFaction.FRIENDLY.toString());
		    	 subItems.add(raiderFriendly);
*/
	    }
	    

	    /**
	     * APplies the given entity ID to the given ItemStack's NBT data.
	     */
	    
	    @SideOnly(Side.CLIENT)
	    public static void applyIDToItemStack(ItemStack stack, String id)
	    {
	        NBTTagCompound nbttagcompound = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
	        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
	        nbttagcompound1.setString("id", id);
	        nbttagcompound.setTag("RaiderTag", nbttagcompound1);
	        stack.setTagCompound(nbttagcompound);
	    }
	    
	    /**
	     * Gets the entity ID associated with a given ItemStack in its NBT data.
	     */
	    @Nullable
	    public static String getEntityIdFromItem(ItemStack stack)
	    {
	        NBTTagCompound nbttagcompound = stack.getTagCompound();

	        if (nbttagcompound == null)
	        {
	            return null;
	        }
	        else if (!nbttagcompound.hasKey("RaiderTag", 10))
	        {
	            return null;
	        }
	        else
	        {
	            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("RaiderTag");
	            return !nbttagcompound1.hasKey("id", 8) ? null : nbttagcompound1.getString("id");
	        }
	    }
	    
	    
	    public static final Map<String, RaiderEggInfo> RAIDERS_EGGS = Maps.<String, RaiderEggInfo>newLinkedHashMap();
	    
	    
	    
	    static
	    {
	    	RAIDERS_EGGS.put(EnumRaiderRole.NONE.toString(), new RaiderEggInfo(EnumRaiderRole.NONE.toString(), -3971048, 15677239, EnumRaiderRole.NONE, EnumFaction.HOSTILE));
	    	RAIDERS_EGGS.put(EnumRaiderRole.BRUTE.toString(), new RaiderEggInfo(EnumRaiderRole.BRUTE.toString(), -3971048, 1310720, EnumRaiderRole.BRUTE, EnumFaction.HOSTILE));
	    	RAIDERS_EGGS.put(EnumRaiderRole.PYROMANIAC.toString(), new RaiderEggInfo(EnumRaiderRole.PYROMANIAC.toString(), -3971048, 15921673, EnumRaiderRole.PYROMANIAC, EnumFaction.HOSTILE));
	    	RAIDERS_EGGS.put(EnumRaiderRole.TWEAKER.toString(), new RaiderEggInfo(EnumRaiderRole.TWEAKER.toString(), -3971048, 10492835, EnumRaiderRole.TWEAKER, EnumFaction.HOSTILE));
	    	RAIDERS_EGGS.put(EnumRaiderRole.WITCH.toString(), new RaiderEggInfo(EnumRaiderRole.WITCH.toString(), -3971048, 971165, EnumRaiderRole.WITCH, EnumFaction.HOSTILE));
	    	RAIDERS_EGGS.put(EnumFaction.FRIENDLY.toString(), new RaiderEggInfo(EnumFaction.FRIENDLY.toString(), -3971048, 65301, EnumRaiderRole.NONE, EnumFaction.FRIENDLY));
	    	RAIDERS_EGGS.put(EnumRaiderRole.RANGED.toString(), new RaiderEggInfo(EnumRaiderRole.RANGED.toString(), -3971048, 13132821, EnumRaiderRole.RANGED, EnumFaction.HOSTILE));
	    	//RAIDERS_EGGS.put(EnumRaiderRole.DROPPERS.toString(), new RaiderEggInfo(EnumFaction.FRIENDLY.toString(), -3971048, 65301, EnumRaiderRole.NONE, EnumFaction.FRIENDLY));
	    }
	    
	    public static class RaiderEggInfo
	    {
	        /** The entityID of the spawned mob */
	        public final String spawnedID;
	        /** Base color of the egg */
	        public final int primaryColor;
	        /** Color of the egg spots */
	        public final int secondaryColor;
	        
	        public final EnumRaiderRole role;
	        
	        public final EnumFaction faction;

	        public RaiderEggInfo(String spawnedIDIn, int primColor, int secondColor, EnumRaiderRole roleIn, EnumFaction factionIn)
	        {
	            this.spawnedID = spawnedIDIn;
	            this.primaryColor = primColor;
	            this.secondaryColor = secondColor;
	            this.role = roleIn;
	            this.faction = factionIn;
	        }
	    }

}
