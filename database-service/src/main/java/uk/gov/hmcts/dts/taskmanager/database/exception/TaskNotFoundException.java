package uk.gov.hmcts.dts.taskmanager.database.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long taskId) {
        super("Task with id %d was not found".formatted(taskId));
    }
}
