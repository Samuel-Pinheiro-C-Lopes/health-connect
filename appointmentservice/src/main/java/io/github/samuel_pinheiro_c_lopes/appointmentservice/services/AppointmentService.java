package io.github.samuel_pinheiro_c_lopes.appointmentservice.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import feign.FeignException;
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
    
    // --- VALIDAÇÕES DE AGENDAMENTO ---

    private void validateAppointmentDayOfWeek(final Appointment appointment) {
        if (appointment.getDateTime().getDayOfWeek().getValue() == 7) {
            throw new IllegalArgumentException("A clínica não abre aos domingos.");
        }
    }
    
    private void validateAppointmentHour(final Appointment appointment) {
        // Regra: Das 07:00 às 19:00
        if (appointment.getDateTime().getHour() < 7 || appointment.getDateTime().getHour() > 18) {
            throw new IllegalArgumentException("Horário inválido (07:00 - 19:00).");
        }
    }
    
    private void validateAppointmentAdvance(final Appointment appointment) {
        // Regra: Antecedência mínima de 30 minutos
        if (Duration.between(LocalDateTime.now(), appointment.getDateTime()).toMinutes() <= 30) {
            throw new IllegalArgumentException("Agendamento deve ser feito com no mínimo 30min de antecedência.");
        }
    }
    
    private void validatePerPatientAndDay(final Appointment appointment, Long currentAppointmentId) {
        // Regra: Máximo 1 consulta por dia para o mesmo paciente
        LocalDateTime start = appointment.getDateTime().toLocalDate().atStartOfDay();
        LocalDateTime end = appointment.getDateTime().toLocalDate().atTime(LocalTime.MAX);
        Long patientId = appointment.getPatientId();
        boolean exists;

        if (currentAppointmentId == null) 
            exists = this.appointmentRepository.existsByDateTimeBetweenAndPatientId(start, end, patientId);
        else 
            exists = this.appointmentRepository.existsByDateTimeBetweenAndPatientIdAndIdNot(start, end, patientId, currentAppointmentId);
        
        if (exists) 
            throw new IllegalArgumentException("Paciente já possui consulta agendada neste dia.");
    }
    
    private void validateChosenDoctor(final Long doctorId, final LocalDateTime dateTime) {
        // Regra: Médico não pode ter outra consulta na mesma hora
        List<Long> busyIds = this.appointmentRepository.findBusyDoctorIds(dateTime);
        if (busyIds.contains(doctorId)) 
            throw new IllegalArgumentException("Médico indisponível neste horário.");
        
        // Regra: Médicos inativos (assumindo que findAllActive retorna apenas os ativos)
        boolean isActive = this.doctorClient.findAllActive().stream()
                .anyMatch(d -> d.id().equals(doctorId));

        if (!isActive) 
            throw new IllegalArgumentException("Médico inválido ou inativo no sistema.");
    }

    private void validatePatientStatus(final Long patientId) {
        // Regra: Pacientes inativos não podem agendar
        try {
            // Tenta buscar o paciente. Se lançar 404 (FeignException.NotFound), o paciente não existe/inativo.
            // Idealmente, o endpoint deve retornar status ou só retornar se ativo.
            // Aqui assumimos que se o registro existe no microsserviço de paciente, ele é válido para agendamento,
            // ou verificamos uma flag específica se o DTO permitir.
            this.patientClient.findById(patientId);
            
            // Opcional: Se houver um endpoint findAllActive no patientClient, usar lógica similar ao médico.
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Paciente não encontrado ou inativo no sistema.");
        } catch (Exception e) {
            // Logar erro de comunicação se necessário
            throw new IllegalArgumentException("Não foi possível validar o status do paciente.");
        }
    }

    private Long getRandomAvailableDoctorId(final LocalDateTime dateTime) {
        // Regra: Escolha aleatória se médico não informado
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
        
        // Valida se o paciente está ativo/existente
        this.validatePatientStatus(appointment.getPatientId());
        
        this.validatePerPatientAndDay(appointment, currentId);
        
        if (appointment.getDoctorId() == null) {
            Long randomId = this.getRandomAvailableDoctorId(appointment.getDateTime());
            appointment.setDoctorId(randomId);
        } else {
            this.validateChosenDoctor(appointment.getDoctorId(), appointment.getDateTime());
        }
    }

    // --- VALIDAÇÕES DE CANCELAMENTO ---

    private void validateCancellation(final Appointment appointment, final String reason) {
        // Regra: Motivo obrigatório
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("É obrigatório informar o motivo do cancelamento.");
        }

        // Regra: Antecedência mínima de 24 horas
        long hoursUntilAppointment = Duration.between(LocalDateTime.now(), appointment.getDateTime()).toHours();
        if (hoursUntilAppointment < 24) {
            throw new IllegalArgumentException("A consulta somente pode ser cancelada com antecedência mínima de 24 horas.");
        }
        
        // Verifica se já não está cancelada ou realizada (boa prática)
        if (appointment.getStatus() == AppointmentStatus.CANCELED || appointment.getStatus() == AppointmentStatus.ATTENTED) {
            throw new IllegalArgumentException("Esta consulta não pode mais ser cancelada.");
        }
    }
    
    // vvv CRUD vvv
    
    public AppointmentResponseDTO save(final AppointmentRequestDTO userRequest) {
        final Appointment appointment = userRequest.toAppointment();
        
        this.verifyAndFillAppointment(appointment, null);
        
        // Define status inicial padrão se necessário
        appointment.setStatus(AppointmentStatus.SCHEDULED); 
        
        final Appointment savedAppointment = this.appointmentRepository.save(appointment);
        
        this.sendAppointmentMadeToDoctor(savedAppointment);
        
        return new AppointmentResponseDTO(savedAppointment);
    }
    
    private void sendAppointmentMadeToDoctor(final Appointment appointment) {
        try {
            final CommonUserResponseDTO doctorUser = this.personClient.findByDoctorId(appointment.getDoctorId());
            final CommonUserResponseDTO patientUser = this.personClient.findByPatientId(appointment.getPatientId());
            
            rabbitTemplate.convertAndSend("email.notification", new CommonMailDTO(
                    "healthconnectpweb@gmail.com",
                    doctorUser.email(),
                    "Consulta Marcada!",
                    "Dr(a). " + doctorUser.name() + ", sua consulta com " + patientUser.name() + " foi marcada para " + appointment.getDateTime() + "." 
            ));
        } catch (Exception e) {
            System.err.println("Erro ao enviar email de notificação: " + e.getMessage());
            // Não impede o agendamento se o email falhar
        }
    }
    
    public AppointmentResponseDTO update(final Long id, final AppointmentRequestDTO appointmentRequest) {
        final Appointment existingAppointment = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));
        
        final Appointment newDatatAppointment = appointmentRequest.toAppointment();
        
        // Mantém ID original para validação de conflito ignorar o próprio registro
        this.verifyAndFillAppointment(newDatatAppointment, id);
        
        existingAppointment.setDateTime(newDatatAppointment.getDateTime());
        existingAppointment.setDoctorId(newDatatAppointment.getDoctorId());
        existingAppointment.setPatientId(newDatatAppointment.getPatientId());
        
        return new AppointmentResponseDTO(this.appointmentRepository.save(existingAppointment));
    }
    
    public AppointmentResponseDTO patch(final Long id, final AppointmentCancelDTO appointmentCancellation) {
        final Appointment appointment = this.appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Consulta não encontrada"));

        // Aplica validações de cancelamento (Regra 24h e Motivo)
        this.validateCancellation(appointment, appointmentCancellation.reason().toString());

        appointment.setCanceledAt(LocalDateTime.now());
        appointment.setCancelReason(appointmentCancellation.reason());
        appointment.setStatus(AppointmentStatus.CANCELED);
        
        return new AppointmentResponseDTO(this.appointmentRepository.save(appointment));
    }
    
    public void delete(final Long id) {
        if (!this.appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Consulta não encontrada para exclusão.");
        }
        this.appointmentRepository.deleteById(id);
    }
    
    // vvv FIND ALL  vvv
    
    public List<AppointmentResponseDTO> findAll() {
        return this.appointmentRepository.findAll()
                .stream()
                .map(AppointmentResponseDTO::new)
                .toList();
    }

    public List<AppointmentFullResponseDTO> findAllFromPatient(final Long patientId) {
        final List<Appointment> appointments = this.appointmentRepository.findByPatientId(patientId);
        
        return appointments.stream()
                .map(this::findFromAppointment)
                .filter(Objects::nonNull)
                .toList();
    }

    public List<AppointmentFullResponseDTO> findAllFromDoctor(Long doctorId) {
        final List<Appointment> appointments = this.appointmentRepository.findByDoctorId(doctorId);
        
        return appointments.stream()
                .map(this::findFromAppointment)
                .filter(Objects::nonNull)
                .toList();
    }
    
    private AppointmentFullResponseDTO findFromAppointment(final Appointment a) {
        try {
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
        } catch (FeignException.NotFound e) {
            System.err.println("Warning: Orphan appointment found with ID " + a.getId() + ".");
            return null; 
        } catch (Exception e) {
            System.err.println("Error fetching details for appointment ID " + a.getId() + ": " + e.getMessage());
            return null;
        }
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