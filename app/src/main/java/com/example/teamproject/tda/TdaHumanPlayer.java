package com.example.teamproject.tda;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamproject.game.GameHumanPlayer;
import com.example.teamproject.game.GameMainActivity;
import com.example.teamproject.game.infoMsg.GameInfo;

public class TdaHumanPlayer extends GameHumanPlayer implements View.OnClickListener{

    // These variables will reference widgets that will be modified during play
    private TextView[] hoards = null;
    private TextView[] handSizes = null;

    private TextView deckSize = null;

    private ImageView[][] flights = null;
    private ImageView[] hand = null;

    private ImageView[] antePile = null;

    private Button[] buttons = null;

    private GameMainActivity myActivity;

    /**
     * constructor
     *
     * @param name the name of the player
     */
    public TdaHumanPlayer(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }

    @Override
    public void onClick(View view) {

    }
}
