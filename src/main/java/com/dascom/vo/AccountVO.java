package com.dascom.vo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountVO {
	
	/**
	 * 单词拼写
	 */
	@Indexed(unique = true)
	private String spell;
	
	/**
	 * 阶段
	 */
	private int step;
	
	/**
	 * 不会次数
	 */
	private int nocount;
	
	/**
	 * 提示次数
	 */
	private int hintcount;
	
	/**
	 * 总次数
	 */
	private int allcount;
	
	/**
	 * 分值
	 */
	private int grade;
		
	/**
	 * 下一次复习时间
	 */
	private String nextDate;
	
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
