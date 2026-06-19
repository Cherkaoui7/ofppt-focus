# RAPPORT DE PROJET : APPLICATION MOBILE « OFPPT FOCUS »
### PORTAIL DE PRODUCTIVITÉ ET DE PRÉPARATION ACADÉMIQUE POUR LES ÉTUDIANTS DE L'OFPPT

---

**Organisme :** Office de la Formation Professionnelle et de la Promotion du Travail (OFPPT)  
**Nom du Projet :** OFPPT Focus  
**Thème de Design :** Clean Minimalism  
**Technologies Principales :** Android (Kotlin, Jetpack Compose, Architecture MVVM, Clean Architecture, Room Database, API Gemini, Navigation Compose)  
**Auteur :** Équipe de Développement « OFPPT Focus »  
**Date :** 19 Juin 2026  

---

## TABLE DES MATIÈRES

1. **PROLEGOMÈNES & INTRODUCTION GÉNÉRALE**
   - 1.1. Contexte et genèse du projet
   - 1.2. Problématique et besoins identifiés chez les étudiants de l'OFPPT
   - 1.3. Objectifs stratégiques et opérationnels de l'application « OFPPT Focus »
   - 1.4. Portée et limites du document

2. **ANALYSE DES BESOINS ET SPÉCIFICATIONS FONCTIONNELLES**
   - 2.1. Cartographie des acteurs (Cas d'utilisation)
   - 2.2. Spécifications fonctionnelles détaillées par module
     - 2.2.1. Module d'authentification et de bienvenue (Welcome Screen)
     - 2.2.2. Tableau de bord centralisé (Dashboard)
     - 2.2.3. Gestion des modules de formation (Mes Modules)
     - 2.2.4. Prise de notes intelligente assistée par l'IA (Notes AI)
     - 2.2.5. Suivi des tâches et planning des examens (Tasks & Exams)
   - 2.3. Exigences non fonctionnelles (Performance, Sécurité, Accessibilité, Portabilité)

3. **CHARTE GRAPHIQUE ET IDENTITÉ VISUELLE : THÈME « CLEAN MINIMALISM »**
   - 3.1. Philosophie du design « Clean Minimalism »
   - 3.2. Palette de couleurs (Système de couleurs M3)
   - 3.3. Typographie d'affichage et corps de texte
   - 3.4. Éléments de design clés (Composants, Espacements, Élévations, Bords arrondis)
   - 3.5. Adaptabilité et responsive design (Prendre en compte les tailles d'écrans)

4. **ARCHITECTURE TECHNIQUE ET PERSISTANCE DES DONNÉES**
   - 4.1. Présentation générale de l'architecture MVVM (Model-View-ViewModel)
   - 4.2. Structuration et découpage des modules (Arborescence du projet)
   - 4.3. Modélisation et persistance des données locales (Room Database & SQLite)
     - 4.3.1. Structure de la base de données Room
     - 4.3.2. Les entités de la base de données
     - 4.3.3. Interfaces d'accès aux données (DAOs) et opérations CRUD
   - 4.4. Gestion des états de l'interface utilisateur (StateFlow et Lifecycle-aware collection)

5. **INTÉGRATION DE L'INTELLIGENCE ARTIFICIELLE DE COIN : L'API GEMINI**
   - 5.1. Rôle de l'IA générative dans le parcours de l'étudiant
   - 5.2. Architecture d'intégration de l'API Gemini
   - 5.3. Conception des Prompts Système (Prompt Engineering) pour un contexte OFPPT
   - 5.4. Traitement asynchrone et gestion de l'affichage de progression

6. **CONCEPTION ET IMPLÉMENTATION DES ÉCRANS (CODE SOURCE SÉLECTIONNÉ)**
   - 6.1. WelcomeScreen : L'expérience d'entrée immersive
   - 6.2. DashboardScreen : Le centre névralgique de l'étudiant
   - 6.3. ModulesScreen : L'espace interactif de cours et de quiz
   - 6.4. NotesAiScreen : Le carrefour de révision augmenté par l'IA
   - 6.5. TasksExamsScreen : Le panneau d'organisation temporelle

7. **ASSURANCE QUALITÉ, TESTS ET COMPILATION**
   - 7.1. Stratégie de tests du projet
   - 7.2. Intégration de Robolectric et Roborazzi pour le rendu d'interface
   - 7.3. Analyse de la conformité du build Gradle et résolution d'erreurs d'intégration

8. **GUIDE DE DÉPLOIEMENT ET D'INSTALLATION EN LOCAL (PC)**
   - 8.1. Prérequis système
   - 8.2. Clonage et ouverture dans Android Studio
   - 8.3. Configuration des variables d'environnement (.env et Secrets Gradle)
   - 8.4. Lancement sur émulateur ou appareil Android physique

9. **PERSPECTIVES D'ÉVOLUTION ET CONCLUSION GÉNÉRALE**
   - 9.1. Fonctionnalités futures d'apprentissage collaboratif
   - 9.2. Extension vers des services cloud centralisés
   - 9.3. Bilan synthétique du projet

---

## 1. PROLEGOMÈNES & INTRODUCTION GÉNÉRALE

### 1.1. Contexte et genèse du projet
L'Office de la Formation Professionnelle et de la Promotion du Travail (OFPPT) est le principal opérateur public marocain en matière de formation professionnelle. Il accueille chaque année des centaines de milliers de stagiaires à travers le Royaume, les formant dans un large spectre de disciplines allant du développement informatique à la gestion d'entreprise, en passant par le génie industriel et le design.

À l'ère de la transformation numérique, l'un des piliers majeurs de la réussite des stagiaires réside dans leur capacité à s'auto-organiser, à réviser efficacement et à consolider de vastes ensembles de connaissances modulaires. "OFPPT Focus" est né de la volonté d'offrir aux stagiaires un compagnon d'apprentissage mobile moderne, capable de centraliser leur emploi du temps, leurs devoirs, leurs examens, tout en intégrant des capacités d'intelligence artificielle de pointe pour synthétiser leurs cours et générer des supports de révision personnalisés.

### 1.2. Problématique et besoins identifiés chez les étudiants de l'OFPPT
Lors d'une phase d'analyse exploratoire réalisée auprès de plusieurs promotions de stagiaires des filières de "Digital Design" et de "Full Stack Development", plusieurs points de friction ont été relevés :
1. **La dispersion de l'information :** Les heures de cours, les dates des évaluations de fin de module (EFM), et la liste des travaux pratiques à rendre sont souvent répartis sur diverses applications (WhatsApp, Teams, e-learning OFPPT), engendrant des retards et du stress.
2. **La surcharge cognitive des programmes modulaires :** Les cursus de l'OFPPT sont denses et s'enchaînent de manière intensive sur de courtes périodes. Les stagiaires ont parfois du mal à identifier les thématiques clés d'un cours pour en dégager des synthèses exploitables.
3. **Le manque d'outils d'auto-évaluation adaptatifs :** Les stagiaires cherchent constamment des moyens interactifs de tester leurs connaissances avant de passer les examens officiels, notamment par des quiz auto-générés.

### 1.3. Objectifs stratégiques et opérationnels de l'application « OFPPT Focus »
L'application "OFPPT Focus" s'est fixé des objectifs précis :
- **Centraliser :** Rassembler dans une interface unique et intuitive l'ensemble des modules d'apprentissage d'un stagiaire, ainsi que ses tâches et échéances académiques.
- **Accompagner par l'IA :** Mettre à disposition un moteur d'IA générative (propulsé par Gemini) capable de rédiger à la demande des fiches de synthèse structurées en français à partir de notes brutes ou de plans de cours.
- **Engager l'utilisateur :** Structurer l'expérience utilisateur de manière élégante, reposant sur des concepts de gamification légère, tels qu'un indicateur de progression quotidien visuel et des statuts urgents pour les examens imminents.

### 1.4. Portée et limites du document
Ce rapport constitue le document de référence technique et conceptuel du projet "OFPPT Focus". Il expose la démarche de conception visuelle, détaille l'architecture logicielle retenue (Kotlin, Jetpack Compose, bases de données SQLite via Room, API Gemini), fournit des extraits clés de code source, commente les défis rencontrés lors de l'intégration, et donne les instructions complètes pour exécuter le projet en local sur une machine de développement.

---

## 2. ANALYSE DES BESOINS ET SPÉCIFICATIONS FONCTIONNELLES

### 2.1. Cartographie des acteurs (Cas d'utilisation)
L'acteur principal du système est le **Stagiaire de l'OFPPT**. L'application fonctionne de manière autonome en mode client-serveur hybride : l'ensemble des données (modules, quiz, leçons, fiches de notes, tâches et examens) est conservé et géré localement sur le téléphone du stagiaire grâce à une base de données SQLite robuste, garantissant un fonctionnement parfait hors ligne (Offline-First). L'IA de Gemini intervient en ligne pour les requêtes de traitement avancé.

```
      +------------------+
      |     STAGIAIRE    |
      +--------+---------+
               |
               +---------------------------------> [ S'authentifier / Configurer Profil ]
               |
               +---------------------------------> [ Visionner le Dashboard / Avancement ]
               |
               +---------------------------------> [ Gérer & Consulter les Modules ]
               |                                     * Consulter cours, leçons, quiz
               |
               +---------------------------------> [ Consolider & Rédiger des Notes IA ]
               |                                     * Saisir des notes manuelles
               |                                     * Lancer l'assistant IA
               |
               +---------------------------------> [ Suivre les Tâches & Planifier Examens ]
                                                     * Marquer les tâches complétées
                                                     * Voir l'échéancier des évaluations
```

### 2.2. Spécifications fonctionnelles détaillées par module

#### 2.2.1. Module d'authentification et de bienvenue (Welcome Screen)
Le point d'entrée de l'application propose l'identification de l'utilisateur. Le stagiaire peut entrer son nom pour personnaliser l'ensemble de l'interface graphique. Cette saisie est persistée localement afin d'éviter toute re-saisie lors des futurs lancements. L'écran de bienvenue utilise un style moderne, reposant sur un arrière-plan à gradient radial épuré, des contrastes doux et un bouton d'action principal à forte affordance.

#### 2.2.2. Tableau de bord centralisé (Dashboard)
Le tableau de bord est conçu comme le cockpit de la journée de l'étudiant. Il intègre :
- **Un en-tête institutionnel :** Affiche la marque « OFPPT Focus » aux côtés d'un avatar reprenant l'initiale de l'utilisateur.
- **Une carte de progression dynamique (Hero Banner) :** Présente le ratio d'avancement des tâches du jour (ex: "4 sur 6 tâches complétées") associé à une jauge de progression circulaire moderne et un badge capsule de type "75% Progress".
- **Un module de raccourcis rapides :** Permet d'explorer instantanément les modules récents (ex: React, Laravel, MongoDB) avec des icônes distinctes.
- **Une zone d'alerte urgente :** Affiche l'examen le plus proche dans le temps sous forme d'une carte exclusive, comportant un badge textuel "URGENT", un badge calendrier stylisé de couleur bleu pastel contrasté (affichant le mois et le jour en gras), et un décompte en temps réel du nombre de jours restants (ex: "Examen UML • Salle 02 • 5 Jours restants").

#### 2.2.3. Gestion des modules de formation (Mes Modules)
Le cursus de formation est structuré en plusieurs modules. Pour chaque module, l'application fournit :
- Une vue d'ensemble avec des statistiques spécifiques (nombre de cours validés, quiz complétés).
- Des listes d'onglets ergonomiques qui segmentent le contenu en "Leçons", "Quiz" et "Assistance IA".
- Un moteur de quiz à choix multiples (QCM) local interactif pour tester l'assimilation du module. Ce module calcule dynamiquement le score de l'étudiant et propose un retour visuel coloré selon la réussite ou l'échec de la réponse sélectionnée.

#### 2.2.4. Prise de notes intelligente assistée par l'IA (Notes AI)
Cette section permet au stagiaire d'éditer ou de coller ses résumés de cours bruts. D'un simple clic sur le bouton "Générer Fiche IA", l'application interroge l'API de Gemini qui, guidée par un prompt structuré de manière rigoureuse, reformule le texte en une fiche d'étude synthétique hautement éducative, comportant un glossaire de définitions clés, des questions de révision théoriques et un résumé concis. L'affichage s'effectue dans un conteneur de texte sélectionnable (permettant la copie aisée) accompagné d'indicateurs de chargement lisses.

#### 2.2.5. Suivi des tâches et planning des examens (Tasks & Exams)
Un agenda modulaire permet à l'apprenant d'ajouter de nouvelles tâches scolaires (ex: "Finir le TP de Laravel") et de planifier ses examens. Les tâches en attente peuvent être validées directement d'un simple clic sur une case à cocher, ce qui déclenche instantanément la mise à jour des jauges de progression globale de l'application. Les examens apparaissent dans un fil chronologique intelligent, calculant et affichant le nombre exact de jours les séparant de la date courante.

### 2.3. Exigences non fonctionnelles
- **Performance :** L'application doit démarrer en moins de 1,5 seconde. Les transitions entre écrans Jetpack Compose doivent maintenir un taux d'image supérieur à 60 FPS, sans saccade notable.
- **Sécurité :** Les clés privées d'API (comme le token d'accès au modèle Gemini) ne doivent jamais être codées en dur dans le dépôt public. Elles doivent être injectées via l'environnement local (.env) et traitées dans le flux de compilation par le plugin `BuildConfig`.
- **Accessibilité :** Tous les boutons interactifs doivent occuper une surface d'action minimale de **48dp x 48dp** (Material 3 standard). Les rapports de contraste de couleur doivent respecter le standard WCAG AA (notamment sur les nuances de gris et de teintes pour le mode sombre). Les descriptions d'icônes (`contentDescription`) doivent être pleinement configurées pour TalkBack.
- **Offline-First :** L'affichage de l'historique et des listes de tâches / examens doit être instantané, y compris en l'absence de réseau internet, en s'appuyant sur la base de données SQLite locale.

---

## 3. CHARTE GRAPHIQUE ET IDENTITÉ VISUELLE : THÈME « CLEAN MINIMALISM »

### 3.1. Philosophie du design « Clean Minimalism »
Délaissant le style surchargé voire agressif des chartes graphiques dites "Cyberpunk" ou de type "Néon", le thème **Clean Minimalism** s'axe sur la sérénité visuelle, la clarté informative et le professionnalisme. L'application utilise ainsi de généreux espaces négatifs, des bordures douces, des arrondis prononcés (jusqu'à `28.dp` pour les cartes principales) et des nuances colorées claires inspirées des systèmes d'exploitation modernes, évitant ainsi la fatigue oculaire du stagiaire durant ses longues sessions d'étude nocturnes.

### 3.2. Palette de couleurs (Système de couleurs M3)
La palette chromatique est organisée de manière cohérente au sein des classes composables d'Android en deux modes distincts : le mode clair et le mode sombre adaptatif.

| Nom de Couleur | Équivalent Hexadécimal | Rôle dans l'application |
|---|---|---|
| **TechTeal** | `#0061A4` | Couleur de marque principale, tons bleus professionnels et engageants de l'OFPPT. |
| **BrandBlueLight** | `#D1E4FF` | Fond des badges actifs, sélections, boutons légers ou arrière-plans de badges calendrier. |
| **DeepNavyText**| `#001E2F` | Teinte de texte principale et intense en mode clair pour une lisibilité maximale. |
| **SlateGrayText**| `#41484D` | Nuance pour légendes, détails optionnels et statuts inactifs. |
| **CyberLightBg** | `#FDFBFF` | Arrière-plan général de l'application en mode clair (assure l'effet épuré). |
| **CyberLightCard**| `#FFFFFF` | Fond de carte pur, délimité par une bordure fine et discrète. |
| **CyberLightBorder**| `#DDE3EA` | Ligne de contour subtile limitant chaque conteneur. |
| **UrgentRedBg** | `#FFDAD6`| Arrière-plan des capsules d'évaluation urgente. |
| **UrgentRedText** | `#410002`| Couleur du texte alertant d'un examen imminent. |
| **AccentOrange** | `#F27D26` | Variante thématique notamment utilisée pour les aspects Laravel ou créatifs. |
| **SoftGrayBg** | `#F0F4F8` | Fond de secours neutre pour délimiter les listes secondaires. |

*Note sur le mode sombre :* En version adaptative sombre, l'application substitue au blanc brillant des teintes de type Midnight Navy Slate (`#0B121F` pour le fond général, `#172033` pour les fonds de cartes, et une bordure fine sombre de couleur `#2C3E5B`), conservant un contraste haut de gamme et reposant pour la vision.

### 3.3. Typographie d'affichage et corps de texte
Le système typographique repose sur l'échelle type de Material Design 3 :
- **Titres principaux (Headline Medium / Large) :** Utilisation de polices sans-serif géométriques, configurées avec un style extra-gras (`FontWeight.Black` / `FontWeight.ExtraBold`), des termes doubles pour marquer la scissure de marque (ex: "OFPPT" en bleu marine sombre et "Focus" en bleu institutionnel TechTeal), et un crénelage réduit pour donner de l'impact.
- **Titres de sections (Title Medium) :** Style gras structuré en gras minimaliste (`FontWeight.Bold`) associé à une couleur contrastée pour guider instantanément l'œil vers l'essentiel.
- **Corps de texte et légendes (Body Small, Label Small) :** Taille fine avec des espacements de lettres accrus (`letterSpacing = 1.2.sp`) spécifiquement sur les petits libellés en majuscules (ex: "STUDENT PORTAL") pour aérer l'interface.

### 3.4. Éléments de design clés
- **Les angles de cartes :** Toutes les cartes d'informations disposent d'angles arrondis prononcés (`RoundedCornerShape(28.dp)`), en cohérence avec les standards Material 3 (Shape ExtraLarge).
- **Les bordures :** Au lieu d'utiliser des ombres lourdes qui salissent les interfaces minimalistes, l'application utilise exclusivement des bordures fines d'un pixel (`1.dp`) et de couleur gris ardoise léger ou sombre, offrant un look découpé au laser, d'une grande finesse ("BorderStroke").
- **Les boutons et bulles interactives :** Les boutons d'actions disposent d'un effet visuel de "ripple" au clic. Les étiquettes actives et capsules n'ont pas de bordure mais utilisent un fond plein pastel (ex : `BrandBlueLight`), ce qui simplifie grandement l'expérience de lecture.

```
       +---------------------------------------------+
       |   STUDENT PORTAL                            |  <- Label épuré espacé
       |   OFPPT Focus                               |  <- Titre principal double teinte
       +---------------------------------------------+
       |  +---------------------------------------+  |
       |  |  Bonjour, Amine!                      |  |
       |  |  You completed 4/6 tasks today.       |  |  <- Hero Banner structurée
       |  |  [ 75% Progress ]      (( 75% ))      |  |
       |  +---------------------------------------+  |
       |  EXAMEN À VENIR                [ URGENT ]   |  <- Capsule alerte minimaliste
       |  +---------------------------------------+  |
       |  | [Dec] UML & Design Patterns           |  |  <- Calendrier compact intégré
       |  | [14 ] Room 02 • 5 DAYS LEFT           |  |
       |  +---------------------------------------+  |
       +---------------------------------------------+
```

### 3.5. Adaptabilité et responsive design
L'écran principal de l'application évite d'utiliser des hauteurs d'éléments fixes et rigides. Les listes d'items recourent à des conteneurs flexibles de type `Column` ou `LazyColumn` avec des coefficients de proportionnalité (`Modifier.weight(1f)`), garantissant que l'interface se redimensionne gracieusement sur de grands écrans cellulaires, de petits téléphones compacts, ou des écrans pliables en cours de transition.

---

## 4. ARCHITECTURE TECHNIQUE ET PERSISTANCE DES DONNÉES

### 4.1. Présentation générale de l'architecture MVVM (Model-View-ViewModel)
L'application propose une séparation stricte des concepts en s'adossant sur le patron d'architecture **MVVM (Model-View-ViewModel)** recommandé par Google pour Android :

1. **La Couche Vue (View / UI) :** Élaborée entièrement en **Jetpack Compose**. Elle écoute les flux d'états exposés par les ViewModels et rafraîchit l'interface de manière réactive. Elle ne prend aucune décision métier et n'accède jamais directement à la base de données.
2. **La Couche ViewModel :** Agit comme le pont entre l'interface utilisateur et les données. Elle charge les entités depuis la base de données Room, expose des structures asynchrones `StateFlow`, et met en œuvre les appels à l'IA Gemini. Les opérations longues s'effectuent via les Couroutines Kotlin sur des Threads secondaires (`Dispatchers.IO`).
3. **La Couche Modèle (Repository & Room Database) :** Gère la persistance locale absolue. Elle fait office d'unique source de vérité (Single Source of Truth) pour toutes les entités de l'élève.

### 4.2. Structuration du Projet (Arborescence)
L'arborescence des paquets respecte scrupuleusement le découpage de l'architecture propre :

```
com.example/
│
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt        # Classe d'initialisation Room
│   │   ├── ExamDao.kt            # Interface DAO pour les Examens
│   │   ├── ModuleDao.kt          # Interface DAO pour les Modules/Quiz
│   │   └── TaskDao.kt            # Interface DAO pour les Tâches
│   │
│   └── model/
│       ├── Exam.kt               # Classe modèle de données des Examens
│       ├── ModuleEntity.kt       # Classe modèle des Modules académique
│       ├── QuizQuestion.kt       # Modèle imbriqué pour les questions de QCM
│       ├── StudyNote.kt          # Classe modèle des fiches de notes
│       └── StudyTask.kt          # Classe modèle pour les devoirs et tâches
│
├── ui/
│   ├── screens/
│   │   ├── DashboardScreen.kt    # Écran d'accueil principal
│   │   ├── ModulesScreen.kt      # Écran des modules, cours et quiz
│   │   ├── NotesAiScreen.kt      # Écran de prise de notes assistée par IA
│   │   ├── TasksExamsScreen.kt   # Écran du calendrier académique et tâches
│   │   └── WelcomeScreen.kt      # Écran de bienvenue et saisie du nom
│   │
│   ├── theme/
│   │   ├── Color.kt              # Fichier de palette du thème Clean Minimalism
│   │   ├── Theme.kt              # Configuration des schémas clair / sombre M3
│   │   └── Type.kt               # Typographies associées à la charte graphique
│   │
│   └── viewmodel/
│       └── MainViewModel.kt      # Unique ViewModel centralisant l'état complet
```

### 4.3. Modélisation et persistance des données locales (Room Database & SQLite)

#### 4.3.1. Structure de la base de données Room
La persistance est assurée par un fichier SQLite local encapsulé par la bibliothèque jetpack **Room Database**. Pour maximiser la rapidité d'exécution et réduire le traitement d'initialisation, la base de données pré-remplit les informations académiques indispensables lors de sa création initiale si aucune donnée n'est pré-existante (Callback Room `onCreate`).

#### 4.3.2. Les entités de la base de données
L'application s'appuie sur des classes Kotlin annotées `@Entity`. Les objets complexes, tels que des listes de questions pour les quiz interactifs ou les listes d'onglets de leçons, sont convertis automatiquement en formats sérialisables JSON (via un convertisseur `@TypeConverter` propre propulsé par `kotlinx.serialization`) afin d'être stockés de manière optimale dans des colonnes de texte SQLite.

Voici un exemple synthétique de modélisation pour l'entité `StudyTask` :
```kotlin
@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val moduleName: String,
    val isCompleted: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis()
)
```

Voici également la structure d'un examen (`Exam.kt`) :
```kotlin
@Entity(tableName = "exams")
data class Exam(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val examTitle: String,
    val moduleName: String,
    val examDate: Long, // Horodatage UNIX en millisecondes
    val revisionStatus: String // Ex: "NON_COMMENCE", "EN_COURS", "PRET"
)
```

#### 4.3.3. Interfaces d'accès aux données (DAOs) et opérations CRUD
Le système implémente des requêtes réactives complexes retournant des objets de type `Flow<List<T>>`. Cela garantit que chaque fois qu'une tâche est insérée, supprimée ou complétée dans la couche de persistance, la liste correspondante affichée à l'écran de l'utilisateur se rafraîchit automatiquement de manière réactive, sans exiger de requête de rafraîchissement manuel.

Exemple d'interface de requêtes typique (`TaskDao`) :
```kotlin
@Dao
interface TaskDao {
    @Query("SELECT * FROM study_tasks ORDER BY dateCreated DESC")
    fun getAllTasksFlow(): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask)

    @Query("UPDATE study_tasks SET isCompleted = :completed WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, completed: Boolean)

    @Delete
    suspend fun deleteTask(task: StudyTask)
}
```

### 4.4. Gestion des états de l'interface utilisateur
Dans le `MainViewModel`, les données issues de Room sont combinées et exposées sous la forme de flux de données sécurisés `StateFlow`. Au niveau de l'interface utilisateur Jetpack Compose, ces flux sont collectés à l'aide de l'extension réactive moderne :
```kotlin
val tasks by viewModel.tasksFlow.collectAsStateWithLifecycle(initialValue = emptyList())
```
Cette méthode suspend l'écoute réactive dès que l'application passe en arrière-plan (économisant ainsi la batterie de l'appareil par rapport aux techniques de flux traditionnelles) et réactive l'abonnement automatiquement dès que l'application redevient visible à l'écran.

---

## 5. INTÉGRATION DE L'INTELLIGENCE ARTIFICIELLE DE COIN : L'API GEMINI

### 5.1. Rôle de l'IA générative dans le parcours de l'étudiant
L'accès instantané aux informations de cours est un atout, mais savoir les trier et en extraire l'essence en est un autre. L'intégration de l'intelligence artificielle de pointe dans "OFPPT Focus" ne vise pas à remplacer l'effort intellectuel du stagiaire, mais à le catalyser. Grâce à des appels sécurisés vers les modèles Gemini de Google, l'étudiant dispose à tout moment d'un assistant de révision capable de :
1. Structurer un texte désorganisé ou des notes brutes écrites à la hâte en cours de TD.
2. Dégager rapidement un lexique technique des termes métiers employés (très utile pour les filières techniques).
3. Soumettre des questions interactives de révision pour valider la compréhension d'un concept complexe.

### 5.2. Architecture d'intégration de l'API Gemini
L'intégration s'effectue directement en Kotlin par l'intermédiaire de requêtes REST HTTP sécurisées vers la passerelle d'API de Google AI Studio, exploitant la méthodologie asynchrone pour éviter tout blocage de l'interface graphique du téléphone.

```
+------------------+         Appel asynchrone         +-------------------------+
|                  | -----------------------------> |                         |
|  NotesAiScreen   |                                |  MainViewModel          |
|  (Interface URL) | <----------------------------- |  (Gestionnaire d'état)  |
|                  |     Mise à jour StateFlow      |                         |
+------------------+                                +------------+------------+
                                                                 |
                                                                 |  Requête HTTP POST
                                                                 |  (Envoi Prompt + Clé API)
                                                                 v
                                                    +-------------------------+
                                                    |    API Google Gemini    |
                                                    |  (Traitement du modèle  |
                                                    |   gemini-1.5-flash /    |
                                                    |   gemini-2.5-flash)     |
                                                    +-------------------------+
```

### 5.3. Conception des Prompts Système (Prompt Engineering)
Afin de forcer l'IA à adopter un comportement éducatif conforme au cadre académique de l'OFPPT, un prompt système strict (System Instruction) est combiné à la saisie de l'étudiant. Ce prompt contraint le format de sortie de l'IA à un style de document rigoureux, évitant de longues phrases superflues et fournissant une structure à puces épurée.

Extrait logique du prompt d'instruction :
> *"Tu es un assistant pédagogique expert de l'OFPPT (Maroc). Ton objectif est de réorganiser, de clarifier et de formaliser de manière professionnelle les notes de cours fournies par le stagiaire de formation. Tu dois rédiger ton retour exclusivement en français. Structure ta réponse avec les sections suivantes : 🔬 RÉSUMÉ SYNTHÉTIQUE (court et précis), 🔑 GLOSSAIRE (définitions des 3 concepts clés), et 📝 QUESTIONS D'AUTO-ÉVALUATION (génère 3 questions et réponses associées pour réviser)."*

### 5.4. Traitement asynchrone et gestion de l'affichage de progression
Afin d'offrir un rendu de qualité fidèle au thème Clean Minimalism, le déclenchement de la génération de notes modifie instantanément l'état de l'écran en positionnant une valeur booléenne `isLoading = true`. Durant le temps de réponse réseau de l'IA stable (généralement entre 1 et 3 secondes), l'écran affiche un composant `CircularProgressIndicator` de taille réduite (`20.dp`) parfaitement intégré dans un bandeau d'information, informant calmement l'apprenant de l'avancement de la tâche.

Une fois la requête résolue avec succès, le texte résultant est injecté dans l'entité correspondante en base de données de manière asynchrone :
```kotlin
viewModel.generateAiSummary(noteId, rawContent)
```
La réactivité de l'architecture propage cette mise à jour à l'écran, élimine le logo de chargement, et affiche le texte final au sein d'un bloc de sélection de texte robuste.

---

## 6. CONCEPTION ET IMPLÉMENTATION DES ÉCRANS (CODE SOURCE SÉLECTIONNÉ)

### 6.1. WelcomeScreen : L'expérience d'entrée immersive
`WelcomeScreen.kt` fournit l'affichage de départ en collectant et validant le nom d'utilisateur du stagiaire. L'arrière-plan de l'écran intègre des éléments décoratifs modernes minimalistes, à l'image d'un grand disque gradient de couleur bleu pastel contrasté (`BrandBlueLight`) avec un effet de flou pour simuler un style "Glassmorphic Aura".

```kotlin
// Extrait de l'interface de WelcomeScreen.kt
Box(
    modifier = Modifier
        .fillMaxSize()
        .background(if (isSystemInDarkTheme()) CyberDarkBg else CyberLightBg),
    contentAlignment = Alignment.Center
) {
    // Halo lumineux en arrière-plan
    Box(
        modifier = Modifier
            .size(350.dp)
            .offset(x = 100.dp, y = (-120).dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(TechTeal.copy(alpha = 0.1f), Color.Transparent)))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) CyberDarkCard else Color.White
        ),
        border = BorderStroke(1.dp, if (isSystemInDarkTheme()) CyberDarkBorder else CyberLightBorder)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo OFPPT
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrandBlueLight),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "F",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = TechTeal,
                        fontWeight = FontWeight.Black
                    )
                )
            }
            // Code de saisie du nom pour l'accueil...
        }
    }
}
```

### 6.2. DashboardScreen : Le centre névralgique de l'étudiant
L'écran d'accueil coordonne l'ensemble des modules. Il a été revu pour incorporer notre en-tête d'identification avec avatar dynamique de couleur bleu ciel (#D1E4FF), suivi de notre carte principale de progression « Study Progress ».

Un aspect de code particulièrement intéressant concerne l'implémentation de la jauge de progression circulaire du Dashboard, qui s'alimente dynamiquement des données stockées dans Room :

```kotlin
// Dans DashboardScreen.kt : Intégration de l'en-tête et avatar Clean Minimalism
Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    Column {
        Text(
            text = "STUDENT PORTAL",
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isDark) Color.LightGray else SlateGrayText,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "OFPPT",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = if (isDark) Color.White else DeepNavyText,
                    fontWeight = FontWeight.Black
                )
            )
            Text(
                text = "Focus",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = TechTeal,
                    fontWeight = FontWeight.Black
                )
            )
        }
    }

    // Avatar d'identification épuré
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(BrandBlueLight)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (userName.isNotEmpty()) userName.take(1).uppercase() else "A",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = DeepNavyText,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
```

### 6.3. ModulesScreen : L'espace interactif de cours et de quiz
Le fragment d'écran des modules propose un contrôleur d'onglets réactif (`TabRow`) avec indicateurs animés (`tabIndicatorOffset`). L'intégration du système de réponse aux questions de quiz permet aux stagiaires de s'entraîner sur un module technique (ex : React). Le composant génère instantanément une bordure teintée en vert électrique ou rouge d'alarme selon que le stagiaire clique sur la bonne ou la mauvaise réponse respective.

```kotlin
// Extrait de sélection de réponse dans les quiz (ModulesScreen)
Card(
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onSelectedAction(index) },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
        containerColor = if (isSelected) {
            if (index == question.correctAnswerIndex) ElectricGreen.copy(alpha = 0.15f)
            else AlarmRed.copy(alpha = 0.15f)
        } else {
            if (isDark) CyberDarkCard else Color.White
        }
    ),
    border = BorderStroke(
        width = 1.dp,
        color = if (isSelected) {
            if (index == question.correctAnswerIndex) ElectricGreen else AlarmRed
        } else {
            if (isDark) CyberDarkBorder else CyberLightBorder
        }
    )
) {
    // Contenu textuel de la question...
}
```

### 6.4. NotesAiScreen : Le carrefour de révision augmenté par l'IA
Cet écran met en exergue l'interaction directe avec l'API de Gemini pour structurer une étude solide. L'étudiant tape ses notes, appuie sur le bouton de déclenchement, et observe les résultats au sein du composant asynchrone.

```kotlin
// NotesAiScreen.kt : Affichage asynchrone lors de la rédaction de fiches de notes
if (note.isAiGenerating) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BrandBlueLight.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 3.dp,
                color = TechTeal
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "L'IA prépare votre fiche méthodologique...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = DeepNavyText,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
```

### 6.5. TasksExamsScreen : Le panneau d'organisation temporelle
C'est ici que l'étudiant planifie de manière exhaustive ses livrables et EFM (Évaluations de Fin de Module). Les formulaires d'ajout intègrent des invites claires pour l'étudiant, avec des champs de saisie structurés pour le nom du devoir, du module requis et la date visée. L'affichage s'effectue sous forme de listes d'items distincts épurés de tout élément de design obstructif.

---

## 7. ASSURANCE QUALITÉ, TESTS ET COMPILATION

### 7.1. Stratégie de tests du projet
Dans l'optique d'offrir un code stable, facilement maintenable à travers diverses évolutions fonctionnelles, le projet supporte d'importantes options d'intégration de tests. Plutôt que de reposer sur de lourds tests instrumentés exigeant un émulateur ouvert ou un appareil de test physique connecté (très consommateurs en bande-passante et en temps machine), l'architecture implémente le standard de **Tests Locaux JVM accélérés**.

### 7.2. Intégration de Robolectric et Roborazzi pour le rendu d'interface
- **Robolectric :** Simule l'environnement système de l'OS Android à l'intérieur de la machine virtuelle Java (JVM) du PC de développement. Cela permet de tester les interactions avec la base de données Room et les flux réactifs du `MainViewModel` en quelques secondes à peine.
- **Roborazzi :** Offre un outillage de capture et de vérification d'images d'écrans (Screenshot Testing). En exécutant une commande de vérification, Roborazzi compare l'aspect pixel à pixel de l'écran généré par Jetpack Compose avec un modèle de référence stocké dans le projet, permettant de traquer immédiatement toute régression visuelle.

### 7.3. Analyse de la conformité du build Gradle et résolution d'erreurs d'intégration
Le projet s'appuie sur le système de construction Gradle (Kotlin DSL). Lors des phases de développement et d'intégration visuelle, plusieurs défauts typographiques ou conflits de portée d'attributs ont été identifiés et résolus avec rigueur par l'équipe :
- *Remplacement des fonctions dépréciées de style de carte :* Des occurrences de configuration d'ombres et de bordures basées sur `CardDefaults.outlinedCardBorder().copy(...)` ont provoqué des incidents de compilation en raison de restrictions d'instanciation sous Android. Ces déclarations ont été remplacées de manière chirurgicale à travers l'ensemble des fichiers par l'opérateur plus stable `BorderStroke(width, color)`.
- *Correction de portées de variables typographiques :* L'insertion initiale d'une référence dynamique à un module de salle (`classroom`) sur les objets d'examens a provoqué des incidents dans l'écran du tableau de bord. La variable a été corrigée en s'adossant sur les constantes d'affichage de salles disponibles dans notre jeu de données.
- *Filiation d'icônes Compose :* L'absence de l'attribut `modifier` sur certains wrappers d'icônes Material Design (ex: `Icon(Icons.Outlined.NoteAlt, size = ... )`) a provoqué des refus de compilation. Ces contraintes ont été nettoyées en insérant les propriétés de taille directement au niveau de modificateurs standards (`modifier = Modifier.size(56.dp)`).

---

## 8. GUIDE DE DÉPLOIEMENT ET D'INSTALLATION EN LOCAL (PC)

Ce guide décrit en détail les étapes méthodologiques pour récupérer le projet "OFPPT Focus" conçu dans Google AI Studio et l'exécuter localement sur un ordinateur de bureau (Windows, macOS ou Linux).

### 8.1. Prérequis système
Pour compiler et exécuter l'application, assurez-vous de disposer des éléments système suivants :
- **Java Development Kit (JDK) :** Version 17 minimale (recommandée : JDK 17 intégrée à Android Studio).
- **Android Studio :** Version Ladybug (2024.2.1) ou supérieure.
- **Gradle :** Version compatible avec le plugin AGP déclaré (géré automatiquement par Android Studio lors de l'ouverture).
- **Connexion Internet stable :** Indispensable pour la première synchronisation et le téléchargement des dépendances Gradle.

### 8.2. Clonage et ouverture dans Android Studio
1. **Téléchargement du ZIP :** Dans l'interface utilisateur de Google AI Studio, cliquez sur le menu des options du projet (icône engrenage / paramètres) puis sélectionnez **Export Project as ZIP**.
2. **Extraction :** Extrayez le fichier ZIP dans votre répertoire de travail habituel (ex: `C:\Projets\OFPPTFocus` ou `~/Developer/OFPPTFocus`).
3. **Ouverture :**
   - Lancez **Android Studio**.
   - Cliquez sur **Open** (Ouvrir un projet existant).
   - Naviguez jusqu'au dossier racine racine contenant le fichier `settings.gradle.kts` et cliquez sur **OK**.
   - Patientez quelques minutes pendant que Gradle effectue le téléchargement et l'indexation de l'arborescence des paquets dépendants.

### 8.3. Configuration des variables d'environnement (.env et Secrets Gradle)
L'interaction avec l'IA de Gemini requiert l'utilisation d'une clé d'accès d'API valide.
1. Générez une clé d'accès d'API gratuite depuis la plateforme **Google AI Studio** sous le lien officiel : [https://aistudio.google.com/](https://aistudio.google.com/).
2. Copiez l'exemple de fichier d'environnement situé à la racine du projet :
   - Renommez le fichier `.env.example` en un nouveau fichier nommé `.env`.
3. Éditez le fichier `.env` nouvellement créé et intégrez-y votre clé d'API de la sorte :
   ```env
   GEMINI_API_KEY=votre_cle_api_gemini_reelle_ici
   ```
4. Lors de la compilation, le plugin Gradle (Secrets Gradle Plugin) se charge de lire ce fichier de configuration local et de générer une variable sécurisée accessible en code via la commande `BuildConfig.GEMINI_API_KEY` sans l'exposer dans le code source visible du projet.

### 8.4. Lancement sur émulateur ou appareil Android physique
- **Appareil Physique (Recommandé) :** 
  - Activez les **Options pour les développeurs** sur votre téléphone ou tablette Android (en appuyant 7 fois consécutives sur le numéro de build du système dans Paramètres > À propos de l'appareil).
  - Cochez l'option **Débogage USB**.
  - Connectez le téléphone au PC par câble. L'appareil apparaît dans le menu déroulant des terminaux cibles en haut de l'interface d'Android Studio.
- **Émulateur :**
  - Ouvrez l'outil de gestion d'appareils virtuels (**Device Manager**) d'Android Studio.
  - Cliquez sur **Create Device** et configurez un appareil moderne (ex: Pixel 7 avec API 34+).
  - Démarrez l'appareil virtuel.
- **Lancer la compilation :** Cliquez sur le bouton vert **Run** (icône triangle vert) ou appuyez sur le raccourci clavier `Shift + F10` sous Windows ou `Control + R` sous macOS. L'APK est construit, transféré et installé automatiquement sur l'appareil cible.

---

## 9. PERSPECTIVES D'ÉVOLUTION ET CONCLUSION GÉNÉRALE

### 9.1. Fonctionnalités futures d'apprentissage collaboratif
Bien qu'exécutée en mode Offline-First pour garantir un confort d'accès optimal et éliminer les obstacles de connexion réseau fluctuante des campus, "OFPPT Focus" pourrait s'enrichir de perspectives collaboratives à court terme :
- **Le partage de fiches IA :** Possibilité d'exporter une fiche rédigée par Gemini au format PDF standardisé ou de la partager à ses collègues de formation par scan de QR code sur place.
- **La synchronisation de groupes d'étude :** Intégration d'un protocole sans fil local (Wi-Fi Direct / Nearby Share) pour planifier des travaux pratiques de groupes ou réviser des plannings d'activités sans recours à une connexion Internet externe.

### 9.2. Extension vers des services de cloud centralisés
Pour les besoins d'intégration au sein de l'environnement global de l'OFPPT, l'application pourrait interroger des APIs centralisées pour alimenter automatiquement les examens programmés (EFM nationales, examens régionaux de passage), supprimant l'exigence de saisie manuelle de ces plannings par les stagiaires tout en leur envoyant des notifications sur l'avancement des fiches de notes créées.

### 9.3. Bilan synthétique du projet
Le projet de développement mobile **OFPPT Focus** a permis d'aboutir à un produit logiciel à forte valeur éducative. En s'appuyant sur des standards actuels et robustes du développement natif Android (Kotlin, Jetpack Compose, Room DB) et en y mêlant les capacités d'analyse de l'intelligence artificielle générative moderne (API Gemini), l'application répond de manière rigoureuse aux points de blocage organisationnels qui jalonnent le parcours d'apprentissage des futurs professionnels du Royaume du Maroc. Tout cela est magnifié par une esthétique visuelle moderne, épurée et très accessible (Thème *Clean Minimalism*), garantissant une appropriation rapide, sereine et pérenne de l'outil par les apprenants de l'institution.
