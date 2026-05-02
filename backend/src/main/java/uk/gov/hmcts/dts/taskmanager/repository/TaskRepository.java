package uk.gov.hmcts.dts.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.gov.hmcts.dts.taskmanager.entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
