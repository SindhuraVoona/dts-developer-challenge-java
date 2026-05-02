import { useEffect, useState } from 'react';
import { getTasks } from '../api/taskApi';
import type { Task } from '../types/task';
import TaskItem from './TaskItem';

interface Props {
  refreshKey: number;
}

export default function TaskList({ refreshKey }: Props) {
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

  if (loading) {
    return <div className="state-message">Loading tasks…</div>;
  }

  if (error) {
    return (
      <div className="alert alert-error">
        {error}
        <button className="btn btn-secondary" onClick={load} style={{ marginLeft: 12 }}>
          Retry
        </button>
      </div>
    );
  }

  if (tasks.length === 0) {
    return <div className="state-message">No tasks yet. Create one above!</div>;
  }

  return (
    <div className="task-list">
      {tasks.map((task) => (
        <TaskItem key={task.id} task={task} onChanged={load} />
      ))}
    </div>
  );
}
