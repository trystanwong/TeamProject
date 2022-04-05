package com.example.teamproject;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.teamproject.tda.TdaGameState;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getPhase() throws Exception {
        TdaGameState tda = new TdaGameState();
        tda.setGamePhase(0);
        int phase = tda.getGamePhase();
        assertEquals(0, phase);
    }

    @Test
    public void getPlayButton() throws Exception {
        TdaGameState tda = new TdaGameState();
        tda.setPlayButton(true);
        boolean canTheyPlay = tda.getPlayButton();
        assertEquals(true, canTheyPlay);
    }

    @Test
    public void settingFlightSize() throws Exception {
        TdaGameState tda = new TdaGameState();
        tda.setFlightSize(0,3);
        int flight = tda.getFlightSize(0);
        assertEquals(3, flight);
    }
}