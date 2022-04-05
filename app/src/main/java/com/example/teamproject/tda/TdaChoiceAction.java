package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.actionMsg.GameAction;

/**?
 *
 * Represents an action of a player making a choice
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class TdaChoiceAction extends GameAction {
    int choiceNum;//index of the choice the player made (0 or 1)
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     * @param choice choice the player made
     */
    public TdaChoiceAction(GamePlayer player, int choice) {
        super(player);
        choiceNum = choice;
    }

    //returns the choice the player made
    public int getChoice(){
        return choiceNum;
    }
}
