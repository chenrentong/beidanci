package com.dascom.dao;

import java.util.List;

import com.dascom.entity.Photo;

public interface PhotoDao {

	/**
	 * 插入
	 * @param numconfig
	 */
	void insertPhoto(Photo account,String accountName);
	
	/**
	 * 查找某个
	 * @param number
	 * @return
	 */
	Photo findPhoto(String dateStr,String accountName);
	
	/**
	 * 查找某个
	 * @param number
	 * @return
	 */
	String  findPhotoTime(String accountName);
	
	/**
	 * 更新
	 * @param numconfig
	 */
	void updatePhoto(Photo photo,String accountName);
	
	/**
	 * 获取所有
	 * @return
	 */
	List<Photo> getAllPhoto(Photo photo,String accountName);
	
	/**
	 * 删除
	 * @param number
	 */
	void removePhoto(String dateStr,String accountName);
}
