package org.jcamilo.generators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DeliveryPathsGen {

    public static final char[] DRONE_ACTIONS = {'A', 'I', 'D'};
    public static final String FILE_EXTENSION = ".txt";

    /**
     * @param dronQtty the number of drones involved in the problem
     * @param qtty the upper bound for the random quantity of delivery 
     *             paths per dron
     * @param length an upperbound for the lenght of the randomly generated
     *              paths.
     */
    public static void generateDeliveryPaths(String folderOutPath, int dronQtty, int qtty, int length) {
        if (!folderOutPath.endsWith("/"))
            folderOutPath += "/";

        Random rnd = new Random();
        StringBuffer deliveryPaths;
        String fileName;
        File outFile;
        for(int i = 0; i < dronQtty; i++) { // per each drone...
            fileName = "in" + String.format("%02d", i + 1);
            outFile = new File(folderOutPath + fileName + FILE_EXTENSION);
            
            deliveryPaths = new StringBuffer();
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                for(int j = 0; j < rnd.nextInt(qtty); j++) { // generate 'qtty' paths...
                    for(int k = 0; k < rnd.nextInt(length); k++) { // of lenght 'length'...
                        deliveryPaths.append(DRONE_ACTIONS[rnd.nextInt(DRONE_ACTIONS.length)]);
                    }
                    deliveryPaths.append(System.getProperty("line.separator"));
                }
                writer.write(deliveryPaths.toString()); 
            } catch(IOException ioe) {
                ioe.printStackTrace();
            }
            // try (FileWritter fw = new FileWriter(file))
        }
    }

    public static void main(String[] args) {
        generateDeliveryPaths("C:\\Users\\Administrador\\Downloads\\repo\\mine\\DronDeliveryManager\\in", 2, 15, 20);
    }
}
