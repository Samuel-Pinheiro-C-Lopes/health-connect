package br.scofield_lopes.email_service.dtos;

import io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos.CommonMailDTO;

public record EmailDto(
		String mailFrom, 
		String mailTo,
		String mailSubject, 
		String mailBody
) implements CommonMailDTO { }
