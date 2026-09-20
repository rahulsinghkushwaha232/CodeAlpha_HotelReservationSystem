package com.codealpha.hotelreservation;

/** Simulates a successful payment gateway transaction and prints a receipt. */
public class PaymentSimulator {
    /** Processes a positive payment amount and returns whether it succeeded. */
    public boolean processPayment(double amount) {
        if (amount <= 0) { System.out.println("Payment failed: amount must be greater than zero."); return false; }
        System.out.println("\n--- PAYMENT RECEIPT ---"); System.out.printf("Payment successful. Amount paid: Rs. %.2f%n", amount); System.out.println("Thank you for your payment."); return true;
    }
}
