package com.demo.authentication.filehandling.task.Entity;

import java.util.*;

import com.demo.authentication.filehandling.Entity.FileUp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class FormData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
 
	private String fileFrom;
	private int filesCount;
	private String fileTypes;
	private String fileMessage;
	private char status = '1';
	
	@OneToMany(mappedBy = "formData",cascade = CascadeType.ALL)
	private List<FileUp> fileDatalist = new ArrayList<FileUp>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileFrom() {
		return fileFrom;
	}
	public void setFileFrom(String fileFrom) {
		this.fileFrom = fileFrom;
	}
	public int getFilesCount() {
		return filesCount;
	}
	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}
	public String getFileTypes() {
		return fileTypes;
	}
	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
	}
	public String getFileMessage() {
		return fileMessage;
	}
	public void setFileMessage(String fileMessage) {
		this.fileMessage = fileMessage;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public List<FileUp> getFileDatalist() {
		return fileDatalist;
	}
	public void setFileDatalist(List<FileUp> fileDatalist) {
		this.fileDatalist = fileDatalist;
	}
	
	
	
}
