package com.april;

import com.april.parameters.GameParameters;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class Card {
    private int x;
    private int y;
    public Image image;
    public Image shirtImage;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getShirtImage() {
        return shirtImage;
    }

    public void setShirtImage(Image shirtImage) {
        this.shirtImage = shirtImage;
    }

    public boolean isShirt() {
        return isShirt;
    }

    public byte getSuit() {
        return suit;
    }

    public void setSuit(byte suit) {
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setRed(boolean red) {
        isRed = red;
    }

    private boolean isShirt;
    public byte suit; // 0 - clubs, 1 - spades, 2 - hearts, 3 - diamonds
    public int rank;
    public boolean isSelected;
    public boolean isRed;

    public Card() {}

    public Card(String path, Image shirtImage, int cardNumber) {
        this.isSelected = false;
        this.shirtImage = shirtImage;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println("Couldn't read an image");
            System.exit(-1);
        }
        this.x = GameParameters.X_START;
        this.y = GameParameters.Y_TOP_ROW_START;

        this.isShirt = true;
        this.suit = (byte) ((cardNumber - 1) % 4);
        this.rank = (cardNumber - 1) / 4;

        this.isRed = this.suit > 1;
    }

    public void setShirt(boolean isShirt) {
        this.isShirt = isShirt;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void increaseOn(int x) {
        if (this.x + x >= 0 && this.x + x <= GameParameters.FRAME_WIDTH) {
            this.x += x;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics graphics) {
        if (!this.isShirt) {
            graphics.drawImage(this.image, this.x, this.y, GameParameters.CARD_WIDTH, GameParameters.CARD_HEIGHT, null);
        } else {
            graphics.drawImage(this.shirtImage, this.x, this.y, GameParameters.CARD_WIDTH, GameParameters.CARD_HEIGHT, null);
        }

        if (this.isSelected) {
            graphics.setColor(Color.YELLOW);
            graphics.drawRect(this.x, this.y, GameParameters.CARD_WIDTH, GameParameters.CARD_HEIGHT);
        }
    }
}
