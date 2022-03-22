package com.example.teamproject.tda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.teamproject.R;

/**
 * MainActivity for the TDA Game
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creating an instance of the GameState and printing out the toString version of it
        TdaGameState gs = new TdaGameState();
        TdaGameState copy = new TdaGameState(gs);
        System.out.println(copy);
    }
}