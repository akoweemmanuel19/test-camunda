# Intégration de Camunda dans un projet Spring Boot — Exemple du projet

Ce document explique comment intégrer Camunda BPM dans une application Spring Boot en s'appuyant sur l'exemple présent dans ce dépôt. Les chemins et classes référencés correspondent au contenu de ce projet.

**Prérequis**
- **Java** : JDK 17+ (selon la configuration du projet).
- **Maven** : utilisé ici via le wrapper `./mvnw` (`mvnw.cmd` sur Windows).

**Fichiers importants dans ce projet**
- `pom.xml` : dépendances Camunda / Spring Boot.
- `src/main/resources/application.properties` : configuration (base de données, Camunda props).
- `src/main/resources/processes/*.bpmn` : définitions de processus (ex. `leave-request.bpmn`, `habilitation-request.bpmn`).
- `src/main/java/.../delegate/UpdateStatusDelegate.java` : exemple d'implémentation de `JavaDelegate`.
- `src/main/java/.../controller/` : contrôleurs REST / web (ex. `AdminCamundaController.java`, `AppController.java`).
- `src/main/java/.../service/` : logique métier et intégration avec les processus.
- `src/main/java/.../seed/AppSeeder.java` : initialisation / seed de données au démarrage.

**1) Dépendances Maven**

Pour intégrer Camunda dans Spring Boot, on ajoute les dépendances `camunda-bpm-spring-boot-starter` (ou la variante webapp si vous voulez l'UI embarquée). Exemple simplifié (vérifier versions dans `pom.xml` du projet) :

```xml
<!-- Extrait conceptuel -->
<dependency>
  <groupId>org.camunda.bpm.springboot</groupId>
  <artifactId>camunda-bpm-spring-boot-starter</artifactId>
  <version>${camunda.version}</version>
</dependency>
<!-- Pour l'interface web (Cockpit/Tasklist) : camunda-bpm-spring-boot-starter-webapp -->
```

Dans ce projet, la dépendance est déjà définie dans `pom.xml` — conservez-la et synchronisez Maven.

**2) Configuration (application.properties)**

Placez les fichiers BPMN dans `src/main/resources/processes/`. Spring Boot / Camunda les détecte automatiquement et les déploie au démarrage si la configuration le permet.

Exemples de propriétés utiles (voir `application.properties` du projet) :

```properties
spring.datasource.url=jdbc:h2:mem:camunda;DB_CLOSE_DELAY=-1
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

# Propriétés Camunda (exemples)
camunda.bpm.admin-user.id=demo
camunda.bpm.admin-user.password=demo
camunda.bpm.admin-user.firstname=Demo
```

Adaptez la datasource à votre base (Postgres, MySQL, etc.).

**3) Implémentation Java et BPMN**

- Définissez vos processus avec Camunda Modeler et enregistrez les fichiers `.bpmn` dans `src/main/resources/processes/`.
- Implémentez les tâches de service en Java en créant des classes qui implémentent `org.camunda.bpm.engine.delegate.JavaDelegate` (ex. `UpdateStatusDelegate.java`) et marquez-les comme beans Spring (`@Component`). Dans le BPMN, utilisez la `class` ou le `delegateExpression` pour lier le service.
- Utilisez des services Spring (dans `service/`) pour séparer la logique métier, et injectez-les dans vos delegates ou contrôleurs.
- Pour les accès persistence, utilisez les `Repository` Spring Data JPA (ex. `LeaveRequestRepository.java`, `UtilisateurRepository.java`) situés dans `repository/`.

Exemple rapide (concept) d'un delegate :

```java
@Component
public class UpdateStatusDelegate implements JavaDelegate {
  @Autowired
  private LeaveRequestService leaveService;

  @Override
  public void execute(DelegateExecution execution) {
    // logique qui met à jour l'entité et le process variables
  }
}
```

**4) Démarrage et exécution**

Dans la racine du module (`test-camunda`), lancez l'application avec Maven Wrapper :

```bash
./mvnw spring-boot:run
# ou sur Windows PowerShell : .\mvnw.cmd spring-boot:run
```

L'application démarre et déploie automatiquement les BPMN présents dans `src/main/resources/processes`.

**5) Accès à l'UI Camunda (si activée)**

Si vous avez inclus la dépendance `camunda-bpm-spring-boot-starter-webapp`, les consoles Camunda sont accessibles par défaut sur :

- `http://localhost:8080/camunda` (ou `/camunda/app/cockpit` / `/camunda/app/tasklist` selon la version)

Sinon, vous pouvez interagir avec les processus via l'API REST Camunda ou via les propres contrôleurs exposés dans `controller/` (ex. `AdminCamundaController`).

**6) Initialisation des données**

Ce projet inclut un seeder : `src/main/java/.../seed/AppSeeder.java` qui insère des données de test (utilisateurs, demandes, etc.). Le seeder est typiquement exécuté au démarrage via `@EventListener(ApplicationReadyEvent.class)` ou `@PostConstruct`.

**7) Exemples spécifiques de ce dépôt**
- Processus : `src/main/resources/processes/leave-request.bpmn` — processus de demande de congé.
- Delegate : `UpdateStatusDelegate.java` — met à jour le statut d'une demande.
- Contrôleurs : `AdminController`, `AppController` — points d'entrée pour démarrer et gérer des instances de processus.
- Services : `LeaveRequestService`, `PersonneService` — logique métier et interaction DB.
- Seeder : `AppSeeder.java` — insère des utilisateurs et données initiales.

Consultez ces fichiers pour voir les bonnes pratiques : séparation des responsabilités, usage de Spring Data pour persistance, et liaison BPMN → Delegate via `class`.

**8) Commandes utiles**
- Build : `./mvnw clean package`
- Run : `./mvnw spring-boot:run`
- Tests : `./mvnw test`

**Conseils et points d'attention**
- Veillez à configurer correctement la datasource pour la production (ne pas utiliser H2 en mémoire en prod).
- Version des dépendances Camunda : respectez les compatibilités entre Spring Boot et Camunda.
- Pour déboguer les processus, ajoutez des variables de process (logs) et testez les delegates unitaires.

---

Si vous voulez, je peux :
- ajouter un exemple minimal d'un `JavaDelegate` commenté dans le projet,
- ou générer un petit guide pas à pas (déployer un BPMN depuis Camunda Modeler et déclencher une instance).

Faites-moi savoir ce que vous préférez comme suite.
