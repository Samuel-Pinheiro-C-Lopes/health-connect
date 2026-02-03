package io.github.samuel_pinheiro_c_lopes.doctorservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.doctorservice.repositories.DoctorRepository;
import io.github.samuel_pinheiro_c_lopes.spring_common.person.clients.PersonClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos.CommonPersonBindPatchDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final PersonClient personClient;

    @Autowired
    public DoctorService(final DoctorRepository doctorRepository, final PersonClient personClient) {
        this.doctorRepository = doctorRepository;
        this.personClient = personClient;
    }

    public DoctorResponseDTO save(final DoctorRequestDTO doctorRequest) {
        final Doctor savedDoctor = this.doctorRepository.save(doctorRequest.toDoctor());

        personClient.patch(doctorRequest.personId(), new PersonBindPatchDTO(null, savedDoctor.getId()));
        
        return new DoctorResponseDTO(savedDoctor);
    }

    public List<DoctorResponseDTO> findAll() {
        return this.doctorRepository.findAll()
                .stream()
                .map(DoctorResponseDTO::new)
                .toList();
    }

    public DoctorResponseDTO findById(final Long id) {
        return new DoctorResponseDTO(this.doctorRepository.getReferenceById(id));
    }

    public DoctorResponseDTO update(final Long id, final DoctorRequestDTO doctorRequest) {
        final Doctor toBeUpdatedDoctor = this.doctorRepository.getReferenceById(id);

        toBeUpdatedDoctor.setPersonId(doctorRequest.personId());
        toBeUpdatedDoctor.setCrm(doctorRequest.crm());
        toBeUpdatedDoctor.setSpecialty(doctorRequest.specialty());

        final Doctor savedDoctor = this.doctorRepository.save(toBeUpdatedDoctor);

        personClient.patch(doctorRequest.personId(), new PersonBindPatchDTO(null, id));

        return new DoctorResponseDTO(savedDoctor);
    }

    public void delete(final Long id) {
        this.doctorRepository.delete(this.doctorRepository.getReferenceById(id));
    }

	public List<DoctorResponseDTO> findAllActive() {
		return this.doctorRepository.findAllByAccountStatus(AccountStatus.ACTIVE)
				.stream()
				.map(DoctorResponseDTO::new)
				.toList();
	}

	public void deactivate(final Long id) {
		final Doctor doctor = this.doctorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
		
		doctor.setAccountStatus(AccountStatus.DISABLED);
		
		this.doctorRepository.save(doctor);
	}
	
	private record PersonBindPatchDTO(
			Long patientId,
			Long doctorId
		) implements CommonPersonBindPatchDTO { }

	public DoctorFullResponseDTO findCurrentlyLoggedIn() {

		return null;
	}
}
