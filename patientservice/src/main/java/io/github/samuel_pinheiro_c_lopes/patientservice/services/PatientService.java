package io.github.samuel_pinheiro_c_lopes.patientservice.services;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientRequestDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import io.github.samuel_pinheiro_c_lopes.patientservice.repositories.PatientRepository;
import io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos.CommonMailDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.clients.UserClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserBindRequestDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PatientService {
	private final PatientRepository patientRepository;
	private final UserClient userClient;
	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public PatientService(
			final PatientRepository patientRepository, 
			final UserClient userClient,
			final RabbitTemplate rabbitTemplate
	) {
		this.patientRepository = patientRepository;
		this.userClient = userClient;
		this.rabbitTemplate = rabbitTemplate;
	}

	public PatientFullResponseDTO getFullResponseFrom(final Patient patient) {
		final CommonUserResponseDTO person = this.userClient.findById(patient.getPersonId());
		
		return new PatientFullResponseDTO(
			patient.getId(),
			person.id(),
			person.name(),
			person.email(),
			person.phone(),
			person.postalCode(),
			person.avenue(),
			person.complement(),
			person.number(),
			person.city(),
			person.district(),
			person.state()
		);
	}
	
	public List<PatientFullResponseDTO> findAll() {
		return this.patientRepository.findAll()
				.stream()
				.map(this::getFullResponseFrom)
				.toList();
	}
	
	public List<PatientFullResponseDTO> findAllActive() {
		return this.patientRepository.findAllByAccountStatus(AccountStatus.ACTIVE)
				.stream()
				.map(this::getFullResponseFrom)
				.toList();
	}
	
	public PatientFullResponseDTO findById(Long id) {
		return this.patientRepository.findById(id).map(this::getFullResponseFrom).orElseThrow(() -> new EntityNotFoundException());
	}
	
	public PatientResponseDTO save(final PatientRequestDTO userRequest) {
		final Patient toSavePatient = userRequest.toPatient();
		
		toSavePatient.setAccountStatus(AccountStatus.ACTIVE);
		
		final Patient savedPatient = this.patientRepository.save(toSavePatient);
		
		this.userClient.patch(userRequest.personId(), new CommonUserBindRequestDTO(savedPatient.getId(), null));
		
		this.sendAccountUpdateTo(savedPatient);
		
		return new PatientResponseDTO(savedPatient);
	}
	
	private void sendAccountUpdateTo(final Patient patient) {
        final CommonUserResponseDTO patientUser = this.userClient.findByPatientId(patient.getId());
        
		rabbitTemplate.convertAndSend("email.notification", new CommonMailDTO(
            	"healthconnectpweb@gmail.com",
            	patientUser.email(),
            	"Status da conta alterada!",
            	"Sr(a). " + patientUser.name() + 
            	", sua conta teve seu estado alterado!\n Estado atual:: " + 
            		patient.getAccountStatus().getMessage()
		));
	}
	
	public PatientResponseDTO update(final Long id, final PatientRequestDTO userRequest) {
		final Patient toBeUpdatedPatient = this.patientRepository.getReferenceById(id);
		
		toBeUpdatedPatient.setCpf(userRequest.cpf());
		toBeUpdatedPatient.setPersonId(userRequest.personId());
		
		final Patient savedPatient = this.patientRepository.save(toBeUpdatedPatient);
		
		this.userClient.patch(userRequest.personId(), new CommonUserBindRequestDTO(id, null));
		
		return new PatientResponseDTO(savedPatient);
	}
	
	public void delete(final Long id) {
		this.patientRepository.delete(this.patientRepository.getReferenceById(id));
	}

	public void deactivate(Long id) {
		final Patient patient = this.patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		
		patient.setAccountStatus(AccountStatus.DISABLED);
		
		this.patientRepository.save(patient);
		
		this.sendAccountUpdateTo(patient);
	}
}
