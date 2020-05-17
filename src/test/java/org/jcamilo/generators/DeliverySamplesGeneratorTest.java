package org.jcamilo.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class DeliverySamplesGeneratorTest {

    @Test
    public static void updateLocation_test() {
        Random rnd = new Random();
        char[] actions = {'A', 'I', 'D'};
        int[] nextLocation;

        for(int i = 0; i < 1000; i++) {
            int[] prevLocation = {rnd.nextInt(1000) - 500, rnd.nextInt(1000) - 500, rnd.nextInt(4)};
            char action = actions[rnd.nextInt(actions.length)];
            nextLocation = DeliverySamplesGenerator.updateLocation(prevLocation, action);
            System.out.println(prevLocation[0] + ", " + prevLocation[1] + ", " + prevLocation[2] + " + " + action + " = " + nextLocation[0] + ", " + nextLocation[1] + ", " + nextLocation[2]);
            switch (action) {
                case 'A':
                    switch (prevLocation[2])  {
                        case 0: // N ~ e.g. (2, 3, N) + A = (2, 4, N)
                            // actual vs expected
                            assertEquals(nextLocation[0], prevLocation[0]);     // x
                            assertEquals(nextLocation[1], prevLocation[1] + 1); // y
                            assertEquals(nextLocation[2], prevLocation[2]);     // dir    
                            break;
                        case 1: // E ~ e.g. (-3, 8, E) + A = (-2, 8, E)
                            assertEquals(nextLocation[0], prevLocation[0] + 1); // x
                            assertEquals(nextLocation[1], prevLocation[1]);     // y
                            assertEquals(nextLocation[2], prevLocation[2]);     // dir
                            break;
                        case 2: // S ~ e.g. (3, 4, S) + A = (3, 3, S)
                            assertEquals(nextLocation[0], prevLocation[0]);     // x
                            assertEquals(nextLocation[1], prevLocation[1] - 1); // y
                            assertEquals(nextLocation[2], prevLocation[2]);     // dir
                            break;
                        case 3: // W ~ e.g. (-3, -5, W) + A = (-4, -5, W)
                            assertEquals(nextLocation[0], prevLocation[0] - 1); // x
                            assertEquals(nextLocation[1], prevLocation[1]);     // y
                            assertEquals(nextLocation[2], prevLocation[2]);     // dir
                            break;
                    }
                    break;
                case 'I':
                    assertEquals(nextLocation[0], prevLocation[0]);                 // x
                    assertEquals(nextLocation[1], prevLocation[1]);                 // y

                    // see the method 'updateLocation()' implementation for details...
                    if(prevLocation[2] == 0)
                        assertEquals(nextLocation[2], 3);     // dir
                    else
                        assertEquals(nextLocation[2], prevLocation[2] - 1);
                        
                    break;
                case 'D':
                    assertEquals(nextLocation[0], prevLocation[0]);             // x
                    assertEquals(nextLocation[1], prevLocation[1]);             // y
                    assertEquals(nextLocation[2], (prevLocation[2] + 1)%4);     // dir
                    break;
            }
        }





    }

    public static class Pair {
        public int[] after;
        public int[] before;

        public Pair(int[] after, int[] before) {
            this.after = after;
            this.before = before;
        }
    }
}