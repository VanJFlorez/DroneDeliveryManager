package org.jcamilo.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jcamilo.common.Constants;

public class Drone implements Runnable {
    private final static int itemLimit = Constants.DRONE_ITEM_LIMIT;
    private final static int maxDistanceAllowed = Constants.DRONE_MAX_DISTANCE_ALLOWED;
    private final static String reportSplitMessage = Constants.REPORT_SPLIT_MESSAGE;

    // One drone has many deliveries (one for each line int the deliveries file), 
    // and each delivery can have several lunchs aka deliverables (upto @itemLimit).
    // 
    // One drone will have many deliveries (orders) as many entries has this
    // list.
    private List<Order> deliveries;
    private File reports;
    private State state;

    public Drone(File deliveries, File reports) throws IOException {
        this.deliveries = new LinkedList<>();
        
        String deliveryDescr;
        BufferedReader reader = new BufferedReader(new FileReader(deliveries));
        while((deliveryDescr = reader.readLine()) != null) {
            Order order = new Order(deliveryDescr);
            this.deliveries.add(order);
        }
        reader.close();
        
        state = new State();
        this.reports = reports;
    }
    
    /**
     * Deliver all orders.
     * @return
     */
    @Override
    public void run() {
        log(reportSplitMessage);
        for(Order order : deliveries) {
            try {
                deliverOne(order);
            } catch(ItemLimitException e) {
                System.out.println("This order exceedes the current limit of items (" + itemLimit + ")");
                System.out.println("Ommiting this delivery.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Here we guide the drone to its destination. To ease the computations a
     * mapping for the cardinal points was made:
     * 
     * N 0 W E 3 1 S 2
     * 
     * and we can compute de direction actions by doing modular arithmetic.
     * 
     * @return void, but writes to reportsOut the drone's final location
     * @throws ItemLimitException
     * @throws IOException
     */
    public void deliverOne(Order order) throws ItemLimitException {
        returnToStore();

        if (order.getItemQtty() > itemLimit) {
            // TODO: note the exception handling here
            throw new ItemLimitException();
        }

        for(char action :  order.getActionPath().toCharArray()) {
            state.performAction(action);

            if(state.getDistanceFromStore() > maxDistanceAllowed) {
                // TODO: do something if drone reachs the max distance allowed
                // no exceptions involved
            }
        }
        log(state.getReadableState());
    }

    public State getCurrentState() {
        return state;
    }
    
    public void returnToStore() {
        state = new State();
    }

    private void log(String str) {
        try (FileWriter fw = new FileWriter(reports, true);) {
            fw.write(str + "\n"); 
        } catch (IOException e) {
            System.out.println("The message cannot be written to the log.");
            e.printStackTrace();
        }
    }

    public static class State {
        private static final char[] charDirections = Constants.DIRECTIONS;
        private static final String[] spanishCoord = Constants.DIRECTIONS_ES;
        private static final String outputTemplate = Constants.OUTPUT_LOG_TEMPLATE;

        private int xPos;
        private int yPos;
        private int direction;

        public State() {
            xPos = Constants.START_POINT[0];
            yPos = Constants.START_POINT[1];
            direction = Constants.START_POINT[2];
        }

        public State(int xPos, int yPos, int direction) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.direction = direction;
        }

        public void performAction(char action) {
            switch (action) {
                case 'A': // Adelante
                    switch (direction) {
                        case 0:
                            yPos += 1;
                            break;
                        case 1:
                            xPos += 1;
                            break;
                        case 2:
                            yPos -= 1;
                            break;
                        case 3:
                            xPos -= 1;
                            break;
                    }
                    break;
                case 'I': // Izquierda
                    direction -= 1;
                    if (direction == -1)
                        direction = 3;
                    break;
                case 'D': // Derecha
                    direction += 1;
                    direction %= charDirections.length;
                    break;
            }
        }

        public String getReadableState() {
            return String.format(outputTemplate, xPos, yPos, spanishCoord[direction]);
        }

        /**
         * @return the current drone's direction represented by a char
         */
        public char getCharDirection() {
            return charDirections[direction];
        }

        /**
         * @return drone's manhattan distance from store.
         */
        public int getDistanceFromStore() {
            int xStore = Constants.START_POINT[0];
            int yStore = Constants.START_POINT[1];
            return Math.abs(xStore - xPos) + Math.abs(yStore - yPos);

        }

        public int getXPos() { return xPos; }
        public int getYPos() { return yPos; }
        public int getDirection() { return direction; }
    }
}