package com.example.teamproject.tda;

import com.example.teamproject.game.GameComputerPlayer;
import com.example.teamproject.game.infoMsg.GameInfo;

/**
 * Represents a dumb AI to be an opponent for the human player
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class TdaComputerPlayer extends GameComputerPlayer{

    /**
     * constructor
     *
     * @param name the player's name
     */
    public TdaComputerPlayer(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {
        if(info instanceof TdaGameState) {

            //gamestate
            TdaGameState tda = (TdaGameState) info;

            //computer doesn't do anything if it's not its turn
            if(tda.getCurrentPlayer() != this.playerNum){
                return;
            }
            else{
                //computer waits a second before making decision
                super.sleep(1000);
                //if the AI has to make a choice
                if(tda.getGamePhase()==tda.CHOICE){
                    //dumb AI always selects first choice
                    TdaChoiceAction ca = new TdaChoiceAction(this,0);
                    super.game.sendAction(ca);
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
