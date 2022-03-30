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

    protected void playCard(int player, int index, Card c){

        //sizes of the current players flight and hand
        int size = tda.getFlightSize(player);
        int handSize = tda.getHandSize(player);
        int stakes = tda.getCurrentStakes();

        //
        if (tda.getGamePhase() == tda.ANTE) {
            tda.setAnteCard(player,c);
            Card currentAnte = tda.getAnteCard(player);
            currentAnte.setPlacement(Card.ANTE);
            int strength =  currentAnte.getStrength();
                tda.setStakes(stakes+strength);
                for(int i = 0; i < 4; i++){
                    int hoard = tda.getHoard(i);
                    tda.setHoard(i,hoard-strength);
                }
            }
            else if(tda.getGamePhase() == tda.ROUND) {
                Card newCard = new Card(c);

                //adding the card from the hand to the flight
                tda.setFlight(player,size,newCard);
                tda.getFlightCard(player,size).setPlacement(Card.FLIGHT);
                tda.setFlightSize(player,tda.getFlightSize(player)+1);
            }
            if (index < handSize - 1) {
                for (int i = index; i < handSize - 1; i++) {
                    tda.setHand(player,i,tda.getHandCard(player,i+1));
                }
            }

            tda.setHand(player,handSize-1,new Card());
            tda.setHandSize(player,tda.getHandSize(player)-1);

            if(tda.getGamePhase() == tda.ANTE){
                if(player==1) {
                    tda.setGamePhase(tda.ROUND);
                }
            }
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


            int hoard = tda.getHoard(tda.getCurrentPlayer());
            int stakes = tda.getCurrentStakes();
            Card c = tda.getHandCard(currentPlayer,selectedIndex);
            String cardName = c.getName();


            if(tda.playCard(currentPlayer,selectedIndex)){

                //dumb AI just plays the first card in their hand.
                if(currentPlayer == 1){
                    playCard(1,0,c);
                    tda.setCurrentPlayer(0);
                    return true;
                }

                //if the flights are full
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
                    playCard(currentPlayer,selectedIndex,c);
                    //if the card has a power
                    switch(cardName){
                        case "Black Dragon":
                            tda.setHoard(currentPlayer,hoard+2);
                            tda.setStakes(stakes-2);
                            tda.setChoice1("Pay 5 Gold");
                            tda.setChoice2("Pay 3 Gold");
                            tda.setGamePhase(tda.CHOICE);
                            return true;
                    }
                    tda.setCurrentPlayer(1);
                    return true;
                }
            }
        }

        //select card action
        if( action instanceof TdaSelectCardAction){

            if(tda.selectCard(tda.getCurrentPlayer())) {

                int selectedIndex = ((TdaSelectCardAction) action).getIndex();

                boolean isFlight = ((TdaSelectCardAction) action).getPlacement();

                Card c = new Card();
                if (!isFlight) {
                    c = tda.getHandCard(tda.getCurrentPlayer(), selectedIndex);
                }
                if (isFlight) {
                    c = tda.getFlightCard(0, selectedIndex);
                }

                tda.setSelectedCardIndex(selectedIndex);
                tda.setSelectedCard(tda.getCurrentPlayer(), c);

                if (c.getPlacement() == c.HAND) {
                    tda.setPlayButton(true);
                } else {
                    tda.setPlayButton(false);
                }

                tda.setGameText(c.getName());

                return true;
            }
        }

        if(action instanceof TdaChoiceAction) {
            if (tda.choiceAction(tda.getCurrentPlayer())) {
                int choice = ((TdaChoiceAction) action).getChoice();
                int player = tda.getCurrentPlayer();
                int hoard = tda.getHoard(player);
                switch (choice) {
                    case 0:
                        tda.setHoard(player, hoard - 5);
                        break;
                    case 1:
                        tda.setHoard(player, hoard - 3);
                        break;
                }
                tda.setCurrentPlayer(Math.abs(tda.getCurrentPlayer() - 1));
                tda.setGamePhase(tda.ROUND);
                return true;
            }
        }

        return false;
    }
}
