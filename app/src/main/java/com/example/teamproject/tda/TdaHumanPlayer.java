package com.example.teamproject.tda;

import android.view.View;

import com.example.teamproject.game.GameHumanPlayer;
import com.example.teamproject.game.GameMainActivity;
import com.example.teamproject.game.infoMsg.GameInfo;

public class TdaHumanPlayer extends GameHumanPlayer {
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
}
