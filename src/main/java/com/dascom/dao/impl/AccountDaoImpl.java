package com.dascom.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.dascom.dao.AccountDao;
import com.dascom.entity.Account;
import com.dascom.entity.Vocabulary;
import com.dascom.util.DynamicConfUtil;

@Repository
public class AccountDaoImpl implements AccountDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void insertAccount(Account account, String accountName) {
		mongoTemplate.insert(account, accountName);

	}

	@Override
	public Account findAccount(String spell, String accountName) {
		Query query = new Query(Criteria.where("spell").is(spell));
		Account account = mongoTemplate.findOne(query, Account.class, accountName);
		return account;
	}

	@Override
	public void updateAccount(Account account, String accountName) {
		Query query = new Query(Criteria.where("spell").is(account.getSpell()));
		// 修改的内容
		Update update = new Update();
		update.set("nocount", account.getNocount());
		update.set("step", account.getStep());
		update.set("hintcount", account.getHintcount());
		update.set("allcount", account.getAllcount());
		update.set("grade", account.getGrade());
		update.set("updateDate", new Date());
		update.set("nextDate", account.getNextDate());
		mongoTemplate.updateFirst(query, update, Account.class, accountName);

	}

	@Override
	public List<Account> getAllaccount(Account account, String accountName) {
		// 今天以前的,且分数大于100要推,
		/* Query query = new Query(); */
		// Query query = new Query(Criteria.where("nextDate").lte(new Date()));
		// 里面大
		int max = Integer.parseInt(DynamicConfUtil.getProperty("start.grade"));
		Query query = new Query(new Criteria().orOperator(Criteria.where("nextDate").lte(new Date()),
				Criteria.where("grade").gte(max)));
		query.addCriteria(Criteria.where("grade").gt(0));
		List<Account> list = mongoTemplate.find(query, Account.class, accountName);
		// List<Vocabulary> list=mongoTemplate.findAll(Vocabulary.class);
		return list;
	}

	@Override
	public void removeaccount(String id, String accountName) {
		Query query = new Query(Criteria.where("spell").is(id));
		mongoTemplate.findAndRemove(query, Account.class, accountName);

	}

	@Override
	public String findAccountTime(String accountName) {
		int max = Integer.parseInt(DynamicConfUtil.getProperty("start.grade"));
		Query query=new Query();
		/*Query query = new Query(new Criteria().orOperator(Criteria.where("nextDate").lte(new Date()),
				Criteria.where("grade").gte(max)));*/
		query.addCriteria(Criteria.where("grade").gt(0));
		query.with(new Sort(new Order(Direction.ASC, "nextDate")));
		Account list = mongoTemplate.findOne(query, Account.class, accountName);
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf1.format(list.getNextDate());
	}

}
