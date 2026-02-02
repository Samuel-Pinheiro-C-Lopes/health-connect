package io.github.samuel_pinheiro_c_lopes.patientservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.patientservice.clients.UserserviceClient;
import io.github.samuel_pinheiro_c_lopes.patientservice.clients.dtos.PersonResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.broker.PatientProducer;
import io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos.PatientDetailsResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos.PatientRequestDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.controllers.dtos.PatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.patientservice.models.Patient;
import io.github.samuel_pinheiro_c_lopes.patientservice.repositories.PatientRepository;


@Service
public class PatientService {
	private final PatientRepository patientRepository;
	private final PatientProducer patientProducer;
	private final UserserviceClient userserviceClient;

	@Autowired
	public PatientService(
			final PatientRepository patientRepository,
			final PatientProducer patientProducer,
			final UserserviceClient userserviceClient
	) {
		this.patientRepository = patientRepository;
		this.patientProducer = patientProducer;
		this.userserviceClient = userserviceClient;
	}
	
	public PatientResponseDTO save(final PatientRequestDTO userRequest) {
		final Patient savedPatient = this.patientRepository.save(userRequest.toPatient());
		
		this.patientProducer.bindPerson(savedPatient.getPersonId(), savedPatient.getId());
		
		return new PatientResponseDTO(savedPatient);
	}
	
	public List<PatientResponseDTO> findAll() {
		return this.patientRepository.findAll()
				.stream()
				.map(u -> new PatientResponseDTO(u))
				.toList();
	}

	public PatientDetailsResponseDTO findDetailsById(final Long id) {
		final Patient patient = this.patientRepository.getReferenceById(id);
		final PersonResponseDTO person = this.userserviceClient.findPersonById(patient.getPersonId());
		return new PatientDetailsResponseDTO(new PatientResponseDTO(patient), person);
	}
	
	public PatientResponseDTO update(final Long id, final PatientRequestDTO userRequest) {
		final Patient toBeUpdatedPatient = this.patientRepository.getReferenceById(id);
		
		toBeUpdatedPatient.setCpf(userRequest.cpf());
		toBeUpdatedPatient.setPersonId(userRequest.personId());
		
		final Patient savedPatient = this.patientRepository.save(toBeUpdatedPatient);
		
		this.patientProducer.bindPerson(savedPatient.getPersonId(), savedPatient.getId());
		
		return new PatientResponseDTO(savedPatient);
	}
	
	public void delete(final Long id) {
		this.patientRepository.delete(this.patientRepository.getReferenceById(id));
	}
}
