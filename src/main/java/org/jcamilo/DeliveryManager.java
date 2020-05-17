package org.jcamilo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcamilo.agents.Drone;

/**
 * This class has the bussiness logic to drive the drone to its delivery
 * destination.
 */
public class DeliveryManager {
    public static void main(String[] args) throws IOException {
        File inFolder = new File("C:\\Users\\Administrador\\Downloads\\repo\\mine\\DroneDeliveryManager\\input\\");
        File outFolder = new File("C:\\Users\\Administrador\\Downloads\\repo\\mine\\DroneDeliveryManager\\output\\");

        // clean out directory
        deleteFiles(outFolder);

        // Do this with multithreading...
        for(File delivery : inFolder.listFiles()) {
            String outFilePath = outFolder.getAbsolutePath() + "\\" + getOutputFilename(delivery.getName());   
            
            // each new dron runs over its own thread
            Thread t = new Thread(new Drone(delivery, new File(outFilePath)));
            t.start();
        }
    }

    private static void deleteFiles(File folder) {
        if(folder.isDirectory()) {
            for(File f : folder.listFiles()) {
                f.delete();
            }
            return;
        }
        System.out.println("ERROR: The associated path is not folder...");
    }

    private static String getOutputFilename(String inputFileName) {
        return "out" + getFileNumber(inputFileName) + ".txt";
    }

    private static String getFileNumber(String fileName) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(fileName);
        matcher.find();
        return matcher.group();
    }
}
