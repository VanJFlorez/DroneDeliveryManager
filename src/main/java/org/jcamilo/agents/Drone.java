package org.jcamilo.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jcamilo.deliverables.Deliverable;

public class Drone implements Runnable {
    private final static int ITEM_LIMIT = 3;
    private final static int MAX_DISTANCE_ALLOWED = 10;
    public static final String REPORT_SPLIT_MESSAGE = "== Reporte de entregas ==";


    private List<String> droneActions;
    private List<Deliverable> deliverables;

    private File reportsOut;

    public Drone(File deliveries, File reportsOut) throws IOException {
        droneActions = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(deliveries));
        String actions;
        while((actions = reader.readLine()) != null) {
            droneActions.add(actions);
        }

        this.reportsOut = reportsOut;
        reader.close();
    }

    /**
     * Supply all deliverables.
     * @return
     */
    @Override
    public void run() {
        log(REPORT_SPLIT_MESSAGE);
        for(String actions : droneActions) {                
            deliverOne(actions.toCharArray());
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
    private void deliverOne(char[] deliveryPath) {
        State state = new State();
        for(char action : deliveryPath) {
            state.performAction(action);
        }
        log(state.getReadableState());
    }

    private void log(String str) {
        try (FileWriter fw = new FileWriter(reportsOut, true);) {
            fw.write(str + "\n"); 
        } catch (IOException e) {
            System.out.println("The message cannot be written to the log.");
            e.printStackTrace();
        }
        
    }

    public static class State {
        private static final char[] charDirections = {'N', 'E', 'S', 'W'};
        private static final String[] spanishCoord = {"Norte", "Oriente", "Sur", "Occidente"};

        private int xPos;
        private int yPos;
        private int direction;

        public State() {
            xPos = 0;
            yPos = 0;
            direction = 0;
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

        public int[] getCurrentStateAsArray() {
            return new int[]{xPos, yPos, direction};
        }

        public String getReadableState() {
            StringBuilder str = new StringBuilder();
            str.append("(");
            str.append(xPos);
            str.append(", ");
            str.append(yPos);
            str.append(")");
            str.append(" dirección ");
            str.append(spanishCoord[direction]);
            return str.toString();
        }

        /**
         * @return the current drone's direction represented by a char
         */
        public char getCharDirection() {
            return charDirections[direction];
        }

    }

}