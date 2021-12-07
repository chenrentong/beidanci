package com.dascom.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Photo {

	/**
	 * 时间作为唯一
	 */
	@Indexed(unique = true)
	private String dateStr;
	
	/**
	 * 阶段
	 */
	private int step;
	
	/**
	 * 不会次数
	 */
	private int nocount;
	
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
	
	
	/**
	 * 科目
	 */
	private String subject;
	
	/**
	 * 备注
	 */
	private String memo;
	
	/**
	 * 图像
	 */
	private String image;
}
