package bj.ent.test_camunda.service;

import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur connexion(String identifiant, String motDePasse) {
        return utilisateurRepository.findByIdentifiant(identifiant)
                .filter(u -> u.getMotDePasse().equals(motDePasse))
                .orElseThrow(() -> new RuntimeException("Identifiants incorrects"));
    }
    
    public Utilisateur getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Utilisateur inconnu"));
    }

        public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    @Transactional
    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.findByIdentifiant(utilisateur.getIdentifiant()).isPresent()) {
            throw new RuntimeException("Cet identifiant est déjà utilisé.");
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur updateUtilisateur(Long id, Utilisateur details) {
        Utilisateur current = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        current.setIdentifiant(details.getIdentifiant());
        if (details.getMotDePasse() != null && !details.getMotDePasse().isEmpty()) {
            current.setMotDePasse(details.getMotDePasse());
        }
        // Mise à jour de la personne liée si nécessaire
        if (details.getPersonne() != null) {
            current.setPersonne(details.getPersonne());
        }
        
        return utilisateurRepository.save(current);
    }

    @Transactional
    public void deleteUtilisateur(Long id) {
        utilisateurRepository.deleteById(id);
    }
}