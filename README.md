
# PresSync – Attendance & Event Management System

PresSync is a modern web-based platform designed to automate the attendance tracking process for recurring academic events such as lectures, labs, or seminars.
The system integrates event scheduling, recurring event generation, real-time presence registration, and user management in a clean and intuitive interface.

---

## 🚀 Features

### 👥 User Features

* Seamless login and identity initialization
* Automatic presence detection when an event is active
* Manual presence registration
* Personal statistics dashboard
* Secure account storage using hashed passwords
* Multi-device support via university network login

### 🛠 Admin Features

* Full CRUD for users
* Manage event categories (templates)
* Create, edit, archive events
* Generate recurring events automatically based on schedule rules
* Real-time attendance monitoring
* Manual override for presence
* User access restriction/unrestriction
* Event statistics and attendance reports

---

## 🧠 System Overview

PresSync operates using two key concepts:

### **1. Event Categories**

Templates defining:

* event start/end time
* attendance window
* repeating schedule (daily, weekly, monthly, custom day-of-week)
* instance generation rules

### **2. Event Instances**

Concrete events automatically created based on category rules.

Attendance is linked to event instances, not categories — ensuring accurate statistics over time.

---

## 🧱 Tech Stack

### **Backend**

* **Spring Boot 3+**
* **Java 17+**
* **Spring Data JPA**
* **Spring Security (JWT optional)**
* **MariaDB / MySQL**
* **Hibernate 6**
* **Maven**

### **Frontend**

* **Angular** (latest)
* **TypeScript**
* **Bootstrap / Tailwind (optional)**

### **Infrastructure**

* **Docker** (MariaDB + optional backend container)
* **OpenWRT Router + Nodogsplash** for automatic redirect inside campus WiFi
* **Raspberry Pi** as server host (lightweight & portable)

---

## 🗄 Database Schema (ERD)

The core database structure contains 4 main tables:

```
User
CategorieEveniment
Eveniment
Attendance
```

Relationships:

* CategorieEveniment → Eveniment: 1-to-many
* User → Attendance → Eveniment: many-to-many through Attendance

---


## 📊 Roadmap

* [ ] JWT authentication
* [ ] Student QR-based quick registration
* [ ] Email notifications for event updates
* [ ] Reports export: PDF / CSV
* [ ] Admin dashboard improvements
* [ ] Mobile-friendly frontend UI

