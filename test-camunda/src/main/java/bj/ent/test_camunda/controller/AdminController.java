package bj.ent.test_camunda.controller;

import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.service.PersonneService;
import bj.ent.test_camunda.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final UtilisateurService utilisateurService;
    private final PersonneService personneService;

    public AdminController(UtilisateurService utilisateurService, PersonneService personneService) {
        this.utilisateurService = utilisateurService;
        this.personneService = personneService;
    }

    // --- CRUD PERSONNES ---
    @GetMapping("/personnes")
    public List<Personne> listPersonnes() {
        return personneService.getAll();
    }

    @PostMapping("/personnes")
    public Personne createPersonne(@RequestBody Personne p) {
        return personneService.save(p);
    }

    @DeleteMapping("/personnes/{id}")
    public ResponseEntity<?> deletePersonne(@PathVariable Long id) {
        personneService.delete(id);
        return ResponseEntity.ok().build();
    }

    // --- CRUD users ---
    @GetMapping("/users")
    public List<Utilisateur> listUtilisateurs() {
        return utilisateurService.getAll();
    }

    @PostMapping("/users")
    public Utilisateur createUtilisateur(@RequestBody Utilisateur u) {
        return utilisateurService.createUtilisateur(u);
    }

    @PutMapping("/users/{id}")
    public Utilisateur updateUtilisateur(@PathVariable Long id, @RequestBody Utilisateur u) {
        return utilisateurService.updateUtilisateur(id, u);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.ok().build();
    }
}