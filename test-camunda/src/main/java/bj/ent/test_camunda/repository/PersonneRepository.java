package bj.ent.test_camunda.repository;

import bj.ent.test_camunda.enums.Poste;
import bj.ent.test_camunda.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
    @Query("SELECT p FROM Personne p WHERE p.nom = ?1 AND p.prenom = ?2")
    Optional<Personne> findByNomAndPrenom(String nom, String prenom);

    List<Personne> findByPoste(Poste poste);
}