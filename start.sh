#!/usr/bin/env bash
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

# ── Resolve Java 21 ────────────────────────────────────────────────────────────
JAVA_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null) || {
  echo "ERROR: Java 21 not found. Install it and try again."
  exit 1
}
export JAVA_HOME

echo ""
echo "╔══════════════════════════════════════════╗"
echo "║       HMCTS Task Manager Launcher        ║"
echo "╚══════════════════════════════════════════╝"
echo ""
echo "  Java:     $JAVA_HOME"
echo "  Postgres: postgresql://task_manager:task_manager@localhost:5432/task_manager"
echo "  DB API:   http://localhost:8081/swagger-ui.html"
echo "  Backend:  http://localhost:8080"
echo "  Frontend: http://localhost:3000"
echo "  Swagger:  http://localhost:8080/swagger-ui.html"
echo ""
echo "  Press Ctrl+C to stop both servers."
echo ""

# ── Free ports if already in use ───────────────────────────────────────────────
for PORT in 8080 8081 3000; do
  PIDS=$(lsof -ti:$PORT 2>/dev/null) && kill -9 $PIDS 2>/dev/null && echo "[setup] Cleared port $PORT" || true
done
sleep 1

echo "[postgres] Starting Docker container..."
docker compose up -d postgres >/dev/null

echo "[postgres] Waiting for port 5432..."
for i in $(seq 1 30); do
  if nc -z localhost 5432 2>/dev/null; then
    echo "[postgres] Ready ✓"
    break
  fi
  sleep 2
done

echo "[database-service] Starting Spring Boot..."
cd "$PROJECT_DIR"
mvn spring-boot:run -pl database-service -am -q &
DATABASE_SERVICE_PID=$!

echo "[database-service] Waiting for port 8081..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null http://localhost:8081/internal/tasks 2>/dev/null; then
    echo "[database-service] Ready ✓"
    break
  fi
  sleep 2
done

# ── Start backend ───────────────────────────────────────────────────────────────
echo "[backend] Starting Spring Boot..."
cd "$PROJECT_DIR"
mvn spring-boot:run -pl backend -am -q &
BACKEND_PID=$!

# ── Wait for backend to be ready ────────────────────────────────────────────────
echo "[backend] Waiting for port 8080..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null http://localhost:8080/api/tasks 2>/dev/null; then
    echo "[backend] Ready ✓"
    break
  fi
  sleep 2
done

# ── Start frontend ──────────────────────────────────────────────────────────────
echo "[frontend] Starting Vite dev server..."
cd "$PROJECT_DIR/frontend"
node_modules/.bin/vite &
FRONTEND_PID=$!

echo ""
echo "  Both servers running. Open http://localhost:3000"
echo ""

# ── Trap Ctrl+C and kill both ───────────────────────────────────────────────────
cleanup() {
  echo ""
  echo "Stopping servers..."
  kill "$DATABASE_SERVICE_PID" 2>/dev/null
  kill "$BACKEND_PID"  2>/dev/null
  kill "$FRONTEND_PID" 2>/dev/null
  wait "$DATABASE_SERVICE_PID" 2>/dev/null
  wait "$BACKEND_PID"  2>/dev/null
  wait "$FRONTEND_PID" 2>/dev/null
  docker compose down >/dev/null 2>&1 || true
  echo "Done."
}
trap cleanup INT TERM

wait
