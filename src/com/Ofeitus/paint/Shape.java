package com.Ofeitus.paint;

import java.awt.*;

public abstract class Shape {
    public Color fillColor;
    public Color strokeColor;
    public float stroke;
    int x, y, x1, y1;

    public Shape(int x, int y, int x1, int y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public abstract void draw(Graphics2D g2);
}
