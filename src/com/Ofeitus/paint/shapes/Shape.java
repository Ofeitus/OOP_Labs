package com.Ofeitus.paint.shapes;

import java.awt.*;

public abstract class Shape {
    public BasicStroke stroke;
    public Color fillColor;
    public Color strokeColor;

    public Shape() {

    }

    public abstract void draw(Graphics2D g2);
}
