# Hotel Reservation System

A menu-driven Java console application built for the CodeAlpha internship. It manages room availability, guest bookings, payments, cancellations, checkout, and local reservation persistence.

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
Guest name: Priya Sharma
Phone: 9876543210
Email: priya@example.com

Booking summary: 2 night(s) x Rs. 4000.00 = Rs. 8000.00
Confirm booking and payment? (Y/N): Y

--- PAYMENT RECEIPT ---
Payment successful. Amount paid: Rs. 8000.00
Thank you for your payment.
Booking confirmed. Reservation ID: A1B2C3D4
```
