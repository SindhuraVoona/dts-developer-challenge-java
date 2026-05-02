import axios from 'axios';
import type { Task, TaskCreateRequest, TaskStatus } from '../types/task';

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
});

export const getTasks = (): Promise<Task[]> =>
  api.get<Task[]>('/tasks').then((r) => r.data);

export const createTask = (payload: TaskCreateRequest): Promise<Task> =>
  api.post<Task>('/tasks', payload).then((r) => r.data);

export const updateTaskStatus = (id: number, status: TaskStatus): Promise<Task> =>
  api.patch<Task>(`/tasks/${id}/status`, { status }).then((r) => r.data);

export const deleteTask = (id: number): Promise<void> =>
  api.delete(`/tasks/${id}`).then(() => undefined);
