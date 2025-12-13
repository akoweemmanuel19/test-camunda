package bj.ent.test_camunda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import bj.ent.test_camunda.enums.StatutDemande;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Personne employee;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Personne manager;

    private Integer numberOfDays;
    
    @Enumerated(EnumType.STRING)
    private StatutDemande statut; // BROUILLON, EN_ATTENTE, etc.

    private String managerComments;
    private String camundaProcessInstanceId;
    private LocalDateTime dateCreation;

    public LeaveRequest() {}

    public LeaveRequest(Personne employee, Personne manager, Integer numberOfDays) {
        this.employee = employee;
        this.manager = manager;
        this.numberOfDays = numberOfDays;
        this.statut = StatutDemande.BROUILLON; // Par défaut en brouillon
        this.dateCreation = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Personne getEmployee() { return employee; }
    public void setEmployee(Personne employee) { this.employee = employee; }
    public Personne getManager() { return manager; }
    public void setManager(Personne manager) { this.manager = manager; }
    public Integer getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(Integer numberOfDays) { this.numberOfDays = numberOfDays; }
    public StatutDemande getStatut() { return statut; }
    public void setStatut(StatutDemande statut) { this.statut = statut; }
    public String getManagerComments() { return managerComments; }
    public void setManagerComments(String managerComments) { this.managerComments = managerComments; }
    public String getCamundaProcessInstanceId() { return camundaProcessInstanceId; }
    public void setCamundaProcessInstanceId(String camundaProcessInstanceId) { this.camundaProcessInstanceId = camundaProcessInstanceId; }
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}