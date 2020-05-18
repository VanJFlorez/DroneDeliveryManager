package org.jcamilo.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.jcamilo.common.Constants;

public class DeliverySamplesGenerator {
    // drone properties
    private static final char[] droneActions = Constants.ACTIONS;
    private static final int[] startPoint = Constants.START_POINT;
    private static final String[] spanishDirections = Constants.DIRECTIONS_ES;
    // file properties
    private static final String fileInPrefix = Constants.FILE_IN_PREFIX;
    private static final String fileOutPrefix = Constants.FILE_OUT_PREFIX;
    private static final String fileExtension = Constants.FILE_EXTENSION;
    private static final String numberingFormat = Constants.NUMBERING_FORMAT;
    private static final String reportSplitMessage = Constants.REPORT_SPLIT_MESSAGE;

    /**
     * Generates several randomly drone actions paths and at the same time computes 
     * the drone's location after performing each individual action.
     * @param dronQtty the number of drones involved in the problem
     * @param deliveryQtty the upper bound for the random quantity of delivery 
     *             paths per dron
     * @param actionSize an upperbound for the lenght of the randomly generated
     *              paths.
     * @return Two files in the host computer per @param dronQtty. One will have the drone
     *         actions and the other will have the partial locations associated to 
     *         each atomic action.
     */
    public static void genActionsAndDeliveryPaths(String folderOutPath, int dronQtty, int deliveryQtty, int actionSize) {
        if (!folderOutPath.endsWith("/"))
            folderOutPath += "/";

        Random rnd = new Random();
        for(int i = 0; i < dronQtty; i++) { // per each drone...
            String inputFileName = fileInPrefix + String.format(numberingFormat, i + 1);
            File inFile = new File(folderOutPath + inputFileName + fileExtension);
            
            String outputFileName = fileOutPrefix + String.format(numberingFormat, i + 1);
            File outFile = new File(folderOutPath + outputFileName + fileExtension);
            
            StringBuffer actions = new StringBuffer();
            StringBuffer locations = new StringBuffer();

            try(BufferedWriter inFileWriter = new BufferedWriter(new FileWriter(inFile));
            BufferedWriter outFileWriter = new BufferedWriter(new FileWriter(outFile));) {
                
                locations.append(reportSplitMessage);
                locations.append(System.getProperty("line.separator"));
                
                int deliveries = rnd.nextInt(deliveryQtty);
                for(int j = 0; j < deliveries; j++) { // generate 'deliveryQtty' paths...
                    // each delivery starts from the initial point
                    int[] location = startPoint;
                    int steps = rnd.nextInt(actionSize);

                    // TODO: -- SHOW ONE BY ONE GENERATION OPERATION
                    for(int k = 0; k < steps; k++) { // of length 'length'...
                        // generate an action and append to actions buffer
                        char action = droneActions[rnd.nextInt(droneActions.length)];
                        actions.append(action);

                        // compute the new cartesian location based on the current action
                        location = updateLocation(location, action);
                    }

                    // append final location to the reports file
                    locations.append(locationToStringReadable(location));
                    locations.append(System.getProperty("line.separator"));

                    // add a new line to differentiate among action strings
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

    /**
     * @param location current dron location
     * @return String containing drone's state according to guidelines
     */
    private static String locationToStringReadable(int[] location) {
        return String.format(Constants.OUTPUT_LOG_TEMPLATE, location[0], location[1], spanishDirections[location[2]]);
    }
}

