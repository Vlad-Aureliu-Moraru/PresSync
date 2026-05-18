### **3. Aplicații Asemănătoare — Analiză Comparativă**

Înainte de a detalia arhitectura sistemului Pressync, este necesară o analiză a pieței actuale și a soluțiilor existente pentru gestionarea prezenței. Se constată o diversitate de soluții disponibile, pornind de la sisteme tradiționale pe suport fizic, soluții hardware convenționale, până la platforme cloud moderne. Majoritatea acestora prezintă limitări semnificative în ceea ce privește validarea prezenței fizice reale, sau impun costuri de implementare, întreținere și dependențe tehnologice nejustificate. În mediul universitar și cel corporate, nevoia principală rămâne un echilibru între gradul de ușurință în utilizare și certitudinea prezenței fizice reale a participantului.

Pentru a evidenția distincțiile arhitecturale ale sistemului Pressync, se va compara acesta cu cele trei tipuri de sisteme larg utilizate în prezent.

---

#### **3.1 Sisteme clasice cu cartele magnetice / RFID**

Acestea reprezintă unele dintre cele mai utilizate sisteme în clădirile de birouri și în spațiile institutionale. Principiul de funcționare se bazează pe autentificarea prin posesia unui token fizic (badge sau cartelă RFID) pe care participantul îl scanează la intrarea în perimetrul autorizat.

* **Avantaje:** Sisteme de acest tip oferă o viteză ridicată de autentificare, scanarea fiind realizată instantaneu. De asemenea, acestea beneficiază de stabilitate ridicată, deoarece sunt de regulă conectate prin cablu la serverele instituției, funcționând în cadrul unei rețele intranet izolate.

* **Dezavantaje:** Principalul dezavantaj constă în vulnerabilitatea la metoda „buddy punching” — delegarea fizică a token-ului de autentificare unui terț pentru înregistrarea unei prezențe frauduloase. Mai mult, aceste sisteme implică costuri semnificative atât de investiție inițială (CAPEX), cât și de întreținere (OPEX): achiziția și instalarea cititoarelor fixe la fiecare punct de acces, costul emiterii și înlocuirii periodice a cartelelor pierdute sau deteriorate. Un alt aspect negativ este lipsa de flexibilitate spațială: infrastructura este ancorată spatial, nepermițând mutarea rapidă a sistemului în alte spații fără cititoare instalate.

---

#### **3.2 Sisteme mobile bazate pe scanare de coduri QR**

Această categorie reprezintă o soluție adoptată pe scară largă în contexte educaționale, în care instructorul afișează un cod QR prin intermediul unui videoproiector, iar participanții îl scanează cu dispozitivele mobile personale.

* **Avantaje:** Costurile de implementare hardware sunt minime, sistemul utilizând infrastructura mobilă deja existentă (telefoanele participantilor) și echipamentul de proiecție din sală. Reprezintă o metodă cu costuri reduse și implementare rapidă.

* **Dezavantaje:** Mecanismul de securitate prezintă limitări fundamentale. Vulnerabilitatea principală constă în reproducibilitatea și redistribuibilitatea codului optic: un singur participant aflat în perimetrul autorizat poate captura codul QR prin fotografiere și îl poate redistribui prin platforme de mesagerie instantă sau rețele sociale. În acest mod, persoane aflate în afara incaperii pot scana codul și pot fi înregistrate ca prezente, deși nu se află fizic în locația respectivă. Chiar și implementările care utilizează coduri QR dinamice (care se reîmprospătează la intervale regulate de câteva secunde) rămân susceptibile de eludare prin partajarea ecranului în cadrul aplicațiilor de teleconferință, deoarece codul poate fi relayat cu latență neglijabilă.

---

#### **3.3 Sisteme de tracking pe bază de localizare GPS (cloud-based)**

Această categorie include platformele software moderne care solicită utilizatorului inițierea manuală a unei operații de „check-in” în cadrul aplicației, validarea prezenței realizându-se prin compararea coordonatelor geografice raportate de receptorul GPS al dispozitivului cu coordonatele unei zone geografic delimitate (geofence).

* **Avantaje:** Aceste sisteme oferă un grad ridicat de versatilitate. Fiind implementate în arhitectură cloud, permit configurarea unui eveniment în orice locație, fără a necesita instalarea de infrastructură hardware dedicată. Se potrivesc bine pentru scenarii precum activitățile agenților de vânzări pe teren sau munca desfășurată la distanță.

* **Dezavantaje:** Pentru contextul de interior (săli de curs, birouri), aceste sisteme prezintă trei categorii de limitări semnificative.

În primul rând, sunt **vulnerabile la falsificarea coordonatelor GPS (GPS spoofing / location mocking)**. Deoarece coordonatele geografice sunt raportate de către sistemul de operare al dispozitivului, aplicațiile software disponibile în ecosistemele de distribuție a aplicațiilor mobile pot intercepta și înlocui datele de localizare la nivelul stratului de aplicație, permițând utilizatorilor să declare o prezență într-o locație în care nu se află fizic.

În al doilea rând, aceste sisteme suscită preocupări privind confidențialitatea datelor de localizare. Colectarea continuă a coordonatelor GPS ale dispozitivului intră în conflict cu principiile de protecție a datelor personale, conform reglementărilor internaționale precum Regulamentul (UE) 2016/679 (GDPR). Utilizatorii manifestă reticență în privința acordării accesului permanent la datele de localizare unor aplicații corporative sau educaționale.

În al treilea rând, funcționalitatea sistemului depinde în mod critic de accesul la internet (conexiune de date mobile sau Wi-Fi). În spații cu acoperire radio insuficientă a rețelelor de date mobile (cum ar fi subsolurile sau amfiteatrele cu construcții ecranante), sistemele devin inoperabile.

---

#### **3.4 Sinteză comparativă**

Pentru o privire de ansamblu structurată asupra aspectelor analizate, se prezintă tabelul comparativ de mai jos:

| Criteriu comparativ | Sisteme RFID / Cartele | Scanare Cod QR | Tracking GPS (Cloud) | Pressync |
| :---- | :---- | :---- | :---- | :---- |
| **Costuri de implementare** | Ridicate (necesită hardware dedicat și cartele) | Foarte scăzute (folosește infrastructura mobilă existentă) | Medii spre ridicate (abonamente SaaS/Cloud) | Scăzute (necesită un router compatibil OpenWRT și/sau un sistem embedded) |
| **Risc de fraudă** | Ridicat (delegarea fizică a token-ului de autentificare) | Foarte ridicat (redistribuirea codului optic în afara perimetrului) | Ridicat (falsificarea coordonatelor GPS prin *location mocking*) | Scăzut (conectivitatea la nivelul Stratului 2 este condiționată de atenuarea fizică a semnalului Wi-Fi) |
| **Versatilitate** | Redusă (infrastructură fixă, instalată permanent) | Ridicată | Foarte ridicată | Ridicată (echipament portabil, instalabil în orice spațiu cu alimentare electrică) |
| **Dependență de conexiune externă** | Nu (funcționează de regulă în rețea intranet) | Da | Da (obligatoriu) | Nu (sistemul funcționează într-un mediu de rețea complet izolat, fără conexiune externă) |

---

#### **3.5 Fundamentarea arhitecturală a sistemului Pressync**

Analizând comparativa de mai sus, justificarea arhitecturală a sistemului Pressync devine clară. Arhitectura propusă răspunde limitarilor identificate în soluțiile concurente prin integrarea unui mecanism de validare al prezenței bazat pe proprietățile fizice ale comunicărilor fără fir. Sistemul integrează avantajele solutiilor concurente, eliminând în același timp limitarile identificate.

Spre deosebire de sistemele RFID, unde token-ul fizic poate fi delegat unui terț, Pressync mută identificatorul de autentificare pe dispozitivul mobil personal al participantului, sub forma unui token JWT criptografic securizat. Dispozitivul mobil personal constituie un token de autentificare care, spre deosebire de cartela RFID, nu poate fi delegat fizic fără prezența utilizatorului, având în vedere că acesta nu împrumută de regulă telefonul personal altor persoane. Această abordare elimină și costurile cu infrastructura hardware fixă specifică sistemelor RFID.

Mecanismul fundamental de diferențiere față de toate soluțiile analizate constă în utilizarea **proprietăților fizice ale propagării undelor radio** ca barieră antifradă. Pentru a explica acest aspect în mod științific, se detaliază mai jos principiile care stau la baza validării prezenței în sistemul Pressync.

Din punct de vedere tehnic, mecanismul de validare a prezenței se bazează pe **conectivitatea la nivelul Stratului 2 (Data Link Layer)** al modelului OSI. Punctul de acces Wi-Fi, configurat în regim de portal captiv, emite pe benzile de frecvență de 2,4 GHz și 5 GHz — benzi ISM (Industrial, Scientific, Medical) caracterizate printr-o propagare a undelor radio puternic influențată de mediul fizic înconjurător.

Semnalele radio în aceste benzi sunt supuse **atenuării progresive** la traversarea materialelor de construcție. Valorile tipice de atenuare sunt:
- Peretii din gips-carton: aproximativ 3–5 dB
- Peretii din caramidă: 8–15 dB
- Peretii din **beton armat** (cu armătură metalică): 15–30 dB sau mai mult

Având în vedere că pragul de sensibilitate al receptorului unui dispozitiv mobil tipic este de aproximativ **-85 dBm până la -92 dBm**, semnalul emis de punctul de acces devine insuficient pentru stabilirea unei conexiuni stabile la o distanță de câțiva metri dincolo de peretii incaperii. Prin urmare, pentru a initia și menține o conexiune la nivelul Stratului 2 care să permită transmiterea token-ului JWT, dispozitivul mobil trebuie să se afle **fizic în interiorul perimetrului** delimitat de atenuarea naturală a semnalului.

Un aspect suplimentar care contribuie la restricționarea ariei de acoperire este conceptul de **zonă Fresnel** — regiunea elipsoidală dintre emitator și receptor în care undele radio se propagă preponderent. Obstacolele solide (pereti, mobilier metalic, elemente structurale) perturba această zonă, diminuând și mai mult puterea semnalului în afara incaperii.

Această proprietate fizică transformă **peretii incaperii intr-o barieră naturala antifradă** care nu poate fi eludată prin mijloace software. Este important de evidențiat distincția fundamentală față de celelalte soluții analizate:

1. **Spre deosebire de sistemele QR:** Codul optic poate fi reprodus prin fotografiere și redistribuit independent de locația fizică. În schimb, conectivitatea la nivelul Stratului 2 la un punct de acces Wi-Fi nu poate fi „copiată” sau „transferată” unei alte persoane aflate în afara incaperii.

2. **Spre deosebire de sistemele GPS:** Coordonatele geografice sunt raportate de către sistemul de operare al dispozitivului și pot fi falsificate prin aplicații de tip *location mocking* la nivelul Stratului 7 (Stratul de Aplicație). În schimb, conectivitatea la nivelul Stratului 2 nu poate fi falsificată software, deoarece depinde de existența fizică a unui semnal radio cu putere suficientă la nivelul receptorului dispozitivului.

Prin urmare, metoda Pressync **ancorează dovada prezenței în legile fizicii propagării undelor radio** — un factor care nu poate fi eludat prin mijloace software.

Un alt avantaj semnificativ al arhitecturii Pressync constă în **independența față de conexiunea externă**. Spre deosebire de sistemele bazate pe cloud (care necesită acces permanent la internet) sau chiar de sistemele QR (care de regulă se bazează pe un server accesibil online), **sistemul Pressync funcționează într-un mediu de rețea complet izolat (offline)**. Aplicația rulează local pe routerul OpenWRT sau pe un sistem embedded de tip Raspberry Pi, sub formă de container software preconfigurat. Această abordare oferă două beneficii majore:

În primul rând, elimină dependența de acoperirea rețelelor de date mobile sau de conexiunea la internet. Sistemul funcționează în orice spațiu care dispune de alimentare electrică, indiferent de calitatea semnalului celular sau de existența unei rețele Wi-Fi externe.

În al doilea rând, **asigură confidențialitatea datelor** prin procesarea locală. Datele privind prezența participanților sunt stocate exclusiv pe dispozitivul local, fără a fi transmise către servicii externe sau servere cloud. Această caracteristică asigură conformitatea cu principiile de protecție a datelor personale și elimină preocupările legate de confidențialitate specifice sistemelor de tracking GPS.

În concluzie, Pressync constituie o **soluție autonomă, gata de utilizare**, care îmbină portabilitatea sistemelor cloud, securitatea fizică bazată pe atenuarea semnalului Wi-Fi și independența față de conexiunea externă specifică sistemelor RFID. Prin fundamentarea validării prezenței în proprietățile fizice ale propagării undelor radio, Pressync oferă un cadru robust pentru gestionarea prezenței în contexte educaționale și corporate, eliminând cele mai semnificative vulnerabilități ale soluțiilor existente.
