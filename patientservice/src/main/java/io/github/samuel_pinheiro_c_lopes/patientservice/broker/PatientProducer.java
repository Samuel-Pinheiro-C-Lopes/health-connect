package io.github.samuel_pinheiro_c_lopes.patientservice.broker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.patientservice.broker.dtos.PersonBindDTO;

@Service
public class PatientProducer {
	private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.queue.person.bind}")
    private String queueName;

    public PatientProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void bindPerson(Long personId, Long patientId) {
    	this.sendPersonBind(new PersonBindDTO(personId, null, patientId));
    }

    private void sendPersonBind(PersonBindDTO personBind) {
        rabbitTemplate.convertAndSend(queueName, personBind);
    }
}
