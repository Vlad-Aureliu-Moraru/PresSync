# PRESSYNC Documentation — File Summary

**Project:** Pressync — a local WiFi-based attendance tracking system using OpenWRT routers with captive portal authentication.

**Language:** Romanian (with diacritics in refined versions, without in WIP versions)

---

## Structure

```
documentation/
├── src/                           # Source files
│   ├── cap1-definirea-problemei/  # Chapter 2 — Problem Definition
│   │   ├── 01-descrierea-problemei/
│   │   ├── 02-obiectivele-sistemului/
│   │   ├── 03-scenarii-de-baza/
│   │   ├── 04-aplicatii-asemanatoare/
│   │   ├── Capitolul2_DefinireaProblemei.md        — compiled chapter (v1)
│   │   └── Capitolul2_DefinireaProblemei_upd.md    — compiled chapter (updated)
│   ├── cap2-analiza-software/     # Chapter 3 — Software System Analysis
│   │   └── modelul-functional/
│   ├── analiza-cerintelor/        # Requirements analysis
│   ├── cerinte/                   # Formal requirements document
│   ├── docs-tehnice/              # Technical docs (codebase, schema, notes)
│   ├── PRESSYNC_Documentation_Summary.md
│   └── PresSync-documentation-roadmap.md
├── assets/
│   ├── uml/                       # UML model files (.asta)
│   └── images/                    # Diagrams and images (.png)
├── archive/                       # ZIP archives (backups, templates)
├── export/                        # Placeholder for generated exports
└── pentru-profesor/
    └── surse/ → documentation_for_teacher/
```

---

## `src/` — Source Files

### `cap1-definirea-problemei/` (Chapter 2 — Problem Definition)

**Compiled chapters:**

| File | Description |
|------|-------------|
| `Capitolul2_DefinireaProblemei.md` | Full compiled Chapter 2 (229 lines). Sections: 2.1 Introduction, 2.2 Existing solutions, 2.3 Proposed architecture, 2.4 Objectives, 2.5 Actors, 2.6 Scenarios. |
| `Capitolul2_DefinireaProblemei_upd.md` | Updated Chapter 2 (268 lines). Restructured: Context & Motivation, Analysis, Proposed Solution, Objectives, Actors, Scenarios. More polished, includes bibliographic references. |

**Individual sections:**

#### `01-descrierea-problemei/`

| File | Description |
|------|-------------|
| `DescriereaProblemei.md` | Problem description (with diacritics). Topics: traditional attendance problems, digital limits, proposed OpenWRT-based solution. |
| `Descrierea problemei WIP.md` | Same content, without diacritics. |
| `Descrierea problemei WIP.docx` | Word version. |
| `Descrierea problemei WIP.pdf` | PDF export. |

#### `02-obiectivele-sistemului/`

| File | Description |
|------|-------------|
| `Obiectivele Sistemului - Revizuit.md` | Revised objectives (with diacritics): autonomous operation, calendar automation, JWT, CQRS, real-time stats. |
| `Obiectivele Sistemului WIP.md` | WIP version (no diacritics). |
| `Obiectivele Sistemului WIP.docx` | Word version. |
| `Obiectivele Sistemului WIP.pdf` | PDF export. |

#### `03-scenarii-de-baza/`

| File | Description |
|------|-------------|
| `Scenarii De Baza Rafinate.md` | Refined scenarios (with diacritics): auth/JWT, recurring events, captive portal attendance, stats. |
| `Scenarii De Baza WIP.md` | WIP version (no diacritics). |
| `Scenarii De Baza WIP.docx` | Word version. |
| `Scenarii De Baza WIP.pdf` | PDF export. |

#### `04-aplicatii-asemanatoare/`

| File | Description |
|------|-------------|
| `Aplicatii asemanatoare - comparatii WIP.md` | Competitive analysis: Pressync vs RFID, QR, GPS. Comparison table. |
| `State_of_the_Art_Aplicatii_Asemanatoare_Refacut.md` | Academic version (with diacritics, 91 lines). Detailed pros/cons, numbered sections. |
| `Aplicatii asemanatoare - comparatii WIP.docx` | Word version. |
| `Aplicatii asemanatoare - comparatii WIP.pdf` | PDF export. |

---

### `cap2-analiza-software/` (Chapter 3 — Software System Analysis)

#### `modelul-functional/`

| File | Description |
|------|-------------|
| `Modelul Functional WIP.md` | Functional model: CQRS pattern, JWT auth, event generation schedulers, actor flows. |
| `Modelul Functional WIP.docx` | Word version. |
| `Modelul Functional WIP.pdf` | PDF export. |

---

### `analiza-cerintelor/` (Requirements Analysis)

| File | Description |
|------|-------------|
| `AnalizaCerintelorPRESSYNC.md` | Use-case analysis (with diacritics). Covers: accounts, login, events, attendance, stats, access control. |
| `AnalizaCerintePresSync.md` | Same, without diacritics (simplified). |
| `AnalizaCerintePRESSYNC_upd.md` | Updated version (22K). |
| `AnalizaCerintelorPRESSYNC(1).docx` | Word version. |
| `AnalizaCerintelorPRESSYNC-1.pdf` | PDF export (with diacritics). |
| `AnalizaCerintePresSync.pdf` | PDF export (simplified). |

---

### `cerinte/` (Formal Requirements Document)

| File | Description |
|------|-------------|
| `DocumentulCerintelor.md` | Formal requirements: actors (Super Admin, Admin, User), functional regs (F1–F4). |
| `DocumentulCerintelor.pdf` | PDF export. |

---

### `docs-tehnice/` (Technical Documentation)

| File | Description |
|------|-------------|
| `Codebase.md` | Codebase docs for Attendance Module: CQRS, Java listings (controller, handlers, repos). ~447 lines. |
| `database-schema.md` | DB schema with Mermaid ERD. 5 tables: User, CategorieEveniment, EventCategoryConfig, Eveniment, Prezenta. |
| `ThePressync Secret SauceNote` | Architecture core idea: OpenWRT + captive portal on Raspberry Pi, no internet required. |

---

## `assets/` — Resources

| Path | Description |
|------|-------------|
| `uml/AnalizaEPrezenta.asta` | Astah/UML model (class & sequence diagrams). |
| `images/pressyncdb_1.png` | Database ER diagram. |

---

## `archive/` — Archives

| File | Description |
|------|-------------|
| `documentation.zip` | Snapshot of core documentation (AnalizaCerinte, Cerinte, UML, ERD). |
| `documentation1.zip` | Extended snapshot (includes DefinireaProblemei dirs, ModelulFunctional). |
| `model-lucrare-2026.zip` | LaTeX thesis template for Ovidius University. |
