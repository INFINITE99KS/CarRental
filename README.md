# 🚗💨 Car Rental Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-UI_Toolkit-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-green?style=for-the-badge)

> **"Rent a car, enjoy the ride!"** 🌟  
> A robust, Object-Oriented desktop application built for the **CSE 331s Advanced Computer Programming** course.

---

## 📖 About The Project

Welcome to the **Car Rental Management System**! This application streamlines the process of renting vehicles. It features a dual-interface design: one for **Administrators** to manage the fleet, and one for **Customers** to browse and book their dream rides.

We built this using strict **OOP principles**, ensuring the code is clean, modular, and scalable. Plus, it remembers everything you do thanks to our custom CSV data persistence layer! 💾

---

## ✨ Key Features

### 👑 For Admins
* **Fleet Management:** Add new Cars, Bikes, or Vans with specific details (e.g., Helmet included? Automatic?).
* **Live Updates:** Remove old vehicles or update stock instantly.
* **Business Intelligence:** View all active bookings and customer history.
* **Secure Access:** Password-protected admin dashboard.

### 👤 For Customers
* **Smart Sorting:** Vehicles are automatically sorted by **price (low to high)** so you find the best deals first! 💸
* **Easy Booking:** Pick your dates, confirm your ride, and go.
* **Booking History:** View past trips and cancel active reservations if plans change.
* **Real-Time Status:** You can't book a car that's already taken! (Thanks to our `VehicleNotAvailableException`).

---

## 🛠️ Tech Stack & Concepts

We didn't just write code; we engineered a solution using advanced concepts:

* **Language:** Java 17+
* **GUI Framework:** JavaFX (FXML)
* **Architecture:** Model-View-Controller (MVC)
* **OOP Pillars:**
    * 🧩 **Polymorphism:** `Vehicle` behaves differently as `Car`, `Bike`, or `Van`.
    * 🔒 **Encapsulation:** Private fields with secure Getters/Setters.
    * 🧬 **Inheritance:** All vehicle types inherit from the abstract `Vehicle` class.
    * 📦 **Abstraction:** `calculateRentalCost()` is abstract, forcing specific implementation.
* **Data Persistence:** Custom File I/O using CSV files (Excel compatible!).

---

## 📸 Screenshots

| **Login Screen** | **Admin Dashboard** |
|:---:|:---:|
| <img src="https://via.placeholder.com/400x300?text=Login+Screen" width="400"> | <img src="https://via.placeholder.com/400x300?text=Admin+Dashboard" width="400"> |

| **Customer Dashboard** | **Booking Dialog** |
|:---:|:---:|
| <img src="https://via.placeholder.com/400x300?text=Customer+View" width="400"> | <img src="https://via.placeholder.com/400x300?text=Booking+Popup" width="400"> |

---

## 📂 Project Structure

Here is a peek under the hood at how we organized our files:

```text
src/
├── 📦 model/            # The Brains (Logic & Data)
│   ├── Account.java
│   ├── Vehicle.java (Abstract)
│   ├── Car.java / Bike.java / Van.java
│   ├── Customer.java
│   ├── Booking.java
│   ├── DataManager.java (CSV Handling)
│   └── CustomExceptions.java
│
├── 🖥️ UI/               # The Beauty (Visuals & Controllers)
│   ├── JavaFx.java (Main Entry)
│   ├── DashboardController.java (Login)
│   ├── AdminDashboardController.java
│   ├── CustomerDashboardController.java
│   └── FXML Files (.fxml)
│
└── 📁 data/             # The Memory (Saved CSVs)
    ├── customers.csv
    ├── vehicles.csv
    └── bookings.csv
