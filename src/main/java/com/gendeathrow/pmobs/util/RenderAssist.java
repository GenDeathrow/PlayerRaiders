package com.gendeathrow.pmobs.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * 
 * Some methods which are usually in GuiIngame, but since we don't have<br>
 * direct access when rendering in HudItem, you may need to use these.
 * 
 * @author maxpowa
 * 
 */
@SideOnly(Side.CLIENT)
public class RenderAssist {

    /**
     * Controls render "level" for layering textures overtop one another.
     */
    public static float zLevel;
    
	private static TextureManager manager = Minecraft.getMinecraft().getTextureManager();
	private static FontRenderer fontObj = Minecraft.getMinecraft().fontRendererObj;

	public static String mcColorUni = "\u00A74";

    /** 
     *  Draws an unfilled Circle
     *  
     * @param posX
     * @param posY
     * @param radius
     * @param num_segments
     * @param color
     */
	 public static void drawUnfilledCircle(float posX, float posY, float radius, int num_segments, int color) 
	 {
	        float f = (color >> 24 & 255) / 255.0F;
	        float f1 = (color >> 16 & 255) / 255.0F;
	        float f2 = (color >> 8 & 255) / 255.0F;
	        float f3 = (color & 255) / 255.0F;
	        Tessellator tessellator = Tessellator.getInstance();
	        VertexBuffer renderer = tessellator.getBuffer();

	        GlStateManager.enableBlend();
	        GlStateManager.disableTexture2D();
	        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	        GlStateManager.color(f1, f2, f3, f);

	        renderer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_TEX_COLOR);
	        
	        for (int i = 0; i < num_segments; i++) 
	        {
	            double theta = 2.0f * Math.PI * i / num_segments;// get the current  angle
	            double x = radius * Math.cos(theta);// calculate the x component
	            double y = radius * Math.sin(theta);// calculate the y component

	            renderer.pos(x + posX, y + posY, 0.0D).endVertex();
	        }
	        
	        tessellator.draw();
	        GlStateManager.enableTexture2D();
	        GlStateManager.disableBlend();
	    }

    /**
     *  Drawys a filled Circle
     * @param posX
     * @param posY
     * @param radius
     * @param num_segments
     * @param color
     */
    public static void drawCircle(float posX, float posY, float radius, int num_segments, int color) {
        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        // Tessellator tessellator = Tessellator.instance;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(f1, f2, f3, f);
      
        GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
        
        GL11.glVertex2f(posX, posY); // center of circle
        for (int i = num_segments; i >= 0; i--) 
        {
            double theta = i * (Math.PI*2) / num_segments;
            GL11.glVertex2d(posX + radius * Math.cos(theta), posY + radius * Math.sin(theta));
        }
        GlStateManager.glEnd();

        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    /**
     * Draws a color rectangle outline with the specified coordinates and color.<br>
     * Color must have all four hex elements (0xFFFFFFFF)
     * 
     * @param x1
     *            First X value
     * @param g
     *            First Y value
     * @param x2
     *            Second X value
     * @param y2
     *            Second Y Value
     */
    public static void drawUnfilledRect(int left, int top, int right, int bottom, int color)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(f1, f2, f3, f);
        
        vertexbuffer.begin(2, DefaultVertexFormats.POSITION);
        vertexbuffer.pos((double)left, (double)bottom, 0.0D).endVertex();
        vertexbuffer.pos((double)right, (double)bottom, 0.0D).endVertex();
        vertexbuffer.pos((double)right, (double)top, 0.0D).endVertex();
        vertexbuffer.pos((double)left, (double)top, 0.0D).endVertex();
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawHorizontalLine(int par1, int par2, int par3, int par4) {
        if (par2 < par1) {
            int i1 = par1;
            par1 = par2;
            par2 = i1;
        }

        drawRect(par1, par3, par2 + 1, par3 + 1, par4);
    }

    public static void drawVerticalLine(int par1, int par2, int par3, int par4) {
        if (par3 < par2) {
            int i1 = par2;
            par2 = par3;
            par3 = i1;
        }

        drawRect(par1, par2 + 1, par1 + 1, par3, par4);
    }

    /**
     * Draws a textured rectangle at the stored z-value.
     * 
     * @param x
     *            X-Axis position to render the sprite into the GUI.
     * @param y
     *            Y-Axis position to render the sprite into the GUI.
     * @param u
     *            X-Axis position on the spritesheet which this sprite is found.
     * @param v
     *            Y-Axis position on the spritesheet which this sprite is found.
     * @param width
     *            Width to render the sprite.
     * @param height
     *            Height to render the sprite.
     */

    public static void drawTexturedModalRect(float x, float y, float width, float height) 
    {
    	drawTexturedModalRect(x, y, width, height,  255);
    }
    
    public static void drawTexturedModalRect(float x, float y, float width, float height, int alpha) 
    {

		float f = (float)alpha / 255.0F;
		
	    Tessellator tessellator = Tessellator.getInstance();
	    VertexBuffer renderer = tessellator.getBuffer();
	    renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		renderer.pos(x + 0, y + height, (double)RenderAssist.zLevel).tex(0, 1).endVertex();
		renderer.pos(x + width, y + height, RenderAssist.zLevel).tex(1, 1).endVertex();;
		renderer.pos(x + width, y + 0, RenderAssist.zLevel).tex(1, 0).endVertex();;
		renderer.pos(x + 0, y + 0, RenderAssist.zLevel).tex(0, 0).endVertex();;
        tessellator.draw();

    }
    
    public static void drawTexturedModalRect(float x, float y, float u, float v, float width, float height) 
    {
        float f = 0.00390625F;
	    Tessellator tessellator = Tessellator.getInstance();
	    VertexBuffer renderer = tessellator.getBuffer();
	    renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	    renderer.pos(x + 0, y + height, RenderAssist.zLevel).tex((u + 0) * f, (v + height) * f).endVertex();
	    renderer.pos(x + width, y + height, RenderAssist.zLevel).tex((u + width) * f, (v + height) * f).endVertex();
	    renderer.pos(x + width, y + 0, RenderAssist.zLevel).tex((u + width) * f, (v + 0) * f).endVertex();
	    renderer.pos(x + 0, y + 0, RenderAssist.zLevel).tex((u + 0) * f, (v + 0) * f).endVertex();
        tessellator.draw();
    }
    
    public static void drawTexturedModalRectCustomSize(float x, float y, float uMin, float vMin,float uMax, float vMax, float width, float height) 
    {
        float f = 0.00390625F;
	    Tessellator tessellator = Tessellator.getInstance();
	    VertexBuffer renderer = tessellator.getBuffer();
	    renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	    renderer.pos(x + 0, y + height, RenderAssist.zLevel).tex((uMin + 0) * f, (vMin + vMax) * f).endVertex();
	    renderer.pos(x + width, y + height, RenderAssist.zLevel).tex((uMin + uMax) * f, (vMin + vMax) * f).endVertex();
	    renderer.pos(x + width, y + 0, RenderAssist.zLevel).tex((uMin + uMax) * f, (vMin + 0) * f).endVertex();
	    renderer.pos(x + 0, y + 0, RenderAssist.zLevel).tex((uMin + 0) * f, (vMin + 0) * f).endVertex();
        tessellator.draw();
    }
    
	public static void drawTexturedFrame(int x, int y,  float uMin, float vMin,float uMax, float vMax, float width, float height)
	{
        float f = 0.00390625F;
        
	    Tessellator tessellator = Tessellator.getInstance();
	    VertexBuffer renderer = tessellator.getBuffer();
	    
	    renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
	    renderer.pos(x + 0, y + height, RenderAssist.zLevel).tex((uMin + 0) * f, (vMin + vMax) * f).endVertex();
	    renderer.pos(x + width, y + height, RenderAssist.zLevel).tex((uMin + uMax) * f, (vMin + vMax) * f).endVertex();
	    renderer.pos(x + width, y + 0, RenderAssist.zLevel).tex((uMin + uMax) * f, (vMin + 0) * f).endVertex();
	    renderer.pos(x + 0, y + 0, RenderAssist.zLevel).tex((uMin + 0) * f, (vMin + 0) * f).endVertex();
        tessellator.draw();
        
        // topleft corner
        
        // top bar
        
        // topright corner
        
        // left side bar
        
        // rightside bar
        
        // bottomleft corner
        
        // bottom bar
        
        // bottomright corner
        
		
		
	}
    
    public static void bindTexture(ResourceLocation res) 
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
    }

    /**
     * Binds a texture, similar to the way renderEngine.bindTexture(String str)
     * used to work.
     * 
     * @param textureLocation
     *            Path to location, you should know how to format this.
     */
    public static void bindTexture(String textureLocation) 
    {
        ResourceLocation res = new ResourceLocation(textureLocation);
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
    }

    /**
     * Draws a solid color rectangle with the specified coordinates and color.
     * 
     * @param g
     *            First X value
     * @param h
     *            First Y value
     * @param i
     *            Second X value
     * @param j
     *            Second Y Value
     */
    public static void drawRect(float g, float h, float i, float j, int color) 
    {
        float j1;

        if (g < i) {
            j1 = g;
            g = i;
            i = j1;
        }

        if (h < j) {
            j1 = h;
            h = j;
            j = j1;
        }

        float f = (color >> 24 & 255) / 255.0F;
        float f1 = (color >> 16 & 255) / 255.0F;
        float f2 = (color >> 8 & 255) / 255.0F;
        float f3 = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
//        GL11.glEnable(GL11.GL_BLEND);
 //       GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glColor4f(f1, f2, f3, f);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(g, j, 0.0D).endVertex();
        worldrenderer.pos(i, j, 0.0D).endVertex();
        worldrenderer.pos(i, h, 0.0D).endVertex();
        worldrenderer.pos(g, h, 0.0D).endVertex();
        tessellator.draw();
///        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_BLEND);
    }

//    /**
//     * Renders the specified item of the inventory slot at the specified
//     * location.
//     */
//    public static void renderInventorySlot(int slot, int x, int y, float partialTick, Minecraft mc) {
//        RenderItem itemRenderer = new RenderItem(manager, null);
//        ItemStack itemstack = mc.thePlayer.inventory.mainInventory[slot];
//        x += 91;
//        y += 12;
//
//        if (itemstack != null) {
//            float f1 = itemstack.animationsToGo - partialTick;
//
//            if (f1 > 0.0F) {
//                GL11.glPushMatrix();
//                float f2 = 1.0F + f1 / 5.0F;
//                GL11.glTranslatef(x + 8, y + 12, 0.0F);
//                GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
//                GL11.glTranslatef(-(x + 8), -(y + 12), 0.0F);
//            }
//
//            itemRenderer.renderItemAndEffectIntoGUI(itemstack, x, y);
//
//            if (f1 > 0.0F) {
//                GL11.glPopMatrix();
//            }
//
//            itemRenderer.renderItemOverlayIntoGUI(mc.fontRendererObj, itemstack, x, y, "something");
//        }
//    }

	public static int getColorFromRGBA_F(float par1, float par2, float par3, float par4)
	{
		int R = (int)(par1 * 255.0F);
		int G = (int)(par2 * 255.0F);
		int B = (int)(par3 * 255.0F);
		int A = (int)(par4 * 255.0F);
		
		return getColorFromRGBA(R, G, B, A);
	}
	
	/**
	 * 
	 * @param R
	 * @param G
	 * @param B
	 * @param A
	 * @return
	 */
	public static int getColorFromRGBA(int R, int G, int B, int A)
	{
		if(R > 255)
		{
			R = 255;
		}
		
		if(G > 255)
		{
			G = 255;
		}
		
		if(B > 255)
		{
			B = 255;
		}
		
		if(A > 255)
		{
			A = 255;
		}
		
		if(R < 0)
		{
			R = 0;
		}
		
		if(G < 0)
		{
			G = 0;
		}
		
		if(B < 0)
		{
			B = 0;
		}
		
		if(A < 0)
		{
			A = 0;
		}
		
		if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
		{
			return A << 24 | R << 16 | G << 8 | B;
		} else
		{
			return B << 24 | G << 16 | R << 8 | A;
		}
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @param ratio
	 * @return
	 */
	public static Color blendColors(int a, int b, float ratio)
	{
		if(ratio > 1f)
		{
			ratio = 1f;
		} else if(ratio < 0f)
		{
			ratio = 0f;
		}
		float iRatio = 1.0f - ratio;
		
		int aA = (a >> 24 & 0xff);
		int aR = ((a & 0xff0000) >> 16);
		int aG = ((a & 0xff00) >> 8);
		int aB = (a & 0xff);
		
		int bA = (b >> 24 & 0xff);
		int bR = ((b & 0xff0000) >> 16);
		int bG = ((b & 0xff00) >> 8);
		int bB = (b & 0xff);
		
		int A = (int)((aA * iRatio) + (bA * ratio));
		int R = (int)((aR * iRatio) + (bR * ratio));
		int G = (int)((aG * iRatio) + (bG * ratio));
		int B = (int)((aB * iRatio) + (bB * ratio));
		
		return new Color(R, G, B, A);
		//return A << 24 | R << 16 | G << 8 | B;
	}
	
/**
 * 
 * @param width
 * @param height
 * @param color
 */
	@SideOnly(Side.CLIENT)
	public static void drawScreenOverlay(int par1, int par2, int par5)
	{
		float f = (float)(par5 >> 24 & 255) / 255.0F;
		float f1 = (float)(par5 >> 16 & 255) / 255.0F;
		float f2 = (float)(par5 >> 8 & 255) / 255.0F;
		float f3 = (float)(par5 & 255) / 255.0F;
				
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
        Tessellator tessellator = Tessellator.getInstance();
        
       
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(0.0D, (double)par2, -90.0D).tex(0.0D, 1.0D).endVertex();;
        worldrenderer.pos((double)par1, (double)par2, -90.0D).tex(1.0D, 1.0D).endVertex();;
        worldrenderer.pos((double)par1, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	/**
	 * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height, scale(Size) 
	 */
	public static void scaledTexturedModalRect(int x, int y, int u, int v, int width, int height, int scale)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos((double)(x + 0), (double)(y + (height * scale)), 0).tex((double)((float)(u + 0) * f), (double)((float)(v + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + (width * scale)), (double)(y + (height * scale)), 0).tex((double)((float)(u + width) * f), (double)((float)(v + height) * f1)).endVertex();
        worldrenderer.pos((double)(x + (width * scale)), (double)(y + 0), 0).tex((double)((float)(u + width) * f), (double)((float)(v + 0) * f1)).endVertex();
        worldrenderer.pos((double)(x + 0), (double)(y + 0), 0).tex((double)((float)(u + 0) * f), (double)((float)(v + 0) * f1)).endVertex();
		tessellator.draw();
	}
	
	public static void renderItem(ItemStack stack, int x, int y, float scale)
	{
		GlStateManager.pushMatrix();
		
			RenderHelper.enableGUIStandardItemLighting();
//			GlStateManager.disableLighting();
//			GlStateManager.enableRescaleNormal();
//			GlStateManager.enableColorMaterial();
//			GlStateManager.enableLighting();
				
				GlStateManager.translate(-x, -y, 0);
				GlStateManager.scale(scale, scale, 0);
        			Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        		GlStateManager.translate(x, y, 0);
        		
//            	GlStateManager.disableLighting();
//            	GlStateManager.depthMask(true);
//            	GlStateManager.enableDepth();

        GlStateManager.popMatrix();
	}
	

	
	
	public static ResourceLocation ExternalResouceLocation(String src) throws IOException
	{
		ResourceLocation resource;
		
		File file = new File(src);
		
        BufferedImage skinBR = ImageIO.read(file);
        
        //System.out.println("image height"+ skinBR.getHeight());
        
		DynamicTexture texture = new DynamicTexture(skinBR);
        
		resource = manager.getDynamicTextureLocation(src, texture);
	
		return resource;
	}

	
	public enum Alignment
	{
		TOP_LEFT("top_left"),
		TOP_CENTER("top_center"),
		TOP_RIGHT("top_right"),
		CENTER_LEFT("center_left"),
		CENTER_CENTER("center_center"),
		CENTER_RIGHT("center_right"),
		BOTTOM_LEFT("bottom_left"),
		BOTTOM_CENTER("bottom_center"),
		BOTTOM_RIGHT("bottom_right");
		
		String alignment;
		public int x;
		public int y;
		
		Alignment(String alignment)
		{
			this.alignment=alignment;
		}
		
		public static Alignment getScreenAlignment(Minecraft mc, Alignment alignment, int objWidth, int objHeight)
		{
			switch(alignment)
			{
				case TOP_LEFT:
					alignment.x = 0;
					alignment.y = 0;
					break;
				case TOP_CENTER:
					alignment.x = (mc.currentScreen.width / 2) - (objWidth / 2);
					alignment.y = 0;
					break;
				case TOP_RIGHT:
					alignment.x = (mc.currentScreen.width ) - (objWidth);
					alignment.y = 0;
					break;
				case CENTER_LEFT:
					alignment.x = 0;
					alignment.y = (mc.currentScreen.height / 2) - (objHeight / 2);
					break;
				case CENTER_CENTER:
					alignment.x = (mc.currentScreen.width / 2) - (objWidth / 2);
					alignment.y = (mc.currentScreen.height / 2) - (objHeight / 2);
					break;
				case CENTER_RIGHT:
					alignment.x = (mc.currentScreen.width ) - (objWidth);
					alignment.y = (mc.currentScreen.height / 2) - (objHeight / 2);
					break;
				case BOTTOM_LEFT:
					alignment.x = 0;
					alignment.y = (mc.currentScreen.height) - (objHeight);
					break;
				case BOTTOM_CENTER:
					alignment.x = (mc.currentScreen.width / 2) - (objWidth / 2);
					alignment.y = (mc.currentScreen.height) - (objHeight);
					break;
				case BOTTOM_RIGHT:
					alignment.x = (mc.currentScreen.width ) - (objWidth);
					alignment.y = (mc.currentScreen.height) - (objHeight);
					break;				
			}
			
			return alignment;
		}
		
	}
	
	
	
	private static final int BYTES_PER_PIXEL = 4;
	public static int loadTexture(BufferedImage image){
	   
	   int[] pixels = new int[image.getWidth() * image.getHeight()];
	     image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

	     ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB
	     
	     for(int y = 0; y < image.getHeight(); y++){
	         for(int x = 0; x < image.getWidth(); x++){
	             int pixel = pixels[y * image.getWidth() + x];
	             buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
	             buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
	             buffer.put((byte) (pixel & 0xFF));               // Blue component
	             buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
	         }
	     }

	     buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

	     // You now have a ByteBuffer filled with the color data of each pixel.
	     // Now just create a texture ID and bind it. Then you can load it using 
	     // whatever OpenGL method you want, for example:

	   int textureID = GL11.glGenTextures(); //Generate texture ID
	   	GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); //Bind texture ID
	     
	     //Setup wrap mode
	     GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11. GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
	     GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

	     //Setup texture scaling filtering
	     GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	     GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	     
	     //Send texel data to OpenGL
	     GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	   
	     //Return the texture ID so we can bind it later again
	   return textureID;
	}
	
	
	
	
	
	
}
