package org.jcamilo.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jcamilo.deliverables.Deliverable;

public class Drone implements Runnable {
    private final static int ITEM_LIMIT = 3;
    private final static int MAX_DISTANCE_ALLOWED = 10;
    private final static int[] START_POINT = { 0, 0, 0 };

    private List<char[]> deliveryPaths;
    private List<Deliverable> deliverables;

    private File reportsOut;

    /**
     * Constructor for one delivery path
     * 
     * @param deliveryPath       a char string with the drone actions
     * @param logDeliveryJourney indicates if each step on the path must be recorded
     */
    public Drone(Deliverable item, char[] deliveryPath) {
        deliverables = Arrays.asList(item);
        deliveryPaths = Arrays.asList(deliveryPath);
    }

    public Drone(File deliveries, File reportsOut) throws IOException {
        deliveryPaths = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader(deliveries));
        String actions = "";
        while((actions = reader.readLine()) != null) {
            deliveryPaths.add(actions.toCharArray());
        }

        this.reportsOut = reportsOut;
        reader.close();
    }

    /**
     * Constructor for several delivery paths
     * 
     * @param deliveryPaths      a List containing char string secuences with drone
     *                           actions
     * @param logDeliveryJourney indicates if each step on the path must be recorded
     * @throws ItemLimitException
     */
    public Drone(List<Deliverable> items, List<char[]> deliveryPaths) throws ItemLimitException {
        if (items.size() > ITEM_LIMIT) {
            throw new ItemLimitException();
        }
        deliverables = items;
        this.deliveryPaths = deliveryPaths;
    }

    /**
     * Supply all deliverables.
     * @return
     */
    @Override
    public void run() {
        for(char[] path : deliveryPaths) {
            try {
                deliverOne(path);
            } catch (IOException e) {
                System.out.println("Cannot complete this delivery");
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
     * @throws IOException
     */
    private void deliverOne(char[] deliveryPath) throws IOException {
        final int mod = 4;
        int[] currLocation = {START_POINT[0], START_POINT[1], START_POINT[2]};
        for(int action : deliveryPath) {
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
                    if (currLocation[2] == -1)
                        currLocation[2] = 3;
                    break;
                case 'D': // Derecha
                    currLocation[2] += 1;
                    currLocation[2] %= mod;
                    break;
            }
        }

        FileWriter fw = new FileWriter(reportsOut, true);
        fw.write(currLocation[0] + " " + currLocation[1] + " " + currLocation[2] + "\n");
        fw.close();
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