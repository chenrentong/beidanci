package com.dascom.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.dascom.vo.AccountVO;
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
public class AccountController {

	private Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private VocabularyDao vocabularyDao;

	@Value(value = "${start.grade}")
	private Integer grade;

	@Value(value = "${step.grade}")
	private Integer stepGrade;
	
	@Value(value = "${inner.accout}")
	private String[] innerAccount;
	
	@Autowired
	private PhotoDao photoDao;
	
	

	private static final long[] part = { 0,12 * 60 * 60 ,	//12小时
											24 * 60 * 60 , //24小时
												2 * 24 * 60 * 60 ,//2天
													4 * 24 * 60 * 60 ,//4天
														7 * 24 * 60 * 60 , //7天
															15 * 24 * 60 * 60 ,  //15天
			
															30 * 24 * 60 * 60 ,//30天
															2* 30 * 24 * 60 * 60};//60天

	/**
	 * 增加或修改新单词
	 */
	@RequestMapping(value = "/putReVocabulary", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object putReVocabulary(@RequestParam(value = "isHint", required =  false, defaultValue="false")  Boolean isHint,
			@RequestParam String account,@RequestParam String spell,
			@RequestParam(value = "isOk", required =  false, defaultValue="true") Boolean isOk) throws Exception {
		boolean isUse=false;
		for(int i=0;i<innerAccount.length;i++) {
			if(account.equals(innerAccount[i])) {
				isUse=true;
			}
		}
		if(!isUse) {
			return BusinessBack.failed(-1, "该账号不可用");
		}
		
		Account a=accountDao.findAccount(spell,account);
		//System.out.println(a);
		boolean isexist=true;
		if(a==null) {
			//logger.info("账号为空");
			isexist=false;
			a=new Account();
			a.setCreatDate(new Date());
			a.setSpell(spell);
			a.setGrade(grade);
			a.setNextDate(new Date());
			a.setStep(0);
		}
		if(isHint) {
			//提示加分数
			a.setHintcount(a.getHintcount()+1);
			a.setGrade(a.getGrade()+stepGrade);
		}
		
		if(isOk) {
			//正确加步数减分数(不能加到大于7)
			if(a.getGrade()-stepGrade<=0) {
				
				a.setGrade(0);
			}else {
				a.setGrade(a.getGrade()-stepGrade);
			}
			
			if(a.getStep()+1 <= 8){
				//logger.info("step+1");
				if(a.getGrade()>95) {
					
				}else {
					int step=a.getStep()+1;
					//System.out.println(step);
					a.setStep(step);
				}
				
			}

		}else {
			//不正确减步数加分数(不能减到小于1)
			a.setNocount(a.getNocount()+1);
			a.setGrade(a.getGrade()+2*stepGrade);
			if(a.getStep()-2 > 1){
				a.setStep(a.getStep()-2);
			}else {
				a.setStep(1);
			}
		}
		
		a.setAllcount(a.getAllcount()+1);
		long currentTime;
		if(a.getGrade()>100) {
			
		}else {
			 currentTime = System.currentTimeMillis() + part[a.getStep()]*1000;
			 Date nextDate = new Date(currentTime);
			//System.out.println("date:"+a.getStep() +","+nextDate +","+ part[a.getStep()]);
			a.setNextDate(nextDate);
		}
		
		if(isexist) {
			accountDao.updateAccount(a,account);
		}else {
			accountDao.insertAccount(a,account);
		}		
		return BusinessBack.success(a);
	}

	/**
	 * 获取复习单词最近的时间
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getReTime", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getReTime(@RequestParam String account) throws Exception {

		boolean isUse=false;
		for(int i=0;i<innerAccount.length;i++) {
			if(account.equals(innerAccount[i])) {
				isUse=true;
			}
		}
		if(!isUse) {
			return BusinessBack.failed(-1, "该账号不可用");
		}
		//今天以前的,且分数大于100要推,
		String accounts = accountDao.findAccountTime(account);

		return BusinessBack.success(accounts);
	
	}
	
	/**
	 * 获取复习单词
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getReVocabulary", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getReVocabulary(@RequestParam String account) throws Exception {
		boolean isUse=false;
		for(int i=0;i<innerAccount.length;i++) {
			if(account.equals(innerAccount[i])) {
				isUse=true;
			}
		}
		if(!isUse) {
			return BusinessBack.failed(-1, "该账号不可用");
		}
		//今天以前的,且分数大于100要推,
		List<Account> accounts = accountDao.getAllaccount(null,account);
		 //创建SimpleDateFormat对象，指定样式    2019-05-13 22:39:30
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		List<AccountVO> accountVOs=new ArrayList<>();
		for(Account a:accounts) {
			AccountVO accountVO=new AccountVO();
			logger.info(a.getSpell());
			Vocabulary vocabulary=vocabularyDao.findVocabulary(a.getSpell());
			//logger.info(vocabulary.toString());
			accountVO.setAllcount(a.getAllcount());
			accountVO.setAnswer(vocabulary.getAnswer());
			accountVO.setGrade(a.getGrade());
			accountVO.setHintcount(a.getHintcount());
			accountVO.setNextDate(sdf1.format(a.getNextDate()));
			accountVO.setNocount(a.getNocount());
			accountVO.setNotebook(vocabulary.getNotebook());
			accountVO.setNoteName(vocabulary.getNoteName());
			accountVO.setSpell(a.getSpell());
			accountVO.setStep(a.getStep());
			accountVO.setType(vocabulary.getType());
			accountVOs.add(accountVO);
		}
	
		return BusinessBack.success(accountVOs);
	}
	
	/**
	 * 回调测试接口
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object login(@RequestParam String account) throws Exception {
		boolean isUse=false;
		for(int i=0;i<innerAccount.length;i++) {
			if(account.equals(innerAccount[i])) {
				isUse=true;
			}
		}
		if(!isUse) {
			return BusinessBack.failed(-1, "该账号不可用");
		}
		return BusinessBack.success(null);
	}
	
	//----------------------------------------------------------

	/**
	 * 获取复习单词最近的时间
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getRePhotoTime", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getRePhotoTime() throws Exception {

		//今天以前的,且分数大于100要推,
		String accounts = accountDao.findAccountTime("tong2");
		return BusinessBack.success(accounts);
	
	}
	
	/**
	 * 增加或修改新单词
	 */
	@RequestMapping(value = "/putReTopic", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object putReTopic(@RequestParam String dateStr, String subject, String memo, String image,
			@RequestParam(value = "isOk", required =  false, defaultValue="true") Boolean isOk) throws Exception {

		Photo a=photoDao.findPhoto(dateStr,"tong2");
		//System.out.println(a);
		boolean isexist=true;
		if(a==null) {
			//logger.info("账号为空");
			isexist=false;
			a=new Photo();
			a.setMemo(memo);
			a.setSubject(subject);
			a.setImage(image);
			a.setCreatDate(new Date());
			a.setDateStr(dateStr);
			a.setGrade(grade);
			a.setNextDate(new Date());
			a.setStep(0);
		}
		
		if(isOk) {
			//正确加步数减分数(不能加到大于7)
			if(a.getGrade()-stepGrade<=0) {
				
				a.setGrade(0);
			}else {
				a.setGrade(a.getGrade()-stepGrade);
			}
			
			if(a.getStep()+1 <= 8){
				//logger.info("step+1");
				if(a.getGrade()>95) {
					
				}else {
					int step=a.getStep()+1;
					//System.out.println(step);
					a.setStep(step);
				}
				
			}

		}else {
			//不正确减步数加分数(不能减到小于1)
			a.setNocount(a.getNocount()+1);
			a.setGrade(a.getGrade()+2*stepGrade);
			if(a.getStep()-2 > 1){
				a.setStep(a.getStep()-2);
			}else {
				a.setStep(1);
			}
		}
		
		a.setAllcount(a.getAllcount()+1);
		long currentTime;
		if(a.getGrade()>100) {
			
		}else {
			 currentTime = System.currentTimeMillis() + part[a.getStep()]*1000;
			 Date nextDate = new Date(currentTime);
			//System.out.println("date:"+a.getStep() +","+nextDate +","+ part[a.getStep()]);
			a.setNextDate(nextDate);
		}
		
		if(isexist) {
			photoDao.updatePhoto(a,"tong2");
		}else {
			photoDao.insertPhoto(a,"tong2");
		}		
		return BusinessBack.success(a);
	}
	
	/**
	 * 获取复习单词
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getReTopic", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getReTopic() throws Exception {
		
		//今天以前的,且分数大于100要推,
		List<Photo> accounts = photoDao.getAllPhoto(null,"tong2");
		 //创建SimpleDateFormat对象，指定样式    2019-05-13 22:39:30
		 SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 if(accounts==null || accounts.size()==0) {			
			 return BusinessBack.failed(-1, "没有记录");
		 }
		 Photo photo=accounts.get(0);
		 return BusinessBack.success(photo);
		 
	}
}
