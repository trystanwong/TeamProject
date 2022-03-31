package com.example.teamproject.tda;

import com.example.teamproject.game.GameComputerPlayer;
import com.example.teamproject.game.infoMsg.GameInfo;

public class TdaComputerPlayer extends GameComputerPlayer{

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
                //computer waits a second before making decision
                super.sleep(1000);

                if(tda.getGamePhase()==TdaGameState.END_ANTE){
                    sleep(100000);
                }
                if(tda.getGamePhase()==tda.CHOICE){
                    //dumb AI always selects first choice
                    TdaChoiceAction ca = new TdaChoiceAction(this,0);
                    super.game.sendAction(ca);
                }

                if(tda.getHandSize(playerNum)==1){
                    TdaBuyAction tba = new TdaBuyAction(this);
                    super.game.sendAction(tba);
                }
                //selecting the first card in their hand
                TdaSelectCardAction sca = new TdaSelectCardAction(this,0,0);
                super.game.sendAction(sca);

                //playing the first card in their hand
                TdaPlayCardAction pca = new TdaPlayCardAction(this);
                super.game.sendAction(pca);

            }
        }
    }

}
