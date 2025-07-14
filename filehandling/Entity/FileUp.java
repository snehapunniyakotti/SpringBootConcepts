package com.demo.authentication.filehandling.Entity;

import java.sql.Date;

import com.demo.authentication.filehandling.task.Entity.FormData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class FileUp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private String location;
	private String date;
	private char status = '1';
	private String extention;
	private String ogname;
	
	@ManyToOne
	@JoinColumn(name="fod_id",nullable = true)
	@JsonIgnore
	private FormData formData;

//	@Lob  
//	private byte[] data;

	public FileUp() {
		super();
		
	}

	public FileUp(String name, String location, String date, String extention, String ogname) {
		super();
		this.name = name;
		this.location = location;
		this.date = date;
		this.extention = extention;
		this.ogname = ogname;
	}

//	public FileUp(String name, String location, String date, String extention, String ogname, byte[] data) {
//		super();
//		this.name = name;
//		this.location = location;
//		this.date = date;
//		this.extention = extention;
//		this.ogname = ogname;
//		this.data = data;
//	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getExtention() {
		return extention;
	}

	public void setExtention(String extention) {
		this.extention = extention;
	}

	public String getOgname() {
		return ogname;
	}

	public void setOgname(String ogname) {
		this.ogname = ogname;
	}

//	public byte[] getData() {
//		return data;
//	}
//
//	public void setData(byte[] data) {
//		this.data = data;
//	}

	
	
	@Override
	public String toString() {
		return "FileUp [id=" + id + ", name=" + name + ", location=" + location + ", date=" + date + ", status="
				+ status + ", extention=" + extention + ", ogname=" + ogname + "]";
	}

	public FormData getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}

}
