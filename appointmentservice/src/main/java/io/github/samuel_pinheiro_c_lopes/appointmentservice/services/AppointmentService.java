package io.github.samuel_pinheiro_c_lopes.appointmentservice.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentCancelDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentFullResponseDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentRequestDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.dtos.AppointmentResponseDTO;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.models.Appointment;
import io.github.samuel_pinheiro_c_lopes.appointmentservice.repositories.AppointmentRepository;
import io.github.samuel_pinheiro_c_lopes.spring_common.appointment.enums.AppointmentStatus;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.clients.DoctorClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.doctor.dtos.CommonDoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.email.dtos.CommonMailDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.clients.PatientClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.clients.UserClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.user.dtos.CommonUserResponseDTO;

@Service
public class AppointmentService {
	private final AppointmentRepository appointmentRepository;
	private final UserClient personClient;
	private final DoctorClient doctorClient;
	private final PatientClient patientClient;
	private final RabbitTemplate rabbitTemplate;

	@Autowired
	public AppointmentService(
			final AppointmentRepository appointmentRepository, 
			final UserClient personClient, 
			final DoctorClient doctorClient, 
			final PatientClient patientClient,
			final RabbitTemplate rabbitTemplate
	) {
		this.appointmentRepository = appointmentRepository;
		this.personClient = personClient;
		this.doctorClient = doctorClient;
		this.patientClient = patientClient;
		this.rabbitTemplate = rabbitTemplate;	
	}
	
	// --- VALIDAÇÕES ---

    private void validateAppointmentDayOfWeek(final Appointment appointment) {
        if (appointment.getDateTime().getDayOfWeek().getValue() == 7) {
            throw new IllegalArgumentException("A clínica não abre aos domingos.");
        }
    }
    
    private void validateAppointmentHour(final Appointment appointment) {
        if (appointment.getDateTime().getHour() < 7 || appointment.getDateTime().getHour() > 18) {
            throw new IllegalArgumentException("Horário inválido (07:00 - 19:00).");
        }
    }
    
    private void validateAppointmentAdvance(final Appointment appointment) {
        if (Duration.between(LocalDateTime.now(), appointment.getDateTime()).toMinutes() <= 30) {
            throw new IllegalArgumentException("Agendamento deve ser feito com 30min de antecedência.");
        }
    }
    
    // CORREÇÃO LÓGICA: Recebe o ID para ignorar (caso seja update)
    private void validatePerPatientAndDay(final Appointment appointment, Long currentAppointmentId) {
        LocalDateTime start = appointment.getDateTime().toLocalDate().atStartOfDay();
        LocalDateTime end = appointment.getDateTime().toLocalDate().atTime(LocalTime.MAX);
        Long patientId = appointment.getPatientId();
        boolean exists;

        if (currentAppointmentId == null) 
        	exists = this.appointmentRepository.existsByDateTimeBetweenAndPatientId(start, end, patientId);
        else 
        	exists = this.appointmentRepository.existsByDateTimeBetweenAndPatientIdAndIdNot(start, end, patientId, currentAppointmentId);
        

        if (exists) 
            throw new IllegalArgumentException("Paciente já possui consulta neste dia.");
    }
    
    private void validateChosenDoctor(final Long doctorId, final LocalDateTime dateTime) {
        List<Long> busyIds = this.appointmentRepository.findBusyDoctorIds(dateTime);
        if (busyIds.contains(doctorId)) 
            throw new IllegalArgumentException("Médico indisponível neste horário.");
        
        boolean isActive = this.doctorClient.findAllActive().stream()
                .anyMatch(d -> d.id().equals(doctorId));

        if (!isActive) 
            throw new IllegalArgumentException("Médico inválido ou inativo.");
    }

    private Long getRandomAvailableDoctorId(final LocalDateTime dateTime) {
        List<Long> busyDoctorIds = this.appointmentRepository.findBusyDoctorIds(dateTime);
        List<CommonDoctorResponseDTO> allActiveDoctors = this.doctorClient.findAllActive();

        List<Long> availableDoctorIds = allActiveDoctors.stream()
                .map(CommonDoctorResponseDTO::id)
                .filter(id -> !busyDoctorIds.contains(id))
                .collect(Collectors.toList());

        if (availableDoctorIds.isEmpty()) 
            throw new IllegalArgumentException("Não há médicos disponíveis neste horário.");
        
        Collections.shuffle(availableDoctorIds);
        return availableDoctorIds.get(0);
    }
    
    private void verifyAndFillAppointment(final Appointment appointment, Long currentId) {
        this.validateAppointmentDayOfWeek(appointment);
        this.validateAppointmentHour(appointment);
        this.validateAppointmentAdvance(appointment);
        this.validatePerPatientAndDay(appointment, currentId);
        
        if (appointment.getDoctorId() == null) {
            Long randomId = this.getRandomAvailableDoctorId(appointment.getDateTime());
            appointment.setDoctorId(randomId);
        } else {
            this.validateChosenDoctor(appointment.getDoctorId(), appointment.getDateTime());
        }
    }
	
	// vvv CRUD vvv
	
	public AppointmentResponseDTO save(final AppointmentRequestDTO userRequest) {
        final Appointment appointment = userRequest.toAppointment();
        
        this.verifyAndFillAppointment(appointment, null);
        this.sendAppointmentMadeToDoctor(appointment);
        
        return new AppointmentResponseDTO(this.appointmentRepository.save(appointment));
    }
	
	private void sendAppointmentMadeToDoctor(final Appointment appointment) {
        final CommonUserResponseDTO doctorUser = this.personClient.findByDoctorId(appointment.getDoctorId());
		final CommonUserResponseDTO patientUser = this.personClient.findByPatientId(appointment.getPatientId());
        
		rabbitTemplate.convertAndSend("email.notification", new EmailDto(
            	"healthconnectpweb@gmail.com",
            	doctorUser.email(),
            	"Consulta Marcada!",
            	"Dr(a). " + doctorUser.name() + ", sua consulta com " + patientUser.name() + " foi marcada para " + appointment.getDateTime() + "." 
		));
	}
	
	private record EmailDto(
			String mailFrom, 
			String mailTo,
			String mailSubject, 
			String mailBody
	) implements CommonMailDTO { }
    
    public AppointmentResponseDTO update(final Long id, final AppointmentRequestDTO appointmentRequest) {
        final Appointment existingAppointment = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        
        final Appointment newDatatAppointment = appointmentRequest.toAppointment();
        
        this.verifyAndFillAppointment(newDatatAppointment, id);
        
        existingAppointment.setDateTime(newDatatAppointment.getDateTime());
        existingAppointment.setDoctorId(newDatatAppointment.getDoctorId());
        existingAppointment.setPatientId(newDatatAppointment.getPatientId());
        
        return new AppointmentResponseDTO(this.appointmentRepository.save(existingAppointment));
    }
	
	public AppointmentResponseDTO patch(final Long id, final AppointmentCancelDTO appointmentCancellation) {
		final Appointment appointment = this.appointmentRepository.getReferenceById(id);

		appointment.setCanceledAt(LocalDateTime.now());
		appointment.setCancelReason(appointmentCancellation.reason());
		appointment.setStatus(AppointmentStatus.CANCELED);
		
		return new AppointmentResponseDTO(this.appointmentRepository.save(appointment));
	}
	
	public void delete(final Long id) {
		this.appointmentRepository.delete(this.appointmentRepository.getReferenceById(id));
	}
	
	// vvv FIND ALL  vvv
	
	public List<AppointmentResponseDTO> findAll() {
		return this.appointmentRepository.findAll()
				.stream()
				.map(u -> new AppointmentResponseDTO(u))
				.toList();
	}

	public List<AppointmentFullResponseDTO> findAllFromPatient(final Long patientId) {
		final List<Appointment> appointments = this.appointmentRepository.findByPatientId(patientId);
		
		final List<AppointmentFullResponseDTO> response = appointments
				.stream()
				.map(this::findFromAppointment)
				.toList();
		
		return response;
	}

	public List<AppointmentFullResponseDTO> findAllFromDoctor(Long doctorId) {
		final List<Appointment> appointments = this.appointmentRepository.findByDoctorId(doctorId);
		
		final List<AppointmentFullResponseDTO> response = appointments
				.stream()
				.map(this::findFromAppointment)
				.toList();
		
		return response;
	}
	
	private AppointmentFullResponseDTO findFromAppointment(final Appointment a) {
		final CommonUserResponseDTO patientPerson = this.personClient.findByPatientId(a.getPatientId());
		final CommonUserResponseDTO doctorPerson = this.personClient.findByDoctorId(a.getDoctorId());
		final CommonPatientResponseDTO patient = this.patientClient.findById(a.getPatientId());
		final CommonDoctorResponseDTO doctor = this.doctorClient.findById(a.getDoctorId());
		
		return new AppointmentFullResponseDTO(
			a.getId(),
			a.getDateTime(),
			a.getStatus(),
			patient.id(),
			patientPerson.name(),
			doctor.id(),
			doctorPerson.name(),
			doctor.crm(),
			doctor.specialty()
		);
	}

	public List<AppointmentFullResponseDTO> findAllFromPatientCurrentlyLoggedIn() {
		final CommonUserResponseDTO person = this.personClient.findCurrentlyLoggedIn();
		
		return this.findAllFromPatient(person.patientId());
	}
	
	public List<AppointmentFullResponseDTO> findAllFromDoctorCurrentlyLoggedIn() {
		final CommonUserResponseDTO person = this.personClient.findCurrentlyLoggedIn();
		
		return this.findAllFromDoctor(person.doctorId());
	}

}
