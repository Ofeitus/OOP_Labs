package com.Ofeitus.paint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawShapes extends JComponent {
    public static ArrayList<Shape> shapes = new ArrayList<>();

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (Shape shape : shapes) {
            shape.draw(g2);
        }
    }
}