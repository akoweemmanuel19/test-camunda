package bj.ent.test_camunda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import org.camunda.bpm.engine.runtime.ProcessInstance;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstanceDto {
    private String id;
    private String processDefinitionId;
    private String processDefinitionKey;
    private String businessKey;
    private String activityId;
    private Date startTime;

    // Cette méthode de construction est essentielle pour mapper l'objet Camunda
    public ProcessInstanceDto(ProcessInstance instance) {
        this.id = instance.getId();
        this.processDefinitionId = instance.getProcessDefinitionId();
        this.processDefinitionKey = instance.getProcessDefinitionKey();
        this.businessKey = instance.getBusinessKey();
        this.activityId = instance.getProcessInstanceId();
        this.startTime = null;
    }
}