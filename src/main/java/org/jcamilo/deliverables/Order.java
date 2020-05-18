package org.jcamilo.deliverables;

import java.util.LinkedList;
import java.util.List;

public class Order {
    private String actionPath;
    private List<Deliverable> deliverables;

    public Order() {
        this.actionPath = "";
        this.deliverables = new LinkedList<>();
    }

    public Order(String deliveryDescription) {
        String[] order = deliveryDescription.split(" ");
        this.deliverables = new LinkedList<>();
        
        if (order.length == 0) {
            this.actionPath = "";
        } else {
            this.actionPath = order[0];
            for(int i = 1; i < order.length; i++) {
                deliverables.add(new Lunch(order[i]));
            }
        }
    }

    public String getActionPath() {
        return actionPath;
    }

    public List<Deliverable> getPayload() {
        return deliverables;
    }

    public int getItemQtty() {
        return deliverables.size();
    }
}