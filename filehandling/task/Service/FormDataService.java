package com.demo.authentication.filehandling.task.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.Exception.UncheckedException;
import com.demo.authentication.filehandling.Entity.FileUp;
import com.demo.authentication.filehandling.Service.FileService;
import com.demo.authentication.filehandling.task.Entity.FormData;
import com.demo.authentication.filehandling.task.Repository.FormDataRepository;
import com.demo.authentication.responseHandler.ResponseHandler;

@Service
public class FormDataService {

	@Autowired
	FormDataRepository formDataRepository;

	@Autowired
	FileService fileservice;

	public ResponseEntity<Object> saveform(FormData formdata, MultipartFile[] files) {

		List<FileUp> fileList = fileservice.fileUploadInLocal(files);
		if (fileList != null) {
			for (FileUp file : fileList) {
				file.setFormData(formdata);
			}
		}
		formdata.setFileDatalist(fileList);
		formDataRepository.save(formdata);

		return ResponseHandler.generateResponse("Data Inserted", HttpStatus.OK);
	}

	public ResponseEntity<Object> getAllRecords() {
		List<FormData> formDataList = formDataRepository.findAll();
		return ResponseHandler.generateResponseWithBody(formDataList,
				formDataList.isEmpty() ? "No data found" : "data found", HttpStatus.OK);
	}

	public ResponseEntity<Object> updateFiles(Integer id, MultipartFile[] files) {

		FormData formdata = formDataRepository.findById(id)
				.orElseThrow(() -> new UncheckedException("Data not found with given id "));
		List<FileUp> oldfilelist = formdata.getFileDatalist();

		List<FileUp> newfilelist = fileservice.fileUploadInLocal(files);
		if (newfilelist != null) {
			for (FileUp file : newfilelist) {
				file.setFormData(formdata);
				oldfilelist.add(file);
			}
		}
		formdata.setFileDatalist(oldfilelist);
//		formdata.setFileDatalist(newfilelist);
		formDataRepository.save(formdata);

		return ResponseHandler.generateResponseWithBody(formDataRepository.findById(id), "data inserted",
				HttpStatus.OK);
	}

	public ResponseEntity<Object> getById(Integer id) {
		FormData formdata = formDataRepository.findById(id)
				.orElseThrow(() -> new UncheckedException("Data not found for the given id"));
		return ResponseHandler.generateResponseWithBody(formdata, "Data found", HttpStatus.OK); 
	} 
}
