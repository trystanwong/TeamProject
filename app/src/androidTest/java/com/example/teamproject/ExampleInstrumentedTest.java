package com.example.teamproject;

import android.content.Context;
import com.example.teamproject.tda.TdaGameState;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.teamproject", appContext.getPackageName());
    }
    @Test
    public void forfeit(){
        TdaGameState tda = new TdaGameState();
        tda.forfeit();
        int gamePhase = tda.getGamePhase();
        assertEquals(gamePhase,tda.FORFEIT);
    }
    @Test
    public void chooseFlight(){

        TdaGameState tda = new TdaGameState();

        boolean cF1 = tda.chooseFlight(1);
        assertEquals(false,cF1);
        boolean cF2 = tda.chooseFlight(0);
        int gamePhase = tda.getGamePhase();
        assertEquals(true,cF2);
        assertEquals(gamePhase,tda.ROUND);
    }
    @Test
    public void endTurn(){
        TdaGameState tda = new TdaGameState();

        boolean endTurn1 = tda.endTurn(1);

        assertEquals(false,endTurn1);

        boolean endTurn2 = tda.endTurn(0);

        int currentPlayer = tda.getCurrentPlayer();

        assertEquals(true,endTurn2);
        assertEquals(currentPlayer,1);
    }

}