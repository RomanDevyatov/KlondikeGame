package com.april;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Game {
    public Image rubashkaImage;
    private Stopka[] stopkas;
    private boolean firstVidacha;
    public boolean endGame;

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

        start();
    }

    public void mouseDragged(int mX, int mY) {

    }

    public void mousePressed(int mX, int mY) {

    }

    public void mouseReleased(int mX, int mY) {
        int nom = getNomKolodaPress(mX, mY);

        if (nom == 0) {
            vidacha();
        }
    }

    public void mouseDoublePressed(int mX, int mY) {

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
    }

    private void load() { // загружает изображения колоды
        for (int i = 0; i <= 52; i++) {
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
                    stopkas[i].get(j).draw(gr);
                }
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
