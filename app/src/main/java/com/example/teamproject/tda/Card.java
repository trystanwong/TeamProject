package com.example.teamproject.tda;
import java.util.ArrayList;
/**
 * Card
 *
 * Representation of what a specific card is within the game
 *
 * @author Trystan Wong
 * @author Kawika Suzuki
 * @author Mohammad Surur
 * @author Marcus Rison
 */
public class Card {

    //instance variables

    private String name;
    private String power;//text found on card
    private int strength;//card strength (1-13)
    private int type;//type of card: (good dragon, evil dragon, or mortal)
    private int placement;//where the card is with respect to the "game"

    //constants for the different placements of each card
    //This will determine the visibility of each card from the perspective of each player
    private static final int DECK = 0;
    private static final int HAND = 1;
    private static final int FLIGHT = 2;
    private static final int ANTE = 3;
    private static final int DISCARD = 4;

    //constant variables for stating what this specific cards type is
    private static final int GOOD = 0;
    private static final int EVIL = 1;
    private static final int MORTAL = 2;

    public Card(){

    }
    /**
     * Card: constructor for initializing the card
     *
     * @param initName: Name of the card
     * @param initStrength: Strength of the card
     *
     */
    public Card(String initName, int initStrength, int initType) {

        name = initName;
        strength = initStrength;

        power = null;//implemented later
        type = initType;


        placement = DECK;//starts in deck

    }

    /**
     * Card: copy constructor for the card class
     *
     * @return none
     * @param cardCopy: copy of the original card class
     */
    public Card(Card cardCopy) {
        this.name = cardCopy.name;
        this.strength = cardCopy.strength;
        this.power = cardCopy.power;
        this.type = cardCopy.type;
        this.placement = cardCopy.placement;
    }
    /**
     * getType: A method for determining what type a card is
     *
     * @return String: a String for what the type of card is
     */
    public String getType() {
        //A string determining what the type of this certain card is
        String nameOfType = "";

        if (type == GOOD) {
            nameOfType = "GoodDragon";
        }
        else if (type == EVIL) {
            nameOfType = "EvilDragon";
        }
        else if (type == MORTAL) {
            nameOfType = "Mortal";
        }

        //return a String representation of what the name of the type is
        return nameOfType;
    }

    /**
     *
     * Getter for the location of each Card
     *
     * @return the placement of the card as a string
     */
    public String getPlacement(Card card) {
        //A string defining where the card is currently within the game

        String nameOfPlacement = "";

        if (placement == DECK) {
            nameOfPlacement = "Currently in deck";
        }
        else if (placement == HAND) {
            nameOfPlacement = "Currently in hand";
        }
        else if (placement == FLIGHT) {
            nameOfPlacement = "Currently in flight";
        }
        else if (placement == ANTE) {
            nameOfPlacement = "Currently in Ante";
        }
        else if(placement == DISCARD) {
            nameOfPlacement = "Currently in discard pile";
        }

        //return what the String representation of what the placement is
        return nameOfPlacement;
    }

    /**
     * toString: method for instantiating the variables and turning them into a String
     *
     * @return String: Description of the variables
     */
    @Override
    public String toString(){
        return name + " Strength: " +strength;
    }

    /**
     * Building the deck of all possible cards given information from the official rule book
     *
     * @return  ArrayList<Card> - deck of cards
     */
    public ArrayList<Card> buildDeck(){
        ArrayList deck = new ArrayList();

        //All Mortals
        deck.add(new Card("The Fool",3,MORTAL));
        deck.add(new Card("The Princess",4,MORTAL));
        deck.add(new Card("The Priest",5,MORTAL));
        deck.add(new Card("The Druid",6,MORTAL));
        deck.add(new Card("The Thief",7,MORTAL));
        deck.add(new Card("The DragonSlayer",8,MORTAL));
        deck.add(new Card("The Archmage",9,MORTAL));

        //All Red Dragons
        deck.add(new Card("Red Dragon", 2, EVIL));
        deck.add(new Card("Red Dragon", 3, EVIL));
        deck.add(new Card("Red Dragon", 5, EVIL));
        deck.add(new Card("Red Dragon", 8, EVIL));
        deck.add(new Card("Red Dragon", 10, EVIL));
        deck.add(new Card("Red Dragon", 12, EVIL));

        //BLACK DRAGONS
        deck.add(new Card("Black Dragon", 1, EVIL));
        deck.add(new Card("Black Dragon", 2, EVIL));
        deck.add(new Card("Black Dragon", 3, EVIL));
        deck.add(new Card("Black Dragon", 5, EVIL));
        deck.add(new Card("Black Dragon", 7, EVIL));
        deck.add(new Card("Black Dragon", 9, EVIL));

        //BLUE DRAGONS
        deck.add(new Card("Blue Dragon", 1, EVIL));
        deck.add(new Card("Blue Dragon", 2, EVIL));
        deck.add(new Card("Blue Dragon", 4, EVIL));
        deck.add(new Card("Blue Dragon", 7, EVIL));
        deck.add(new Card("Blue Dragon", 9, EVIL));
        deck.add(new Card("Blue Dragon", 11, EVIL));

        //BRASS DRAGONS
        deck.add(new Card("Brass Dragon", 1, EVIL));
        deck.add(new Card("Brass Dragon", 2, EVIL));
        deck.add(new Card("Brass Dragon", 4, EVIL));
        deck.add(new Card("Brass Dragon", 5, EVIL));
        deck.add(new Card("Brass Dragon", 7, EVIL));
        deck.add(new Card("Brass Dragon", 9, EVIL));

        //Gold dragons
        deck.add(new Card("Gold Dragon", 2, GOOD));
        deck.add(new Card("Gold Dragon", 4, GOOD));
        deck.add(new Card("Gold Dragon", 6, GOOD));
        deck.add(new Card("Gold Dragon", 9, GOOD));
        deck.add(new Card("Gold Dragon", 11, GOOD));
        deck.add(new Card("Gold Dragon", 13, GOOD));

        //Copper dragon
        deck.add(new Card("Copper Dragon", 1, GOOD));
        deck.add(new Card("Copper Dragon", 3, GOOD));
        deck.add(new Card("Copper Dragon", 5, GOOD));
        deck.add(new Card("Copper Dragon", 7, GOOD));
        deck.add(new Card("Copper Dragon", 8, GOOD));
        deck.add(new Card("Copper Dragon", 10, GOOD));

        //White Dragon
        deck.add(new Card("White Dragon", 1, EVIL));
        deck.add(new Card("White Dragon", 2, EVIL));
        deck.add(new Card("White Dragon", 3, EVIL));
        deck.add(new Card("White Dragon", 4, EVIL));
        deck.add(new Card("White Dragon", 6, EVIL));
        deck.add(new Card("White Dragon", 8, EVIL));

        //Silver Dragons
        deck.add(new Card("Silver Dragon", 2, GOOD));
        deck.add(new Card("Silver Dragon", 3, GOOD));
        deck.add(new Card("Silver Dragon", 6, GOOD));
        deck.add(new Card("Silver Dragon", 8, GOOD));
        deck.add(new Card("Silver Dragon", 10, GOOD));
        deck.add(new Card("Silver Dragon", 12, GOOD));

        //Bronze Dragons
        deck.add(new Card("Bronze Dragon", 1, GOOD));
        deck.add(new Card("Bronze Dragon", 3, GOOD));
        deck.add(new Card("Bronze Dragon", 6, GOOD));
        deck.add(new Card("Bronze Dragon", 7, GOOD));
        deck.add(new Card("Bronze Dragon", 9, GOOD));
        deck.add(new Card("Bronze Dragon", 11, GOOD));

        //Green Dragons
        deck.add(new Card("Green Dragon", 1, EVIL));
        deck.add(new Card("Green Dragon", 2, EVIL));
        deck.add(new Card("Green Dragon", 4, EVIL));
        deck.add(new Card("Green Dragon", 6, EVIL));
        deck.add(new Card("Green Dragon", 8, EVIL));
        deck.add(new Card("Green Dragon", 10, EVIL));

        //God Dragons
        deck.add(new Card("Tiamat", 13, EVIL));
        deck.add(new Card("Dracolich", 10, EVIL));
        deck.add(new Card("Bahamut", 13, GOOD));

        return deck;

    }
}