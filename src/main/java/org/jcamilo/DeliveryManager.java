package org.jcamilo;

import java.io.File;

/**
 * This class has the bussiness logic to drive the drone to its delivery
 * destination.
 */
public class DeliveryManager {
    public static void main( String[] args ) {
        String inPath = "C:\\Users\\Administrador\\Downloads\\repo\\mine\\DronDeliveryManager\\in\\";
        String outPath = "C:\\Users\\Administrador\\Downloads\\repo\\mine\\DronDeliveryManager\\out\\";

        // how many drones there are
        int droneQtty = ( new File(inPath)).listFiles().length;

        // Do this with multithreading...
        for(int i = 0; i < droneQtty; i++) {

        }
    }
}
