**Nume** : Creare Cont

**Descriere** : Descrie comportamentul sistemului si interactiunea dintre utilizator

si sistem pentru crearea unui cont nou

**Actor**: Utilizator (main)

**Eveniment Declansator** : utilizator cere crearea unui cont nou

**Preconditii** : Sistemul trebuie sa functioneze corect

**Postconditii** : Sistemul a memorat datele contului creat si afisat profilul utilizatorului

**Referinte incrucisate:** 

**Flux principal:**

| Utilizator | Sistem |
| :---- | :---- |
|  1.Cere crearea unui cont | 2.Afiseaza un formular |
| 3.Completeaza datele    |  4.Verifica datele primite sintactic si semantic\[A1\] \[A2\] |
|  | 5.Creaza cont nou, pastrand parola in baza de date |
|  | 6.Memoreaza datele contului creat |
|  | 7\. Afiseaza un mesaj de terminare cu succes a procesului |

**Fluxuri alternative:**

**\[A1\] : Datele sunt introduse eronat**

1. Sistemul afisează un mesaj de eroare si marchează câmpurile greșite.  
2. Continuă fluxul de la pasul 2 din fluxul principal

**\[A2\] Utilizatorul există deja (Eroare Semantică):**

1. Sistemul verifică baza de date și detectează că Username-ul sau Email-ul este deja utilizat.  
2. Sistemul afișează eroarea: "Contul există deja".  
3. Fluxul revine la pasul 2 (formular).

---

**Nume** : Login

**Descriere** : Descrie comportamentul sistemului si interactiunea dintre utilizator

si sistem pentru  logarea unui utilizator

**Actor**: Utilizator (main)

**Eveniment Declansator** : utilizator cere logarea 

**Preconditii** : Sistemul trebuie sa aiba cel putin un cont in baza de date

**Postconditii** :Sistemul redirecționează utilizatorul fie către confirmarea prezenței (dacă există eveniment activ), fie către pagina de statistici.

**Referinte incrucisate:**

**Flux principal:**

| UTILIZATOR              | SISTEM |
| :---- | :---- |
| 1.Cere logarea           | 2.Afiseaza un formular de autentificare |
| 3.Completeaza datele | 4.Verifica datele primite (sintactic si semantic) \[A1\] |
|  | 5.Identifica rolul utilizatorului |
|  | 6\. Verifica existenta unui eveniment activ     \[A2\] |
|  | 7.Afiseaza dashboard-ul cu statistici |

**Fluxuri alternative:**

**\[A1\]  Datele sunt introduse eronat**

1. Sistemul afișează un mesaj de eroare și marchează câmpurile greșite.  
2. Sistemul golește câmpul de parolă și permite reintroducerea datelor

**\[A2\] : Exista eveniment activ si nerestrictionat**

1. Inițiază automat cazul de utilizare **"***Înregistrare Prezență***"**.

---

**Nume** : Vizualizare prezenta cu statistica

**Descriere** : Utilizatorul vizualizeaza datele legate de statistica proprie a prezentei si primeste o predictie 

**Actor**: utilizator (main)

**Eveniment Declansator** : Actorul selectează opțiunea "Statistici" sau "Rapoarte" din meniul principal.

**Preconditii** : ctorul este autentificat; Există date de prezență înregistrate în baza de date.

**Postconditii** : Sistemul afișează datele solicitate sub formă de tabel și/sau grafice

**Referinte incrucisate**:

**Flux principal (Admin):**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează opțiunea "Rapoarte și Statistici" | 2\. Afișează Dashboard-ul cu statistici agregate (Ex: total evenimente, prezență medie) |
| 3\. Cere aplicarea unui filtru (Ex: Curs/Eveniment, Interval de timp, Grupă) | 4\. Validează criteriile de filtrare \[A1\] |
|  | 5\. Afișează lista de rezultate (Ex: Toți utilizatorii cu Procentul de Prezență) |
| 6\. Selectează un utilizator sau un eveniment pentru vizualizare detaliată | 7\. Generează și afișează **Raportul Detaliat** (istoric prezențe, prezențe/absențe) |
| 8\. Selectează opțiunea "Descarcă Raport" | 9\. Generează fișierul (PDF/CSV) \[A2\] |
|  | 10\. Afișează mesaj de succes. Fluxul se termină. |


**Flux Principal (Utilizator):**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează opțiunea "Statistici" | 2\. Aplică automat filtrul pe propriul ID de utilizator |
|  | 3\. Generează și afișează **Istoricul de Prezență** (Tabel evenimente cu coloane: Data, Titlu, Status: Prezent/Absent) |
| 4\. Selectează un eveniment specific | 5\. Afișează detaliile evenimentului |

**Fluxuri Alternative:**

**\[A1\] Criterii de filtrare invalide:**

1. Adminul introduce o dată de sfârșit anterioară datei de început.  
   2. Sistemul afișează mesaj de eroare și marchează câmpurile greșite.  
   3. Fluxul revine la pasul 3\.

**\[A2\] Eșec la generarea raportului (Admin):**

1. Sistemul întâmpină o eroare tehnică (Ex: Lipsă memorie sau probleme cu motorul de raportare).  
2. Sistemul afișează mesajul "Eroare la generarea fișierului. Vă rugăm reîncercați."

**\[A3\] Nu există date (Utilizator/Admin):**

1. Sistemul nu găsește nicio prezență sau eveniment conform filtrului.  
2. Sistemul afișează mesajul "Nu au fost găsite date de prezență înregistrate."

---

**Nume** : Inregistrare prezenta

**Descriere** : Utilizatorul inregistreaza prezenta la un eveniment activ

**Actor**: utilizator (main)

**Eveniment Declansator** : Utilizatorul accesează funcția de prezență sau se loghează în timpul unui eveniment.

**Preconditii** : Exista eveniment activ la care nu are deja prezenta si nu este in lista utilizatorilor restrictionati , utilizatorul este logat

**Postconditii** : Sistemul a memorat prezenta 

**Referinte incrucisate:**

**Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1, Cere inregistrarea prezentei | 2.Verifica daca exista evenimente active si daca utilizatorul are drept de acces \[A1\] |
|  | 3\. Afiseaza detaliile evenimentului curent |
| 4\. Confirma prezenta \[A2\] | 5\. Salveaza prezenta la evenimentul activ |
|  | 6.Afiseaza mesaj de inregistrare completa |

**Fluxuri alternative:**

**\[A1\]  Nu exista eveniment activ la care utilizatorul nu are prezenta/ Exista eveniment activ dar restrictionat pentru utilizator**

1. Sistemul afișează statistica prezentelor înregistrate   
2. Fluxul se termina

**\[A2\] Utilizatorul refuză/amână confirmarea:**

1. Utilizatorul apasă "Nu acum" sau "Anulează".  
2. Sistemul redirectioneaza utilizatorul către ecranul principal (Statistici).

---

**Nume** : Administreaza User

**Descriere** : Adminul modifică drepturile de acces ale utilizatorilor la cursurile sale.

**Actor**: Admin (main) 

**Eveniment Declansator** : Admin selectează secțiunea „User Management”.

**Preconditii** : Admin este autentificat.

**Postconditii** : Modificările utilizatorilor sunt salvate în sistem.

**Referinte incrucisate:**

**Flux principal**:

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Accesează secțiunea „User Management” | 2\. Afișează lista de utilizatori |
| 3\. Selecteaza un utilizator | 4\. Afiseaza detaliile si starea curenta (Permis/Restrictionat) |
| 5.Selecteaza actiunea opusa starii curente | 6\. Cere confirmare |
| 7\. Confirma actiunea \[A1\] | 5\. Salvează modificările |
|  | 6\. Afișează mesaj de succes |

**Fluxuri alternative:**

**Flux Alternativ \[A1\] \- Admin renunță:**

1. Admin apasă "Anulează".  
2. Fluxul revine la pasul 2\.

---
**Nume** :  Administreaza prezenta manual
**Descriere** : Adminul poate modifica manual prezența unui student (ex: dacă a uitat să se marcheze).
**Actor**: Admin (main) 
**Eveniment Declansator** : Adminul selectează un eveniment trecut/curent.
**Preconditii** : Evenimentul există, utilizatorul există.
**Postconditii** : Prezențele sunt actualizate.
**Referinte incrucisate**:
**Flux principal**:

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează lista de participanti si statusul lor |
| 3\. Selecteaza un utilizator si modifica starea | 4\. Valideaza actiunea |
|  | 5\. Salvează modificarea |
|  | 6\. Recalculează statisticile pentru acel utilizator |
|  | 7\. Afișează lista actualizată |

    **Fluxuri alternative:**

---

**Nume** : Cautare utilizator

**Descriere** : Admin caută un utilizator după criterii (nume, grupă, an, email).

**Actor**: Admin (main) 

**Eveniment Declansator** : 

**Preconditii** : Există utilizatori în sistem.

**Postconditii** : Sunt afișate rezultatele.

**Referinte incrucisate**:

**Flux principal**:

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Introduce criteriul de căutare | 2\. Procesează cererea |
|  | 3\. Afișează rezultatele \[A1\] |

**Fluxuri alternative:**

**\[A1\] Niciun rezultat găsit:**

1. Sistemul afișează mesajul "Nu a fost găsit niciun utilizator conform criteriilor".  
2. Sistemul permite introducerea unor noi criterii (revine la pasul 1).

---

**Nume** : Administrare evenimente

**Descriere** : Admin poate crea, edita, șterge sau arhiva evenimente.

**Actor**: Admin (main) 

**Eveniment Declansator** : 

**Preconditii** : Admin autentificat.

**Postconditii** : Evenimentele sunt actualizate.

**Referinte incrucisate:**

**Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Accesează pagina de evenimente | 2\. Afișează lista de evenimente |
| 3\. Selectează acțiunea (creare/editare/ștergere/arhivare) | 4\. Execută cazul de ut asociat optiunii alese |

**Fluxuri alternative:**

---

    **Nume** : Creare categorii de evenimente

    **Descriere** : Admin poate crea o categorie de evenimente (ex: Curs, Seminar, Laborator).

    **Actor**: Admin (main) 

    **Eveniment Declansator** : Administratorul selecteaza creare categorii de evinemte

    **Preconditii** : Admin autentificat.

    **Postconditii** : Categoria este salvată.

    **Referinte incrucisate:**

    **Flux principal:**

| ADMINISTRATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează „Creare categorie” | 2\. Afișează formularul (inclusiv modul de logare ) |
| 2\. Completează datele | 3\. Verifică datele \[A1\] |
|  | 4\. Salvează categoria in baza de date |
|  | 5\. Afiseaza un mesaj corespunzator |

    **Fluxuri alternative:**

---

    **Nume** : Creare evenimente

    **Descriere** : Admin creează un eveniment unic sau repetitiv.

    **Actor**: Admin (main) 

    **Eveniment Declansator** : Adminul selecteaza optiunea “Creare Eveniment”

    **Preconditii** : Există cel puțin o categorie si adminul este logat.

    **Postconditii** : Evenimentul este salvat si devine vizibil.

    **Referinte incrucisate:**

    **Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează „Creare eveniment” | 2\. Afișează formularul |
| 2\. Completează datele (ora, durată, categorie, repetitivitate) | 3\. Validează datele introduse semantic \[A1\] \[A2\] |
|  | 4 Salvează evenimentul în baza de date |
|  | 5\. Afișează mesaj de succes și lista actualizată |

**Fluxuri alternative:**

**\[A1\] Date invalide:**

1. Sistemul marchează câmpurile obligatorii necompletate.  
2. Fluxul revine la pasul 2\.

**\[A2\] Conflict de resurse (Eroare Semantică):**

1. Sistemul detectează că există deja un eveniment în aceeași sală la aceeași oră.  
2. Sistemul afișează un avertisment privind suprapunerea.  
3. Adminul poate alege să modifice datele (revine la pasul 2).

---

    **Nume** : Editare evenimente

    **Descriere** : Admin poate modifica datele unui eveniment.

    **Actor**: Admin (main) 

    **Eveniment Declansator :** 

    **Preconditii** : Evenimentul există.

    **Postconditii** : Evenimentul este actualizat.

    **Referinte incrucisate:**

    **Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează formularul de editare |
| 2\. Editează datele | 3\. Validează \[A1\] |
|  | 4\. Salvează modificările |

    Fluxuri alternative:

**\[A1\] Date invalide sau incomplete:**

1. Sistemul marchează câmpurile eronate și afișează un mesaj de eroare.  
2. Fluxul revine la pasul 2 al fluxului principal.

**\[A2\] Admin anulează editarea:**

1. Admin apasă butonul "Anulare".  
2. Sistemul renunță la modificări și reafisează detaliile evenimentului nemodificate.  
3. Fluxul se termină.

---

    **Nume** : Editare categorii

    **Descriere** : Admin modifică o categorie existentă.

    **Actor**: Admin (main) 

    **Eveniment Declansator** : 

    **Preconditii** : Categoria există.

    **Postconditii** : Categoria este actualizată.

    **Referinte incrucisate:**

    **Flux principal:**

| ADMIN | SISTEM |
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

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează butonul „Arhivează”  |
| 2\. Confirmă arhivarea \[A1\] | 3\. Seteaza starea evenimentului ca arhivat in baza de date |

    Fluxuri alternative:

**\[A1\] Anulare arhivare:**

1. La pasul de confirmare, Admin selectează "Nu" / "Anulează".  
2. Sistemul închide fereastra de confirmare fără a schimba starea evenimentului.  
3. Fluxul se termină.

---

    **Nume** : Administrare evenimentelor repetitive

    **Descriere** : Sistemul generează automat instanțe noi pentru evenimentele configurate ca repetitive.

    **Actor**:Sistem / Timp (Main)

    **Eveniment Declansator** : Ora programată (Trigger temporal).

    **Preconditii** : Există definiții de evenimente repetitive.

    **Postconditii** : O nouă instanță a evenimentului este creată.

    **Referinte incrucisate:**

    **Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Verifică periodic evenimentele repetitive  | 2\. Identifică evenimentele care necesită o nouă instanță |
| 3\. Generează eveniment nou (copiază detalii, setează data nouă) | 4\. Salvează noul eveniment în baza de date |
|  | 5\. Evenimentul devine activ pentru prezență la ora stabilită |

    **Fluxuri alternative:**

