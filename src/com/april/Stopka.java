package com.april;

import java.util.ArrayList;

public class Stopka {
    private ArrayList<Karta> kartaArrayList;

    public Stopka() {
        kartaArrayList = new ArrayList<>();
    }

    public Karta get(int kartNumber) {
        return kartaArrayList.get(kartNumber);
    }

    public void add(Karta karta) {
        kartaArrayList.add(karta);
    }

    public void remove(int kartNumber) {
        kartaArrayList.remove(kartNumber);
    }

    public int size() {
        return kartaArrayList.size();
    }

    public void clear() {
        kartaArrayList.clear();
    }
}
