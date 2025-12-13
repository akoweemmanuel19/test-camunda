package bj.ent.test_camunda.repository;

import bj.ent.test_camunda.model.LeaveRequest;
import bj.ent.test_camunda.enums.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    // Pour l'employé : voir ses demandes (tous statuts confondus)
    List<LeaveRequest> findByEmployeeId(Long employeeId);

    // Pour le manager : voir les demandes qui lui sont adressées ET qui sont envoyées (pas brouillon)
    List<LeaveRequest> findByManagerIdAndStatut(Long managerId, StatutDemande statut);
    
    // Variante : voir tout sauf brouillon
    List<LeaveRequest> findByManagerIdAndStatutNot(Long managerId, StatutDemande statut);
}