package org.jcamilo.deliverables;

// extend from Deliverable class that will fields such as recipientName or address
public class Lunch extends Deliverable {
    private String orderID;
    private float price;
    private int priority;
    private String description;

    public Lunch(String str) {}

    public Lunch(String orderID, float price, int priority, String description) {
        this.orderID = orderID;
        this.price = price;
        this.priority = priority;
        this.description = description;
    }

    public String getOrderID() {
        return orderID;
    }

    public float getPrice() {
        return price;
    }

    public int getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }
}