package br.scofield_lopes.email_service.dtos;

import java.time.LocalDateTime;

import br.scofield_lopes.email_service.entities.Email;
import br.scofield_lopes.email_service.enums.EmailStatus;

public record EmailDetailsDto
(
		long id, 
		String mailFrom, 
		String mailTo, 
		String mailSubject, 
		String mailBody, 
		LocalDateTime mailSendAt,
		EmailStatus status
) {
	public EmailDetailsDto(Email emailModel) {
		this(emailModel.getId(), 
			 emailModel.getMailFrom(), 
			 emailModel.getMailTo(), 
			 emailModel.getMailSubject(), 
			 emailModel.getMailBody(),
			 emailModel.getSendAt(),
			 emailModel.getStatus());
	}
}
