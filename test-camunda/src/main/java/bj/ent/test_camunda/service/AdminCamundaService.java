package bj.ent.test_camunda.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bj.ent.test_camunda.dto.ProcessInstanceDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCamundaService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;

    /**
     * Déploie un fichier BPMN/XML dans le moteur Camunda.
     * 
     * @param deploymentName Le nom du déploiement.
     * @param file           Le fichier BPMN/XML à déployer.
     * @return L'objet Deployment créé.
     * @throws IOException Si le fichier ne peut pas être lu.
     */
    @Transactional
    public Deployment deployProcess(String deploymentName, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".bpmn") && !filename.endsWith(".xml"))) {
            throw new IllegalArgumentException("Le fichier doit être un fichier BPMN (.bpmn ou .xml).");
        }

        try (InputStream inputStream = file.getInputStream()) {
            return repositoryService.createDeployment()
                    .name(deploymentName)
                    .addInputStream(filename, inputStream)
                    .deploy();
        }
    }

    /**
     * Récupère toutes les instances de processus actives et les mappe en DTOs.
     * 
     * @return Liste des ProcessInstanceDto actives.
     */
    public List<ProcessInstanceDto> getRunningProcessInstances() {
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .active()
                .list();

        return instances.stream()
                .map(instance -> {
                    ProcessInstanceDto dto = new ProcessInstanceDto();
                    dto.setId(instance.getId());
                    dto.setProcessDefinitionId(instance.getProcessDefinitionId());
                    dto.setProcessDefinitionKey(instance.getProcessDefinitionKey());
                    dto.setBusinessKey(instance.getBusinessKey());

                    // L'ID de l'activité en cours est bien "instance.getActivityId()"
                    dto.setActivityId(getCurrentActivityId(instance.getId()));
                    // Pour obtenir la date de début (startTime), on doit interroger
                    // l'HistoryService
                    HistoricProcessInstance historic = historyService.createHistoricProcessInstanceQuery()
                            .processInstanceId(instance.getId())
                            .singleResult();

                    if (historic != null) {
                        dto.setStartTime(historic.getStartTime());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Récupère le diagramme BPMN XML (contenu) pour une définition de processus
     * donnée.
     * 
     * @param processDefinitionId L'ID de la définition de processus.
     * @return Le contenu XML/BPMN en chaîne de caractères.
     */
    public String getProcessDiagramXml(String processDefinitionId) {
        try (InputStream resourceStream = repositoryService.getProcessModel(processDefinitionId)) {
            return new String(resourceStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du diagramme BPMN pour ID: " + processDefinitionId,
                    e);
        }
    }

    /**
     * Termine (supprime) une instance de processus Camunda.
     * 
     * @param instanceId L'ID de l'instance à terminer.
     */
    @Transactional
    public void terminateProcessInstance(String instanceId) {
        runtimeService.deleteProcessInstance(
                instanceId,
                "Terminé par l'administrateur" // Raison de la suppression
        );
    }

    /**
     * Récupère la liste des IDs des activités actuellement en cours pour une
     * instance.
     * 
     * @param instanceId L'ID de l'instance de processus.
     * @return L'ID de l'activité en cours (une tâche utilisateur ou autre).
     */
    private String getCurrentActivityId(String instanceId) {
        try {
            // Récupère l'arbre d'activités pour l'instance
            org.camunda.bpm.engine.runtime.ActivityInstance activityInstance = runtimeService
                    .getActivityInstance(instanceId);

            if (activityInstance == null) {
                return null;
            }

            // Nous cherchons le premier ID d'activité réel (qui n'est pas le root
            // 'ProcessDefinition')
            // C'est l'ID de l'élément BPMN en cours d'exécution.
            if (activityInstance.getChildActivityInstances().length > 0) {
                // Pour les processus simples, le premier enfant est souvent l'activité réelle
                return activityInstance.getChildActivityInstances()[0].getActivityId();
            }

            // Si c'est un processus très simple ou une seule activité, l'ID peut être au
            // niveau racine
            return activityInstance.getActivityId();

        } catch (Exception e) {
            // Log l'erreur mais ne bloque pas la réponse
            System.err.println(
                    "Erreur de récupération de l'activité pour l'instance " + instanceId + ": " + e.getMessage());
            return "N/A (Erreur)";
        }
    }
}