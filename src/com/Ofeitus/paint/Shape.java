package com.Ofeitus.paint;

import java.awt.*;
import java.util.ArrayList;

public abstract class Shape {
    public Color fillColor;
    public Color strokeColor;
    public float stroke;
    public int x, y, x1, y1;
    public boolean finished;
    public ArrayList<Point> points;

    public Shape(int x, int y, int x1, int y1) {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }

    public void addPoint(int x, int y) {

    }

    public abstract void draw(Graphics2D g2);
}
