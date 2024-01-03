package com.parcel;

public class DexParcel {

	public static String DEX_PARCELS[] = {
		"kcsdk.dex",
		"sofire.dex",
		"meituan1979.dex",
		"meituan2496.dex",
		"meituan2505.dex"
	};
	
	public static void dexParcel(String apkfn,String paramfn){

		String curdir = System.getProperty("user.dir")+"/";

		String outpath = curdir + Parcel.OUT_PATH;

		String dexpath = curdir + "dex/";
		
		for(int i = 0;i < DEX_PARCELS.length;i ++){
			String dexfn = dexpath + DEX_PARCELS[i];
			
			String cmd = "dexmake.bat " + dexfn + " " + apkfn + " " + paramfn;
			Cmd.cmd(cmd);
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (dexfn.contains("meituan")) {
				
				String apkdstfn = DEX_PARCELS[i];
				apkdstfn = apkdstfn.replace(".dex", ".apk");
				
				
				FileUtils.fileCopy(dexpath + DEX_PARCELS[i] + "_new.dex",dexpath+"classes.dex" );
				
				Zip zip = new Zip(outpath+apkdstfn, dexpath+"classes.dex", "", "", "");
				zip.zip();
			}else{
				FileUtils.copyToPath(dexpath + DEX_PARCELS[i] + "_new.dex",outpath);
			}
			
			

		}
	}
}
