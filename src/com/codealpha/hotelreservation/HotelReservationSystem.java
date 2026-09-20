package com.codealpha.hotelreservation;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** Console entry point for the Hotel Reservation System. */
public class HotelReservationSystem {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final Path DATA_FILE = Path.of("reservations.dat");
    private final Scanner scanner = new Scanner(System.in);
    private final Hotel hotel = new Hotel();
    public static void main(String[] args) { new HotelReservationSystem().run(); }

    /** Loads data and displays the menu until the user saves and exits. */
    private void run() {
        hotel.loadReservations(DATA_FILE); boolean running = true;
        while (running) {
            printMenu();
            switch (readInt("Choose an option: ")) {
                case 1 -> searchRooms(); case 2 -> bookRoom(); case 3 -> cancelReservation(); case 4 -> viewReservation(); case 5 -> hotel.viewAllReservations(); case 6 -> checkOutGuest();
                case 7 -> { hotel.saveReservations(DATA_FILE); running = false; System.out.println("Goodbye!"); }
                default -> System.out.println("Please enter a number from 1 to 7.");
            }
        }
    }
    /** Collects filters and displays matching rooms. */
    private void searchRooms() {
        RoomCategory category = readCategory(); LocalDate[] stay = readStayDates();
        List<Room> rooms = hotel.searchAvailableRooms(category, stay[0], stay[1]);
        if (rooms.isEmpty()) { System.out.println("No " + category + " rooms are available for those dates."); return; }
        System.out.println("\nAvailable rooms:"); for (Room room : rooms) System.out.println(room);
    }
    /** Collects guest details, shows a quote, and requests confirmation before payment. */
    private void bookRoom() {
        RoomCategory category = readCategory(); LocalDate[] stay = readStayDates(); List<Room> available = hotel.searchAvailableRooms(category, stay[0], stay[1]);
        if (available.isEmpty()) { System.out.println("No matching rooms are available."); return; }
        System.out.println("Available room numbers: " + available.stream().map(r -> String.valueOf(r.getRoomNumber())).toList());
        Room room = hotel.getRoomByNumber(readInt("Select room number: "));
        if (room == null || !available.contains(room)) { System.out.println("That room is not available for the selected dates."); return; }
        Guest guest = new Guest(readNonBlank("Guest name: "), readNonBlank("Phone: "), readNonBlank("Email: "));
        long nights = ChronoUnit.DAYS.between(stay[0], stay[1]); double total = nights * room.getPricePerNight();
        System.out.printf("%nBooking summary: %d night(s) x Rs. %.2f = Rs. %.2f%n", nights, room.getPricePerNight(), total);
        if (!readNonBlank("Confirm booking and payment? (Y/N): ").equalsIgnoreCase("Y")) { System.out.println("Booking cancelled by user."); return; }
        Reservation reservation = hotel.bookRoom(guest, room, stay[0], stay[1]);
        System.out.println(reservation == null ? "Booking could not be completed." : "Booking confirmed. Reservation ID: " + reservation.getReservationId());
    }
    private void cancelReservation() { System.out.println(hotel.cancelReservation(readNonBlank("Reservation ID: ")) ? "Reservation cancelled." : "Only an active booking can be cancelled."); }
    private void checkOutGuest() { System.out.println(hotel.checkOutGuest(readNonBlank("Reservation ID: ")) ? "Guest checked out successfully." : "Only an active booking can be checked out."); }
    /** Prints all details of one reservation. */
    private void viewReservation() {
        Reservation r = hotel.viewReservation(readNonBlank("Reservation ID: "));
        if (r == null) { System.out.println("Reservation not found."); return; }
        System.out.printf("%n--- RESERVATION DETAILS ---%nID: %s%nGuest: %s%nRoom: %s%nCheck-in: %s%nCheck-out: %s%nNights: %d%nTotal: Rs. %.2f%nStatus: %s%nPayment: %s%n", r.getReservationId(), r.getGuest(), r.getRoom(), r.getCheckInDate().format(DATE_FORMAT), r.getCheckOutDate().format(DATE_FORMAT), r.getNumberOfNights(), r.getTotalAmount(), r.getStatus(), r.getPaymentStatus());
    }
    private RoomCategory readCategory() { while (true) try { return RoomCategory.valueOf(readNonBlank("Category (STANDARD/DELUXE/SUITE): ").toUpperCase(Locale.ROOT)); } catch (IllegalArgumentException exception) { System.out.println("Invalid category. Please choose STANDARD, DELUXE, or SUITE."); } }
    private LocalDate[] readStayDates() { while (true) { LocalDate in = readDate("Check-in date (dd-MM-yyyy): "); LocalDate out = readDate("Check-out date (dd-MM-yyyy): "); if (out.isAfter(in)) return new LocalDate[]{in, out}; System.out.println("Check-out must be after check-in. Please try again."); } }
    private LocalDate readDate(String prompt) { while (true) try { return LocalDate.parse(readNonBlank(prompt), DATE_FORMAT); } catch (DateTimeParseException exception) { System.out.println("Invalid date. Use dd-MM-yyyy, for example 25-12-2026."); } }
    private int readInt(String prompt) { while (true) try { System.out.print(prompt); int value = scanner.nextInt(); scanner.nextLine(); return value; } catch (InputMismatchException exception) { System.out.println("Please enter a whole number."); scanner.nextLine(); } }
    private String readNonBlank(String prompt) { while (true) { System.out.print(prompt); String value = scanner.nextLine().trim(); if (!value.isEmpty()) return value; System.out.println("This field cannot be blank."); } }
    private void printMenu() { System.out.println("\n========== HOTEL RESERVATION SYSTEM =========="); System.out.println("1. Search Available Rooms\n2. Book a Room\n3. Cancel a Reservation\n4. View Reservation Details\n5. View All Reservations\n6. Check-out Guest\n7. Save & Exit"); }
}
