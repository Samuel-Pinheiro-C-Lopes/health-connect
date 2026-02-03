package io.github.samuel_pinheiro_c_lopes.doctorservice.services;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.dtos.DoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.doctorservice.repositories.DoctorRepository;
import io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos.CommonMailDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.general.enums.AccountStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.clients.UserClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserBindRequestDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;
import jakarta.persistence.EntityNotFoundException;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserClient personClient;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DoctorService(
    		final DoctorRepository doctorRepository, 
    		final UserClient personClient,
    		final RabbitTemplate rabbitTemplate
	) {
        this.doctorRepository = doctorRepository;
        this.personClient = personClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public DoctorResponseDTO save(final DoctorRequestDTO doctorRequest) {
    	final Doctor toSaveDoctor = doctorRequest.toDoctor();
    	
    	toSaveDoctor.setAccountStatus(AccountStatus.PENDING);
    	
        final Doctor savedDoctor = this.doctorRepository.save(toSaveDoctor);

        personClient.patch(doctorRequest.personId(), new PersonBindPatchDTO(null, savedDoctor.getId()));
        
        this.sendAccountUpdateTo(savedDoctor);
        
        return new DoctorResponseDTO(savedDoctor);
    }

    public List<DoctorResponseDTO> findAll() {
        return this.doctorRepository.findAll()
                .stream()
                .map(DoctorResponseDTO::new)
                .toList();
    }

    public List<DoctorFullResponseDTO> findAllFull() {
        return this.doctorRepository.findAll()
                .stream()
                .map(this::getDoctorFullResponseFrom)
                .toList();
    }

    public List<DoctorFullResponseDTO> findAllActiveFull() {
        return this.doctorRepository.findAllByAccountStatus(AccountStatus.ACTIVE)
                .stream()
                .map(this::getDoctorFullResponseFrom)
                .toList();
    }

    public List<DoctorFullResponseDTO> findAllPending() {
        return this.doctorRepository.findAllByAccountStatus(AccountStatus.PENDING)
                .stream()
                .map(this::getDoctorFullResponseFrom)
                .toList();
    }

    public void approve(final Long id) {
        final Doctor doctor = this.doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        
        doctor.setAccountStatus(AccountStatus.ACTIVE);
        
        this.doctorRepository.save(doctor);
        
        this.sendAccountUpdateTo(doctor);
    }

    public void reject(final Long id) {
        final Doctor doctor = this.doctorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found"));
        
        doctor.setAccountStatus(AccountStatus.DISABLED);
        
        this.doctorRepository.save(doctor);
        
        this.sendAccountUpdateTo(doctor);
    }
    
	private void sendAccountUpdateTo(final Doctor doctor) {
        final CommonUserResponseDTO doctorUser = this.personClient.findByDoctorId(doctor.getId());
        
		rabbitTemplate.convertAndSend("email.notification", new EmailDto(
            	"healthconnectpweb@gmail.com",
            	doctorUser.email(),
            	"Status da conta alterada!",
            	"Dr(a). " + doctorUser.name() + 
            	", sua conta teve seu estado alterado!\n Estado atual:: " + 
            			doctor.getAccountStatus().getMessage()
		));
	}
	
	private record EmailDto(
			String mailFrom, 
			String mailTo,
			String mailSubject, 
			String mailBody
	) implements CommonMailDTO { }
    
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
		) implements CommonUserBindRequestDTO { }
	

	private DoctorFullResponseDTO getDoctorFullResponseFrom(final Doctor doctor) {
		final CommonUserResponseDTO user = this.personClient.findByDoctorId(doctor.getId());
		
		return new DoctorFullResponseDTO(
				doctor.getId(),
				user.id(),
				doctor.getCrm(),
				doctor.getSpecialty(),
				user.name(),
				user.phone(),
				user.postalCode(),
				user.avenue(),
				user.complement(),
				user.number(),
				user.city(),
				user.district(),
				user.state()
		);
	}
}
