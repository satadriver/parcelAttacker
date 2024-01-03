package com.parcel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Zip {
	private String mZipFileName;
	private String mSrcPath;
	private String mBaseInPath;
	private String mAppendFn;
	private String mAppendPrefix;
	
	private int mType = 0;
	
	
	public Zip(String zipfn,String srcpath,String basepath,String appendfn,String appendprf)
	{
	    this.mZipFileName = zipfn;
	    
	    this.mSrcPath = srcpath;
	    
	    this.mBaseInPath = basepath;
	    
	    this.mAppendFn = appendfn; 
	    
	    mAppendPrefix = appendprf;
	    
	    File srcpathfile = new File(srcpath);
	    if(srcpathfile == null || srcpathfile.exists() == false){
	    	mType = -1;
	    	return;
	    }
	    
	    if(srcpathfile.isDirectory()){
		    if(mSrcPath.endsWith("/") == false && mSrcPath.endsWith("\\") == false){
		    	mSrcPath = mSrcPath + "/";
		    }
		    
		    mType = 1;
	    }else if(srcpathfile.isFile()){
	    	mType = 2;
	    }else{
	    	mType = 3;
	    }
	}
	
	public void zip()
	{
		try {
			if(mType <= 0){
				System.out.println("error file:" + mSrcPath + " not found");
				return;
			}else{
				System.out.println("compressing:" + mSrcPath);
			}
		    
	    	File zipFile = new File(mZipFileName);
	    	if (zipFile.exists() == false) {
				zipFile.createNewFile();
			}
	    	
		    if (mType == 1) {

		    	FileOutputStream fout = new FileOutputStream(mZipFileName);
		    	ZipOutputStream zos = new ZipOutputStream(fout);
		    	compressDir(zos,mSrcPath,mBaseInPath);		
		    	if (mAppendFn != null) {
		    		compressFile(zos,mAppendFn,mAppendPrefix);
				}
			    zos.flush();
			    zos.close();
			}
		    else if(mType == 2){
		    	FileOutputStream fout = new FileOutputStream(mZipFileName);
		    	ZipOutputStream zos = new ZipOutputStream(fout);
		    	compressFile(zos,mSrcPath,mBaseInPath);		
			    zos.flush();
			    zos.close();
		    }else{
		    	System.out.println("compress param error");
		    	return;
		    }

		    System.out.println("compress:" + mZipFileName + " ok");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

	

	public void compressDir(ZipOutputStream zos,String srcpath,String basepath) throws Exception
	{
		File[] flist = new File(srcpath).listFiles();
		
        for(int i=0; i < flist.length; i++)
        {
        	String filename = flist[i].getName();
		    if(flist[i].isDirectory())
		    {
		    	ZipEntry entry = new ZipEntry(basepath + filename + "/");
	        	
	        	zos.putNextEntry( entry );

            	compressDir(zos,flist[i].getAbsolutePath(),basepath + filename + "/");
		    }
		    else
		    {
		    	ZipEntry entry = new ZipEntry(basepath + filename);
				
				zos.putNextEntry( entry );
				
		        FileInputStream fin = new FileInputStream(flist[i]);
		        
		        byte buf[] = new byte[0x10000];
		        
		        while(true)
		        {
		        	int len = fin.read(buf,0,0x10000);
		        	if(len == -1){
		        		break;
		        	}
		        	zos.write(buf,0,len);
		        }

		        fin.close();
		    }
        }
	}
	    
	
	
	public void compressFile(ZipOutputStream zos,String srcpath,String basepath) throws Exception
	{
		File srcpathFile = new File(srcpath);
		if (srcpathFile.exists() == false) {
			return;
		}

    	String filename = srcpathFile.getName();

    	ZipEntry entry = new ZipEntry(basepath + filename);
		
		zos.putNextEntry( entry );
		
        FileInputStream fin = new FileInputStream(srcpath);
        
        byte buf[] = new byte[0x10000];
        
        while(true)
        {
        	int len = fin.read(buf,0,0x10000);
        	if(len == -1){
        		break;
        	}
        	zos.write(buf,0,len);
        }

        fin.close();   
	}
	
	
	
	
    public static void unZip(String unZipfileName, String mDestPath) {
    	
    	System.out.println("unzipping:" + unZipfileName);
    	
        if (mDestPath.endsWith("/") == false && mDestPath.endsWith("\\") == false) {
            mDestPath = mDestPath + "/";
        }
        
        FileOutputStream fileOut = null;
        ZipInputStream zipIn = null;
        ZipEntry zipEntry = null;
        File file = null;
        int readedBytes = 0;
        byte buf[] = new byte[0x10000];
        try {
            zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)));
            while ((zipEntry = zipIn.getNextEntry()) != null) {
                file = new File(mDestPath + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                } else {

                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOut = new FileOutputStream(file);
                    while ((readedBytes = zipIn.read(buf)) > 0) {
                        fileOut.write(buf, 0, readedBytes);
                    }
                    fileOut.close();
                }
                zipIn.closeEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

