package bj.ent.test_camunda.seed;

import bj.ent.test_camunda.model.Personne;
import bj.ent.test_camunda.enums.Poste; // Attention: Assurez-vous que votre Enum est bien dans ce package
import bj.ent.test_camunda.model.Utilisateur;
import bj.ent.test_camunda.repository.PersonneRepository;
import bj.ent.test_camunda.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AppSeeder {

    // --- Fonction Helper pour Créer ou Récupérer une Personne ---
    // Cette fonction garantit que l'entité Personne retournée est gérée (managed)
    // par la transaction actuelle si elle est créée ou attachée si elle est trouvée.
    private Personne getOrCreatePersonne(PersonneRepository repo, String nom, String prenom, Poste poste) {
         return repo.findByNomAndPrenom(nom, prenom)
                .orElseGet(() -> {
                    Personne p = new Personne(nom, prenom, poste);
                    // L'appel à save() attache l'entité à la session JPA
                    return repo.save(p); 
                });
    }
    
    // --- Lancement du Seeder ---
    @Bean
    @Transactional // Nécessaire pour les opérations de base de données
    public CommandLineRunner initDatabase(
            PersonneRepository personneRepository,
            UtilisateurRepository utilisateurRepository
    ) {
        return args -> {
            System.out.println("--- Initialisation des données de base ---");

            // =========================================================
            // 1. CRÉATION DU SUPER ADMIN : AKOWE Darius
            // =========================================================

            Personne adminPersonne = getOrCreatePersonne(
                personneRepository, 
                "AKOWE", 
                "Darius", 
                Poste.MANAGER // Le super admin est aussi un manager
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
                                u.setPersonne(adminPersonne); // L'entité Personne est attachée
                                utilisateurRepository.save(u);
                                System.out.println("✅ Utilisateur Super Admin AKOWE Darius créé.");
                            }
                    );

            // =========================================================
            // 2. CRÉATION DES DONNÉES DE TEST (Employé et Manager)
            // =========================================================

            // Employé de test (Alice)
            Personne alicePersonne = getOrCreatePersonne(
                personneRepository, 
                "Alice", 
                "Demo", 
                Poste.DEVELOPPEUR
            );

            utilisateurRepository.findByIdentifiant("alice_d")
                    .ifPresentOrElse(
                            u -> { /* Do nothing if exists */ },
                            () -> {
                                Utilisateur u = new Utilisateur();
                                u.setIdentifiant("alice_d");
                                u.setMotDePasse("pass"); 
                                u.setPersonne(alicePersonne);
                                utilisateurRepository.save(u);
                                System.out.println("✅ Utilisateur de test Alice créé.");
                            }
                    );
            
            // Manager de test (Boss)
            Personne bossPersonne = getOrCreatePersonne(
                personneRepository,
                "Manager",
                "Boss",
                Poste.MANAGER
            );
            
            utilisateurRepository.findByIdentifiant("boss_m")
                    .ifPresentOrElse(
                            u -> { /* Do nothing if exists */ },
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
        };
    }
}