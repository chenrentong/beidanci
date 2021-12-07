package com.dascom.dao;

import java.util.List;

import com.dascom.entity.Account;



public interface AccountDao {

	/**
	 * 插入
	 * @param numconfig
	 */
	void insertAccount(Account account,String accountName);
	
	/**
	 * 查找某个
	 * @param number
	 * @return
	 */
	Account findAccount(String spell,String accountName);
	
	/**
	 * 查找某个
	 * @param number
	 * @return
	 */
	String  findAccountTime(String accountName);
	
	/**
	 * 更新
	 * @param numconfig
	 */
	void updateAccount(Account account,String accountName);
	
	/**
	 * 获取所有
	 * @return
	 */
	List<Account> getAllaccount(Account account,String accountName);
	
	/**
	 * 删除
	 * @param number
	 */
	void removeaccount(String spell,String accountName);
}
