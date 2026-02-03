package br.scofield_lopes.email_service.exceptions;

import br.scofield_lopes.email_service.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.scofield_lopes.email_service.dtos.ResponseSendEmailDto;
import br.scofield_lopes.email_service.enums.EmailStatus; 	

@RestControllerAdvice
public class GlobalExceptionHandler {

	private final EmailService emailService;

	public GlobalExceptionHandler(EmailService emailService){
		this.emailService = emailService;
	}

	@ExceptionHandler(EmailException.class)
		public ResponseEntity<ResponseSendEmailDto> handleEmailBadRequest(EmailException ex){
		ResponseSendEmailDto response = new ResponseSendEmailDto(EmailStatus.ERROR, ex.getMessage());
		this.emailService.saveEmailError(ex.getMessage());
		return new ResponseEntity<ResponseSendEmailDto>(response, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleInternalServerError(Exception ex) {
		this.emailService.saveEmailError(ex.getMessage());
		return new ResponseEntity<String>("Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
