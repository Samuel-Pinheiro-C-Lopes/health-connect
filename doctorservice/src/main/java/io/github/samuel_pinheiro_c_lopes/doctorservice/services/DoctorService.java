package io.github.samuel_pinheiro_c_lopes.doctorservice.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.samuel_pinheiro_c_lopes.doctorservice.broker.DoctorProducer;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorRequestDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.controllers.dtos.DoctorResponseDTO;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
import io.github.samuel_pinheiro_c_lopes.doctorservice.repositories.DoctorRepository;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorProducer doctorProducer;

    @Autowired
    public DoctorService(final DoctorRepository doctorRepository, final DoctorProducer doctorProducer) {
        this.doctorRepository = doctorRepository;
        this.doctorProducer = doctorProducer;
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
        return new DoctorResponseDTO(this.doctorRepository.getReferenceById(id));
    }

    public DoctorResponseDTO update(final Long id, final DoctorRequestDTO doctorRequest) {
        final Doctor toBeUpdatedDoctor = this.doctorRepository.getReferenceById(id);

        toBeUpdatedDoctor.setPersonId(doctorRequest.personId());
        toBeUpdatedDoctor.setCrm(doctorRequest.crm());
        toBeUpdatedDoctor.setSpecialty(doctorRequest.specialty());

        final Doctor savedDoctor = this.doctorRepository.save(toBeUpdatedDoctor);
        this.doctorProducer.bindPerson(savedDoctor.getPersonId(), savedDoctor.getId());

        return new DoctorResponseDTO(savedDoctor);
    }

    public void delete(final Long id) {
        this.doctorRepository.delete(this.doctorRepository.getReferenceById(id));
    }
}
