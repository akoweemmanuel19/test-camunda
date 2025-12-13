package bj.ent.test_camunda.controller;

import bj.ent.test_camunda.dto.ProcessInstanceDto;
import bj.ent.test_camunda.service.AdminCamundaService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/camunda")
@RequiredArgsConstructor
// Assurez-vous que seul l'ADMIN peut accéder à ces endpoints via Spring Security
public class AdminCamundaController {

    private final AdminCamundaService adminCamundaService;

    // POST /api/admin/camunda/deploy
    @PostMapping("/deploy")
    public ResponseEntity<Deployment> deployProcess(
            @RequestParam("deploymentName") String deploymentName,
            @RequestParam("file") MultipartFile file) {
        try {
            Deployment deployment = adminCamundaService.deployProcess(deploymentName, file);
            return ResponseEntity.ok(deployment);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // GET /api/admin/camunda/processes/running
    @GetMapping("/processes/running")
    public ResponseEntity<?> getRunningProcessInstances() {
        List<ProcessInstanceDto> instances = adminCamundaService.getRunningProcessInstances();
        return ResponseEntity.ok(instances);
    }

    // DELETE /api/admin/camunda/processes/{instanceId}/terminate
    @DeleteMapping("/processes/{instanceId}/terminate")
    public ResponseEntity<Void> terminateProcessInstance(@PathVariable String instanceId) {
        adminCamundaService.terminateProcessInstance(instanceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/diagram/{processDefinitionId}", produces = "text/xml")
    public ResponseEntity<String> getProcessDiagram(@PathVariable String processDefinitionId) {
        try {
            String xml = adminCamundaService.getProcessDiagramXml(processDefinitionId);
            return ResponseEntity.ok(xml);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Diagramme non trouvé: " + e.getMessage());
        }
    }
}