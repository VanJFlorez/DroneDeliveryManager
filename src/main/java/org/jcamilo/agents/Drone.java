package org.jcamilo.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jcamilo.common.Constants;
import org.jcamilo.deliverables.Deliverable;

public class Drone implements Runnable {
    private final static int ITEM_LIMIT = 3;
    private final static int MAX_DISTANCE_ALLOWED = 10;
    public static final String reportSplitMessage = Constants.REPORT_SPLIT_MESSAGE;

    private List<String> droneActions;
    private List<Deliverable> deliverables;
    private File reports;
    private State state;

    public Drone(File deliveries, File reports) throws IOException {
        droneActions = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(deliveries));
        String actions;
        while((actions = reader.readLine()) != null) {
            droneActions.add(actions);
        }
        state = new State();
        this.reports = reports;
        reader.close();
    }

    /**
     * Supply all deliverables.
     * @return
     */
    @Override
    public void run() {
        log(reportSplitMessage);
        for(String actions : droneActions) {                
            deliverOne(actions);
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
     * @throws IOException
     */
    public void deliverOne(String deliveryPath) {
        returnToStore();
        for(char action : deliveryPath.toCharArray()) {
            state.performAction(action);
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

        public int getXPos() { return xPos; }
        public int getYPos() { return yPos; }
        public int getDirection() { return direction; }

    }
}