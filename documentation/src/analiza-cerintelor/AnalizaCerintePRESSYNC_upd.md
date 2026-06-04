**Nume** : Creare Cont

**Descriere** : Procesul complet de înregistrare a unui utilizator nou în sistem. Utilizatorul furnizează datele personale, iar sistemul creează contul, hash-uiește parola și declanșează verificarea prin email (OTP) înainte ca acesta să poată fi utilizat.

**Actor**: Utilizator (main)

**Eveniment Declansator** : Utilizatorul accesează pagina de înregistrare și trimite formularul cu datele personale.

**Preconditii** : Sistemul este operațional. Adresa de email nu este deja asociată unui cont existent.

**Postconditii** : Contul este creat în baza de date (stare inactivă până la verificare OTP), parola este stocată securizat (BCrypt), iar un cod OTP este trimis pe email pentru activare.

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

**Descriere** : Autentificarea unui utilizator existent în sistem. După validarea credențialelor, utilizatorul este redirecționat fie către înregistrarea prezenței (dacă există eveniment activ), fie către dashboard-ul cu statistici. În funcție de rol, sistemul poate solicita verificare MFA suplimentară (OTP pe email).

**Actor**: Utilizator (main)

**Eveniment Declansator** : Utilizatorul accesează pagina de autentificare și trimite emailul și parola.

**Preconditii** : Contul utilizatorului există și este activat (a trecut de verificarea OTP la înregistrare).

**Postconditii** : Utilizatorul primește un token JWT (direct sau după verificare OTP) și este redirecționat către pagina corespunzătoare rolului și contextului (prezență activă sau statistici).

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

**Descriere** : Utilizatorul vizualizează istoricul propriu de prezență și primește statistici (inclusiv predicții). Adminul poate vizualiza statistici agregate, aplica filtre și genera rapoarte detaliate.

**Actor**: Utilizator / Admin (main)

**Eveniment Declansator** : Actorul selectează opțiunea "Statistici" sau "Rapoarte" din meniul principal.

**Preconditii** : Actorul este autentificat. Există date de prezență înregistrate în baza de date.

**Postconditii** : Sistemul afișează datele solicitate sub formă de tabel și/sau grafice.

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

**Descriere** : Procesul prin care un utilizator își înregistrează prezența la un eveniment activ. Sistemul verifică existența evenimentului activ, dreptul de acces al utilizatorului și confirmă prezența.

**Actor**: Utilizator (main)

**Eveniment Declansator** : Utilizatorul accesează funcția de prezență sau se loghează în timpul unui eveniment activ.

**Preconditii** : Există un eveniment activ la care utilizatorul nu are deja prezența înregistrată și nu este în lista utilizatorilor restricționați. Utilizatorul este autentificat.

**Postconditii** : Sistemul salvează prezența utilizatorului la evenimentul activ.

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

**Descriere** : Adminul gestionează permisiunile de acces ale utilizatorilor la cursurile/evenimentele sale, putând permite sau restricționa accesul acestora.

**Actor**: Admin (main) 

**Eveniment Declansator** : Admin selectează secțiunea „User Management”.

**Preconditii** : Admin este autentificat. Există utilizatori în sistem.

**Postconditii** : Starea de acces a utilizatorului selectat este actualizată (Permis/Restricționat).

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

**Descriere** : Adminul poate modifica manual prezența unui student la un eveniment (de exemplu, dacă a uitat să se marcheze în fereastra de prezență).

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează un eveniment trecut sau curent pentru administrare.

**Preconditii** : Evenimentul există. Utilizatorul țintă există. Adminul este autentificat.

**Postconditii** : Înregistrările de prezență sunt actualizate, iar statisticile pentru acel utilizator sunt recalculate.

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

**Descriere** : Adminul poate căuta utilizatori după criterii precum nume, prenume, email, grupă sau an, pentru a vizualiza sau gestiona mai rapid conturile.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul tastează un criteriu de căutare în câmpul dedicat din interfața de administrare.

**Preconditii** : Există utilizatori în sistem. Adminul este autentificat.

**Postconditii** : Sistemul afișează utilizatorii care corespund criteriilor introduse.

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

**Descriere** : Caz de utilizare umbrelă — Adminul poate alege dintre operațiile disponibile pentru gestionarea evenimentelor: creare, editare, ștergere sau arhivare.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul accesează pagina de administrare a evenimentelor.

**Preconditii** : Adminul este autentificat.

**Postconditii** : În funcție de acțiunea aleasă, evenimentele sau categoriile sunt actualizate.

**Referinte incrucisate:**

**Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Accesează pagina de evenimente | 2\. Afișează lista de evenimente |
| 3\. Selectează acțiunea (creare/editare/ștergere/arhivare) | 4\. Execută cazul de ut asociat optiunii alese |

**Fluxuri alternative:**

---

**Nume** : Creare categorii de evenimente

**Descriere** : Adminul definește o nouă categorie de evenimente (ex: Curs, Seminar, Laborator) specificând numele, intervalul orar, fereastra de prezență și regulile de recurență.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează opțiunea "Creare categorie" din pagina de administrare.

**Preconditii** : Adminul este autentificat.

**Postconditii** : Categoria este salvată în baza de date și devine disponibilă pentru crearea de evenimente.

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

**Descriere** : Adminul creează un eveniment (instanță a unei categorii existente) pentru o anumită dată, cu setări de activ/arhivat și opțiuni de repetitivitate.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează opțiunea "Creare Eveniment".

**Preconditii** : Există cel puțin o categorie de eveniment definită. Adminul este autentificat.

**Postconditii** : Evenimentul este salvat în baza de date și devine vizibil utilizatorilor.

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

**Descriere** : Adminul poate modifica datele unui eveniment existent, cum ar fi categoria asociată, data, ora sau starea (activ/arhivat).

**Actor**: Admin (main) 

**Eveniment Declansator :** Adminul selectează un eveniment și alege opțiunea de editare.

**Preconditii** : Evenimentul există. Adminul este autentificat.

**Postconditii** : Evenimentul este actualizat în baza de date.

**Referinte incrucisate:**

**Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează formularul de editare |
| 2\. Editează datele | 3\. Validează \[A1\] |
|  | 4\. Salvează modificările |

**Fluxuri alternative:**

**\[A1\] Date invalide sau incomplete:**

1. Sistemul marchează câmpurile eronate și afișează un mesaj de eroare.  
2. Fluxul revine la pasul 2 al fluxului principal.

**\[A2\] Admin anulează editarea:**

1. Admin apasă butonul "Anulare".  
2. Sistemul renunță la modificări și reafisează detaliile evenimentului nemodificate.  
3. Fluxul se termină.

---

**Nume** : Editare categorii

**Descriere** : Adminul modifică setările unei categorii de eveniment existente (nume, interval orar, fereastră de prezență, recurență).

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează o categorie și alege opțiunea de editare.

**Preconditii** : Categoria există. Adminul este autentificat.

**Postconditii** : Categoria este actualizată în baza de date.

**Referinte incrucisate:**

**Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează categoria | 2\. Afișează formularul |
| 2\. Modifică numele / setările | 3\. Validează și salvează |

**Fluxuri alternative:**

---

**Nume** : Arhivare eveniment

**Descriere** : Adminul arhivează un eveniment încheiat pentru a nu-l mai afișa în listele active. Evenimentul arhivat rămâne în baza de date cu `active=false`, `archived=true`.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează un eveniment și alege opțiunea "Arhivează".

**Preconditii** : Evenimentul este trecut (a avut loc deja). Adminul este autentificat.

**Postconditii** : Evenimentul devine arhivat (`active=false`, `archived=true`) și nu mai apare în lista de evenimente active.

**Referinte incrucisate:**

**Flux principal:**

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează evenimentul | 2\. Afișează butonul „Arhivează”  |
| 2\. Confirmă arhivarea \[A1\] | 3\. Seteaza starea evenimentului ca arhivat in baza de date |

**Fluxuri alternative:**

**\[A1\] Anulare arhivare:**

1. La pasul de confirmare, Admin selectează "Nu" / "Anulează".  
2. Sistemul închide fereastra de confirmare fără a schimba starea evenimentului.  
3. Fluxul se termină.

---

**Nume** : Administrare evenimentelor repetitive

**Descriere** : Sistemul generează automat instanțe noi de evenimente pe baza regulilor de recurență configurate la nivel de categorie (daily, weekly, biweekly, monthly, yearly), fără intervenția manuală a adminului.

**Actor**: Sistem / Timp (Main)

**Eveniment Declansator** : Trigger temporal — schedulerul intern rulează la fiecare minut (generare) și la miezul nopții (încărcare orar zilnic).

**Preconditii** : Există definiții de evenimente repetitive active în configurația categoriilor.

**Postconditii** : O nouă instanță a evenimentului este creată la ora de început și arhivată la ora de sfârșit.

**Referinte incrucisate:**

**Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Verifică periodic evenimentele repetitive  | 2\. Identifică evenimentele care necesită o nouă instanță |
| 3\. Generează eveniment nou (copiază detalii, setează data nouă) | 4\. Salvează noul eveniment în baza de date |
|  | 5\. Evenimentul devine activ pentru prezență la ora stabilită |

**Fluxuri alternative:**

---

## Cazuri adiționale

---

**Nume** : Verificare MFA / OTP

**Descriere** : Procesul de verificare în doi pași prin cod OTP (6 cifre) trimis pe email. Este obligatoriu după înregistrare și poate fi necesar la login pentru utilizatorii cu rol ADMIN sau MODERATOR.

**Actor**: Utilizator (main)

**Eveniment Declansator** : Sistemul solicită verificare MFA ca urmare a unui flux de înregistrare sau autentificare (când `requiresMfa=true` în răspuns).

**Preconditii** : Utilizatorul a inițiat un flux de înregistrare sau login care a returnat `requiresMfa=true`. Codul OTP a fost trimis pe email și nu a expirat (valabil 5 minute).

**Postconditii** : Contul este activat (dacă era inactiv), utilizatorul primește un token JWT și este redirecționat către dashboard.

**Referinte incrucisate:**

**Flux principal:**

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Este redirecționat la pagina de verificare OTP | 2\. Afișează adresa de email mascată și un câmp pentru cod |
| 3\. Introduce codul OTP primit pe email | 4\. Validează codul OTP \[A1\] |
|  | 5\. Activează contul (dacă era inactiv) |
|  | 6\. Returnează token JWT |
| 7\. Este redirecționat către dashboard | |

**Fluxuri alternative:**

**\[A1\] Cod OTP invalid sau expirat:**

1. Sistemul verifică codul și constată că nu corespunde sau a expirat (5 minute).  
2. Sistemul afișează mesajul "Cod OTP invalid sau expirat."  
3. Utilizatorul poate solicita un cod nou reluând fluxul de login/înregistrare.

---

**Nume** : Vizualizare Stare Utilizator

**Descriere** : Adminul sau Moderatorul vizualizează informații detaliate despre prezența unui utilizator specific, inclusiv istoric complet și statistici.

**Actor**: Admin / Moderator (main) 

**Eveniment Declansator** : Adminul selectează un utilizator din lista de utilizatori și alege opțiunea de vizualizare a prezenței.

**Preconditii** : Utilizatorul există. Adminul/Moderatorul este autentificat.

**Postconditii** : Sistemul afișează datele de prezență, grafice și statistici pentru utilizatorul selectat.

**Referinte incrucisate**:

**Flux principal**:

| ADMIN / MODERATOR | SISTEM |
| :---- | :---- |
| 1\. Selectează utilizatorul | 2\. Afișează datele de prezență, grafice, statistici |
| 3\. (Opțional) Navighează la alt utilizator | 4\. Reîncarcă datele pentru noul utilizator |

**Fluxuri alternative:**

**\[A1\] Utilizatorul nu există:**

1. Sistemul nu găsește utilizatorul cu ID-ul specificat.  
2. Sistemul afișează mesajul de eroare corespunzător.

---

**Nume** : Ștergere Utilizator

**Descriere** : Adminul poate șterge definitiv un cont de utilizator din sistem, inclusiv înregistrările de prezență asociate.

**Actor**: Admin (main) 

**Eveniment Declansator** : Adminul selectează un utilizator și alege opțiunea "Șterge cont".

**Preconditii** : Adminul este autentificat. Utilizatorul țintă există.

**Postconditii** : Contul utilizatorului și înregistrările de prezență asociate sunt șterse definitiv.

**Referinte incrucisate**:

**Flux principal**:

| ADMIN | SISTEM |
| :---- | :---- |
| 1\. Selectează utilizatorul și alege "Șterge cont" | 2\. Cere confirmare |
| 3\. Confirmă ștergerea \[A1\] | 4\. Verifică existența utilizatorului \[A2\] |
|  | 5\. Șterge înregistrările de prezență asociate |
|  | 6\. Șterge contul utilizatorului |
|  | 7\. Afișează mesaj de succes |

**Fluxuri alternative:**

**\[A1\] Admin anulează:**

1. Adminul apasă "Anulează".  
2. Fluxul se încheie fără modificări.

**\[A2\] Utilizatorul nu există:**

1. Sistemul nu găsește utilizatorul.  
2. Sistemul afișează eroare.  
3. Fluxul se încheie.

---

**Nume** : Vizualizare Orar Zilnic

**Descriere** : Utilizatorul vizualizează evenimentele programate pentru ziua curentă, incluzând intervalele orare și ferestrele de prezență disponibile. Orarul este populat automat de scheduler și actualizat la orice modificare a categoriilor.

**Actor**: Utilizator / Admin / Moderator (main)

**Eveniment Declansator** : Utilizatorul accesează Dashboard-ul după autentificare.

**Preconditii** : Utilizatorul este autentificat. Schedulerul zilnic a populat cache-ul cu evenimentele zilei.

**Postconditii** : Sistemul afișează lista evenimentelor din ziua curentă.

**Referinte incrucisate**:

**Flux principal**:

| UTILIZATOR | SISTEM |
| :---- | :---- |
| 1\. Accesează Dashboard-ul | 2\. Interoghează cache-ul evenimentelor zilei |
|  | 3\. Afișează lista de evenimente (nume, interval orar, fereastră prezență) |
| 4\. (Opțional) Selectează un eveniment | 5\. Redirecționează către detaliile/statisticile evenimentului |

**Fluxuri alternative:**

**\[A1\] Nu există evenimente programate astăzi:**

1. Sistemul nu găsește evenimente în cache pentru ziua curentă.  
2. Sistemul afișează mesajul "Nu există evenimente programate pentru astăzi."

---
