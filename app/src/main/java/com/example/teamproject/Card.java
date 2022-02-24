package com.example.teamproject;

public class Card {
    private String type;
    private int strength;

    public Card(String initType, int initStrength)
    {
    type = initType;
    strength = initStrength;
    }

    public Card(Card copy){

    }

    @Override
    public String toString(){
        return type+" Power: "+strength;
    }
}
