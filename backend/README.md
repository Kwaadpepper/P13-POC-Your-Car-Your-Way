# Backend

Monorepo **Java / Spring Boot** (build via **Gradle Kotlin DSL**) organisé en **microservices** :  
- **Platform Service Registry (Eureka)**  
- **Platform Config Server**  
- **Platform API Gateway**  
- **User Service** (PostgreSQL + Redis + RabbitMQ)  
- **Support Service** (PostgreSQL + Redis + RabbitMQ)  

L’écosystème se lance via **Docker Compose** (bases de données, cache, broker, observabilité).  

---

## Sommaire

- [Backend](#backend)
  - [Sommaire](#sommaire)
  - [1. Présentation \& Objectifs](#1-présentation--objectifs)
  - [2. Principes d’architecture](#2-principes-darchitecture)
  - [3. Architecture logicielle \& technique](#3-architecture-logicielle--technique)
  - [4. Prérequis](#4-prérequis)
  - [5. Structure du projet](#5-structure-du-projet)
  - [6. Gestion de la configuration](#6-gestion-de-la-configuration)
  - [7. Observabilité (Logs \& Monitoring)](#7-observabilité-logs--monitoring)
  - [8. Démarrage local](#8-démarrage-local)
    - [Avec Docker](#avec-docker)
  - [8bis. Démarrage sans Docker](#8bis-démarrage-sans-docker)
  - [9. Accès aux services](#9-accès-aux-services)
  - [10. Variables / Comptes par défaut](#10-variables--comptes-par-défaut)
  - [11. Tests](#11-tests)
  - [11bis. Analyse statique (Error Prone + NullAway)](#11bis-analyse-statique-error-prone--nullaway)
  - [11ter. Vérification de style (Checkstyle)](#11ter-vérification-de-style-checkstyle)
  - [12. Contribution](#12-contribution)
  - [13. Ressources](#13-ressources)

---

## 1. Présentation & Objectifs

Le projet **Your Car, Your Way** a pour objectif de moderniser la gestion de la **location automobile** et du **support client**, en unifiant des applications existantes hétérogènes.  
Le backend fournit une **architecture évolutive et résiliente** pour :  
- accompagner la croissance sur les marchés **US & EU**,  
- garantir une **haute disponibilité**,  
- simplifier la **maintenance et l’évolutivité** des services.  

---

## 2. Principes d’architecture

- **Disponibilité & résilience** : réplication des services, reprise après panne (RPO/RTO réduits).  
- **Évolutivité** : scalabilité horizontale fine (chaque microservice peut monter en charge).  
- **Observabilité** : logs centralisés (Loki), métriques (Grafana), healthchecks (Actuator).  
- **Sécurité** : isolation des bases, JWT en cookie HttpOnly, révocation via Redis.  
- **Maintenabilité** : monorepo avec microservices indépendants (DDD : Domain / Application / Infrastructure).  
- **Interopérabilité** : communication inter-services par **RabbitMQ (event-driven)** et **Feign + Eureka (REST direct)**.  

---

## 3. Architecture logicielle & technique

- **Microservices** :  
  - `user-service` : gestion des utilisateurs et sessions, JWT + Redis.  
  - `support-service` : gestion du support client (tickets, chat temps réel via RabbitMQ).  
  - futurs services : catalogue, réservation, facturation (extension facile).  

- **Communication inter-services** :  
  - **RabbitMQ (topic exchange)** : événements diffusés aux services abonnés (multi-queues par instance).  
  - **Feign + Eureka** : appels directs avec découverte dynamique.  

- **API Gateway** : unique point d’entrée, gère CORS, routage et sécurité.  
- **Config Server** : centralise la configuration YAML.  
- **Eureka** : assure la découverte et le load-balancing dynamique.  
- **Bases de données** : isolation stricte par domaine (un PostgreSQL par service).  
- **Observabilité** : logs collectés par **Loki**, dashboards via **Grafana**.  

---

## 4. Prérequis

- **Docker** & **Docker Compose** (ou **Podman**)  
- **JDK 21+** (pour builder en local si besoin)  
- **Gradle Wrapper** (`./gradlew`)  

---

## 5. Structure du projet

- `platform/`  
  - `platform-service-registry` (Eureka)  
  - `platform-config-server`  
  - `platform-gateway`  
- `services/`  
  - `user-service/` (domain, application, infrastructure)  
  - `support-service/` (domain, application, infrastructure)  
- `config/` : fichiers de configuration centralisée (YAML)  
- `docker/` : Dockerfile et provisioning Grafana/Loki  
- `docker-compose.yml` : orchestration complète  

---

## 6. Gestion de la configuration

- **Config Server** lit les fichiers depuis le volume monté `./config`.  
- Les services consomment la conf via `SPRING_CONFIG_IMPORT=optional:configserver:http://config-server:8888`.  
- Les secrets de dev (mots de passe DB, Redis, RabbitMQ) sont injectés par `docker-compose.yml`.  

---

## 7. Observabilité (Logs & Monitoring)

- **Loki** agrège les logs applicatifs.  
- **Grafana** propose des dashboards préconfigurés.  
- **Healthchecks** exposés sur `/actuator/health`.  

---

## 8. Démarrage local

### Avec Docker

```bash
docker-compose up --build
# ou en arrière-plan
docker-compose up -d --build
```

Arrêt :

```bash
docker-compose down
```

**Attention: il est possible qu'un des services s'arrete au démarrage car il démarre trop tôt, vérifiez que tous sont bien lancés et relancez ceux qui sont arrêtés si nécessaire.**

---

## 8bis. Démarrage sans Docker

Exemples pour lancer un service indépendamment (les dépendances externes doivent tourner) :  

```bash
# Lancer le User Service
./gradlew :services:user-service:user-service-application:bootRun

# Lancer le Support Service
./gradlew :services:support-service:support-service-application:bootRun

# Lancer la Gateway
./gradlew :platform:platform-gateway:bootRun
```

---

## 9. Accès aux services

| Service                        | Port  | URL / Accès                                                      |
| ------------------------------ | ----- | ---------------------------------------------------------------- |
| **API Gateway**                | 8080  | [http://localhost:8080](http://localhost:8080)                   |
| **Eureka (Service Registry)**  | 8761  | [http://localhost:8761](http://localhost:8761)                   |
| **Config Server**              | 8888  | [http://localhost:8888](http://localhost:8888)                   |
| **User Service**               | 8081  | [http://localhost:8081](http://localhost:8081)                   |
| **Support Service**            | 8082  | [http://localhost:8082](http://localhost:8082)                   |
| **Support Service (2ᵉ inst.)** | 8083  | [http://localhost:8083](http://localhost:8083)                   |
| **RabbitMQ (AMQP)**            | 5672  | `amqp://localhost:5672`                                          |
| **RabbitMQ UI**                | 15672 | [http://localhost:15672](http://localhost:15672) *(guest/guest)* |
| **Redis**                      | 6379  | `redis://:redispassword@localhost:6379`                          |
| **PostgreSQL (user)**          | 5432  | DB `userdb` (postgres / password)                                |
| **PostgreSQL (support)**       | 5433  | DB `supportdb` (postgres / password)                             |
| **Loki**                       | 3100  | [http://localhost:3100](http://localhost:3100)                   |
| **Grafana**                    | 3000  | [http://localhost:3000](http://localhost:3000) *(admin/admin)*   |

---

## 10. Variables / Comptes par défaut

- **RabbitMQ** : user `guest`, pass `guest`  
- **Redis** : mot de passe `redispassword`  
- **PostgreSQL** :  
  - user-db : `postgres / password` (DB `userdb`, port `5432`)  
  - support-db : `postgres / password` (DB `supportdb`, port `5433`)  
- **Grafana** : `admin / admin`  

---

## 11. Tests

```bash
./gradlew build
./gradlew test
```

---

## 11bis. Analyse statique (Error Prone + NullAway)

Error Prone (et NullAway) est activé lors de la compilation.  

```bash
# Compilation avec Error Prone
./gradlew compileJava

# Vérifier tous les modules
./gradlew build

# Exécution explicite avec NullAway
./gradlew :support-service-application:build -Pnullaway=true --no-daemon
```

---

## 11ter. Vérification de style (Checkstyle)

```bash
./gradlew checkstyleMain --no-daemon --stacktrace
```

---

## 12. Contribution

- Branche : `feature/<votre-nom>/<description>`  
- Commits : **Conventional Commits**  
- Lint/format : respecter Checkstyle et Spotless si configurés  

---

## 13. Ressources

- [Spring Boot](https://spring.io/projects/spring-boot)  
- [Spring Cloud Netflix (Eureka)](https://spring.io/projects/spring-cloud-netflix)  
- [Spring Cloud Config](https://spring.io/projects/spring-cloud-config)  
- [Docker Compose](https://docs.docker.com/compose/)  
- [Grafana Loki](https://grafana.com/oss/loki/)  
- [Error Prone](https://errorprone.info/)  
- [NullAway](https://github.com/uber/NullAway)  
- [Checkstyle](https://checkstyle.sourceforge.io/)  

