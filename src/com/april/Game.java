package com.april;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Game {
    public Image rubashkaImage;
    private Stopka[] stopkas;
    private boolean firstVidacha;
    public boolean endGame;

    private int nomStopki;
    private int nomKarti;
    private int dx, dy;
    private int oldX, oldY;
    private Timer tmEndGame;

    private static final String KARTA_IMAGES_FOLDER_PATH = "./koloda";
    private static final String PATH_TO_KARTA_K0 = "./koloda/k0.png";

    public Game() {
        try {
            rubashkaImage = ImageIO.read(new File(PATH_TO_KARTA_K0));
        } catch (Exception e) {
            System.out.println("Can't open PATH_TO_KARTA_K0:" + e.getMessage());
        }
        stopkas = new Stopka[13];

        for (int i = 0; i < 13; i++) {
            stopkas[i] = new Stopka();
        }
        // в конце игры эффект
        tmEndGame = new Timer(100, e -> {
            for(int i = 2; i <= 5; i++) {
                Karta karta = stopkas[i].get(0);
                stopkas[i].add(karta);
                stopkas[i].remove(0);
            }
        });

        start();
    }

    public void mouseDragged(int mX, int mY) {
        if (nomStopki >= 0) {
            Karta karta = stopkas[nomStopki].get(nomKarti);
            karta.setX(mX - dx);
            karta.setY(mY - dy);

            if (karta.getX() < 0) {
                karta.setX(0);
            }
            if (karta.getX() > 720) {
                karta.setX(720);
            }
            if (karta.getY() < 0) {
                karta.setY(0);
            }
            if (karta.getY() > 650) {
                karta.setY(650);
            }

            int y = 20;
            for (int i = nomKarti + 1; i < stopkas[nomStopki].size(); i++) {
                stopkas[nomStopki].get(i).setX(karta.getX());
                stopkas[nomStopki].get(i).setY(karta.getY() + y);
                y += 20;
            }
        }
    }

    public void mousePressed(int mX, int mY) {
        int number = getNomKolodaPress(mX, mY);
        setSelected(number, mX, mY);
    }

    public void mouseReleased(int mX, int mY) {
        int nom = getNomKolodaPress(mX, mY);

        if (nomStopki != -1) {
            stopkas[nomStopki].get(nomKarti).setSelected(false);
            if (nom == -1 || !testPerenos(nomStopki, nom)) {
                int y = 0;
                for (int i = nomKarti; i < stopkas[nomStopki].size(); i++) {
                    Karta karta = stopkas[nomStopki].get(i);
                    karta.setX(oldX);
                    karta.setY(oldY);
                    y += 20;
                }
            }
            nomStopki = -1;
            nomKarti = -1;
            openKarta();
        } else {
            if (nom == 0) {
                vidacha();
            }
        }


    }

    public void mouseDoublePressed(int mX, int mY) {
        int number = getNomKolodaPress(mX, mY);
        if (number == 1 || (number >= 6 && number <= 12)) {
            if (stopkas[number].size() > 0) {
                int lastCardNumber = stopkas[number].size() - 1;
                Karta lastCard = stopkas[number].get(lastCardNumber);
                if (mY >=lastCard.getY() && mY <= lastCard.getY() + 97) {
                    for (int i = 2; i < 5; i++) {
                        int res = -1;
                        if (stopkas[i].size() == 0) {
                            if (lastCard.getType() == 12) { // если туз
                                res = i;
                            }
                        } else {
                            int homeLastCardNumber = stopkas[i].size() - 1;
                            Karta homeLastCard = stopkas[i].get(homeLastCardNumber);
                            if (homeLastCard.getType() == 12
                                    && homeLastCard.getMast() == lastCard.getMast()
                                    && lastCard.getType() == 0) {
                                res = i;
                            } else if (homeLastCard.getType() >= 0
                                        && homeLastCard.getType() < 11
                                        && homeLastCard.getMast() == lastCard.getMast()) {
                                if (homeLastCard.getType() + 1 == lastCard.getType()) {
                                    res = i;
                                }
                            }
                        }

                        if (res >=0) {
                            lastCard.setX(110 * (res + 1) + 30);
                            lastCard.setY(15);
                            stopkas[res].add(lastCard);
                            stopkas[number].remove(lastCardNumber);
                            testEndGame();
                            break;
                        }
                    }
                }
            }
        }
        openKarta();
    }

    // number - стопка из которой переносят
    // numberAnother - стопка в которую переносят
    private boolean testPerenos(int number, int numberAnother) {
        boolean res = false;
        Karta karta = stopkas[number].get(nomKarti);
        Karta anotherKarta = null;
        if (stopkas[numberAnother].size() > 0) {
            anotherKarta = stopkas[numberAnother].get(stopkas[numberAnother].size() - 1);
        }
        if (numberAnother >= 2 && numberAnother <= 5) {
            if (nomKarti == (stopkas[number].size() - 1)) {
                if (anotherKarta == null) {
                    if (karta.getType() == 12) {
                        res = true;
                    }
                } else if (anotherKarta.getType() == 12
                                && karta.getMast() == anotherKarta.getMast()
                                && karta.getType() == 0) {
                    res = true;
                } else if (anotherKarta.getType() >= 0
                        && karta.getMast() == anotherKarta.getMast()
                        && anotherKarta.getType() < 11) {
                    if (karta.getType() == anotherKarta.getType() + 1) {
                        res = true;
                    }
                }

                if (res) {
                    karta.setX(110 * (numberAnother + 1) + 30);
                    karta.setY(15);
                    stopkas[numberAnother].add(karta);
                    stopkas[number].remove(nomKarti);
                    testEndGame();
                }
            }
        }

        if (numberAnother >= 6 && numberAnother <= 12) {
            int x = 30 + (numberAnother - 6) * 110;
            int y = 130;

            if (anotherKarta == null) {
                if (karta.getType() == 11) { // короля можно перенести в пустую стопку
                    res = true;
                }
            } else {
                if (!anotherKarta.isRubashkaType()) {
                    if (anotherKarta.getType() != 12) {
                        if (karta.getType() + 1 == anotherKarta.getType()
                                || (anotherKarta.getType() == 0 && karta.getType() == 12)) {
                            if (anotherKarta.isRed() != karta.isRed()) {
                                y = anotherKarta.getY() + 20;
                                res = true;
                            }
                        }
                    }
                }
            }

            if (res) {
                for (int i = nomKarti; i < stopkas[number].size(); i++) {
                    Karta karta_ = stopkas[number].get(i);
                    karta_.setX(x);
                    karta_.setY(y);
                    stopkas[numberAnother].add(karta_);
                    y += 20;
                }
                for (int i = stopkas[number].size() - 1; i >= nomKarti; i--) {
                    stopkas[number].remove(i);
                }
            }
        }
        return res;
    }

    private int getNomKolodaPress(int mX, int mY) {
        int nom = -1;

        if (mY >= 15 && mY <= 15 + 97) { // курсор в зоне верхних стопок
            if (mX >= 30 && mX <= 30 + 72) { nom = 0; }
            if (mX >= 140 && mX <= 140 + 72) { nom = 1; }
            if (mX >= 360 && mX <= 360 + 72) { nom = 2; }
            if (mX >= 470 && mX <= 470 + 72) { nom = 3; }
            if (mX >= 580 && mX <= 580 + 72) { nom = 4; }
            if (mX >= 690 && mX <= 690 + 72) { nom = 5; }
        } else if (mY >= 130 && mY <= 700) { // курсор в зоне нижних стопок
            if (mX >= 30 && mX <= 110 * 7) {
                if (((mX - 30) % 110) <= 72) {
                    nom = (mX - 30) / 110;
                    nom += 6;
                }
            }
        }

        return nom;
    }

    private void setSelected(int number, int mX, int mY) {
        if (number >= 1 && number <= 5) {
            if (stopkas[number].size() > 0) {
                int lastCardNumber = stopkas[number].size() - 1;
                Karta karta = stopkas[number].get(lastCardNumber);
                karta.setSelected(true);
                nomKarti = lastCardNumber;
                nomStopki = number;
                dx = mX - karta.getX();
                dy = mY - karta.getY();

                oldX = karta.getX();
                oldY = karta.getY();
            }
        } else if (number >= 6 && number <= 12) {
            if (stopkas[number].size() > 0) {
                int lastCardNumber = stopkas[number].size() - 1;
                Karta karta = stopkas[number].get(lastCardNumber);
                int selectedCardNumber = -1;
                if (mY >= karta.getY() && mY <= karta.getY() + 97) {
                    selectedCardNumber = lastCardNumber;
                } else if (mY < karta.getY()) {
                    selectedCardNumber = (mY - 130) / 20;
                    if (stopkas[number].get(selectedCardNumber).isRubashkaType()) {
                        selectedCardNumber = -1;
                    }
                }

                if (selectedCardNumber != -1) {
                    Karta selectedKarta = stopkas[number].get(selectedCardNumber);
                    if (!selectedKarta.isRubashkaType()) {
                        selectedKarta.setSelected(true);
                    }
                    nomKarti = selectedCardNumber;
                    nomStopki = number;
                    dx = mX - selectedKarta.getX();
                    dy = mY - selectedKarta.getY();

                    oldX = selectedKarta.getX();
                    oldY = selectedKarta.getY();
                }
            }
        }

    }

    private void testEndGame() { // домашние стопки
        if (stopkas[2].size()==13
            && stopkas[3].size()==13
            && stopkas[4].size()==13
            && stopkas[5].size()==13) {
            endGame = true;
            tmEndGame.start();
        }
    }

    private void openKarta() {
        for (int i = 6; i <= 12; i++) {
            if (stopkas[i].size() > 0) {
                int lastCardNumber = stopkas[i].size() - 1;
                Karta karta = stopkas[i].get(lastCardNumber);
                if (karta.isRubashkaType()) {
                    karta.setRubashkaType(false);
                }
            }
        }
    }

    private void vidacha() {
        if (stopkas[0].size() > 0) {
            int nom;

            if (firstVidacha) {
                nom = (int) (Math.random() * stopkas[0].size());
            } else {
                nom = stopkas[0].size() - 1;
            }
            Karta karta = stopkas[0].get(nom);
            karta.setRubashkaType(false);
            karta.increaseOn(110);
            stopkas[1].add(karta);
            stopkas[0].remove(nom);
        } else { // no cards
            int lastKartaNom = stopkas[1].size() - 1;
            for (int i = lastKartaNom; i >= 0; i--) {
                Karta karta = stopkas[1].get(i);
                karta.setRubashkaType(true);
                karta.increaseOn(-110);
                stopkas[0].add(karta);
            }
            stopkas[1].clear();
            firstVidacha = false;
        }
    }

    public void start() {
        for (int i = 0; i < 13; i++) {
            stopkas[i].clear();
        }
        load();
        razdacha();
        this.endGame = false;
        this.firstVidacha = true;
        this.nomStopki = -1;
        this.nomKarti = -1;
    }

    private void load() { // загружает изображения колоды
        for (int i = 1; i <= 52; i++) {
            stopkas[0].add(new Karta(KARTA_IMAGES_FOLDER_PATH + "/" + "k" + i + ".png", rubashkaImage, i));
        }

    }

    public void drawKoloda(Graphics gr) {
        // левая верхняя
        if (stopkas[0].size() > 0) {
            stopkas[0].get(stopkas[0].size() - 1).draw(gr);
        }
        // вторая левая верхняя стопка
        if (stopkas[1].size() > 1) {
            stopkas[1].get(stopkas[1].size() - 2).draw(gr);
            stopkas[1].get(stopkas[1].size() - 1).draw(gr);
        } else if (stopkas[1].size() == 1) {
            stopkas[1].get(stopkas[1].size() - 1).draw(gr);
        }

        // 4 стопки домашние
        for (int i = 2; i <= 5; i++) {
            if (stopkas[i].size() > 1) {
                stopkas[i].get(stopkas[i].size() - 2).draw(gr);
                stopkas[i].get(stopkas[i].size() - 1).draw(gr);
            } else if (stopkas[i].size() == 1) {
                stopkas[i].get(stopkas[i].size() - 1).draw(gr);
            }
        }

        // нижние семь стопок
        for (int i = 6; i < 13; i++) {
            if (stopkas[i].size() > 0) {
                for (int j = 0; j < stopkas[i].size(); j++) {
                    if (stopkas[i].get(j).isSelected()) {
                        break;
                    }
                    stopkas[i].get(j).draw(gr);
                }
            }
        }

        // переносим карты мышью
        if (nomStopki != -1) {
            for (int i = nomKarti; i < stopkas[nomStopki].size(); i++) {
                stopkas[nomStopki].get(i).draw(gr);
            }
        }


    }

    private void razdacha() {
        int x = 30;
        for (int i = 6; i < 13; i++) {
            for (int j = 6; j <= i; j++) {
                int randomNomKart = (int) (Math.random() * stopkas[0].size());
                Karta karta = stopkas[0].get(randomNomKart);
                if (j < i) {
                    karta.setRubashkaType(true);
                } else {
                    karta.setRubashkaType(false);
                }
                karta.setX(x);
                karta.setY(130 + stopkas[i].size() * 20);
                stopkas[i].add(karta);
                stopkas[0].remove(randomNomKart);
            }
            x += 110;
        }
    }
}
