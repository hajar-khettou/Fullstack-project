# GameBoard — Plateforme de notation de jeux de société

Projet Fullstack S8 — APC

## Équipe

| Nom | GitHub |
|-----|--------|
| Amina SERRANO | à compléter |
| Christina LOPES |[@atina92](https://github.com/atina92) |
| Hajar KHETTOU | [@hajar-khettou](https://github.com/hajar-khettou) |
| Lyna MEDJEDOUB | à compléter |
| Rawane OUFFA | à compléter |

## Lancer la stack

### Prérequis
- Docker + Docker Compose
- Java 21
- Maven

### Démarrage
```bash
# Lancer PostgreSQL + Kafka
docker-compose up -d

# Lancer le backend
cd backend
./mvnw spring-boot:run
```

## Structure du projet
```
├── backend/          → API Spring Boot
├── frontend/         → Interface Angular (Sprint 2)
├── docs/             → Document de conception
├── docker-compose.yml
└── .github/workflows/ci.yml
```

## Document de conception

Disponible dans le dossier `docs/`.
