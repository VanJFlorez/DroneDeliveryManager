package org.jcamilo;

import java.io.File;
import java.io.IOException;

import org.jcamilo.agents.Drone;
import org.jcamilo.common.Constants;
import org.jcamilo.common.FileUtils;
import org.jcamilo.generators.DeliverySamplesGenerator;

/**
 * This class has the bussiness logic to drive the drone to its delivery
 * destination.
 */
public class DeliveryManager {
    public static void main(String[] args) throws IOException {
        File inFolder = new File(Constants.IN_FOLDER_NAME);
        File outFolder = new File(Constants.OUT_FOLDER_NAME);
        File datasetsFolder = new File(Constants.DATASETS_FOLDER_NAME);



        int dronQtty = Constants.DRONE_QTTY;
        int deliveryQtty = Constants.DELIVERIES_PER_DRONE;
        int actionSize = Constants.ACTIONS_PER_DELIVERY;

        // Empty the dataset folder
        FileUtils.deleteFiles(datasetsFolder);

        // Generate sampledata in .\datasets\ folder
        DeliverySamplesGenerator.genActionsAndDeliveryPaths(datasetsFolder.getPath(), dronQtty, deliveryQtty, actionSize);

        // Copy the generated input datasets to .\input\ directory
        FileUtils.copyInputDataset();

        // Clean out directory the .\output\. We do that since the drone will append the existing files.
        FileUtils.deleteFiles(outFolder);

        // Multhreaded loop
        for(File delivery : inFolder.listFiles()) {
            File report = FileUtils.getOutFile(delivery.getName());
            
            // Each new drone runs over its own thread
            Thread t = new Thread(new Drone(delivery, report));
            t.start();
        }
    }

}
