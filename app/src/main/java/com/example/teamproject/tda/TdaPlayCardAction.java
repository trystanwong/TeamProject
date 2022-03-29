package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaPlayCardAction extends GameAction {
    private int index;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TdaPlayCardAction(GamePlayer player) {
        super(player);
    }
}
