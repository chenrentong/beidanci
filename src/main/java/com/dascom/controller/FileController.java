package com.dascom.controller;





import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.dascom.util.BusinessBack;

@RestController
@RequestMapping("file")
public class FileController {
	
	/**
	 * 添加
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public Object add(@RequestParam MultipartFile file,@RequestParam String type) throws Exception {
		System.out.println("接收到文件");
		String fileOrigName=stringToMD5(file.getBytes());
		 //获取项目根路径并转到static/videos
        String path = "/images/"+type+"/";
        File file2=new File(path);
        if(!file2.exists())//文件夹不存在就创建
        {
            file2.mkdirs();
        }
        String filepath=path+ fileOrigName+".png";
		//保存文件
       /* try {
        	File file3=new File(file2+"\\"+ fileOrigName+".png");
        	System.out.println(file3.getAbsolutePath());
        	file.transferTo(file3);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println(filepath);
      //将文件输出到本地
        File files = new File(filepath);
        try {
            if (!files.exists()) {				//如果文件不存在则新建文件
                files.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(files);

            byte[] bytes = file.getBytes();

            output.write(bytes);				//将数组的信息写入文件中

            output.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return BusinessBack.success("http://localhost:8888/file/get/"+type+"/"+ fileOrigName+".png");
		
	}
	
	/**
	 * 添加
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/get/{type}/{fileName}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object get(@PathVariable String fileName,@PathVariable String type) throws Exception {
		try {
			
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png")
						.body(InputStream2ByteArray("/images/"+type+"/"+fileName));
			
		} catch (Exception e) {
			JSONObject json = new JSONObject();
			json.put("code", -1);
			json.put("data", e.getMessage());
			return json.toString();
		}
	}
	
	private byte[] InputStream2ByteArray(String filePath) throws IOException {
		 
	    InputStream in = new FileInputStream(filePath);
	    byte[] data = toByteArray(in);
	    in.close();
	 
	    return data;
	}
	
	private byte[] toByteArray(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024 * 4];
	    int n = 0;
	    while ((n = in.read(buffer)) != -1) {
	        out.write(buffer, 0, n);
	    }
	    return out.toByteArray();
	}
	
	public static String stringToMD5(byte[] plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }
	
}
