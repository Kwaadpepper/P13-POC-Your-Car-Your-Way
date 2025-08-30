# OpenClassrooms P13 – Architecture Micro-Frontends & Micro-Services

Ce dépôt présente une **preuve de concept (POC)** d’architecture moderne utilisant micro-frontends et micro-services, adaptée à des enjeux métier tels que l’authentification et le support chat.

[![Pipeline](https://github.com/Kwaadpepper/P13-POC-Your-Car-Your-Way/actions/workflows/pipeline.yml/badge.svg)](https://github.com/Kwaadpepper/P13-POC-Your-Car-Your-Way/actions/workflows/pipeline.yml)

---

## Sommaire

- [OpenClassrooms P13 – Architecture Micro-Frontends \& Micro-Services](#openclassrooms-p13--architecture-micro-frontends--micro-services)
  - [Sommaire](#sommaire)
  - [Objectif du projet](#objectif-du-projet)
  - [Vue d’ensemble de l’architecture](#vue-densemble-de-larchitecture)
  - [Principaux bénéfices](#principaux-bénéfices)
  - [Technologies utilisées](#technologies-utilisées)
  - [Organisation du dépôt \& documentation](#organisation-du-dépôt--documentation)
  - [Pour aller plus loin](#pour-aller-plus-loin)

---

## Objectif du projet

L’objectif est de démontrer la **scalabilité, la robustesse et l’évolutivité** d’une architecture découplée :
- **Micro-frontends** : chaque domaine fonctionnel (Support, Réservation, Catalogue, Utilisateur) est une application Angular indépendante, fédérée dynamiquement pour garantir isolation, modularité et rapidité de livraison.
- **Micro-services** : chaque backend métier est développé en Spring Boot, déployé indépendamment et orchestré via un Gateway (Spring Cloud Gateway), offrant haute disponibilité et maintenance facilitée.
- **Interopérabilité** : L’architecture s’intègre avec des systèmes externes (ex : visioconférence Vonage) et expose ses APIs via des standards (REST, WebSocket, WebRTC).

---

## Vue d’ensemble de l’architecture

![Diagramme de déploiement.drawio.svg](./diagramme-de-deploiement.drawio.svg)

*Figure : Architecture cible, flux technologiques et découplage des responsabilités.*

> **Remarque :**
> Le schéma ci-dessous représente à titre indicatif les principaux *domaines métiers* adressés par la solution : chaque bloc (Support, Réservation, Catalogue, Utilisateur) illustre une séparation fonctionnelle qui, dans une organisation réelle, peut évoluer selon les besoins du métier.

**Principaux composants du schéma :**
- **Frontends Angular** (Cluster K8S-A) :
  - 4 micro-frontends (Support, Réservation, Catalogue, Utilisateur) fédérés dans un shell
  - Communication via HTTPS/WebSocket
  - Expérience fluide pour le client et l’opérateur

- **Gateway & Services** (Cluster K8S-B/C) :
  - **Spring Cloud Gateway** : gestion des routes, sécurité, centralisation des accès
  - **Services Spring Boot** : chaque domaine métier a son service et sa BDD PostgreSQL dédiée
  - Monitoring natif (Prometheus/Grafana), configuration centralisée (Spring Cloud Config, Eureka)

- **Infrastructure** :
  - Multi-cluster Kubernetes pour la disponibilité, la sécurité et la scalabilité
  - RabbitMQ pour la communication asynchrone
  - Bases de données PostgreSQL sectorisées

- **Interopérabilité** :
  - API REST/WebSocket pour l’intégration de services externes (ex : Vonage)
  - Ouverture vers des applications tierces

---

## Principaux bénéfices

- **Scalabilité** : chaque domaine métier évolue et se déploie indépendamment
- **Sécurité** : isolation stricte des données, approche Zero Trust
- **Maintenance facilitée** : chaque équipe travaille sur son périmètre sans risque de régression globale
- **Observabilité** : supervision avancée via Prometheus/Grafana
- **Interopérabilité** : APIs ouvertes, communication standardisée

---

## Technologies utilisées

- **Frontend** : Angular 20 (Native Federation, TailwindCSS, PrimeNG)
- **Backend** : Spring Boot, Spring Cloud Gateway, Eureka, Config Server
- **Infrastructure** : Kubernetes, RabbitMQ, Prometheus, Grafana
- **Data** : PostgreSQL
- **Interopérabilité** : REST, WebSocket, WebRTC (Vonage)

---

## Organisation du dépôt & documentation

Le dépôt est structuré par domaine :
- **frontend/** : micro-frontends Angular (un README dédié explique l’installation, le développement, les scripts et le déploiement)
- **backend/** : micro-services Spring Boot (README avec instructions par service)
- **guides/** : documentation pour l’ajout d’un micro-frontend ou micro-service

Chaque dossier contient les instructions nécessaires pour l’installation, le développement, les tests et le déploiement.

---

## Pour aller plus loin

- Consultez les READMEs spécifiques à chaque sous-projet pour démarrer rapidement.
  - [./backend/README.md](./backend/README.md)
  - [./frontend/README.md](./frontend/README.md)
- Utilisez les guides pour contribuer ou étendre l’architecture.
- Explorez le schéma d’architecture pour comprendre les flux et les interactions.

---

> Ce projet constitue un socle d’expérimentation et d’apprentissage, validant la pertinence d’une architecture micro-frontends / micro-services sur des cas d’usage réels et critiques.
