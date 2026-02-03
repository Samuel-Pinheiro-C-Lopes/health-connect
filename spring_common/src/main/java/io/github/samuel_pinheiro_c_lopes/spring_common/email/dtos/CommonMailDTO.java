package io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos;

public interface CommonMailDTO {
	String mailFrom(); 
	String mailTo(); 
	String mailSubject();
	String mailBody();
}
