package com.Ofeitus.paint.shapes;

import java.awt.*;

public class Line extends PrimitiveShape {

    public Line(int x, int y, int x1, int y1) {
        super(x, y, x1, y1);
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(stroke);
        g2.setColor(strokeColor);
        g2.drawLine(x, y, x1, y1);
    }
}
