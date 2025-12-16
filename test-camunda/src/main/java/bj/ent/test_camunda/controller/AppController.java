package bj.ent.test_camunda.controller;

import bj.ent.test_camunda.dto.DecisionDto;
import bj.ent.test_camunda.dto.LoginDto;
import bj.ent.test_camunda.enums.Poste;
import bj.ent.test_camunda.model.LeaveRequest;
import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.repository.PersonneRepository;
import bj.ent.test_camunda.service.LeaveRequestService;
import bj.ent.test_camunda.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Pour Nuxt
public class AppController {

    private final LeaveRequestService leaveRequestService;
    private final UtilisateurService utilisateurService;
    private final PersonneRepository personneRepository;

    public AppController(LeaveRequestService leaveRequestService, UtilisateurService utilisateurService,
            PersonneRepository personneRepository) {
        this.leaveRequestService = leaveRequestService;
        this.utilisateurService = utilisateurService;
        this.personneRepository = personneRepository;
    }

    // --- AUTHENTIFICATION (Simplifiée) ---
    @PostMapping("/login")
    public ResponseEntity<Utilisateur> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(utilisateurService.connexion(loginDto.identifiant(), loginDto.motDePasse()));
    }

    // --- EMPLOYE : Créer Brouillon ---
    @PostMapping("/requests/draft")
    public ResponseEntity<LeaveRequest> createDraft(@RequestParam Long userId, @RequestParam Integer days,
            @RequestParam Long managerId) {
        // Note: userId correspond ici à l'ID de la Personne liée à l'utilisateur
        Utilisateur u = utilisateurService.getUtilisateurById(userId);
        return ResponseEntity.ok(leaveRequestService.createDraft(u.getPersonne().getId(), days, managerId));
    }

    // --- EMPLOYE : Modifier Brouillon ---
    @PutMapping("/requests/{id}")
    public ResponseEntity<LeaveRequest> updateDraft(@PathVariable Long id, @RequestParam Integer days) {
        return ResponseEntity.ok(leaveRequestService.updateDraft(id, days));
    }

    // --- EMPLOYE : Supprimer Brouillon ---
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteDraft(@PathVariable Long id) {
        leaveRequestService.deleteDraft(id);
        return ResponseEntity.ok().build();
    }

    // --- EMPLOYE : Envoyer au Manager ---
    @PostMapping("/requests/{id}/send")
    public ResponseEntity<LeaveRequest> sendRequest(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.sendRequestToManager(id));
    }

    // --- EMPLOYE : Voir mes demandes ---
    @GetMapping("/my-requests")
    public ResponseEntity<List<LeaveRequest>> getMyRequests(@RequestParam Long userId) {
        Utilisateur u = utilisateurService.getUtilisateurById(userId);
        return ResponseEntity.ok(leaveRequestService.getEmployeeRequests(u.getPersonne().getId()));
    }

    @GetMapping("/manager")
    public List<Personne> getManagers() {
        return personneRepository.findByPoste(Poste.MANAGER);
    }

    // --- MANAGER : Voir les demandes à traiter ---
    @GetMapping("/manager/requests")
    public ResponseEntity<List<LeaveRequest>> getManagerRequests(@RequestParam Long userId) {
        Utilisateur u = utilisateurService.getUtilisateurById(userId);
        return ResponseEntity.ok(leaveRequestService.getManagerRequests(u.getPersonne().getId()));
    }

    // --- MANAGER : Valider/Refuser ---
    @PostMapping("/manager/requests/{id}/decide")
    public ResponseEntity<String> decideRequest(@PathVariable Long id, @RequestBody DecisionDto decision) {
        leaveRequestService.processManagerDecision(id, decision.approved(), decision.comment());
        return ResponseEntity.ok("Décision enregistrée");
    }
}


