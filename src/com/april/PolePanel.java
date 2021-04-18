package com.april;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PolePanel extends JPanel {
    private Timer tmDraw;
    private JButton btn1, btn2;
    private Image fon;
    private Game game;

    public class MyMouse1 implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!game.endGame) {
                int mX = e.getX();
                int mY = e.getY();

                if (e.getButton() == 1 && e.getClickCount() == 1) {
                    game.mousePressed(mX, mY);
                } else if (e.getButton() == 1 && e.getClickCount() == 2) {
                    game.mouseDoublePressed(mX, mY);
                }

            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (!game.endGame) {
                int mX = e.getX();
                int mY = e.getY();

                if (e.getButton() == 1) {
                    game.mouseReleased(mX, mY);
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public class MyMouse2 implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!game.endGame) {
                int mX = e.getX();
                int mY = e.getY();

                game.mouseDragged(mX, mY);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public PolePanel() {
        addMouseListener(new MyMouse1());
        addMouseMotionListener(new MyMouse2());

        this.game = new Game();
        try {
            this.fon = ImageIO.read(new File("c:\\fon.jpg"));
        } catch (IOException e) {
            System.out.println("Картинка не читается");
        }

        this.btn1 = new JButton();
        this.btn1.setText("Новая игра");
        this.btn1.setForeground(Color.BLUE);
        this.btn1.setFont(new Font("serif", Font.PLAIN, 20));
        this.btn1.setBounds(820, 50, 150, 50);
        this.btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.start();
            }
        });

        this.btn2 = new JButton();
        this.btn2.setText("Выход");
        this.btn2.setForeground(Color.RED);
        this.btn2.setFont(new Font("serif", Font.PLAIN, 20));
        this.btn2.setBounds(820, 150, 150, 50);
        this.btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //now
        this.tmDraw = new Timer(20, (event) -> repaint());
        // was
//        this.tmDraw = new Timer(20, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                repaint();
//            }
//        });


        this.tmDraw.start();
    }

    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        gr.drawImage(this.fon, 0, 0, 1000, 700, null);
        gr.setColor(Color.WHITE);
        for (int i = 0; i < 7; i++) {
            gr.drawRect(30 + i * 110, 130, 72, 97);
        }

        for (int i = 0; i < 7; i++) {
            gr.drawRect(30 + i * 130, 130, 72, 97);
        }

        game.drawKoloda(gr);
    }
}
