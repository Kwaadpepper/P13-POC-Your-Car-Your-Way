#!/usr/bin/env bash
# Attendre qu'un service soit prêt avant de lancer l'application
# Usage :
#   ./wait-for-service.sh host port [--timeout=60] [--wait=2] -- command_to_run args...

HOST="$1"
PORT="$2"
shift 2

DELAYED_START=0  # délai avant de commencer les vérifications (en secondes)
TIMEOUT=60   # délai max en secondes
WAIT=2       # délai entre chaque tentative

# Parsing des options
while [[ $# -gt 0 ]]; do
  case "$1" in
    --timeout=*)
      TIMEOUT="${1#*=}"
      shift
      ;;
    --wait=*)
      WAIT="${1#*=}"
      shift
      ;;
    --delayed-start=*)
      DELAYED_START="${1#*=}"
      shift
      ;;
    --)
      shift
      CMD=("$@")
      break
      ;;
    *)
      echo "Option inconnue : $1"
      exit 1
      ;;
  esac
done

echo "⏳ Attente de $HOST:$PORT (timeout=${TIMEOUT}s, intervalle=${WAIT}s, démarrage différé=${DELAYED_START}s)..."

sleep "$DELAYED_START"

SECONDS=0
while true; do
  # Vérifier si le port est ouvert et le host résolu
  if (echo > /dev/tcp/"$HOST"/"$PORT") >/dev/null 2>&1; then
    echo "✅ $HOST:$PORT est accessible !"
    break
  fi

  if (( SECONDS >= TIMEOUT )); then
    echo "❌ Timeout atteint après ${TIMEOUT}s, impossible de joindre $HOST:$PORT"
    exit 1
  fi

  echo "… $HOST:$PORT pas encore prêt, nouvelle tentative dans ${WAIT}s"
  sleep "$WAIT"
done

# Exécution de la commande passée après --
exec "${CMD[@]}"
