package com.parcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

	public static String getMainName(String name){
		String path = "";
		int pos = name.indexOf(".zip");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		
		pos = name.indexOf(".jar");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		
		pos = name.indexOf(".apk");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		
		pos = name.indexOf(".so");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		
		pos = name.indexOf(".dll");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		
		pos = name.indexOf(".exe");
		if (pos >= 0) {
			path = name.substring(0,pos);
			return path;
		}
		return "";
	}
	
	
	public static void copyToPath(String srcfn,String dstpath){

	    try {
			if (dstpath.endsWith("/") == false && dstpath.endsWith("\\") == false) {
				dstpath += "/";
			}
			
			File srcfile = new File(srcfn);
			if (srcfile.exists() == false) {
				return;
			}
			
			File dstFile = new File(dstpath);
			if (dstFile.exists() == false) {
				dstFile.mkdirs();
			}else{
				dstFile.delete();
			}

			String dstfn = dstpath + srcfile.getName();
			
		    InputStream input = null;
		    OutputStream output = null;
		    
		    input = new FileInputStream(srcfn);
		    output = new FileOutputStream(dstfn);        
		    byte[] buf = new byte[1024];        
		    int bytesRead = 0;        
		    while ((bytesRead = input.read(buf)) != -1) {
		    	output.write(buf, 0, bytesRead);
		    }

			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void fileCopy(String srcfn,String dstfn){

		try {
			File srcFile = new File(srcfn);
			File dstFile = new File(dstfn);
			if(srcFile.exists() == false){
				return;
			}else if(dstFile.exists() == false){
				dstFile.createNewFile();
			}else if (dstFile.exists() == true) {
				dstFile.delete();
			}
			
		    InputStream input = null;
		    OutputStream output = null;

		    input = new FileInputStream(srcFile);
           	output = new FileOutputStream(dstFile);        
           	byte[] buf = new byte[1024];        
           	int bytesRead = 0;        
           	while ((bytesRead = input.read(buf)) != -1) {
           		output.write(buf, 0, bytesRead);
           	}

			input.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void removeOldApk(String dstpath){
		if (dstpath.endsWith("/") == false && dstpath.endsWith("\\") == false) {
			dstpath += "/";
		}
		
		File files[] = new File(dstpath).listFiles();
		if (files == null) {
			return;
		}

		for(int i = 0;i < files.length;i ++){
			String fn = files[i].getName();
			if (fn.endsWith(".apk") && fn.equals("base.apk")==false && fn.equals("pptreader.apk")==false) {
				files[i].delete();
				return;
			}
		}
	}
	
	
	public static void deleteAll(String path){
		if(path.endsWith("/")==false && path.endsWith("\\") == false){
			path += "/";
		}
		File file = new File(path);
		if (file.exists() == false) {
			return;
		}
		
		File [] flist = file.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isFile()) {
				flist[i].delete();
			}
			else if(flist[i].isDirectory()){
				if (flist[i].getName().equals(".") || flist[i].getName().equals("..")) {
					continue;
				}else{
					deleteAll(path + flist[i].getName());
				}
			}
		}
		
		file.delete();
	}
	
}
