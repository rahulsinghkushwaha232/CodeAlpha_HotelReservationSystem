# CodeAlpha_HotelReservationSystem 🏨

A clean, menu-driven Java console application developed for the CodeAlpha Software Development Internship. The Hotel Reservation System lets hotel staff search rooms, create bookings, simulate payments, manage cancellations and check-outs, and preserve reservation data between application runs.

**Developed by:** Rahul Singh Kushwaha  
**GitHub:** [@rahulsinghkushwaha232](https://github.com/rahulsinghkushwaha232)

---

## 📌 Project Overview

- **Internship:** CodeAlpha Software Development Internship
- **Project Name:** Hotel Reservation System
- **Language:** Java (JDK 17+)
- **Architecture:** Object-Oriented Programming (OOP)
- **Package:** `com.codealpha.hotelreservation`
- **Application Type:** Menu-driven console application

---

## ✨ Key Features

1. **Room Search with Date Validation**
   - Searches Standard, Deluxe, and Suite rooms for selected check-in and check-out dates.
   - Prevents double-booking by applying correct date-overlap validation.

2. **Complete Booking Workflow**
   - Captures guest contact details, calculates nights and total cost, then requests confirmation.
   - Generates a unique reservation ID and a simulated payment receipt.

3. **Reservation Management**
   - View individual reservation details or all reservations in an aligned console table.
   - Cancel active bookings and check out guests easily.

4. **File-Based Persistence**
   - Saves reservations in `reservations.dat` on exit.
   - Automatically restores saved reservations when the application starts.

5. **Robust Input Handling**
   - Validates room categories, menu choices, date format, and stay duration.
   - Handles invalid input without crashing the application.

---

## Features

- Fifteen preloaded Standard, Deluxe, and Suite rooms
- Date-aware room search that prevents overlapping reservations
- Booking summary and payment receipt simulation
- Cancellation, checkout, reservation lookup, and aligned reservation table
- Input validation for categories, numeric choices, and `dd-MM-yyyy` dates
- Automatic startup loading and Save & Exit persistence in `reservations.dat`

## OOP Concepts Used

- **Encapsulation:** private fields with focused public methods/accessors
- **Classes and composition:** `Hotel` manages `Room` and `Reservation` objects; a reservation joins a `Guest` and a `Room`
- **Enums:** room category, payment status, and reservation status are constrained types
- **Collections:** `ArrayList` stores the inventory and reservations
- **Exception handling:** scanner, date parsing, and file I/O errors are handled without crashing

## 📁 Project Structure

```text
CodeAlpha_HotelReservationSystem/
├── src/com/codealpha/hotelreservation/
│   ├── HotelReservationSystem.java
│   ├── Hotel.java
│   ├── Room.java
│   ├── Guest.java
│   ├── Reservation.java
│   ├── PaymentSimulator.java
│   └── enums for room, payment, and reservation status
├── README.md
├── LICENSE
└── HotelReservationSystem.jar
```

## Compile and Run

```powershell
javac -d out src/com/codealpha/hotelreservation/*.java
java -cp out com.codealpha.hotelreservation.HotelReservationSystem
```

Requires Java 17 or newer. The data file is created in the directory from which the application is run.

## Sample Console Output

```text
========== HOTEL RESERVATION SYSTEM ==========
1. Search Available Rooms
2. Book a Room
...
Choose an option: 2
Category (STANDARD/DELUXE/SUITE): DELUXE
Check-in date (dd-MM-yyyy): 10-10-2026
Check-out date (dd-MM-yyyy): 12-10-2026
Available room numbers: [201, 202, 203, 204, 205]
Select room number: 201
Guest name: Rahul Singh
Phone: 9876543210
Email: rahul@example.com

Booking summary: 2 night(s) x Rs. 4000.00 = Rs. 8000.00
Confirm booking and payment? (Y/N): Y

--- PAYMENT RECEIPT ---
Payment successful. Amount paid: Rs. 8000.00
Thank you for your payment.
Booking confirmed. Reservation ID: A1B2C3D4
```
