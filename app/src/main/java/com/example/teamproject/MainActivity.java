package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

/**
 * MainActivity for the TDA Game
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

        //adding 36 "Dummy" cards to the deck to be used to test the toString method
        ArrayList<Card> cards = new ArrayList<>();
        for(int i = 0; i < 36; i++){
            cards.add(new Card("Red Dragon ", i));
        }

        //creating an instance of the GameState and printing out the toString version of it
        GameState gs = new GameState(cards);
        System.out.println(gs);
    }
}