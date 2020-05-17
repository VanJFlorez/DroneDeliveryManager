package org.jcamilo.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DeliverySamplesGenerator {
    // drone properties
    public static final char[] DRONE_ACTIONS = {'A', 'I', 'D'};
    public static final int[] START_POINT = {0, 0, 0};
    
    // file properties
    public static final String FILE_IN_PREFIX = "in";
    public static final String FILE_OUT_PREFIX = "out";
    public static final String FILE_EXTENSION = ".txt";
    public static final String NUMBERING_FORMAT = "%02d";
    public static final String REPORT_SPLIT_MESSAGE = "== Reporte de entregas ==";


    
    public static void main(String[] args) {
        String outpath = "C:\\Users\\Administrador\\Downloads\\repo\\mine\\DroneDeliveryManager\\datasets\\";
        genActionsAndDeliveryPaths(outpath, 3, 15, 20);
    }

    /**
     * Generates several randomly drone actions paths and at the same time computes 
     * the drone's location after perform each individual action.
     * @param dronQtty the number of drones involved in the problem
     * @param deliveryQtty the upper bound for the random quantity of delivery 
     *             paths per dron
     * @param length an upperbound for the lenght of the randomly generated
     *              paths.
     * @return Two files in the host computer per @param dronQtty. One will have the drone
     *         actions and the other will have the partial locations associated to 
     *         each atomic action.
     */
    public static void genActionsAndDeliveryPaths(String folderOutPath, int dronQtty, int deliveryQtty, int length) {
        if (!folderOutPath.endsWith("/"))
            folderOutPath += "/";

        StringBuffer actions;
        StringBuffer locations;
        String inputFileName;
        String outputFileName;
        File inFile;
        File outFile;
        char action;
        int[] location;
        Random rnd = new Random();

        for(int i = 0; i < dronQtty; i++) { // per each drone...
            inputFileName = FILE_IN_PREFIX + String.format(NUMBERING_FORMAT, i + 1);
            inFile = new File(folderOutPath + inputFileName + FILE_EXTENSION);
            
            outputFileName = FILE_OUT_PREFIX + String.format(NUMBERING_FORMAT, i + 1);
            outFile = new File(folderOutPath + outputFileName + FILE_EXTENSION);
            
            actions = new StringBuffer();
            locations = new StringBuffer();

            try(BufferedWriter inFileWriter = new BufferedWriter(new FileWriter(inFile));
            BufferedWriter outFileWriter = new BufferedWriter(new FileWriter(outFile));) {
                
                locations.append(REPORT_SPLIT_MESSAGE);
                locations.append(System.getProperty("line.separator"));
                
                int deliveries = rnd.nextInt(deliveryQtty);
                for(int j = 0; j < deliveries; j++) { // generate 'deliveryQtty' paths...
                    // each delivery starts from the initial point
                    location = START_POINT;
                    int steps = rnd.nextInt(length);
                    for(int k = 0; k < steps; k++) { // of length 'length'...
                        // generate an action and append to actions buffer
                        action = DRONE_ACTIONS[rnd.nextInt(DRONE_ACTIONS.length)];
                        actions.append(action);

                        // compute the new cartesian location based on the current action
                        int[] newlocation = updateLocation(location, action);
                        location = newlocation;
                        int a = 0;
                    }

                    // append final location to the reports file
                    locations.append(locationToStringReadable(location));
                    locations.append(System.getProperty("line.separator"));

                    // add a new line to differentiate among action paths
                    actions.append(System.getProperty("line.separator"));
                }
                inFileWriter.write(actions.toString());
                outFileWriter.write(locations.toString());
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
   
    /*
    * To ease the computations a mapping for the cardinal points was made:
    *  
    *      N               0
    *    W   E           3   1
    *      S               2
    * 
    * and we can compute de direction actions by doing modular arithmetic.
    */
    public static int[] updateLocation(int[] currLocation, char action) {
        final int mod = 4;
        int[] newLocation = {currLocation[0], currLocation[1], currLocation[2]};
        
        // compute the new location
        switch (action) {
            case 'A': // Adelante
                switch (newLocation[2]) {
                    case 0:
                        newLocation[1] += 1;
                        break;
                    case 1:
                        newLocation[0] += 1;
                        break;
                    case 2:
                        newLocation[1] -= 1;
                        break;
                    case 3:
                        newLocation[0] -= 1;
                        break;
                }
                break;
            case 'I': // Izquierda
                // more efficient
                if(newLocation[2] == 0) {
                    newLocation[2] = 3;
                } else {
                    newLocation[2] -= 1;
                }

                // newLocation[2] %= mod; // % here is remainder, not modulus
                break;
            case 'D': // Derecha
                newLocation[2] += 1;
                newLocation[2] %= mod; // as we work with positive values modulus equals the remainder
                break;
        }

        return newLocation;
    }

    private static String locationToStringReadable(int[] location) {
        StringBuilder str = new StringBuilder();
        str.append("(");
        str.append(location[0]);
        str.append(", ");
        str.append(location[1]);
        str.append(")");
        str.append(" dirección ");
        switch (location[2]) {
            case 0:
                str.append("Norte");
                break;
            case 1:
                str.append("Occidente");
                break;
            case 2:
                str.append("Sur");
                break;
            case 3:
                str.append("Oriente");
                break;
        }
        return str.toString();
    }



    public static void printArr(int[] arr) {
        System.out.print("[ ");
        for(int i = 0; i < arr.length - 1; i++)
            System.out.print(arr[i] + ", ");
        System.out.print(arr[arr.length - 1] + " ]");
    }
}

