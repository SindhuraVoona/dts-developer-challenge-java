import { useState } from 'react';
import { createTask } from '../api/taskApi';
import type { TaskCreateRequest, TaskStatus } from '../types/task';

interface Props {
  onCreated: () => void;
}

const STATUSES: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'COMPLETED'];

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
      const payload: TaskCreateRequest = {
        title,
        description: description || undefined,
        status,
        dueDateTime: dueDateTime.replace('T', 'T').slice(0, 19),
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

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <h2>New Task</h2>

      {error && <div className="alert alert-error">{error}</div>}

      <div className="form-group">
        <label htmlFor="title">Title *</label>
        <input
          id="title"
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Task title"
          required
        />
      </div>

      <div className="form-group">
        <label htmlFor="description">Description</label>
        <textarea
          id="description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Optional description"
          rows={3}
        />
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="status">Status *</label>
          <select
            id="status"
            value={status}
            onChange={(e) => setStatus(e.target.value as TaskStatus)}
          >
            {STATUSES.map((s) => (
              <option key={s} value={s}>
                {s.replace('_', ' ')}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="dueDateTime">Due Date &amp; Time *</label>
          <input
            id="dueDateTime"
            type="datetime-local"
            value={dueDateTime}
            onChange={(e) => setDueDateTime(e.target.value)}
            required
          />
        </div>
      </div>

      <button type="submit" className="btn btn-primary" disabled={submitting}>
        {submitting ? 'Creating…' : 'Create Task'}
      </button>
    </form>
  );
}
