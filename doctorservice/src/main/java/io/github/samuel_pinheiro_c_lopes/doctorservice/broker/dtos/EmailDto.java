package io.github.samuel_pinheiro_c_lopes.doctorservice.broker.dtos;

/**
 * DTO compatível com o consumidor do emailservice (fila: email.notification).
 */
public record EmailDto(
		String mailFrom,
		String mailTo,
		String mailSubject,
		String mailBody
) { }
