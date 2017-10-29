package com.gendeathrow.pmobs.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.pmobs.core.RaidersMain;

import net.minecraft.util.ResourceLocation;

public enum LayerFeatures
{
    NONE(0, null),
    DIRT(1, new ResourceLocation(RaidersMain.MODID ,"textures/entity/dirtySkin.png")),
    BLOOD(2, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bloodySkin.png")),
    BLOODHANDS(3, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bloodyHands.png")),
    HORROR(4, new ResourceLocation(RaidersMain.MODID ,"textures/entity/horror.png")),
    STAB(5, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bleeding.png")),
    TUX(6, new ResourceLocation(RaidersMain.MODID ,"textures/entity/tux.png")),
    BUTCHER(7, new ResourceLocation(RaidersMain.MODID ,"textures/entity/butcher.png")),
    MARTIAL(8, new ResourceLocation(RaidersMain.MODID ,"textures/entity/russianSoldier.png")),
    RUSSIAN(9, new ResourceLocation(RaidersMain.MODID ,"textures/entity/martialArtist.png")),
    ZOMBIE1(7, new ResourceLocation(RaidersMain.MODID ,"textures/entity/detail1.png")),
    ZOMBIE2(8, new ResourceLocation(RaidersMain.MODID ,"textures/entity/detail2.png")),
    ZOMBIE3(9, new ResourceLocation(RaidersMain.MODID ,"textures/entity/detail3.png")),
    POLICE(10, new ResourceLocation(RaidersMain.MODID ,"textures/entity/police.png")),
    MILITARY1(11, new ResourceLocation(RaidersMain.MODID ,"textures/entity/military1.png")),
    MILITARY2(12, new ResourceLocation(RaidersMain.MODID ,"textures/entity/military2.png")),
    WWII(13, new ResourceLocation(RaidersMain.MODID ,"textures/entity/wwii.png")),
    DOCTOR(14, new ResourceLocation(RaidersMain.MODID ,"textures/entity/doctor.png")),
    NAVYCAPTAIN(15, new ResourceLocation(RaidersMain.MODID ,"textures/entity/navycaptian.png")),
    MUSCESMAN(16, new ResourceLocation(RaidersMain.MODID ,"textures/entity/muscleman.png")),
    SURVIVOR1(17, new ResourceLocation(RaidersMain.MODID ,"textures/entity/survivor1.png")),
    SURVIVOR2(18, new ResourceLocation(RaidersMain.MODID ,"textures/entity/survivor2.png")),
    SURVIVOR3(19, new ResourceLocation(RaidersMain.MODID ,"textures/entity/survivor3.png")),
    PRISONER1(20, new ResourceLocation(RaidersMain.MODID ,"textures/entity/prisoner.png")),
    PRISONER2(21, new ResourceLocation(RaidersMain.MODID ,"textures/entity/prisoner2.png")),
    BLACKHOODROBE(22, new ResourceLocation(RaidersMain.MODID ,"textures/entity/black_hooded_robe.png")),
    BEARDEDWARRIOR(23, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bearded_warrior.png")),
    ANONYMOUS(24, new ResourceLocation(RaidersMain.MODID ,"textures/entity/anonymous.png")),
    WOLFHAT(25, new ResourceLocation(RaidersMain.MODID ,"textures/entity/wolfhat.png")),
    EXPLORER(26, new ResourceLocation(RaidersMain.MODID ,"textures/entity/explorer.png")),
    SCIENTIST1(27, new ResourceLocation(RaidersMain.MODID ,"textures/entity/scientist1.png")),
    SCIENTIST2(28, new ResourceLocation(RaidersMain.MODID ,"textures/entity/scientist2.png")),
    HARNESS(29, new ResourceLocation(RaidersMain.MODID ,"textures/entity/harness.png")),
    WARRIORARMOR(30, new ResourceLocation(RaidersMain.MODID ,"textures/entity/warriorarmor.png")),
    SPACESUIT(31, new ResourceLocation(RaidersMain.MODID ,"textures/entity/spacesuit.png")),
    REDROBE(32, new ResourceLocation(RaidersMain.MODID ,"textures/entity/redrobe.png")),
    WORKER(33, new ResourceLocation(RaidersMain.MODID ,"textures/entity/worker.png")),
    ARCHER1(34, new ResourceLocation(RaidersMain.MODID ,"textures/entity/archer.png")),
    ARCHER2(35, new ResourceLocation(RaidersMain.MODID ,"textures/entity/archer2.png")),
    ARCHER3(36, new ResourceLocation(RaidersMain.MODID ,"textures/entity/archer3.png")),
    BLUEROBES(37, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bluerobes.png")),
    PESANTS(38, new ResourceLocation(RaidersMain.MODID ,"textures/entity/peasant.png")),
    BODYARMOR(39, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bodyarmor.png")),
    SUPERMAN(40, new ResourceLocation(RaidersMain.MODID ,"textures/entity/superman.png")),
    CRUSADER1(41, new ResourceLocation(RaidersMain.MODID ,"textures/entity/crusader.png")),
    CRUSADER2(42, new ResourceLocation(RaidersMain.MODID ,"textures/entity/crusader2.png")),
    SANATASUIT(43, new ResourceLocation(RaidersMain.MODID ,"textures/entity/santasuit.png")),
    FREDDY(44, new ResourceLocation(RaidersMain.MODID ,"textures/entity/freddy.png")),
    MAYTAG(45, new ResourceLocation(RaidersMain.MODID ,"textures/entity/maytag.png")),
    BOWTIE(46, new ResourceLocation(RaidersMain.MODID ,"textures/entity/bowtie.png")),
    GREENSUIT(47, new ResourceLocation(RaidersMain.MODID ,"textures/entity/greensuit.png")),
    REDSUIT(48, new ResourceLocation(RaidersMain.MODID ,"textures/entity/redsuit.png")),
    UTILITYBELT(49, new ResourceLocation(RaidersMain.MODID ,"textures/entity/utilitybelt.png")),
    DARKSKIN(50, new ResourceLocation(RaidersMain.MODID ,"textures/entity/darkskin.png"));

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
    
    @Nullable
    public static LayerFeatures getFeature(int ordinal)
    {
    	for(LayerFeatures feature : VALUES)
    	{
    		if(feature.ordinal() == ordinal) return feature;
    	}
		return null;
    }
}
