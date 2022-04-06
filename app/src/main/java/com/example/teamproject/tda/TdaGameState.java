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
    private int[] id; //id of each player (placeholder for subclass of Player)
    private String[] names; // names of all players
    private int round; //current round in gambit (resets to 1 after gambit ends)
    private int gambit; //number of gambits that have occurred
    private int currentPlayer; //id of the player who's turn it is
    private int roundLeader; //id of the current round leader
    private int gamePhase; //what phase of the game we are in
    private int numCardsInDeck; //cards remaining in the deck
    private ArrayList<Card> deck; //current stack of deck
    private ArrayList<Card> discardPile; //current stack of deck
    private String gameText; //game text to tell the user what to do.
    private boolean canPlay; //can the selected card be played;

    //constants for different phases of the game in gamePhase
    public static final int BEGIN_GAME = 0;
    public static final int ANTE = 1;
    public static final int ROUND = 2;
    public static final int CHOICE = 3;
    public static final int END_GAMBIT = 4;
    public static final int FORFEIT = 5;
    public static final int END_ANTE = 6;

    //choices triggered from cards
    private String choice1;
    private String choice2;
    private String[][] choices;

    private Card[] selectedCard; //card that's currently selected
    private int selectedCardIndex[]; //index of the currently selected card

    //all player flights
    private ArrayList<Card>[] flights;

    //all player hands
    private ArrayList<Card>[] hands;

    //Ante Pile
    private ArrayList<Card> antePile;

    //all player hoards and current stakes for the current gambit
    private int currentStakes;
    private int[] hoards;

    /**
     * Default Constructor for the GameState at the beginning of the game
     */
    public TdaGameState(){

        //Game starts in an ante
        gameText = "Choose an Ante Card";
        gamePhase = ANTE;//game starts off in an ante

        round = 1;
        gambit = 0;
        canPlay = false;
        currentStakes = 0; //no stakes until a gambit begins
        names = new String[4];

        //initializing the choices array;
        choice1 = "";
        choice2 = "";
        choices = new String[4][2];
        for(int i = 0; i<4; i++){
            for(int j = 0; j<2; j++){
                choices[i][j] = "";
            }
        }

        //ALL POSSIBLE CARDS WITH CHOICES IN THE GAME:

        //blue dragon
        choices[0][0] = "Steal 1 Gold from the stakes for each evil dragon in your flight";
        choices[0][1] = "Opponent(s) pay that much gold to the stakes.";

        //green dragon
        choices[1][0] = "Give Weakest Dragon to Opponent";
        choices[1][1] = "Pay 5 gold to Opponent";

        //brass dragon
        choices[2][0] = "Give Strongest Good Dragon to Opponent";
        choices[2][1] = "Give 5 Gold to Opponent";

        //end of gambit
        choices[3][0] = "OK";
        choices[3][1] = "";

        //there is no current player or round leader in the beginning phase of the game
        //its decided in the ante phase but it'll default to player1
        currentPlayer = 0;
        roundLeader = 0;

        //Initializing the deck of cards using the buildDeck function in the card class
        numCardsInDeck = 70;
        Card c = new Card();
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        deck = c.buildDeck();

        //initializing all of the arrays found in the game state
        selectedCard = new Card[4];

        //selected index defaulted to the first card
        selectedCardIndex = new int[2];

        //initializing all card arrays
        flights = new ArrayList[4];
        hands = new ArrayList[4];
        hoards = new int[4];
        antePile = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            hoards[i] = 50;
            hands[i] = new ArrayList<>();
            flights[i] = new ArrayList<>();
        }

        //initializing each players starting hand with 6 random cards
        for(int i = 0; i<4; i++) {
            for (int j = 0; j < 6; j++) {
                Card newHand = randomCard();
                newHand.setPlacement(Card.HAND);
                hands[i].add(newHand);
            }
            //each players "selected" card is defaulted to the first card in their hand (for A.I)
            selectedCard[i] = hands[i].get(0);
        }
    }

    /**
     * Copy Constructor for the GameState
     */
    public TdaGameState(TdaGameState tdaGameStateCopy){

        //copying instance variables that don't require a deep copy
        this.gamePhase = tdaGameStateCopy.gamePhase;
        this.round = tdaGameStateCopy.round;
        this.gambit = tdaGameStateCopy.gambit;
        this.currentPlayer = tdaGameStateCopy.currentPlayer;
        this.roundLeader = tdaGameStateCopy.roundLeader;
        this.canPlay = tdaGameStateCopy.canPlay;
        this.hoards = tdaGameStateCopy.hoards;
        this.names = new String[4];
        this.names = tdaGameStateCopy.names;
        this.choices = new String[4][2];
        this.choices = tdaGameStateCopy.choices;
        this.choice1 = tdaGameStateCopy.choice1;
        this.choice2 = tdaGameStateCopy.choice2;
        this.gameText = tdaGameStateCopy.gameText;
        this.currentStakes = tdaGameStateCopy.currentStakes;

        //copying the cards in the deck
        deck = new ArrayList<>();
        for(Card d : tdaGameStateCopy.deck){
            this.deck.add(new Card(d));
        }

        //copying the discard pile
        discardPile = new ArrayList<>();
        for(Card d : tdaGameStateCopy.discardPile){
            this.discardPile.add(new Card(d));
        }

        //all card arrays
        this.flights = new ArrayList[4];
        this.hands = new ArrayList[4];
        this.antePile = new ArrayList<>();
        this.selectedCard = new Card[4];

        //copying all ante pile cards
        for(Card c : tdaGameStateCopy.antePile){
            antePile.add(new Card(c));
        }

        //copying all ante, hand, and flight cards
        for(int i = 0; i < 4; i++){

            selectedCard[i] = new Card(tdaGameStateCopy.selectedCard[i]);
            hands[i] = new ArrayList<>();
            flights[i] = new ArrayList<>();

            //copying all hand cards
            for(int j = 0; j < tdaGameStateCopy.getHand(i).size(); j++){
                Card copy  = tdaGameStateCopy.hands[i].get(j);
                this.hands[i].add(new Card(copy));
            }
            //copying all flights
            for(int k = 0; k < tdaGameStateCopy.getFlight(i).size(); k++){
                Card copy  = tdaGameStateCopy.flights[i].get(k);
                this.flights[i].add(new Card(copy));
            }
        }
    }

    /**
     *
     * toString method builds a String containing all of the information needed in the current
     * state of the game (all instance variables).
     *
     * @return String that will be printed
     *
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
            sb.append("\t\t\t PLAYER "+i+":\n");
            sb.append("Hoard: " + hoards[i] + "\n");
            sb.append("Hand: \n");
            for(int j = 0; j < hands[i].size(); j++){
                Card c = hands[i].get(j);
                if(c == null){
                    sb.append((j+1)+".\t");
                }
                else {
                    sb.append((j + 1) + ".\t" + hands[i].get(j).toString() + "\n");
                }
            }
            sb.append("Flight: \n");
            for(int k = 0; k < flights[i].size(); k++){
                Card c = flights[i].get(k);
                if(c == null){
                    sb.append((k+1)+".\t");
                }
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

        //seed set to 150000 to get the same set of "random" cards every time for testing
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
     * @param player - the player who played the card that triggered a choice
     * @return - true if it's a valid move / false if it's not
     */
    public boolean choiceAction(int player){
        if(gamePhase == CHOICE){
            return true;
        }
        if(gamePhase == END_ANTE){
            return true;
        }
        if(gamePhase == END_GAMBIT){
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

    /**
     * Draws a card from the deck and adds it to the given player's hand
     * @param player - player drawing a card
     */
    public void drawCard(int player) {

        //if the deck is going to be empty after drawing a card
        Card newCard = randomCard();
        newCard.setPlacement(Card.HAND);
        hands[player].add(new Card(newCard));
        if(deck.size()==0){
            //shuffle the deck if the deck is now empty
            shuffleDeck();
        }
    }

    /**
     * clear all player flights and the ante pile
     */
    public void clearBoard(){

        //clearing flights
        for(int i = 0; i<2;i++) {
            for(int j = getFlight(i).size()-1; j>=0;j--){
                Card c = new Card(getFlight(i).get(j));
                discardPile.add(c);
                getFlight(i).remove(j);

            }
        }
        //clearing the ante pile
        for(int i = 1; i>=0; i--){
            Card c = new Card(getAntePile().get(i));
            discardPile.add(c);
            getAntePile().remove(i);
        }
    }

    //the deck takes all cards from the discard pile
    public void shuffleDeck(){
        for(Card c : discardPile){
            c.setPlacement(c.DECK);
            deck.add(new Card(c));
            discardPile.remove(c);
        }
    }

    /**
     * Decides the leader of a round based on what is played in the ante
     *
     * @return the number of the player who won the ante
     */
    public int isRoundLeader() {

        //defaulted to first ante card
        int highestStrength = antePile.get(0).getStrength();
        int strongestAntePlayer = 0;

        //checks for the strongest ante card
        for (int i = 0; i < 2; i++) {
            if (antePile.get(i).getStrength() > highestStrength) {
                strongestAntePlayer = i;
            }
        }
        return strongestAntePlayer;
    }

    //setter for the play button boolean
    public void setPlayButton(boolean b){
        canPlay = b;
    }

    //getter for if the card can be played
    public boolean getPlayButton(){ return canPlay; }

    //getter for the current player
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    //setter for the current player
    public void setCurrentPlayer(int num){
        currentPlayer = num;
    }

    //setter for the array of names
    public void setNames(int index, String n){
        names[index] = n;
    }

    //getter for the array of names
    public String[] getNames(){
        return names;
    }

    //getter for Round leader
    public int getRoundLeader(){
        return roundLeader;
    }

    //setter for Round leader
    public int setRoundLeader(int leader){ return roundLeader = leader; }

    //sets the game phase of the game
    public void setGamePhase(int phase){
        gamePhase = phase;
    }

    //getter for the current phase of the game
    public int getGamePhase(){
        return gamePhase;
    }

    //sets the given players hoard to the given amount
    public void setHoard(int hoard, int amount){
        hoards[hoard] = amount;
    }

    //returns the card at the given index of a hand
    public Card getHandCard(int id,int index){
        return hands[id].get(index);
    }

    //returns all hands
    public ArrayList<Card>[] getHands() {
        return hands;
    }

    //returns specific hands
    public ArrayList<Card> getHand(int index) {
        return hands[index];
    }

    //gets a card in the ante pile
    public Card getAnteCard(int index){
        return antePile.get(index);
    }

    public ArrayList<Card> getAntePile() {
        return antePile;
    }

    //adds a given card to the ante pile
    public void setAnteCard(int index, Card c){
        antePile.set(index,new Card(c));
    }

    //returns the deck of cards
    public ArrayList<Card> getDeck(){
        return deck;
    }

    //returns the discard pile
    public ArrayList<Card> getDiscardPile(){
        return discardPile;
    }

    //sets the stakes of the game
    public void setStakes(int amount){
        currentStakes = amount;
    }

    //returns the stakes of teh game
    public int getCurrentStakes(){
        return currentStakes;
    }

    //returns the hoard of a given player
    public int getHoard(int index){
        return hoards[index];
    }

    //getter for the game text
    public String getGameText(){
        return gameText;
    }

    //set the game text
    public void setGameText(String s){
        gameText = s;
    }

    //set the currently selected card
    public void setSelectedCard(int index, Card c){
        selectedCard[index] = new Card(c);
    }

    //return the currently selected card on the GUI
    public Card getSelectedCard(int index){
        return new Card(selectedCard[index]);
    }

    //return the index of the currently selected card
    public int getSelectedCardIndex(int player){ return selectedCardIndex[player]; }

    //set the index of the currently selected card
    public void setSelectedCardIndex(int player, int index){ selectedCardIndex[player] = index; }

    //get a card in a given flight at a given index
    public Card getFlightCard(int flight, int index) {
        return flights[flight].get(index);
    }

    //returns the given player's flight
    public ArrayList<Card> getFlight(int player){
        return flights[player];
    }

    //returns all flights
    public ArrayList<Card>[] getFlights(){
        return flights;
    }

    //getters for a set of choices
    public String[][] getChoices(){
        return choices;
    }
    public String getChoice(int index, int num){
        return choices[index][num];
    }


    //returns the choices given to players
    public String getChoice1(){
        return choice1;
    }
    public String getChoice2(){return choice2;}

    //sets the choices given to players
    public void setChoice1(String s){
        choice1 = s;
    }
    public void setChoice2(String s){
        choice2 = s;
    }

    //set the currentRound
    public void setRound(int r){
        round = r;
    }

    //get the Round
    public int getRound(){
        return round;
    }
}

