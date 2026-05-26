# UniEvent Backend — Fonctionnalités 3.6 / 3.7 / 3.8

> **Auteur :** Safae  
> **Branche :** `feature/houda-backend`  
> **Date :** 2026-05-26

---

## Résumé des changements

### Fichiers modifiés

| Fichier | Modification |
|---|---|
| `pom.xml` | Ajout des dépendances ZXing 3.5.3 (QR code) |
| `repository/EvenementRepository.java` | Ajout de 2 requêtes JPQL : `countByStatut()`, `countByCategorie()` |
| `repository/InscriptionRepository.java` | Ajout de `countByPresent()` et `topEvenementsByInscriptions()` |
| `repository/EvaluationRepository.java` | Ajout de `averageNote()` |

### Fichiers créés (20 nouveaux fichiers)

#### 3.6 — Génération badges / QR codes
| Fichier | Rôle |
|---|---|
| `dto/BadgeResponseDTO.java` | DTO de réponse badge : nom étudiant, événement, QR code base64 |
| `service/BadgeService.java` | Génère le QR code PNG via ZXing (300×300 px) |
| `controller/BadgeController.java` | Expose `GET /inscriptions/{id}/qrcode` et `GET /inscriptions/{id}/badge` |

#### 3.7 — Gestion partenaires / sponsors
| Fichier | Rôle |
|---|---|
| `enums/TypePartenaireEnum.java` | `SPONSOR_OR`, `SPONSOR_ARGENT`, `SPONSOR_BRONZE`, `PARTENAIRE_MEDIA`, `PARTENAIRE_ACADEMIQUE`, `PARTENAIRE_INSTITUTIONNEL`, `AUTRE` |
| `entity/Partenaire.java` | Entité JPA → table `partenaire` |
| `dto/PartenaireCreateDTO.java` | DTO création |
| `dto/PartenaireResponseDTO.java` | DTO réponse |
| `dto/PartenaireUpdateDTO.java` | DTO mise à jour (PATCH partiel) |
| `repository/PartenaireRepository.java` | `findByActif()`, `findByTypePartenariat()` |
| `mapper/PartenaireMapper.java` | MapStruct : entity ↔ DTO |
| `service/PartenaireService.java` | CRUD + activer/désactiver |
| `controller/PartenaireController.java` | 8 endpoints REST `/partenaires` |

#### 3.8 — Tableau de bord analytique & KPIs
| Fichier | Rôle |
|---|---|
| `dto/TopEvenementDTO.java` | `evenementId`, `titre`, `nbInscriptions` |
| `dto/DashboardKpiDTO.java` | DTO principal avec tous les KPIs |
| `service/AnalytiqueService.java` | Agrège les données de tous les repositories |
| `controller/AnalytiqueController.java` | Expose `GET /analytique/dashboard` |

---

## 3.6 — Génération badges / QR codes

### Architecture
```
InscriptionRepository
        ↓
BadgeService  →  ZXing QRCodeWriter  →  PNG bytes / Base64
        ↓
BadgeController
```

### Contenu encodé dans le QR code
```
INS-{id}|{NOM Prénom}|{Titre événement}|{date inscription}
```
Exemple : `INS-12|ALAMI Fatima|Hackathon 2026|15/05/2026 10:30`

### Endpoints
| Méthode | URL | Description |
|---|---|---|
| GET | `/inscriptions/{id}/qrcode` | Retourne l'image PNG directement (`image/png`) |
| GET | `/inscriptions/{id}/badge` | Retourne JSON avec QR code en Base64 + infos étudiant |

---

## 3.7 — Gestion partenaires / sponsors

### Modèle de données (`partenaire`)
| Champ | Type | Obligatoire |
|---|---|---|
| id | BIGINT (auto) | — |
| nom | VARCHAR | Oui |
| description | TEXT | Non |
| logo | VARCHAR | Non |
| site_web | VARCHAR | Non |
| contact_nom | VARCHAR | Non |
| contact_email | VARCHAR | Non |
| contact_telephone | VARCHAR | Non |
| type_partenariat | ENUM | Non |
| actif | BOOLEAN (défaut `true`) | — |
| date_creation | TIMESTAMP (auto) | — |

### Endpoints
| Méthode | URL | Description |
|---|---|---|
| POST | `/partenaires` | Créer un partenaire |
| GET | `/partenaires` | Lister tous |
| GET | `/partenaires/actifs` | Lister les actifs seulement |
| GET | `/partenaires?type=SPONSOR_OR` | Filtrer par type |
| GET | `/partenaires/{id}` | Lire un partenaire |
| PUT | `/partenaires/{id}` | Modifier (remplacement complet) |
| PATCH | `/partenaires/{id}/activer` | Activer |
| PATCH | `/partenaires/{id}/desactiver` | Désactiver |
| DELETE | `/partenaires/{id}` | Supprimer |

---

## 3.8 — Tableau de bord analytique & KPIs

### Endpoint
| Méthode | URL | Description |
|---|---|---|
| GET | `/analytique/dashboard` | Retourne tous les KPIs en un seul appel |

### Structure de la réponse
```json
{
  "totalEvenements": 12,
  "evenementsParStatut": { "APPROUVE": 5, "BROUILLON": 4, "ANNULE": 3 },
  "evenementsParCategorie": { "CONFERENCE": 6, "ATELIER": 4, "SPORTIF": 2 },

  "totalInscriptions": 150,
  "inscriptionsPresentes": 110,
  "inscriptionsAbsentes": 40,
  "tauxPresence": 73.3,

  "totalReservations": 20,
  "reservationsParStatut": { "APPROUVEE": 15, "EN_ATTENTE": 3, "REJETEE": 2, "ANNULEE": 0 },

  "totalClubs": 8,
  "totalUtilisateurs": 95,
  "totalPartenaires": 5,
  "partenairesActifs": 4,

  "totalEvaluations": 60,
  "noteMoyenneGlobale": 4.2,

  "topEvenements": [
    { "evenementId": 3, "titre": "Hackathon 2026", "nbInscriptions": 45 },
    { "evenementId": 1, "titre": "Conférence IA",  "nbInscriptions": 38 }
  ]
}
```

---

## Comment démarrer

```bash
# 1. Télécharger les nouvelles dépendances Maven
mvn clean install -DskipTests

# 2. Lancer le serveur (PostgreSQL doit être démarré)
mvn spring-boot:run
```

Le serveur écoute sur **http://localhost:8080**

---

## Guide de tests Postman

> **Prérequis :** PostgreSQL démarré, serveur lancé avec `mvn spring-boot:run`  
> **Base URL :** `http://localhost:8080`  
> **Content-Type pour tous les POST/PUT :** `application/json`

---

### ÉTAPE 0 — Préparer des données (si base vide)

Avant de tester 3.6 (QR code), il faut avoir une inscription en base.  
Créer dans l'ordre : un utilisateur → un événement → une inscription.

#### 0.1 Créer un utilisateur
```
POST http://localhost:8080/utilisateurs
Body :
{
  "nom": "ALAMI",
  "prenom": "Fatima",
  "email": "fatima@unievt.ma",
  "motDePasse": "pass123",
  "telephone": "0612345678"
}
```
> Noter l'`id` retourné (ex: `1`) → c'est l'`idEtudiant`

#### 0.2 Créer un événement
```
POST http://localhost:8080/evenements
Body :
{
  "titre": "Hackathon 2026",
  "description": "Compétition de programmation",
  "categorie": "COMPETITION",
  "dateDebut": "2026-06-10T09:00:00",
  "dateFin": "2026-06-10T18:00:00",
  "capacite": 50,
  "statut": "APPROUVE",
  "visibilite": "UNIVERSITE",
  "type": "CLUB",
  "organisateurId": 1
}
```
> Noter l'`id` retourné (ex: `1`) → c'est l'`idEvenement`

#### 0.3 Créer une inscription
```
POST http://localhost:8080/inscriptions
Body :
{
  "etudiantId": 1,
  "evenementId": 1,
  "statut": "CONFIRMEE"
}
```
> Noter l'`id` retourné (ex: `1`) → utilisé dans les tests 3.6

---

### PARTIE 3.7 — Gestion partenaires / sponsors

#### Test P1 — Créer un partenaire (SPONSOR_OR)
```
POST http://localhost:8080/partenaires
Body :
{
  "nom": "TechCorp Maroc",
  "description": "Sponsor technologique principal de l'université",
  "siteWeb": "https://techcorp.ma",
  "contactNom": "Ahmed Benali",
  "contactEmail": "ahmed@techcorp.ma",
  "contactTelephone": "0661234567",
  "typePartenariat": "SPONSOR_OR"
}
```
**Résultat attendu :** `201 Created`
```json
{
  "id": 1,
  "nom": "TechCorp Maroc",
  "actif": true,
  "typePartenariat": "SPONSOR_OR",
  "dateCreation": "2026-05-26T..."
}
```

#### Test P2 — Créer un deuxième partenaire (PARTENAIRE_MEDIA)
```
POST http://localhost:8080/partenaires
Body :
{
  "nom": "Radio Campus",
  "description": "Partenaire médiatique",
  "siteWeb": "https://radiocampus.ma",
  "contactEmail": "contact@radiocampus.ma",
  "typePartenariat": "PARTENAIRE_MEDIA"
}
```
**Résultat attendu :** `201 Created` avec `"id": 2`

#### Test P3 — Lister tous les partenaires
```
GET http://localhost:8080/partenaires
```
**Résultat attendu :** `200 OK` — tableau de 2 partenaires

#### Test P4 — Lire un partenaire par ID
```
GET http://localhost:8080/partenaires/1
```
**Résultat attendu :** `200 OK` — objet du partenaire 1

#### Test P5 — Filtrer par type
```
GET http://localhost:8080/partenaires?type=SPONSOR_OR
```
**Résultat attendu :** `200 OK` — tableau avec seulement TechCorp Maroc

#### Test P6 — Modifier un partenaire (PUT)
```
PUT http://localhost:8080/partenaires/1
Body :
{
  "nom": "TechCorp Maroc (MAJ)",
  "description": "Description mise à jour",
  "siteWeb": "https://techcorp.ma",
  "contactNom": "Ahmed Benali",
  "contactEmail": "ahmed@techcorp.ma",
  "contactTelephone": "0661234567",
  "typePartenariat": "SPONSOR_OR"
}
```
**Résultat attendu :** `200 OK` — nom mis à jour

#### Test P7 — Désactiver un partenaire
```
PATCH http://localhost:8080/partenaires/1/desactiver
```
**Résultat attendu :** `200 OK` avec `"actif": false`

#### Test P8 — Lister les actifs seulement
```
GET http://localhost:8080/partenaires/actifs
```
**Résultat attendu :** `200 OK` — seulement Radio Campus (actif)

#### Test P9 — Réactiver le partenaire
```
PATCH http://localhost:8080/partenaires/1/activer
```
**Résultat attendu :** `200 OK` avec `"actif": true`

#### Test P10 — Supprimer un partenaire
```
DELETE http://localhost:8080/partenaires/2
```
**Résultat attendu :** `204 No Content`

#### Test P11 — Vérifier la suppression
```
GET http://localhost:8080/partenaires/2
```
**Résultat attendu :** `404 Not Found`

#### Test P12 — Créer sans nom (validation)
```
POST http://localhost:8080/partenaires
Body :
{
  "description": "Sans nom"
}
```
**Résultat attendu :** `400 Bad Request` — erreur de validation

---

### PARTIE 3.8 — Tableau de bord analytique & KPIs

#### Test A1 — Dashboard global
```
GET http://localhost:8080/analytique/dashboard
```
**Résultat attendu :** `200 OK`
```json
{
  "totalEvenements": 1,
  "evenementsParStatut": { "APPROUVE": 1 },
  "evenementsParCategorie": { "COMPETITION": 1 },
  "totalInscriptions": 1,
  "inscriptionsPresentes": 0,
  "inscriptionsAbsentes": 0,
  "tauxPresence": 0.0,
  "totalReservations": 0,
  "reservationsParStatut": { "EN_ATTENTE": 0, "APPROUVEE": 0, "REJETEE": 0, "ANNULEE": 0 },
  "totalClubs": 0,
  "totalUtilisateurs": 1,
  "totalPartenaires": 1,
  "partenairesActifs": 1,
  "totalEvaluations": 0,
  "noteMoyenneGlobale": 0.0,
  "topEvenements": [
    { "evenementId": 1, "titre": "Hackathon 2026", "nbInscriptions": 1 }
  ]
}
```

---

### PARTIE 3.6 — Génération badges / QR codes

> **Prérequis :** avoir exécuté l'Étape 0 (inscription avec id=1)

#### Test B1 — Obtenir le QR code en image PNG
```
GET http://localhost:8080/inscriptions/1/qrcode
```
**Dans Postman :**
1. Cliquer sur la flèche à côté de **Send**
2. Choisir **"Send and Download"**
3. Enregistrer le fichier `.png`
4. Ouvrir l'image → un QR code noir et blanc 300×300 s'affiche

**Résultat attendu :** fichier PNG téléchargé contenant un QR code  
**Contenu du QR :** `INS-1|ALAMI Fatima|Hackathon 2026|01/05/2026 10:30`

#### Test B2 — Obtenir le badge JSON
```
GET http://localhost:8080/inscriptions/1/badge
```
**Résultat attendu :** `200 OK`
```json
{
  "inscriptionId": 1,
  "nomEtudiant": "ALAMI",
  "prenomEtudiant": "Fatima",
  "emailEtudiant": "fatima@unievt.ma",
  "titreEvenement": "Hackathon 2026",
  "dateEvenement": "10/06/2026 09:00",
  "statut": "CONFIRMEE",
  "qrCodeBase64": "data:image/png;base64,iVBORw0KGgo..."
}
```
Le champ `qrCodeBase64` peut être collé directement dans un navigateur pour afficher le QR code.

#### Test B3 — ID inexistant (gestion d'erreur)
```
GET http://localhost:8080/inscriptions/999/badge
```
**Résultat attendu :** `404 Not Found`
```json
{ "status": 404, "error": "Not Found", "message": "Inscription introuvable: 999" }
```

---

### Tableau récapitulatif des tests

| # | Partie | Méthode | URL | Résultat attendu |
|---|---|---|---|---|
| P1 | 3.7 | POST | `/partenaires` | 201 — partenaire créé |
| P2 | 3.7 | POST | `/partenaires` | 201 — 2ème partenaire |
| P3 | 3.7 | GET | `/partenaires` | 200 — liste de 2 |
| P4 | 3.7 | GET | `/partenaires/1` | 200 — 1 objet |
| P5 | 3.7 | GET | `/partenaires?type=SPONSOR_OR` | 200 — liste filtrée |
| P6 | 3.7 | PUT | `/partenaires/1` | 200 — modifié |
| P7 | 3.7 | PATCH | `/partenaires/1/desactiver` | 200 — actif:false |
| P8 | 3.7 | GET | `/partenaires/actifs` | 200 — 1 seul actif |
| P9 | 3.7 | PATCH | `/partenaires/1/activer` | 200 — actif:true |
| P10 | 3.7 | DELETE | `/partenaires/2` | 204 No Content |
| P11 | 3.7 | GET | `/partenaires/2` | 404 Not Found |
| P12 | 3.7 | POST | `/partenaires` (sans nom) | 400 Bad Request |
| A1 | 3.8 | GET | `/analytique/dashboard` | 200 — KPIs complets |
| B1 | 3.6 | GET | `/inscriptions/1/qrcode` | PNG téléchargé |
| B2 | 3.6 | GET | `/inscriptions/1/badge` | 200 — JSON + base64 |
| B3 | 3.6 | GET | `/inscriptions/999/badge` | 404 Not Found |
