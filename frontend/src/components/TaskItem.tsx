import { useState } from 'react';
import { updateTaskStatus, deleteTask } from '../api/taskApi';
import type { Task, TaskStatus } from '../types/task';

interface Props {
  task: Task;
  onChanged: () => void;
  onEdit: (task: Task) => void;
}

const STATUS_LABELS: Record<TaskStatus, string> = {
  TODO: 'To Do',
  IN_PROGRESS: 'In Progress',
  COMPLETED: 'Completed',
};

const STATUS_CLASS: Record<TaskStatus, string> = {
  TODO: 'pending',
  IN_PROGRESS: 'in-progress',
  COMPLETED: 'completed',
};

const STATUS_ORDER: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'COMPLETED'];

function nextStatus(current: TaskStatus): TaskStatus {
  const index = STATUS_ORDER.indexOf(current);
  return STATUS_ORDER[(index + 1) % STATUS_ORDER.length];
}

export default function TaskItem({ task, onChanged, onEdit }: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleNextStatus = async () => {
    setError(null);
    setLoading(true);
    try {
      await updateTaskStatus(task.id, nextStatus(task.status));
      onChanged();
    } catch {
      setError('Failed to update status.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm(`Delete task "${task.title}"?`)) return;
    setError(null);
    setLoading(true);
    try {
      await deleteTask(task.id);
      onChanged();
    } catch {
      setError('Failed to delete task.');
    } finally {
      setLoading(false);
    }
  };

  const due = new Date(task.dueDateTime).toLocaleString('en-GB', {
    dateStyle: 'medium',
    timeStyle: 'short',
  });

  return (
    <>
      <div
        className="task"
        style={loading ? { opacity: 0.5, pointerEvents: 'none' } : undefined}
      >
        <div>
          <div className="task-title">{task.title}</div>
          <div className="task-meta">{task.description || 'No description'}</div>
        </div>

        <div>
          <span className={`badge ${STATUS_CLASS[task.status]}`}>
            {STATUS_LABELS[task.status]}
          </span>
        </div>

        <div>
          <div className="task-meta">Due</div>
          <div>{due}</div>
        </div>

        <div className="task-actions">
          <button className="btn" onClick={() => onEdit(task)}>Edit</button>
          <button className="btn" onClick={handleNextStatus}>Next Status</button>
          <button className="btn danger" onClick={handleDelete}>Delete</button>
        </div>
      </div>

      {error && (
        <div className="alert alert-error" style={{ marginTop: -4 }}>{error}</div>
      )}
    </>
  );
}
