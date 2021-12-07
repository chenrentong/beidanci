package com.dascom.entity;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="vacabulary")
public class Vocabulary {
	
	/**
	 * 单词拼写
	 */
	@Indexed(unique = true)
	private String spell;
	
	/**
	 * 笔记本
	 */
	private String notebook;
	
	/**
	 * 笔记
	 */
	private String noteName;
	
	/**
	 * 意思
	 */
	private List<String> answer;
	
	/**
	 * 词性
	 */
	private List<String> type;
	
}
