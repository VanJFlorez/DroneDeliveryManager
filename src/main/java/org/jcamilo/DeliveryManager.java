package org.jcamilo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jcamilo.agents.Drone;
import org.jcamilo.common.FileUtils;

/**
 * This class has the bussiness logic to drive the drone to its delivery
 * destination.
 */
public class DeliveryManager {
    public static void main(String[] args) throws IOException {
        File inFolder = new File("C:\\Users\\Administrador\\Downloads\\repo\\mine\\DroneDeliveryManager\\input\\");
        File outFolder = new File("C:\\Users\\Administrador\\Downloads\\repo\\mine\\DroneDeliveryManager\\output\\");

        // clean out directory
        FileUtils.deleteFiles(outFolder);

        // Multhreaded loop
        for(File delivery : inFolder.listFiles()) {
            File report = FileUtils.getOutFile(delivery.getName());
            
            // each new drone runs over its own thread
            Thread t = new Thread(new Drone(delivery, report));
            t.start();
        }
    }

}
