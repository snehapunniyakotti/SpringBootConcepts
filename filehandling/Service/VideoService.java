package com.demo.authentication.filehandling.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;


@Service
public class VideoService {
	
	@Value("${file.vcompressor-dir}") 
	private String VFILE_DIR;
	
	public File videoCompressor(MultipartFile videoFile) throws IOException {
		
		File dir = new File(VFILE_DIR);
		if(!dir.exists()) {
			dir.mkdir();
		}
		File tempInputFile = File.createTempFile(VFILE_DIR+"og_"+videoFile.getOriginalFilename(), ".mp4");
		videoFile.transferTo(tempInputFile);
		
		File compressedFile = File.createTempFile(VFILE_DIR+"compressed_"+videoFile.getOriginalFilename(), ".mp4");
		
		FFmpeg ffmpeg = new FFmpeg(VFILE_DIR);
		FFprobe ffprobe = new FFprobe(VFILE_DIR);
		
		FFmpegBuilder ffmpegBuilder = new FFmpegBuilder()
				.setInput(tempInputFile.getAbsolutePath())
				.overrideOutputFiles(true) 
				.addOutput(compressedFile.getAbsolutePath())
				.setVideoCodec("libx264")
				.setVideoFrameRate(24)          
                .setVideoResolution(640, 480) 
				.setConstantRateFactor(28.0)
				.setAudioCodec("aac")
				.setAudioBitRate(96000)
				.done();
		
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		executor.createJob(ffmpegBuilder).run();
//		ffmpeg.run(ffmpegBuilder);
		
		tempInputFile.delete();
		return compressedFile;  
	}
	
	 public File compressVideo3(MultipartFile videoFile) throws VideoException, IOException {
		 IVCompressor compressor = new IVCompressor();
//		 IVSize cusResolution = new IVSize();
		 
//		 cusResolution.setWidth(720);
//		 cusResolution.setHeight(480);
		 
	     File file = new File(VFILE_DIR+videoFile.getOriginalFilename());
	     videoFile.transferTo(file);
	     
//	     File resfile = new File(VFILE_DIR+"compressed_"+videoFile.getOriginalFilename());
	     
	     compressor.reduceVideoSizeAndSaveToAPath(file, VideoFormats.MP4, ResizeResolution.R480P, VFILE_DIR);
	     return file;
		  
	 }

}











