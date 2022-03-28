package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaSelectCardAction extends GameAction {
    private int index;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TdaSelectCardAction(GamePlayer player,int i) {
        super(player);
        index = i;
    }

    public int getIndex(){
        return index;
    }
}
