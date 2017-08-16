package com.example.rakshit.firebaseauth;

public class POJOExpense
{
    private double cost;
    private int category;
    private int payment;
    private String name;
    private long timestamp;

    public POJOExpense(String name, double cost, int category, int payment, long timestamp)
    {
        this.cost = cost;
        this.category = category;
        this.payment = payment;
        this.name = name;
        this.timestamp = timestamp;
    }

    public double getCost() {
        return cost;
    }

    public int getCategory() {
        return category;
    }

    public int getPayment() {
        return payment;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
