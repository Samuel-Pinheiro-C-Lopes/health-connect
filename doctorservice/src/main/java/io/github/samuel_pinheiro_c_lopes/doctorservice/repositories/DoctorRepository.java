package io.github.samuel_pinheiro_c_lopes.doctorservice.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.samuel_pinheiro_c_lopes.doctorservice.enums.StatusSolicitacao;
import io.github.samuel_pinheiro_c_lopes.doctorservice.models.Doctor;
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	List<Doctor> findByStatusSolicitacao(StatusSolicitacao statusSolicitacao);
}