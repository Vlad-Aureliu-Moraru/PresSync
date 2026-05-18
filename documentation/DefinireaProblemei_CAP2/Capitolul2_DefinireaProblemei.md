# Capitolul 2. Definirea problemei

Prezentul capitol își propune să definească problema gestionării prezenței în contexte educaționale și organizaționale, oferind o imagine de ansamblu asupra metodelor actuale de pontaj și a limitărilor acestora. Se realizează o analiză comparativă a soluțiilor existente pe piață, se descrie soluția propusă — sistemul PresSync — și se enumeră obiectivele generale și specifice ale acestuia. Capitolul se încheie cu definirea actorilor sistemului și prezentarea scenariilor de bază de utilizare.

## 2.1. Introducere și Contextualizare

În contextul actual al activităților educaționale, profesionale și organizaționale, gestionarea prezenței participanților la diverse evenimente rămâne o problemă recurentă și, frecvent, subestimată. Metodele tradiționale de pontaj — precum completarea manuală a listelor pe suport de hârtie, apelarea nominală în sală sau utilizarea cartelelor de acces fizice — implică un consum semnificativ de timp și resurse umane. Cadrele didactice sau administratorii sunt nevoiți să verifice manual fiecare participant, ceea ce întârzie începerea activității propriu-zise și amplifică riscul de erori. Mai mult, aceste abordări clasice sunt vulnerabile la fraude elementare, precum frauda prin interpuși (cunoscută în literatura de specialitate drept *buddy punching*), prin care un participant marchează prezența în locul unui coleg absent. Astfel de practici nu doar că denaturează datele statistice, ci afectează și echitatea procesului de evaluare sau de monitorizare a activității.

Problema nu se limitează însă exclusiv la mediul universitar, deși aplicația de față își are originea în nevoile specifice ale studenților și cadrelor didactice. În realitate, necesitatea unei monitorizări exacte a prezenței apare în domenii extrem de diverse. La conferințe internaționale, de exemplu, organizatorii trebuie să cunoască cu precizie participanții la fiecare sesiune pentru a genera rapoarte de acreditare sau pentru a calcula indicatorii de feedback. În mediul *corporate*, ședințele de echipă, sesiunile de formare sau chiar pontajul zilnic al angajaților ridică aceleași provocări: cum poate fi verificată prezența fără a întrerupe fluxul de lucru și fără a permite abuzuri? Chiar și în cadrul întrunirilor de voluntariat, al cluburilor sportive sau al evenimentelor culturale, absența unui sistem eficient generează statistici inexacte, care, la rândul lor, afectează planificarea ulterioară și alocarea resurselor. Astfel, oriunde există un grup de persoane care se reunesc într-un spațiu fizic pentru o activitate comună, apare necesitatea unui mecanism fiabil de înregistrare a prezenței, însoțit de capacitatea de a genera statistici și predicții utile.

## 2.2. Analiza soluțiilor existente

Diversitatea metodelor de pontaj menționată în secțiunea anterioară se reflectă și în peisajul soluțiilor digitale disponibile în prezent. Pentru o evaluare obiectivă a stadiului actual al tehnologiilor de gestionare a prezenței, în continuare se prezintă o analiză comparativă a principalelor categorii de sisteme existente pe piață, urmată de o sinteză structurată și de fundamentarea arhitecturală a soluției propuse.

Se constată o diversitate de soluții disponibile, pornind de la sisteme tradiționale pe suport fizic, soluții hardware convenționale, până la platforme *cloud* moderne. Majoritatea acestora prezintă limitări semnificative în ceea ce privește validarea prezenței fizice reale, sau impun costuri de implementare, întreținere și dependențe tehnologice nejustificate. În mediul universitar și cel *corporate*, nevoia principală rămâne un echilibru între gradul de ușurință în utilizare și certitudinea prezenței fizice reale a participantului.

Pentru a evidenția distincțiile arhitecturale ale sistemului PresSync, se va compara acesta cu cele trei tipuri de sisteme larg utilizate în prezent.

### 2.2.1. Sisteme clasice cu cartele magnetice / RFID

Acestea reprezintă unele dintre cele mai utilizate sisteme în clădirile de birouri și în spațiile instituționale. Principiul de funcționare se bazează pe autentificarea prin posesia unui *token* fizic (badge sau cartelă RFID) pe care participantul îl scanează la intrarea în perimetrul autorizat.

**Avantaje:** Sisteme de acest tip oferă o viteză ridicată de autentificare, scanarea fiind realizată instantaneu. De asemenea, acestea beneficiază de stabilitate ridicată, deoarece sunt de regulă conectate prin cablu la serverele instituției, funcționând în cadrul unei rețele Intranet izolate.

**Dezavantaje:** Principalul dezavantaj constă în vulnerabilitatea la metoda *„buddy punching”* — delegarea fizică a *token*-ului de autentificare unui terț pentru înregistrarea unei prezențe frauduloase. Mai mult, aceste sisteme implică costuri semnificative atât de investiție inițială (CAPEX), cât și de întreținere (OPEX): achiziția și instalarea cititoarelor fixe la fiecare punct de acces, costul emiterii și înlocuirii periodice a cartelelor pierdute sau deteriorate. Un alt aspect negativ este lipsa de flexibilitate spațială: infrastructura este ancorată spațial, nepermițând mutarea rapidă a sistemului în alte spații fără cititoare instalate.

### 2.2.2. Sisteme mobile bazate pe scanare de coduri QR

Această categorie reprezintă o soluție adoptată pe scară largă în contexte educaționale, în care instructorul afișează un cod QR prin intermediul unui videoproiector, iar participanții îl scanează cu dispozitivele mobile personale.

**Avantaje:** Costurile de implementare hardware sunt minime, sistemul utilizând infrastructura mobilă deja existentă (telefoanele participanților) și echipamentul de proiecție din sală. Reprezintă o metodă cu costuri reduse și implementare rapidă.

**Dezavantaje:** Mecanismul de securitate prezintă limitări fundamentale. Vulnerabilitatea principală constă în reproducibilitatea și redistribuibilitatea codului optic: un singur participant aflat în perimetrul autorizat poate captura codul QR prin fotografiere și îl poate redistribui prin platforme de mesagerie instantă sau rețele sociale. În acest mod, persoane aflate în afara încăperii pot scana codul și pot fi înregistrate ca prezente, deși nu se află fizic în locația respectivă. Chiar și implementările care utilizează coduri QR dinamice (care se reîmprospătează la intervale regulate de câteva secunde) rămân susceptibile de eludare prin partajarea ecranului în cadrul aplicațiilor de teleconferință, deoarece codul poate fi *relayat* cu latență neglijabilă.

### 2.2.3. Sisteme de tracking pe bază de localizare GPS (*cloud*-based)

Această categorie include platformele software moderne care solicită utilizatorului inițierea manuală a unei operații de *„check-in”* în cadrul aplicației, validarea prezenței realizându-se prin compararea coordonatelor geografice raportate de receptorul GPS al dispozitivului cu coordonatele unei zone geografice delimitate (*geofence*).

**Avantaje:** Aceste sisteme oferă un grad ridicat de versatilitate. Fiind implementate în arhitectură *cloud*, permit configurarea unui eveniment în orice locație, fără a necesita instalarea de infrastructură hardware dedicată. Se potrivesc bine pentru scenarii precum activitățile agenților de vânzări pe teren sau munca desfășurată la distanță.

**Dezavantaje:** Pentru contextul de interior (săli de curs, birouri), aceste sisteme prezintă trei categorii de limitări semnificative.

În primul rând, sunt **vulnerabile la falsificarea coordonatelor GPS (GPS spoofing / location mocking)**. Deoarece coordonatele geografice sunt raportate de către sistemul de operare al dispozitivului, aplicațiile software disponibile în ecosistemele de distribuție a aplicațiilor mobile pot intercepta și înlocui datele de localizare la nivelul stratului de aplicație, permițând utilizatorilor să declare o prezență într-o locație în care nu se află fizic.

În al doilea rând, aceste sisteme suscită preocupări privind confidențialitatea datelor de localizare. Colectarea continuă a coordonatelor GPS ale dispozitivului intră în conflict cu principiile de protecție a datelor personale, conform reglementărilor internaționale precum Regulamentul (UE) 2016/679 (GDPR). Utilizatorii manifestă reticență în privința acordării accesului permanent la datele de localizare unor aplicații corporative sau educaționale.

În al treilea rând, funcționalitatea sistemului depinde în mod critic de accesul la internet (conexiune de date mobile sau Wi-Fi). În spații cu acoperire radio insuficientă a rețelelor de date mobile (cum ar fi subsolurile sau amfiteatrele cu construcții ecranante), sistemele devin inoperabile.

### 2.2.4. Sinteză comparativă

Pentru o privire de ansamblu structurată asupra aspectelor analizate, se prezintă tabelul comparativ de mai jos:

| Criteriu comparativ | Sisteme RFID / Cartele | Scanare Cod QR | Tracking GPS (Cloud) | PresSync |
| :--- | :--- | :--- | :--- | :--- |
| **Costuri de implementare** | Ridicate (necesită hardware dedicat și cartele) | Foarte scăzute (folosește infrastructura mobilă existentă) | Medii spre ridicate (abonamente SaaS/Cloud) | Scăzute (necesită un router compatibil OpenWRT și/sau un sistem embedded) |
| **Risc de fraudă** | Ridicat (delegarea fizică a *token*-ului de autentificare) | Foarte ridicat (redistribuirea codului optic în afara perimetrului) | Ridicat (falsificarea coordonatelor GPS prin *location mocking*) | Scăzut (conectivitatea la nivelul Stratului 2 este condiționată de atenuarea fizică a semnalului Wi-Fi) |
| **Versatilitate** | Redusă (infrastructură fixă, instalată permanent) | Ridicată | Foarte ridicată | Ridicată (echipament portabil, instalabil în orice spațiu cu alimentare electrică) |
| **Dependență de conexiune externă** | Nu (funcționează de regulă în rețea Intranet) | Da | Da (obligatoriu) | Nu (sistemul funcționează într-un mediu de rețea complet izolat, fără conexiune externă) |

### 2.2.5. Fundamentarea arhitecturală a sistemului PresSync

Analizând comparativa de mai sus, justificarea arhitecturală a sistemului PresSync devine clară. Arhitectura propusă răspunde limitărilor identificate în soluțiile concurente prin integrarea unui mecanism de validare al prezenței bazat pe proprietățile fizice ale comunicărilor fără fir. Sistemul integrează avantajele soluțiilor concurente, eliminând în același timp limitările identificate.

Spre deosebire de sistemele RFID, unde *token*-ul fizic poate fi delegat unui terț, PresSync mută identificatorul de autentificare pe dispozitivul mobil personal al participantului, sub forma unui *token* JWT criptografic securizat. Dispozitivul mobil personal constituie un *token* de autentificare care, spre deosebire de cartela RFID, nu poate fi delegat fizic fără prezența utilizatorului, având în vedere că acesta nu împrumută de regulă telefonul personal altor persoane. Această abordare elimină și costurile cu infrastructura hardware fixă specifică sistemelor RFID.

Mecanismul fundamental de diferențiere față de toate soluțiile analizate constă în utilizarea **proprietăților fizice ale propagării undelor radio** ca barieră antifraudă. Pentru a explica acest aspect în mod științific, se detaliază mai jos principiile care stau la baza validării prezenței în sistemul PresSync.

Din punct de vedere tehnic, mecanismul de validare a prezenței se bazează pe **conectivitatea la nivelul Stratului 2 (Data Link Layer)** al modelului OSI. Punctul de acces Wi-Fi, configurat în regim de portal captiv, emite pe benzile de frecvență de 2,4 GHz și 5 GHz — benzi ISM (Industrial, Scientific, Medical) caracterizate printr-o propagare a undelor radio puternic influențată de mediul fizic înconjurător.

Semnalele radio în aceste benzi sunt supuse **atenuării progresive** la traversarea materialelor de construcție. Valorile tipice de atenuare sunt:
- Pereții din gips-carton: aproximativ 3–5 dB
- Pereții din cărămidă: 8–15 dB
- Pereții din **beton armat** (cu armătură metalică): 15–30 dB sau mai mult

Având în vedere că pragul de sensibilitate al receptorului unui dispozitiv mobil tipic este de aproximativ **-85 dBm până la -92 dBm**, semnalul emis de punctul de acces devine insuficient pentru stabilirea unei conexiuni stabile la o distanță de câțiva metri dincolo de pereții încăperii. Prin urmare, pentru a iniția și menține o conexiune la nivelul Stratului 2 care să permită transmiterea *token*-ului JWT, dispozitivul mobil trebuie să se afle **fizic în interiorul perimetrului** delimitat de atenuarea naturală a semnalului.

Un aspect suplimentar care contribuie la restricționarea ariei de acoperire este conceptul de **zonă Fresnel** — regiunea elipsoidală dintre emițător și receptor în care undele radio se propagă preponderent. Obstacolele solide (pereți, mobilier metalic, elemente structurale) perturbă această zonă, diminuând și mai mult puterea semnalului în afara încăperii.

Această proprietate fizică transformă **pereții încăperii într-o barieră naturală antifraudă** care nu poate fi eludată prin mijloace software. Este important de evidențiat distincția fundamentală față de celelalte soluții analizate:

1. **Spre deosebire de sistemele QR:** Codul optic poate fi reprodus prin fotografiere și redistribuit independent de locația fizică. În schimb, conectivitatea la nivelul Stratului 2 la un punct de acces Wi-Fi nu poate fi „copiată” sau „transferată” unei alte persoane aflate în afara încăperii.

2. **Spre deosebire de sistemele GPS:** Coordonatele geografice sunt raportate de către sistemul de operare al dispozitivului și pot fi falsificate prin aplicații de tip *location mocking* la nivelul Stratului 7 (Stratul de Aplicație). În schimb, conectivitatea la nivelul Stratului 2 nu poate fi falsificată software, deoarece depinde de existența fizică a unui semnal radio cu putere suficientă la nivelul receptorului dispozitivului.

Prin urmare, metoda PresSync **ancorează dovada prezenței în legile fizicii propagării undelor radio** — un factor care nu poate fi eludat prin mijloace software.

Un alt avantaj semnificativ al arhitecturii PresSync constă în **independența față de conexiunea externă**. Spre deosebire de sistemele bazate pe *cloud* (care necesită acces permanent la internet) sau chiar de sistemele QR (care de regulă se bazează pe un server accesibil *online*), **sistemul PresSync funcționează într-un mediu de rețea complet izolat (offline)**. Aplicația rulează local pe routerul OpenWRT sau pe un sistem embedded de tip Raspberry Pi, sub formă de container software preconfigurat. Această abordare oferă două beneficii majore:

În primul rând, elimină dependența de acoperirea rețelelor de date mobile sau de conexiunea la internet. Sistemul funcționează în orice spațiu care dispune de alimentare electrică, indiferent de calitatea semnalului celular sau de existența unei rețele Wi-Fi externe.

În al doilea rând, **asigură confidențialitatea datelor** prin procesarea locală. Datele privind prezența participanților sunt stocate exclusiv pe dispozitivul local, fără a fi transmise către servicii externe sau servere *cloud*. Această caracteristică asigură conformitatea cu principiile de protecție a datelor personale și elimină preocupările legate de confidențialitate specifice sistemelor de tracking GPS.

În concluzie, PresSync constituie o **soluție autonomă, gata de utilizare**, care îmbină portabilitatea sistemelor *cloud*, securitatea fizică bazată pe atenuarea semnalului Wi-Fi și independența față de conexiunea externă specifică sistemelor RFID. Prin fundamentarea validării prezenței în proprietățile fizice ale propagării undelor radio, PresSync oferă un cadru robust pentru gestionarea prezenței în contexte educaționale și *corporate*, eliminând cele mai semnificative vulnerabilități ale soluțiilor existente.

## 2.3. Descrierea problemei și Soluția propusă (PresSync)

Problemele identificate în cadrul analizei comparative — fraudabilitatea sistemelor RFID prin delegarea *token*-ului fizic, redistribuibilitatea codurilor QR în afara perimetrului autorizat, vulnerabilitatea sistemelor GPS la falsificarea coordonatelor și dependența critică de conexiunea externă a soluțiilor *cloud* — demonstrează necesitatea unei abordări fundamental diferite în ceea ce privește înregistrarea prezenței.

Soluțiile digitale existente au încercat să modernizeze acest proces, însă prezintă limitări semnificative care le fac inadecvate în numeroase scenarii practice. Multe aplicații se bazează pe geolocalizare prin GPS, solicitând utilizatorului să confirme prezența prin poziția telefonului. Totuși, această metodă poate fi eludată cu relativă ușurință, prin intermediul aplicațiilor care simulează locația sau al dispozitivelor care redirecționează semnalul. Dependența de conexiunea la internet reprezintă o altă problemă majoră, în special în locații cu semnal slab sau în contexte în care confidențialitatea datelor este esențială. Soluțiile bazate pe *cloud* stochează informațiile pe servere terțe, ceea ce ridică întrebări legitime privind protecția datelor personale și riscul apariției breșelor de securitate. În plus, aceste platforme impun frecvent abonamente costisitoare și necesită conexiune permanentă, ceea ce le face inaccesibile pentru instituții mici sau pentru evenimente temporare desfășurate în spații fără acoperire stabilă de rețea.

Toate aceste aspecte evidențiază necesitatea unui sistem care să îmbine simplitatea și siguranța fizică cu avantajele tehnologiei moderne. O soluție ideală ar trebui să elimine complet posibilitatea fraudelor, prin legarea prezenței de un factor fizic incontestabil, cum ar fi conectarea la o rețea locală. În același timp, ar trebui să permită administrarea facilă a evenimentelor — inclusiv a celor recurente — să ofere statistici detaliate și să suporte roluri diferențiate de utilizatori, de la administratori până la participanți simpli. Având în vedere deficiențele menționate anterior, s-a identificat necesitatea dezvoltării unui sistem izolat, găzduit local pe un dispozitiv accesibil, precum un router OpenWRT. În această configurație, prezența fizică în rețea constituie o dovadă suficientă a participării, fără a mai depinde de GPS — ușor de manipulat — sau de servere externe. Datele astfel colectate pot fi ulterior procesate pentru a genera rapoarte, istorice și chiar predicții privind comportamentul de participare, toate într-un mediu controlat și securizat.

Această descriere a problemei subliniază nu doar inconvenientele practice cotidiene, ci și impactul pe termen lung asupra eficienței organizaționale și a transparenței. În absența unui instrument dedicat, instituțiile și organizațiile pierd timp valoros, își asumă riscul unor date inexacte și nu pot beneficia de analize care să îmbunătățească procesele viitoare. PresSync răspunde exact acestor nevoi, propunând o abordare integrată care transformă pontajul dintr-o sarcină birocratică într-un proces automatizat, sigur și util pentru procesul decizional. Prin implementarea sa, se urmărește nu doar rezolvarea imediată a problemelor curente, ci și crearea unui cadru scalabil, adaptabil la diverse contexte de utilizare.

## 2.4. Obiectivele sistemului

Pe baza problemei identificate și a analizei comparative a soluțiilor existente, s-au definit următoarele obiective pentru sistemul PresSync.

### Obiective Generale

Scopul fundamental al aplicației PresSync este înlocuirea metodelor tradiționale de pontaj — lente și predispuse la erori — cu un motor universal și localizat de gestionare a prezenței și a evenimentelor. Deși ideea a pornit de la o problemă reală și frecvent întâlnită în contextul orelor de facultate, obiectivul major este ca sistemul să fie scalabil și adaptabil dincolo de mediul academic. Platforma a fost concepută pentru a putea fi integrată cu ușurință în mediul *corporate* (ședințe, training-uri), în cadrul conferințelor cu sute de participanți sau chiar în cadrul întâlnirilor organizațiilor non-guvernamentale. În esență, sistemul urmărește simplificarea muncii administrative oriunde un grup de persoane se reunește fizic pentru o activitate comună.

### Obiective Specifice

Pentru a materializa viziunea descrisă mai sus, arhitectura și dezvoltarea sistemului PresSync au fost ghidate de următoarele obiective funcționale și tehnice:

* **Funcționare autonomă și independență hardware:** Sistemul este proiectat să ruleze izolat pe echipamente accesibile, precum un router OpenWRT. Această abordare elimină complet dependența de o conexiune externă stabilă la internet sau de servere *cloud* costisitoare, asigurând un mediu securizat și nealterabil în care prezența în rețeaua fizică generată de router constituie dovada fundamentală a participării.
* **Automatizarea calendarului și a recurenței:** Printr-un modul comun ce utilizează componente de tip *scheduler* (*DailyLoaderScheduler* și *MinuteEventScheduler*), sistemul preia munca repetitivă a administratorului. Odată definită o categorie de evenimente împreună cu regulile sale de recurență (zilnic, săptămânal etc.), aplicația instanțiază automat noi evenimente la momentul oportun, fără nicio intervenție manuală.
* **Securizarea și delimitarea strictă a accesului:** Având în vedere că aplicația gestionează date din diverse medii organizatorice, autentificarea utilizatorilor este realizată prin *token*-uri JWT, asigurând un flux clar de autorizare. Fiecare actor are vizibilitate limitată la resursele relevante: participantul își confirmă prezența și își verifică istoricul personal, în timp ce organizatorul (profesor/manager) administrează resursele colective și accesează datele întregului grup.
* **Procesare concurentă și scalabilitate ridicată:** Sistemul separă operațiile de modificare a datelor (comenzi) de cele de citire (interogări), aplicând principiul arhitectural CQRS (Command Query Responsibility Segregation). Această separare asigură că cererile de marcare a prezenței sunt procesate instantaneu și fără conflicte, iar cererile complexe de calcul al statisticilor pe o categorie întreagă de evenimente rulează pe un flux propriu optimizat, fără a bloca aplicația.
* **Generarea de statistici în timp real:** În loc să stocheze datele în formă brută, sistemul le transformă în informații utile la cerere. Acesta agreghează automat istoricul, calculează procentajul de prezență la nivel de utilizator sau eveniment și permite extragerea de rapoarte detaliate, ușor de interpretat direct din interfață.

## 2.5. Participanți (Actori)

Sistemul PresSync definește doi actori principali, care reprezintă stakeholderii primari și, respectiv, secundari ai aplicației.

**Administratorul (Organizatorul)** — stakeholder primar — este persoana responsabilă cu configurarea evenimentelor și a categoriilor acestora, gestionarea participanților, definirea regulilor de recurență și generarea rapoartelor statistice. Acesta beneficiază de acces deplin la funcționalitățile de administrare ale sistemului.

**Participantul** — stakeholder secundar — interacționează cu sistemul exclusiv pentru marcarea prezenței în cadrul evenimentelor la care participă și pentru vizualizarea propriului istoric de prezență. Accesul acestuia este limitat la resursele care îi sunt direct asociate.

Această delimitare strictă a rolurilor asigură o securitate sporită a datelor și o experiență adaptată fiecărui tip de utilizator, în concordanță cu principiul privilegiului minim (*principle of least privilege*).

## 2.6. Scenarii de bază

Pentru o mai bună înțelegere a modului de interacțiune a participanților cu sistemul PresSync, prezentăm următoarele scenarii de utilizare:

---

**Titlu Scenariu: Autentificarea în sistem și obținerea accesului**

**Actor principal:** Utilizator (Participant sau Administrator)

**Precondiții:** Contul există deja în baza de date, iar utilizatorul cunoaște datele de conectare.

**Flux principal (Pași):**

1. Utilizatorul accesează pagina de *login* a aplicației web.
2. Introduce credențialele (nume de utilizator și parolă) și apasă butonul de autentificare.
3. Sistemul validează datele introduse comparându-le cu cele din baza de date (verifică parola criptată).
4. Dacă datele sunt corecte, sistemul generează un *token* securizat (JWT) și îl trimite către client.
5. Sistemul redirecționează utilizatorul către interfața specifică rolului său (*dashboard* de administrare sau panou personal de prezență).

**Flux alternativ / Excepții:** Dacă parola sau numele de utilizator sunt incorecte, sistemul respinge cererea de generare a *token*-ului și afișează un mesaj generic de eroare pe aceeași pagină, fără a dezvălui care dintre cele două câmpuri este greșit, pentru a preveni atacurile de enumerare a utilizatorilor (*user enumeration*).

---

**Titlu Scenariu: Configurarea și generarea unei recurente pentru un eveniment**

**Actor principal:** Administrator (Organizator)

**Precondiții:** Administratorul este autentificat și are drepturi de a modifica programul din sistem.

**Flux principal (Pași):**

1. Administratorul accesează secțiunea de gestionare și alege opțiunea de a crea o categorie nouă de evenimente.
2. Completează detaliile necesare (titlu, locație, descrierea sesiunii) și stabilește regulile de repetare (orele de început și sfârșit, zilele de repetare, durata ferestrei de marcare a prezenței).
3. Apasă butonul de salvare a configurației.
4. Sistemul preia cererea (o comandă de tip scriere, conform principiului CQRS) și salvează detaliile în baza de date.
5. Modulul de fundal al sistemului (*scheduler*-ele de încărcare zilnică și la nivel de minut) preia noile reguli și generează automat în baza de date instanțele individuale ale evenimentului, exact la momentul potrivit, fără a necesita altă intervenție umană.

**Flux alternativ / Excepții:** Dacă administratorul introduce date invalide (de exemplu, fereastra de prezență începe după terminarea evenimentului), sistemul oprește acțiunea, marchează vizual câmpurile problematice din formular și afișează un mesaj care cere corectarea acestora.

---

**Titlu Scenariu: Confirmarea prezenței sigure prin rețeaua locală**

**Actor principal:** Participant

**Precondiții:** Participantul este autentificat în contul său de pe propriul telefon sau laptop. Routerul OpenWRT din locație rulează un portal captiv configurat pentru evenimentul curent. În sistem există un eveniment activ în acest moment.

**Flux principal (Pași):**

1. Participantul se conectează fizic la rețeaua wireless securizată din locație.
2. Portalul captiv al routerului OpenWRT interceptează cererea HTTP și redirecționează automat participantul către aplicația web. Aceasta forțează prezența fizică — doar utilizatorii aflați în raza de acoperire a rețelei pot trece de portal.
3. Participantul accesează din aplicație butonul de marcare a prezenței pentru evenimentul curent.
4. Aplicația trimite o cerere care conține *token*-ul JWT pentru identificare și validare.
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

**Precondiții:** Administratorul este autentificat. În sistem există cel puțin o categorie de evenimente care are deja istoric (s-au marcat prezențe).

**Flux principal (Pași):**

1. Administratorul navighează în secțiunea de statistici și selectează dintr-o listă o categorie specifică pe care dorește să o analizeze.
2. Sistemul direcționează cererea către fluxul de citire (partea de *read* din CQRS) pentru a garanta viteza de procesare.
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
2. Sistemul extrage identitatea utilizatorului direct din *token*-ul de sesiune, fără a-i mai cere alte date.
3. Sistemul rulează o interogare personalizată care aduce la suprafață doar informațiile și activitățile relevante pentru participant.
4. Aplicația afișează o listă cronologică a tuturor instanțelor la care participantul a luat parte în trecut.

**Flux alternativ / Excepții:** Dacă *token*-ul JWT expiră în timp ce aplicația rămăsese deschisă în fundal, sistemul refuză interogarea datelor. Sistemul deconectează automat utilizatorul și îl redirecționează către pagina de *login* pentru a obține un acces nou.
