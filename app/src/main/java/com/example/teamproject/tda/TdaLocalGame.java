package com.example.teamproject.tda;

import com.example.teamproject.game.GamePlayer;
import com.example.teamproject.game.LocalGame;
import com.example.teamproject.game.actionMsg.GameAction;

import java.util.Random;

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

                        //Steal 2 gold from the stakes
                        case "Black Dragon":
                            tda.setHoard(currentPlayer,hoard+2);
                            tda.setStakes(stakes-2);
                            tda.setChoice1("Pay 5 Gold");
                            tda.setChoice2("Pay 3 Gold");
                            tda.setGamePhase(tda.CHOICE);
                            return true;


                        //Choose: Steal 1 gold from the stakes for each evil dragon in your flight
                        //or each opponent pays that much gold to the stakes
                        case "Blue Dragon":
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
                            for (int i = 0; i < tda.getFlightSize(tda.getCurrentPlayer()); i++) {
                                if (tda.getFlightCard(tda.getCurrentPlayer(), i).equals("GoodDragon")) {
                                    numGoodDragons++;
                                }
                            }
                            //go through a for loop to draw a card for each good dragon
                            for (int j = 0; j < numGoodDragons; j++) {
                                tda.drawCard(tda.getCurrentPlayer());
                            }
                            break;

                        //The opponent to your left chooses either to give you a weaker evil dragon from their hand
                        //or to pay you 5 gold
                        case "Green Dragon":
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
                            //variables to help
                            int HighestStrength = 0;
                            int strength = 0;
                            int strongestPlayer = 0;
                            Card takenCard;
                            Random rand = new Random();
                            //check to see which opponent has the strongest flight
                            for (int i = 0; i < 2; i++) {
                                for (int j = 0; j < tda.getFlightSize(i); i++) {
                                    //have a counter for the strength of the total strength in player flight
                                    strength = tda.getFlightCard(i,j).getStrength();
                                }
                                //if the strength of that players flight is bigger than the current highest set it
                                if (strength > HighestStrength && i != tda.getCurrentPlayer()) {
                                    //set the highest strength to the strength and the player corresponding with it
                                    HighestStrength = strength;
                                    strongestPlayer = i;
                                }
                            }
                            //have the opponent pay you and take a random card from him
                            if (strongestPlayer != tda.getCurrentPlayer()) {
                                tda.setHoard(tda.getCurrentPlayer(), hoard + 1);
                                tda.setHoard(strongestPlayer, hoard - 1);
                                takenCard = tda.getHandCard(strongestPlayer, rand.nextInt(tda.getHandSize(strongestPlayer) + 1));
                                tda.addCard(tda.getCurrentPlayer(), takenCard);
                            }
                            break;

                        //Each player with at least one good dragon in their flight draws a card
                        case "Silver Dragon":
                            break;

                        //If any flight includes a mortal, steal 3 gold from the rakes.
                        case "White Dragon":
                            //check the two players flights
                            for (int i = 0; i < 2; i++) {
                                //check the players flight for mortals
                                for (int j = 0; j < tda.getFlightSize(i); i++) {
                                    if (tda.getFlightCard(i,j).getType().equals("Mortal")) {
                                        tda.setHoard(tda.getCurrentPlayer(), hoard + 3);
                                        tda.setStakes(stakes - 3);
                                        break;
                                    }
                                    else
                                        continue;
                                }
                            }
                            break;
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
