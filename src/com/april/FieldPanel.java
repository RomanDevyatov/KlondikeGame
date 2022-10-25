package com.april;

import com.april.parameters.GameParameters;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JButton;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;


public class FieldPanel extends JPanel {
    private Image backgroundImage;
    private Game game;

    public class MouseListenerImpl implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            if (!game.isEndGame) {
                int xMouse = e.getX();
                int yMouse = e.getY();

                if (e.getButton() == GameParameters.LEFT_BUTTON_CLICKED && e.getClickCount() == 1) {
                    game.mousePressed(xMouse, yMouse);
                } else if (e.getButton() == GameParameters.LEFT_BUTTON_CLICKED && e.getClickCount() == 2) {
                    game.mouseDoublePressed(xMouse, yMouse);
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!game.isEndGame) {
                int xMouse = e.getX();
                int yMouse = e.getY();

                if (e.getButton() == GameParameters.LEFT_BUTTON_CLICKED) {
                    game.mouseReleased(xMouse, yMouse);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

    }

    public class MouseMotionListenerImpl implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!game.isEndGame) {
                int xMouse = e.getX();
                int yMouse = e.getY();

                game.mouseDragged(xMouse, yMouse);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {}

    }

    public FieldPanel() {
        addMouseListener(new MouseListenerImpl());
        addMouseMotionListener(new MouseMotionListenerImpl());

        this.game = new Game();
        try {
            this.backgroundImage = ImageIO.read(new File(GameParameters.PATH_TO_BACKGROUND_IMAGE));
        } catch (IOException e) {
            System.out.println("Couldn't read background image");
        }
        setLayout(null);
        JButton newGameButton = new JButton();
        newGameButton.setText("New Game");
        newGameButton.setForeground(Color.BLUE);
        newGameButton.setFont(new Font("serif", Font.PLAIN, 20));
        newGameButton.setBounds(820, 50, 150, 50);
        newGameButton.addActionListener(e -> game.startGame());
        add(newGameButton);

        JButton endGameButton = new JButton();
        endGameButton.setText("Exit");
        endGameButton.setForeground(Color.RED);
        endGameButton.setFont(new Font("serif", Font.PLAIN, 20));
        endGameButton.setBounds(820, 150, 150, 50);
        endGameButton.addActionListener(e -> System.exit(0));
        add(endGameButton);

        Timer drawTimer = new Timer(GameParameters.DRAW_TIMER_DELAY, (event) -> repaint());
        drawTimer.start();
    }

    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);

        gr.drawImage(this.backgroundImage, 0, 0, GameParameters.FRAME_WIDTH, GameParameters.FRAME_HEIGHT, null);

        gr.setColor(Color.WHITE);
        for (int i = 0; i < GameParameters.CARD_COLUMN_NUMBER; i++) {
            if (i != 2) {
                gr.drawRect(GameParameters.X_START + i * GameParameters.X_SHIFT, GameParameters.Y_TOP_ROW_START, GameParameters.CARD_WIDTH, GameParameters.CARD_HEIGHT);
            }
        }

        for (int i = 0; i < GameParameters.CARD_COLUMN_NUMBER; i++) {
            gr.drawRect(GameParameters.X_START + i * GameParameters.X_SHIFT, GameParameters.Y_BOTTOM_START, GameParameters.CARD_WIDTH, GameParameters.CARD_HEIGHT);
        }

        game.drawStacks(gr);
    }
}
