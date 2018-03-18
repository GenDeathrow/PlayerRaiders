package com.gendeathrow.pmobs.entity.mob;

import java.util.Calendar;
import java.util.UUID;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.client.LayerFeatures;
import com.gendeathrow.pmobs.client.RaidersSkinManager;
import com.gendeathrow.pmobs.common.EnumFaction;
import com.gendeathrow.pmobs.core.PMSettings;
import com.gendeathrow.pmobs.core.init.RaidersSoundEvents;
import com.gendeathrow.pmobs.entity.ai.EntityAIShootLaser;
import com.gendeathrow.pmobs.entity.neutral.EntityDropPod;
import com.gendeathrow.pmobs.handlers.DifficultyProgression;
import com.gendeathrow.pmobs.handlers.EquipmentManager;
import com.gendeathrow.pmobs.handlers.RaiderManager;
import com.gendeathrow.pmobs.handlers.random.ArmorSetWeigthedItem;
import com.gendeathrow.pmobs.storage.InventoryStroageModifiable;
import com.gendeathrow.pmobs.world.RaidersWorldDifficulty;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

@Mod.EventBusSubscriber
public class EntityRaiderBase extends EntityMob {
	
	@SubscribeEvent
	public static void onSpawnEvent(LivingSpawnEvent event) {
		if(event.getEntity() instanceof EntityRaiderBase) {
			boolean canspawn = ((EntityRaiderBase)event.getEntity()).canSpawnAtDifficulty();
			
			if(!canspawn)
				event.setResult(Result.DENY);
		}
	}

	protected GameProfile playerProfile;
	
    public boolean profileset = false;

    public static final UUID SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D837");
    public static final UUID DAY_SPEED_MODIFIER_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D838");
    public static final UUID SPEED_OFFSET_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D839");

    protected static final DataParameter<Boolean> ARMS_RAISED = EntityDataManager.<Boolean>createKey(EntityRaiderBase.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<String> SKIN_VARIANT = EntityDataManager.<String>createKey(EntityRaiderBase.class, DataSerializers.STRING);
    protected static final DataParameter<Integer> RAIDER_FACTION = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> OVERLAY_VARIANT = EntityDataManager.<Integer>createKey(EntityRaiderBase.class, DataSerializers.VARINT);
  
	private static final AttributeModifier DAY_SPEED_MODIFIER = new AttributeModifier(DAY_SPEED_MODIFIER_ID, "Day speed reduced", PMSettings.daySpeedRestiction - 1, 2);

	protected DifficultyProgression difficultyManager;
	
    private final InventoryStroageModifiable raidersInventory;
    
	private float raiderWidth = 0.6F;
	private float raiderHeight = 1.95F;
	
	protected final EntityAINearestAttackableTarget<EntityPlayer> attackPlayerAI = new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true);
	protected final EntityAINearestAttackableTarget<EntityVillager> attackVillagerAI =  new EntityAINearestAttackableTarget<EntityVillager>(this, EntityVillager.class, false);
	protected final EntityAINearestAttackableTarget<EntityLiving> attackLivingAI = new EntityAINearestAttackableTarget<EntityLiving>(this, EntityLiving.class, true);
	    
	protected final EntityAIWatchClosest watchClosestAI =  new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F);
	protected final EntityAIWander wanderAI = new EntityAIWander(this, 1.0D);
	protected final EntityAILookIdle lookIdleAI = new EntityAILookIdle(this);
	
    private boolean isLeapAttackTaskSet = false;
    private final EntityAILeapAtTarget leapAttack = new EntityAILeapAtTarget(this, 0.4F);
	    
	
	public EntityRaiderBase(World worldIn){
		super(worldIn);
		this.raidersInventory = new InventoryStroageModifiable("Items", 3);
		this.setSize(0.6F, 1.95F);
		difficultyManager = new DifficultyProgression(this);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(SKIN_VARIANT, "Steve");
        this.getDataManager().register(RAIDER_FACTION, Integer.valueOf(0));
        this.getDataManager().register(OVERLAY_VARIANT,Integer.valueOf(0));
        this.getDataManager().register(ARMS_RAISED, Boolean.valueOf(false));
	}
	
	@Override
	protected void applyEntityAttributes()
	{
	        super.applyEntityAttributes();
	        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
	        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(PMSettings.movementSpeedStat);
	        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(PMSettings.attackDamageStat);
	        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(PMSettings.armorStat);
	        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(PMSettings.maxHealthStat);
	}

    @Override
	protected void initEntityAI() {
    	super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIShootLaser(this));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
	}
    
    
	public void setLeapAttack(boolean enabled)
	{
		if (this.isLeapAttackTaskSet != enabled)
		{
			this.isLeapAttackTaskSet = enabled;

			if (enabled)
			{
				this.tasks.addTask(1, this.leapAttack);
			}
			else
			{
	           this.tasks.removeTask(this.leapAttack);
			}
		}
	}
    /**
     * Will the raider walking speed be reduced in daylight?
     * @return
     */
    public boolean isDaylightSpeedReduced() {
    	return PMSettings.daySpeedRestiction != 1;
    }
    
	@Override
	public void onLivingUpdate() {
		
		IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		if(!this.isChild()&& this.getRaiderFaction() != EnumFaction.FRIENDLY) {
			if(this.world.isDaytime() && isDaylightSpeedReduced()) {
				if(!iattributeinstance.hasModifier(DAY_SPEED_MODIFIER))	{
					iattributeinstance.applyModifier(DAY_SPEED_MODIFIER);
				}

			}
			else{
				if(iattributeinstance.hasModifier(DAY_SPEED_MODIFIER)){
					iattributeinstance.removeModifier(DAY_SPEED_MODIFIER);
				}
			}
		}

		super.onLivingUpdate();
	}
	
    @Override
    protected void updateEquipmentIfNeeded(EntityItem itemEntity)
    {
    	super.updateEquipmentIfNeeded(itemEntity);
    	
        if (!itemEntity.isDead && !this.world.isRemote)
        {
        	for(int i = 0; i < this.getRaidersInventory().getSlots(); i++)
        	{
        		ItemStack returnStack = this.getRaidersInventory().insertItem(i, itemEntity.getItem(), false);
  			
        		if(returnStack.isEmpty()) itemEntity.setDead();
        		else itemEntity.setItem(returnStack);
        	}
        }
    }

	// Raider attacks Target
	public boolean attackEntityAsMob(Entity entityIn)
	{
		boolean hasHitTarget = super.attackEntityAsMob(entityIn);

		if (hasHitTarget){
			int i = this.world.getDifficulty().getDifficultyId();
			if (this.getHeldItemMainhand() == null && this.isBurning() && this.rand.nextFloat() < (float)i * 0.3F)
				entityIn.setFire(2 * i);
		}

		return hasHitTarget;
	}
	
	/**
	 * Called when the raider gets attacked.
	 */
    public boolean attackEntityFrom(DamageSource source, float amount){
		
    	if (super.attackEntityFrom(source, amount)){
			EntityLivingBase entitylivingbase = null;
			
			if (source.getTrueSource() instanceof EntityLivingBase){
				entitylivingbase = (EntityLivingBase)source.getTrueSource();
			}

			int i = MathHelper.floor(this.posX);
			int j = MathHelper.floor(this.posY);
			int k = MathHelper.floor(this.posZ);
			
			if(entitylivingbase != null && this.getRNG().nextInt(2) == 0){
				this.setAttackTarget((EntityLivingBase)entitylivingbase);
			}

			return true;
		}
		else{
			return false;
		}
	}
    
    @Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata){
    		
    		livingdata = super.onInitialSpawn(difficulty, livingdata);
	        
    		
            if (livingdata == null)
            {
                livingdata = new EntityRaiderBase.GroupData(this.world.rand.nextFloat() < net.minecraftforge.common.ForgeModContainer.zombieBabyChance, EnumFaction.getRandomFaction(this, this.difficultyManager));
            }
            
            if (livingdata instanceof EntityRaiderBase.GroupData)
            {
            	EntityRaiderBase.GroupData entityraider$groupdata = (EntityRaiderBase.GroupData)livingdata;

            	this.setRaiderFaction(entityraider$groupdata.faction);
            }
            
	        float f = difficulty.getClampedAdditionalDifficulty();
	        
	        GameProfile gameprofile = getRandomPlayerSkin();
	        
	        ((PathNavigateGround)this.getNavigator()).setEnterDoors(true);

	        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
	        
	        this.setAlwaysRenderNameTag(false);
	  	 
	        this.setOwner(gameprofile.getName());
	        
	        this.setRandomFeatures(); 
	        
	        this.setCustomNameTag(gameprofile.getName());
	        
	        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
	                
	        double d0 = this.rand.nextDouble() * 1.5D * (double)f;
	        if (d0 > 1.0D)
	            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
	        
	        
	        this.setEquipmentBasedOnDifficulty(difficulty);
	        
	        this.setEnchantmentBasedOnDifficulty(difficulty);
	        
	        this.difficultyManager.setupDifficutlyOfRaider(difficulty);
	        
	        setCanPickUpLoot(true);
	        
	        return livingdata;
	}
    
    
    public void dateEasterEggs() {

        Calendar calendar = this.world.getCurrentDate();
        
    	//Halloween
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
        {
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }
        
        //Christmas
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 25) {
        		this.setFeatures(LayerFeatures.SANATASUIT.ordinal());
        }
    }
    
    
    @Override
    public ITextComponent getDisplayName()
    {
    	TextFormatting color = getRaiderFaction() == EnumFaction.FRIENDLY ? TextFormatting.GREEN : getRaiderFaction() == EnumFaction.HOSTILE ? TextFormatting.RED : TextFormatting.GRAY;
    	
        TextComponentString textcomponentstring = new TextComponentString(color + this.getName());
        textcomponentstring.getStyle().setHoverEvent(this.getHoverEvent());
        textcomponentstring.getStyle().setInsertion(this.getCachedUniqueIdString());
        return textcomponentstring;
    }
    
	public DifficultyProgression getDifficultyProgession(){
		return this.difficultyManager;
	}
    
    public GameProfile getPlayerProfile(){
		return RaiderManager.getRaiderProfile(this.getOwner());
    }
   
    // Handle Skins
    public ResourceLocation getLocationSkin(){
		return RaidersSkinManager.DownloadPlayersSkin(this);
    }
    
    /** 
     * Set raiders skin owner
     * @param name
     */
    public void setOwner(String name){
        this.dataManager.set(SKIN_VARIANT, name);
    }
    
    /**
     * Get raiders skin owner
     * @return
     */
	public String getOwner(){
    	return this.dataManager.get(SKIN_VARIANT);
    }
	
    public void setRandomFeatures()
    {
    	setFeatures(LayerFeatures.randomFeature(this.rand).ordinal()); 
    }
    
    public void setFeatures(int ordinal)
    {
    	this.dataManager.set(EntityRaiderBase.OVERLAY_VARIANT, ordinal);
    }
    
    public LayerFeatures getFeatures()
    {
		return LayerFeatures.values()[this.dataManager.get(OVERLAY_VARIANT).intValue()]; 
    }
    
	
    public void setRaiderFaction(EnumFaction faction){
    	this.getDataManager().set(RAIDER_FACTION, faction.ordinal());
    }
    
    public EnumFaction getRaiderFaction()
    {
    	return EnumFaction.get(((Integer)this.getDataManager().get(RAIDER_FACTION)).intValue());
    }
    
    public InventoryStroageModifiable getRaidersInventory(){
        return this.raidersInventory;
    }
    
    public void setArmsRaised(boolean armsRaised) {
        this.getDataManager().set(ARMS_RAISED, Boolean.valueOf(armsRaised));
    }

    public boolean isArmsRaised() {
        return ((Boolean)this.getDataManager().get(ARMS_RAISED)).booleanValue();
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Owner"))
            this.setOwner(compound.getString("Owner"));
        
        if(compound.hasKey("OverlayType"))
        	this.setFeatures(compound.getInteger("OverlayType"));
        
        this.raidersInventory.readFromNBT(compound);
        
        if(compound.hasKey("faction"))
        	this.setRaiderFaction(EnumFaction.get(compound.getInteger("faction")));
        
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound){
    	
        super.writeEntityToNBT(compound);
        
        compound.setString("Owner", this.getOwner());

        compound.setInteger("OverlayType", this.getFeatures().ordinal());
        
        this.raidersInventory.writeToNBT(compound);

        compound.setInteger("faction", this.getRaiderFaction().ordinal());
    }
    
    @Override
    public boolean isOnSameTeam(Entity entityIn)
    {
    	if(entityIn instanceof EntityPlayer && this.getRaiderFaction() == EnumFaction.FRIENDLY)
    	{
    		return true;
    	}
    	else if(entityIn instanceof EntityRaiderBase)
    	{
    		return this.getRaiderFaction() == ((EntityRaiderBase) entityIn).getRaiderFaction();
    	}
    	else return this.isOnScoreboardTeam(entityIn.getTeam());
    }
    
    @Override
    public boolean canAttackClass(Class <? extends EntityLivingBase >  entity) 
    {
    	if(EntityPlayer.class.isAssignableFrom(entity))
    		return true;
    	else if(PMSettings.veryHostile && EntityMob.class.isAssignableFrom(entity))
    		return true;
    	else if(EntityRaiderBase.class.isAssignableFrom(entity))
    		return true;
    	else if (EntityLiving.class.isAssignableFrom(entity))
    		return true;
    	
    	return false;
    }
    
    @Override
    public void onDeath(DamageSource cause) {
    	super.onDeath(cause);
    	
    	for(int i = 0; i < this.raidersInventory.getSlots(); i++) {
    		
    		ItemStack stack = this.raidersInventory.getStackInSlot(i);
    		
    		if(stack != null) {
    			EntityItem entity = new EntityItem(world, this.posX, this.posY, this.posZ, stack);
    			this.world.spawnEntity(entity);
    		}
    	}
    }
    
    @Override
    protected int getExperiencePoints(EntityPlayer player) {

        if(RaidersWorldDifficulty.INSTANCE.getCurrentRaidDifficulty(this.world) > 0) {
        	this.experienceValue = this.experienceValue + this.getRNG().nextInt(RaidersWorldDifficulty.INSTANCE.getCurrentRaidDifficulty(this.world));
        }

        if (this.isChild()) {
            this.experienceValue = (int)((float)this.experienceValue * 0.5F);
        }
        
        return super.getExperiencePoints(player);
    }

    /**
     * If a Raider should wear Armor
     * @return
     */
    protected boolean wearsArmor() {
    	return true;
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
    	if(!wearsArmor()) return;
    	
    	if(!PMSettings.isDifficultyProgressionEnabled)
    		super.setEquipmentBasedOnDifficulty(difficulty);

    	if (this.rand.nextFloat() < (0.25F * difficulty.getClampedAdditionalDifficulty()) + this.difficultyManager.calculateProgressionDifficulty(.05))
        {
	    	//System.out.println((0.25F * difficulty.getClampedAdditionalDifficulty()) + this.difficultyManager.calculateProgressionDifficulty(.05));
	    	
           
            float f = this.world.getDifficulty() == EnumDifficulty.HARD ? PMSettings.setEquptmentHard : PMSettings.setEquitmentDefault;

            boolean armorflag = true;
            boolean handflag = true;

            ArmorSetWeigthedItem ArmorSet = EquipmentManager.getRandomArmor();
            //System.out.println("isEmpty: "+ (ArmorSet == null));
            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
            {
            	//System.out.print(entityequipmentslot.getName());
                if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR)
                {
                    ItemStack armorSlot = this.getItemStackFromSlot(entityequipmentslot);
    
                    if (!armorflag && this.rand.nextFloat() > f + this.difficultyManager.calculateProgressionDifficulty(.05))
                    {
                        break;
                    }

                    armorflag = false;

                    if (armorSlot.isEmpty() & ArmorSet != null)
                    {
                   		ItemStack stack = ArmorSet.getArmorbyEquipmentSlot(entityequipmentslot);
                   		
                        if (!stack.isEmpty() && stack.getItem() != null)
                        {
                        	this.setItemStackToSlot(entityequipmentslot, stack.copy());
                        }
                    }
                }
                else if(entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.HAND)
                {
                    ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

                    if (!handflag && this.rand.nextFloat() > f + this.difficultyManager.calculateProgressionDifficulty(.05))
                    {
                        break;
                    }

                    handflag = false;

                    if (itemstack == null)
                    {
                    	ItemStack stack = null;
                    	
                    	if(entityequipmentslot == EntityEquipmentSlot.MAINHAND)
                    	{
                    		stack = EquipmentManager.getRandomWeapons().getCopy();
                    	}
                    	else if(entityequipmentslot == EntityEquipmentSlot.OFFHAND)
                    	{
                    		stack = EquipmentManager.getRandomOffHand().getCopy();
                    	}
                        if (stack != null && stack.getItem() != null)
                        {
                            this.setItemStackToSlot(entityequipmentslot, stack);
                        }
                    }
                }
                
            }
        } 
    }
    
    @Override
    protected float getWaterSlowDown() {
        return .9F;
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 4;
    }
    
    /**
     * Determines if a raider can spawn at this difficulty or not. 
     * 
     * @return
     */
    public boolean canSpawnAtDifficulty() {
    	return true;
    };
    
    @Override
    public boolean getCanSpawnHere() {
    	if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) 
    		return false;
    	
    	return this.isValidLightLevel();
    }
    
    @Override
    protected boolean isValidLightLevel() {
     	if(PMSettings.shouldDaylightSpawm) {
     		BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
     		if (this.world.getLightFor(EnumSkyBlock.BLOCK, blockpos) >= 8) {
     			return false;
     		}
     		return true;
    	}
     	else
     		return super.isValidLightLevel();
    }
       
    //TODO
    @Override
	public void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
	{

		if(source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayer)
		{
			double dropit = this.rand.nextDouble();
			
			if( dropit < (.025)) //lootingModifier*0.025 + 
			{
				ItemStack stack = new ItemStack(Items.SKULL, 1, 3);
				
				if(stack.getTagCompound() == null) stack.setTagCompound(new NBTTagCompound());

				stack.getTagCompound().setString("SkullOwner", this.getOwner());

				EntityItem skull = new EntityItem(world, this.posX, this.posY, this.posZ, stack);
				
				this.world.spawnEntity(skull);
			}

		}
		super.dropLoot(wasRecentlyHit, lootingModifier, source);
	}
    
	@Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }
	
	//TODO
    protected boolean canDespawn() {
    	if(this.getRidingEntity() instanceof EntityDropPod) return false;
        return super.canDespawn();
    }  
    
    @Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return RaidersSoundEvents.RAIDERS_HURT;
	}

    @Override
	protected SoundEvent getDeathSound(){
		return SoundEvents.ENTITY_PLAYER_DEATH;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn){
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute(){
		return EnumCreatureAttribute.UNDEFINED;
	}
	
	/**
	 *  This gets a random GameProfile from registered raider player skins. 
	 */
	protected static GameProfile getRandomPlayerSkin(){
		GameProfile profile;
			 profile = RaiderManager.getRandomRaider().getProfile();
		return profile;
	}

	
    protected final void multiplySize(float size)
    {
        super.setSize(this.raiderWidth * size, this.raiderHeight * size);
    }
    
    protected final void multiplySize(float sizeWidth, float sizeHeight)
    {
        super.setSize(this.raiderWidth * sizeWidth, this.raiderHeight * sizeHeight);
    }
    
    /**
     * Remove all Attributes within this entity. Used to get around a client side only call.
     * 
     * @param attribute
     */
    public void removeAllModifiers(IAttribute attribute)
    {
    	for(AttributeModifier modifier : this.getEntityAttribute(attribute).getModifiers())
    		this.getEntityAttribute(attribute).removeModifier(modifier);
    }
    
    
    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
    	if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
    		return true;
    	else
    		return super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return (T) this.raidersInventory;
        }
        return super.getCapability(capability, facing);    
    }
    
    
    class GroupData implements IEntityLivingData
    {
        public boolean isChild;
        public EnumFaction faction;

        private GroupData(boolean p_i47328_2_, EnumFaction factionIn)
        {
            this.isChild = p_i47328_2_;
            this.faction = factionIn;
        }
    }
}
