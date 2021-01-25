package com.us.weavx.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AccessKeyGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader bR = null;
		try {
			bR = new BufferedReader(new InputStreamReader(System.in));
			String comando = bR.readLine();
			String argsC[] = comando.split("[ ]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
			for (int i = 0; i < argsC.length; i++) {
				System.out.println(argsC[i].replaceAll("\"",""));
			}
		} catch (Exception e) {
			System.err.println("ERROR: "+e);
		} finally {
			if (bR != null) {
				try {
					bR.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		

	}

}
