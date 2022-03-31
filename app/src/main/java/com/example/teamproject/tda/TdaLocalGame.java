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
            if (e <= 0){
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

                //if the card has a power, its triggered when played
                powers(c.getName());
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

    public void powers(String name){

        int player = tda.getCurrentPlayer();
        int opponent = Math.abs(player-1);
        int playerHoard = tda.getHoard(player);
        int opponentHoard = tda.getHoard(opponent);
        int stakes = tda.getCurrentStakes();

        switch(name){
            case "Black Dragon":
                tda.setHoard(player,playerHoard+2);
                tda.setStakes(stakes-2);
                break;
            case "Green Dragon":
                tda.setChoice1(tda.getChoice(1,0));
                tda.setChoice2(tda.getChoice(1,1));

                tda.setCurrentPlayer(opponent);
                tda.setGamePhase(TdaGameState.CHOICE);
                break;
            case "Blue Dragon":
                tda.setChoice1(tda.getChoice(0,0));
                tda.setChoice2(tda.getChoice(0,1));

                tda.setCurrentPlayer(opponent);
                tda.setGamePhase(TdaGameState.CHOICE);
        }
    }

    /**
     * discard a card from the given hand to a location
     *
     * @param player
     * @param index
     * @param placement - where the card is going to.
     */
    protected void discardCard(int player, int index, int placement){

        int handSize = tda.getHandSize(player);
        int opponent = Math.abs(player-1);
        int opponentHandSize = tda.getHandSize(opponent);
        Card c = new Card(tda.getHandCard(player,index));

        if(placement!=2) {
            if (index < handSize - 1) {
                for (int i = index; i < handSize - 1; i++) {
                    tda.setHand(player, i, tda.getHandCard(player, i + 1));
                }
            }

            tda.setHand(player, handSize - 1, new Card());
            tda.setHandSize(player, tda.getHandSize(player) - 1);
        }

        switch(placement){

            //if the card is being discarded to an opponent
            case 0:
                tda.setHand(opponent,opponentHandSize-1,new Card(c));
                tda.setHandSize(opponent,tda.getHandSize(opponent)+1);
                break;
            case 1:
                tda.setFlight(player,tda.getFlightSize(player),new Card(c));
                tda.setFlightSize(player,tda.getFlightSize(player)-1);
                break;
            case 2:
                tda.setFlight(player,index,new Card());
                tda.setFlightSize(player,tda.getFlightSize(player)-1);

        }
    }

    public int strongestFlight(){

        int player = tda.getCurrentPlayer();
        int opponent = Math.abs(player-1);

        int playerFlightSize = tda.getFlightSize(player);
        int oppFlightSize = tda.getFlightSize(opponent);

        Card[][] flights = tda.getFlights();

        //players flight strength
        int playerStrength = 0;

        //opponents flight strength
        int oppStrength = 0;

        //going through players flight
        for(int i = 0; i < playerFlightSize; i++){
            playerStrength += tda.getFlightCard(player,i).getStrength();
        }

        //going through opponents flight
        for(int i = 0; i < oppFlightSize; i++){
            oppStrength += tda.getFlightCard(player,i).getStrength();
        }

        //returning the player with the strongest flight
        if(playerStrength>oppStrength){
            return player;
        }
        return opponent;

    }

    public void clearFlights(){
        int player = tda.getCurrentPlayer();
        int opponent = Math.abs(player-1);

        int playerFlightSize = tda.getFlightSize(player);
        int oppFlightSize = tda.getFlightSize(opponent);

       //clearing
        for(int i = 0; i<2; i++){
            for(int j = 0; j<3;j++){
                discardCard(i,j,2);
            }
        }

        //clearing the ante pile
        for(int i = 0; i<4; i++){
            tda.setAnteCard(i,new Card());
        }
    }

    @Override
    protected boolean makeMove(GameAction action) {

        //buy action
        if(action instanceof TdaBuyAction){
            int player = tda.getCurrentPlayer();
            int hoard = tda.getHoard(player);
            int handSize = tda.getHandSize(player);
            int strengths = 0;
            for(int i = 0; i<3; i++){
                Card c = new Card(tda.randomCard());
                c.setPlacement(c.HAND);
                tda.setHand(player,i+1,c);
                tda.setHandSize(player,i+2);
                strengths += c.getStrength();
            }
            tda.setHoard(player,hoard-strengths);
            return true;
        }

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
            int opponent = Math.abs(currentPlayer-1);

            int stakes = tda.getCurrentStakes();
            Card c = tda.getHandCard(currentPlayer,selectedIndex);
            String cardName = c.getName();


            if(tda.playCard(currentPlayer,selectedIndex)){
                //if the flights are full
                if(tda.getFlightSize(currentPlayer)==3){
                    if(tda.getFlightSize(opponent)==3){
                        int gambitWinner = strongestFlight();
                        tda.setGameText("Player "+gambitWinner+" won that gambit");
                        tda.setHoard(gambitWinner,tda.getHoard(gambitWinner)+stakes);
                        tda.setStakes(0);
                        clearFlights();
                        tda.setChoice1(tda.getChoice(3,0));
                        tda.setChoice2(tda.getChoice(3,1));
                        tda.setGamePhase(TdaGameState.CHOICE);
                        return true;
                    }
                }
                //dumb AI just plays the first card in their hand.
                if(currentPlayer == 1){
                    playCard(1,0,c);
                    tda.setCurrentPlayer(0);
                    return true;
                }

                //if card is in a flight, the player cant play it
                if(c.getPlacement()!=c.HAND){
                    tda.setPlayButton(false);
                    return false;
                }

                else if(currentPlayer==0){
                    tda.setPlayButton(true);
                    playCard(currentPlayer,selectedIndex,c);
                    tda.setCurrentPlayer(1);
                    return true;
                }
            }
        }

        //select card action
        if( action instanceof TdaSelectCardAction){

            if(tda.selectCard(tda.getCurrentPlayer())) {

                int selectedIndex = ((TdaSelectCardAction) action).getIndex();

                //is the selected card is a flight or a hand
                boolean isFlight = ((TdaSelectCardAction) action).getPlacement();

                Card c = new Card();

                //if the selected card is in a hand
                if (!isFlight) {
                    c = tda.getHandCard(tda.getCurrentPlayer(), selectedIndex);
                }
                //if the selected card is in a flight
                if (isFlight) {
                    c = tda.getFlightCard(0, selectedIndex);
                }

                //setting the game state selected card and index
                tda.setSelectedCardIndex(selectedIndex);
                tda.setSelectedCard(tda.getCurrentPlayer(), c);

                //if the cards placement is in a flight, the play button should be gray
                if (c.getPlacement() == c.HAND) {
                    tda.setPlayButton(true);
                } else {
                    tda.setPlayButton(false);
                }
                return true;
            }
        }

        if(action instanceof TdaChoiceAction) {
            if (tda.choiceAction(tda.getCurrentPlayer())) {

                //current stats for each player
                int choice = ((TdaChoiceAction) action).getChoice();
                int player = tda.getCurrentPlayer();
                int opponent = Math.abs(player-1);
                int hoard = tda.getHoard(player);
                int opponentHoard = tda.getHoard(opponent);
                int choiceText = 0;
                int stakes = tda.getCurrentStakes();



                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 2; j++){
                        if(tda.getChoices()[i][j].equals(tda.getChoice1())){
                            choiceText = i;
                        }
                    }
                }
                switch (choiceText) {
                    case 0:
                        int counter = 0;
                        for(Card c : tda.getFlight(player)) {
                            if (c.getType() == c.EVIL) {
                                counter++;
                            }
                        }
                        if(choice == 0){

                            //amount of evil dragons in the players flight

                            tda.setHoard(player, hoard + counter);
                            tda.setStakes(stakes - counter);
                        }
                        else {
                            tda.setHoard(opponent, hoard - counter);
                            tda.setStakes(stakes+counter);
                        }
                        break;
                    case 1:
                        if(choice == 0){
                        discardCard(player,1,0);
                        }
                        else {
                            tda.setHoard(player, hoard - 5);
                            tda.setHoard(opponent, opponentHoard + 5);
                        }
                        break;
                    case 3:
                        tda.setGamePhase(tda.ANTE);
                        return true;

                }

                tda.setGamePhase(tda.ROUND);
                return true;
            }
        }

        return false;
    }
}
