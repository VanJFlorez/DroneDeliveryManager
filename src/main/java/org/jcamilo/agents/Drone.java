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

    /**
     * Here we guide the drone to its destination. 
     * To ease the computations a mapping for the cardinal points was made:
     *  
     *      N               0
     *    W   E           3   1
     *      S               2
     * 
     * and we can compute de direction actions by doing modular arithmetic.
     */
    public void deliver(char[] deliveryPath) {
            final int mod = 4;
            int[] currLocation = {START_POINT[0], START_POINT[1], getIntDirection(START_POINT[2])};
            for(char action : deliveryPath) {
                switch (action) {
                    case 'A': // Adelante
                        switch (currLocation[2]) {
                            case 0:
                                currLocation[1] += 1;
                                break;
                            case 1:
                                currLocation[0] += 1;
                                break;
                            case 2:
                                currLocation[1] -= 1;
                                break;
                            case 3:
                                currLocation[0] -= 1;
                                break;
                        }
                        break;
                    case 'I': // Izquierda
                        currLocation[2] -= 1;
                        currLocation[2] %= mod;
                        break;
                    case 'D': // Derecha
                        currLocation[2] += 1;
                        currLocation[2] %= mod;
                        break;
                }
        }
    }

    /**
     * A little helper
     * @param direction takes a char and @return a number previously defined.
     */
    private static int getIntDirection(char direction) {
        switch (direction) {
            case 'N':
                return 0;
            case 'S':
                return 1;
            case 'W':
                return 2;
            case 'E':
                return 3;
            default:
                return -1;
        }
    }
}