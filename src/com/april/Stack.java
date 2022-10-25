package com.april;

import java.util.ArrayList;

public class Stack {
    private ArrayList<Card> cardArrayList;

    public Stack() {
        cardArrayList = new ArrayList<>();
    }

    public Card get(int cardNumber) {
        return cardArrayList.get(cardNumber);
    }

    public void add(Card card) {
        cardArrayList.add(card);
    }

    public void remove(int cardNumber) {
        cardArrayList.remove(cardNumber);
    }

    public int size() {
        return cardArrayList.size();
    }

    public void clear() {
        cardArrayList.clear();
    }
}
