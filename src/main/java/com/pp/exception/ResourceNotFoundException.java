package com.pp.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//constructor
	public ResourceNotFoundException (String message) {
		super(message);
	}
	
	
	
}
