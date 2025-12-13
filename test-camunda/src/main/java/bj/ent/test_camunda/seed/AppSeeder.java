package bj.ent.test_camunda.seed;

import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.enums.Poste;
import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.repository.PersonneRepository;
import bj.ent.test_camunda.repository.UtilisateurRepository;
import jakarta.persistence.EntityManager; // Importation nécessaire
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AppSeeder {

    // Injecter l'EntityManager pour utiliser merge()
    private final EntityManager entityManager;

    public AppSeeder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Fonction Helper pour Créer, Récupérer, et S'ASSURER que la Personne est attachée (MERGED)
    private Personne getOrCreateAndMergePersonne(PersonneRepository repo, String nom, String prenom, Poste poste) {
         Personne p = repo.findByNomAndPrenom(nom, prenom)
                .orElseGet(() -> {
                    Personne newP = new Personne(nom, prenom, poste);
                    // Sauvegarde initiale (optionnel, mais garde la logique de création)
                    return repo.save(newP); 
                });
        
        // !!! C'EST LA CLÉ !!! : Raccrocher l'entité à la session si elle était détachée
        // merge() retourne l'instance managée que nous allons utiliser.
        return entityManager.merge(p);
    }
    
    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            PersonneRepository personneRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        return args -> {
            System.out.println("--- Initialisation des données de base ---");

            // =========================================================
            // 1. CRÉATION DU SUPER ADMIN : AKOWE Darius
            // =========================================================

            Personne adminPersonneAttached = getOrCreateAndMergePersonne(
                personneRepository, 
                "AKOWE", 
                "Darius", 
                Poste.MANAGER
            );

            utilisateurRepository.findByIdentifiant("akowedarius")
                    .ifPresentOrElse(
                            u -> {
                                System.out.println("✅ Utilisateur akowedarius (Darius) existe déjà.");
                            },
                            () -> {
                                Utilisateur u = new Utilisateur();
                                u.setIdentifiant("akowedarius");
                                u.setMotDePasse("root"); 
                                u.setPersonne(adminPersonneAttached); // Entité garantie attachée
                                utilisateurRepository.save(u);
                                System.out.println("✅ Utilisateur Super Admin AKOWE Darius créé.");
                            }
                    );

            // =========================================================
            // 2. CRÉATION DES DONNÉES DE TEST
            // =========================================================

            Personne alicePersonneAttached = getOrCreateAndMergePersonne(
                personneRepository, 
                "Alice", 
                "Demo", 
                Poste.DEVELOPPEUR
            );

            utilisateurRepository.findByIdentifiant("alice_d")
                    .ifPresentOrElse(
                            u -> { /* ... */ },
                            () -> {
                                Utilisateur u = new Utilisateur();
                                u.setIdentifiant("alice_d");
                                u.setMotDePasse("pass"); 
                                u.setPersonne(alicePersonneAttached);
                                utilisateurRepository.save(u);
                                System.out.println("✅ Utilisateur de test Alice créé.");
                            }
                    );
            
            Personne bossPersonneAttached = getOrCreateAndMergePersonne(
                personneRepository,
                "Manager",
                "Boss",
                Poste.MANAGER
            );
            
            utilisateurRepository.findByIdentifiant("boss_m")
                    .ifPresentOrElse(
                            u -> { /* ... */ },
                            () -> {
                                Utilisateur u = new Utilisateur();
                                u.setIdentifiant("boss_m");
                                u.setMotDePasse("pass");
                                u.setPersonne(bossPersonneAttached);
                                utilisateurRepository.save(u);
                                System.out.println("✅ Utilisateur de test Manager Boss créé.");
                            }
                    );

            System.out.println("--- Initialisation terminée ---");
        };
    }
}