package com.artichourey.ecommerce.productservice.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
public class GlobalExceptionHandler {
	
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String,Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
		Map<String,Object> m= Map.of( "error", "Not Found",
			    "message", ex.getMessage(),
			    "timestamp", Instant.now());
		
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
		
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,Object>> handleMethodNotValidException(MethodArgumentNotValidException ex){
		Map<String,Object>error=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err->error.put(err.getField(), err.getDefaultMessage()));
		Map<String,Object> body=Map.of("errors","Validation Failed",
				"details",error,
				"timestamp", Instant.now());
		
		return ResponseEntity.badRequest().body(body);
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,Object>> handleAllException(Exception ex){
		Map<String,Object>m=Map.of("error","Internal Server Error",
				"message", ex.getMessage(),
				"timestamp", Instant.now());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m);
		
	}

}
