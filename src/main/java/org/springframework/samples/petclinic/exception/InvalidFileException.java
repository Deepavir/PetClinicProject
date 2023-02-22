package org.springframework.samples.petclinic.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class InvalidFileException extends RuntimeException{

	public InvalidFileException(String message)
	    {
	        super(message);
	    }
/*
	    public String toString()
	    {
	        return ("Sorry but this system only accepts .png files");
	    } */
}
