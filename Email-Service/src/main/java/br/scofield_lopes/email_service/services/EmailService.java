package br.scofield_lopes.email_service.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import br.scofield_lopes.email_service.dtos.EmailDetailsDto;
import br.scofield_lopes.email_service.dtos.EmailDto;
import br.scofield_lopes.email_service.dtos.ResponseSendEmailDto;
import br.scofield_lopes.email_service.entities.Email;
import br.scofield_lopes.email_service.enums.EmailStatus;
import br.scofield_lopes.email_service.exceptions.EmailException;
import br.scofield_lopes.email_service.repositories.EmailRepository;

@Service
public class EmailService {
	
	private final EmailRepository repository;
	private final JavaMailSender emailSender;
	
	public EmailService(EmailRepository repository, JavaMailSender emailSender) {
		this.repository = repository;
		this.emailSender = emailSender;
	}
	
	public List<EmailDetailsDto> getAll(){
		return this.repository
				.findAll()
				.stream()
				.map(EmailDetailsDto::new)
				.toList();
	}
	
	public ResponseSendEmailDto sendEmail(EmailDto data) throws EmailException {
		this.validateEmail(data);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(data.mailFrom());
		message.setTo(data.mailTo());
		message.setSubject(data.mailSubject());
		message.setText(data.mailBody());		
		emailSender.send(message);
		Email email = new Email(data);
		email.setStatus(EmailStatus.SENT);
		repository.save(email);
		return new ResponseSendEmailDto(EmailStatus.SENT, "E-mail to " + data.mailTo() + " has sent!");
	}

	@RabbitListener(queues = "email.notification")
	public void listenEmailQueue(@Payload EmailDto emailDto) throws EmailException{
		this.sendEmail(emailDto);
	}

	public void saveEmailError(String errorMessage){
		Email emailError = new Email();
		emailError.setMailBody("Error: " + errorMessage);
		emailError.setStatus(EmailStatus.ERROR);
		emailError.setMailSubject("Error in send email");
		emailError.setSendAt(LocalDateTime.now());
		emailError.setMailFrom("error");
		emailError.setMailTo("error");
		repository.save(emailError);
	}

	private void validateEmail(EmailDto data) throws EmailException{
		if(data.mailFrom() == null ||  data.mailFrom().isBlank()) {
			throw new EmailException("Field 'from' cannot be null or empty");
		}
		if(data.mailTo() == null ||  data.mailTo().isBlank()) {
			throw new EmailException("Field 'to' cannot be null or empty");
		}
		if(data.mailSubject() == null ||  data.mailSubject().isBlank()) {
			throw new EmailException("Field 'subject' cannot be null or empty");
		}
		
	}
}
