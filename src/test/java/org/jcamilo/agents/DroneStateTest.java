package org.jcamilo.agents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import java.util.regex.Pattern;

import org.jcamilo.agents.Drone.State;
import org.jcamilo.common.Constants;
import org.junit.jupiter.api.Test;

public class DroneStateTest {
    private static final int testFactor = Constants.TEST_FACTOR;

    @Test 
    public void getReadableStateTest() {
        Random rnd = new Random();

        for(int i = 0; i < testFactor; i++) {
            int x = rnd.nextInt(testFactor) - testFactor/2;
            int y = rnd.nextInt(testFactor) - testFactor/2;
            int dir = rnd.nextInt(Constants.DIRECTIONS.length);

            State state = new State(x, y, dir);

            String readableState = state.getReadableState();
            String regex = Constants.TEST_OUTPUT_LOG_TEMPLATE_REGEX;
            assertTrue(Pattern.matches(regex, readableState));
        }
    }

    @Test
    public void performActionTest() {
        Random rnd = new Random();

        for(int i = 0; i < testFactor; i++) {
            char[] actions = Constants.ACTIONS;
            int x = rnd.nextInt(testFactor) - testFactor/2;
            int y = rnd.nextInt(testFactor) - testFactor/2;
            int dir = rnd.nextInt(Constants.DIRECTIONS.length);
            char action = actions[rnd.nextInt(actions.length)];

            State prevState = new State(x, y, dir);
            State nextState = new State(x, y, dir);
            nextState.performAction(action);
            
            // System.out.println(action + " + " + prevState.getReadableState() + " = " + nextState.getReadableState());
            switch (action) {
                case 'A':
                    switch (prevState.getDirection()) {
                        case 0:
                            assertEquals(prevState.getXPos(), nextState.getXPos());
                            assertEquals(prevState.getYPos() + 1, nextState.getYPos());
                            assertEquals(prevState.getDirection(), nextState.getDirection());
                            break;
                        case 1:
                            assertEquals(prevState.getXPos() + 1, nextState.getXPos());
                            assertEquals(prevState.getYPos(), nextState.getYPos());
                            assertEquals(prevState.getDirection(), nextState.getDirection());
                            break;
                        case 2:
                            assertEquals(prevState.getXPos(), nextState.getXPos());
                            assertEquals(prevState.getYPos() - 1, nextState.getYPos());
                            assertEquals(prevState.getDirection(), nextState.getDirection());
                            break;
                        case 3:     
                            assertEquals(prevState.getXPos() - 1, nextState.getXPos());
                            assertEquals(prevState.getYPos(), nextState.getYPos());
                            assertEquals(prevState.getDirection(), nextState.getDirection());
                            break;
                    }
                    break;
                case 'I':    
                    assertEquals(prevState.getXPos(), nextState.getXPos());
                    assertEquals(prevState.getYPos(), nextState.getYPos());
                    if (prevState.getDirection() == 0)
                        assertEquals(3, nextState.getDirection());
                    else
                        assertEquals(prevState.getDirection() - 1, nextState.getDirection());
                    break;
                case 'D':
                    assertEquals(prevState.getXPos(), nextState.getXPos());
                    assertEquals(prevState.getYPos(), nextState.getYPos());
                    assertEquals((prevState.getDirection() + 1)%4, nextState.getDirection());
                    break;
            }           
        }
    }

    @Test
    public void getCharDirectionTest() {
        for(int i = 0; i < Constants.DIRECTIONS.length; i++) {
            State st = new State(0, 0, i);
            char dir = st.getCharDirection();
            assertEquals(Constants.DIRECTIONS[i], dir);
        }
    }
}