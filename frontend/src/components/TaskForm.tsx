import { useState } from 'react';
import { createTask } from '../api/taskApi';
import type { TaskCreateRequest, TaskStatus } from '../types/task';

interface Props {
  onCreated: () => void;
}

const STATUSES: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'COMPLETED'];
const STATUS_LABELS: Record<TaskStatus, string> = {
  TODO: 'To Do',
  IN_PROGRESS: 'In Progress',
  COMPLETED: 'Completed',
};

export default function TaskForm({ onCreated }: Props) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState<TaskStatus>('TODO');
  const [dueDateTime, setDueDateTime] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setSubmitting(true);
    try {
      // datetime-local gives "YYYY-MM-DDTHH:mm" (16 chars); backend needs seconds too
      const normalised = dueDateTime.length === 16 ? `${dueDateTime}:00` : dueDateTime.slice(0, 19);
      const payload: TaskCreateRequest = {
        title,
        description: description || undefined,
        status,
        dueDateTime: normalised,
      };
      await createTask(payload);
      setTitle('');
      setDescription('');
      setStatus('TODO');
      setDueDateTime('');
      onCreated();
    } catch {
      setError('Failed to create task. Please check your input and try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const handleClear = () => {
    setTitle('');
    setDescription('');
    setStatus('TODO');
    setDueDateTime('');
    setError(null);
  };

  return (
    <>
      <h2>Create Task</h2>

      {error && <div className="alert alert-error">{error}</div>}

      <form className="form" onSubmit={handleSubmit}>
        <div className="row">
          <div className="field">
            <label htmlFor="title">Title</label>
            <input
              id="title"
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Task title"
              required
            />
          </div>
          <div className="field">
            <label htmlFor="status">Status</label>
            <select
              id="status"
              value={status}
              onChange={(e) => setStatus(e.target.value as TaskStatus)}
            >
              {STATUSES.map((s) => (
                <option key={s} value={s}>{STATUS_LABELS[s]}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="row">
          <div className="field">
            <label htmlFor="description">Description (optional)</label>
            <input
              id="description"
              type="text"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Task description"
            />
          </div>
          <div className="field">
            <label htmlFor="dueDateTime">Due date/time</label>
            <input
              id="dueDateTime"
              type="datetime-local"
              value={dueDateTime}
              onChange={(e) => setDueDateTime(e.target.value)}
              required
            />
          </div>
        </div>

        <div className="actions">
          <button type="submit" className="btn primary" disabled={submitting}>
            {submitting ? 'Creating…' : 'Create Task'}
          </button>
          <button type="button" className="btn" onClick={handleClear}>
            Clear
          </button>
        </div>
      </form>
    </>
  );
}
