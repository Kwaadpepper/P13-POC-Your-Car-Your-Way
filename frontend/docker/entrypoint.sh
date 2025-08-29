#!/bin/sh
# Génère le manifest federation dynamique
envsubst < /usr/share/nginx/html/federation.manifest.template.json > /usr/share/nginx/html/federation.manifest.json

# Génère l'environnement dynamique JS
envsubst < /usr/share/nginx/html/assets/environment.template.js > /usr/share/nginx/html/assets/environment.js

exec nginx -g 'daemon off;'
