package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

/**
 *
 * Represents an action of a player selecting a card
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class TdaSelectCardAction extends GameAction {
    private int index;//index of the selected card
    private int placement;//where the card is located on the board
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param i - index of the card selected
     * @param place- is the card in the players hand
     */
    public TdaSelectCardAction(GamePlayer player,int i, int place) {
        super(player);
        index = i;
        placement = place;
    }

    //getters for the instance variables
    public int getPlacement(){
        return placement;
    }
    public int getIndex(){
        return index;
    }
}
