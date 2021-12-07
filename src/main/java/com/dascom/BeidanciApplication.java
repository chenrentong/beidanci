package com.dascom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

@SpringBootApplication
public class BeidanciApplication {

	public static void main(String[] args) {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SpringApplication.run(BeidanciApplication.class, args);
	}

	/**
	 * 静态初始化
	 * @throws IOException
	 */
	private static void init() throws IOException {
		File configPath = new File(System.getProperty("user.dir"), "config");
		
		
		if (!configPath.exists()) {
			boolean result = configPath.mkdirs();
			System.out.println("创建config目录结果:" + result);
		}	
		copyConfigFile("",new File(configPath, "application.properties"));	
	}
	
	private static void copyConfigFile(String pre,File dest) throws IOException {
		if (!dest.exists()) {
			InputStream inputStream = new ClassPathResource(pre+dest.getName()).getInputStream();
			FileCopyUtils.copy(inputStream, new FileOutputStream(dest));
		}
	}
}
