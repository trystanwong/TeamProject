package com.example.teamproject;

import java.util.ArrayList;
import java.util.Random;

public class GameState {


    //private Player[] players; //all players
    private int numPlayers;
    private String[] id; //id of each player (placeholder for subclass of Player)
    private String currentPlayer; //id of the player who's turn it is
    private String roundLeader; //id of the current round leader
    private int gamePhase; //what phase of the game we are in

    //constants for different phases of the game in gamePhase
    private static final int BEGIN_GAME = 0;
    private static final int ANTE = 1;
    private static final int ROUND = 2;
    private static final int CHOICE = 3;
    private static final int END_GAMBIT = 4;

    private int numCardsInDeck; //cards remaining in the deck
    private int numCardsOnBoard;

    private ArrayList<Card> boardCards; //all the cards that are visible to all players
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

    //all player hoards and current stakes for the round
    private int currentStakes;
    private int player1Hoard;
    private int player2Hoard;
    private int player3Hoard;
    private int player4Hoard;

    /**
     *  Constructor for the GameState at the beginning of the game
     * @param initDeck
     */
    public GameState(ArrayList<Card> initDeck){

        gamePhase = BEGIN_GAME;
        numPlayers = 4;
        id = new String[numPlayers];
        for(int i = 0; i<numPlayers; i++){
            id[i] = "000"+i;
        }

        //there is no current player or round leader in the beginning phase of the game
        currentPlayer = null;
        roundLeader = null;

        numCardsInDeck = 24; //70 playable cards in the deck

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
        for(int j = 0; j < 6; j++){
           player1Hand.add(randomCard());
           player2Hand.add(randomCard());
           player3Hand.add(randomCard());
           player4Hand.add(randomCard());
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

        sb.append("All Player ID's:\t");
        for(String e : id)
        {
            sb.append(e);
            sb.append("\t");
        }
        sb.append("\n");

        sb.append("\n");
        sb.append("Player 1 Hoard: " + player1Hoard + "\n");

        sb.append("Player 1 Hand: \n");
        for(int i = 0; i < player1HandSize; i++){
            sb.append((i+1)+".\t"+player1Hand.get(i).toString()+"\n");
        }
        sb.append("\n");

        sb.append("Player 2 Hoard: " + player2Hoard + "\n");

        sb.append("Player 2 Hand: \n");
        for(int i = 0; i < player2HandSize; i++){
            sb.append((i+1)+".\t"+player2Hand.get(i).toString()+"\n");
        }

        sb.append("\n");
        sb.append("Player 3 Hoard: " + player3Hoard + "\n");

        sb.append("Player 3 Hand: \n");
        for(int i = 0; i < player3HandSize; i++){
            sb.append((i+1)+".\t"+player3Hand.get(i).toString()+"\n");
        }

        sb.append("\n");
        sb.append("Player 4 Hoard: " + player4Hoard + "\n");

        sb.append("Player 4 Hand: \n");
        for(int i = 0; i < player4HandSize; i++){
            sb.append((i+1)+".\t"+player4Hand.get(i).toString()+"\n");
        }

        sb.append("\n");
        sb.append("Player 1 Flight: \n");
        for(int i = 0; i < flight1.size(); i++){
            sb.append((i+1)+".\t"+flight1.get(i).toString()+"\n");
        }

        sb.append("\n");
        sb.append("Current Stakes:" + "\t" + currentStakes + "\n");

        return sb.toString();
    }

    /**
     * returns a random card from the deck and removes it from the deck list
     * @return Card Object
     */
    public Card randomCard(){

        Random r = new Random();
        int random = r.nextInt(numCardsInDeck);
        Card newCard = deck.get(random);
        numCardsInDeck--;
        deck.remove(random);

        //newCard.setPlacement(hand);
        return newCard;

    }

}
