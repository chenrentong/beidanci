package com.dascom.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.dascom.dao.VocabularyDao;
import com.dascom.entity.Vocabulary;

@Repository
public class VocabularyDaoImpl implements VocabularyDao {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	@Override
	public void insertVocabulary(Vocabulary vocabulary) {
		mongoTemplate.insert(vocabulary);
		
	}

	@Override
	public void updateVocabulary(Vocabulary vocabulary){
       /* Query query = new Query(Criteria.where("_id").is(numconfig.getNumber()));
        //修改的内容
        Update update = new Update();
        update.set("_id",numconfig.getNumber());
        update.set("voiceswitch", numconfig.isVoiceswitch());
        update.set("printtipswitch", numconfig.isPrinttipswitch());
        update.set("readidle", numconfig.getReadidle());
        update.set("voicetime", numconfig.getVoicetime());
        update.set("deviceUrl", numconfig.getDeviceUrl());
        update.set("resultUrl", numconfig.getResultUrl());
        update.set("messageUrl", numconfig.getMessageUrl());
        mongoTemplate.updateFirst(query,update,NumConfig.class);*/
	}

	@Override
	public List<Vocabulary> getAllVocabulary(Vocabulary vocabulary) {
		Query query = new Query(Criteria.where("notebook").is(vocabulary.getNotebook()));
		query.addCriteria(Criteria.where("noteName").is(vocabulary.getNoteName()));
		List<Vocabulary> list=mongoTemplate.find(query, Vocabulary.class);
		//List<Vocabulary> list=mongoTemplate.findAll(Vocabulary.class);
		return list;
	}
	
	
	@Override
	public Vocabulary findVocabulary(String spell){
		Query query = new Query(Criteria.where("spell").is(spell));
		Vocabulary vocabulary=mongoTemplate.findOne(query, Vocabulary.class);
        return vocabulary;
	}

	@Override
	public void removeVocabulary(String id){
		Query query = new Query(Criteria.where("_id").is(id));
		mongoTemplate.findAndRemove(query, Vocabulary.class);
	}

}
