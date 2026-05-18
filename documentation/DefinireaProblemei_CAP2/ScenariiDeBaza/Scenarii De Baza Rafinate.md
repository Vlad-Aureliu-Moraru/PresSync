**Titlu Scenariu: Autentificarea în sistem și obținerea accesului**

**Actor principal:** Utilizator (Participant sau Administrator)

**Precondiții:** Contul există deja în baza de date, iar utilizatorul cunoaște datele de conectare.

**Flux principal (Pași):**

1. Utilizatorul accesează pagina de login a aplicației web.
2. Introduce credentialele (nume de utilizator și parolă) și apasă butonul de autentificare.
3. Sistemul validează datele introduse comparându-le cu cele din baza de date (verifică parola criptată).
4. Dacă datele sunt corecte, sistemul generează un token securizat (JWT) și îl trimite către client.
5. Sistemul redirecționează utilizatorul către interfața specifică rolului său (dashboard de administrare sau panou personal de prezență).

**Flux alternativ / Excepții:** Dacă parola sau numele de utilizator sunt incorecte, sistemul respinge cererea de generare a tokenului și afișează un mesaj generic de eroare pe aceeași pagină, fără a dezvălui care dintre cele două câmpuri este greșit, pentru a preveni atacurile de enumerare a utilizatorilor (user enumeration).

---

**Titlu Scenariu: Configurarea și generarea unei recurente pentru un eveniment**

**Actor principal:** Administrator (Organizator)

**Precondiții:** Administratorul este autentificat și are drepturi de a modifica programul din sistem.

**Flux principal (Pași):**

1. Administratorul accesează secțiunea de gestionare și alege opțiunea de a crea o categorie nouă de evenimente.
2. Completează detaliile necesare (titlu, locație, descrierea sesiunii) și stabilește regulile de repetare (orele de început și sfârșit, zilele de repetare, durata ferestrei de marcare a prezenței).
3. Apasă butonul de salvare a configurației.
4. Sistemul preia cererea (o comandă de tip scriere, conform principiului CQRS) și salvează detaliile în baza de date.
5. Modulul de fundal al sistemului (schedulerele de încărcare zilnică și la nivel de minut) preia noile reguli și generează automat în baza de date instanțele individuale ale evenimentului, exact la momentul potrivit, fără a necesita altă intervenție umană.

**Flux alternativ / Excepții:** Dacă administratorul introduce date invalide (de exemplu, fereastra de prezență începe după terminarea evenimentului), sistemul oprește acțiunea, marchează vizual câmpurile problematice din formular și afișează un mesaj care cere corectarea acestora.

---

**Titlu Scenariu: Confirmarea prezenței sigure prin rețeaua locală**

**Actor principal:** Participant

**Precondiții:** Participantul este autentificat în contul său de pe propriul telefon sau laptop. Routerul OpenWRT din locație rulează un portal captiv configurat pentru evenimentul curent. În sistem există un eveniment activ în acest moment.

**Flux principal (Pași):**

1. Participantul se conectează fizic la rețeaua wireless securizată din locație.
2. Portalul captiv al routerului OpenWRT interceptează cererea HTTP și redirecționează automat participantul către aplicația web. Aceasta forțează prezența fizică — doar utilizatorii aflați în raza de acoperire a rețelei pot trece de portal.
3. Participantul accesează din aplicație butonul de marcare a prezenței pentru evenimentul curent.
4. Aplicația trimite o cerere care conține tokenul JWT pentru identificare și validare.
5. Sistemul verifică dacă utilizatorul se află în rețeaua internă agreată (adresa IP sursă aparține subnetului local), eliminând astfel încercările de fraudare de la distanță.
6. Sistemul se asigură că există o sesiune activă și că se respectă fereastra de timp definită.
7. Sistemul interoghează baza de date pentru a verifica dacă participantul nu a fost deja marcat.
8. Sistemul creează și salvează o nouă înregistrare asociată participantului și momentului exact al conectării, iar pe ecran apare un mesaj de succes.

**Flux alternativ / Excepții:**
- Dacă participantul încearcă să apese butonul de pe date mobile sau de pe o altă rețea wireless, sistemul detectează anomalia (IP-ul sursă nu aparține subnetului local) și blochează salvarea, afișând mesajul că acțiunea necesită prezența în locație.
- Dacă participantul apasă din greșeală de două ori, sistemul identifică la pasul 7 că există deja o înregistrare și informează că prezența a fost deja confirmată, fără a crea date duplicate.

---

**Titlu Scenariu: Generarea și exportul de rapoarte privind participarea**

**Actor principal:** Administrator (Organizator)

**Precondiții:** Administratorul este autentificat. În sistem există cel puțin o categorie de evenimente care are deja istoric (s-au marcat prezente).

**Flux principal (Pași):**

1. Administratorul navighează în secțiunea de statistici și selectează dintr-o listă o categorie specifică pe care dorește să o analizeze.
2. Sistemul direcționează cererea către fluxul de citire (partea de read din CQRS) pentru a garanta viteza de procesare.
3. Sistemul preia datele brute din baza de date și execută agregări în timp real (calculează numărul de participanți prezenți versus absenți, procentul de prezență general, media per utilizator).
4. Interfața grafică a aplicației primește aceste date și populează o serie de grafice intuitive.
5. Administratorul apasă butonul de export, iar sistemul procesează datele afișate și generează instantaneu un fișier PDF sau CSV pe care administratorul îl descarcă pe dispozitivul local.

**Flux alternativ / Excepții:** Dacă categoria de evenimente aleasă este una abia creată, la care nu a avut loc încă nicio sesiune, sistemul afișează un mesaj prietenos care anunță că nu există date suficiente pentru a genera un raport, menținând graficele goale.

---

**Titlu Scenariu: Vizualizarea istoricului personal**

**Actor principal:** Participant

**Precondiții:** Participantul este conectat în contul său.

**Flux principal (Pași):**

1. Participantul dă click pe secțiunea de profil sau pe istoricul prezenței.
2. Sistemul extrage identitatea utilizatorului direct din tokenul de sesiune, fără a-i mai cere alte date.
3. Sistemul rulează o interogare personalizată care aduce la suprafață doar informațiile și activitățile relevante pentru participant.
4. Aplicația afișează o listă cronologică a tuturor instanțelor la care participantul a luat parte în trecut.

**Flux alternativ / Excepții:** Dacă tokenul JWT expiră în timp ce aplicația rămăsese deschisă în fundal, sistemul refuză interogarea datelor. Sistemul deconectează automat utilizatorul și îl redirecționează către pagina de login pentru a obține un acces nou.
