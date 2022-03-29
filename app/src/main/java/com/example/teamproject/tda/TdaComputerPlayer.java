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
        if(info instanceof TdaGameState) {
            TdaGameState tda = (TdaGameState) info;
            if(tda.getCurrentPlayer() != this.playerNum){
                return;
            }
            else{
                TdaSelectCardAction sca = new TdaSelectCardAction(this,0,false);
                super.sleep(1000);
                super.game.sendAction(sca);
                TdaPlayCardAction pca = new TdaPlayCardAction(this);
                super.game.sendAction(pca);
            }
        }
        else{
            return;
        }
    }
}
