# YCYW Backend Monorepo (Spring Boot, Java 21)

Monorepo Maven pour héberger des micro‑services Spring Boot alignés sur le diagramme cible:
- Plateforme: Spring Cloud Config Server, Eureka Service Registry, Spring Cloud Gateway
- Services métiers: ex. user-service (et futurs: catalogue, réservation, support…)
- Messagerie: RabbitMQ
- Base de données: un Postgres par service
- Observabilité: Actuator + Micrometer Prometheus

## Stack technique
- Java 21
- Spring Boot 3.3+ / 3.4+ (compatibles Java 21)
- Spring Cloud 2024.0.x
- Maven (avec Maven Wrapper)
- Docker (Postgres, RabbitMQ)

## Arborescence
```
backend/
├─ pom.xml                       # POM parent (packaging=pom)
├─ platform/
│  ├─ config-server/             # Spring Cloud Config Server (port 8888)
│  ├─ service-registry/          # Eureka Server (port 8761)
│  └─ gateway/                   # Spring Cloud Gateway (port 8080)
├─ services/
│  └─ user-service/              # Exemple de micro-service (port 8081)
├─ config/                       # Config centralisée (servie par Config Server)
│  ├─ application.yml
│  └─ user-service.yml
├─ docker/
│  └─ docker-compose.yml         # RabbitMQ + Postgres pour user-service
└─ .vscode/
   └─ launch.json                # Configs Run/Debug VS Code
```

## Prérequis
- JDK 21 installé (JAVA_HOME pointe vers le JDK 21)
- Docker Desktop (ou équivalent)
- VS Code:
  - Extension Pack for Java
  - Spring Boot Extension Pack
- Port disponibles: 8888, 8761, 8080, 8081, 5432, 5672, 15672

## Démarrage rapide

Ouvrez un terminal dans `backend/`.

1) Build global
```bash
./mvnw clean verify
```
Attendu: BUILD SUCCESS.

2) Lancer l’infra locale (RabbitMQ + Postgres)
```bash
docker compose -f docker/docker-compose.yml up -d
docker compose -f docker/docker-compose.yml ps
# RabbitMQ UI: http://localhost:15672 (guest/guest)
```

3) Démarrer le Config Server (port 8888)
```bash
./mvnw -pl platform/config-server spring-boot:run
# Santé:
curl http://localhost:8888/actuator/health   # {"status":"UP"}
```

4) Démarrer le Service Registry Eureka (port 8761)
```bash
./mvnw -pl platform/service-registry spring-boot:run
# Santé:
curl http://localhost:8761/actuator/health   # {"status":"UP"}
# UI:
# http://localhost:8761
```

5) Démarrer la Gateway (port 8080)
```bash
./mvnw -pl platform/gateway spring-boot:run
# Santé:
curl http://localhost:8080/actuator/health   # {"status":"UP"}
```

6) Démarrer le user-service (port 8081)
```bash
# Si server.port est déjà 8081 dans application.yml:
./mvnw -pl services/user-service spring-boot:run

# Ou en forçant le port à la volée:
./mvnw -pl services/user-service spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

7) Vérifications fonctionnelles
```bash
# Direct
curl http://localhost:8081/users/hello
# -> Hello from user-service

# Via la Gateway (service discovery locator activé)
curl http://localhost:8080/user-service/users/hello
# -> Hello from user-service

# Dans Eureka (UI)
# http://localhost:8761  (user-service doit apparaître)
```

Arrêt
```bash
# Ctrl+C dans chaque terminal Spring Boot
docker compose -f docker/docker-compose.yml down
```

## Configuration

- Config centralisée via Spring Cloud Config (mode "native"):
  - Le Config Server lit `backend/config/` (voir `platform/config-server/src/main/resources/application.yml`, `search-locations`).
  - Exemple de clés communes: `management.endpoints.web.exposure.include`, `logging.level.root`, etc.
- Chaque service importe la config:
  ```yaml
  spring:
    config:
      import: optional:configserver:http://localhost:8888
  ```
- Variables d’environnement utiles:
  - Base de données:
    - `DB_URL` (ex: jdbc:postgresql://localhost:5432/userdb)
    - `DB_USER`, `DB_PASSWORD`
  - RabbitMQ:
    - `RABBIT_HOST`, `RABBIT_PORT`, `RABBIT_USER`, `RABBIT_PASSWORD`
  - Port service (optionnel):
    - `SERVER_PORT` (ex: 8081)

## Ajouter un nouveau micro‑service

Vous avez deux approches: dupliquer `user-service` (rapide) ou générer via Spring Initializr (VS Code).

### Option A — Dupliquer `user-service` (recommandé pour démarrer)
1) Copier le dossier
```bash
cp -R services/user-service services/catalog-service
```

2) Mettre à jour dans `services/catalog-service`:
- `pom.xml`:
  - `<artifactId>catalog-service</artifactId>`
  - Parent inchangé
- Packages Java et classe main:
  - Renommez le package `com.ycyw.users` en `com.ycyw.catalog` et la classe en `CatalogServiceApplication`
- `src/main/resources/application.yml`:
  - `spring.application.name: catalog-service`
  - `server.port: 8082` (ou laissez vide et lancez avec `--server.port=...`)
  - URL de sa base: `jdbc:postgresql://localhost:5433/catalogdb` (par exemple)
- Ajoutez un contrôleur minimal (ex: GET `/catalog/hello`)

3) Ajouter le module dans le POM parent
Dans `backend/pom.xml`, ajoutez:
```xml
<modules>
  ...
  <module>services/catalog-service</module>
</modules>
```

4) Config centralisée (optionnel mais conseillé)
Créez `backend/config/catalog-service.yml` avec les surcharges nécessaires:
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5433/catalogdb}
```

5) Base de données (docker-compose)
Ajoutez un service Postgres dédié dans `backend/docker/docker-compose.yml`:
```yaml
catalog-postgres:
  image: postgres:16
  environment:
    POSTGRES_DB: catalogdb
    POSTGRES_USER: catalog
    POSTGRES_PASSWORD: password
  ports:
    - "5433:5432"
  healthcheck:
    test: ["CMD-SHELL", "pg_isready -U catalog -d catalogdb"]
    interval: 5s
    timeout: 5s
    retries: 10
```
Puis relancez:
```bash
docker compose -f docker/docker-compose.yml up -d
```

6) Build et démarrage du nouveau service
```bash
./mvnw -q clean verify
./mvnw -pl services/catalog-service spring-boot:run -Dspring-boot.run.arguments=--server.port=8082
```
Vérifiez:
```bash
curl http://localhost:8082/catalog/hello
curl http://localhost:8080/catalog-service/catalog/hello
# Eureka UI: http://localhost:8761 -> catalog-service présent
```

Dépendances minimales conseillées pour un service type:
- spring-boot-starter-web
- spring-boot-starter-validation
- spring-boot-starter-data-jpa
- postgresql (scope runtime)
- spring-boot-starter-amqp
- spring-cloud-starter-netflix-eureka-client
- spring-cloud-starter-config
- spring-boot-starter-actuator
- micrometer-registry-prometheus
- spring-boot-starter-test (test)

### Option B — Spring Initializr (dans VS Code)
- VS Code > “Spring Initializr: Generate a Maven Project”
- Group: `com.ycyw`, Artifact: `catalog-service`
- Java 21, Spring Boot 3.3+/3.4+
- Dépendances: celles listées ci‑dessus
- Générez directement dans `backend/services/`
- Ajustez:
  - `spring.application.name`
  - `application.yml` (ports, DB, RabbitMQ)
  - Ajoutez le module dans le `<modules>` du parent
  - Ajoutez `config/catalog-service.yml` si nécessaire

## Observabilité

- Chaque service expose `/actuator/prometheus` (Micrometer Prometheus).
- Vous pouvez ajouter ultérieurement un `docker-compose` pour Prometheus + Grafana et scrapper les services.
- Endpoints santé:
  - Config Server: `http://localhost:8888/actuator/health`
  - Eureka: `http://localhost:8761/actuator/health`
  - Gateway: `http://localhost:8080/actuator/health`
  - Services: `http://localhost:<port>/actuator/health`

## Exécution avec VS Code

- `.vscode/launch.json` inclut:
  - “Start Platform (Config + Eureka + Gateway)” (compound)
  - Lances ensuite chaque micro-service avec sa configuration dédiée (“User Service”, etc.)
- Palette de commande:
  - “Maven: Reload All Projects” si VS Code ne détecte pas les modules
  - “Java: Clean Java Language Server Workspace” en cas de souci d’indexation

## Dépannage

- Port déjà utilisé (“Adresse déjà utilisée”)
  - Changer le port: `--server.port=808x` ou `server.port` dans `application.yml`
  - Voir les processus:
    ```bash
    lsof -iTCP:8080 -sTCP:LISTEN -nP
    lsof -iTCP:8081 -sTCP:LISTEN -nP
    ```
- Le service n’apparaît pas dans Eureka
  - Vérifier `eureka.client.service-url.defaultZone`
  - Vérifier que le service a bien `spring-cloud-starter-netflix-eureka-client`
- Erreur Config Server
  - Assurez-vous que Config Server est démarré (8888)
  - Vérifier `spring.config.import=optional:configserver:http://localhost:8888`
  - Vérifier `search-locations` pointe bien vers `backend/config`
- Problèmes DB
  - Vérifier variables `DB_URL`, `DB_USER`, `DB_PASSWORD`
  - Vérifier que l’instance Postgres du service est UP (`docker compose ps`)

## Commandes utiles

- Build global:
  ```bash
  ./mvnw -q clean verify
  ```
- Démarrer un module:
  ```bash
  ./mvnw -pl platform/config-server spring-boot:run
  ./mvnw -pl platform/service-registry spring-boot:run
  ./mvnw -pl platform/gateway spring-boot:run
  ./mvnw -pl services/user-service spring-boot:run
  ```
- Forcer un port à la volée:
  ```bash
  ./mvnw -pl services/user-service spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
  ```
- Infra locale:
  ```bash
  docker compose -f docker/docker-compose.yml up -d
  docker compose -f docker/docker-compose.yml down
  ```
