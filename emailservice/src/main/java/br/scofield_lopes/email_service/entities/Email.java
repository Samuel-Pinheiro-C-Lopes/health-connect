package br.scofield_lopes.email_service.entities;

import java.time.LocalDateTime;

import br.scofield_lopes.email_service.dtos.EmailDto;
import br.scofield_lopes.email_service.enums.EmailStatus;
import jakarta.persistence.*;

@Entity(name = "email_history")
public class Email {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(length = 255, nullable = false)
	private String mailFrom;
	@Column(length = 255, nullable = false)
	private String mailTo;
	@Column(length = 100, nullable = false)
	private String mailSubject;
	@Column(length = 500, nullable = false)
	private String mailBody;
	private LocalDateTime sendAt;
	@Enumerated(EnumType.STRING)
	private EmailStatus status;
	
	public Email() {
		super();
	}
	
	public Email(EmailDto emailDto) {
		this.mailFrom = emailDto.mailFrom();
		this.mailTo = emailDto.mailTo();
		this.mailSubject = emailDto.mailSubject();
		this.mailBody = emailDto.mailBody();
		this.sendAt = LocalDateTime.now();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailBody() {
		return mailBody;
	}

	public void setMailBody(String mailBody) {
		this.mailBody = mailBody;
	}

	public LocalDateTime getSendAt() {
		return sendAt;
	}

	public void setSendAt(LocalDateTime sendAt) {
		this.sendAt = sendAt;
	}

	public EmailStatus getStatus(){
		return this.status;
	}

	public void setStatus(EmailStatus status){
		this.status = status;
	}
}
