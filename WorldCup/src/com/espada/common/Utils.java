package com.espada.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Utils {
	
	
	/**
	 * 将asset中的文件拷贝到本地目录
	 * @param context
	 * @param assetsName 在asserts需要copy的文件name
	 * @param outputPath out put path 输出路径
	 * @param outPutName 输出的命名
	 * @throws IOException
	 */
	public static void copyAssetsFile(Context context,String assetsName,String outputPath) throws IOException{
		String[] fileList = context.getAssets().list(assetsName);
		
		for(int i=0;i<fileList.length;i++){
			System.out.println("copy asset file is "+fileList[i]);
		}
		
		if(fileList.length>0){
			
			for(int i=0;i<fileList.length;i++){
				File fileDir = new File(outputPath);
				
				if(!fileDir.exists()){
					fileDir.mkdirs();
				}
				
				String assetName = assetsName+"/"+fileList[i];
				String assetOutputPath = outputPath;
				
				if(context.getAssets().list(assetName).length>0){
					assetOutputPath = outputPath+"/"+fileList[i]+"/";
				}
				copyAssetsFile(context,assetName,assetOutputPath);
			}
						
		}else{
			File file = new File(outputPath);
	        if(!file.exists())
	        	file.mkdir();
			InputStream myInput = context.getAssets().open(assetsName);
	        String outFileName = outputPath + assetsName.substring(assetsName.lastIndexOf("/")+1);
	        OutputStream myOutput = new FileOutputStream(outFileName);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myInput.read(buffer))>0){
	            myOutput.write(buffer, 0, length);
	        }
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
		}
				
	}
	
	public Bitmap getBitmapByPath(String path)
	{
//		String path = path;
		Bitmap btp = null;
		try {
			InputStream is = new FileInputStream(path);
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inTempStorage = new byte[100 * 1024];
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true; 
//			opts.inJustDecodeBounds = true;
//			opts.inSampleSize = 4;
			opts.inInputShareable = true;
			btp =BitmapFactory.decodeStream(is,null, opts); 
//			
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return btp;
	}
	
	public Drawable getDrawableByPath(String path)
	{
//		String path = path;
		BitmapDrawable btpDrawable = null;
		Bitmap btp = null;
		try {
			InputStream is = new FileInputStream(path);
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inTempStorage = new byte[100 * 1024];
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true; 
//			opts.inJustDecodeBounds = true;
//			opts.inSampleSize = 4;
			opts.inInputShareable = true;
			btp =BitmapFactory.decodeStream(is,null, opts); 
			btp = Bitmap.createScaledBitmap(btp, 200, 120, false);
			btpDrawable=new BitmapDrawable(btp);
//			
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return btpDrawable;
	}
	

}
