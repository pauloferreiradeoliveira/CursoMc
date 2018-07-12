package com.paulo.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final Path rootLocation = Paths.get("arquivos");
 
	/**
	 * Salvando Aquivo E retornado sua URL
	 * */
	public URI store(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			String contentType = file.getContentType();
			return store(is,fileName,contentType);
		}catch (IOException e){
			throw new RuntimeException("Erro de IO " + e.getMessage());
		}
	}
	
	public URI store(InputStream is, String fileName, String contentType) {
		try {
			Files.copy(is, this.rootLocation.resolve(fileName));
			return loadFile(fileName).getURI();
		} catch (Exception e) {
			throw new RuntimeException("Falha ao upload");
		}
	}
 
	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Erro! Arquivo não EXITE");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Erro! Não achou a URL ");
		}
	}
 
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
 
	public void init() {
		try {
			Files.createDirectory(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Não Foi possivel criar diretorio");
		}
	}

}
