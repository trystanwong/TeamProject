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
    private int numCardsOnBoard; //number cards that are not in the deck

    private ArrayList<Card> boardCards; //all the cards that have been removed from the deck
    private ArrayList<Card> deck; //current stack of deck

    //all player flights
    private ArrayList<Card> flight1;
    private ArrayList<Card> flight2;
    private ArrayList<Card> flight3;
    private ArrayList<Card> flight4;

    //all player hands and hand sizes
    private int player1HandSize;
    private int player2HandSize;
    private int player3HandSize;
    private int player4HandSize;

    private ArrayList<Card> player1Hand;
    private ArrayList<Card> player2Hand;
    private ArrayList<Card> player3Hand;
    private ArrayList<Card> player4Hand;

    //all player hoards and current stakes for the current gambit
    private int currentStakes;
    private int player1Hoard;
    private int player2Hoard;
    private int player3Hoard;
    private int player4Hoard;

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

        //initializing each players flight and hand
        flight1 = new ArrayList<>();
        flight2 = new ArrayList<>();
        flight3 = new ArrayList<>();
        flight4 = new ArrayList<>();

        player1Hand = new ArrayList<>();
        player2Hand = new ArrayList<>();
        player3Hand = new ArrayList<>();
        player4Hand = new ArrayList<>();

        //all players start with 6 cards
        player1HandSize = 6;
        player2HandSize = 6;
        player3HandSize = 6;
        player4HandSize = 6;

        //adding 6 random cards to each players hand using the randomCard function
        //this is the starting hand of each player
        for(int j = 0; j < 6; j++){
           player1Hand.add(randomCard());
           player2Hand.add(randomCard());
           player3Hand.add(randomCard());
           player4Hand.add(randomCard());
        }

        //adding 3 random cards to each players flights to test the toString function
        for(int k = 0; k < 3; k++){
            flight1.add(randomCard());
            flight2.add(randomCard());
            flight3.add(randomCard());
            flight4.add(randomCard());
        }

        numCardsOnBoard = 0; //there are no visible cards in the beginning of the game
        currentStakes = 0; //no stakes until a gambit begins

        //each player starts with 50 gold
        player1Hoard = 50;
        player2Hoard = 50;
        player3Hoard = 50;
        player4Hoard = 50;

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

        //copying each player's hand
        for(Card e : gameStateCopy.player1Hand) {
            int index = gameStateCopy.player1Hand.indexOf(e);
            this.player1Hand.add(new Card(gameStateCopy.player1Hand.get(index)));
        }
        for(Card e : gameStateCopy.player2Hand) {
            int index = gameStateCopy.player2Hand.indexOf(e);
            this.player2Hand.add(new Card(gameStateCopy.player2Hand.get(index)));
        }
        for(Card e : gameStateCopy.player3Hand) {
            int index = gameStateCopy.player3Hand.indexOf(e);
            this.player3Hand.add(new Card(gameStateCopy.player3Hand.get(index)));
        }
        for(Card e : gameStateCopy.player4Hand) {
            int index = gameStateCopy.player4Hand.indexOf(e);
            this.player4Hand.add(new Card(gameStateCopy.player4Hand.get(index)));
        }

        //copying each player's flight
        for(Card f : gameStateCopy.flight1) {
            int index = gameStateCopy.flight1.indexOf(f);
            this.flight1.add(new Card(gameStateCopy.flight1.get(index)));
        }
        for(Card f : gameStateCopy.flight2) {
            int index = gameStateCopy.flight1.indexOf(f);
            this.flight2.add(new Card(gameStateCopy.flight2.get(index)));
        }
        for(Card f : gameStateCopy.flight3) {
            int index = gameStateCopy.flight3.indexOf(f);
            this.flight3.add(new Card(gameStateCopy.flight3.get(index)));
        }
        for(Card f : gameStateCopy.flight4) {
            int index = gameStateCopy.flight4.indexOf(f);
            this.flight4.add(new Card(gameStateCopy.flight4.get(index)));
        }

        this.currentStakes = gameStateCopy.currentStakes;

        //copying all the players hoards
        this.player1Hoard = gameStateCopy.player1Hoard;
        this.player2Hoard = gameStateCopy.player2Hoard;
        this.player3Hoard = gameStateCopy.player3Hoard;
        this.player4Hoard = gameStateCopy.player4Hoard;


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

        //printing each resources for player1
        sb.append("----------------------------------\n");
        sb.append("PLAYER 1:\n");
        sb.append("Hoard: " + player1Hoard + "\n");
        sb.append("Hand: \n");
        for(int i = 0; i < player1HandSize; i++){
            sb.append((i+1)+".\t"+player1Hand.get(i).toString()+"\n");
        }
        sb.append("Flight: \n");
        for(int i = 0; i < flight1.size(); i++){
            sb.append((i+1)+".\t"+flight1.get(i).toString()+"\n");
        }

        //printing each resources for player2
        sb.append("----------------------------------\n");
        sb.append("PLAYER 2: \n");
        sb.append("Hoard: " + player2Hoard + "\n");
        sb.append("Hand: \n");
        for(int i = 0; i < player2HandSize; i++){
            sb.append((i+1)+".\t"+player2Hand.get(i).toString()+"\n");
        }
        sb.append("Flight: \n");
        for(int i = 0; i < flight2.size(); i++){
            sb.append((i+1)+".\t"+flight2.get(i).toString()+"\n");
        }

        //printing each resources for player3
        sb.append("----------------------------------\n");
        sb.append("PLAYER 3: \n");
        sb.append("Hoard: " + player3Hoard + "\n");
        sb.append("Hand: \n");
        for(int i = 0; i < player3HandSize; i++){
            sb.append((i+1)+".\t"+player3Hand.get(i).toString()+"\n");
        }
        sb.append("Flight: \n");
        for(int i = 0; i < flight3.size(); i++){
            sb.append((i+1)+".\t"+flight3.get(i).toString()+"\n");
        }

        //printing each resources for player4
        sb.append("----------------------------------\n");
        sb.append("PLAYER 4: \n");
        sb.append("Hoard: " + player3Hoard + "\n");
        sb.append("Hand: \n");
        for(int i = 0; i < player4HandSize; i++){
            sb.append((i+1)+".\t"+player4Hand.get(i).toString()+"\n");
        }
        sb.append("Flight: \n");
        for(int i = 0; i < flight4.size(); i++){
            sb.append((i+1)+".\t"+flight4.get(i).toString()+"\n");
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
                player1Hand.remove(card);
                player1HandSize --;
            }
            if(currentPlayer == 1){
                player2Hand.remove(card);
                player2HandSize --;
            }
            if(currentPlayer == 2){
                player3Hand.remove(card);
                player3HandSize --;
            }
            if(currentPlayer == 3){
                player4Hand.remove(card);
                player4HandSize --;
            }
            return true;
        }
        else{
            return false;
        }
    }



}
