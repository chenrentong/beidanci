package com.dascom.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Account {
	
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
	 * 创建时间
	 */
	private Date creatDate;
	
	/**
	 * 修改时间
	 */
	private Date updateDate;
	
	/**
	 * 下一次复习时间
	 */
	private Date nextDate;
}
