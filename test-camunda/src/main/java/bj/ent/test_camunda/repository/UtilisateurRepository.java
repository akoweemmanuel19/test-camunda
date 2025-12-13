package bj.ent.test_camunda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bj.ent.test_camunda.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByIdentifiant(String identifiant);
    
}
    