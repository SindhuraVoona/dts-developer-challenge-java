{{/*
Common labels applied to every resource.
*/}}
{{- define "task-manager.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels for a given component (pass component name as second arg via dict).
Usage: {{ include "task-manager.selectorLabels" (dict "root" . "component" "backend") }}
*/}}
{{- define "task-manager.selectorLabels" -}}
app.kubernetes.io/name: {{ .root.Chart.Name }}
app.kubernetes.io/component: {{ .component }}
{{- end }}
