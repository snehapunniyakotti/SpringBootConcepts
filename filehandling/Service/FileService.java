package com.demo.authentication.filehandling.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.demo.authentication.Exception.UncheckedException;
import com.demo.authentication.controller.CustomAuthenticationSuccessHandler;
import com.demo.authentication.filehandling.Entity.FileUp;
import com.demo.authentication.filehandling.Repository.FileRepository;
import com.demo.authentication.filehandling.task.Entity.FormData;
import com.demo.authentication.filehandling.task.Repository.FormDataRepository;
import com.demo.authentication.responseHandler.ResponseHandler;

import jakarta.mail.Multipart;

@Service
@Transactional
public class FileService {

	@Value("${file.upload-dir}")
	String FILE_DIRECTORY;

	@Value("${file.zip-dir}")
	String FILE_ZIP_DIR;

	@Value("${file.unzip-dir}")
	String FILE_UNZIP_DIR;

	private List<FileUp> fileList; 

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FormDataRepository formDataRepository;

	// file upload service
	// -----------------------------------------------------------------------------

	public ResponseEntity<Object> fileUploadService(MultipartFile file) throws IOException {

		File dir = new File(FILE_DIRECTORY);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String randomname = Integer.toString(new Random().nextInt(10000000));

		String res = uploadFileInLocalDir(file, randomname);
		if (!(res.isEmpty())) {
			return ResponseHandler.generateResponse(res, HttpStatus.OK);
		}

		String name = file.getOriginalFilename();
		int dotindex = name.indexOf('.');
		String ogname = name.substring(0, dotindex);
		String extention = name.substring(dotindex, name.length());

		FileUp f = new FileUp(randomname, FILE_DIRECTORY, getCurDateTime(), extention, ogname);

		System.out.println("printing the file : " + f.toString());

		return uploadFileInDB(f);
	}

	public ResponseEntity<Object> multiFileUploadService(MultipartFile[] files) {

		File dir = new File(FILE_DIRECTORY);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String res = multiFileUploadInLocalDir(files);
		if (!(res.isEmpty())) {
			return ResponseHandler.generateResponse(res, HttpStatus.OK);
		}

		return multiFileUploadInDB(fileList);

	}

	// save file in directory
	// ---------------------------------------------------------------------------

	public String multiFileUploadInLocalDir(MultipartFile[] files) {
		try {
			fileList = new ArrayList<FileUp>();
			for (MultipartFile file : files) {
				String randomname = Integer.toString(new Random().nextInt(10000000));

				File myfile = new File(FILE_DIRECTORY + randomname);
				myfile.createNewFile();
				FileOutputStream fos = new FileOutputStream(myfile);
				fos.write(file.getBytes());
				fos.close();

				String name = file.getOriginalFilename();

				int dotindex = name.indexOf('.');
				String ogname = name.substring(0, dotindex);
				String extention = name.substring(dotindex, name.length());

				fileList.add(new FileUp(randomname, FILE_DIRECTORY, getCurDateTime(), extention, ogname));

			}
		} catch (Exception e) {
			return e.toString();
		}
		return "";
	}

	public String uploadFileInLocalDir(MultipartFile file, String name) {
		try {
			File myfile = new File(FILE_DIRECTORY + name);
			myfile.createNewFile();
			FileOutputStream fos = new FileOutputStream(myfile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		}
		return "";
	}

	// save file in db
	// --------------------------------------------------------------------------------

	public ResponseEntity<Object> uploadFileInDB(FileUp f) {
		fileRepository.save(f);
		return ResponseHandler.generateResponse("file Saved", HttpStatus.OK);
	}

	public ResponseEntity<Object> multiFileUploadInDB(List<FileUp> files) {
		fileRepository.saveAll(files);
		return ResponseHandler.generateResponse("Files Uploaded", HttpStatus.OK);
	}

	// get current date and time
	// --------------------------------------------------------------------------------------

	public String getCurDateTime() {
		Date currentDateTime = new Date();
		SimpleDateFormat formatedDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String datetime = formatedDateTime.format(currentDateTime);
		return datetime;
	}

	// get file by id / file download
	// -----------------------------------------------------
	public ResponseEntity<Resource> getFileById(Integer id) {
		try {
			FileUp f = fileRepository.findById(id)
					.orElseThrow(() -> new UncheckedException("File Not Found for this Id"));

			Path fileStorageLocation = Paths.get(f.getLocation());
			Path filePath = fileStorageLocation.resolve(f.getName()).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.notFound().build();
			}
//				return ResponseEntity.ok().body(resource); 
//				return ResponseHandler.generateResponseWithBody(resource,"S", HttpStatus.OK); 
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment; filename=\"" + f.getOgname() + f.getExtention() + "\"")
					.body(resource);

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}

	}

	// --------------------------------------------------------------------------------------------------------------
	// save files in local dir and return list of filelist
	public List<FileUp> fileUploadInLocal(MultipartFile[] files) {
		String res = multiFileUploadInLocalDir(files);
		if (!(res.isEmpty())) {
			throw new UncheckedException(res);
		}
		return fileList;
	}

	// zip Files
	// -----------------------------------------------------------------------------
	public ResponseEntity<Object> zipFile(MultipartFile file) throws Exception {

		File dir = new File(FILE_ZIP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File fileToZip = new File(FILE_ZIP_DIR + "og_" + file.getOriginalFilename());
		file.transferTo(fileToZip);
		String filename = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));

		FileOutputStream fos = new FileOutputStream(FILE_ZIP_DIR + filename + ".zip");
		ZipOutputStream zos = new ZipOutputStream(fos);

		FileInputStream fis = new FileInputStream(fileToZip.getAbsolutePath());
		ZipEntry zipEntry = new ZipEntry(fileToZip.getName()); // zipEntry la enna kudukaramo adhutha zip aagum
//		ZipEntry zipEntry = new ZipEntry(fileToZip.getAbsolutePath());
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int len;
		while ((len = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, len);
		}
		fis.close();
		zos.close();
		fos.close();
		return ResponseHandler.generateResponse("File ziped", HttpStatus.OK);
	}

	// zip multiple files
	public ResponseEntity<Object> zipMultipleFile(MultipartFile[] files) {
		for (MultipartFile file : files) {
			try {
				zipFile(file); 
			} catch (Exception e) {
				return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return ResponseHandler.generateResponse("Files ziped", HttpStatus.OK);
	}

	// unzip file
	public ResponseEntity<Object> unzipFile(MultipartFile file) throws IllegalStateException, IOException {

		File dir = new File(FILE_UNZIP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File fileToUnZip = new File(FILE_UNZIP_DIR + "og_" + file.getOriginalFilename());
		file.transferTo(fileToUnZip);

		FileInputStream fis = new FileInputStream(fileToUnZip.getAbsolutePath());
		ZipInputStream zis = new ZipInputStream(fis);

		ZipEntry zipEntry = zis.getNextEntry();
		System.out.println("zipEntry checking 0: " + zipEntry);
		while (zipEntry != null) {

			String fileName = zipEntry.getName();
			File newFile = new File(
					FILE_UNZIP_DIR + File.separator + fileName.substring(fileName.indexOf('_') + 1, fileName.length()));

			byte[] buffer = new byte[1024];
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			System.out.println("zipEntry checking 1 : " + zipEntry);
			fos.close();
			zis.closeEntry();
			zipEntry = zis.getNextEntry();
			System.out.println("zipEntry checking  2: " + zipEntry);
		}
		zis.closeEntry();
		zis.close();
		fis.close();

		return ResponseHandler.generateResponse("file unZiped successfull", HttpStatus.OK);

	}

	// unzip multiple file
	public ResponseEntity<Object> unzipMultipleFile(MultipartFile[] files) {
		for (MultipartFile file : files) {
			try {
//				unzipFile(file); // this only unzip the files 
				unzipNestedFoldersandFiles(file); // this can unzip both files and floders
			} catch (Exception e) {
				return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return ResponseHandler.generateResponse("Files unzipped successfully", HttpStatus.OK);
	}

	// ---------------------------------------------------------------------------------------------------------------

	// unzip folders and files nested
	public ResponseEntity<Object> unzipNestedFoldersandFiles(MultipartFile file)
			throws IllegalStateException, IOException {
		File dir = new File(FILE_UNZIP_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File fileToUnzip = new File(FILE_UNZIP_DIR + "og_" + file.getOriginalFilename());
		file.transferTo(fileToUnzip);

		String name = fileToUnzip.getName();
		String MasterFlodername = name.substring(0, name.indexOf("."));

		File destinationDir = new File(FILE_UNZIP_DIR + MasterFlodername);
		if (!destinationDir.exists()) {
			destinationDir.mkdir();
		}

		FileInputStream fis = new FileInputStream(fileToUnzip.getAbsolutePath());
		ZipInputStream zis = new ZipInputStream(fis);

		ZipEntry zipEntry = zis.getNextEntry();

		if (!zipEntry.isDirectory()) {
			destinationDir = new File(FILE_UNZIP_DIR);
		}
		while (zipEntry != null) {
			String fileName = zipEntry.getName();
			File newFile = new File(
					destinationDir + File.separator + fileName.substring(fileName.indexOf('_') + 1, fileName.length()));

			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdir()) {
					throw new UncheckedException("Failed to create directory " + newFile);
				}
			} else {
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdir()) {
					throw new UncheckedException("Failed to create directory " + parent);
				}

				FileOutputStream fos = new FileOutputStream(newFile);
				byte[] bytes = new byte[1024];
				int len;
				while ((len = zis.read(bytes)) > 0) {
					fos.write(bytes, 0, len);
				}
				fos.close();

			}
			zis.closeEntry();
			zipEntry = zis.getNextEntry();
		}
		fis.close();
		zis.close();

		return ResponseHandler.generateResponse(" unZipped successfully", HttpStatus.OK);
	}

}
