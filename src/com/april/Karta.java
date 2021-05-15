package com.april;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Karta {
    private int x;
    private int y;
    public Image img;
    public Image rubashkaImg;
    private boolean rubashkaType;
    public byte mast; // 0 - крести, 1 - пики, 2 - черви, 3 - буби
    public int type; // 12 - туз, 11 - король, 10 - дама, 9 валет, 8 - десятка, 7 - девятка... 0 - двойка
    public boolean isSelected;
    public boolean isRed;

    public Karta() {}

    public Karta(String path, Image rubashkaImg, int cardNumber) {
        this.isSelected = false;
        this.rubashkaImg = rubashkaImg;
        try {
            this.img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Не могу прочесть изображение");
            System.exit(-1);
        }
        this.x = 30;
        this.y = 15;

        this.rubashkaType = true; // расположение рубашкой вверх
        this.mast = (byte) ((cardNumber - 1) % 4);
        this.type = (cardNumber - 1) / 4;

        this.isRed = this.mast > 1;
    }

    public void setRubashkaType(boolean rubashkaType) {
        this.rubashkaType = rubashkaType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void increaseOn(int x) {
        if (this.x + x >= 0 && this.x + x <= 1000) {
            this.x += x;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics gr) {
        if (!this.rubashkaType) {
            gr.drawImage(this.img, this.x, this.y, 72, 97, null);
        } else {
            gr.drawImage(this.rubashkaImg, this.x, this.y, 72, 97, null);
        }

        if (this.isSelected) {
            gr.setColor(Color.YELLOW);
            gr.drawRect(this.x, this.y, 72, 97);
        }
    }
}
