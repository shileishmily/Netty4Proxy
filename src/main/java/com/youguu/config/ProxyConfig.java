package com.youguu.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by leo on 2017/11/14.
 */
public class ProxyConfig {

	private static Properties properties = new Properties();

	static {
		try{
			String filePath = ProxyConfig.class.getResource("/").getPath() + File.separatorChar + "properties/config.properties";
			InputStream inputStream = new FileInputStream(filePath);

			properties.load(inputStream);
			if (inputStream != null)
				inputStream.close();
		} catch (Exception e){

		}
	}

	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static int getInteger(String key){
		return Integer.parseInt(getString(key));
	}
}
