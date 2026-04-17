# 🎲 GameBoard — Plateforme de notation de jeux de société

Projet Fullstack S8 — EPITA APC

**GitHub** : https://github.com/hajar-khettou/Fullstack-project

---

## Équipe

- Amina SERRANO
- Christina LOPES
- Hajar KHETTOU
- Lyna MEDJEDOUB
- Rawane OUFFA

**Encadrant** : David THIBAU — david.thibau@gmail.com

---

## Stack technique

| Couche | Technologies |
|--------|-------------|
| Backend | Java 21 · Spring Boot 3.5 · Spring Data JPA · Spring Security |
| Messaging | Apache Kafka · Topic `game-imported` |
| Base de données | Neon PostgreSQL (cloud partagé) · H2 (tests) |
| API externe | BoardGameGeek XML API v2 |
| Frontend | Angular 21 · TypeScript |
| DevOps | Docker · docker-compose · GitHub Actions |
| Documentation | Swagger / OpenAPI 3 |

---

## Base de données partagée (Neon)

La base de données est hébergée sur **[Neon](https://neon.tech)** (cloud PostgreSQL gratuit). Elle est **partagée entre tous les membres de l'équipe** — pas besoin d'installer PostgreSQL localement.

- Les connexions sont déjà configurées dans `application-local.properties` et `application-docker.properties`
- Au premier démarrage, 15 jeux de société sont insérés automatiquement
- Les jeux ajoutés via l'app (et approuvés par un admin) sont visibles par tout le monde en temps réel

---

## Prérequis

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (inclut Docker Compose) — pour Kafka uniquement
- [Node.js 20+](https://nodejs.org/) + Angular CLI (`npm install -g @angular/cli`)
- Java 21 (pour lancer le backend)

---

## Lancement from scratch

### Option 1 — Backend en local + Kafka Docker (recommandé)

```bash
git clone https://github.com/hajar-khettou/Fullstack-project.git
cd Fullstack-project

# Démarrer Kafka uniquement (la base est sur Neon, pas besoin de postgres local)
docker-compose up -d zookeeper kafka

# Lancer le backend
cd backend
./mvnw spring-boot:run

# Lancer le frontend (dans un autre terminal)
cd frontend
npm install
ng serve
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:4200 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui/index.html |

---

### Option 2 — Stack complète en Docker

```bash
docker-compose up --build -d
cd frontend && npm install && ng serve
```

---

## Comptes disponibles

| Identifiant | Mot de passe | Rôle | Accès |
|-------------|-------------|------|-------|
| `user` | `user` | Utilisateur | Consulter, noter |
| `editor` | `editor` | Éditeur | + Proposer des jeux |
| `admin` | `admin` | Webmaster | + Administrer tout |

> Les comptes sont créés automatiquement au démarrage si la base est vide.

---

## Fonctionnalités

### Utilisateur
- Catalogue paginé des jeux (sans connexion)
- Fiche détaillée avec notes et avis
- Recherche et filtres par titre, genre, année
- Noter un jeu (1–5 étoiles + commentaire)

### Éditeur
- Proposer un nouveau jeu (enrichissement automatique via BGG)
- Suivre le statut de ses propositions (En attente / Approuvé / Refusé)
- Modifier ou supprimer ses propositions en attente

### Webmaster (Admin)
- Valider ou refuser les jeux proposés
- Modifier ou supprimer les jeux du catalogue
- Gérer les comptes utilisateurs (ajouter / modifier / supprimer)

---

## Architecture

```
┌─────────────────────────────────────┐
│         FRONTEND (Angular 21)        │
│  Catalogue · Fiche · Proposer · Admin │
└──────────────┬──────────────────────┘
               │ HTTP REST + Basic Auth
┌──────────────▼──────────────────────┐
│        BACKEND (Spring Boot 3.5)     │
│  Controller → Service → Repository  │
│  BGG Client + Kafka Producer         │
└──────┬───────────────────┬──────────┘
       │ JPA               │ Topic: game-imported
┌──────▼──────┐    ┌───────▼──────────┐
│ PostgreSQL  │    │  Kafka Cluster   │
└─────────────┘    └──────────────────┘
```

### Kafka

Quand un jeu est créé ou importé depuis BGG, un événement est publié sur le topic `game-imported` :

```json
{
  "gameId": 1,
  "title": "Catan",
  "source": "editor",
  "importedAt": "2026-04-17T12:00:00Z"
}
```

Le consumer log l'événement en temps réel. Voir les logs :

```bash
docker logs -f gameboard-backend
```

---

## API — Documentation Swagger

Disponible sur **http://localhost:8080/swagger-ui/index.html**

Principaux endpoints :

| Méthode | Endpoint | Accès |
|---------|----------|-------|
| GET | `/api/games` | Public |
| GET | `/api/games/{id}` | Public |
| POST | `/api/games` | EDITOR, WEBMASTER |
| PUT | `/api/games/{id}` | EDITOR, WEBMASTER |
| DELETE | `/api/games/{id}` | EDITOR, WEBMASTER |
| PATCH | `/api/games/{id}/status` | WEBMASTER |
| GET | `/api/games/pending` | WEBMASTER |
| GET | `/api/games/my-proposals` | EDITOR, WEBMASTER |
| POST | `/api/games/{id}/ratings` | USER, EDITOR, WEBMASTER |
| GET | `/api/users` | WEBMASTER |
| POST | `/api/users` | WEBMASTER |
| PUT | `/api/users/{id}` | WEBMASTER |
| DELETE | `/api/users/{id}` | WEBMASTER |
| GET | `/api/auth/me` | Authentifié |
| GET | `/actuator/health` | Public |

---

## Tests

```bash
cd backend
./mvnw test
```

Les tests utilisent le profil `test` (H2 en mémoire, sans Docker, sans Kafka).

---

## Structure du projet

```
Fullstack-project/
├── backend/                  → API Spring Boot
│   ├── src/main/java/com/gameboard/
│   │   ├── game/             → CRUD jeux
│   │   ├── rating/           → Notes et avis
│   │   ├── user/             → Gestion utilisateurs
│   │   ├── kafka/            → Producer + Consumer
│   │   ├── bgg/              → Client BoardGameGeek
│   │   ├── security/         → Spring Security
│   │   └── config/           → Swagger, DataInitializer
│   └── src/test/             → Tests unitaires
├── frontend/                 → Application Angular
│   └── src/app/
│       ├── pages/            → game-list, game-detail, admin, login...
│       └── core/             → Services, interceptors
├── docker-compose.yml        → PostgreSQL + Kafka + Backend
└── .github/workflows/        → CI GitHub Actions
```

---

## CI/CD — GitHub Actions

La pipeline se déclenche à chaque push sur `main` :

1. Checkout du code
2. Installation Java 21
3. Compilation : `./mvnw clean install -DskipTests`
4. Tests unitaires : `./mvnw test` (profil `test`, H2)
5. Build de l'image Docker backend

Voir `.github/workflows/ci.yml`
