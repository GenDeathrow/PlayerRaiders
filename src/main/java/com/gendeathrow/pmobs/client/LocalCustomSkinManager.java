package com.gendeathrow.pmobs.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.gendeathrow.pmobs.core.configs.MainConfig;
import com.gendeathrow.pmobs.handlers.RaiderData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class LocalCustomSkinManager {

    private final TextureManager textureManager;
	public static File assestsDir = new File(MainConfig.configDir, "assets");
       
    public LocalCustomSkinManager(TextureManager textureManagerInstance) {
        this.textureManager=textureManagerInstance;
    }
    
    
    public static void setupDirectory() {
    	
    	File dir= new File(assestsDir, "skins");
    	File dir2= new File(assestsDir, "overlays");
    	
    	if(!dir.isDirectory())
    		dir.mkdirs();
    }
    
    public static ResourceLocation loadSkin(RaiderData raiderProfile){

        final ResourceLocation resourcelocation = raiderProfile.getCustomSkin();
        
        ITextureObject itextureobject = Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation);

		if(itextureobject != null) return resourcelocation;
		
		if(!resourcelocation.getResourceDomain().equalsIgnoreCase(RaidersMain.MODID)) {
			//RaidersMain.logger.error("Could not file Skin Resource!!! {"+ resourcelocation.toString()+"}");
			return DefaultPlayerSkin.getDefaultSkinLegacy();
		}
		
        SimpleTexture itexture = new SimpleTexture(resourcelocation) {
        	   
        	@Override
        	public void loadTexture(IResourceManager resourceManager) throws IOException
        	    {
        	        this.deleteGlTexture();

        	        try {
        	    			InputStream inputstream = new FileInputStream(parseFileLoc(resourcelocation));
           	    			BufferedImage bufferedimage = TextureUtil.readBufferedImage(inputstream);
        	    			boolean flag = false;
        	    			boolean flag1 = false;
        	    			TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
        	    			
        	        } catch (FileNotFoundException e) {
        	    			e.printStackTrace();
        	        }

        	    }
        };
        
        Minecraft.getMinecraft().getTextureManager().loadTexture(resourcelocation, itexture);
        
        return resourcelocation;
    }
    
	private static File parseFileLoc(ResourceLocation resourceIn) {
		
		//System.out.println("is File"+ new File (assestsDir, resourceIn.getResourcePath()+".png").isFile());
		return new File (assestsDir, resourceIn.getResourcePath()+".png");
	}

}
