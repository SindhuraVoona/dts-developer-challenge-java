package uk.gov.hmcts.dts.taskmanager.database.mapper;

import org.springframework.stereotype.Component;

import uk.gov.hmcts.dts.taskmanager.database.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.database.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;

@Component
public class TaskRecordMapper {

    public TaskEntity toEntity(TaskCreateRequest request) {
        TaskEntity task = new TaskEntity();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDueDateTime(request.dueDateTime());
        return task;
    }

    public TaskResponse toResponse(TaskEntity entity) {
        return new TaskResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatus(),
            entity.getDueDateTime(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
