package com.Ofeitus.paint.shapes;

import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.min;

public class Ellipse extends PrimitiveShape {

    public Ellipse(int x, int y, int x1, int y1) {
        super(x, y, x1, y1);
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.setColor(fillColor);
        g2.fillOval(min(x, x1), min(y, y1), abs(x - x1), abs(y - y1));
        g2.setColor(strokeColor);
        g2.drawOval(min(x, x1), min(y, y1), abs(x - x1), abs(y - y1));
    }
}
