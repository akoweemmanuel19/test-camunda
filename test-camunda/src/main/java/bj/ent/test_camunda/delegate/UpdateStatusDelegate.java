package bj.ent.test_camunda.delegate;

import bj.ent.test_camunda.model.LeaveRequest;
import bj.ent.test_camunda.enums.StatutDemande;
import bj.ent.test_camunda.repository.LeaveRequestRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateStatusDelegate implements JavaDelegate {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // 1. Récupérer les variables
        Long requestId = (Long) execution.getVariable("leaveRequestId");
        Boolean approved = (Boolean) execution.getVariable("approved");

        // 2. Récupérer l'entité en base
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable dans le Delegate"));

        // 3. Mettre à jour le statut selon la décision
        if (Boolean.TRUE.equals(approved)) {
            request.setStatut(StatutDemande.APPROUVEE);
            System.out.println("✅ Demande " + requestId + " : APPROUVÉE par le manager.");
        } else {
            request.setStatut(StatutDemande.REJETEE);
            System.out.println("❌ Demande " + requestId + " : REJETÉE par le manager.");
        }

        // 4. Sauvegarder
        leaveRequestRepository.save(request);
    }
}