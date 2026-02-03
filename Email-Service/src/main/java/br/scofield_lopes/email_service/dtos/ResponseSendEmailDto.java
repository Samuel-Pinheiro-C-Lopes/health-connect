package br.scofield_lopes.email_service.dtos;

import br.scofield_lopes.email_service.enums.EmailStatus;

public record ResponseSendEmailDto(EmailStatus status, String message) {

	public ResponseSendEmailDto(EmailStatus status, String message) {
		this.status = status;
		this.message = message;
	}
	
}
