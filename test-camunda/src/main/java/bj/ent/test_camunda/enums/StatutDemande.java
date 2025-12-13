package bj.ent.test_camunda.enums;

public enum StatutDemande {
    BROUILLON,  // Modifiable par l'employé, non visible par le manager
    EN_ATTENTE, // Envoyé au manager (Processus Camunda démarré)
    APPROUVEE,  // Validé
    REJETEE     // Refusé
}