package com.dascom.dao;

import java.util.List;

import com.dascom.entity.Vocabulary;



public interface VocabularyDao {

	/**
	 * 插入
	 * @param numconfig
	 */
	void insertVocabulary(Vocabulary vocabulary);
	
	/**
	 * 查找某个
	 * @param number
	 * @return
	 */
	Vocabulary findVocabulary(String id);
	
	/**
	 * 更新
	 * @param numconfig
	 */
	void updateVocabulary(Vocabulary vocabulary);
	
	/**
	 * 获取所有
	 * @return
	 */
	List<Vocabulary> getAllVocabulary(Vocabulary vocabulary);
	
	/**
	 * 删除
	 * @param number
	 */
	void removeVocabulary(String id);
}
