package com.gendeathrow.pmobs.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.util.ResourceLocation;

import com.gendeathrow.pmobs.core.RaidersCore;

public enum LayerFeatures
{
    NONE(0, null),
    DIRT(1, new ResourceLocation(RaidersCore.MODID ,"textures/entity/dirtySkin.png")),
    BLOOD(2, new ResourceLocation(RaidersCore.MODID ,"textures/entity/bloodySkin.png")),
    BLOODHANDS(3, new ResourceLocation(RaidersCore.MODID ,"textures/entity/bloodyHands.png")),
    HORROR(4, new ResourceLocation(RaidersCore.MODID ,"textures/entity/horror.png")),
    STAB(5, new ResourceLocation(RaidersCore.MODID ,"textures/entity/bleeding.png")),
    TUX(6, new ResourceLocation(RaidersCore.MODID ,"textures/entity/tux.png")),
    BUTCHER(7, new ResourceLocation(RaidersCore.MODID ,"textures/entity/butcher.png")),
    MARTIAL(8, new ResourceLocation(RaidersCore.MODID ,"textures/entity/russianSoldier.png")),
    RUSSIAN(9, new ResourceLocation(RaidersCore.MODID ,"textures/entity/martialArtist.png")),
    ZOMBIE1(7, new ResourceLocation(RaidersCore.MODID ,"textures/entity/detail1.png")),
    ZOMBIE2(8, new ResourceLocation(RaidersCore.MODID ,"textures/entity/detail2.png")),
    ZOMBIE3(9, new ResourceLocation(RaidersCore.MODID ,"textures/entity/detail3.png")),
    POLICE(10, new ResourceLocation(RaidersCore.MODID ,"textures/entity/police.png")),
    MILITARY1(11, new ResourceLocation(RaidersCore.MODID ,"textures/entity/military1.png")),
    MILITARY2(12, new ResourceLocation(RaidersCore.MODID ,"textures/entity/military2.png")),
    WWII(13, new ResourceLocation(RaidersCore.MODID ,"textures/entity/wwii.png")),
    DOCTOR(14, new ResourceLocation(RaidersCore.MODID ,"textures/entity/doctor.png")),
    NAVYCAPTAIN(15, new ResourceLocation(RaidersCore.MODID ,"textures/entity/navycaptian.png"));
    
   
    
    private static final List<LayerFeatures> VALUES =  Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    
    public ResourceLocation resource;
    public int id;
    
    LayerFeatures(int id, ResourceLocation loc)
    {
    	this.id = id;
    	this.resource = loc;
    }
    
    public static LayerFeatures randomFeature(Random rand)  
    {
        return VALUES.get(rand.nextInt(SIZE));
    }
}
