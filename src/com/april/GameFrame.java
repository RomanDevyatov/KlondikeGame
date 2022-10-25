package com.april;

import com.april.parameters.GameParameters;

import javax.swing.JFrame;
import java.awt.Container;

public class GameFrame extends JFrame {

    public GameFrame() {
        FieldPanel panel = new FieldPanel();

        Container container = getContentPane();
        container.add(panel);

        setTitle("Game \"Klondike\"");
        setBounds(0, 0, GameParameters.FRAME_WIDTH, GameParameters.FRAME_HEIGHT);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }
}
