# **Descrierea sistemului**

Sistemul **PresSync** se ocupă cu gestionarea evenimentelor, a participanților și a prezențelor în cadrul acestora. Sistemul permite administrarea utilizatorilor, configurarea evenimentelor, înregistrarea participanților și marcarea automată sau manuală a prezenței.

---

# **1\. Actori**

**Super Admin** – gestionează întregul sistem, creează administratori și controlează permisiunile globale.

**Admin** – gestionează evenimente, utilizatori simpli, și vizualizează rapoarte.

**User** – participă la evenimente și își poate vizualiza istoricul prezențelor.

---

# **2\. Cerințe funcționale**

## **F1. Înregistrare și autentificare utilizator**

**F1.1** Cere categoria utilizatorului (Super Admin / Admin / User).

**F1.2** În funcție de categoria selectată, afișează un formular specific.

**F1.3** Verifică datele introduse dpdv sintactic și semantic.

**F1.4** Creează un cont nou cu username unic și parolă.

**F1.5** Memorează datele contului în baza de date.

**F1.6** Afișează profilul utilizatorului după înregistrare.

**F1.7** Dacă datele sunt incorecte, marchează câmpurile eronate și afișează un mesaj.

**F1.8** La autentificare, cere username și parolă.

**F1.9** Afișează opțiunile specifice rolului utilizatorului.

**F1.10** Reafișează pagina principală dacă utilizatorul anulează procesul.

---

## **F2. Gestionare evenimente**

**F2.1** Afișează un formular pentru crearea unui eveniment (titlu, dată, locație, descriere, tip).

**F2.2** Verifică completarea corectă a datelor.

**F2.3** Permite modificarea unui eveniment existent.

**F2.4** Permite ștergerea unui eveniment, după confirmarea actorului.

**F2.5** Memorează datele evenimentelor în sistem.

**F2.6** Afișează lista tuturor evenimentelor.

**F2.7** Afișează detaliile unui eveniment selectat.

---

## **F3. Gestionare utilizatori**

**F3.1** Super Admin-ul poate crea administratori.

**F3.4** Permite dezactivarea/reactivarea unui utilizator.

**F3.5** Afișează lista utilizatorilor existenți.

**F3.6** Afișează profilul complet al unui utilizator selectat.

---

## **F4. Gestionare prezențe**

**F4.1** Permite selectarea unui eveniment pentru gestionarea prezențelor.

**F4.2** Permite marcarea manuală a prezenței unui utilizator.

**F4.3** Verifică dacă utilizatorul a fost deja marcat prezent.

**F4.4** Memorează prezența utilizatorilor.

**F4.5** Afișează lista participanților prezenți și absenți.

---

## **F5. Notificări și rapoarte**

**F5.1** Generează rapoarte statistice privind participarea.

**F5.2** Permite descărcarea rapoartelor în format PDF sau CSV.

**F5.3** Afișează istoricul prezențelor pentru fiecare utilizator.

---

# **3\. Cerințe nefuncționale**

**CN1. Usabilitate** – Sistem intuitiv, cu interfețe grafice ușor de utilizat.

**CN2. Robustete** – Sistemul validează datele introduse și oferă mesaje de eroare clare.

**CN3. Implementare** – Sistemul trebuie implementat în Java.

**CN4. Portabilitate** – Sistemul trebuie să funcționeze pe orice sistem de operare.

**CN5. Securitate** – Parole criptate, autentificare sigură, protecția datelor utilizatorilor.

---

