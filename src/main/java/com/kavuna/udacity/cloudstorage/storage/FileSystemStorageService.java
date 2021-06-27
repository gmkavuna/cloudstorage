/**
 * The file upload part of this application borrows heavily from  spring-guides/gs-uploading-files
 * https://github.com/spring-guides/gs-uploading-files
 */


package com.kavuna.udacity.cloudstorage.storage;

import com.kavuna.udacity.cloudstorage.model.File;
import com.kavuna.udacity.cloudstorage.service.FileService;
import com.kavuna.udacity.cloudstorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;
	private final FileService fileService;
	private final UserService userService;

	@Autowired
	public FileSystemStorageService(StorageProperties properties, FileService fileService, UserService userService) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.fileService = fileService;
		this.userService = userService;
	}

	@Override
	public String store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				file.getSize();
				throw new StorageException("Failed to store empty file.");
			}
			if (file.getSize() > 1048576000 ){
				throw new StorageException("File is too big. Maximum size allowed is 1048576000 bytes");
			}
			String fileName = new Date().getTime()+"_" + file.getOriginalFilename();
			Path destinationFile = this.rootLocation.resolve(
					Paths.get(fileName))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {

				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}
			return fileName;
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
