package com.demo.authentication.filehandling.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.authentication.filehandling.Entity.FileUp;
import com.demo.authentication.filehandling.task.Entity.FormData;

public interface FileRepository extends JpaRepository<FileUp, Integer> {
	
	List<FileUp> findByFormDataId(FormData formDataId);

}
