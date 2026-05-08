import { useEffect, useState } from 'react';
import { getTasks } from '../api/taskApi';
import type { Task } from '../types/task';
import TaskItem from './TaskItem';

interface Props {
  refreshKey: number;
  onRefresh: () => void;
  onEdit: (task: Task) => void;
}

export default function TaskList({ refreshKey, onRefresh, onEdit }: Props) {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const load = () => {
    setLoading(true);
    setError(null);
    getTasks()
      .then(setTasks)
      .catch(() => setError('Could not load tasks. Is the backend running?'))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, [refreshKey]);

  return (
    <>
      <div className="card-header">
        <h2>Tasks</h2>
        <button className="btn" onClick={onRefresh}>Refresh</button>
      </div>

      {error && (
        <div className="alert alert-error">
          {error}
          <button className="btn" onClick={load} style={{ marginLeft: 12 }}>Retry</button>
        </div>
      )}

      {loading && <div className="loading">Loading tasks…</div>}

      {!loading && !error && tasks.length === 0 && (
        <div className="empty">No tasks yet. Create one above.</div>
      )}

      {!loading && !error && tasks.length > 0 && (
        <div className="table">
          {tasks.map((task) => (
            <TaskItem key={task.id} task={task} onChanged={load} onEdit={onEdit} />
          ))}
        </div>
      )}
    </>
  );
}
