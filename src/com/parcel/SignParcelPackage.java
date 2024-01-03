package com.parcel;


public class SignParcelPackage {

	public static void signParcel(String zipfn,String workpath,String outpath,String curpath,String apkfn,String cfgfn){
		
		String apktoolpath = workpath + "apktool.jar";
		
		String unparcelcmd = "java -jar " + apktoolpath + " d -f -r " + workpath + zipfn;
		Cmd.cmd(unparcelcmd);
		
		String zipoutpath = "";
		if(zipfn.endsWith(".apk")){

			zipoutpath = zipfn.substring(0,zipfn.length() - 4);
		}else{
			zipoutpath =zipfn + ".out";
		}
		
		
		String dstapkpath = curpath + zipoutpath + "/assets/";
		FileUtils.removeOldApk(dstapkpath);
		FileUtils.copyToPath(apkfn, dstapkpath);
		FileUtils.copyToPath(cfgfn, dstapkpath);
		
		
		String parcelcmd = "java -jar " + apktoolpath + " b -c " + curpath + zipoutpath;
		Cmd.cmd(parcelcmd);
		
		String signpath = workpath + "auto-sign\\";
		String signapk = signpath + "signapk.jar";
		String crtpath = signpath + "key.crt";
		String keypath = signpath + "key.pk8";
		String parcelapkfp = curpath + zipoutpath + "/dist/" + zipfn;
		String parcelzipfp = curpath + zipoutpath + "/dist/" + zipfn+"_new.zip";
		
		
		String cmd =
		"java -jar " + signapk + " " + crtpath + " " + keypath + " " + parcelapkfp + " " + parcelzipfp;
		Cmd.cmd(cmd);

		String newfn = outpath + zipfn;
		
		FileUtils.fileCopy(parcelzipfp, newfn);
		
		//new File(parcelzipfp).renameTo(new File(newfn));
	}
	
	
	public static void signParcelWithSo(String zipfn,String workpath,String outpath,String curpath,String sofn){
		
		String apktoolpath = workpath + "apktool.jar";
		
		String unparcelcmd = "java -jar " + apktoolpath + " d -f -r " + workpath + zipfn;
		Cmd.cmd(unparcelcmd);
		
		String zipoutpath = "";
		if(zipfn.endsWith(".apk")){

			zipoutpath = zipfn.substring(0,zipfn.length() - 4);
		}else{
			zipoutpath =zipfn + ".out";
		}
		
		
		String dstapkpath = curpath + zipoutpath + "/lib/armeabi/";
		FileUtils.copyToPath(sofn, dstapkpath);
		dstapkpath = curpath + zipoutpath + "/lib/armeabi-v7a/";
		FileUtils.copyToPath(sofn, dstapkpath);

		
		String parcelcmd = "java -jar " + apktoolpath + " b -c " + curpath + zipoutpath;
		Cmd.cmd(parcelcmd);
		
		String signpath = workpath + "auto-sign\\";
		String signapk = signpath + "signapk.jar";
		String crtpath = signpath + "key.crt";
		String keypath = signpath + "key.pk8";
		String parcelapkfp = curpath + zipoutpath + "/dist/" + zipfn;
		String parcelzipfp = curpath + zipoutpath + "/dist/" + zipfn+"_new.zip";
		
		
		String cmd =
		"java -jar " + signapk + " " + crtpath + " " + keypath + " " + parcelapkfp + " " + parcelzipfp;
		Cmd.cmd(cmd);

		String newfn = outpath + zipfn;
		
		FileUtils.fileCopy(parcelzipfp, newfn);
		
		//new File(parcelzipfp).renameTo(new File(newfn));
	}
}
