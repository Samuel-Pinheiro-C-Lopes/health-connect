package io.github.samuel_pinheiro_c_lopes.patientservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientRequestDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.dtos.PatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import io.github.samuel_pinheiro_c_lopes.patientservice.repositories.PatientRepository;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.clients.UserClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserBindRequestDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;


@Service
public class PatientService {
	private final PatientRepository patientRepository;
	private final UserClient userClient;

	@Autowired
	public PatientService(final PatientRepository patientRepository, final UserClient userClient) {
		this.patientRepository = patientRepository;
		this.userClient = userClient;
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
		final Patient savedPatient = this.patientRepository.save(userRequest.toPatient());
		
		this.userClient.patch(userRequest.personId(), new PersonBindPatchDTO(savedPatient.getId(), null));
		
		return new PatientResponseDTO(savedPatient);
	}
	
	public PatientResponseDTO update(final Long id, final PatientRequestDTO userRequest) {
		final Patient toBeUpdatedPatient = this.patientRepository.getReferenceById(id);
		
		toBeUpdatedPatient.setCpf(userRequest.cpf());
		toBeUpdatedPatient.setPersonId(userRequest.personId());
		
		final Patient savedPatient = this.patientRepository.save(toBeUpdatedPatient);
		
		this.userClient.patch(userRequest.personId(), new PersonBindPatchDTO(id, null));
		
		return new PatientResponseDTO(savedPatient);
	}
	
	public void delete(final Long id) {
		this.patientRepository.delete(this.patientRepository.getReferenceById(id));
	}

	public void deactivate(Long id) {
		final Patient patient = this.patientRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		
		patient.setAccountStatus(AccountStatus.DISABLED);
		
		this.patientRepository.save(patient);
		
	}
	
	
	private record PersonBindPatchDTO(
		Long patientId,
		Long doctorId
	) implements CommonUserBindRequestDTO { }
}
