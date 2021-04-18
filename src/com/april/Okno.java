package com.april;

import javax.swing.*;
import java.awt.*;

public class Okno extends JFrame {

    public Okno() {
        PolePanel panel = new PolePanel();

        Container cont = getContentPane();
        cont.add(panel);

        setTitle("Игра \"Косынка\"");
        setBounds(0, 0, 1000,700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }
}
