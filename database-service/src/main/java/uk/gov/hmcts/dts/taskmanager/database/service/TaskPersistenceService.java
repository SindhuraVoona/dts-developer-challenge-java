package uk.gov.hmcts.dts.taskmanager.database.service;

import java.util.List;

import org.springframework.stereotype.Service;

import uk.gov.hmcts.dts.taskmanager.database.dto.TaskCreateRequest;
import uk.gov.hmcts.dts.taskmanager.database.dto.TaskResponse;
import uk.gov.hmcts.dts.taskmanager.database.exception.TaskNotFoundException;
import uk.gov.hmcts.dts.taskmanager.database.mapper.TaskRecordMapper;
import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;
import uk.gov.hmcts.dts.taskmanager.entity.TaskStatus;
import uk.gov.hmcts.dts.taskmanager.repository.TaskRepository;

@Service
public class TaskPersistenceService {

    private final TaskRepository taskRepository;
    private final TaskRecordMapper taskRecordMapper;

    public TaskPersistenceService(TaskRepository taskRepository, TaskRecordMapper taskRecordMapper) {
        this.taskRepository = taskRepository;
        this.taskRecordMapper = taskRecordMapper;
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        TaskEntity savedTask = taskRepository.save(taskRecordMapper.toEntity(request));
        return taskRecordMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        return taskRecordMapper.toResponse(findTask(id));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
            .stream()
            .map(taskRecordMapper::toResponse)
            .toList();
    }

    public TaskResponse updateTaskStatus(Long id, TaskStatus status) {
        TaskEntity task = findTask(id);
        task.setStatus(status);
        TaskEntity savedTask = taskRepository.save(task);
        return taskRecordMapper.toResponse(savedTask);
    }

    public void deleteTask(Long id) {
        taskRepository.delete(findTask(id));
    }

    private TaskEntity findTask(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
