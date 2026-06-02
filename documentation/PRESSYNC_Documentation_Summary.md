# PRESSYNC Documentation — File Summary

**Project:** Pressync — a local WiFi-based attendance tracking system using OpenWRT routers with captive portal authentication.

**Language:** Romanian (with diacritics in refined versions, without in WIP versions)

---

## Root (`documentation/`)

| File | Type | Description |
|---|---|---|
| `PresSync-documentation-roadmap.md` | .md | Table of contents/roadmap for the entire documentation. Lists chapters: Introduction, Definirea Problemei (Ch.2), Analiza software a sistemului (Ch.3), Proiectarea si implementarea (Ch.4), Concluzii, Anexe, Bibliografie. Most items marked `[done]`. |
| `Codebase.md` | .md | Technical codebase documentation for the Attendance Module. Describes CQRS pattern, connections to User/Event/EventCategory modules. Includes full Java listings for `Attendance`, `AttendanceController`, `CommandHandler`, `QueryHandler`, and repository classes (~447 lines). |
| `ThePressync Secret SauceNote` | txt | Architecture note describing core idea: OpenWRT router with captive portal sends users to a local app on a Raspberry Pi. Runs in a "vacuum" (no internet needed); captive portal acts as location verifier. |
| `AnalizaEPrezenta.asta` | .asta (ZIP) | Astah/UML model file containing UML diagrams (class, sequence, etc.) for the system. |
| `pressyncdb_1.png` | .png | Database diagram / ER diagram of the Pressync schema. |
| `documentation.zip` | .zip | Archive of Pressync documentation. |
| `documentation1.zip` | .zip | Archive of Pressync documentation (alternate version). |

---

## `.misc/`

Contains exact duplicates of root files: `Codebase.md`, `ThePressync Secret SauceNote`, `pressyncdb_1.png`, `documentation.zip`, `documentation1.zip`.

---

## `AnalizaCerinte/` (Requirements Analysis)

| File | Type | Description |
|---|---|---|
| `AnalizaCerintelorPRESSYNC.md` | .md | Requirements analysis (with diacritics). Use-case format: name, description, actor, trigger, pre/post conditions, main flow, alternative flows. Covers: Creare Cont, Login, Gestionare Evenimente, Prezenta, Statistici, Restrictionare Acces. |
| `AnalizaCerintePresSync.md` | .md | Same content as above but without diacritics, simplified (fewer alternative flows). |
| `AnalizaCerintelorPRESSYNC(1).docx` | .docx | Word version of `AnalizaCerintelorPRESSYNC.md`. |
| `AnalizaCerintelorPRESSYNC-1.pdf` | Export of the requirements analysis (with diacritics). |
| `AnalizaCerintePresSync.pdf` | Export of the simplified version (no diacritics). |

---

## `Cerinte/` (Requirements Document)

| File | Type | Description |
|---|---|---|
| `DocumentulCerintelor.md` | .md | Formal requirements document. Defines system, actors (Super Admin, Admin, User), and functional requirements (F1: Registration & Auth, F2: Event Management, F3: Attendance Marking, F4: Statistics). |
| `DocumentulCerintelor.pdf` | Export of the formal requirements document. |

---

## `ModelulFunctional/` (Functional Model)

| File | Type | Description |
|---|---|---|
| `Modelul Functional WIP.md` | .md | Functional model description. Covers: (1) system overview (CQRS, JWT, schedulers), (2) actors (Admin/User), (3) event creation flow, (4) attendance marking flow, (5) statistics generation. |
| `Modelul Functional WIP.docx` | .docx | Word version of the functional model. |
| `Modelul Functional WIP.pdf` | Export of the functional model. |

---

## `3_AnalizaSoftwareASistemului_CAP3/` (Chapter 3 — Software System Analysis)

| File | Type | Description |
|---|---|---|
| `AnalizaEPrezenta.asta` | .asta (ZIP) | Same UML model as root — class/sequence diagrams for software analysis. |
| `Modelul Functional WIP.md` | .md | Duplicate of `ModelulFunctional/Modelul Functional WIP.md`. |
| `Modelul Functional WIP.docx` | .docx | Duplicate Word version. |
| `Modelul Functional WIP.pdf` | Duplicate PDF export. |

---

## `DefinireaProblemei/` (Problem Definition — WIP source sections)

### `DescriereaProblemei/`

| File | Type | Description |
|---|---|---|
| `DescriereaProblemei.md` | .md | Problem description (with diacritics). Topics: traditional attendance problems (paper lists, buddy punching), applicability (education/corporate/conferences), limits of digital solutions (GPS spoofing, internet dependency, privacy), proposed solution (local OpenWRT router). |
| `Descrierea problemei WIP.md` | .md | Same content, without diacritics, slightly different wording. |
| `Descrierea problemei WIP.docx` | .docx | Word version. |
| `Descrierea problemei WIP.pdf` | PDF export. |

### `ObiectiveleSistemului/`

| File | Type | Description |
|---|---|---|
| `Obiectivele Sistemului - Revizuit.md` | .md | Revised system objectives (with diacritics). Goals: autonomous OpenWRT operation, calendar automation via schedulers, JWT access control, CQRS for concurrency, real-time statistics. |
| `Obiectivele Sistemului WIP.md` | .md | WIP version (no diacritics), slightly informal wording. |
| `Obiectivele Sistemului WIP.docx` | .docx | Word version. |
| `Obiectivele Sistemului WIP.pdf` | PDF export. |

### `ScenariiDeBaza/`

| File | Type | Description |
|---|---|---|
| `Scenarii De Baza Rafinate.md` | .md | Refined scenarios (with diacritics, 91 lines): (1) Auth & login/JWT flow, (2) Configuring recurring events, (3) Secure attendance via local network (captive portal, IP verification, time windows). Includes main flow + alternative flows. |
| `Scenarii De Baza WIP.md` | .md | WIP version (no diacritics, 85 lines), less detailed (no captive portal redirect step). |
| `Scenarii De Baza WIP.docx` | .docx | Word version. |
| `Scenarii De Baza WIP.pdf` | PDF export. |

### `AplicatiiAsemanatoare/`

| File | Type | Description |
|---|---|---|
| `Aplicatii asemanatoare - comparatii WIP.md` | .md | Competitive analysis (no diacritics, 44 lines). Compares Pressync vs (1) RFID/magnetic cards, (2) QR code scanning, (3) GPS-based cloud tracking. Includes comparison table with cost/fraud risk/versatility/internet dependency criteria. |
| `State_of_the_Art_Aplicatii_Asemanatoare_Refacut.md` | .md | Refined academic version (with diacritics, 91 lines). Same 3 categories with detailed pros/cons + comparative synthesis table. Section numbering (3.1–3.4). |
| `Aplicatii asemanatoare - comparatii WIP.docx` | .docx | Word version. |
| `Aplicatii asemanatoare - comparatii WIP.pdf` | PDF export. |

---

## `DefinireaProblemei_CAP2/` (Chapter 2 — compiled version)

Contains the full compiled chapter plus **identical copies** of all files from `DefinireaProblemei/` sections above.

| File | Type | Description |
|---|---|---|
| `Capitolul2_DefinireaProblemei.md` | .md | **Full compiled Chapter 2** (with diacritics, 229 lines). Sections: 2.1 Introduction, 2.2 Existing solutions (RFID/QR/GPS + comparative synthesis), 2.3 Proposed Pressync architecture, 2.4 System objectives, 2.5 System actors, 2.6 Basic usage scenarios. |

---

## `2_DefinireaProblemei_CAP2/` (Chapter 2 — updated version)

Contains the updated compiled chapter plus **identical copies** of all files from `DefinireaProblemei/` sections above.

| File | Type | Description |
|---|---|---|
| `Capitolul2_DefinireaProblemei_upd.md` | .md | **Updated Chapter 2** (with diacritics, 268 lines). Revised structure: 2.1 Context si Motivatie, 2.2 Analiza solutiilor existente, 2.3 Solutia propusa (arhitectura PresSync), 2.4 Obiectivele sistemului, 2.5 Actorii sistemului, 2.6 Scenarii de baza. Includes bibliographic references (GDPR, QR dynamic). More polished than the previous version. |

---

## Duplicate Summary

| Source Dir | Duplicated In | Files |
|---|---|---|
| Root | `.misc/` | `Codebase.md`, `ThePressync Secret SauceNote`, `pressyncdb_1.png`, `documentation.zip`, `documentation1.zip` |
| `ModelulFunctional/` | `3_AnalizaSoftwareASistemului_CAP3/` | `Modelul Functional WIP.md`, `.docx`, `.pdf` |
| `DefinireaProblemei/*` | `DefinireaProblemei_CAP2/*` and `2_DefinireaProblemei_CAP2/*` | All section files (Descriere, Obiective, Scenarii, Aplicatii) |
