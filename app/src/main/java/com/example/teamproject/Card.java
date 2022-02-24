package com.example.teamproject;

/**
 * Card
 *
 * representation of what a specific card is within the game
 */
public class Card {
    //instance variables

    //Represents what the certain cards name is
    private String name;
    //Represents what the strength of the certain card is
    private int Strength;
    //Represents what the power of the certain card is
    private String power;
    //Represents what type this specific card is being either a good dragon, evil dragon, or mortal
    private int type;
    //Represents what mode the card is in
    private int placement;

    //Representation of constant variables for stating where the cards placement is within the current game
    private static final int inDeck = 0;
    private static final int inHand = 1;
    private static final int inFlight = 2;
    private static final int inAnte = 3;
    private static final int inDiscard = 4;
    //Representation of constant variables for stating what this specific cards type is
    private static final int goodDragon = 0;
    private static final int evilDragon = 1;
    private static final int mortal = 2;

    /**
     * Card: constructor for initializing the card
     *
     * @param initName: Name of the card
     * @param initStrength: Strength of the card
     * @param initPower: the description of power of this card
     * @param initType: type of card (evil dragon, good dragon, or mortal)
     * @param initPlacement: where the card is currently (deck, hand, flight, ante, or discard pile)
     */
    public Card(String initName, int initStrength, String initPower, int initType, int initPlacement) {
        this.name = initName;
        this.Strength = initStrength;
        this.power = initPower;
        this.type = initType;
        this.placement = initPlacement;
    }

    /**
     * getType: A method for determining what type a card is
     *
     * @return String: a String for what the type of card is
     */
    public String getType() {
        //A string determining what the type of this certain card is
        String nameOfType = "";

        if (type == this.goodDragon) {
            nameOfType = "GoodDragon";
        }
        else if (type == this.evilDragon) {
            nameOfType = "EvilDragon";
        }
        else if (type == this.mortal) {
            nameOfType = "Mortal";
        }

        //return a String representation of what the name of the type is
        return nameOfType;
    }

    /**
     * getPlacement: A method for determining where the card is placed in the moment of the
     * game
     *
     * @return String: A string for saying where the card is placed in the game
     */
    public String getPlacement() {
        //A string defining where the card is currently within the game
        String nameOfPlacement = "";

        if (placement == inDeck) {
            nameOfPlacement = "Currently in deck";
        }
        else if (placement == inHand) {
            nameOfPlacement = "Currently in hand";
        }
        else if (placement == inFlight) {
            nameOfPlacement = "Currently in flight";
        }
        else if (placement == inAnte) {
            nameOfPlacement = "Currently in Ante";
        }
        else if(placement == inDiscard) {
            nameOfPlacement = "Currently in discard pile";
        }

        //return what the String representation of what the placement is
        return nameOfPlacement;
    }

    /**
     * Card: copy constructor for the card class
     *
     * @return none
     * @param cardCopy: copy of the original card class
     */
    public Card(Card cardCopy) {
        this.name = cardCopy.name;
        this.Strength = cardCopy.Strength;
        this.power = cardCopy.power;
        this.type = cardCopy.type;
        this.placement = cardCopy.placement;
    }

    /**
     * toString: method for instantiating the variables and turning them into a String
     *
     * @return String: Description of the variables
     */
    @Override
    public String toString() {
        return this.name + "is a " + this.getType() + " with a strength of " + this.Strength +
                ", and its power is " + this.power + ". This card is " + this.getPlacement();
    }
}

