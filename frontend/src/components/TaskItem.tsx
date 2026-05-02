import { useState } from 'react';
import { updateTaskStatus, deleteTask } from '../api/taskApi';
import type { Task, TaskStatus } from '../types/task';

interface Props {
  task: Task;
  onChanged: () => void;
}

const STATUSES: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'COMPLETED'];

const STATUS_LABELS: Record<TaskStatus, string> = {
  TODO: 'To Do',
  IN_PROGRESS: 'In Progress',
  COMPLETED: 'Completed',
};

const STATUS_CLASS: Record<TaskStatus, string> = {
  TODO: 'badge-todo',
  IN_PROGRESS: 'badge-progress',
  COMPLETED: 'badge-done',
};

export default function TaskItem({ task, onChanged }: Props) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleStatusChange = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    setError(null);
    setLoading(true);
    try {
      await updateTaskStatus(task.id, e.target.value as TaskStatus);
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
    <div className={`task-item ${loading ? 'task-item--loading' : ''}`}>
      <div className="task-item__header">
        <span className={`badge ${STATUS_CLASS[task.status]}`}>
          {STATUS_LABELS[task.status]}
        </span>
        <span className="task-item__due">Due: {due}</span>
      </div>

      <h3 className="task-item__title">{task.title}</h3>

      {task.description && (
        <p className="task-item__description">{task.description}</p>
      )}

      {error && <div className="alert alert-error">{error}</div>}

      <div className="task-item__actions">
        <select
          value={task.status}
          onChange={handleStatusChange}
          disabled={loading}
          className="status-select"
          aria-label="Change status"
        >
          {STATUSES.map((s) => (
            <option key={s} value={s}>
              {STATUS_LABELS[s]}
            </option>
          ))}
        </select>

        <button
          className="btn btn-danger"
          onClick={handleDelete}
          disabled={loading}
        >
          Delete
        </button>
      </div>
    </div>
  );
}
