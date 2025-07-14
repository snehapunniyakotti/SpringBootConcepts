package com.demo.authentication.filehandling.Service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.demo.authentication.jpql.Controller.SalesController;
import com.demo.authentication.responseHandler.ResponseHandler;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

	@Value("${file.compress-dir}")
	String FILE_DIRECTORE;

	@Value("${file.lib-dir}")
	String FILE_DIR_LIB;

	@Value("${file.converter-dir}")
	String FILE_CON_DIR;

	public void imageCompress(MultipartFile file, String outputPath, float compressQuality)
			throws IllegalStateException, IOException {
		File dir = new File(FILE_DIRECTORE);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File imageFile = new File(FILE_DIRECTORE + "original_" + file.getOriginalFilename());
		file.transferTo(imageFile);
		File compressedImageFile = new File(FILE_DIRECTORE + "compressed_" + file.getOriginalFilename());
		File reduceResolutionImage = new File(FILE_DIRECTORE + "lessResolution_" + file.getOriginalFilename());

		compress(imageFile, compressedImageFile, reduceResolutionImage, compressQuality);

//		BufferedImage image = ImageIO.read(imageFile);
//		int num = image.getWidth() / 800 ;
//		int newHeight = image.getHeight() / num ;
//		int newWidth = image.getWidth() / num;
//		reduceImageResolution(imageFile.getPath(), compressedImageFile.getPath(), newWidth, newHeight);

		long oglen = imageFile.length();
		long newlen = compressedImageFile.length();

		System.out.println("oglen :" + oglen);
		System.out.println("compressed len :" + newlen);
	}

	public void compress(File imageFile, File compressedImageFile, File reduceResolutionImage, float compressQuality)
			throws IOException {

//		reduceImageResolution(imageFile.getPath(), reduceResolutionImage.getPath(), 800, 600);

		BufferedImage image = ImageIO.read(imageFile);
		
		int maxsize = Math.max(image.getHeight(), image.getWidth());
		double num = maxsize / 1280.00;
		int newHeight = (int) (image.getHeight() / num); 
		int newWidth = (int) (image.getWidth() / num);
		reduceImageResolution(imageFile.getPath(), reduceResolutionImage.getPath(), newWidth, newHeight);

		BufferedImage image2 = ImageIO.read(reduceResolutionImage);

		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter writer = writers.next();

		ImageOutputStream ios = ImageIO.createImageOutputStream(new FileOutputStream(compressedImageFile));
		writer.setOutput(ios);

		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(compressQuality);

		writer.write(null, new IIOImage(image2, null, null), param);

		ios.close();
		writer.dispose();

//		int num = image.getWidth() / 800;
//		int newHeight = image.getHeight() / num;
//		int newWidth = image.getWidth() / num;
//		reduceImageResolution(imageFile.getPath(), compressedImageFile.getPath(), newWidth, newHeight);

//		reduceResolution(imageFile, compressedImageFile, 800, 600);
	}

	public static void reduceImageResolution(String inputImagePath, String outputImagePath, int newWidth, int newHeight)
			throws IOException {
		File inputFile = new File(inputImagePath);
		BufferedImage originalImage = ImageIO.read(inputFile);

		// Resize the image
		Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage outputImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

		// Draw the resized image onto the output image
		Graphics2D graphics2D = outputImage.createGraphics();
		graphics2D.drawImage(resizedImage, 0, 0, null);
		graphics2D.dispose(); 

		// Write the output image
		File outputFile = new File(outputImagePath);
		String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1); 
		System.out.println("formatName in reduce resolution : " + formatName);
		ImageIO.write(outputImage,formatName, outputFile);
	}

	public void usingThumbnailatorLibrary(MultipartFile file) throws IOException {

		File dir = new File(FILE_DIR_LIB);
		if (!dir.exists()) {
			dir.mkdir();
		}

		File inputFile = new File(FILE_DIR_LIB + "og_" + file.getOriginalFilename());
		file.transferTo(inputFile);
		File outputFile = new File(FILE_DIR_LIB + "Compressed_" + file.getOriginalFilename());

		imageCompresslibrary(inputFile, outputFile, 0.4f);
	}

	public void imageCompresslibrary(File inputFile, File outputFile, float quantity) throws IOException {
		Thumbnails.of(inputFile)
//		.scale(1)
				.size(800, 600).outputQuality(quantity).toFile(outputFile);
	}

	public void imageConverter(String inputImgPath, String outputImgPath, String formatName) throws IOException {
		File ogFile = new File(inputImgPath);
		BufferedImage ogImage = ImageIO.read(ogFile);

		File newFile = new File(outputImgPath);
		System.out.println("formatName in converter : " + formatName);
		ImageIO.write(ogImage, formatName, newFile); 

	}

	public void converter(MultipartFile file, String formatName) throws IllegalStateException, IOException {

		File dir = new File(FILE_CON_DIR);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String filePath = file.getOriginalFilename();

		File inputFile = new File(FILE_CON_DIR + "og_" + filePath);
		file.transferTo(inputFile);

		String fileName = filePath.substring(0, filePath.lastIndexOf('.')) + "." + formatName;
		File outputFile = new File(FILE_CON_DIR + "res_" + fileName);

		imageConverter(inputFile.getPath(), outputFile.getPath(), formatName);

	}
	

}
