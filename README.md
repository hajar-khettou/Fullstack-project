# GameBoard — Plateforme de notation de jeux de société

Projet Fullstack S8 — APC

## Équipe

| Nom | GitHub |
|-----|--------|
| Amina SERRANO | à compléter |
| Christina LOPES | à compléter |
| Hajar KHETTOU | [@hajar-khettou](https://github.com/hajar-khettou) |
| Lyna MEDJEDOUB | à compléter |
| Rawane OUFFA |

## Lancer la stack

### Prérequis

- Docker + Docker Compose
- Java 21
- Maven (ou `./mvnw` dans `backend/`)

### 1. Toute la stack dans Docker (API + Postgres + Kafka)

À la racine du dépôt :

```bash
docker compose up -d --build
```

| Service    | Port | Rôle |
|------------|------|------|
| **Backend** | 8080 | Image construite avec `backend/Dockerfile`, profil Spring `docker` |
| PostgreSQL | 5432 | Base `gameboard`, user / mot de passe : `gameboard` |
| Kafka      | 9092 | Broker (à brancher dans l’app quand le code Kafka sera prêt) |

Pour **ne pas** reconstruire l’image à chaque fois : `docker compose up -d`. Après une modif du code : `docker compose up -d --build backend`.

### 1 bis. Infra seulement (tu lances le backend avec Maven)

```bash
docker compose up -d postgres zookeeper kafka
```

Puis **§2** : `./mvnw spring-boot:run` dans `backend/`. Le profil `local` ne démarre via Spring que **postgres, zookeeper et kafka** (pas le service `backend` du compose, pour éviter deux instances d’API).

### 2. Backend (Spring Boot)

Le profil **`local`** est actif par défaut : l’API se connecte à **Postgres sur `localhost:5432`** avec les mêmes identifiants que le conteneur.

```bash
cd backend
./mvnw spring-boot:run
```

Depuis le dossier `backend/`, Spring Boot peut **démarrer tout seul** le fichier `docker-compose.yml` à la racine (module `spring-boot-docker-compose`) si le **daemon Docker** est lancé — mais **uniquement** les services `postgres`, `zookeeper` et `kafka` (pas le conteneur `backend`, voir §1 bis). Sinon : `docker compose up -d postgres zookeeper kafka` à la racine. Pour désactiver ce comportement : `export SPRING_DOCKER_COMPOSE_ENABLED=false`.

**Dépannage**

- **`Connection to localhost:5432 refused`** : Postgres n’est pas démarré ; lance l’infra (§1 bis) ou Docker + Spring Compose comme ci-dessus.
- **`Port 8080 was already in use`** : tu as **deux** backends qui veulent le même port. Soit le conteneur `gameboard-backend` tourne (`docker compose up` complet) — dans ce cas **arrête-le** : `docker compose stop backend`, ou n’utilise **que** l’API Docker et pas Maven. Soit un autre programme occupe 8080 : `ss -tlnp | grep 8080` (Linux) pour voir le processus, ou lance Maven sur un autre port : `SERVER_PORT=8081 ./mvnw spring-boot:run`.

**Variables utiles** (optionnel ; valeurs par défaut = alignées sur `docker-compose.yml`) :

| Variable | Exemple | Rôle |
|----------|---------|------|
| `SPRING_PROFILES_ACTIVE` | `local` | `local` = Postgres sur l’hôte ; `test` = H2 (tests / CI) ; `docker` = hôte DB `postgres` (conteneur backend) |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/gameboard` | URL JDBC |
| `SPRING_DATASOURCE_USERNAME` | `gameboard` | Utilisateur SQL |
| `SPRING_DATASOURCE_PASSWORD` | `gameboard` | Mot de passe SQL |

Tu peux copier `.env.example` vers `.env`, puis **exporter** ces variables dans ton shell avant `spring-boot:run` (le fichier `.env` n’est pas chargé automatiquement par Spring).

**Profil `docker`** : quand le JAR tournera dans Compose sur le même réseau que le service `postgres`, lance avec `SPRING_PROFILES_ACTIVE=docker` (URL par défaut : `jdbc:postgresql://postgres:5432/gameboard`). Voir `backend/src/main/resources/application-docker.properties`.

**Surcharge locale non versionnée** : crée `backend/src/main/resources/application-local-overrides.properties` (ignoré par Git) pour tes réglages perso.

### 3. Frontend (Angular)

```bash
cd frontend
npm install
npm start
```

L’API est attendue sur `http://localhost:8080` par défaut (voir les services Angular).

## Structure du projet

```
├── backend/          → API Spring Boot
├── frontend/         → Interface Angular
├── docs/             → Document de conception
├── docker-compose.yml
├── .env.example      → Modèle de variables pour le dev local
└── .github/workflows/ci.yml
```

La CI exécute `mvn verify` avec **`SPRING_PROFILES_ACTIVE=test`** (H2, sans Docker), puis **`docker build`** sur `backend/` pour valider l’image.

## Document de conception

Disponible dans le dossier `docs/`.
