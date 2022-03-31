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
        for(int i = 0; i<2;i++){
            tda.setNames(i,super.playerNames[i]);
        }
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
                    int roundLeader = tda.isRoundLeader();
                    tda.setCurrentPlayer(roundLeader);
                    tda.setRoundLeader(roundLeader);
                    tda.setGameText("Player " + roundLeader + "is the Round Leader");
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

        //all card powers
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
                //tda.setCurrentPlayer(player);
                tda.setGamePhase(TdaGameState.CHOICE);
                sendUpdatedStateTo(players[player]);
                break;

            //The opponent with the strongest flight chooses either to give you a stronger good dragon
            //or pay you 5 gold
            case "Brass Dragon":
                break;

            //Put the two weakest ante cards into your hand
            case "Bronze Dragon":
                break;

            //Discard this card and replace it with the top card of the deck.
            //That card's power triggers regardless of its strength
            case "Copper Dragon":
                break;

            //Dragon God - Tiamat counts as a Black, Blue, Green, Red, and White Dragon
            //As long as you have Tiamat and a good dragon in you flight, you can't win the gambit
            case "Tiamat":
                break;

            //Copy the power of an evil dragon in any flight
            case "Dracolich":
                break;

            //Each other player with both good and evil dragons in the same flight pays you 10 gold
            //As long as you have Bahamut and a evil dragon in you flight, you can't win the gambit
            case "Bahamut":
                break;

            //Draw a card for each good dragon in your flight
            case "Gold Dragon":
                break;

            //Pay 1 gold to the stakes. Draw a card for each player with a flight stronger than yours
            case "The Fool":
                break;

            //Pay 1 gold to the stakes. The power of each good dragon in your flight triggers
            case "The Princess":
                break;

            //Pay 1 gold to the stakes. You are the leader of the next round of this gambit instead of any other player.
            case "The Priest":
                break;

            //Pay 1 gold to the stakes. The player with the weakest flight wins the gambit instead of the player with the strongest flight.
            case "The Druid":
                break;

            //Steal 7 gold from the stakes. Discard a card from your hand
            case "The Thief":
                break;

            //Pay 1 gold to the stakes. Discard a weaker dragon from any flight
            case "The Dragonslayer":
                break;

            //Pay 1 gold to the stakes. Copy the power of ante card.
            case "The Archmage":
                break;

            //The opponent with the strongest flight pays you 1 gold. Take a random card from that player's hand.
            case "Red Dragon":
                break;

            //Each player with at least one good dragon in their flight draws a card
            case "Silver Dragon":
                break;
            //If any flight includes a mortal, steal 3 gold from the rakes.
            case "White Dragon":
                break;

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

            //discarding a card to your flight
            case 1:
                tda.setFlight(player,tda.getFlightSize(player),new Card(c));
                tda.setFlightSize(player,tda.getFlightSize(player)-1);
                break;
            case 2:
                tda.setFlight(player,index,new Card());
                tda.setFlightSize(player,tda.getFlightSize(player)-1);
                break;
                //discarding a card back to the deck
            case 3:
                tda.getDeck().add(c);
                break;

        }
    }

    /**
     * Determines the player with the strongest flight
     *
     * @return the id of the player with the strongest flight
     */
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
            if(tda.getCurrentPlayer() == 1){
                int num = 0;
            }
            int hoard = tda.getHoard(player);
            int handSize = tda.getHandSize(player);
            int strengths = 0;
            for(int i = 0; i<3; i++){
                Card c = new Card(tda.randomCard());
                c.setPlacement(c.HAND);
                tda.setHand(player,i+1,new Card(c));
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


            //can the player play the selected card?
            if(tda.playCard(currentPlayer,selectedIndex)){

                //if the flights are going to be full after the current player plays this card
                if(tda.getFlightSize(currentPlayer)==2){
                    if(tda.getFlightSize(opponent)==3){

                        //plays the final card in the flight before deciding a winner
                        if(currentPlayer == 1){
                            playCard(1,0,c);
                            tda.setCurrentPlayer(0);
                        }
                        else if(currentPlayer ==0){
                            playCard(0,selectedIndex,c);
                            tda.setCurrentPlayer(1);
                        }

                        //deciding the winner of the Gambit
                        int gambitWinner = strongestFlight();
                        tda.setGameText("Player "+gambitWinner+" won that gambit!");
                        tda.setHoard(gambitWinner,tda.getHoard(gambitWinner)+stakes);

                        //clears the stakes and flights and tells the player to confirm the
                        //end of the gambit.
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

                if(currentPlayer==0){
                    tda.setPlayButton(true);
                    playCard(currentPlayer,selectedIndex,c);
                    if(!c.getName().equals("Blue Dragon")) {
                        tda.setCurrentPlayer(1);
                        return true;
                    }
                    return true;
                }

                //if card is in a flight, the player cant play it
                if(c.getPlacement()!=c.HAND){
                    tda.setPlayButton(false);
                    return false;
                }

                //human player plays the selected card

                return true;
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

                //checks what choice was given to the player
                for(int i = 0; i < 4; i++){
                    for(int j = 0; j < 2; j++){
                        if(tda.getChoices()[i][j].equals(tda.getChoice1())){
                            choiceText = i;
                        }
                    }
                }

                //all the possible choices a player can make (6 total not including confirm)
                switch (choiceText) {

                    //blue dragon
                    case 0:
                        //number of evil dragons in the current players flight
                        int counter = 0;
                        for(Card c : tda.getFlight(player)) {
                            if (c.getType() == c.EVIL) {
                                counter++;
                            }
                        }
                        if(choice == 0){

                            tda.setHoard(player, hoard + counter);
                            tda.setStakes(stakes - counter);
                        }
                        if(choice == 1){
                            tda.setHoard(opponent, hoard - counter);
                            tda.setStakes(stakes+counter);

                        }
                        tda.setCurrentPlayer(opponent);
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

                    //confirming the end of a gambit
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
