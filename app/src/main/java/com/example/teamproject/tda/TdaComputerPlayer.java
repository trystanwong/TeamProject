package com.example.teamproject.tda;

import com.example.teamproject.game.GameComputerPlayer;
import com.example.teamproject.game.infoMsg.GameInfo;

public class TdaComputerPlayer extends GameComputerPlayer {

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public TdaComputerPlayer(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {

    }
}
