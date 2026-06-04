### **Aplicatii asemanatoare \- comparatie**

Inainte de a detalia arhitectura sistemului Pressync, este necesara o analiza a pietei actuale si a solutiilor existente pentru pontaj si gestionarea prezentei. Desi exista o multime de variante, de la banalele liste pe hartie si sisteme hardware invechite, pana la platforme cloud destul de complexe, majoritatea prezinta vulnerabilitati majore cand vine vorba de validarea prezentei fizice reale, sau impun niste costuri si dependente tehnice nejustificate. In mediul universitar si cel corporate, nevoia principala ramane un echilibru intre usurinta in utilizare si certitudinea ca persoana pontata se afla cu adevarat in sala.

Pentru a sublinia avantajele abordarii noastre, voi compara Pressync cu cele mai populare trei tipuri de sisteme folosite in prezent:

**1\. Sisteme clasice cu cartele magnetice / RFID**

Acestea sunt probabil cele mai intalnite sisteme in cladirile de birouri si pe holurile facultatilor. Principiul este simplu: fiecare participant are un badge fizic pe care il scaneaza la intrare.

* **Avantaje:** Sunt extrem de rapide, scanarea se face instantaneu, iar sistemele sunt de regula foarte stabile deoarece sunt cablate direct la serverele institutiei.  
* **Dezavantaje:** Cel mai mare minus este vulnerabilitatea la metoda "buddy punching". Un student sau un angajat ii poate da cartela sa unui coleg pentru a fi pontat in locul lui, el fiind de fapt acasa. Pe langa asta, implica niste costuri initiale si de mentenanta destul de mari (achizitia cititoarelor pentru fiecare usa, costul emiterii si inlocuirii continue a cartelelor pierdute). Mai mult, aceste sisteme sunt fixe; nu ofera deloc flexibilitate daca evenimentul se muta intr-o sala fara cititor sau intr-un spatiu inchiriat.

**2\. Aplicatii mobile bazate pe scanare de cod QR**

O solutie destul de populara recent, mai ales la cursuri, unde profesorul proiecteaza un cod QR urias pe tabla, iar studentii il scaneaza cu telefonul personal.

* **Avantaje:** Costurile de implementare hardware sunt nule. Utilizeaza infrastructura deja existenta (telefoanele participantilor si videoproiectorul salii). Este o metoda ieftina si aparent la indemana.  
* **Dezavantaje:** Securitatea este practic inexistenta. E de ajuns ca un singur student aflat in sala sa faca o poza codului QR si sa o trimita pe grupul de WhatsApp al grupei. Imediat, zeci de colegi care stau in pat il pot scana de pe laptop si sistemul ii va inregistra ca fiind prezenti. Chiar si incercarile de a pune un timer (QR dinamic care se schimba la cateva secunde) pot fi fentate banal prin partajarea ecranului pe Discord sau Zoom.

**3\. Sisteme de tracking bazate pe locatie GPS (cloud-based)**

Aici discutam despre platformele software moderne care iti cer sa apesi un buton de "Check-in" in aplicatie, validand prezenta prin compararea coordonatelor GPS ale telefonului tau cu coordonatele cladirii.

* **Avantaje:** Ofera o versatilitate uriasa. Fiind in cloud, poti configura un eveniment oriunde pe glob fara sa instalezi vreun cablu. Se potrivesc bine pentru agentii de vanzari pe teren sau munca remote.  
* **Dezavantaje:** Au trei probleme fatale pentru mediul de interior. In primul rand, pot fi pacalite ridicol de usor descarcand o aplicatie de "Fake GPS" sau "Mock Location" din magazinul de aplicatii. In al doilea rand, ridica mari probleme etice si de privacy; putini oameni se simt confortabil stiind ca o aplicatie de la munca sau facultate le acceseaza senzorul GPS. In final, aceste sisteme sunt paralizate fara o conexiune stabila la internet. Daca ai curs intr-un amfiteatru la demisol unde nu este semnal 4G/5G, sistemul devine inutil.

Pentru o privire de ansamblu mai clara, am sintetizat aceste aspecte in tabelul urmator:

| Criteriu comparativ | Sisteme RFID / Cartele | Scanare Cod QR | Tracking GPS (Cloud) | Pressync |
| :---- | :---- | :---- | :---- | :---- |
| **Costuri de implementare** | Ridicate (hardware aditional si cartele) | Foarte scazute (foloseste telefoanele) | Medii spre mari (abonamente SaaS/Cloud) | Scazute (necesita doar un router ieftin/Raspberry Pi) |
| **Risc de frauda** | Ridicat (imprumutarea cartelei) | Critic de ridicat (distribuire poze/ecran) | Ridicat (aplicatii de GPS spoofing) | Aproape zero (limitat de propagarea fizica a undelor Wi-Fi) |
| **Versatilitate** | Nula (infrastructura fixa in pereti) | Mare | Foarte mare | Mare (echipamentul este portabil intr-un rucsac) |
| **Dependenta de internet extern** | Nu (de obicei folosesc retea intranet) | Da | Da (obligatoriu) | Nu (sistemul functioneaza complet izolat/in vid) |

### **Concluzie**

Privind aceasta comparatie, motivatia din spatele arhitecturii Pressync devine evidenta. Prin utilizarea unui router OpenWRT care ruleaza aplicatia local (eventual alaturi de un Raspberry Pi), am reusit sa preluam cele mai bune parti din solutiile concurente si sa le eliminam punctele slabe.

Fata de cartelele RFID, mutam identificatorul direct pe dispozitivul pe care utilizatorul nu il imprumuta nimanui (telefonul mobil) sub forma unui token JWT securizat, eliminand costurile cu hardware-ul fix. Spre deosebire de vulnerabilitatile scanarii QR sau ale sistemelor GPS, Pressync foloseste insasi fizica retelei ca metoda de validare: portalul captiv al routerului actioneaza ca un verificator strict al locatiei. Pentru a primi si trimite token-ul JWT, telefonul trebuie sa fie conectat fizic la reteaua Wi-Fi a salii. Deoarece raza de actiune a semnalului e oprita de peretii incaperii, frauda de la distanta devine imposibila.

Mai mult, pentru ca sistemul functioneaza "intr-un vid" (izolat si pre-impachetat intr-un container), elimina total dependenta de conexiunea la internet si garanteaza ca datele utilizatorilor raman doar pe masina locala, asigurand astfel intimitatea participantilor si oferind un produs "plug-and-play" cu adevarat superior pentru acest context de utilizare.

