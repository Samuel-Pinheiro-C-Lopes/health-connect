package io.github.samuel_pinheiro_c_lopes.doctorservice.broker;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.doctorservice.broker.dtos.PersonBindDTO;

@Service
public class DoctorProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.broker.queue.person.bind}")
    private String queueName;

    public DoctorProducer(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void bindPerson(final Long personId, final Long doctorId) {
        this.sendPersonBind(new PersonBindDTO(personId, doctorId, null));
    }

    private void sendPersonBind(final PersonBindDTO personBind) {
        rabbitTemplate.convertAndSend(queueName, personBind);
    }
}
