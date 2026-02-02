package io.github.samuel_pinheiro_c_lopes.appointmentservice.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.clients.PatientClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.patient.dtos.CommonPatientResponseDTO;
import io.github.samuel_pinheiro_c_lopes.spring_common.person.clients.PersonClient;
import io.github.samuel_pinheiro_c_lopes.spring_common.person.dtos.CommonPersonResponseDTO;

@Service
public class AppointmentService {
	private final AppointmentRepository appointmentRepository;
	private final PersonClient personClient;
	private final DoctorClient doctorClient;
	private final PatientClient patientClient;

	@Autowired
	public AppointmentService(final AppointmentRepository appointmentRepository, final PersonClient personClient, final DoctorClient doctorClient, final PatientClient patientClient) {
		this.appointmentRepository = appointmentRepository;
		this.personClient = personClient;
		this.doctorClient = doctorClient;
		this.patientClient = patientClient;
	}
	
	// funcionamento da clínica é de 
	// segunda a sábado
	private Boolean validateAppointmentDayOfWeek(final Appointment appointment) {
		return appointment.getDateTime().getDayOfWeek().getValue() != 7;
	}
	
	// O horário de funcionamento da clínica é 
	// das 07:00 às 19:00
	private Boolean validateAppointmentHour(final Appointment appointment) {
		return appointment.getDateTime().getHour() >= 7 && appointment.getDateTime().getHour() <= 19;
	}
	
	// antecedência de 30 minutos 
	private Boolean validateAppointmenteAdvance(final Appointment appointment) {
		return Duration.between(LocalDateTime.now(), appointment.getDateTime()).toMinutes() > 30;
	}
	
	// Não permitir o agendamento de mais de uma
	// consulta no mesmo dia para um mesmo paciente
	private Boolean validateAppointmentPerPatientAndDay(final Appointment appointment) {
		return !this.appointmentRepository.existsByByDateTimeBetweenAndPatientId(
				appointment.getDateTime().toLocalDate().atStartOfDay(), 
				appointment.getDateTime().toLocalDate().atTime(LocalTime.MAX), 
				appointment.getPatientId()
		);
	}
	

	// A escolha do médico é opcional, sendo que nesse 
	// caso o sistema deve escolher aleatoriamente algum 
	// médico disponível na data/hora preenchida.
	private Long getRandomAvailableDoctorId(final LocalDateTime dateTime) {
		final List<CommonDoctorResponseDTO> doctors = this.doctorClient.findAll();
		
		final Long chosenOneId = doctors
				.stream()
				.filter(d -> !this.appointmentRepository.existsByByDateTimeBetweenAndDoctorId(
						dateTime.minusHours(1), 
						dateTime, 
						d.id())
				)
				.findFirst()
				.map(d -> d.id())
				.orElse(null);
		
		return chosenOneId;
	}
	
	// verificações comuns ao save e update
	private void verifyAndHandleAppointment(final Appointment appointment) {
		if (!this.validateAppointmentDayOfWeek(appointment))
			throw new IllegalArgumentException();
		
		if (!this.validateAppointmentHour(appointment))
			throw new IllegalArgumentException();
		
		if (!this.validateAppointmenteAdvance(appointment))
			throw new IllegalArgumentException();
		
		if (!this.validateAppointmentPerPatientAndDay(appointment))
			throw new IllegalArgumentException();
		
		if (appointment.getDoctorId() == null) {
			final Long doctorId = this.getRandomAvailableDoctorId(appointment.getDateTime());
			
			if (doctorId == null)
				throw new IllegalArgumentException();
			
			appointment.setDoctorId(doctorId);
		}
	}
	
	// vvv CRUD vvv
	
	public AppointmentResponseDTO save(final AppointmentRequestDTO userRequest) {
		final Appointment appointment = userRequest.toAppointment();
		
		this.verifyAndHandleAppointment(appointment);
		
		return new AppointmentResponseDTO(this.appointmentRepository.save(appointment));
	}
	
	public AppointmentResponseDTO update(final Long id, final AppointmentRequestDTO appointmentRequest) {
		final Appointment toBeUpdatedAppointment = this.appointmentRepository.getReferenceById(id);
		
		this.verifyAndHandleAppointment(appointmentRequest.toAppointment());
		
		return new AppointmentResponseDTO(this.appointmentRepository.save(toBeUpdatedAppointment));
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
		final CommonPersonResponseDTO patientPerson = this.personClient.findByPatientId(a.getPatientId());
		final CommonPersonResponseDTO doctorPerson = this.personClient.findByDoctorId(a.getDoctorId());
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
		final CommonPersonResponseDTO person = this.personClient.findCurrentlyLoggedIn();
		
		return this.findAllFromPatient(person.patientId());
	}
	
	public List<AppointmentFullResponseDTO> findAllFromDoctorCurrentlyLoggedIn() {
		final CommonPersonResponseDTO person = this.personClient.findCurrentlyLoggedIn();
		
		return this.findAllFromDoctor(person.doctorId());
	}

}
