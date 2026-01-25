package io.github.samuel_pinheiro_c_lopes.userservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.samuel_pinheiro_c_lopes.userservice.models.Person;
import io.github.samuel_pinheiro_c_lopes.userservice.broker.dtos.PersonBindDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.PersonRequestDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.controllers.dtos.PersonResponseDTO;
import io.github.samuel_pinheiro_c_lopes.userservice.models.Address;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.PersonRepository;
import io.github.samuel_pinheiro_c_lopes.userservice.repositories.UserRepository;

public class PersonService {
	private final PersonRepository personRepository;
	private final UserRepository userRepository;

	@Autowired
	public PersonService(final UserRepository userRepository, final PersonRepository personRepository) {
		this.userRepository = userRepository;
		this.personRepository = personRepository;
	}
	
	public PersonResponseDTO save(final PersonRequestDTO personRequest) {
		final Person person = personRequest.toPerson();
		
		return new PersonResponseDTO(this.personRepository.save(person));
	}
	
	public PersonResponseDTO findById(final Long id) {
		return new PersonResponseDTO(this.personRepository.getReferenceById(id));
	}
	
	public List<PersonResponseDTO> findAll() {
		return this.personRepository.findAll()
				.stream()
				.map(u -> new PersonResponseDTO(u))
				.toList();
	}
	
	public void bindPerson(final PersonBindDTO personBind) {
		final Person toBindPerson = this.personRepository.getReferenceById(personBind.personId());
		boolean binded = false;
		
		if (personBind.doctorId() == null && personBind.patientId() == null) 
			return;
		
		if (toBindPerson.getDoctorId() == null) {
			toBindPerson.setDoctorId(personBind.doctorId());
			binded = true;
		}
		
		if (toBindPerson.getPatientId() == null) {
			toBindPerson.setPatientId(personBind.patientId());
			binded = true;
		}
		
		if (binded) 
			this.personRepository.save(toBindPerson);
	}
	
	public PersonResponseDTO update(final Long id, final PersonRequestDTO userRequest) {
		final Person toBeUpdatedPerson = this.personRepository.getReferenceById(id);
		
		final Address newAddress = new Address(
				userRequest.postalCode(),
				userRequest.avenue(),
				userRequest.complement(),
				userRequest.number(),
				userRequest.city(),
				userRequest.district(),
				userRequest.state()
		);
		
		toBeUpdatedPerson.setName(userRequest.name());
		toBeUpdatedPerson.setPhone(userRequest.phone());
		toBeUpdatedPerson.setAddress(newAddress);
		
		return new PersonResponseDTO(this.personRepository.save(toBeUpdatedPerson));
	}
	
	public void delete(final Long id) {
		this.userRepository.delete(this.userRepository.getReferenceById(id));
	}
}
