package com.codealpha.hotelreservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Manages room inventory, reservation rules, payment processing, and storage. */
public class Hotel {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final List<Room> rooms = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final PaymentSimulator paymentSimulator = new PaymentSimulator();

    public Hotel() { loadRooms(); }

    /** Pre-loads a fixed inventory of fifteen rooms. */
    private void loadRooms() {
        for (int number = 101; number <= 105; number++) rooms.add(new Room(number, RoomCategory.STANDARD, 2500));
        for (int number = 201; number <= 205; number++) rooms.add(new Room(number, RoomCategory.DELUXE, 4000));
        for (int number = 301; number <= 305; number++) rooms.add(new Room(number, RoomCategory.SUITE, 6500));
    }

    /** Finds category-matching rooms without an active reservation overlapping the requested stay. */
    public List<Room> searchAvailableRooms(RoomCategory category, LocalDate checkIn, LocalDate checkOut) {
        List<Room> matches = new ArrayList<>();
        for (Room room : rooms) if (room.getCategory() == category && !hasConflict(room, checkIn, checkOut)) matches.add(room);
        return matches;
    }

    /** Books an available room after simulated payment; returns null if unavailable or payment fails. */
    public Reservation bookRoom(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        if (!rooms.contains(room) || !checkOut.isAfter(checkIn) || hasConflict(room, checkIn, checkOut)) return null;
        Reservation reservation = new Reservation(room, guest, checkIn, checkOut);
        if (!paymentSimulator.processPayment(reservation.getTotalAmount())) return null;
        reservation.setPaymentStatus(PaymentStatus.PAID);
        reservations.add(reservation);
        refreshRoomAvailability(room);
        return reservation;
    }

    /** Cancels a currently booked reservation and refreshes its room availability state. */
    public boolean cancelReservation(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null || reservation.getStatus() != ReservationStatus.BOOKED) return false;
        reservation.setStatus(ReservationStatus.CANCELLED); refreshRoomAvailability(reservation.getRoom()); return true;
    }

    /** Returns a reservation by ID, or null when it does not exist. */
    public Reservation viewReservation(String reservationId) { return findReservation(reservationId); }

    /** Prints every reservation in a clean aligned table. */
    public void viewAllReservations() {
        if (reservations.isEmpty()) { System.out.println("No reservations found."); return; }
        System.out.printf("%-10s %-18s %-6s %-10s %-12s %-12s %-11s %-8s%n", "ID", "Guest", "Room", "Category", "Check-in", "Check-out", "Status", "Payment");
        System.out.println("---------------------------------------------------------------------------------------------");
        for (Reservation r : reservations) System.out.printf("%-10s %-18.18s %-6d %-10s %-12s %-12s %-11s %-8s%n", r.getReservationId(), r.getGuest().getName(), r.getRoom().getRoomNumber(), r.getRoom().getCategory(), r.getCheckInDate().format(DATE_FORMAT), r.getCheckOutDate().format(DATE_FORMAT), r.getStatus(), r.getPaymentStatus());
    }

    /** Marks a booked reservation as completed. */
    public boolean checkOutGuest(String reservationId) {
        Reservation reservation = findReservation(reservationId);
        if (reservation == null || reservation.getStatus() != ReservationStatus.BOOKED) return false;
        reservation.setStatus(ReservationStatus.COMPLETED); refreshRoomAvailability(reservation.getRoom()); return true;
    }

    /** Finds a room from the hotel inventory. */
    public Room getRoomByNumber(int roomNumber) { for (Room room : rooms) if (room.getRoomNumber() == roomNumber) return room; return null; }

    /** Saves all reservation fields to a comma-separated file using try-with-resources. */
    public void saveReservations(Path file) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("id,room,name,phone,email,checkIn,checkOut,payment,status"); writer.newLine();
            for (Reservation r : reservations) {
                writer.write(String.join(",", encode(r.getReservationId()), String.valueOf(r.getRoom().getRoomNumber()), encode(r.getGuest().getName()), encode(r.getGuest().getPhone()), encode(r.getGuest().getEmail()), r.getCheckInDate().format(DATE_FORMAT), r.getCheckOutDate().format(DATE_FORMAT), r.getPaymentStatus().name(), r.getStatus().name())); writer.newLine();
            }
            System.out.println("Reservations saved to " + file + ".");
        } catch (IOException exception) { System.out.println("Could not save reservations: " + exception.getMessage()); }
    }

    /** Loads stored reservations when a file is present, handling malformed data safely. */
    public void loadReservations(Path file) {
        if (!Files.exists(file)) return;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            reader.readLine(); String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",", -1); if (data.length != 9) continue;
                Room room = getRoomByNumber(Integer.parseInt(data[1]));
                if (room != null) reservations.add(new Reservation(decode(data[0]), room, new Guest(decode(data[2]), decode(data[3]), decode(data[4])), LocalDate.parse(data[5], DATE_FORMAT), LocalDate.parse(data[6], DATE_FORMAT), PaymentStatus.valueOf(data[7]), ReservationStatus.valueOf(data[8])));
            }
            for (Room room : rooms) refreshRoomAvailability(room);
            System.out.println(reservations.size() + " reservation(s) loaded from " + file + ".");
        } catch (IOException | IllegalArgumentException exception) { System.out.println("Could not load reservations: " + exception.getMessage()); }
    }

    /** Two stays overlap when each begins before the other's checkout; checkout day is reusable. */
    private boolean hasConflict(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (Reservation r : reservations) if (r.getRoom() == room && r.getStatus() == ReservationStatus.BOOKED && checkIn.isBefore(r.getCheckOutDate()) && checkOut.isAfter(r.getCheckInDate())) return true;
        return false;
    }
    private Reservation findReservation(String id) { for (Reservation r : reservations) if (r.getReservationId().equalsIgnoreCase(id.trim())) return r; return null; }
    private void refreshRoomAvailability(Room room) { room.setAvailable(!hasConflict(room, LocalDate.MIN, LocalDate.MAX)); }
    private String encode(String text) { return java.util.Base64.getUrlEncoder().encodeToString(text.getBytes(java.nio.charset.StandardCharsets.UTF_8)); }
    private String decode(String text) { return new String(java.util.Base64.getUrlDecoder().decode(text), java.nio.charset.StandardCharsets.UTF_8); }
}
