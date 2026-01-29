package br.scofield_lopes.email_service.dtos;

public record EmailDto(String mailFrom, String mailTo, String mailSubject, String mailBody) {

}
