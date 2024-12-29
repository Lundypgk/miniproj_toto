package com.mini.project.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Ticket {

    private final String ticketId;  // Unique identifier for each ticket
    private String betType;         // Type of ticket (e.g., Ordinary, System 7)
    private LocalDate purchaseDate; // Date of ticket purchase
    private List<Integer> numbers;  // List of selected numbers
    private double betAmount;       // Bet amount
    private double prizeAmount;     // Prize amount (if it's in the past)

    // Constructor
    public Ticket(String betType, LocalDate purchaseDate, List<Integer> numbers, double betAmount) {
        this.ticketId = UUID.randomUUID().toString(); // Generate unique ID
        this.betType = betType;
        this.purchaseDate = purchaseDate;
        this.numbers = numbers;
        this.betAmount = betAmount;
        this.prizeAmount = 0.0; // Default prize amount
    }

    // Getters and Setters
    public String getTicketId() {
        return ticketId;
    }

    public String getBetType() {
        return betType;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public double getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(double prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    // Check if the ticket is for a past draw
    public boolean isPastDraw() {
        return purchaseDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", betType='" + betType + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", numbers=" + numbers +
                ", betAmount=" + betAmount +
                ", prizeAmount=" + prizeAmount +
                '}';
    }
}
