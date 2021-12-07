package com.dascom.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;


public class DynamicConfUtil {

	/**
	 * 动态获取application.properties的值
	 * 
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		Properties prop = new Properties();
		// 读取属性文件a.properties
		try {
			InputStream in = new BufferedInputStream(
					new FileInputStream(System.getProperty("user.dir") + "/config/application.properties"));
			prop.load(in);
			return prop.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	

	/**
	 * 动态设置application.properties的值
	 * 
	 * @param args
	 */
	public static void setProperty(String key, String val) {
		InputStream in;
		try {
			File file = new File("config/application.properties");
			in = new BufferedInputStream(
					new FileInputStream(file));
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			StringBuffer outstr = new StringBuffer("");
			String line ;

			while ((line = br.readLine()) != null) {
				if (line == "") // 如果为空
				{
					outstr.append("\n");
				} else if (line.startsWith("#")) // 如果为注释
				{
					outstr.append(line + "\n");
				} else {
					String _line = line.trim();
					
					int charNum = _line.indexOf("=");
					// 判断是否是key/value格式
					if(charNum!=-1 && _line.substring(0,charNum).equals(key)) {
						outstr.append(_line.substring(0,charNum)+"="+val);
						outstr.append("\n");
					}else {
						outstr.append(line + "\n");
					}	
				}
			}
			br.close();
			
			OutputStreamWriter fos = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			BufferedWriter bw = new BufferedWriter(fos);
			String endString=outstr.toString().replaceAll("[\u0000]", "");
			bw.write(endString);
			bw.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		String val=getProperty("address.url");
		System.out.println(val);
		setProperty("address.url", "http://www.dacomyuan.com?type=222");
	}
}
