**Titlu Scenariu: Autentificarea in sistem si obtinerea accesului**

**Actor principal:** Utilizator (Participant sau Administrator)

**Preconditii:** Contul a fost deja creat in baza de date, iar utilizatorul cunoaste datele de conectare.

**Flux principal (Pasi):**

1. Utilizatorul acceseaza pagina de login a aplicatiei web.  
2. Introduce credentialele (username si parola) si apasa butonul de autentificare.  
3. Sistemul valideaza datele introduse comparandu-le cu cele din baza de date (verificand parola criptata).  
4. Daca datele sunt corecte, sistemul genereaza un token securizat (JWT) si il trimite catre client.  
5. Utilizatorul este redirectionat catre interfata specifica rolului sau (dashboard de administrare sau panou personal de prezenta).  
   **Flux alternativ / Exceptii:** Daca parola sau numele de utilizator sunt incorecte, sistemul respinge cererea de generare a tokenului si afiseaza un mesaj de eroare pe aceeasi pagina, fara a oferi detalii despre motivul exact al respingerii, din motive de securitate.

---

**Titlu Scenariu: Configurarea si generarea unei recurente pentru un eveniment**

**Actor principal:** Administrator (Organizator)

**Preconditii:** Administratorul este autentificat si are drepturi pentru a modifica programul din sistem.

**Flux principal (Pasi):**

1. Administratorul acceseaza sectiunea de gestionare si alege optiunea de a crea o categorie noua de evenimente.  
2. Completeaza detaliile necesare (titlu, locatie, descrierea pentru acea sesiune) si stabileste regulile de repetare (orele de inceput si sfarsit, zilele in care se repeta, durata in care e permisa marcarea prezentei).  
3. Apasa butonul de salvare a configuratiei.  
4. Sistemul preia cererea (o comanda de tip scriere, conform principiului CQRS) si salveaza detaliile in baza de date.  
5. Modulul de fundal al sistemului (schedulerele de incarcare zilnica si la nivel de minut) preia noile reguli si genereaza automat in baza de date instantele individuale ale evenimentului, exact la momentul potrivit, fara sa mai fie nevoie de o alta interventie umana.  
   **Flux alternativ / Exceptii:** Daca administratorul a introdus date invalide (de exemplu, fereastra de prezenta incepe dupa terminarea evenimentului), sistemul va opri actiunea, va marca vizual campurile problematice din formular si va afisa un mesaj care cere corectarea acestora.

---

**Titlu Scenariu: Confirmarea prezentei sigure prin reteaua locala**

**Actor principal:** Participant

**Preconditii:** Participantul este autentificat in contul sau de pe propriul telefon sau laptop. Echipamentul hardware (routerul OpenWRT configurat pentru acea locatie) ruleaza in background, iar in sistem exista un eveniment activ in acel moment.

**Flux principal (Pasi):**

1. Participantul se conecteaza fizic la reteaua wireless securizata din locatie.  
2. Acceseaza din aplicatie butonul de marcare a prezentei pentru evenimentul curent.  
3. Aplicatia trimite o cerere care contine tokenul JWT pentru identificare si validare.  
4. Sistemul verifica intai daca utilizatorul se afla intr-adevar in reteaua interna agreata, eliminand astfel incercarile de fraudare de la distanta.  
5. Sistemul se asigura ca exista o sesiune activa si ca se respecta fereastra de timp definita.  
6. Se face o ultima interogare in baza de date pentru a verifica daca participantul nu a fost deja marcat.  
7. Se creeaza si se salveaza o noua inregistrare legata de acel participant si de momentul exact al conectarii, urmand ca pe ecran sa apara un mesaj de succes.  
   **Flux alternativ / Exceptii:** \* Daca participantul incearca sa apese butonul de pe date mobile sau de pe o alta retea wireless, sistemul detecteaza anomalia si blocheaza salvarea, afisand mesajul ca actiunea necesita prezenta in locatie.  
* Daca participantul apasa din greseala de doua ori, sistemul identifica la pasul 6 ca exista deja o inregistrare si informeaza simplu ca prezenta a fost deja confirmata, fara a crea date duplicate.

---

**Titlu Scenariu: Generarea si exportul de rapoarte privind participarea**

**Actor principal:** Administrator (Organizator)

**Preconditii:** Administratorul este autentificat. In sistem exista cel putin o categorie de evenimente care are deja istoric (s-au marcat prezente).

**Flux principal (Pasi):**

1. Administratorul navigheaza in sectiunea de statistici si selecteaza dintr-o lista o categorie specifica pe care vrea sa o analizeze.  
2. Sistemul directioneaza cererea catre fluxul de citire (partea de read din CQRS) pentru a garanta viteza de procesare.  
3. Se preiau datele brute din baza de date si se executa agregrari in timp real (se calculeaza numarul de participanti prezenti versus absenti, procentul de prezenta general, media per utilizator).  
4. Interfata grafica a aplicatiei primeste aceste date si populeaza o serie de grafice intuitive.  
5. Administratorul apasa butonul de export, iar sistemul proceseaza datele afisate si genereaza instantaneu un fisier PDF sau CSV pe care il descarca pe dispozitivul local.  
   **Flux alternativ / Exceptii:** Daca categoria de evenimente aleasa este una abia creata, la care nu a avut loc inca nicio sesiune, sistemul va afisa un mesaj prietenos care anunta ca nu exista date suficiente pentru a genera un raport, mentinand graficele goale.

---

**Titlu Scenariu: Vizualizarea istoricului personal**

**Actor principal:** Participant

**Preconditii:** Participantul este conectat in contul lui.

**Flux principal (Pasi):**

1. Participantul da click pe sectiunea de profil sau pe istoricul prezentei.  
2. Sistemul extrage identitatea utilizatorului direct din tokenul de sesiune, fara a-i mai cere alte date.  
3. Ruleaza o interogare personalizata in sistem care aduce la suprafata doar informatiile si activitatile relevante pentru el.  
4. Aplicatia afiseaza o lista cronologica a tuturor instantelor la care a participat in trecut.  
   **Flux alternativ / Exceptii:** Daca tokenul JWT a expirat in timp ce aplicatia ramasese deschisa in fundal, sistemul va refuza interogarea datelor. Utilizatorul va fi deconectat automat si redirectionat catre pagina de login pentru a obtine un acces nou.

