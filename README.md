# 🚗💨 Car Rental Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)

![JavaFX](https://img.shields.io/badge/JavaFX-UI_Toolkit-blue?style=for-the-badge)

![Status](https://img.shields.io/badge/Status-Completed-green?style=for-the-badge)
---

## 🛠 Project Overview

This isn't just a basic CRUD app. It’s a multi-role management system designed to handle real-world rental logic—from automated tax calculations to "auto-returning" expired rentals.

### The "Brains" of the System:
* **Relational CSV Persistence:** A custom `DataManager` serves as the database. It doesn't just save text; it reconstructs complex object relationships (linking Bookings to specific Vehicles and Customers) on startup.
* **Polymorphic Tax Engine:** Using a `Taxable` interface, the system dynamically calculates costs based on the vehicle type (30% for Cars, 15% for Vans, 10% for Bikes).
* **Defensive Error Handling:** Instead of generic crashes, the app uses a custom suite of exceptions (`InvalidDateException`, `VehicleNotAvailableException`, etc.) to guide the user.



---

## 🚀 Core Functionalities

### 👑 Admin Control
- **Dynamic Fleet Management:** Add specialized vehicles with unique traits (e.g., Load Capacity for Vans, Transmission for Cars).
- **Fleet Integrity:** A safety-lock prevents admins from deleting vehicles that are currently "Rented."
- **Financial Tracking:** Real-time revenue aggregation based on historical transaction data.

### 👤 Customer Features
- **Price-Optimized Browsing:** Implements `Comparable<Vehicle>` to automatically sort the fleet from cheapest to most expensive.
- **Reservation Workflow:** Users pick dates, the system validates availability, calculates total cost (rate + tax), and generates a unique `BookingID`.
- **Auto-Maintenance:** On launch, the system checks `LocalDate` against active bookings. If a rental is past due, it automatically marks the vehicle as "Available."

---

## 🏗 System Architecture

The app follows the **MVC (Model-View-Controller)** pattern to separate the UI (FXML) from the heavy-lifting logic.

### OOP Pillars Applied:
- **Abstraction:** The `Vehicle` class acts as an abstract blueprint for all fleet types.
- **Composition:** `Customer` objects "own" an `Account` object, separating login security from user profile data.
- **Singleton Pattern:** The `DataManager` is implemented as a Singleton to ensure a single point of truth for file I/O operations.



---

## 📂 Project Structure

```text
src/
├── model/               # The Logic Layer
│   ├── Taxable.java     // Financial interface
│   ├── Vehicle.java     // Abstract base class
│   ├── Car/Bike/Van.java // Specialized implementations
│   ├── DataManager.java // Singleton I/O handler
│   └── CustomExceptions.java
│
├── UI/                  # The Presentation Layer
│   ├── JavaFx.java      // Entry point
│   ├── *Controller.java // Scene logic
│   └── *.fxml           // View layouts
│
└── data/                # The Persistence Layer (Auto-generated)
    ├── customers.csv
    ├── vehicles.csv
    └── bookings.csv
