package com.example.teamproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView largeView = findViewById(R.id.largeViewCenter);
        ImageView smallImage = findViewById(R.id.playerFlightCenter);
        View.OnClickListener displayListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("hi");
                if(largeView.getVisibility()==View.INVISIBLE){
                    largeView.setVisibility(View.VISIBLE);
                }
                else if(largeView.getVisibility()==View.VISIBLE){
                    largeView.setVisibility(View.INVISIBLE);
                }
            }
        };
        smallImage.setOnClickListener(displayListener);
        displayListener.onClick(smallImage);
    }
}