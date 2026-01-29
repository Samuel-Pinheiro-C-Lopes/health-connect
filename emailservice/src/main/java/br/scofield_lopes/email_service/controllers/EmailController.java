package br.scofield_lopes.email_service.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.scofield_lopes.email_service.dtos.EmailDetailsDto;
import br.scofield_lopes.email_service.dtos.EmailDto;
import br.scofield_lopes.email_service.dtos.ResponseSendEmailDto;
import br.scofield_lopes.email_service.exceptions.EmailException;
import br.scofield_lopes.email_service.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	private EmailService emailService;
	
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@GetMapping
	public ResponseEntity<List<EmailDetailsDto>> getAll(){
		return new ResponseEntity<List<EmailDetailsDto>>(this.emailService.getAll(), HttpStatus.OK);
	}

	@PostMapping("send")
	public ResponseEntity<ResponseSendEmailDto> sendEmail(@RequestBody EmailDto data) throws EmailException{
		return new ResponseEntity<ResponseSendEmailDto>(this.emailService.sendEmail(data), HttpStatus.CREATED);
	}
}
