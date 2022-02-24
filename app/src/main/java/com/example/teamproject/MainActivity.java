package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Card> cards = new ArrayList<>();

        //adding 36 "Dummy" cards to the deck
        for(int i = 0; i < 36; i++){
            cards.add(new Card("Red Dragon ", i));
        }

        GameState gs = new GameState(cards);
        System.out.println(gs);
    }
}