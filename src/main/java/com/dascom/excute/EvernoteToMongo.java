package com.dascom.excute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.dascom.entity.Vocabulary;
import com.dascom.util.DynamicConfUtil;
import com.dascom.util.XmlUtil;
import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteCollectionCounts;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.TException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class EvernoteToMongo {

	private static Logger logger = LoggerFactory.getLogger(EvernoteToMongo.class);

	public static void main(String[] args) throws Exception {
		logger.info("开始导入");

		String server = DynamicConfUtil.getProperty("mongo.host");
		Integer port = Integer.parseInt(DynamicConfUtil.getProperty("mongo.port"));
		String username = DynamicConfUtil.getProperty("mongo.username");
		String password = DynamicConfUtil.getProperty("mongo.password");
		String database = DynamicConfUtil.getProperty("mongo.databaseName");

		ServerAddress address1 = new ServerAddress(server, port);
		MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
		builder.threadsAllowedToBlockForConnectionMultiplier(10);
		
		
		MongoClientOptions mongoClientOptions = builder.build();
		ArrayList<MongoCredential> credentialsList = new ArrayList<MongoCredential>();
		credentialsList.add(MongoCredential.createScramSha1Credential(username, database, password.toCharArray()));
		@SuppressWarnings("deprecation")
		SimpleMongoDbFactory factory1 = new SimpleMongoDbFactory(
				new MongoClient(address1, credentialsList, mongoClientOptions), database);

		DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory1);
		MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
		mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

		MongoTemplate mongoTemplate = new MongoTemplate(factory1, mappingConverter);

		/**
		 * 提取单词部分
		 */
		String developerToken = DynamicConfUtil.getProperty("evernote.token");
		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.YINXIANG, developerToken);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		NoteStoreClient noteStore = factory.createNoteStoreClient();

		List<Notebook> notebooks = noteStore.listNotebooks();

		for (Notebook notebook : notebooks) { //
			//System.out.println(notebook.getStack());
			if (notebook.getStack().equals("英语单词")) { // logger.info(notebook.getGuid());
				logger.info("Notebook: " + notebook.getName());
				if(notebook.getName().equals("初中")) {
					logger.info("初中的不读取了");
					continue;
				}
				if(notebook.getName().equals("高中")) {
					logger.info("高中的不读取了");
					continue;
				}
				/*if(notebook.getName().equals("词组")) {
					logger.info("词组的不读取了");
					continue;
				}*/
				if(notebook.getName().equals("其他")) {
					logger.info("其他的不读取了");
					continue;
				}
				if(notebook.getName().equals("四级")) {
					logger.info("四级的不读取了");
					continue;
				}
				if(notebook.getName().equals("六级")) {
					logger.info("六级的不读取了");
					continue;
				}
				if(notebook.getName().equals("考研")) {
					logger.info("考研的不读取了");
					continue;
				}
				
				NoteFilter filter = new NoteFilter();
				filter.setNotebookGuid(notebook.getGuid());
				NoteCollectionCounts noteCollectionCounts = noteStore.findNoteCounts(filter, false);
				int count = noteCollectionCounts.getNotebookCountsSize();
				NoteList noteList = noteStore.findNotes(filter, 0, 1);
				for (Note note : noteList.getNotes()) {
					logger.info("note: " + note.getTitle());
					
					String a = noteStore.getNoteContent(note.getGuid());
					System.out.println(a);
					Element root = XmlUtil.getRootElement(a);
					List<Element> div = root.elements();

					for (Element d : div) {
						Vocabulary vocabulary = new Vocabulary();
						vocabulary.setNotebook(notebook.getName());
						vocabulary.setNoteName(note.getTitle());
						List<String> type = new ArrayList<String>();
						List<String> answer = new ArrayList<String>();
						String text = d.getText(); // System.out.println(text);
						StringBuffer sb = new StringBuffer();
						int time = 1;
						for (int i = 0; i <= text.length(); i++) {
							if (i == text.length()) { // logger.info("值为:" +sb.toString()); 
								answer.add(sb.toString());
								vocabulary.setType(type);							
								vocabulary.setAnswer(answer);
								break;
							}
							char c = text.charAt(i);
							if (c != ' ' && c != ' ') {
								sb.append(c);

							} else { // logger.info("值为:" + sb.toString()); // logger.info("time为:" +time);
								if (time == 1) { // logger.info("值为:" + sb.toString());
									vocabulary.setSpell(sb.toString());
									time++;
								} else if (time % 2 == 0 && sb.length() != 0) { // logger.info("值为:" + sb.toString());
									type.add(sb.toString());
									time++;
								} else if (time % 2 != 0 && sb.length() != 0) { // logger.info("值为:" + sb.toString());
									answer.add(sb.toString());											
									time++;
								}
								sb = new StringBuffer();

							}
						}
						System.out.println(vocabulary);
						Query query = new Query();
						query.addCriteria(Criteria.where("spell").is(vocabulary.getSpell()));
						Vocabulary vobulary = mongoTemplate.findOne(query, Vocabulary.class, "vacabulary");
						if (vobulary == null) {
							logger.info("插入单词" + vocabulary.getSpell());
							mongoTemplate.insert(vocabulary, "vacabulary");
						} else {
							logger.info("单词" + vocabulary.getSpell() + "已存在");
						}
					}
					logger.info("导入完成");
				}
			}
		}

		/**
		 * 提取单词部分结束
		 */
	}
}
