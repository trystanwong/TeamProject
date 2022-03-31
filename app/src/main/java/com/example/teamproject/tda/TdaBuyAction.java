package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaBuyAction extends GameAction{
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TdaBuyAction(GamePlayer player) {
        super(player);
    }
}
