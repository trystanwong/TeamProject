package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.LocalGame;
import com.example.teamproject.game.actionMsg.GameAction;

public class TdaLocalGame extends LocalGame {

    private TdaGameState tda;

    /**
     * This ctor creates a new game state
     */
    public TdaLocalGame() {
        tda = new TdaGameState();
        System.out.println(tda.getHandSize(1));
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        TdaGameState update = new TdaGameState(tda);
        p.sendInfo(update);
    }

    /**
     *
     * Checks to see if the given player can make a move
     *
     * @param playerIdx
     * 		the player's player-number (ID)
     * @return true if the player can move false if they cant
     *
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(tda.getCurrentPlayer() == playerIdx){
            return true;
        }
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        for(int e : tda.getHoards()){
            if (e == 0){
                return "Game is Over";
            }
        }
        if(tda.getGamePhase()==TdaGameState.FORFEIT){
            return "Game is over";
        }
        return null;
    }

    @Override
    protected boolean makeMove(GameAction action) {

        if (action instanceof TdaForfeitAction){
            tda.forfeit();
            tda.setGamePhase(5);
            return true;
        }
        if (action instanceof TdaPlayCardAction){
           tda.playCard(tda.getCurrentPlayer(),5);
           System.out.println("hi");
           return true;
        }

        if( action instanceof TdaSelectCardAction){
            tda.setSelectedCard(tda.getHandCard(((TdaSelectCardAction) action).getIndex()));
            tda.selectCard();
            return true;
        }

        return false;
    }
}
