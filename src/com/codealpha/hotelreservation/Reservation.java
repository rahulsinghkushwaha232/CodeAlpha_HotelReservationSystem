package com.codealpha.hotelreservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/** Connects a guest, room, dates, payment, and booking lifecycle information. */
public class Reservation {
    private final String reservationId; private final Room room; private final Guest guest; private final LocalDate checkInDate, checkOutDate; private final long numberOfNights; private final double totalAmount;
    private PaymentStatus paymentStatus; private ReservationStatus status;
    public Reservation(Room room, Guest guest, LocalDate checkInDate, LocalDate checkOutDate) { this(UUID.randomUUID().toString().substring(0, 8).toUpperCase(), room, guest, checkInDate, checkOutDate, PaymentStatus.PENDING, ReservationStatus.BOOKED); }
    /** Constructor used when restoring an existing reservation from storage. */
    public Reservation(String id, Room room, Guest guest, LocalDate in, LocalDate out, PaymentStatus payment, ReservationStatus status) {
        reservationId = id; this.room = room; this.guest = guest; checkInDate = in; checkOutDate = out; numberOfNights = ChronoUnit.DAYS.between(in, out); totalAmount = numberOfNights * room.getPricePerNight(); paymentStatus = payment; this.status = status;
    }
    public String getReservationId() { return reservationId; } public Room getRoom() { return room; } public Guest getGuest() { return guest; } public LocalDate getCheckInDate() { return checkInDate; } public LocalDate getCheckOutDate() { return checkOutDate; }
    public long getNumberOfNights() { return numberOfNights; } public double getTotalAmount() { return totalAmount; } public PaymentStatus getPaymentStatus() { return paymentStatus; } public void setPaymentStatus(PaymentStatus value) { paymentStatus = value; }
    public ReservationStatus getStatus() { return status; } public void setStatus(ReservationStatus value) { status = value; }
}
