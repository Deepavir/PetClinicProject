package org.springframework.samples.petclinic.exception;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/*@ControllerAdvice
public class OwnerException {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<String>();
		
		for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getMessage());
			
		}
		//rors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath()

		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,errors);
		//return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);

		// If you want to throw apiError directly, uncomment this
		return  ResponseEntity.ok(apiError);
	
		
		
}
} */