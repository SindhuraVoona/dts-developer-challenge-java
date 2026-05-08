import { useState, useEffect } from 'react';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import { updateTaskStatus } from './api/taskApi';
import type { Task, TaskStatus } from './types/task';
import './App.css';

const STATUSES: TaskStatus[] = ['TODO', 'IN_PROGRESS', 'COMPLETED'];
const STATUS_LABELS: Record<TaskStatus, string> = {
  TODO: 'To Do',
  IN_PROGRESS: 'In Progress',
  COMPLETED: 'Completed',
};

export default function App() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [apiConnected, setApiConnected] = useState<boolean | null>(null);
  const [editingTask, setEditingTask] = useState<Task | null>(null);
  const [editStatus, setEditStatus] = useState<TaskStatus>('TODO');
  const [updating, setUpdating] = useState(false);

  const refresh = () => setRefreshKey((k) => k + 1);

  useEffect(() => {
    fetch('/api/tasks')
      .then((r) => setApiConnected(r.ok))
      .catch(() => setApiConnected(false));
  }, []);

  const startEdit = (task: Task) => {
    setEditingTask(task);
    setEditStatus(task.status);
    setTimeout(() => {
      document.getElementById('edit-section')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }, 50);
  };

  const cancelEdit = () => setEditingTask(null);

  const handleUpdateStatus = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!editingTask) return;
    setUpdating(true);
    try {
      await updateTaskStatus(editingTask.id, editStatus);
      refresh();
      setEditingTask(null);
    } finally {
      setUpdating(false);
    }
  };

  const pillClass =
    apiConnected === true
      ? 'status-pill status-pill--connected'
      : apiConnected === false
      ? 'status-pill status-pill--disconnected'
      : 'status-pill';

  const pillText =
    apiConnected === true
      ? 'API: Connected'
      : apiConnected === false
      ? 'API: Not connected'
      : 'API: Checking…';

  return (
    <div className="app">
      <header className="header">
        <div>
          <h1>Task Manager</h1>
          <p className="subtitle">Create, update, and track your tasks</p>
        </div>
        <div className={pillClass}>{pillText}</div>
      </header>

      <section className="card">
        <TaskForm onCreated={refresh} />
      </section>

      <section className="card">
        <TaskList refreshKey={refreshKey} onRefresh={refresh} onEdit={startEdit} />
      </section>

      {editingTask && (
        <section className="card" id="edit-section">
          <h2>Update Task Status</h2>
          <form className="form" onSubmit={handleUpdateStatus}>
            <div className="row">
              <div className="field">
                <label>Task</label>
                <input type="text" value={editingTask.title} disabled />
              </div>
              <div className="field">
                <label htmlFor="edit-status">New Status</label>
                <select
                  id="edit-status"
                  value={editStatus}
                  onChange={(e) => setEditStatus(e.target.value as TaskStatus)}
                >
                  {STATUSES.map((s) => (
                    <option key={s} value={s}>{STATUS_LABELS[s]}</option>
                  ))}
                </select>
              </div>
            </div>
            <div className="actions">
              <button type="submit" className="btn primary" disabled={updating}>
                {updating ? 'Updating…' : 'Update Status'}
              </button>
              <button type="button" className="btn" onClick={cancelEdit}>
                Cancel
              </button>
            </div>
          </form>
        </section>
      )}
    </div>
  );
}
