package com.caveofprogramming.service;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caveofprogramming.exceptions.ImageTooSmallException;
import com.caveofprogramming.exceptions.InvalidFileException;
import com.caveofprogramming.model.FileInfo;

@Service
public class FileService {
	
	@Value("${photo.file.extensions}")
	private String imageExtensions;
	
	private Random random = new Random();
	
	private String getFileExtension(String filename) {
		int dotPosition = filename.lastIndexOf(".");
		
		if(dotPosition < 0) {
			return null;
		}
		
		return filename.substring(dotPosition+1).toLowerCase();
	}
	
	private boolean isImageExtension(String extension) {
		
		String testExtension = extension.toLowerCase();
		
		for(String validExtension: imageExtensions.split(",")) {
			if(testExtension.equals(validExtension)) {
				return true;
			}
		}
		
		return false;
	}
	
	// photo093
	
	private File makeSubdirectory(String basePath, String prefix) {
		int nDirectory = random.nextInt(1000);
		String sDirectory = String.format("%s%03d", prefix, nDirectory);
		
		File directory = new File(basePath, sDirectory);
		
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		return directory;
	}
	
	
	public FileInfo saveImageFile(MultipartFile file, String baseDirectory, String subDirPrefix, String filePrefix, int width, int height) throws InvalidFileException, IOException, ImageTooSmallException {
		int nFilename = random.nextInt(1000);
		String filename = String.format("%s%03d", filePrefix, nFilename);
		
		String extension = getFileExtension(file.getOriginalFilename());
		
		if(extension == null) {
			throw new InvalidFileException("No file extension");
		}
		
		if(isImageExtension(extension) == false) {
			throw new InvalidFileException("Not an image file.");
		}
		
		File subDirectory = makeSubdirectory(baseDirectory, subDirPrefix);
		
		Path filepath = Paths.get(subDirectory.getCanonicalPath(), filename + "." + extension);
		
		BufferedImage resizedImage = resizeImage(file, width, height);
		
		ImageIO.write(resizedImage, extension, filepath.toFile());
		
		return new FileInfo(filename, extension, subDirectory.getName(), baseDirectory);
	}

	private BufferedImage resizeImage(MultipartFile inputFile, int width, int height) throws IOException, ImageTooSmallException {
		BufferedImage image = ImageIO.read(inputFile.getInputStream());
		
		if(image.getWidth() < width || image.getHeight() < height) {
			throw new ImageTooSmallException();
		}
		
		double widthScale = (double)width/image.getWidth();
		double heightScale = (double)height/image.getHeight();
		
		double scale = Math.max(widthScale, heightScale);
		
		BufferedImage scaledImage = new BufferedImage((int)(scale * image.getWidth()),
				(int)(scale * image.getHeight()), image.getType());
		
		Graphics2D g = scaledImage.createGraphics();
		
		AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
		
		g.drawImage(image, transform, null);
		
		return scaledImage.getSubimage(0, 0, width, height);
	}
	
	
	
	
}
