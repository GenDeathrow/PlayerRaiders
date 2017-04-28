package com.gendeathrow.pmobs.entity;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.RaidersCore;
import com.gendeathrow.pmobs.entity.New.EntityRangedAttacker;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumFaction;
import com.gendeathrow.pmobs.entity.New.EntityRaiderBase.EnumRaiderRole;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.google.common.collect.Iterables;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.Property;

public class EntityPlayerRaider extends EntityRangedAttacker
{

	public EntityPlayerRaider(World worldIn) 
	{
		super(worldIn);
	}
	

	
	public void CreateRaider(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata, EnumRaiderRole role, EnumFaction faction)
	{
	       livingdata = super.onInitialSpawn(difficulty, livingdata);
	        
	        float f = difficulty.getClampedAdditionalDifficulty();
	        
	        this.setRandomFeatures(); 
	        
	        this.setRaiderFaction(faction);
	        
	        this.setCanPickUpLoot(true);
	      
	        this.playerProfile = randomSkin();//RaiderManager.getRandomRaider().getProfile();

	        setOwner(this.playerProfile != null ? this.playerProfile.getName() : "Steve");

	        this.setCustomNameTag(getPlayerProfile().getName());
	        
	        // Make sure they can walk though doors
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);

	        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
    	
	        this.setAlwaysRenderNameTag(false);
	        
	        this.setEquipmentBasedOnDifficulty(difficulty);
	        
	        this.setEnchantmentBasedOnDifficulty(difficulty);

	        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
	        {
	            Calendar calendar = this.worldObj.getCurrentDate();

	            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
	            {
	                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
	                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
	            }
	        }
	        
	        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
	        
	        double d0 = this.rand.nextDouble() * 1.5D * (double)f;

	        if (d0 > 1.0D)
	        { 
	            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
	        }

	        
	        if(!this.isHeroBrine() && !this.isChild())
	        {
	        	this.setRaiderRole(role);
	        	
	        	if(PMSettings.tweakerOnlyNight && this.worldObj.isDaytime() && this.getRaiderRole() == EnumRaiderRole.TWEAKER)
	        	{
	        		this.setRaiderRole(EnumRaiderRole.NONE);
	        	}
	        	
	        	if(this.getRaiderRole().equals(EnumRaiderRole.TWEAKER))
	        	{
	        		this.setTweakerMelee(true);
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Tweaker Health", -.5, 2));
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.BRUTE))
	        	{
		        	this.setBrute(true);
	        		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(.7);
	        		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(.15);
	        		this.stepHeight = 2F;
	        		//Apparetnly this is side only
	        		//this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeAllModifiers();
	        		this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
		        	this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(7);
		        	this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Brute Health", 1.25, 2));
		        	if(this.getHeldItem(EnumHand.OFF_HAND) != null) this.setHeldItem(EnumHand.OFF_HAND, null);
		        	if(this.getHeldItem(EnumHand.MAIN_HAND) == null || this.getHeldItem(EnumHand.MAIN_HAND).getItem() != Items.IRON_SWORD) this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));

	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.PYROMANIAC))
	        	{
	        		this.setPyromaniac(true);
	        		this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.FLINT_AND_STEEL));
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.WITCH))
	        	{
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15);
	        		this.setRaiderFaction(EnumFaction.HOSTILE);
	        		
	        		this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
	        		this.setHealth(this.getMaxHealth());
	        		
	        	}else if(this.getRaiderRole().equals(EnumRaiderRole.RANGED))
	        	{
	        		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5);

	        		this.isRangedAttacker = true;
	        		this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	        		
	        		if(this.rand.nextDouble() < .05 + (this.difficultyManager.calculateProgressionDifficulty(.05) > .25 ? .25 : this.difficultyManager.calculateProgressionDifficulty(.05))) 
	        		{
	        			try
	        			{
	        				ItemStack tippedArrow = EquipmentManager.getRandomArrows().getCopy();
	        				if(tippedArrow != null)	this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, tippedArrow);
	        			}catch(Exception e){ RaidersCore.logger.error(e); }
	        		}
	                this.tasks.addTask(1, this.aiArrowAttack);
	                this.removeAllModifiers(SharedMonsterAttributes.MOVEMENT_SPEED);
	                
	        	}
	           	
	        	
	        }
	        

	        if(this.getRaiderRole() == EnumRaiderRole.NONE && this.worldObj.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance && !this.getOwner().equalsIgnoreCase("herobrine") && !((EntityRangedAttacker)this).isRangedAttacker) 
	        {
	        	this.setChild(true); 
	        	//this.setFeatures(0);
	        }
  
	        
	        if(!this.isChild() && (this.getRaiderRole() == EnumRaiderRole.NONE || this.getRaiderRole() == EnumRaiderRole.PYROMANIAC) && PMSettings.leapAttackAI && rand.nextDouble() < .15 + difficultyManager.calculateProgressionDifficulty(.05, .35))
	        {
	        	this.setLeapAttack(true);
	        }
	        
	        difficultyManager.setHealthDifficulty(difficulty);
	        
	        difficultyManager.setDamageDifficulty(difficulty);
	        
	        if(!this.isChild() && !this.isHeroBrine())
	        {
	        	difficultyManager.setSpeedDifficulty(difficulty);
	        }
	        
	        this.setHealth(this.getMaxHealth());
	       	        
	        this.applyEntityAIPostInitalSpawn();
 
	}	
	
	public static void CreateBrute()
	{
		
	}
	
	public static void CreateWitch()
	{
		
	}
	
	public static void CreateRanged()
	{
		
	}
	
	public static void CreateTweaker()
	{
		
	}
	
	
	
	@Override
    @SuppressWarnings(value = { "unchecked" })
    public ResourceLocation getLocationSkin()
    {
		ResourceLocation resourcelocation =  DefaultPlayerSkin.getDefaultSkinLegacy();
		
	
		
		if (this.getPlayerProfile() != null && this.getPlayerProfile().getName() != null) 
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			
            Property property = (Property)Iterables.getFirst(this.getPlayerProfile().getProperties().get("textures"), null);

            if ((this.profileset == true && property == null ))
            {
            	//RaidersSkinManager.addToBadList(this.getOwner());
            	return resourcelocation;
            }
            else if(this.profileset = false)
            {
            	//RaidersSkinManager.addToBadList(this.getOwner());
            	return resourcelocation;
            }
            
            
			Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(getPlayerProfile());

			if (map.containsKey(Type.SKIN))
			{
				resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
			}
		}
		else
		{
			if(RaiderManager.getRaiderProfile(this.getOwner()) != null)
			{
				this.setPlayerProfile(RaiderManager.getRaiderProfile(this.getOwner()));
			}
			else
			{
				this.setPlayerProfile(RaiderManager.getRaiderProfile(this.getOwner()));
				
				RaidersSkinManager.updateProfile(this);
			
			}
		}
		
		return resourcelocation;
    	
    }

}
