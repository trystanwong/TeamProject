package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.LocalGame;
import com.example.teamproject.game.actionMsg.GameAction;

import java.util.Random;

/**
 *
 * Local Game of TDA
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class TdaLocalGame extends LocalGame {

    private TdaGameState tda;

    /**
     * This constructor creates a new game state
     */
    public TdaLocalGame() {
        tda = new TdaGameState();
    }

    /**
     * sends an updated state of the game to a gamePlayer
     * @param p - game player the game is being sent to
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

        //current game state
        TdaGameState update = new TdaGameState(tda);

        //names of players
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

    /**
     * Checks if the game is over
     * @return - message to be displayed if the game is over
     */
    @Override
    protected String checkIfGameOver() {
        if(tda.getGamePhase()==TdaGameState.FORFEIT){
            return "PLAYER FORFEITED";
        }
        return null;
    }

    /**
     * Plays a card to either a flight or ante
     *
     * @param player - who's playing the card
     * @param index - what is the index of the card being played
     * @param c - card being played
     */
    protected void playCard(int player, int index, Card c){

        //sizes of the current players flight and hand
        int size = tda.getFlightSize(player);
        int handSize = tda.getHandSize(player);

        //if the game is currently in the ante phase
        if (tda.getGamePhase() == tda.ANTE) {

            //set the ante card in the ante pile
            c.setPlacement(Card.ANTE);
            tda.setAnteCard(player,c);
            Card currentAnte = tda.getAnteCard(player);

            //stakes set to first ante card by default
            int strength = tda.getAnteCard(0).getStrength();

            //if the ante card being played is greater than the current stakes
            //set the stakes to the new strength
            if(currentAnte.getStrength() > strength){
                strength = currentAnte.getStrength();
            }
            tda.setStakes(2*strength);
            int stakes = tda.getCurrentStakes();

            //if all ante cards have been played decide who the round leader is
            if(tda.getAnteCard(1).getName()!=""){

                //taking the ante's from the player's hoards
                for(int i = 0; i < 4; i++){
                    int hoard = tda.getHoard(i);
                    tda.setHoard(i,hoard-strength);
                }

                //deciding the round leader
                int roundLeader = tda.isRoundLeader();
                tda.setRoundLeader(roundLeader);

                //player confirms
                tda.setGameText("Player " + roundLeader + " is the Round Leader");
                tda.setChoice1(tda.getChoice(3,0));
                tda.setChoice2(tda.getChoice(3,1));

            }
        }

        //if the card is played during a round
        else if(tda.getGamePhase() == tda.ROUND) {

            //setting the card in the flight
            Card newCard = new Card(c);
            newCard.setPlacement(newCard.FLIGHT);
            tda.setFlight(player,size,newCard);
            tda.setFlightSize(player,tda.getFlightSize(player)+1);
        }

        //taking the card out of the player's hand
        if (index < handSize - 1) {
            for (int i = index; i < handSize - 1; i++) {
                tda.setHand(player,i,tda.getHandCard(player,i+1));
            }
        }
        tda.setHand(player,handSize-1,new Card());
        tda.setHandSize(player,tda.getHandSize(player)-1);

        //if the card has a power and its a round, its triggered when played
        if(tda.getGamePhase()==tda.ROUND) {
            powers(c.getName());
        }

    }

    /**
     * A method to trigger all of the powers of the cards
     * @param name - name of the card
     */
    public void powers(String name){

        Random rand = new Random();
        int player = tda.getCurrentPlayer();
        int opponent = Math.abs(player-1);
        int playerHoard = tda.getHoard(player);
        int opponentHoard = tda.getHoard(opponent);
        int stakes = tda.getCurrentStakes();
        int playerHandSize = tda.getHandSize(player);
        int oppHandSize = tda.getHandSize(opponent);

        //all card powers
        switch(name){

            case "Black Dragon":
                tda.setHoard(player,playerHoard+2);
                tda.setStakes(stakes-2);
                break;
            case "Green Dragon":
                tda.setChoice1(tda.getChoices()[1][0]);
                tda.setChoice2(tda.getChoices()[1][1]);
                tda.setGamePhase(tda.CHOICE);
                break;
            case "Blue Dragon":
                tda.setChoice1(tda.getChoices()[0][0]);
                tda.setChoice2(tda.getChoices()[0][1]);
                tda.setGamePhase(tda.CHOICE);
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
                //counter for amount of good dragons
                int numGoodDragons = 0;
                //check current players flight to see how many good dragons are within flight
                for (int i = 0; i < tda.getFlightSize(player); i++) {
                    if (tda.getFlightCard(player, i).getType() == 0) {
                        numGoodDragons++;
                    }
                }
                //go through a for loop to draw a card for each good dragon
                for (int j = 0; j < numGoodDragons; j++) {
                    tda.drawCard(player);
                }
                break;

            //Pay 1 gold to the stakes. Draw a card for each player with a flight stronger than yours
            case "The Fool":
                tda.setHoard(player,playerHoard-1);
                tda.setStakes(stakes+1);
                if (strongestFlight() != player) {
                    tda.drawCard(player);
                }
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
            case "The DragonSlayer":
                //set the strength to the DragonSlayers strength and find the weakest card
                int theSlayerStrength = 8;
                boolean weakestCardFound = false;
                int index = 0;
                tda.setHoard(player,playerHoard-1);
                tda.setStakes(stakes+1);
                while (!weakestCardFound) {
                    if (tda.getFlightCard(opponent, index).getStrength() < theSlayerStrength) {
                        weakestCardFound = true;
                    }
                    else {
                        index++;
                    }
                }
                discardCard(opponent, index, 2);
                break;

            //Pay 1 gold to the stakes. Copy the power of ante card.
            case "The Archmage":
                break;

            //The opponent with the strongest flight pays you 1 gold. Take a random card from that player's hand.
            case "Red Dragon":
                //random card from opponents hand
                int indexOfCard = rand.nextInt(tda.getHandSize(opponent));

                //if the strength of the opponents flight is bigger than the current player do the power
                if (strongestFlight() != player) {
                    discardCard(opponent, indexOfCard, 0);
                    tda.setHoard(player, playerHoard + 1);
                    tda.setHoard(opponent, opponentHoard - 1);
                }
                break;

            //Each player with at least one good dragon in their flight draws a card
            case "Silver Dragon":
                for (int i = 0; i <  tda.getFlightSize(player); i++) {
                    if (tda.getFlightCard(player, i).getType() == 0) {
                        Card c = new Card(tda.randomCard());
                        c.setPlacement(c.HAND);
                        tda.drawCard(player);
                    }
                }
                //check the opponents flight for a good dragon
                for (int j = 0; j <  tda.getFlightSize(opponent); j++) {
                    if (tda.getFlightCard(opponent, j).getType() == 0) {
                        tda.drawCard(opponent);
                    }
                }
                break;
            //If any flight includes a mortal, steal 3 gold from the rakes.
            case "White Dragon":
                boolean mortalFound = false;
                //check the players flight for mortals
                for (int i = 0; i < tda.getFlightSize(player); i++) {
                    if (tda.getFlightCard(player, i).getType() == 2) {
                        tda.setHoard(tda.getCurrentPlayer(), playerHoard + 3);
                        tda.setStakes(stakes - 3);
                        mortalFound = true;
                    }
                }
                if (mortalFound != true) {
                    //check the opponents flight for mortals
                    for (int j = 0; j < tda.getFlightSize(opponent); j++) {
                        if (tda.getFlightCard(opponent, j).getType() == 2) {
                            tda.setHoard(tda.getCurrentPlayer(), playerHoard + 3);
                            tda.setStakes(stakes - 3);
                        }
                    }
                }
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
                c.setPlacement(1);
                tda.setHand(opponent,opponentHandSize,new Card(c));
                tda.setHandSize(opponent,tda.getHandSize(opponent)+1);
                break;

            //discarding a card to your flight
            case 1:
                c.setPlacement(2);
                tda.setFlight(player,tda.getFlightSize(player),new Card(c));
                tda.setFlightSize(player,tda.getFlightSize(player)-1);
                break;

            //if the card is being discarded from a flight
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
            oppStrength += tda.getFlightCard(opponent,i).getStrength();
        }

        //returning the player with the strongest flight
        if(playerStrength>oppStrength){
            return player;
        }
        else{
            return opponent;
        }


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

    protected void gambitWinner(){
        //deciding the winner of the Gambit
        int stakes = tda.getCurrentStakes();
        int gambitWinner = strongestFlight();
        tda.setGameText("Player "+gambitWinner+" won that gambit!");
        tda.setHoard(gambitWinner,tda.getHoard(gambitWinner)+stakes);

        //clears the stakes and flights and tells the player to confirm the
        //end of the gambit.
        tda.setStakes(0);
        tda.setChoice1(tda.getChoice(3,0));
        tda.setChoice2(tda.getChoice(3,1));
        tda.setGamePhase(TdaGameState.END_GAMBIT);
    }

    /**
     *
     *
     *
     * @param action
     * 			The move that the player has sent to the game
     * @return true if the move was legal, false if not
     */
    @Override
    protected boolean makeMove(GameAction action) {

        //buy action
        if(action instanceof TdaBuyAction){

            int player = tda.getCurrentPlayer();
            int hoard = tda.getHoard(player);
            int handSize = tda.getHandSize(player);

            //draw top card add it to hand
            Card c = new Card(tda.randomCard());
            c.setPlacement(c.HAND);
            tda.setHand(player,handSize,new Card(c));

            tda.setHandSize(player,handSize+1);
            int strength = c.getStrength();

            //add cards until player has 4 cards
            for(int i = 1; i<3; i++){
                Card d = new Card(tda.randomCard());
                d.setPlacement(c.HAND);
                tda.setHand(player,i+1,new Card(d));
                tda.setHandSize(player,i+2);

            }
            tda.setHoard(player,hoard-strength);
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
            int currentPlayer = tda.getCurrentPlayer();
            int selectedIndex = tda.getSelectedCardIndex(currentPlayer);
            int opponent = Math.abs(currentPlayer-1);

            int stakes = tda.getCurrentStakes();
            Card c = new Card(tda.getSelectedCard(currentPlayer));
            String cardName = c.getName();

            //if card is in a flight, the player cant play it
            if(c.getPlacement()!=c.HAND){
                return false;
            }

            //can the player play the selected card?
            if(tda.playCard(currentPlayer,selectedIndex)){

                //if the flights are going to be full after the current player plays this card


                //if we are at the end of an ante
                if(tda.getGamePhase()==TdaGameState.ANTE) {
                    if (tda.getAnteCard(0).getName() != "") {
                        if (currentPlayer == 1) {
                            playCard(1, 0, c);
                            tda.setCurrentPlayer(0);
                            tda.setGamePhase(TdaGameState.END_ANTE);
                            return true;
                        } else if (currentPlayer == 0) {
                            playCard(0, selectedIndex, c);
                            tda.setGamePhase(TdaGameState.END_ANTE);
                            return true;
                        }
                    }
                }

                //dumb AI just plays the first card in their hand.
                if(currentPlayer == 1){

                    playCard(1,0,c);

                    //if cards with choices are played
                    if(c.getName().equals("Green Dragon")&&tda.getGamePhase()==tda.CHOICE){
                        tda.setCurrentPlayer(opponent);
                        return true;
                    }

                    if(c.getName().equals("Blue Dragon")&&tda.getGamePhase()==tda.CHOICE){
                        sendAction(new TdaChoiceAction(players[1],0));
                    }

                    if(tda.getHandSize(currentPlayer)==1){
                        sendAction(new TdaBuyAction(players[currentPlayer]));
                    }
                    if(tda.getFlightSize(currentPlayer)==3){
                        if(tda.getFlightSize(opponent)==3){
                            gambitWinner();
                        }
                    }
                    tda.setCurrentPlayer(opponent);
                    return true;
                }

                else if(currentPlayer==0){
                    tda.setPlayButton(true);

                    playCard(currentPlayer,selectedIndex,c);

                    //if cards with choices are played
                    if(c.getName().equals("Green Dragon")&&tda.getGamePhase()==tda.CHOICE){
                        tda.setCurrentPlayer(opponent);
                        return true;
                    }
                    if(c.getName().equals("Blue Dragon")&&tda.getGamePhase()==tda.CHOICE){
                        return true;
                    }

                    //if the player only has 1 card after playing a card, they must buy 4 cards
                    //from the deck
                    if(tda.getHandSize(currentPlayer)==1){
                        sendAction(new TdaBuyAction(players[currentPlayer]));
                    }

                    //checks if the gambit is over
                    if(tda.getFlightSize(currentPlayer)==3){
                        if(tda.getFlightSize(opponent)==3){
                            gambitWinner();
                            sendUpdatedStateTo(players[0]);
                            return true;
                        }
                    }

                    //if the current phase is an ante
                    if(tda.getGamePhase()==tda.ANTE){
                        tda.setCurrentPlayer(opponent);
                        return true;
                    }
                    tda.setCurrentPlayer(opponent);
                    return true;
                }
                return true;
            }
            return false;
        }

        //select card action
        if( action instanceof TdaSelectCardAction){

            if(tda.selectCard(tda.getCurrentPlayer())) {

                int selectedIndex = ((TdaSelectCardAction) action).getIndex();

                //is the selected card is a flight or a hand
                int placement = ((TdaSelectCardAction) action).getPlacement();

                Card c = new Card();

                //possible placements for a selected card
                switch(placement){
                    //in a hand
                    case 0:
                        c = tda.getHandCard(tda.getCurrentPlayer(), selectedIndex);
                        tda.setPlayButton(true);
                        break;

                    //in a flight
                    case 1:
                        c = tda.getFlightCard(tda.getCurrentPlayer(), selectedIndex);
                        tda.setPlayButton(false);
                        break;

                    //in the ante
                    case 2:
                        c = tda.getAnteCard(selectedIndex);
                        tda.setPlayButton(false);
                        break;
                }

                //setting the game state to reflect the newly selected card, and its index
                tda.setSelectedCardIndex(tda.getCurrentPlayer(),selectedIndex);
                tda.setSelectedCard(tda.getCurrentPlayer(), new Card(c));

                return true;
            }
        }

        //player made a choice
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

                    //green dragon
                    case 1:
                        if(choice == 0){
                        discardCard(player,1,0);
                        }
                        else {
                            tda.setHoard(player, hoard - 5);
                            tda.setHoard(opponent, opponentHoard + 5);
                        }
                        break;

                    //brass dragon
                    case 2:
                        if(choice == 0){
                            discardCard(player,2,0);
                        }
                        else {
                            tda.setHoard(player, hoard - 5);
                            tda.setHoard(opponent, opponentHoard + 5);
                        }
                        break;

                    //confirming the end of a gambit or end of ante
                    case 3:
                        if(tda.getGamePhase()==tda.END_ANTE){
                            tda.setCurrentPlayer(tda.getRoundLeader());
                            tda.setGamePhase(tda.ROUND);
                            return true;
                        }
                        else if(tda.getGamePhase()==tda.END_GAMBIT) {
                            //clear flights
                            clearFlights();
                            //each player draws 2 cards at the end of a gambit
                            for(int i = 0; i<2;i++){
                                tda.drawCard(i);
                                tda.drawCard(i);
                                sendUpdatedStateTo(players[0]);
                            }
                            //game goes back to an ante after a gambit ends
                            tda.setGamePhase(tda.ANTE);
                            return true;
                        }
                        break;
                }

                //after a choice is made we return to a round phase
                tda.setGamePhase(tda.ROUND);
                return true;
            }
        }
        return false;
    }
}
