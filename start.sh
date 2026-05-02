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
echo "  Backend:  http://localhost:8080"
echo "  Frontend: http://localhost:3000"
echo "  Swagger:  http://localhost:8080/swagger-ui.html"
echo ""
echo "  Press Ctrl+C to stop both servers."
echo ""

# ── Start backend ───────────────────────────────────────────────────────────────
echo "[backend] Starting Spring Boot..."
cd "$PROJECT_DIR/backend"
mvn spring-boot:run -q &
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
  kill "$BACKEND_PID"  2>/dev/null
  kill "$FRONTEND_PID" 2>/dev/null
  wait "$BACKEND_PID"  2>/dev/null
  wait "$FRONTEND_PID" 2>/dev/null
  echo "Done."
}
trap cleanup INT TERM

wait
