package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaChoiceAction extends GameAction {
    int choiceNum;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public TdaChoiceAction(GamePlayer player, int choice) {
        super(player);
        choiceNum = choice;
    }

    public int getChoice(){
        return choiceNum;
    }
}
