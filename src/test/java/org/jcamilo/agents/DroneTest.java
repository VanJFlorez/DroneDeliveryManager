package org.jcamilo.agents;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jcamilo.common.FileUtils;
import org.jcamilo.deliverables.Order;
import org.junit.jupiter.api.Test;

public class DroneTest {
    /**
     * The main objective of this test is assert if the final destination given by
     * drone.deliverOne() method and the output provided by DeliverySamplesGenerator
     * is the same.
     * 
     * Note that the generator computes the delivery destination differently from
     * the Drone.deliverOne() implementation. Drone's implementation takes a
     * delivery action string and gives a result, whereas the generator computes the
     * result as long as randomly actions are generated.
     * 
     * @throws IOException
     */
    @Test
    public void deliverOneTest() throws IOException {
        HashMap<File, File> datasets = FileUtils.getDataset();
        for (Map.Entry<File, File> entry : datasets.entrySet()) {
            File sampleInputFile = entry.getKey();
            File sampleOutputFile = entry.getValue();
            File testReport = FileUtils.getOutFile(sampleInputFile.getName());

            Drone drone = new Drone(sampleInputFile, testReport);

            BufferedReader sampleInput = new BufferedReader(new FileReader(sampleInputFile));
            BufferedReader sampleOutput = new BufferedReader(new FileReader(sampleOutputFile));

            String deliveryDescr;
            String expectedState = sampleOutput.readLine();// skip the first line (the report title)
            // read the sample input and output files line by line
            while ((deliveryDescr = sampleInput.readLine()) != null
                    && (expectedState = sampleOutput.readLine()) != null) {
                try {
                    drone.deliverOne(new Order(deliveryDescr));
                } catch (ItemLimitException e) {
                    e.printStackTrace();
                }
                String droneState = drone.getCurrentState().getReadableState();
                
                // System.out.println("RESULT: " + droneState + " EXPECTED: " + expectedState);
                assertEquals(droneState, expectedState);
            }
            sampleInput.close();
            sampleOutput.close();
        }
    }   
}