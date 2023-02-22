package org.springframework.samples.petclinic.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(InvalidFileException.class)
	public String handleInvalidFileException(InvalidFileException e) {
		String message = e.getMessage();
		return message;
	}
	@ExceptionHandler(ImageSizeException.class)
	public String handleImageSizeException(ImageSizeException e){
		String message = e.getMessage();
		return message;
	}
}