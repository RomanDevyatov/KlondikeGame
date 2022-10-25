package com.april;

import com.april.parameters.CardParameters;
import com.april.parameters.GameParameters;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.Image;
import java.awt.Graphics;
import java.io.File;

public class Game {
    public Image shirtImage;
    private final Stack[] stacks;
    private boolean isFirstCardsIssue;
    public boolean isEndGame;

    private int stackNumber;
    private int cardNumber;
    private int dX, dY;
    private int oldX, oldY;
    private Timer timerEndGame;

    public Game() {
        try {
            shirtImage = ImageIO.read(new File(GameParameters.PATH_TO_CARD_K0));
        } catch (Exception e) {
            System.out.println("Couldn't open PATH_TO_CARD_K0:" + e.getMessage());
        }
        stacks = new Stack[GameParameters.STACK_NUMBER];

        for (int i = 0; i < GameParameters.STACK_NUMBER; i++) {
            stacks[i] = new Stack();
        }

        timerEndGame = new Timer(GameParameters.ENDGAME_TIMER_DELAY, e -> {
            for (int stackIndex = 2; stackIndex <= 5; stackIndex++) {
                Card card = stacks[stackIndex].get(0);
                stacks[stackIndex].add(card);
                stacks[stackIndex].remove(0);
            }
        });

        start();
    }

    public void mouseDragged(int xMouse, int yMouse) {
        if (stackNumber >= 0) {
            Card card = stacks[stackNumber].get(cardNumber);
            card.setX(xMouse - dX);
            card.setY(yMouse - dY);

            if (card.getX() < 0) {
                card.setX(0);
            }
            if (card.getX() > 720) {
                card.setX(720);
            }
            if (card.getY() < 0) {
                card.setY(0);
            }
            if (card.getY() > 650) {
                card.setY(650);
            }
            int newY = 20;
            for (int cardIndex = cardNumber + 1; cardIndex < stacks[stackNumber].size(); cardIndex++) {
                Card currentCard = stacks[stackNumber].get(cardIndex);
                currentCard.setX(card.getX());
                currentCard.setY(card.getY() + newY);
                newY += GameParameters.CARD_DISTANT;
            }
        }
    }

    public void mousePressed(int mX, int mY) {
        int number = getStackNumberPressed(mX, mY);
        setSelectedCardNumber(number, mX, mY);
    }

    public void mouseReleased(int mX, int mY) {
        int stackNumberPressed = getStackNumberPressed(mX, mY);

        if (stackNumber != -1) {
            stacks[stackNumber].get(cardNumber).setSelected(false);

            if (stackNumberPressed == -1 || !isMoved(stackNumber, stackNumberPressed)) {
                int newY = 0;
                for (int cardIndex = cardNumber; cardIndex < stacks[stackNumber].size(); cardIndex++) {
                    Card card = stacks[stackNumber].get(cardIndex);
                    card.setX(oldX);
                    card.setY(oldY + newY);
                    newY += GameParameters.CARD_DISTANT;
                }
            }
            stackNumber = -1;
            cardNumber = -1;
            openCard();
        } else {
            if (stackNumberPressed == 0) {
                issuance();
            }
        }
    }

    public void mouseDoublePressed(int xMouse, int yMouse) {
        int stackNumberPressed = getStackNumberPressed(xMouse, yMouse);
        if (stackNumberPressed == 1 || (stackNumberPressed >= 6 && stackNumberPressed < GameParameters.STACK_NUMBER)) {
            if (stacks[stackNumberPressed].size() > 0) {
                int lastCardNumber = stacks[stackNumberPressed].size() - 1;
                Card lastCard = stacks[stackNumberPressed].get(lastCardNumber);
                if (yMouse >=lastCard.getY() && yMouse <= lastCard.getY() + GameParameters.CARD_HEIGHT) {
                    for (int homeStackIndex = 2; homeStackIndex < 5; homeStackIndex++) {
                        int result = -1;
                        if (stacks[homeStackIndex].size() == 0) {
                            if (lastCard.getRank() == CardParameters.ACE_RANK) {
                                result = homeStackIndex;
                            }
                        } else {
                            int homeLastCardNumber = stacks[homeStackIndex].size() - 1;
                            Card homeLastCard = stacks[homeStackIndex].get(homeLastCardNumber);
                            if (homeLastCard.getRank() == CardParameters.ACE_RANK
                                    && homeLastCard.getSuit() == lastCard.getSuit()
                                    && lastCard.getRank() == 0) {
                                result = homeStackIndex;
                            } else if (homeLastCard.getRank() >= CardParameters.TWO_RANK
                                        && homeLastCard.getRank() < CardParameters.KING_RANK
                                        && homeLastCard.getSuit() == lastCard.getSuit()) {
                                if (homeLastCard.getRank() + 1 == lastCard.getRank()) {
                                    result = homeStackIndex;
                                }
                            }
                        }

                        if (result >= 0) {
                            lastCard.setX(GameParameters.X_SHIFT * (result + 1) + GameParameters.X_START);
                            lastCard.setY(GameParameters.Y_TOP_ROW_START);
                            stacks[result].add(lastCard);
                            stacks[stackNumberPressed].remove(lastCardNumber);
                            checkEndGame();
                            break;
                        }
                    }
                }
            }
        }
        openCard();
    }

    private boolean isMoved(int stackNumber, int stackNumberTarget) {
        boolean result = false;
        Card card = stacks[stackNumber].get(cardNumber);
        Card anotherCard = null;

        if (stacks[stackNumberTarget].size() > 0) {
            anotherCard = stacks[stackNumberTarget].get(stacks[stackNumberTarget].size() - 1);
        }

        if (stackNumberTarget >= 2 && stackNumberTarget <= 5) {
            if (cardNumber == (stacks[stackNumber].size() - 1)) {
                if (anotherCard == null) {
                    if (card.getRank() == CardParameters.ACE_RANK) {
                        result = true;
                    }
                } else if (anotherCard.getRank() == CardParameters.ACE_RANK
                                && card.getSuit() == anotherCard.getSuit()
                                && card.getRank() == 0) {
                    result = true;
                } else if (anotherCard.getRank() >= 0
                        && card.getSuit() == anotherCard.getSuit()
                        && anotherCard.getRank() < CardParameters.KING_RANK) {
                    if (card.getRank() == anotherCard.getRank() + 1) {
                        result = true;
                    }
                }

                if (result) {
                    card.setX(GameParameters.X_SHIFT * (stackNumberTarget + 1) + GameParameters.X_START);
                    card.setY(GameParameters.Y_TOP_ROW_START);
                    stacks[stackNumberTarget].add(card);
                    stacks[stackNumber].remove(cardNumber);
                    checkEndGame();
                }
            }
        }

        if (stackNumberTarget >= 6 && stackNumberTarget < GameParameters.STACK_NUMBER) {
            int x = GameParameters.X_START + (stackNumberTarget - 6) * GameParameters.X_SHIFT;
            int y = GameParameters.Y_BOTTOM_START;

            if (anotherCard == null) {
                if (card.getRank() == CardParameters.KING_RANK) {
                    result = true;
                }
            } else {
                if (!anotherCard.isShirt()) {
                    if (anotherCard.getRank() != CardParameters.ACE_RANK) {
                        if (card.getRank() + 1 == anotherCard.getRank()
                                || (anotherCard.getRank() == CardParameters.TWO_RANK && card.getRank() == CardParameters.ACE_RANK)) {
                            if (anotherCard.isRed() != card.isRed()) {
                                y = anotherCard.getY() + GameParameters.CARD_DISTANT;
                                result = true;
                            }
                        }
                    }
                }
            }

            if (result) {
                for (int i = cardNumber; i < stacks[stackNumber].size(); i++) {
                    card = stacks[stackNumber].get(i);
                    card.setX(x);
                    card.setY(y);
                    stacks[stackNumberTarget].add(card);
                    y += GameParameters.CARD_DISTANT;
                }
                for (int i = stacks[stackNumber].size() - 1; i >= cardNumber; i--) {
                    stacks[stackNumber].remove(i);
                }
            }
        }

        return result;
    }

    private int getStackNumberPressed(int xMouse, int yMouse) {
        int stackNumberPressed = -1;

        if (yMouse >= GameParameters.Y_TOP_ROW_START && yMouse <= GameParameters.Y_TOP_ROW_START + GameParameters.CARD_HEIGHT) {
            if (xMouse >= GameParameters.X_START && xMouse <= GameParameters.X_START + GameParameters.CARD_WIDTH) { stackNumberPressed = 0; }
            if (xMouse >= 140 && xMouse <= 140 + GameParameters.CARD_WIDTH) { stackNumberPressed = 1; }
            if (xMouse >= 360 && xMouse <= 360 + GameParameters.CARD_WIDTH) { stackNumberPressed = 2; }
            if (xMouse >= 470 && xMouse <= 470 + GameParameters.CARD_WIDTH) { stackNumberPressed = 3; }
            if (xMouse >= 580 && xMouse <= 580 + GameParameters.CARD_WIDTH) { stackNumberPressed = 4; }
            if (xMouse >= 690 && xMouse <= 690 + GameParameters.CARD_WIDTH) { stackNumberPressed = 5; }
        } else if (yMouse >= GameParameters.Y_BOTTOM_START && yMouse <= GameParameters.FRAME_HEIGHT) {
            if (xMouse >= GameParameters.X_START && xMouse <= GameParameters.X_SHIFT * 7) {
                if (((xMouse - GameParameters.X_START) % GameParameters.X_SHIFT) <= GameParameters.CARD_WIDTH) {
                    stackNumberPressed = (xMouse - GameParameters.X_START) / GameParameters.X_SHIFT;
                    stackNumberPressed += 6;
                }
            }
        }

        return stackNumberPressed;
    }

    private void setSelectedCardNumber(int stackNumber, int mX, int mY) {
        if (stackNumber >= 1 && stackNumber <= 5) {
            if (stacks[stackNumber].size() > 0) {
                int lastCardNumber = stacks[stackNumber].size() - 1;
                Card card = stacks[stackNumber].get(lastCardNumber);
                card.setSelected(true);
                cardNumber = lastCardNumber;
                this.stackNumber = stackNumber;
                dX = mX - card.getX();
                dY = mY - card.getY();

                oldX = card.getX();
                oldY = card.getY();
            }
        } else if (stackNumber >= 6 && stackNumber < GameParameters.STACK_NUMBER) {
            if (stacks[stackNumber].size() > 0) {
                int lastCardNumber = stacks[stackNumber].size() - 1;
                Card card = stacks[stackNumber].get(lastCardNumber);
                int selectedCardNumber = -1;
                if (mY >= card.getY() && mY <= card.getY() + GameParameters.CARD_HEIGHT) {
                    selectedCardNumber = lastCardNumber;
                } else if (mY < card.getY()) {
                    selectedCardNumber = (mY - GameParameters.Y_BOTTOM_START) / GameParameters.CARD_DISTANT;
                    if (stacks[stackNumber].get(selectedCardNumber).isShirt()) {
                        selectedCardNumber = -1;
                    }
                }

                if (selectedCardNumber != -1) {
                    Card selectedCard = stacks[stackNumber].get(selectedCardNumber);
                    if (!selectedCard.isShirt()) {
                        selectedCard.setSelected(true);
                    }
                    cardNumber = selectedCardNumber;
                    this.stackNumber = stackNumber;
                    dX = mX - selectedCard.getX();
                    dY = mY - selectedCard.getY();

                    oldX = selectedCard.getX();
                    oldY = selectedCard.getY();
                }
            }
        }

    }

    private void checkEndGame() {
        if (stacks[2].size() == GameParameters.STACK_NUMBER
            && stacks[3].size() == GameParameters.STACK_NUMBER
            && stacks[4].size() == GameParameters.STACK_NUMBER
            && stacks[5].size() == GameParameters.STACK_NUMBER) {
            isEndGame = true;
            timerEndGame.start();
        }
    }

    private void openCard() {
        for (int stackIndex = 6; stackIndex < GameParameters.STACK_NUMBER; stackIndex++) {
            if (stacks[stackIndex].size() > 0) {
                int lastCardNumber = stacks[stackIndex].size() - 1;
                Card card = stacks[stackIndex].get(lastCardNumber);
                if (card.isShirt()) {
                    card.setShirt(false);
                }
            }
        }
    }

    private void issuance() {
        if (stacks[0].size() > 0) {
            int cardNumber;

            if (isFirstCardsIssue) {
                cardNumber = (int) (Math.random() * stacks[0].size());
            } else {
                cardNumber = stacks[0].size() - 1;
            }
            Card card = stacks[0].get(cardNumber);
            card.setShirt(false);
            card.increaseOn(GameParameters.X_SHIFT);
            stacks[1].add(card);
            stacks[0].remove(cardNumber);
        } else { // no cards
            int lastCardNumber = stacks[1].size() - 1;
            for (int i = lastCardNumber; i >= 0; i--) {
                Card card = stacks[1].get(i);
                card.setShirt(true);
                card.increaseOn(-GameParameters.X_SHIFT);
                stacks[0].add(card);
            }
            stacks[1].clear();
            isFirstCardsIssue = false;
        }
    }

    public void start() {
        for (int stackIndex = 0; stackIndex < GameParameters.STACK_NUMBER; stackIndex++) {
            stacks[stackIndex].clear();
        }
        loadCardImagesToZeroStack();
        distribution();
        this.isEndGame = false;
        this.isFirstCardsIssue = true;
        this.stackNumber = -1;
        this.cardNumber = -1;
    }

    private void loadCardImagesToZeroStack() {
        for (int cardIndex = 1; cardIndex <= 52; cardIndex++) {
            stacks[0].add(new Card(GameParameters.PATH_TO_CARD_IMAGES_FOLDER + "/k" + cardIndex + ".png", shirtImage, cardIndex));
        }
    }

    public void drawStacks(Graphics gr) {
        if (stacks[0].size() > 0) {
            stacks[0].get(stacks[0].size() - 1).draw(gr);
        }

        if (stacks[1].size() > 1) {
            stacks[1].get(stacks[1].size() - 2).draw(gr);
            stacks[1].get(stacks[1].size() - 1).draw(gr);
        } else if (stacks[1].size() == 1) {
            stacks[1].get(stacks[1].size() - 1).draw(gr);
        }

        for (int i = 2; i <= 5; i++) {
            if (stacks[i].size() > 1) {
                stacks[i].get(stacks[i].size() - 2).draw(gr);
                stacks[i].get(stacks[i].size() - 1).draw(gr);
            } else if (stacks[i].size() == 1) {
                stacks[i].get(stacks[i].size() - 1).draw(gr);
            }
        }

        for (int stackIndex = 6; stackIndex < GameParameters.STACK_NUMBER; stackIndex++) {
            if (stacks[stackIndex].size() > 0) {
                for (int j = 0; j < stacks[stackIndex].size(); j++) {
                    if (stacks[stackIndex].get(j).isSelected()) {
                        break;
                    }
                    stacks[stackIndex].get(j).draw(gr);
                }
            }
        }

        if (stackNumber != -1) {
            for (int i = cardNumber; i < stacks[stackNumber].size(); i++) {
                stacks[stackNumber].get(i).draw(gr);
            }
        }


    }

    private void distribution() {
        int x = GameParameters.X_START;
        for (int stackIndex = 6; stackIndex < GameParameters.STACK_NUMBER; stackIndex++) {
            for (int j = 6; j <= stackIndex; j++) {
                int randomCardNumber = (int) (Math.random() * stacks[0].size());
                Card card = stacks[0].get(randomCardNumber);
                card.setShirt(j < stackIndex);
                card.setX(x);
                card.setY(GameParameters.Y_BOTTOM_START + stacks[stackIndex].size() * GameParameters.CARD_DISTANT);
                stacks[stackIndex].add(card);
                stacks[0].remove(randomCardNumber);
            }
            x += GameParameters.X_SHIFT;
        }
    }
}
