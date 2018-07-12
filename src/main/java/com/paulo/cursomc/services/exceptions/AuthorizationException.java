package com.paulo.cursomc.services.exceptions;

/**
 * Exeção se usuario não Atenticado
 * @author Paulo Ferreira
 * @version 1.0
 *
 */
public class AuthorizationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public AuthorizationException(String msg) {
		super(msg);
	}
	
	public AuthorizationException (String msg, Throwable cause) {
		super(msg,cause);
	}

}
