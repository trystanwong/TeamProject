package com.example.teamproject;

import java.util.ArrayList;
import java.util.Random;

/**
 * GameState Class keeps track of the current state of the TDA game
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class GameState {

    //Instance Variables

    //private Player[] players; //all players
    private int numPlayers;
    private int[] id; //id of each player (placeholder for subclass of Player)

    private int round; //current round in gambit (resets to 1 after gambit ends)
    private int gambit; //number of gambits that have occurred
    private int currentPlayer; //id of the player who's turn it is
    private int roundLeader; //id of the current round leader
    private int gamePhase; //what phase of the game we are in

    //constants for different phases of the game in gamePhase
    private static final int BEGIN_GAME = 0;
    private static final int ANTE = 1;
    private static final int ROUND = 2;
    private static final int CHOICE = 3;
    private static final int END_GAMBIT = 4;
    private static final int FORFEIT = 5;

    private int numCardsInDeck; //cards remaining in the deck
    private int numCardsOnBoard; //number cards that are visible to every player

    private ArrayList<Card> boardCards; //all the cards that have been removed from the deck
    private ArrayList<Card> deck; //current stack of deck

    //all player flights
    private ArrayList<Card>[] flights;

    //all player hands
    private ArrayList<Card>[] hands;

    //all player hoards and current stakes for the current gambit
    private int currentStakes;
    private int[] hoards;

    /**
     * Constructor for the GameState at the beginning of the game
     * @param initDeck - deck of all Card objects found in the game
     */
    public GameState(ArrayList<Card> initDeck){

        numPlayers = 4;
        gamePhase = BEGIN_GAME;
        round = 1;
        gambit = 0;

        //setting up the id of each player (placeholder for player class)
        id = new int[numPlayers];
        for(int i = 0; i<numPlayers; i++){
            id[i] = i;
        }

        //there is no current player or round leader in the beginning phase of the game
        //its decided in the ante phase but it'll default to player1
        currentPlayer = 0;
        roundLeader = 0;

        //70 playable cards
        numCardsInDeck = 36;

        deck = new ArrayList<Card>();
        //adding all of 70 cards into the deck
        for(int i = 0; i < numCardsInDeck; i++){
            deck.add(initDeck.get(i));
        }

        //initializing each players flight, hand, and hoard (each player starts with 50 gold)
        flights = new ArrayList[4];
        hands = new ArrayList[4];
        hoards = new int[4];
        for(int i = 0; i < 4; i++){
            flights[i] = new ArrayList<Card>();
            hands[i] = new ArrayList<Card>();
            hoards[i] = 50;
        }

        currentStakes = 0; //no stakes until a gambit begins
        numCardsOnBoard = 0; //there are no visible cards in the beginning of the game

        for(int i = 0; i<4; i++) {
            //adding 6 random cards to each players hand using the randomCard function
            //this is the starting hand of each player
            for (int j = 0; j < 6; j++) {
                hands[i].add(randomCard());
            }
            //adding 3 random cards to each players flights to test the toString function
            for(int k = 0; k < 3; k++){
                flights[i].add(randomCard());
            }
        }
    }
    /**
     * Copy Constructor for the GameState
     */
    public GameState(GameState gameStateCopy){

        this.gamePhase = gameStateCopy.gamePhase;
        this.round = gameStateCopy.round;
        this.gambit = gameStateCopy.gambit;
        this.currentPlayer = gameStateCopy.currentPlayer;
        this.roundLeader = gameStateCopy.roundLeader;

        //copying the names
        for(int i = 0; i < 4; i++){
            this.id[i] = gameStateCopy.id[i];
        }

        //copying the cards in the deck
        this.numCardsInDeck = gameStateCopy.numCardsInDeck;
        for(Card d : deck){
            int index = gameStateCopy.deck.indexOf(d);
            this.deck.add(new Card(gameStateCopy.deck.get(index)));
        }

        //copying the cards on the board
        this.numCardsOnBoard = gameStateCopy.numCardsOnBoard;
        for(Card b : boardCards){
            int index = gameStateCopy.boardCards.indexOf(b);
            this.boardCards.add(new Card(gameStateCopy.boardCards.get(index)));
        }

        //copying each player's hand and flight
        for(int i = 0; i < 4; i++){
            for(Card c : gameStateCopy.hands[i] ){
                int index = gameStateCopy.hands[i].indexOf(c);
                this.hands[i].add(new Card(gameStateCopy.hands[i].get(index)));
            }
            for(Card f : gameStateCopy.flights[i]){
                int index = gameStateCopy.flights[i].indexOf(f);
                this.flights[i].add(new Card(gameStateCopy.flights[i].get(index)));
            }
        }
        this.currentStakes = gameStateCopy.currentStakes;
    }

    /**
     *
     * toString method builds a String containing all of the information needed in the current
     * state of the game (all instance variables).
     *
     * @return String that will be printed
     *
     External Citation
     Date: 22 February 2022
     Problem: Did not know the most efficient way to add all elements of an array onto
     one string
     https://stackoverflow.com/questions/599161/best-way-to-convert-an-arraylist-to-a-string
     Solution: I used a String Builder to append each element of the array
     */
    @Override
    public String toString(){

        StringBuilder sb = new StringBuilder();

        //string version of the gamephase to be printed
        String gp = "";
        switch(gamePhase){
            case BEGIN_GAME:
                gp = "The game has just begun";
                break;
            case ANTE:
                gp = "We are currently in the Ante phase.";
                break;
            case ROUND:
                gp = "We are in the middle of a round";
                break;
            case CHOICE:
                gp = "A player is currently making a choice.";
                break;
            case END_GAMBIT:
                gp = "A Gambit has just ended.";
                break;
            case FORFEIT:
                gp = "The game has ended.";
                break;
            default:
                gp = "no info on the current phase of the game";
                break;
        }

        //printing all player id's (useful for reading who the current player and round leader is
        sb.append("----------------------------------\n");
        sb.append("All Player ID's:\n");
        for(int i = 0; i < numPlayers; i++)
        {
            sb.append("Player "+(i+1)+": " + id[i]);
            sb.append("\n");
        }

        //printing out various info about the current round and gambit
        sb.append("----------------------------------\n");
        sb.append("Current Phase: "+gp+"\n");
        sb.append("Current Round: " + round+"\n");
        sb.append("ID of Current Player: " + currentPlayer+"\n");
        sb.append("ID of Current Round Leader: " + roundLeader+"\n");
        sb.append("Current Stakes: " + currentStakes + "\n");
        sb.append("Total Gambits: " + gambit +"\n");

        //printing the resources for each player
        for(int i = 0; i < 4; i++){
            sb.append("----------------------------------\n");
            sb.append("PLAYER "+i+":\n");
            sb.append("Hoard: " + hoards[i] + "\n");
            sb.append("Hand: \n");
            for(int j = 0; j < hands[i].size(); j++){
                sb.append((j+1)+".\t"+hands[i].get(j).toString()+"\n");
            }
            sb.append("Flight: \n");
            for(int k = 0; k < flights[i].size(); k++){
                sb.append((k+1)+".\t"+flights[i].get(k).toString()+"\n");
            }
        }
        sb.append("----------------------------------\n");

        return sb.toString();

    }

    /**
     * returns a random card from the deck and removes it from the deck list
     * used to test the toString method by initializing hands and flights with random cards
     * @return Card Object
     */
    public Card randomCard(){

        Random r = new Random();
        int random = r.nextInt(numCardsInDeck);
        Card newCard = deck.get(random);

        //removes the card from the deck so it cant be chosen again
        numCardsInDeck--;
        deck.remove(random);

        return newCard;

    }

    /**
     * Forfeit action ends the game
     *
     * @return true if it's a valid move / false if it's not
     */
    public boolean forfeit(){
        gamePhase = FORFEIT;
        return true;
    }

    /**
     * Choose Flight action occurs when a card's power requires the player to choose an
     * opponent's flight.
     *
     * @param player - player choosing a flight
     * @return true if it's a valid move / false if it's not
     */
    public boolean chooseFlight(int player){
        if(player == currentPlayer && gamePhase == CHOICE) {

            //do something (implemented later)

            //choice phase is over returns to the round
            gamePhase = ROUND;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Choose Option action occurs when certain cards' powers trigger a choice for a player
     * That choice is displayed through the game text
     *
     * @param chosenPlayer - the player the choice is given to
     * @param player - the player who played the card that triggered a choice
     * @return - true if it's a valid move / false if it's not
     */
    public boolean chooseOption(int chosenPlayer, int player){
        if(gamePhase == CHOICE){

            //it's now the chosen player's turn (temporarily until they make a choice)
            currentPlayer = chosenPlayer;

            //do something (implemented later)

            //its now the original players turn again
            currentPlayer = player;

            //choice phase is over returns to the round
            gamePhase = ROUND;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * choose ante action used when certain powers require a player to choose an ante card
     *
     * @param player - player who is trying to make the move
     * @return - true if it's a valid move / false if it's not
     */
    public boolean chooseAnte(int player){
        if(gamePhase == CHOICE && player == currentPlayer){

            //do something (implemented later)

            //choice phase is over returns to the round
            gamePhase = ROUND;
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * End Turn function used for the end turn button
     *
     * @param player - player trying to end their turn
     * @return - true if it's a valid move / false if it's not
     */
    public boolean endTurn(int player){
        if(player == currentPlayer){

            //sets the current player to the next player
            currentPlayer++;

            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Select Card action to view a card
     *
     * See card class for placement of cards
     * @return true if it's a valid move / false if it's not
     */
    public boolean selectCard(){

        //returns true if there are any visible cards on the board to be selected
        if(boardCards.size()>0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Play Card action using the play button (only visible when a card is selected)
     *
     * @param player - player making the action
     * @param card - card they are trying to play
     * @return - true if it's a valid move / false if it's not
     */
    public boolean playCard(int player, Card card){

        //the card must be selected in order to play the card
        selectCard();

        // a player can only play a card if it's their turn
        if(currentPlayer == player){

            //removes the card from the hand of the current player
            if(currentPlayer == 0){
                hands[0].remove(card);
            }
            if(currentPlayer == 1){
                hands[1].remove(card);
            }
            if(currentPlayer == 2){
                hands[2].remove(card);
            }
            if(currentPlayer == 3){
                hands[3].remove(card);
            }
            return true;
        }
        else{
            return false;
        }
    }



}
