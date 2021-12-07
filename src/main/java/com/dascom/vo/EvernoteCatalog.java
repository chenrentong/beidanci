package com.dascom.vo;

import java.util.Date;
import java.util.List;

import com.evernote.edam.type.Note;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EvernoteCatalog {
	
	private String notebook;
	
	private List<Note>  note;
}
