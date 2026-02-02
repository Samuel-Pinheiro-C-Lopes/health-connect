package io.github.samuel_pinheiro_c_lopes.doctorservice.broker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.github.samuel_pinheiro_c_lopes.doctorservice.broker.dtos.EmailDto;
@Component
public class EmailNotificationProducer {
	private final RabbitTemplate rabbitTemplate;
	@Value("${app.email.from:no-reply@health-connect.com}")
	private String defaultMailFrom;

	@Autowired
	public EmailNotificationProducer(final RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void sendStatusUpdateEmail(final String mailTo, final String subject, final String body) {
		final EmailDto emailDto = new EmailDto(defaultMailFrom, mailTo, subject, body);
		this.rabbitTemplate.convertAndSend("email.notification", emailDto);
	}
}