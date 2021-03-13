package com.Ofeitus.paint;

import com.Ofeitus.paint.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawShapes extends JComponent {
    private ArrayList<Shape> shapes;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (Shape shape : shapes) {
            shape.draw(g2);
        }
    }

    public DrawShapes() {
        shapes = new ArrayList<>();
    }

    public int shapesCount() {
        return shapes.size();
    }

    public Shape getShape(int index) {
        return shapes.get(index);
    }

    public Object lastShape() {
        return shapes.get(shapes.size() - 1);
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }
}