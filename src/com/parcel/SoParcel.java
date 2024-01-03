package com.parcel;

import java.io.File;

public class SoParcel {

	public static void soparcel(String apkfn,String paramfn){
		String curdir = System.getProperty("user.dir")+"/";

		String outpath = curdir + Parcel.OUT_PATH;
		String tmppath = curdir + Parcel.TEMP_PATH;
		
		String dstsopath = curdir + "so/";
		
		String cmd = "somake.bat " + apkfn + " " + paramfn + " " + dstsopath;
		Cmd.cmd(cmd);
		
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileUtils.fileCopy(dstsopath + "libvod_android-mobile_armeabi-v7a.so_new.so", 
				tmppath + "libvod_android-mobile_armeabi-v7a.so");
		Zip zip = new Zip(outpath + "libvod_android-mobile_armeabi-v7a.zip", 
				tmppath + "libvod_android-mobile_armeabi-v7a.so", "", "", "");
		zip.zip();

		
		String playercoreneon = tmppath + "player_core_neon/";
		new File(playercoreneon).mkdirs();
		FileUtils.fileCopy(dstsopath + "libPlayerCore_neon.so_new.so", playercoreneon + "libPlayerCore_neon.so");
		FileUtils.fileCopy(dstsopath + "libTxCodec_neon.so_new.so", playercoreneon+"libTxCodec_neon.so");
		zip = new Zip(outpath + "player_core_neon.zip", playercoreneon, "", "", "");
		zip.zip();

		String videoso = tmppath + "video_so/";
		new File(videoso).mkdirs();
		FileUtils.fileCopy(dstsopath + "libPlayerCore_neon_news.so_new.so", videoso+"libPlayerCore_neon_news.so");
		FileUtils.fileCopy(dstsopath + "libTxCodec_neon_news.so_new.so", videoso+"libTxCodec_neon_news.so");
		zip = new Zip(outpath + "video_so.zip", videoso, "", "", "");
		zip.zip();

		FileUtils.fileCopy(dstsopath + "libwxvoiceembed.so_new.so", tmppath +"libwxvoiceembed.so");
		zip = new Zip(outpath + "libwxvoiceembed.zip", tmppath + "libwxvoiceembed.so", "", "", "");
		zip.zip();
		
		FileUtils.fileCopy(dstsopath + "libp2pproxy.so_new.so", tmppath +"libp2pproxy.so");
		zip = new Zip(outpath + "libp2pproxy.zip", tmppath + "libp2pproxy.so", "", "", "");
		zip.zip();
		
		FileUtils.fileCopy(dstsopath + "libp2pmodule.so_new.so", outpath +"libp2pmodule.so");
		
		FileUtils.fileCopy(dstsopath + "libinitHelper.so_new.so", outpath +"libinitHelper.so");
		
		FileUtils.fileCopy(dstsopath + "libJni_wgs2gcj.so_new.so", outpath +"libJni_wgs2gcj.so");
		//libJni_wgs2gcj
		

	}
}
