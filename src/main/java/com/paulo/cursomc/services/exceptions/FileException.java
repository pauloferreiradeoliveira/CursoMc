package com.paulo.cursomc.services.exceptions;

/**
 * Exeção se usuario não Atenticado
 * @author Paulo Ferreira
 * @version 1.0
 *
 */
public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public FileException(String msg) {
		super(msg);
	}
	
	public FileException (String msg, Throwable cause) {
		super(msg,cause);
	}

}
