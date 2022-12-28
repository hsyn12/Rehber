package com.tr.hsyn.files;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Locale;



public class Files{
   
   public static long getDuration(String filePath){
      
      long fileDuration = -60L;
      
      try{
         
         MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
         metaRetriever.setDataSource(filePath);
         
         String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
         
         metaRetriever.release();
         fileDuration = Long.parseLong(duration);
      }
      catch(Exception e){
   
         Log.d("Rehber", "Bilgi alınamadı");
      }
      
      return fileDuration;
   }
   
   public static String getMp3Duration(File file){
      
      return formatMilliSeconds(getDuration(file.getAbsolutePath()));
   }
   
   public static boolean deleteFile(File file){
      
      if(file == null){
   
         Log.d("Rehber", "file = null (bu bir hata değil)");
         return false;
      }
      
      if(file.delete()){
   
         Log.d("Rehber", "Dosya silindi : " + file.getName());
         return true;
      }
   
      if(!file.exists()){
   
         Log.d("Rehber", "Dosya silinemedi çünkü böyle bir dosya yok : " + file.getName());
      }
      else{
   
         Log.d("Rehber", "Dosya silinemedi  : " + file.getName());
      }
      
      return false;
   }
   
   public static String formatMilliSeconds(long milliseconds){
      
      // Convert total duration into time
      int hours   = (int) (milliseconds / (1000 * 60 * 60));
      int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
      int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
      
      if(hours != 0){
         
         return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
      }
      
      return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
   }
	
	public static String formatSeconds(int seconds) {
		
   	return formatMilliSeconds(seconds * 1000L);
	}
	
   public static String getAudioFolder(Context context){
      
      File file = new File(context.getFilesDir(), "audio");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file.getAbsolutePath();
         }
      }
      else{
         return file.getAbsolutePath();
      }
      
      return context.getFilesDir() + "/audio";
   }
   
   public static String getCallAudioFolder(Context context){
      
      File file = new File(context.getFilesDir(), "call");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file.getAbsolutePath();
         }
      }
      else{
         return file.getAbsolutePath();
      }
      
      return context.getFilesDir() + "/call";
   }
   
   public static File getAudioFolderFile(Context context){
      
      File file = new File(context.getFilesDir(), "audio");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file;
         }
      }
      else{
         return file;
      }
      
      return new File(context.getFilesDir() + "/audio");
   }
   
   public static File getCallAudioFolderFile(Context context){
      
      File file = new File(context.getFilesDir(), "call");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file;
         }
      }
      else{
         return file;
      }
      
      return new File(context.getFilesDir() + "/call");
   }
   
   @Nullable
   private static File getMailbox(Context context){
   
      File file = new File(context.getFilesDir(), "mailbox");
   
      if(!file.exists()){
      
         if(file.mkdir()){
         
            return file;
         }
         else{
   
            return null;
         }
      }
      else{
         
         return file;
      }
   }
   
   @Nullable
   public static File getSentbox(Context context){
      
      File file = new File(getMailbox(context), "sentbox");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file;
         }
         else{
   
            return null;
         }
      }
      else{
         return file;
      }
   }
   
   @Nullable
   public static File getOutbox(Context context){
      
      File file = new File(getMailbox(context), "outbox");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file;
         }
         else{
   
            return null;
         }
      }
      else{
         return file;
      }
   }
   
   @Nullable
   public static File getInbox(Context context){
      
      File file = new File(getMailbox(context), "inbox");
      
      if(!file.exists()){
         
         if(file.mkdir()){
            
            return file;
         }
         else{
            
            return null;
         }
      }
      else{
         return file;
      }
   }
   
   @Nullable @SuppressWarnings("ResultOfMethodCallIgnored")
   public static String getFileContent(@NonNull File file){
      
      if(!file.exists()) return null;
      
      long len = file.length();
      
      byte[] byteArray = new byte[(int) len];
      
      try{
         
         FileInputStream stream = new FileInputStream(file);
         stream.read(byteArray);
         stream.close();
         
         return new String(byteArray);
      }
      catch(Exception ignore){}
      
      return null;
   }
	
	@Nullable
	public static<T> File writeObjects(@NonNull File file, @NonNull List<T> objects) {
		
		ObjectOutputStream out = null;
		
		try {
			
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(objects);
			out.flush();
			return file;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			
			if(out != null) {
				try {
					out.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> List<T> readObjects(@NonNull File file){
		
		if (file.exists()) {
			
			ObjectInputStream in = null;
			
			try {
				
				in = new ObjectInputStream(new FileInputStream(file));
				return (List<T>) in.readObject();
			}
			catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally {
				
				if(in != null) {
					try {
						in.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return null;
	}
   
	
	
	
}
