package com.dascom.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dascom.dao.AccountDao;
import com.dascom.dao.PhotoDao;
import com.dascom.dao.VocabularyDao;
import com.dascom.entity.Account;
import com.dascom.entity.Photo;
import com.dascom.entity.Vocabulary;
import com.dascom.util.BusinessBack;
import com.dascom.util.DynamicConfUtil;
import com.dascom.vo.EvernoteCatalog;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.notestore.NoteCollectionCounts;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;

@RestController
public class VocabularyController {

	private Logger logger = LoggerFactory.getLogger(VocabularyController.class);
	
	@Autowired
	private VocabularyDao vocabularyDao;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private PhotoDao photoDao;
	

	/**
	 * 获取单词
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getVocabulary", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getVocabulary(@RequestParam String noteBook,@RequestParam String noteName,
			@RequestParam String account,
			@RequestParam(value = "isRead", required = false, defaultValue="true") Boolean isRead) throws Exception {
		Vocabulary vocabulary=new Vocabulary();
		vocabulary.setNotebook(noteBook);
		vocabulary.setNoteName(noteName);
		if(isRead) {
			List<Vocabulary> vocabularys=vocabularyDao.getAllVocabulary(vocabulary);
			List<Vocabulary> result=new ArrayList<>();
			for(Vocabulary v:vocabularys) {
				Account accountN=accountDao.findAccount(v.getSpell(),account);
				if(accountN==null) {
					result.add(v);
				}
			}
			return BusinessBack.success(result);		
		}else {			
			List<Vocabulary> vocabularys=vocabularyDao.getAllVocabulary(vocabulary);
			return BusinessBack.success(vocabularys);
		}
		
	}
}
