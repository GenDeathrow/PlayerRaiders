package com.gendeathrow.pmobs.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.logging.log4j.Level;

import com.gendeathrow.pmobs.core.RaidersMain;
import com.google.gson.JsonObject;

	public class Tools 
	{
		//
		
	    public static InputStreamReader HttpRequest() throws IOException 
	    {
	    	
	            String stringUrl = "https://api.twitch.tv/kraken/search/streams?q=starcraft";
		        URL url = new URL(stringUrl);
		        URLConnection uc = url.openConnection();

		        uc.setRequestProperty("application/vnd.twitchtv.v3+json", "Accept");

		        InputStreamReader inputStreamReader = new InputStreamReader(uc.getInputStream());

		        //System.out.println(IOUtils.toString(inputStreamReader));
		        
		        return inputStreamReader;
		}
	    
	    
	    public static void DownloadFile(String url, String fileName) throws IOException 
	    {
			 
			 URL link = new URL(url); //The file that you want to download
			
			 InputStream in = new BufferedInputStream(link.openStream());
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 byte[] buf = new byte[1024];
			 int n = 0;
			 while (-1!=(n=in.read(buf)))
			 {
			    out.write(buf, 0, n);
			 }
			 out.close();
			 in.close();
			 byte[] response = out.toByteArray();

			 FileOutputStream fos = new FileOutputStream(fileName);
			 fos.write(response);
			 fos.close();

		}
	    
	    public static String CreateSaveFile(File file, String content)
		{
			try 
			{

				// if file doesnt exists, then create it
				if (!file.exists()) 
				{
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(content);
				bw.close();
				
				
				return "File saved to: "+ file.getPath();
			} catch (IOException e) 
			{
				e.printStackTrace();
				
				return "Error: While creating/saving file. Check Logs!";
			}
		}
	    
	    @SuppressWarnings("resource")
		public static String URLReader(String urlString) throws Exception 
	    {
	    	String string = "";
	    		try {
	    		   URL url = new URL(urlString);
	    		   Scanner s = new Scanner(url.openStream());
	    		   // read from your scanner
	    		   
	    		   while(s.hasNextLine())
	    		   {
	    			  string += s.nextLine() + System.getProperty("line.separator");
	    		   }
	    		   

	    		   
	    		}
	    		catch(IOException ex) {

	    		   ex.printStackTrace(); 
	    		   return null;
	    		}
	    		
	    		return string;
	    }
	    
	    
		public static File lastFileModified(String dir) 
		{
		    File fl = new File(dir);
		    File[] files = fl.listFiles(new FileFilter() {          
		        public boolean accept(File file) {
		            return file.isFile();
		        }
		    });
		    long lastMod = Long.MIN_VALUE;
		    File choice = null;
		    for (File file : files) {
		        if (file.lastModified() > lastMod) {
		            choice = file;
		            lastMod = file.lastModified();
		        }
		    }
		    return choice;
		}
		
		public static String readFile(String path, Charset encoding) throws IOException 
		{
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded, encoding);
		}
		
		public static String sendJsonHttpPost(String url, JsonObject postData) throws IOException
		{
			URL object=new URL(url);
			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json"); 
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("POST");
			
			con.getOutputStream().write(postData.toString().getBytes());

			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder stringBuilder = new StringBuilder();
			
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				stringBuilder.append(line + "\n");
				System.out.println(line);
			}
		    	
			con.connect();
		        
			return stringBuilder.toString();
		}
		 
	    public static void sendpost() throws IOException
	    {
	    	URL object=new URL("http://logs-01.loggly.com/inputs/a522e114-193a-4518-ae3e-10a2732bc9f3/tag/http/");

	    	HttpURLConnection con = (HttpURLConnection) object.openConnection();
	    	con.setDoOutput(true);
	    	con.setDoInput(true);
	    	con.setRequestProperty("Content-Type", "content-type:text/plain");
	    	//con.setRequestProperty("Accept", "application/json");
	    	con.setRequestMethod("POST");
	    	String hello = "hello";
	    	
	    	con.getOutputStream().write(hello.getBytes());
	    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        StringBuilder stringBuilder = new StringBuilder();
	   
	        String line = null;
	        while ((line = reader.readLine()) != null)
	        {
	          stringBuilder.append(line + "\n");
	          System.out.println(line);
	        }
	    	
	        con.connect();
	    	
	        
	    }


		public static boolean CopytoClipbard(String string)
		{
			try
			{
				StringSelection selection = new StringSelection(string);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				return true;
			}catch(Exception e)
			{
				RaidersMain.logger.log(Level.ERROR, e);
				return false;
			}
		}
		
		
//		public static String getItemInfo(String[] args, String itemID, ItemStack stack)
//		{
//
//			String ore = "";
//			String nbt = "";
//			
//			if(args.length > 1)
//			{
//			
//				boolean flag1 = false;
//				boolean flag2 = false;
//				boolean flag3 = false;
//				
//				for(String arg : args)
//				{
//
//					if(arg.toLowerCase().trim().equals("<>") && !flag1)
//					{
//						itemID = "<"+ itemID+">";
//						flag1 = true;
//					}
//					else if(arg.toLowerCase().trim().equals("ore")  && !flag2)
//					{
//						ore += " Ores:[";
//						boolean f = false;
//						for(int id : OreDictionary.getOreIDs(stack))
//						{
//							ore += (!f ? " ore:" : " | ore:")+ OreDictionary.getOreName(id);
//							f = true;
//						}
//						ore += "]";
//						flag2 = true;
//					}
//					else if(arg.toLowerCase().trim().equals("nbt") && !flag3)
//					{
//						NBTTagCompound nbtdata = stack.getTagCompound();
//					
//						nbt += " NBT:";
//						if(nbtdata != null)
//						{
//							nbt += " "+ new GsonBuilder().create().toJson(NBTConverter.NBTtoJSON_Compound(nbtdata, new JsonObject()));
//						}
//						else nbt += " {NBT Null}";
//						flag3 = true;
//					}
//				}
//			
//			}
//			return itemID + ore + nbt +  System.getProperty("line.separator");
//		}

}
