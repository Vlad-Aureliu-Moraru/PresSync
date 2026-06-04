**Nume** : Creare Cont

**Descriere** : Descrie comportamentul sistemului si interactiunea dintre utilizator

si sistem pentru crearea unui cont nou

**Actor**: utilizator (main)

**Eveniment Declansator** : utilizator cere crearea unui cont nou

**Preconditii** : Sistemul trebuie sa functioneze corect

**Postconditii** : Sistemul a memorat datele contului creat si afisat profilul utilizatorului

**Referinte incrucisate:**

**Flux principal:**

| Utilizator | Sistem |
| :---- | :---- |
|  1.Cere crearea unui cont | 2.Afiseaza un formular |
| 3.Completeaza datele    |  4.Verifica datele primite\[A1\] |
|  | 5.Creaza cont nou , pastrand parola hashuita in baza de date |
|  | 6.Memoreaza datele contului creat  |

Fluxuri alternative:

\[A1\] : Datele sunt introduse eronat

1.Sistemul Afiseaza un mesaj de eroare ,

2.Continua fluxul de la pasul 2 din fluxul principal

---

**Nume** : Login

**Descriere** : Descrie comportamentul sistemului si interactiunea dintre utilizator

si sistem pentru  logarea unui utilizator

**Actor**: utilizator (main)

**Eveniment Declansator** : utilizator cere logarea 

**Preconditii** : Sistemul trebuie sa aiba cel putin un cont in DB

**Postconditii** : 

**Referinte incrucisate:**

Flux principal:

| UTILIZATOR              | SISTEM |
| :---- | :---- |
| 1.Cere logarea           | 2.Afiseaza un formular |
| 3.Completeaza datele | 4.Verifica datele primite\[A1\] |
|  | 5.Afiseaza profilul utilizatorului |

Fluxuri alternative:

\[A1\] : Datele sunt introduse eronat

1.Sistemul Afiseaza un mesaj de eroare ,

2.Continua fluxul de la pasul 2 din fluxul principal

---

**Nume** : Vizualizare prezenta cu statistica

**Descriere** : Utilizatorul vizualizeaza datele legate de statistica proprie a prezentei si primeste o predictie 

**Actor**: utilizator (main)

**Eveniment Declansator** : Utilizatorul a inregistrat deja prezenta/Dupa logare nu exista evenimente active

**Preconditii** : prezente \> 0 

**Postconditii** : 

**Referinte incrucisate**:

Flux principal:

| UTILIZATOR | SISTEM |
| :---- | :---- |
|   1.Utilizatorul s-a logat| | 2.Verifica daca exista evenimente active\[A1\] |
|  | 3.Afiseaza statistica prezentelor utilizatorului |

  Fluxuri alternative:

\[A1\] : Exista eveniment activ

1\. Sistemul trimite Utilizatorul catre pagina de Inregistrare a prezentei

2\. Sistemul continua de la pasul 3 al fluxului principal

---

**Nume** : Inregistrare prezenta

**Descriere** : Utilizatorul inregistreaza prezenta la un eveniment activ

**Actor**: utilizator (main)

**Eveniment Declansator** : utilizatorului cere inregistrarea

**Preconditii** : Exista eveniment activ , Utilizatorul este logat

**Postconditii** : Sistemul a memorat prezenta 

**Referinte incrucisate:**

Flux principal:

| UTILIZATOR | SISTEM |
| :---- | :---- |
|   1.Utilizatorul s-a logat| | 2.Verifica daca exista evenimente active\[A1\] |
|  | 3.Sistemul salveaza prezenta la evenimentul activ |

Fluxuri alternative:

\[A1\] : Nu exista eveniment activ/ Exista eveniment activ dar restrictionat pentru utilizator

1\. Sistemul Afiseaza statistica prezentelor inregistrate 

---

**Nume** : Administreaza User

**Descriere** : 

**Actor**: Admin (main) 

**Eveniment Declansator** : Admin selectează secțiunea „User Management”.

**Preconditii** : Admin este autentificat.

**Postconditii** : Modificările utilizatorilor sunt salvate în sistem.

**Referinte incrucisate:**

**Flux principal**:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Accesează secțiunea „User Management” | 2\. Afișează lista de utilizatori |
| 3\. Selectează acțiunea (permite/ restrictioneaza) | 4\. Execută acțiunea aleasă (A1, A2) |
|  | 5\. Salvează modificările |
|  | 6\. Afișează lista actualizată |

Fluxuri alternative:

**A1 – Permite Acces utilizator**

1. Sistemul adauga utilizatorul in lista utilizatorilor care au acces la cursurile create de adminul ce a initiat actiunea.

2. Sistemul verifică datele (erori → revine la formular).

3. Sistemul salveaza.

**A2 – Restrictioneaza acces utilizator**

1. Sistemul sterge utilizatorul din lista utilizatorilor care au acces la cursurile create de adminul ce a initiat actiunea.

2. Sistemul salvează modificările.

---

    **Nume** :  Administreaza prezenta manual

    **Descriere** : Admin poate adăuga, modifica sau șterge prezența unui utilizator la un eveniment.

    **Actor**: Admin (main) 

    **Eveniment Declansator** : 

    **Preconditii** : Evenimentul există, utilizatorul există.

    **Postconditii** : Prezențele sunt actualizate.

    **Referinte incrucisate**:

    **Flux principal**:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează lista de utilizatori și prezențele lor |
| 3\. Alege acțiunea (adaugă/ editează/ șterge) | 4\. Execută acțiunea |
|  | 5\. Salvează modificarea |
|  | 6\. Afișează lista actualizată |

    Fluxuri alternative:

---

    Nume : Restrictioneaza Acces User

    Descriere : 

    Actor: Admin (main) 

    Eveniment Declansator : 

    Preconditii : 

    Postconditii : 

    Referinte incrucisate:

    Flux principal:

| UTILIZATOR | SISTEM |
| :---- | :---- |
|  |  |

    Fluxuri alternative:

---

Nume : Permite Acces User

Descriere : 

Actor: Admin (main) 

Eveniment Declansator : 

Preconditii : 

Postconditii : 

Referinte incrucisate:

Flux principal:

| UTILIZATOR | SISTEM |
| :---- | :---- |
|  |  |

Fluxuri alternative:

---

**Nume** : Vizualizare Stare utilizator

**Descriere** : Admin vizualizează informații despre prezența unui utilizator.

**Actor**: Admin (main) 

**Eveniment Declansator** : 

**Preconditii** : Utilizatorul există.

**Postconditii** : Admin vede informațiile.

**Referinte incrucisate**:

**Flux principal**:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează utilizatorul | 2\. Afișează datele de prezență, grafice, statistici |

Fluxuri alternative:

---

**Nume** : Cautare utilizator

**Descriere** : Admin caută un utilizator după criterii (nume, grupă, an, email).

**Actor**: Admin (main) 

**Eveniment Declansator** : 

**Preconditii** : Există utilizatori în sistem.

**Postconditii** : Sunt afișate rezultatele.

**Referinte incrucisate**:

**Flux principal**:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Introduce criteriul de căutare | 2\. Procesează cererea |
|  | 3\. Afișează rezultatele |

Fluxuri alternative:

---

**Nume** : Administrare evenimente

**Descriere** : Admin poate crea, edita, șterge sau arhiva evenimente.

**Actor**: Admin (main) 

**Eveniment Declansator** : 

**Preconditii** : Admin autentificat.

**Postconditii** : Evenimentele sunt actualizate.

**Referinte incrucisate:**

**Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Accesează pagina de evenimente | 2\. Afișează lista de evenimente |
| 3\. Selectează acțiunea (creare/editare/ștergere/arhivare) | 4\. Execută acțiunea respectivă |

Fluxuri alternative:

---

    **Nume** : Creare categorii

    **Descriere** : Admin poate crea o categorie de evenimente (ex: Curs, Seminar, Laborator).

    **Actor**: Admin (main) 

    **Eveniment Declansator** : 

    **Preconditii** : Admin autentificat.

    **Postconditii** : Categoria este salvată.

    **Referinte incrucisate:**

    **Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează „Creare categorie” | 2\. Afișează formularul |
| 2\. Completează datele | 3\. Verifică datele (A1) |
|  | 4\. Salvează categoria |

    Fluxuri alternative:

---

    Nume : Creare evenimente

    Descriere : Admin creează un eveniment unic sau repetitiv.

    Actor: Admin (main) 

    Eveniment Declansator : 

    Preconditii : Există cel puțin o categorie.

    Postconditii : Evenimentul este înregistrat.

    Referinte incrucisate:

    Flux principal:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează „Creare eveniment” | 2\. Afișează formularul |
| 2\. Completează datele (ora, durată, categorie, repetitivitate) | 3\. Validează datele |
|  | 4\. Salvează evenimentul |

    Fluxuri alternative:

---

    **Nume** : Editare evenimente

    **Descriere** : Admin poate modifica datele unui eveniment.

    **Actor**: Admin (main) 

    **Eveniment Declansator :** 

    **Preconditii** : Evenimentul există.

    **Postconditii** : Evenimentul este actualizat.

    **Referinte incrucisate:**

    **Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează formularul de editare |
| 2\. Editează datele | 3\. Validează |
|  | 4\. Salvează modificările |

    Fluxuri alternative:

---

    **Nume** : Editare categorii

    **Descriere** : Admin modifică o categorie existentă.

    **Actor**: Admin (main) 

    **Eveniment Declansator** : 

    **Preconditii** : Categoria există.

    **Postconditii** : Categoria este actualizată.

    **Referinte incrucisate:**

    **Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează categoria | 2\. Afișează formularul |
| 2\. Modifică numele / setările | 3\. Validează și salvează |

    Fluxuri alternative:

---

    **Nume** : Arhivare eveniment

    **Descriere** : Admin arhivează un eveniment încheiat pentru a nu-l mai afișa în listă.

    **Actor**: Admin (main) 

    **Eveniment Declansator** : 

    **Preconditii** : Evenimentul este trecut.

    **Postconditii** : Evenimentul devine arhivat.

    **Referinte incrucisate:**

    **Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează butonul „Arhivează” |
| 2\. Confirmă arhivarea | 3\. Mută evenimentul în arhivă |

    Fluxuri alternative:

---

**Nume** : Administrare evenimentelor repetitive

**Descriere** : Sistemul generează automat evenimente pe baza regulilor de repetitivitate.

**Actor**: Timp (main)

**Eveniment Declansator** : Ora programată

**Preconditii** : Există evenimente repetitive definite.

**Postconditii** : Se creează evenimentele următoare.

**Referinte incrucisate:**

**Flux principal:**

| SISTEM | SISTEM |
| :---- | :---- |
| 1\. Verifică evenimentele repetitive | 2\. Generează instanța nouă |
|  | 3\. Salvează evenimentul |

Fluxuri alternative:

