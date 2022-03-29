package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaSelectCardAction extends GameAction {
    private int index;
    private boolean flight;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TdaSelectCardAction(GamePlayer player,int i, boolean f) {
        super(player);
        index = i;
        flight = f;
    }

    public boolean getPlacement(){
        return flight;
    }

    public int getIndex(){
        return index;
    }
}
