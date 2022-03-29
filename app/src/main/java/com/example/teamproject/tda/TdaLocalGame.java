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

    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        TdaGameState update = new TdaGameState(tda);
        tda.setNames(super.playerNames);
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

        //forfeit action
        if (action instanceof TdaForfeitAction){
            tda.forfeit();
            tda.setGamePhase(5);
            return true;
        }

        //play card action
        if (action instanceof TdaPlayCardAction){

            //currently selected card
            int selectedIndex = tda.getSelectedCardIndex();
            int currentPlayer = tda.getCurrentPlayer();

            //dumb AI just plays the first card in their hand.
            if(currentPlayer == 1){
                tda.playCard(1,0);
                tda.setCurrentPlayer(0);
            }

            int hoard = tda.getHoard(tda.getCurrentPlayer());
            int stakes = tda.getCurrentStakes();
            Card c = tda.getHandCard(currentPlayer,selectedIndex);

            String cardName = c.getName();

            if(tda.getFlightSize(tda.getCurrentPlayer())>=3){
                //grey the play button so the user knows not to press it
                tda.setPlayButton(false);
                return false;
            }
            if(c.getPlacement()!=c.HAND){
                tda.setPlayButton(false);
                return false;
            }
            else if(currentPlayer==0){
                tda.setPlayButton(true);
                tda.playCard(tda.getCurrentPlayer(), selectedIndex);

                //if the card has a power
                switch(cardName){
                    case "Black Dragon":
                        tda.setHoard(currentPlayer,hoard+2);
                        tda.setStakes(stakes-2);
                        break;
                }
                tda.setCurrentPlayer(1);
                return true;
            }
        }

        //select card action
        if( action instanceof TdaSelectCardAction){

            //tda.selectCard();

            int selectedIndex = ((TdaSelectCardAction) action).getIndex();
            System.out.println(selectedIndex);

            boolean isFlight = ((TdaSelectCardAction) action).getPlacement();

            Card c = new Card();
            if(!isFlight){
                c = tda.getHandCard(tda.getCurrentPlayer(),selectedIndex);
            }
            if(isFlight){
                c = tda.getFlightCard(0,selectedIndex);
            }

            tda.setSelectedCardIndex(selectedIndex);
            tda.setSelectedCard(tda.getCurrentPlayer(),c);

            System.out.println(c);

            if(c.getPlacement()==c.HAND) {
                tda.setPlayButton(true);
            }
            else{
                tda.setPlayButton(false);
            }

            return true;
        }

        return false;
    }
}
