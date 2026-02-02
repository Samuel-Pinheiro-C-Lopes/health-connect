package io.github.samuel_pinheiro_c_lopes.userservice.broker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import io.github.samuel_pinheiro_c_lopes.userservice.broker.dtos.PersonBindDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.services.PersonService;

@Component
public class PersonConsumer {
	private final PersonService personService;
	
	@Autowired
	public PersonConsumer(final PersonService personService) {
		this.personService = personService;
	}

	@RabbitListener(queues = "${app.broker.queue.person.bind}")
    public void receberMensagem(@Payload PersonBindDTO personBind) {
        this.personService.bindPerson(personBind);
    }
}
