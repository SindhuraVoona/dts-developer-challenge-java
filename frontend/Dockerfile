# Build stage
# Built with frontend/ as Docker build context:
#   docker build -t frontend:latest ./frontend
FROM node:22-alpine AS build
WORKDIR /app

COPY package.json package-lock.json* ./
RUN npm ci --ignore-scripts

COPY . .
RUN npm run build

# Runtime stage — nginx serves the static Vite output
# API calls (/api/*) are routed by the Kubernetes Ingress to backend-service
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
