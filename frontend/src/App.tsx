import { useState } from 'react';
import TaskForm from './components/TaskForm';
import TaskList from './components/TaskList';
import './App.css';

export default function App() {
  const [refreshKey, setRefreshKey] = useState(0);

  const refresh = () => setRefreshKey((k) => k + 1);

  return (
    <div className="app">
      <header className="app-header">
        <h1>HMCTS Task Manager</h1>
        <p>Manage caseworker tasks</p>
      </header>

      <main className="app-main">
        <section className="card">
          <TaskForm onCreated={refresh} />
        </section>

        <section className="card">
          <h2>All Tasks</h2>
          <TaskList refreshKey={refreshKey} />
        </section>
      </main>
    </div>
  );
}
