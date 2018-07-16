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

import com.paulo.cursomc.services.exceptions.FileException;


/**
 * Servico para salvar Arquivos
 * @author Paulo Ferreiro
 *
 */
@Service
public class StorageService {
	
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	// Local (PASTA) onde vai ser Salvo
	private final Path rootLocation = Paths.get("arquivos");
 
	/**
	 * Salvando Aquivo E retornado sua URL
	 * @param MultipartFile - Arquivos para ser salvo
	 * @return URI - Retornar a URL do arquivo salvo
	 */
	public URI store(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			InputStream is = file.getInputStream();
			String contentType = file.getContentType();
			return store(is,fileName,contentType);
		}catch (IOException e){
			throw new FileException("Erro de IO " + e.getMessage());
		}
	}
	
	public URI store(InputStream is, String fileName, String contentType) {
		
			
			try {
				FileSystemUtils.deleteRecursively(this.rootLocation.resolve(fileName));
				Files.copy(is, this.rootLocation.resolve(fileName));
				return loadFile(fileName).getURI();
			} catch (IOException e) {
				throw new FileException("Erro de IO " + e.getMessage());
			}
	
	}
	
	
	public Resource loadFile(String filename) {
		try {
			Path file = rootLocation.resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileException("Erro! Arquivo não existe");
			}
		} catch (MalformedURLException e) {
			throw new FileException("Erro ao converter URL para URI ");
		}
	}
 
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
 
	/**
	 * Iniciar o Diretorio
	 */
	public void init() {
		try {
			if (!Files.exists(rootLocation)) {
				Files.createDirectory(rootLocation);
			}
		} catch (IOException e) {
			throw new FileException("Não Foi possivel criar diretorio");
		}
	}

}
