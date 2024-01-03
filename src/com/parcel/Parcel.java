package com.parcel;

import java.io.File;

public class Parcel {
	

	static String TEMP_PATH = "tmp\\";
	static String OUT_PATH = "out\\";
	static String WORK_PATH = "work\\";

	//zip path
	public static void main(String args[]){
		String curdir = System.getProperty("user.dir")+"\\";
		
		File pathFile = new File(curdir + Parcel.TEMP_PATH);
		if (pathFile.exists() == false) {
			pathFile.mkdirs();
		}else{
			FileUtils.deleteAll(curdir + Parcel.TEMP_PATH);
			pathFile.mkdirs();
			//Cmd.cmd("cmd /c del /F /S /Q "+curdir + Parcel.TEMP_PATH);	//rmdir == rd
		}
		
		pathFile = new File(curdir + Parcel.OUT_PATH);
		if (pathFile.exists() == false) {
			pathFile.mkdirs();
		}else{
			//Cmd.cmd("cmd /c del /F /S /Q "+curdir + Parcel.OUT_PATH);
			FileUtils.deleteAll(curdir + Parcel.OUT_PATH);
			pathFile.mkdirs();
		}
		
		pathFile = new File(curdir + Parcel.WORK_PATH);
		if (pathFile.exists() == false) {
			pathFile.mkdirs();
		}


		
		String apkfn = args[2];
		
		String username = args[1];
		
		String ip = args[0];
		
		System.out.println("apk filename:"+apkfn + "\r\nusername:"+username + "\r\nip:"+ip);

		new AndroidParcel(apkfn,ip,username);

		new WindowsParcel(ip,username);
	}

}
