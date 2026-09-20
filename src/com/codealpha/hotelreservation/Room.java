package com.codealpha.hotelreservation;

/** Represents one hotel room and its basic inventory information. */
public class Room {
    private int roomNumber; private RoomCategory category; private double pricePerNight; private boolean isAvailable;
    public Room(int roomNumber, RoomCategory category, double pricePerNight) { this.roomNumber = roomNumber; this.category = category; this.pricePerNight = pricePerNight; isAvailable = true; }
    public int getRoomNumber() { return roomNumber; } public void setRoomNumber(int value) { roomNumber = value; }
    public RoomCategory getCategory() { return category; } public void setCategory(RoomCategory value) { category = value; }
    public double getPricePerNight() { return pricePerNight; } public void setPricePerNight(double value) { pricePerNight = value; }
    public boolean isAvailable() { return isAvailable; } public void setAvailable(boolean available) { isAvailable = available; }
    /** Returns a concise room description for console displays. */
    @Override public String toString() { return String.format("Room %d | %-8s | Rs. %.2f/night | %s", roomNumber, category, pricePerNight, isAvailable ? "Available" : "Reserved"); }
}
