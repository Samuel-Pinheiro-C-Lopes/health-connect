package io.github.samuel_pinheiro_c_lopes.doctorservice.services;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.samuel_pinheiro_c_lopes.doctorservice.broker.EmailNotificationProducer;
import io.github.samuel_pinheiro_c_lopes.doctorservice.broker.DoctorProducer;
import io.github.samuel_pinheiro_c_lopes.doctorservice.clients.UserserviceClient;
import io.github.samuel_pinheiro_c_lopes.doctorservice.clients.dtos.PersonResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorDetailsResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.StatusSolicitacao;
import io.github.samuel_pinheiro_c_lopes.doctorservice.exceptions.DoctorNotFoundException;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.doctorservice.repositories.DoctorRepository;
@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorProducer doctorProducer;
    private final UserserviceClient userserviceClient;
    private final EmailNotificationProducer emailNotificationProducer;
    @Autowired
    public DoctorService(
    		final DoctorRepository doctorRepository,
    		final DoctorProducer doctorProducer,
    		final UserserviceClient userserviceClient,
    		final EmailNotificationProducer emailNotificationProducer
    ) 
    {
        this.doctorRepository = doctorRepository;
        this.doctorProducer = doctorProducer;
        this.userserviceClient = userserviceClient;
        this.emailNotificationProducer = emailNotificationProducer;
    }
    public DoctorResponseDTO save(final DoctorRequestDTO doctorRequest) {
        final Doctor savedDoctor = this.doctorRepository.save(doctorRequest.toDoctor());
        this.doctorProducer.bindPerson(savedDoctor.getPersonId(), savedDoctor.getId());
        return new DoctorResponseDTO(savedDoctor);
    }

    public List<DoctorResponseDTO> findAll() {
        return this.doctorRepository.findAll()
                .stream()
                .map(DoctorResponseDTO::new)
                .toList();
    }

    public DoctorResponseDTO findById(final Long id) {
        return new DoctorResponseDTO(
				this.doctorRepository.findById(id)
						.orElseThrow(() -> new DoctorNotFoundException("Doctor not found for id: " + id))
		);
    }

	public DoctorDetailsResponseDTO findDetailsById(final Long id) {
		final Doctor doctor = this.doctorRepository.findById(id)
				.orElseThrow(() -> new DoctorNotFoundException("Doctor not found for id: " + id));
		final PersonResponseDTO person = this.userserviceClient.findPersonById(doctor.getPersonId());
		return new DoctorDetailsResponseDTO(new DoctorResponseDTO(doctor), person);
	}

	public List<DoctorResponseDTO> findByStatus(final StatusSolicitacao status) {
		return this.doctorRepository.findByStatusSolicitacao(status)
				.stream()
				.map(DoctorResponseDTO::new)
				.toList();
	}

	public DoctorResponseDTO updateStatus(final Long id, final StatusSolicitacao status) {
		final Doctor doctor = this.doctorRepository.findById(id)
				.orElseThrow(() -> new DoctorNotFoundException("Doctor not found for id: " + id));
		doctor.setStatusSolicitacao(status);
		final Doctor savedDoctor = this.doctorRepository.save(doctor);

		final String subject = "Atualização de status do cadastro";
		final String body = "Olá! Seu cadastro de médico foi atualizado para o status: " + status + ".";
		this.emailNotificationProducer.sendStatusUpdateEmail(savedDoctor.getEmail(), subject, body);

		return new DoctorResponseDTO(savedDoctor);
	}

    public DoctorResponseDTO update(final Long id, final DoctorRequestDTO doctorRequest) {
        final Doctor toBeUpdatedDoctor = this.doctorRepository.findById(id)
        		.orElseThrow(() -> new DoctorNotFoundException("Doctor not found for id: " + id));

        toBeUpdatedDoctor.setPersonId(doctorRequest.personId());
        toBeUpdatedDoctor.setCrm(doctorRequest.crm());
        toBeUpdatedDoctor.setEmail(doctorRequest.email());
        toBeUpdatedDoctor.setSpecialty(doctorRequest.specialty());

        final Doctor savedDoctor = this.doctorRepository.save(toBeUpdatedDoctor);
        this.doctorProducer.bindPerson(savedDoctor.getPersonId(), savedDoctor.getId());

        return new DoctorResponseDTO(savedDoctor);
    }

    public void delete(final Long id) {
        this.doctorRepository.delete(
				this.doctorRepository.findById(id)
						.orElseThrow(() -> new DoctorNotFoundException("Doctor not found for id: " + id))
		);
    }
}