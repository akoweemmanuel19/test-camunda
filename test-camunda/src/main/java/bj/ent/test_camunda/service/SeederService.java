package bj.ent.test_camunda.service;

import bj.ent.test_camunda.enums.Poste;
import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.repository.PersonneRepository;
import bj.ent.test_camunda.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeederService {

    private final  PersonneRepository personneRepository;
    private final UtilisateurRepository utilisateurRepository;

    public SeederService(PersonneRepository personneRepository, UtilisateurRepository utilisateurRepository) {
        this.personneRepository = personneRepository;
        this.utilisateurRepository = utilisateurRepository;
    }


    private Personne getOrCreatePersonne(String nom, String prenom, Poste poste) {
         return personneRepository.findByNomAndPrenom(nom, prenom)
                .orElseGet(() -> {
                    Personne newP = new Personne(nom, prenom, poste);
                    return personneRepository.save(newP); 
                });
    }
    
    @Transactional
    public void seedInitialUsers() {
        System.out.println("--- Démarrage de l'initialisation transactionnelle (V2) ---");

        // 1. CRÉATION SUPER ADMIN
        Personne adminPersonne = getOrCreatePersonne(
            "AKOWE", "Darius", Poste.ADMIN
        );

        utilisateurRepository.findByIdentifiant("akowedarius")
                .ifPresentOrElse(
                        u -> System.out.println("✅ Utilisateur akowedarius (Darius) existe déjà."),
                        () -> {
                            Utilisateur u = new Utilisateur();
                            u.setIdentifiant("akowedarius");
                            u.setMotDePasse("root"); 
                            u.setPersonne(adminPersonne); // Entité attachée utilisée
                            utilisateurRepository.save(u);
                            System.out.println("✅ Utilisateur Super Admin AKOWE Darius créé.");
                        }
                );

        // 2. CRÉATION DONNÉES DE TEST (Alice)
        Personne alicePersonne = getOrCreatePersonne(
            "Alice", "Demo", Poste.DEVELOPPEUR
        );

        utilisateurRepository.findByIdentifiant("alice_d")
                .ifPresentOrElse(
                        u -> { /* ... */ },
                        () -> {
                            Utilisateur u = new Utilisateur();
                            u.setIdentifiant("alice_d");
                            u.setMotDePasse("pass"); 
                            u.setPersonne(alicePersonne);
                            utilisateurRepository.save(u);
                            System.out.println("✅ Utilisateur de test Alice créé.");
                        }
                );
        
        // 3. CRÉATION DONNÉES DE TEST (Boss Manager)
        Personne bossPersonne = getOrCreatePersonne(
            "Manager", "Boss", Poste.MANAGER
        );
        
        utilisateurRepository.findByIdentifiant("boss_m")
                .ifPresentOrElse(
                        u -> { /* ... */ },
                        () -> {
                            Utilisateur u = new Utilisateur();
                            u.setIdentifiant("boss_m");
                            u.setMotDePasse("pass");
                            u.setPersonne(bossPersonne);
                            utilisateurRepository.save(u);
                            System.out.println("✅ Utilisateur de test Manager Boss créé.");
                        }
                );

        System.out.println("--- Initialisation terminée ---");
    }
}