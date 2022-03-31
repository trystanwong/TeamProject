package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaSelectCardAction extends GameAction {
    private int index;
    private int placement;
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

    public int getPlacement(){
        return placement;
    }

    public int getIndex(){
        return index;
    }
}
