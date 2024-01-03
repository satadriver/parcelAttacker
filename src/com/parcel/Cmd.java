package com.parcel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Cmd {
	
    public static void cmd(String command) {

    	System.out.println("exec cmd:" + command);
    	
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec(command);
            InputStream in = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String message="";
            while((message = br.readLine()) != null) {
                sb.append(message);
            }
            System.out.println(sb);
            try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
