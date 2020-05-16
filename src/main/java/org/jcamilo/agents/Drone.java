package org.jcamilo.agents;

import java.util.Arrays;
import java.util.List;

import org.jcamilo.deliverables.Deliverable;

public class Drone {
    private final static int ITEM_LIMIT = 3;
    private final static int MAX_DISTANCE_ALLOWED = 10;
    private final static char[] START_POINT = {0, 0, 'N'};

    private List<char[]> deliveryPaths;
    private boolean logDeliveryJourney;
    private List<Deliverable> deliverables;

    /**
     * Constructor for one delivery path
     * @param deliveryPath a char string with the drone actions
     * @param logDeliveryJourney indicates if each step on the path must be recorded
     */
    public Drone(Deliverable item, char[] deliveryPath, boolean logDeliveryJourney) {
        deliverables = Arrays.asList(item);
        deliveryPaths = Arrays.asList(deliveryPath);
        this.logDeliveryJourney = logDeliveryJourney;
    }

    /**
     * Constructor for several delivery paths
     * 
     * @param deliveryPaths      a List containing char string secuences with drone
     *                           actions
     * @param logDeliveryJourney indicates if each step on the path must be recorded
     * @throws ItemLimitException
     */
    public Drone(List<Deliverable> items, List<char[]> deliveryPaths, boolean logDeliveryJourney) throws ItemLimitException {
        if (items.size() > ITEM_LIMIT) {
            throw new ItemLimitException();
        }
        deliverables = items;
        this.deliveryPaths = deliveryPaths;
        this.logDeliveryJourney = logDeliveryJourney;
    }
}