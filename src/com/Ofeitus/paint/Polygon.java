package com.Ofeitus.paint;

import java.awt.*;
import java.awt.geom.Line2D;

public class Polygon extends Shape {
    int n, r;
    double a, b, z;
    double angle;

    public Polygon(int x, int y, int x1, int y1, int n) {
        super(x, y, x1, y1);
        this.n = n;
        angle = 360.0 / n;
    }

    public void draw(Graphics2D g2) {
        this.r = (int) Math.pow(Math.pow(x1 - x, 2) + Math.pow(y1 - y, 2), 0.5);
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(stroke));
        z = (int)(Math.asin((double)(y1 - y) / r) / Math.PI * 180);
        if (x1 < x) {
            z = 180 - z;
        }
        int X, Y, X1, Y1;
        X = x1;
        Y = y1;
        for(int i = 0; i <= n; i++) {
            X1 = X;
            Y1 = Y;
            a = Math.cos(z / 180 * Math.PI);
            b = Math.sin(z / 180 * Math.PI);
            X = x + (int)(Math.round(a * r));
            Y = y + (int)(Math.round(b * r));
            g2.draw(new Line2D.Double(X, Y, X1, Y1));
            z = z + angle;
        }
    }
}
