#!/usr/bin/env bash
set -euo pipefail

# check podman or docker
if command -v podman >/dev/null 2>&1; then
  DOCKER_CMD="podman"
elif command -v docker >/dev/null 2>&1; then
  DOCKER_CMD="docker"
else
  echo "Error: neither podman nor docker available in PATH"
  exit 1
fi

echo "Using command: ${DOCKER_CMD}"

#!/bin/sh
set -u

echo "== Containers =="
${DOCKER_CMD} ps --format 'table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}' || ${DOCKER_CMD} ps -a

echo
echo "== Volumes =="
${DOCKER_CMD} volume ls

echo
echo "== Loki readiness =="
if curl -sS http://localhost:3100/ready >/dev/null 2>&1; then
  echo "Loki: ready"
else
  echo "Loki: NOT ready (check logs: ${DOCKER_CMD} logs docker_loki_1)"
fi

echo
echo "== Loki metrics (head) =="
curl -sS http://localhost:3100/metrics 2>/dev/null | head -n 20 || echo "No metrics (Loki may be down)"

echo
echo "== Push test log to Loki =="
TS=$(date +%s%N)
PAYLOAD=$(cat <<EOF
{ "streams": [ { "stream": {"service":"manual-test"}, "values": [ [ "${TS}", "hello loki from curl" ] ] } ] }
EOF
)
echo "Payload timestamp: $TS"
curl -s -X POST http://localhost:3100/loki/api/v1/push \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD" >/dev/null && echo "Push OK" || echo "Push FAILED (check Loki logs)"

echo
echo "== Query the test log via Loki API =="
START=$((TS - 600000000000)) # 10 minutes before (ns)
END=$TS
echo "Querying for {service=\"manual-test\"} between $START and $END"
curl -sG 'http://localhost:3100/loki/api/v1/query_range' \
  --data-urlencode 'query={service="manual-test"}' \
  --data "start=${START}" \
  --data "end=${END}" \
  | (command -v jq >/dev/null 2>&1 && jq '.' || cat) || echo "Query failed"

echo
echo "== Grafana health =="
curl -sS -u admin:admin http://localhost:3000/api/health >/dev/null 2>&1 && echo "Grafana: healthy (admin/admin)" || echo "Grafana: no response (check ${DOCKER_CMD} logs docker_grafana_1)"

echo
echo "== Inspect loki-data volume content =="
${DOCKER_CMD} run --rm -v loki-data:/loki:ro alpine ls -la /loki || echo "Cannot list loki-data (volume missing or permissions)"

echo
echo "== Done =="
