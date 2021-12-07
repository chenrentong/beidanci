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

import com.dascom.dao.PhotoDao;
import com.dascom.entity.Account;
import com.dascom.entity.Photo;
import com.dascom.util.DynamicConfUtil;

@Repository
public class PhotoDaoImpl implements PhotoDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insertPhoto(Photo account, String accountName) {
		mongoTemplate.insert(account, accountName);
	}

	@Override
	public Photo findPhoto(String dateStr, String accountName) {
		Query query = new Query(Criteria.where("dateStr").is(dateStr));
		Photo account = mongoTemplate.findOne(query, Photo.class, accountName);
		return account;
	}

	@Override
	public String findPhotoTime(String accountName) {
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

	@Override
	public void updatePhoto(Photo photo, String accountName) {
		Query query = new Query(Criteria.where("dateStr").is(photo.getDateStr()));
		// 修改的内容
		Update update = new Update();
		update.set("nocount", photo.getNocount());
		update.set("step", photo.getStep());
		update.set("allcount", photo.getAllcount());
		update.set("grade", photo.getGrade());
		update.set("updateDate", new Date());
		update.set("nextDate", photo.getNextDate());
		mongoTemplate.updateFirst(query, update, Photo.class, accountName);

	}

	@Override
	public List<Photo> getAllPhoto(Photo photo, String accountName) {
		int max = Integer.parseInt(DynamicConfUtil.getProperty("start.grade"));
		Query query = new Query(new Criteria().orOperator(Criteria.where("nextDate").lte(new Date()),
				Criteria.where("grade").gte(max)));
		query.addCriteria(Criteria.where("grade").gt(0));
		List<Photo> list = mongoTemplate.find(query, Photo.class, accountName);
		return list;
	}

	@Override
	public void removePhoto(String dateStr, String accountName) {
		// TODO Auto-generated method stub

	}

}
