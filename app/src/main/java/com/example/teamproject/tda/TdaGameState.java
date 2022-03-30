package com.example.teamproject.tda;
import com.example.teamproject.game.infoMsg.GameState;

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
public class TdaGameState extends GameState{

    //Instance Variables

    //private Player[] players; //all players
    private int numPlayers;
    private int[] id; //id of each player (placeholder for subclass of Player)

    private String[] names; // names of all players

    private int round; //current round in gambit (resets to 1 after gambit ends)
    private int gambit; //number of gambits that have occurred
    private int currentPlayer; //id of the player who's turn it is
    private int roundLeader; //id of the current round leader
    private int gamePhase; //what phase of the game we are in

    private String gameText; //game text to tell the user what to do.

    //constants for different phases of the game in gamePhase
    public static final int BEGIN_GAME = 0;
    public static final int ANTE = 1;
    public static final int ROUND = 2;
    public static final int CHOICE = 3;
    public static final int END_GAMBIT = 4;
    public static final int FORFEIT = 5;

    //choices triggered from cards
    public String choice1;
    public String choice2;

    private int numCardsInDeck; //cards remaining in the deck
    private int numCardsOnBoard; //number cards that are visible to every player

    private ArrayList<Card> boardCards; //all the cards that have been removed from the deck
    private ArrayList<Card> deck; //current stack of deck

    private Card[] selectedCard; //card that's currently selected
    private int selectedCardIndex; //index of the currently selected card

    private boolean canPlay; //can the selected card be played;

    //all player flights
    private Card[][] flights;
    private int[] flightSizes;

    //all player hands
    private Card[][] hands;
    private int[] handSizes;

    //Ante Pile
    private Card[] antePile;

    //all player hoards and current stakes for the current gambit
    private int currentStakes;
    private int[] hoards;

    /**
     * Default Constructor for the GameState at the beginning of the game
     */
    public TdaGameState(){

        numPlayers = 4;

        //game starts off in an ante
        gamePhase = ANTE;
        round = 1;
        gambit = 0;
        canPlay = false;

        names = new String[4];

        choice1 = "";
        choice2 = "";

        gameText = "Choose an Ante Card";

        //setting up the id of each player (placeholder for player class)
        id = new int[numPlayers];
        for(int i = 0; i<numPlayers; i++){
            id[i] = i;
        }

        //there is no current player or round leader in the beginning phase of the game
        //its decided in the ante phase but it'll default to player1
        currentPlayer = 0;
        roundLeader = 0;

        //Initializing the deck of cards using the buildDeck function in the card class
        numCardsInDeck = 70;
        Card c = new Card();
        deck = new ArrayList<>();
        deck = c.buildDeck();

        //initializing all of the arrays found in the game state
        selectedCard = new Card[4];

        selectedCardIndex = 0;
        flights = new Card[4][3];
        flightSizes = new int[4];
        hands = new Card[4][10];
        handSizes = new int[4];
        hoards = new int[4];
        antePile = new Card[4];

        boardCards = new ArrayList<>();
        //setting all hands and flights to blank cards and setting all hoards to 50;
        for(int i = 0; i < 4; i++){
            hoards[i] = 50;
            antePile[i] = new Card();
            for(int j = 0; j<3;j++){
                flights[i][j]=new Card();
                flights[i][j].setPlacement(Card.FLIGHT);
                boardCards.add(new Card(flights[i][j]));
            }
            for(int k = 0; k<10;k++){
                hands[i][k]=new Card();
            }
            selectedCard[i] = new Card(hands[i][0]);
        }


        currentStakes = 0; //no stakes until a gambit begins
        numCardsOnBoard = 0; //there are no visible cards in the beginning of the game

        for(int i = 0; i<4; i++) {
            flightSizes[i] = 0;
            //adding 6 random cards to each players hand using the randomCard function
            //this is the starting hand of each player
            for (int j = 0; j < 6; j++) {
                Card newHand = randomCard();
                newHand.setPlacement(Card.HAND);
                hands[i][j] = newHand;
                handSizes[i]++;
            }

        }

        for(int i = 0; i < 6; i++){
            Card handCard = hands[0][i];
            boardCards.add(new Card(handCard));
        }





    }
    /**
     * Copy Constructor for the GameState
     */
    public TdaGameState(TdaGameState tdaGameStateCopy){

        this.gamePhase = tdaGameStateCopy.gamePhase;
        this.round = tdaGameStateCopy.round;
        this.gambit = tdaGameStateCopy.gambit;
        this.currentPlayer = tdaGameStateCopy.currentPlayer;
        this.roundLeader = tdaGameStateCopy.roundLeader;

        //copying the hoards
        this.hoards = tdaGameStateCopy.hoards;

        //copying the names
        names = new String[4];
        names = tdaGameStateCopy.names;

        choice1 = tdaGameStateCopy.choice1;
        choice2 = tdaGameStateCopy.choice2;



        this.gameText = tdaGameStateCopy.gameText;//copying the game text


        this.canPlay = tdaGameStateCopy.canPlay;

        //copying the cards in the deck
        Card c = new Card();
        deck = new ArrayList<>();
        this.numCardsInDeck = tdaGameStateCopy.numCardsInDeck;

        for(Card d : tdaGameStateCopy.deck){
            int index = tdaGameStateCopy.deck.indexOf(d);
            this.deck.add(new Card(d));
        }

        //copying the cards on the board

        this.numCardsOnBoard = tdaGameStateCopy.numCardsOnBoard;
        boardCards = new ArrayList<>();

        for(Card b : tdaGameStateCopy.boardCards){
            this.boardCards.add(new Card(b));
        }

        //all card arrays
        flights = new Card[4][3];
        hands = new Card[4][10];
        antePile = new Card[4];
        selectedCard = new Card[4];
        handSizes = tdaGameStateCopy.handSizes;
        flightSizes = tdaGameStateCopy.flightSizes;

        for(int i = 0; i < 4; i++){
            antePile[i] = new Card(tdaGameStateCopy.antePile[i]);
            selectedCard[i] = new Card(tdaGameStateCopy.selectedCard[i]);
            for(int j = 0; j < 10; j++){
                Card copy  = tdaGameStateCopy.hands[i][j];
                this.hands[i][j] = new Card(copy);
            }
            for(int k = 0; k < 3; k++){
                Card copy  = tdaGameStateCopy.flights[i][k];
                this.flights[i][k] = new Card(copy);
            }
        }

        this.currentStakes = tdaGameStateCopy.currentStakes;
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
        sb.append("Total Cards on Board: " + numCardsOnBoard +"\n");

        //printing the resources for each player
        for(int i = 0; i < 4; i++){
            sb.append("----------------------------------\n");
            sb.append("\t\t\t PLAYER "+i+":\n");
            sb.append("Hoard: " + hoards[i] + "\n");
            sb.append("Hand: \n");
            for(int j = 0; j < 10; j++){
                Card c = hands[i][j];
                if(c == null){
                    sb.append((j+1)+".\t");
                }
                else {
                    sb.append((j + 1) + ".\t" + hands[i][j].toString() + "\n");
                }
            }
            sb.append("Flight: \n");
            for(int k = 0; k < 3; k++){
                Card c = flights[i][k];
                if(c == null){
                    sb.append((k+1)+".\t");
                }
                sb.append((k+1)+".\t"+flights[i][k].toString()+"\n");
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

        //seed set to 65 to get the same set of "random" cards every time for testing
        Random r = new Random(65);
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
     * @param player - the player who played the card that triggered a choice
     * @return - true if it's a valid move / false if it's not
     */
    public boolean choiceAction(int player){
        if(gamePhase == CHOICE){

            return true;
        }
        return false;
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
            //gamePhase = ROUND; implemented later to be able to call other functions in test
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
    public boolean selectCard(int player){
        if(player == currentPlayer){
            return true;
        }
        return false;
    }

    /**
     * Play Card action using the play button (only visible when a card is selected)
     *
     * @param player - player making the action
     * @return - true if it's a valid move / false if it's not
     */
    public boolean playCard(int player,int index){

        // a player can only play a card if it's their turn
        if(currentPlayer == player) {
            return true;
        }
        return false;

    }

    //setter for the play button boolean
    public void setPlayButton(boolean b){
        canPlay = b;
    }

    //getter for the play button boolean
    public boolean getPlayButton(){
        return canPlay;
    }

    /**
     *
     * Getter for the current player instance variable
     *
     * @return current player
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(int num){
        currentPlayer = num;
    }

    public void setNames(String[] n){
        for(int i = 0; i<4; i++){
            names[i]=n[0];
        }
    }

    public String[] getNames(){
        return names;
    }

    public int getRoundLeader(){
        return roundLeader;
    }

    public int setRoundLeader(int leader){
       return
               roundLeader = leader;
    }



    /**
     *
     * Returns a Card Object for the tests of each function that require a card.
     *
     * @param player - player who has the card in their hand
     * @param index - where the card is in the hand
     * @return
     */
    public Card getCard(int player, int index){
        return hands[player][index];
    }

    /**
     * getter for the hoards
     * @return
     */
    public int[] getHoards(){
        return hoards;
    }

    /**
     * set the current phase of the game
     * @param phase
     */
    public void setGamePhase(int phase){
        gamePhase = phase;
    }

    /**
     * Get the current phase of the game
     * @return
     */
    public int getGamePhase(){
        return gamePhase;
    }

    /**
     * returns the hand of a size
     * @param index - which hand
     * @return size of hand
     */
    public int getHandSize(int index){

        return handSizes[index];
    }

    public void setHandSize(int index, int size){
        handSizes[index] = size;
    }

    /**
     * changing the value of a player's hoard
     * @param hoard
     * @param amount
     */
    public void setHoard(int hoard, int amount){
        hoards[hoard] = amount;
    }

    /**
     * returns a card in the human players hand
     * @param index
     * @return
     */
    public Card getHandCard(int id,int index){
        return hands[id][index];
    }

    public void setHand(int player, int index, Card c){
        hands[player][index] = c;
    }

    public Card getAnteCard(int index){
        return antePile[index];
    }

    public void setAnteCard(int index, Card c){
        antePile[index] = new Card(c);
    }

    /**
     * Returns the deck of cards
     * @return
     */
    public ArrayList<Card> getDeck(){
        return deck;
    }

    public void setStakes(int amount){
        currentStakes = amount;
    }

    public int getCurrentStakes(){
        return currentStakes;
    }

    /**
     * returns the hoard value of the given player index
     * @param index
     * @return
     */
    public int getHoard(int index){
        return hoards[index];
    }

    /**
     * returns the number of cards on the board
     * @return
     */
    public int getBoardSize() {
        return boardCards.size();
    }

    //getter for the game text
    public String getGameText(){
        return gameText;
    }

    public void setGameText(String s){
        gameText = s;
    }

    public void setSelectedCard(int index, Card c){
        selectedCard[index] = new Card(c);
    }

    public Card getSelectedCard(int index){
        return new Card(selectedCard[index]);
    }

    public int getSelectedCardIndex(){
        return selectedCardIndex;
    }
    public void setSelectedCardIndex(int index){
        selectedCardIndex = index;
    }

    public int getFlightSize(int player){
        return flightSizes[player];
    }

    public void setFlightSize(int player, int size){
        flightSizes[player]=size;
    }

    public Card getFlightCard(int flight, int index) {
        return flights[flight][index];
    }

    public void setFlight(int player, int index, Card c){
        flights[player][index] = new Card(c);
    }

    public String getChoice1(){
        return choice1;
    }
    public String getChoice2(){return choice2;}
    public void setChoice1(String s){
        choice1 = s;
    }
    public void setChoice2(String s){
        choice2 = s;
    }
}

