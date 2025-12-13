package bj.ent.test_camunda.service;

import bj.ent.test_camunda.model.LeaveRequest;
import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.enums.StatutDemande;
import bj.ent.test_camunda.repository.LeaveRequestRepository;
import bj.ent.test_camunda.repository.PersonneRepository;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    // 1. Créer un brouillon (Pas de Camunda ici)
    public LeaveRequest createDraft(Long utilisateurId, Integer days, Long managerPersonneId) {
        // Dans une vraie app, on récupère l'utilisateur connecté via Spring Security
        // Ici on suppose qu'on reçoit l'ID de l'utilisateur connecté
        Personne employe = personneRepository.findById(utilisateurId) // Attention: lier Utilisateur -> Personne
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        Personne manager = personneRepository.findById(managerPersonneId)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé"));

        LeaveRequest request = new LeaveRequest(employe, manager, days);
        return leaveRequestRepository.save(request);
    }

    // 2. Modifier une demande (Seulement si BROUILLON)
    public LeaveRequest updateDraft(Long requestId, Integer days) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (request.getStatut() != StatutDemande.BROUILLON) {
            throw new RuntimeException("Impossible de modifier une demande déjà envoyée !");
        }

        request.setNumberOfDays(days);
        return leaveRequestRepository.save(request);
    }

    // 3. Supprimer une demande (Seulement si BROUILLON)
    public void deleteDraft(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (request.getStatut() != StatutDemande.BROUILLON) {
            throw new RuntimeException("Impossible de supprimer une demande déjà envoyée !");
        }
        leaveRequestRepository.delete(request);
    }

    // 4. ENVOYER LA DEMANDE (Démarre Camunda)
    @Transactional
    public LeaveRequest sendRequestToManager(Long requestId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        if (request.getStatut() != StatutDemande.BROUILLON) {
            throw new RuntimeException("Cette demande a déjà été envoyée.");
        }

        // Mise à jour statut
        request.setStatut(StatutDemande.EN_ATTENTE);
        
        // Démarrage Workflow Camunda
        Map<String, Object> variables = Map.of(
            "leaveRequestId", request.getId(),
            "employeeName", request.getEmployee().getNom(),
            "managerId", request.getManager().getId(), // Utile pour assigner la tâche
            "numberOfDays", request.getNumberOfDays()
        );

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("leave-request", request.getId().toString(), variables);
        request.setCamundaProcessInstanceId(pi.getId());
        
        return leaveRequestRepository.save(request);
    }

    // 5. MANAGER : Récupérer les tâches
    public List<LeaveRequest> getManagerRequests(Long managerPersonneId) {
        // Retourne tout ce qui n'est pas brouillon pour ce manager
        return leaveRequestRepository.findByManagerIdAndStatutNot(managerPersonneId, StatutDemande.BROUILLON);
    }
    
    // 6. EMPLOYÉ : Récupérer mes demandes
    public List<LeaveRequest> getEmployeeRequests(Long employeePersonneId) {
        return leaveRequestRepository.findByEmployeeId(employeePersonneId);
    }

    // 7. MANAGER : Approuver ou Rejeter (Complète la tâche Camunda)
    @Transactional
    public void processManagerDecision(Long requestId, boolean approved, String comment) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // Trouver la tâche Camunda associée à cette demande
        Task task = taskService.createTaskQuery()
                .processInstanceId(request.getCamundaProcessInstanceId())
                .taskDefinitionKey("Activity_ManagerApproval") // ID de la tâche dans le BPMN
                .singleResult();

        if (task == null) {
            throw new RuntimeException("Aucune tâche d'approbation en cours pour cette demande.");
        }

        // Mettre à jour les données métier
        request.setManagerComments(comment);
        // Le statut final sera mis à jour par le Delegate Camunda, 
        // ou on peut le faire ici si on veut un retour immédiat UI.
        // Pour être propre, on passe la variable à Camunda.
        
        Map<String, Object> variables = Map.of(
            "approved", approved,
            "managerComments", comment
        );

        taskService.complete(task.getId(), variables);
    }
}