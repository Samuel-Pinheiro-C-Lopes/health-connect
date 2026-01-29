package br.scofield_lopes.email_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.scofield_lopes.email_service.entities.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long>{

}