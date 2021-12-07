package com.dascom.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
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
public class EvernoteController {

	private Logger logger = LoggerFactory.getLogger(EvernoteController.class);

	
	

	/**
	 * 获取印象笔记的目录
	 * 
	 * @param loadBalance
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getEvernote", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getEvernote() throws Exception {
		String developerToken = DynamicConfUtil.getProperty("evernote.token");
		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.YINXIANG, developerToken);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		NoteStoreClient noteStore = factory.createNoteStoreClient();

		List<Notebook> notebooks = noteStore.listNotebooks();

		List<EvernoteCatalog> noteBack=new ArrayList<>();
		
		for (Notebook notebook : notebooks) { 
			if (notebook.getStack().equals("英语单词")) { 
				EvernoteCatalog catalog=new EvernoteCatalog();
				catalog.setNotebook(notebook.getName());
				NoteFilter filter = new NoteFilter();
				filter.setNotebookGuid(notebook.getGuid());
				
				//NoteCollectionCounts noteCollectionCounts = noteStore.findNoteCounts(filter, false);
				//int count = noteCollectionCounts.getNotebookCountsSize();
				//System.out.println(count);
				NoteList noteList = noteStore.findNotes(filter, 0, 100);
				catalog.setNote(noteList.getNotes());
				noteBack.add(catalog);
			}
		}
		return BusinessBack.success(noteBack);

	}
	
	/**
	 * 预览笔记功能
	 */
	@RequestMapping(value = "/preNote", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	public Object getEvernote(@RequestParam String guid) throws Exception {
		String developerToken = DynamicConfUtil.getProperty("evernote.token");
		EvernoteAuth evernoteAuth = new EvernoteAuth(EvernoteService.YINXIANG, developerToken);
		ClientFactory factory = new ClientFactory(evernoteAuth);
		NoteStoreClient noteStore = factory.createNoteStoreClient();
		String a = noteStore.getNoteContent(guid);
		return BusinessBack.success(a);
	}
	
	
}
