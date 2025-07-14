
package com.demo.authentication.filehandling.Service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DirectoryInitializer {
	
	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;
	
//	@PostConstruct
	public void init() {
		File dir = new File(FILE_DIRECTORY);
		if(!dir.exists()) {
			dir.mkdir();
		}
	}

}
